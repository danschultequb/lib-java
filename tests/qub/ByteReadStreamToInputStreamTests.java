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
                    test.assertFalse(byteReadStream.isOpen());
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("read()", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
                try
                {
                    final int byteRead = inputStream.read();
                    test.assertEqual(-1, byteRead);
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });

            runner.test("read(byte[])", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
                final byte[] bytes = new byte[100];
                try
                {
                    final int bytesRead = inputStream.read(bytes);
                    test.assertEqual(-1, bytesRead);
                    test.assertEqual(new byte[100], bytes);
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
            });
        });
    }
}
