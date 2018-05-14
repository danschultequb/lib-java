package qub;

import java.io.IOException;

public class ByteReadStreamToInputStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteReadStreamToInputStream.class, () ->
        {
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
                try
                {
                    inputStream.close();
                    test.assertTrue(byteReadStream.isDisposed());
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("read()", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 10, 11, 12 });
                final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
                try
                {
                    final int byteRead = inputStream.read();
                    test.assertEqual(10, byteRead);
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("read(byte[])", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream(new byte[] { 100 });
                final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
                final byte[] bytes = new byte[100];
                try
                {
                    final int bytesRead = inputStream.read(bytes);
                    test.assertEqual(1, bytesRead);
                    test.assertEqual((byte)100, bytes[0]);
                    for (int i = 1; i < bytes.length; ++i)
                    {
                        test.assertEqual((byte)0, bytes[i]);
                    }
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });
        });
    }
}
