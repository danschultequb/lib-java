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

                    test.assertError(new IllegalArgumentException("Cannot invoke ByteReadStream asynchronous functions when an AsyncRunner was not provided when the ByteReadStream was created."), byteReadStream.readByteAsync().awaitReturn());
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
