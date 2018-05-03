package qub;

public class InMemoryByteReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryByteReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, InMemoryByteReadStream::new);

            runner.test("constructor()", (Test test) ->
            {
                final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                test.assertFalse(readStream.isDisposed());
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                try
                {
                    readStream.close();
                    test.assertTrue(readStream.isDisposed());
                    readStream.close();
                    test.assertTrue(readStream.isDisposed());
                }
                catch (Exception e)
                {
                    test.fail(e);
                }
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();

                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 10 });

                    test.assertSuccess((byte)10, byteReadStream.readByte());
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 10, 20 });

                    test.assertSuccess((byte)10, byteReadStream.readByte());
                    test.assertSuccess((byte)20, byteReadStream.readByte());
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                    readStream.dispose();

                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), readStream.readByte());
                });
            });

            runner.testGroup("readByteAsync()", () ->
            {
                runner.test("with no AsyncRunner assigned", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();

                    test.assertError(new IllegalArgumentException("Cannot invoke ByteReadStream asynchronous functions when the ByteReadStream was not provided an AsyncRunner to its constructor."), byteReadStream.readByteAsync().awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());

                    test.assertSuccess(null, byteReadStream.readByteAsync().awaitReturn());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 10 }, test.getMainAsyncRunner());

                    test.assertSuccess((byte)10, byteReadStream.readByteAsync().awaitReturn());
                    test.assertSuccess(null, byteReadStream.readByteAsync().awaitReturn());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 10, 20 }, test.getMainAsyncRunner());

                    test.assertSuccess((byte)10, byteReadStream.readByteAsync().awaitReturn());
                    test.assertSuccess((byte)20, byteReadStream.readByteAsync().awaitReturn());
                    test.assertSuccess(null, byteReadStream.readByteAsync().awaitReturn());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream readStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    readStream.dispose();

                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), readStream.readByteAsync().awaitReturn());
                });
            });
            
            runner.test("readBytes(int)", (Test test) ->
            {
                InMemoryByteReadStream readStream = new InMemoryByteReadStream();

                test.assertError(new IllegalArgumentException("bytesToRead (-5) must be greater than 0."), readStream.readBytes(-5));
                test.assertError(new IllegalArgumentException("bytesToRead (0) must be greater than 0."), readStream.readBytes(0));
                test.assertSuccess(null, readStream.readBytes(1));
                test.assertSuccess(null, readStream.readBytes(5));

                readStream = new InMemoryByteReadStream(new byte[] { 0, 1, 2, 3 });

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

                test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), readStream.readBytes(1));
            });

            runner.test("readBytesAsync(int)", (Test test) ->
            {
                InMemoryByteReadStream readStream = new InMemoryByteReadStream(test.getMainAsyncRunner());

                test.assertError(new IllegalArgumentException("bytesToRead (-5) must be greater than 0."), readStream.readBytesAsync(-5).awaitReturn());
                test.assertError(new IllegalArgumentException("bytesToRead (0) must be greater than 0."), readStream.readBytesAsync(0).awaitReturn());
                test.assertSuccess(null, readStream.readBytesAsync(1).awaitReturn());
                test.assertSuccess(null, readStream.readBytesAsync(5).awaitReturn());

                readStream = new InMemoryByteReadStream(new byte[] { 0, 1, 2, 3 }, test.getMainAsyncRunner());

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

                test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), readStream.readBytesAsync(1).awaitReturn());
            });

            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    byteReadStream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), byteReadStream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), byteReadStream.readBytes(outputBytes));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), byteReadStream.readBytes(outputBytes));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, byteReadStream.readBytes(outputBytes));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4 });
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, byteReadStream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytes(outputBytes));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, byteReadStream.readByte());
                });
            });

            runner.testGroup("readBytesAsync(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    byteReadStream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4 }, test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytesAsync(outputBytes).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, byteReadStream.readByte());
                });
            });

            runner.testGroup("readBytes(byte[],int,int)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    byteReadStream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), byteReadStream.readBytes(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), byteReadStream.readBytes(outputBytes, 1, 3));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), byteReadStream.readBytes(outputBytes, 1, 3));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (-2) must be between 0 and 9."), byteReadStream.readBytes(outputBytes, -2, 3));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (10) must be between 0 and 9."), byteReadStream.readBytes(outputBytes, outputBytes.length, 3));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (-1) must be between 1 and 9."), byteReadStream.readBytes(outputBytes, 1, -1));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (0) must be between 1 and 9."), byteReadStream.readBytes(outputBytes, 1, 0));
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (10) must be between 1 and 9."), byteReadStream.readBytes(outputBytes, 1, 10));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, byteReadStream.readBytes(outputBytes, 1, 3));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4 });
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, byteReadStream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with more bytes to read than length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytes(outputBytes, 0, 10));
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, byteReadStream.readByte());
                });
            });

            runner.testGroup("readBytesAsync(byte[],int,int)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    byteReadStream.dispose();

                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), byteReadStream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = null;
                    test.assertError(new IllegalArgumentException("outputBytes cannot be null."), byteReadStream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[0];
                    test.assertError(new IllegalArgumentException("outputBytes.length (0) must be greater than 0."), byteReadStream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (-2) must be between 0 and 9."), byteReadStream.readBytesAsync(outputBytes, -2, 3).awaitReturn());
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("startIndex (10) must be between 0 and 9."), byteReadStream.readBytesAsync(outputBytes, outputBytes.length, 3).awaitReturn());
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (-1) must be between 1 and 9."), byteReadStream.readBytesAsync(outputBytes, 1, -1).awaitReturn());
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (0) must be between 1 and 9."), byteReadStream.readBytesAsync(outputBytes, 1, 0).awaitReturn());
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertError(new IllegalArgumentException("length (10) must be between 1 and 9."), byteReadStream.readBytesAsync(outputBytes, 1, 10).awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(null, byteReadStream.readBytesAsync(outputBytes, 1, 3).awaitReturn());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4 }, test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(4, byteReadStream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess(null, byteReadStream.readByte());
                });

                runner.test("with more bytes to read than length", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test.getMainAsyncRunner());
                    final byte[] outputBytes = new byte[10];
                    test.assertSuccess(10, byteReadStream.readBytesAsync(outputBytes, 0, 10).awaitReturn());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertSuccess((byte)11, byteReadStream.readByte());
                });
            });

            runner.testGroup("readAllBytes()", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    byteReadStream.dispose();
                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), byteReadStream.readAllBytes());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                    test.assertSuccess(new byte[0], byteReadStream.readAllBytes());
                });
            });

            runner.testGroup("readAllBytesAsync()", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    byteReadStream.dispose();
                    test.assertError(new IllegalArgumentException("byteReadStream cannot be disposed."), byteReadStream.readAllBytesAsync().awaitReturn());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(test.getMainAsyncRunner());
                    test.assertSuccess(new byte[0], byteReadStream.readAllBytesAsync().awaitReturn());
                });
            });

            runner.test("asLineReadStream()", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final LineReadStream lineReadStream = byteReadStream.asLineReadStream();
                test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final LineReadStream lineReadStream = byteReadStream.asLineReadStream(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(boolean)", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final LineReadStream lineReadStream = byteReadStream.asLineReadStream(true);
                test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                test.assertTrue(lineReadStream.getIncludeNewLines());
                test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
            });

            runner.test("asLineReadStream(CharacterEncoding,boolean)", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final LineReadStream lineReadStream = byteReadStream.asLineReadStream(CharacterEncoding.UTF_8, false);
                test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                test.assertFalse(lineReadStream.getIncludeNewLines());
                test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
            });
        });
    }
}
