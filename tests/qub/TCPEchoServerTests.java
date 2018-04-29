package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class TCPEchoServerTests
{
    public static void test(TestRunner runner)
    {
        final AtomicInteger port = new AtomicInteger(14000);

        runner.testGroup(TCPEchoServer.class, () ->
        {
            runner.test("echo()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).getValue())
                {
                    final AsyncFunction<Result<Boolean>> echoTask = asyncRunner.schedule(echoServer::echo);

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).getValue())
                        {
                            final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();
                            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                            tcpClientLineWriteStream.writeLine("Hello");
                            test.assertEqual("Hello", tcpClientLineReadStream.readLine());

                            tcpClientLineWriteStream.writeLine("World");
                            test.assertEqual("World", tcpClientLineReadStream.readLine());
                        }
                        catch (Exception e)
                        {
                            test.fail(e);
                        }
                    });

                    clientTask.await();
                    final Result<Boolean> serverResult = echoTask.awaitReturn();
                    test.assertSuccess(true, serverResult);
                }
                catch (Exception e)
                {
                    test.fail(e);
                }
            });

            runner.test("echoAsync()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).getValue())
                {
                    final AsyncAction echoServerTask = echoServer.echoAsync();

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        //PARALLEL
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).getValue())
                        {
                            final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();
                            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                            tcpClientLineWriteStream.writeLine("Hello");
                            test.assertEqual("Hello", tcpClientLineReadStream.readLine());

                            tcpClientLineWriteStream.writeLine("World");
                            test.assertEqual("World", tcpClientLineReadStream.readLine());
                        }
                        catch (Exception e)
                        {
                            test.fail(e);
                        }
                    });

                    //MAIN
                    echoServerTask.await();
                    clientTask.await();
                }
                catch (Exception e)
                {
                    test.fail(e);
                }
            });
        });
    }
}
