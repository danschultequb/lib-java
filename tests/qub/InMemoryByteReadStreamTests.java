package qub;

public class InMemoryByteReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryByteReadStream.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                test.assertFalse(readStream.isDisposed());
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                readStream.close();
                test.assertTrue(readStream.isDisposed());
                readStream.close();
                test.assertTrue(readStream.isDisposed());
            });
            
            runner.test("readBytes(int)", (Test test) ->
            {
                InMemoryByteReadStream readStream = new InMemoryByteReadStream();

                test.assertError(new IllegalArgumentException("bytesToRead must be greater than 0."), readStream.readBytes(-5));
                test.assertError(new IllegalArgumentException("bytesToRead must be greater than 0."), readStream.readBytes(0));
                test.assertSuccess(null, readStream.readBytes(1));
                test.assertSuccess(null, readStream.readBytes(5));

                readStream = new InMemoryByteReadStream(new byte[] { 0, 1, 2, 3 });

                test.assertError(new IllegalArgumentException("bytesToRead must be greater than 0."), readStream.readBytes(-5));
                test.assertError(new IllegalArgumentException("bytesToRead must be greater than 0."), readStream.readBytes(0));
                test.assertSuccess(new byte[] { 0 }, readStream.readBytes(1));
                test.assertSuccess(new byte[] { 1, 2 }, readStream.readBytes(2));
                test.assertSuccess(new byte[] { 3 }, readStream.readBytes(3));
                test.assertSuccess(null, readStream.readBytes(1));
                test.assertSuccess(null, readStream.readBytes(5));

                readStream.close();

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
