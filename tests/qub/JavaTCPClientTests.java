package qub;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPClientTests
{
    public static void test(TestRunner runner)
    {
        final AtomicInteger port = new AtomicInteger(13000);

        runner.testGroup(JavaTCPClient.class, () ->
        {
            runner.testGroup("create(Socket)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Result<TCPClient> tcpClientResult = JavaTCPClient.create((Socket)null, test.getParallelAsyncRunner());
                    test.assertError(new IllegalArgumentException("socket cannot be null."), tcpClientResult);
                });
            });
        });
    }
}
