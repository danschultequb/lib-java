package qub;

public class InMemoryByteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryByteStream.class, () ->
        {
            AsyncDisposableTests.test(runner, InMemoryByteStream::new);

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

                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10 }, test);

                    test.assertSuccess((byte)10, stream.readByte());
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10, 20 }, test);

                    test.assertSuccess((byte)10, stream.readByte());
                    test.assertSuccess((byte)20, stream.readByte());
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream readStream = create(test);
                    readStream.dispose();

                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), readStream.readByte());
                });
            });

            runner.testGroup("readByteAsync()", () ->
            {
                runner.test("with no AsyncRunner assigned", (Test test) ->
                {
                    final InMemoryByteStream stream = new InMemoryByteStream((AsyncRunner)null);

                    test.assertError(new IllegalArgumentException("Cannot invoke ByteReadStream asynchronous functions when the ByteReadStream has not been assigned an AsyncRunner."), stream.readByteAsync().awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);

                    test.assertSuccess(null, stream.readByteAsync().awaitReturn());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10 }, test);

                    test.assertSuccess((byte)10, stream.readByteAsync().awaitReturn());
                    test.assertSuccess(null, stream.readByteAsync().awaitReturn());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10, 20 }, test);

                    test.assertSuccess((byte)10, stream.readByteAsync().awaitReturn());
                    test.assertSuccess((byte)20, stream.readByteAsync().awaitReturn());
                    test.assertSuccess(null, stream.readByteAsync().awaitReturn());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream readStream = create(test);
                    readStream.dispose();

                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), readStream.readByteAsync().awaitReturn());
                });
            });
            
            runner.test("readBytes(int)", (Test test) ->
            {
                InMemoryByteStream readStream = create(test);

                test.assertError(new IllegalArgumentException("bytesToRead (-5) must be greater than 0."), readStream.readBytes(-5));
                test.assertError(new IllegalArgumentException("bytesToRead (0) must be greater than 0."), readStream.readBytes(0));
                test.assertSuccess(null, readStream.readBytes(1));
                test.assertSuccess(null, readStream.readBytes(5));

                readStream = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertError(new IllegalArgumentException("bytesToRead (-5) must be greater than 0."), readStream.readBytes(-5));
                test.assertError(new IllegalArgumentException("bytesToRead (0) must be greater than 0."), readStream.readBytes(0));
                test.assertSuccess(new byte[] { 0 }, readStream.readBytes(1));
                test.assertSuccess(new byte[] { 1, 2 }, readStream.readBytes(2));
                test.assertSuccess(new byte[] { 3 }, readStream.readBytes(3));
                test.assertSuccess(null, readStream.readBytes(1));
                test.assertSuccess(null, readStream.readBytes(5));

                try
                {
                    readStream.close();
                }
                catch (Exception e)
                {
                    test.fail(e);
                }

                test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), readStream.readBytes(1));
            });

            runner.test("readBytesAsync(int)", (Test test) ->
            {
                InMemoryByteStream readStream = create(test);

                test.assertError(new IllegalArgumentException("bytesToRead (-5) must be greater than 0."), readStream.readBytesAsync(-5).awaitReturn());
                test.assertError(new IllegalArgumentException("bytesToRead (0) must be greater than 0."), readStream.readBytesAsync(0).awaitReturn());
                test.assertSuccess(null, readStream.readBytesAsync(1).awaitReturn());
                test.assertSuccess(null, readStream.readBytesAsync(5).awaitReturn());

                readStream = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertError(new IllegalArgumentException("bytesToRead (-5) must be greater than 0."), readStream.readBytesAsync(-5).awaitReturn());
                test.assertError(new IllegalArgumentException("bytesToRead (0) must be greater than 0."), readStream.readBytesAsync(0).awaitReturn());
                test.assertSuccess(new byte[] { 0 }, readStream.readBytesAsync(1).awaitReturn());
                test.assertSuccess(new byte[] { 1, 2 }, readStream.readBytesAsync(2).awaitReturn());
                test.assertSuccess(new byte[] { 3 }, readStream.readBytesAsync(3).awaitReturn());
                test.assertSuccess(null, readStream.readBytesAsync(1).awaitReturn());
                test.assertSuccess(null, readStream.readBytesAsync(5).awaitReturn());

                try
                {
                    readStream.close();
                }
                catch (Exception e)
                {
                    test.fail(e);
                }

                test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), readStream.readBytesAsync(1).awaitReturn());
            });

            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), stream.readBytes(outputBytes));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), stream.readBytes(outputBytes));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, stream.readByte());
                });
            });

            runner.testGroup("readBytesAsync(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), stream.readBytesAsync(outputBytes).awaitReturn());
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), stream.readBytesAsync(outputBytes).awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
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
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readBytes(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), stream.readBytes(outputBytes, 1, 3));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), stream.readBytes(outputBytes, 1, 3));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (-2) must be between 0 and 9."), stream.readBytes(outputBytes, -2, 3));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (10) must be between 0 and 9."), stream.readBytes(outputBytes, outputBytes.length, 3));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (-1) must be between 1 and 9."), stream.readBytes(outputBytes, 1, -1));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (0) must be between 1 and 9."), stream.readBytes(outputBytes, 1, 0));
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (10) must be between 1 and 9."), stream.readBytes(outputBytes, 1, 10));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, stream.readBytes(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
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
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), stream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), stream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (-2) must be between 0 and 9."), stream.readBytesAsync(outputBytes, -2, 3).awaitReturn());
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (10) must be between 0 and 9."), stream.readBytesAsync(outputBytes, outputBytes.length, 3).awaitReturn());
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (-1) must be between 1 and 9."), stream.readBytesAsync(outputBytes, 1, -1).awaitReturn());
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (0) must be between 1 and 9."), stream.readBytesAsync(outputBytes, 1, 0).awaitReturn());
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (10) must be between 1 and 9."), stream.readBytesAsync(outputBytes, 1, 10).awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, stream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, stream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, stream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, stream.readByte());
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
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readAllBytes());
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
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readAllBytesAsync().awaitReturn());
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
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readBytesUntil((byte)5));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertSuccess(null, stream.readBytesUntil((byte)5));
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
                    test.assertError(new IllegalArgumentException("byteReadStream.isDisposed() (true) must be false."), stream.readBytesUntilAsync((byte)5).awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertSuccess(null, stream.readBytesUntilAsync((byte)5).awaitReturn());
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

            runner.test("asLineReadStream()", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream().getValue();
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream(CharacterEncoding.US_ASCII).getValue();
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(boolean)", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream(true).getValue();
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertTrue(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(CharacterEncoding,boolean)", (Test test) ->
            {
                final InMemoryByteStream stream = create(test);
                final LineReadStream lineReadStream = stream.asLineReadStream(CharacterEncoding.US_ASCII, false).getValue();
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(stream, lineReadStream.asByteReadStream());
            });

            runner.test("write(byte)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                stream.write((byte)17);
                test.assertEqual(new byte[] { 17 }, stream.getBytes());
            });

            runner.test("write(byte[])", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                stream.write(new byte[0]);
                test.assertEqual(new byte[0], stream.getBytes());

                stream.write(new byte[] { 1, 2, 3, 4 });
                test.assertEqual(new byte[] { 1, 2, 3, 4 }, stream.getBytes());
            });

            runner.test("write(byte[],int,int)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                stream.write(new byte[0], 0, 0);
                test.assertEqual(new byte[0], stream.getBytes());

                stream.write(new byte[] { 1, 2, 3, 4 }, 1, 0);
                test.assertEqual(new byte[0], stream.getBytes());

                stream.write(new byte[] { 1, 2, 3, 4 }, 1, 2);
                test.assertEqual(new byte[] { 2, 3 }, stream.getBytes());
            });

            runner.test("writeAll(ByteReadStream)", (Test test) ->
            {
                final InMemoryByteStream stream = new InMemoryByteStream();
                test.assertError(new IllegalArgumentException("byteReadStream cannot be null."), stream.writeAll(null));
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
