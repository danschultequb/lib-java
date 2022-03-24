package qub;

public interface ByteWriteStreamToOutputStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ByteWriteStreamToOutputStream.class, () ->
        {
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                final ByteWriteStreamToOutputStream outputStream = ByteWriteStreamToOutputStream.create(byteWriteStream);
                try
                {
                    outputStream.close();
                    test.assertTrue(byteWriteStream.isDisposed());
                }
                catch (java.io.IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("writeByte()", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                final ByteWriteStreamToOutputStream outputStream = ByteWriteStreamToOutputStream.create(byteWriteStream);
                try
                {
                    outputStream.write((byte)15);
                    test.assertEqual(new byte[] { 15 }, byteWriteStream.getBytes());
                }
                catch (java.io.IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("writeByte(byte[])", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                final ByteWriteStreamToOutputStream outputStream = ByteWriteStreamToOutputStream.create(byteWriteStream);
                try
                {
                    outputStream.write(new byte[] { 16, 17, 18, 19, 20 });
                    test.assertEqual(new byte[] { 16, 17, 18, 19, 20 }, byteWriteStream.getBytes());
                }
                catch (java.io.IOException e)
                {
                    test.fail(e);
                }
            });
        });
    }
}
