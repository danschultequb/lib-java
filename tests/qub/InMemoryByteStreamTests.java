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
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                stream.close();
                test.assertTrue(stream.isDisposed());
                test.assertNull(stream.getBytes());
                stream.close();
                test.assertTrue(stream.isDisposed());
                test.assertNull(stream.getBytes());
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
                    readStream.dispose();

                    test.assertThrows(readStream::readByte);
                });
            });

            runner.testGroup("readByteAsync()", () ->
            {
                runner.test("with no AsyncRunner assigned", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream((AsyncRunner)null);

                    test.assertThrows(stream::readByteAsync);
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);

                    test.assertError(new EndOfStreamException(), stream.readByteAsync().awaitReturn());
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
                    readStream.dispose();

                    test.assertThrows(readStream::readByteAsync);
                });
            });
            
            runner.test("readBytes(int)", (Test test) ->
            {
                final InMemoryByteStream readStream1 = create(test);

                test.assertThrows(() -> readStream1.readBytes(-5));
                test.assertThrows(() -> readStream1.readBytes(0));
                test.assertError(new EndOfStreamException(), readStream1.readBytes(1));
                test.assertError(new EndOfStreamException(), readStream1.readBytes(5));

                final InMemoryByteStream readStream2 = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertThrows(() -> readStream2.readBytes(-5));
                test.assertThrows(() -> readStream2.readBytes(0));
                test.assertSuccess(new byte[] { 0 }, readStream2.readBytes(1));
                test.assertSuccess(new byte[] { 1, 2 }, readStream2.readBytes(2));
                test.assertSuccess(new byte[] { 3 }, readStream2.readBytes(3));
                test.assertError(new EndOfStreamException(), readStream2.readBytes(1));
                test.assertError(new EndOfStreamException(), readStream2.readBytes(5));

                test.assertSuccess(true, readStream2.dispose());

                test.assertThrows(() -> readStream2.readBytes(1));
            });

            runner.test("readBytesAsync(int)", (Test test) ->
            {
                final InMemoryByteStream readStream1 = create(test);

                test.assertThrows(() -> readStream1.readBytesAsync(-5));
                test.assertThrows(() -> readStream1.readBytesAsync(0));
                test.assertErrorAsync(new EndOfStreamException(), readStream1.readBytesAsync(1));
                test.assertErrorAsync(new EndOfStreamException(), readStream1.readBytesAsync(5));

                final InMemoryByteStream readStream2 = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertThrows(() -> readStream2.readBytesAsync(-5));
                test.assertThrows(() -> readStream2.readBytesAsync(0));
                test.assertSuccessAsync(new byte[] { 0 }, readStream2.readBytesAsync(1));
                test.assertSuccessAsync(new byte[] { 1, 2 }, readStream2.readBytesAsync(2));
                test.assertSuccessAsync(new byte[] { 3 }, readStream2.readBytesAsync(3));
                test.assertErrorAsync(new EndOfStreamException(), readStream2.readBytesAsync(1));
                test.assertErrorAsync(new EndOfStreamException(), readStream2.readBytesAsync(5));

                test.assertSuccess(true, readStream2.dispose());

                test.assertThrows(() -> readStream2.readBytesAsync(1));
            });

            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                    test.assertEqual(null, stream.getCurrent());
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytes(outputBytes));
                    test.assertEqual(null, stream.getCurrent());
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytes(outputBytes));
                    test.assertEqual(null, stream.getCurrent());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new EndOfStreamException(), stream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                    test.assertEqual(null, stream.getCurrent());
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual((byte)4, stream.getCurrent());

                    test.assertError(new EndOfStreamException(), stream.readByte());
                    test.assertEqual(null, stream.getCurrent());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual((byte)10, stream.getCurrent());

                    test.assertError(new EndOfStreamException(), stream.readByte());
                    test.assertEqual(null, stream.getCurrent());
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual((byte)10, stream.getCurrent());

                    test.assertSuccess((byte)11, stream.readByte());
                    test.assertEqual((byte)11, stream.getCurrent());
                });
            });

            runner.testGroup("readBytesAsync(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes));
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
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, -2, 3));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, outputBytes.length, 3));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, -1));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 0));
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 10));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new EndOfStreamException(), stream.readBytes(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertError(new EndOfStreamException(), stream.readByte());
                });

                runner.test("with more bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, stream.readByte());
                });
            });

            runner.testGroup("readBytesAsync(byte[],int,int)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 3));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 3));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, -2, 3));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, outputBytes.length, 3));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, -1));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 0));
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytesAsync(outputBytes, 1, 10));
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
                    test.assertThrows(stream::readAllBytes);
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertSuccess(new byte[0], stream.readAllBytes());
                });
            });

            runner.testGroup("readAllBytesAsync()", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(() -> stream.readAllBytesAsync());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertSuccess(new byte[0], stream.readAllBytesAsync().awaitReturn());
                });
            });

            runner.testGroup("readBytesUntil(byte)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(() -> stream.readBytesUntil((byte)5));
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
                    test.assertNull(stream.getCurrent());
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
                    test.assertThrows(() -> stream.readBytesUntilAsync((byte)5));
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
                    test.assertNull(stream.getCurrent());
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
                    test.assertThrows(() -> stream.readBytesUntil(Array.fromValues(new byte[] { 5 })));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertError(new EndOfStreamException(), stream.readBytesUntil(Array.fromValues(new byte[] { 5 })));
                });

                runner.test("with bytes but not the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntil(Array.fromValues(new byte[] { 5 })));
                    test.assertNull(stream.getCurrent());
                });

                runner.test("with bytes including the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntil(Array.fromValues(new byte[] { 5 })));
                    test.assertEqual((byte)5, stream.getCurrent());
                });
            });

            runner.testGroup("readBytesUntilAsync(Iterable<Byte>)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(() -> stream.readBytesUntilAsync(Array.fromValues(new byte[] { 5 })));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertError(new EndOfStreamException(), stream.readBytesUntilAsync(Array.fromValues(new byte[] { 5 })).awaitReturn());
                });

                runner.test("with bytes but not the stopBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntilAsync(Array.fromValues(new byte[] { 6, 6 })).awaitReturn());
                    test.assertNull(stream.getCurrent());
                });

                runner.test("with bytes including the stopBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntilAsync(Array.fromValues(new byte[] { 3, 4, 5 })).awaitReturn());
                    test.assertEqual((byte)5, stream.getCurrent());
                });
            });

            runner.test("asLineReadStream()", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream();
                test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(boolean)", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream(true);
                test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                test.assertTrue(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(CharacterEncoding,boolean)", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream(CharacterEncoding.US_ASCII, false);
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("writeByte(byte)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                stream.writeByte((byte)17);
                test.assertEqual(new byte[] { 17 }, stream.getBytes());
            });

            runner.test("writeBytes(byte[])", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                test.assertThrows(() -> stream.writeBytes(new byte[0]));
                test.assertEqual(new byte[0], stream.getBytes());

                stream.writeBytes(new byte[] { 1, 2, 3, 4 });
                test.assertEqual(new byte[] { 1, 2, 3, 4 }, stream.getBytes());
            });

            runner.test("writeBytes(byte[],int,int)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                test.assertThrows(() -> stream.writeBytes(new byte[0], 0, 0));
                test.assertEqual(new byte[0], stream.getBytes());

                test.assertThrows(() -> stream.writeBytes(new byte[] { 1, 2, 3, 4 }, 1, 0));
                test.assertEqual(new byte[0], stream.getBytes());

                stream.writeBytes(new byte[] { 1, 2, 3, 4 }, 1, 2);
                test.assertEqual(new byte[] { 2, 3 }, stream.getBytes());
            });

            runner.test("writeAllBytes(ByteReadStream)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                test.assertThrows(() -> stream.writeAllBytes((ByteReadStream)null));
                test.assertEqual(new byte[0], stream.getBytes());
            });

            runner.test("asCharacterWriteStream()", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final CharacterWriteStream characterWriteStream = stream.asCharacterWriteStream();
                test.assertNotNull(characterWriteStream);
                test.assertSame(stream, characterWriteStream.asByteWriteStream());
            });

            runner.test("asCharacterWriteStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final CharacterWriteStream characterWriteStream = stream.asCharacterWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(characterWriteStream);
                test.assertSame(stream, characterWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream()", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final LineWriteStream lineWriteStream = stream.asLineWriteStream();
                test.assertNotNull(lineWriteStream);
                test.assertSame(stream, lineWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final LineWriteStream lineWriteStream = stream.asLineWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(lineWriteStream);
                test.assertSame(stream, lineWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream(String)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final LineWriteStream lineWriteStream = stream.asLineWriteStream("\r\n");
                test.assertNotNull(lineWriteStream);
                test.assertEqual("\r\n", lineWriteStream.getLineSeparator());
                test.assertSame(stream, lineWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream(CharacterEncoding,String)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                final LineWriteStream lineWriteStream = stream.asLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                test.assertNotNull(lineWriteStream);
                test.assertSame(CharacterEncoding.US_ASCII, lineWriteStream.getCharacterEncoding());
                test.assertEqual("\r\n", lineWriteStream.getLineSeparator());
                test.assertSame(stream, lineWriteStream.asByteWriteStream());
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
