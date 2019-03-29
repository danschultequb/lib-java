package qub;

public class InMemoryByteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryByteStream.class, () ->
        {
            AsyncDisposableTests.test(runner, (Test test) -> new InMemoryByteStream(test.getMainAsyncRunner()));

            runner.test("constructor()", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                test.assertFalse(stream.isDisposed());
                test.assertEqual(new byte[0], stream.getBytes());
                test.assertNull(stream.getMaxBytesPerRead());
                test.assertNull(stream.getMaxBytesPerWrite());
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                stream.close();
                test.assertTrue(stream.isDisposed());
                test.assertEqual(new byte[0], stream.getBytes());
                test.assertFalse(stream.hasCurrent());
                stream.close();
                test.assertTrue(stream.isDisposed());
                test.assertEqual(new byte[0], stream.getBytes());
                test.assertFalse(stream.hasCurrent());
            });

            runner.testGroup("setMaxBytesPerRead(Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertSame(stream, stream.setMaxBytesPerRead(null));
                    test.assertNull(stream.getMaxBytesPerRead());
                });

                runner.test("with -1", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.setMaxBytesPerRead(-1),
                        new PreConditionFailure("maxBytesPerRead (-1) == null || maxBytesPerRead (-1) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerRead());
                });

                runner.test("with 0", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.setMaxBytesPerRead(0),
                        new PreConditionFailure("maxBytesPerRead (0) == null || maxBytesPerRead (0) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerRead());
                });

                runner.test("with 1", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertSame(stream, stream.setMaxBytesPerRead(1));
                    test.assertEqual(1, stream.getMaxBytesPerRead());
                });

                runner.test("with 2", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertSame(stream, stream.setMaxBytesPerRead(2));
                    test.assertEqual(2, stream.getMaxBytesPerRead());
                });
            });

            runner.testGroup("setMaxBytesPerWrite(Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertSame(stream, stream.setMaxBytesPerWrite(null));
                    test.assertNull(stream.getMaxBytesPerWrite());
                });

                runner.test("with -1", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.setMaxBytesPerWrite(-1),
                        new PreConditionFailure("maxBytesPerWrite (-1) == null || maxBytesPerWrite (-1) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerWrite());
                });

                runner.test("with 0", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.setMaxBytesPerWrite(0),
                        new PreConditionFailure("maxBytesPerWrite (0) == null || maxBytesPerWrite (0) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerWrite());
                });

                runner.test("with 1", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertSame(stream, stream.setMaxBytesPerWrite(1));
                    test.assertEqual(1, stream.getMaxBytesPerWrite());
                });

                runner.test("with 2", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertSame(stream, stream.setMaxBytesPerWrite(2));
                    test.assertEqual(2, stream.getMaxBytesPerWrite());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);

                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10 }, test);

                    test.assertSuccess((byte)10, stream.readByte());
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10, 20 }, test);

                    test.assertSuccess((byte)10, stream.readByte());
                    test.assertSuccess((byte)20, stream.readByte());
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream readStream = create(test);
                    readStream.dispose().await();

                    test.assertThrows(readStream::readByte, new PreConditionFailure("isDisposed() cannot be true."));
                });
            });

            runner.testGroup("readByteAsync()", () ->
            {
                runner.test("with no AsyncRunner assigned", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream((AsyncRunner)null);

                    test.assertThrows(stream::readByteAsync, new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);

                    test.assertThrows(() -> stream.readByteAsync().awaitReturn().awaitError(), new EndOfStreamException());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10 }, test);

                    test.assertSuccess((byte)10, stream.readByteAsync().awaitReturn());
                    test.assertError(new EndOfStreamException(), stream.readByteAsync().awaitReturn());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10, 20 }, test);

                    test.assertSuccess((byte)10, stream.readByteAsync().awaitReturn());
                    test.assertSuccess((byte)20, stream.readByteAsync().awaitReturn());
                    test.assertError(new EndOfStreamException(), stream.readByteAsync().awaitReturn());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream readStream = create(test);
                    readStream.dispose().await();

                    test.assertThrows(readStream::readByteAsync, new PreConditionFailure("isDisposed() cannot be true."));
                });
            });
            
            runner.test("readBytes(int)", (Test test) ->
            {
                final InMemoryByteStream readStream1 = create(test);

                test.assertThrows(() -> readStream1.readBytes(-5), new PreConditionFailure("bytesToRead (-5) must be greater than or equal to 1."));
                test.assertThrows(() -> readStream1.readBytes(0), new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                test.assertThrows(() -> readStream1.readBytes(1).awaitError(), new EndOfStreamException());
                test.assertThrows(() -> readStream1.readBytes(5).awaitError(), new EndOfStreamException());

                final InMemoryByteStream readStream2 = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertThrows(() -> readStream2.readBytes(-5), new PreConditionFailure("bytesToRead (-5) must be greater than or equal to 1."));
                test.assertThrows(() -> readStream2.readBytes(0), new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                test.assertEqual(new byte[] { 0 }, readStream2.readBytes(1).await());
                test.assertEqual(new byte[] { 1, 2 }, readStream2.readBytes(2).await());
                test.assertEqual(new byte[] { 3 }, readStream2.readBytes(3).await());
                test.assertThrows(() -> readStream2.readBytes(1).awaitError(), new EndOfStreamException());
                test.assertThrows(() -> readStream2.readBytes(5).awaitError(), new EndOfStreamException());

                test.assertTrue(readStream2.dispose().await());

                test.assertThrows(() -> readStream2.readBytes(1), new PreConditionFailure("isDisposed() cannot be true."));
            });

            runner.test("readBytesAsync(int)", (Test test) ->
            {
                final InMemoryByteStream readStream1 = create(test);

                test.assertThrows(() -> readStream1.readBytesAsync(-5), new PreConditionFailure("bytesToRead (-5) must be greater than or equal to 1."));
                test.assertThrows(() -> readStream1.readBytesAsync(0), new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                test.assertErrorAsync(new EndOfStreamException(), readStream1.readBytesAsync(1));
                test.assertErrorAsync(new EndOfStreamException(), readStream1.readBytesAsync(5));

                final InMemoryByteStream readStream2 = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertThrows(() -> readStream2.readBytesAsync(-5), new PreConditionFailure("bytesToRead (-5) must be greater than or equal to 1."));
                test.assertThrows(() -> readStream2.readBytesAsync(0), new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                test.assertSuccessAsync(new byte[] { 0 }, readStream2.readBytesAsync(1));
                test.assertSuccessAsync(new byte[] { 1, 2 }, readStream2.readBytesAsync(2));
                test.assertSuccessAsync(new byte[] { 3 }, readStream2.readBytesAsync(3));
                test.assertErrorAsync(new EndOfStreamException(), readStream2.readBytesAsync(1));
                test.assertErrorAsync(new EndOfStreamException(), readStream2.readBytesAsync(5));

                test.assertSuccess(true, readStream2.dispose());

                test.assertThrows(() -> readStream2.readBytesAsync(1), new PreConditionFailure("isDisposed() cannot be true."));
            });

            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose().await();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes), new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[10], outputBytes);
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytes(outputBytes), new PreConditionFailure("outputBytes cannot be null."));
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytes(outputBytes), new PreConditionFailure("outputBytes cannot be empty."));
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new EndOfStreamException(), stream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual((byte)4, stream.getCurrent());

                    test.assertError(new EndOfStreamException(), stream.readByte());
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual((byte)10, stream.getCurrent());

                    test.assertThrows(() -> stream.readByte().awaitError(), new EndOfStreamException());
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual((byte)10, stream.getCurrent());

                    test.assertEqual((byte)11, stream.readByte().await());
                    test.assertEqual((byte)11, stream.getCurrent());
                });

                runner.test("with fewer bytes to read than limiter", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4 }, test)
                        .setMaxBytesPerRead(20);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(5, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(4, stream.getCurrent());

                    test.assertThrows(() -> stream.readByte().awaitError(), new EndOfStreamException());
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with equal bytes to read to limiter", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4 }, test)
                        .setMaxBytesPerRead(5);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(5, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(4, stream.getCurrent());

                    test.assertThrows(() -> stream.readByte().awaitError(), new EndOfStreamException());
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with more bytes to read than limiter", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4 }, test)
                        .setMaxBytesPerRead(3);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(3, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(2, stream.getCurrent());
                });
            });

            runner.testGroup("readBytesAsync(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes), new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes), new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes), new PreConditionFailure("outputBytes cannot be empty."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new EndOfStreamException(), stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, stream.readByte());
                });
            });

            runner.testGroup("readBytes(byte[],int,int)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("outputBytes cannot be empty."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, -2, 3),
                        new PreConditionFailure("startIndex (-2) must be between 0 and 9."));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, outputBytes.length, 3),
                        new PreConditionFailure("startIndex (10) must be between 0 and 9."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 9."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 0),
                        new PreConditionFailure("length (0) must be between 1 and 9."));
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 10),
                        new PreConditionFailure("length (10) must be between 1 and 9."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3).awaitError(),
                        new EndOfStreamException());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(4, stream.readBytes(outputBytes, 0, 10).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertThrows(() -> stream.readByte().awaitError(), new EndOfStreamException());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(10, stream.readBytes(outputBytes, 0, 10).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertThrows(() -> stream.readByte().awaitError(), new EndOfStreamException());
                });

                runner.test("with more bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(10, stream.readBytes(outputBytes, 0, 10).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual((byte)11, stream.readByte().await());
                });
            });

            runner.testGroup("readBytesAsync(byte[],int,int)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 3), new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 3), new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 3), new PreConditionFailure("outputBytes cannot be empty."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, -2, 3), new PreConditionFailure("startIndex (-2) must be between 0 and 9."));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, outputBytes.length, 3), new PreConditionFailure("startIndex (10) must be between 0 and 9."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 9."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 0),
                        new PreConditionFailure("length (0) must be between 1 and 9."));
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 10),
                        new PreConditionFailure("length (10) must be between 1 and 9."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new EndOfStreamException(), stream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with more bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, stream.readByte());
                });
            });

            runner.testGroup("readAllBytes()", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(stream::readAllBytes, new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertThrows(() -> stream.readAllBytes().awaitError(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("readBytesUntil(byte)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertTrue(stream.dispose().await());
                    test.assertThrows(() -> stream.readBytesUntil((byte)5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertError(new EndOfStreamException(), stream.readBytesUntil((byte)5));
                });

                runner.test("with bytes but not the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntil((byte)5));
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with bytes including the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntil((byte)5));
                    test.assertEqual((byte)5, stream.getCurrent());
                });
            });

            runner.testGroup("readBytesUntilAsync(byte)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(() -> stream.readBytesUntilAsync((byte)5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertError(new EndOfStreamException(), stream.readBytesUntilAsync((byte)5).awaitReturn());
                });

                runner.test("with bytes but not the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntilAsync((byte)5).awaitReturn());
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with bytes including the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntilAsync((byte)5).awaitReturn());
                    test.assertEqual((byte)5, stream.getCurrent());
                });
            });

            runner.testGroup("readBytesUntil(Iterable<Byte>)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(() -> stream.readBytesUntil(Array.createByte(5)), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertError(new EndOfStreamException(), stream.readBytesUntil(Array.createByte(5)));
                });

                runner.test("with bytes but not the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntil(Array.createByte(5)));
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with bytes including the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntil(Array.createByte(5)));
                    test.assertEqual((byte)5, stream.getCurrent());
                });
            });

            runner.testGroup("readBytesUntilAsync(Iterable<Byte>)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(() -> stream.readBytesUntilAsync(Array.createByte(5)), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertError(new EndOfStreamException(), stream.readBytesUntilAsync(Array.createByte(5)).awaitReturn());
                });

                runner.test("with bytes but not the stopBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntilAsync(Array.createByte(6, 6)).awaitReturn());
                    test.assertThrows(stream::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with bytes including the stopBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntilAsync(Array.createByte(3, 4, 5)).awaitReturn());
                    test.assertEqual((byte)5, stream.getCurrent());
                });
            });

            runner.testGroup("writeByte(byte)", () ->
            {
                runner.test("with 1 byte", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    stream.writeByte((byte)17);
                    test.assertEqual(new byte[] { 17 }, stream.getBytes());
                });

                final int byteCount = 500000;
                runner.speedTest("with " + byteCount + " bytes", Duration.milliseconds(100), (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream writeStream = new BufferedByteWriteStream(innerStream);

                    for (int i = 0; i < byteCount; ++i)
                    {
                        writeStream.writeByte((byte)42).await();
                    }
                    test.assertTrue(writeStream.dispose().await());
                    test.assertEqual(byteCount, innerStream.getCount());
                });
            });

            runner.test("writeBytes(byte[])", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                test.assertThrows(() -> stream.writeBytes(new byte[0]), new PreConditionFailure("toWrite cannot be empty."));
                test.assertEqual(new byte[0], stream.getBytes());

                stream.writeBytes(new byte[] { 1, 2, 3, 4 }).await();
                test.assertEqual(new byte[] { 1, 2, 3, 4 }, stream.getBytes());
            });

            runner.testGroup("writeBytes(byte[],int,int)", () ->
            {
                runner.test("with null bytes", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(null, 0, 0),
                        new PreConditionFailure("bytes cannot be null."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, -1 startIndex, and -1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], -1, -1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, -1 startIndex, and 0 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], -1, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, -1 startIndex, and 1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], -1, 1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 0 startIndex, and -1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], 0, -1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 0 startIndex, and 1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], 0, 1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 1 startIndex, and -1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], 1, -1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 1 startIndex, and 0 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], 1, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 1 startIndex, and 1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], 1, 1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 0 startIndex, and 0 length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeBytes(new byte[0], 0, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, and valid length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertEqual(2, stream.writeBytes(new byte[] { 0, 1, 2, 3, 4 }, 1, 2).await());
                    test.assertEqual(new byte[] { 1, 2 }, stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, valid length, and maxBytesPerWrite less than length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(3);
                    test.assertEqual(3, stream.writeBytes(new byte[] { 0, 1, 2, 3, 4 }, 1, 4).await());
                    test.assertEqual(new byte[] { 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, valid length, and maxBytesPerWrite equal to length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(3);
                    test.assertEqual(3, stream.writeBytes(new byte[] { 0, 1, 2, 3, 4 }, 1, 3).await());
                    test.assertEqual(new byte[] { 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, valid length, and maxBytesPerWrite greater than length", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(3);
                    test.assertEqual(2, stream.writeBytes(new byte[] { 0, 1, 2, 3, 4 }, 1, 2).await());
                    test.assertEqual(new byte[] { 1, 2 }, stream.getBytes());
                });
            });

            runner.testGroup("writeAllBytes(ByteReadStream)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeAllBytes((ByteReadStream)null), new PreConditionFailure("byteReadStream cannot be null."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream();
                    readStream.dispose();
                    test.assertThrows(() -> stream.writeAllBytes(readStream), new PreConditionFailure("byteReadStream.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    stream.dispose();
                    final InMemoryByteStream readStream = new InMemoryByteStream().endOfStream();
                    test.assertThrows(() -> stream.writeAllBytes(readStream), new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream().endOfStream();
                    test.assertEqual(0L, stream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with non-empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    test.assertEqual(4, stream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with read limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(5)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(2);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter less than write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(4);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter equal to write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(3);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter greater than write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(2);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });
            });

            runner.testGroup("writeAllBytes(ByteReadStream,int)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    test.assertThrows(() -> stream.writeAllBytes((ByteReadStream)null, 1), new PreConditionFailure("byteReadStream cannot be null."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream();
                    readStream.dispose();
                    test.assertThrows(() -> stream.writeAllBytes(readStream, 1), new PreConditionFailure("byteReadStream.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    stream.dispose();
                    final InMemoryByteStream readStream = new InMemoryByteStream().endOfStream();
                    test.assertThrows(() -> stream.writeAllBytes(readStream, 1), new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with negative initialBufferCapacity", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream().endOfStream();
                    test.assertThrows(() -> stream.writeAllBytes(readStream, -1), new PreConditionFailure("initialBufferCapacity (-1) must be greater than or equal to 1."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with zero initialBufferCapacity", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream().endOfStream();
                    test.assertThrows(() -> stream.writeAllBytes(readStream, 0), new PreConditionFailure("initialBufferCapacity (0) must be greater than or equal to 1."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream().endOfStream();
                    test.assertEqual(0L, stream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with non-empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    test.assertEqual(4, stream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with read limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream();
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(5)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(2);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter less than write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(4);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter equal to write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(3);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter greater than write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = new InMemoryByteStream()
                        .setMaxBytesPerWrite(2);
                    final InMemoryByteStream readStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAllBytes(readStream, 1).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });
            });

            runner.test("asCharacterWriteStream()", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final CharacterWriteStream characterWriteStream = stream.asCharacterWriteStream();
                test.assertNotNull(characterWriteStream);
            });

            runner.test("asCharacterWriteStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final CharacterWriteStream characterWriteStream = stream.asCharacterWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(characterWriteStream);
            });
        });
    }

    private static InMemoryByteStream create(Test test)
    {
        return create(null, test);
    }

    private static InMemoryByteStream create(byte[] contents, Test test)
    {
        return new InMemoryByteStream(contents, test.getMainAsyncRunner()).endOfStream();
    }
}
