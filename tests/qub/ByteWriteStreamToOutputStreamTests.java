package qub;

import java.io.IOException;

public class ByteWriteStreamToOutputStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteWriteStreamToOutputStream.class, () ->
        {
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final ByteWriteStreamToOutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
                try
                {
                    outputStream.close();
                    test.assertFalse(byteWriteStream.isOpen());
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("writeByte()", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final ByteWriteStreamToOutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
                try
                {
                    outputStream.write((byte)15);
                    test.assertEqual(new byte[] { 15 }, byteWriteStream.getBytes());
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("write(byte[])", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final ByteWriteStreamToOutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
                try
                {
                    outputStream.write(new byte[] { 16, 17, 18, 19, 20 });
                    test.assertEqual(new byte[] { 16, 17, 18, 19, 20 }, byteWriteStream.getBytes());
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });
        });
    }
}
