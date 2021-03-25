package qub;

public interface TCPServerTests
{
    static void test(TestRunner runner, Function1<Clock,? extends TCPServer> creator)
    {
        runner.testGroup(TCPServer.class, () ->
        {
            runner.testGroup("accept()", () ->
            {
                runner.test("when disposed",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        test.assertTrue(server.dispose().await());

                        test.assertThrows(() -> server.accept().await(),
                            new SocketClosedException(
                                new java.net.SocketException("Socket is closed")));
                    }
                });

                runner.test("when disposed either before or while accepting",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner(), resources.getClock()),
                    (Test test, AsyncRunner parallelAsyncRunner, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        final Result<Void> serverTask = parallelAsyncRunner.schedule(() ->
                        {
                            server.accept().await();
                        });

                        test.assertTrue(server.dispose().await());

                        test.assertThrows(() -> serverTask.await(),
                            new SocketClosedException(
                                new java.net.SocketException("Socket is closed")));
                    }
                });
            });

            runner.testGroup("accept(Duration)", () ->
            {
                runner.test("with no clock provided when created", (Test test) ->
                {
                    try (final TCPServer server = creator.run(null))
                    {
                        test.assertThrows(() -> server.accept(Duration.seconds(1)).await(),
                            new PreConditionFailure("this.clock cannot be null."));
                    }
                });

                runner.test("when disposed",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        test.assertTrue(server.dispose().await());

                        test.assertThrows(() -> server.accept(Duration.seconds(1)).await(),
                            new SocketClosedException(
                                new java.net.SocketException("Socket is closed")));
                    }
                });

                runner.test("when disposed either before or while accepting",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner(), resources.getClock()),
                    (Test test, AsyncRunner parallelAsyncRunner, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        final Result<Void> serverTask = parallelAsyncRunner.schedule(() ->
                        {
                            server.accept(Duration.seconds(1)).await();
                        });

                        test.assertTrue(server.dispose().await());

                        test.assertThrows(() -> serverTask.await(),
                            new SocketClosedException(
                                new java.net.SocketException("Socket is closed")));
                    }
                });

                runner.test("with no incoming clients",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        test.assertThrows(() -> server.accept(Duration.seconds(0.1)).await(),
                            new TimeoutException());
                    }
                });
            });

            runner.testGroup("accept(DateTime)", () ->
            {
                runner.test("with no clock provided when created", (Test test) ->
                {
                    try (final TCPServer server = creator.run(null))
                    {
                        test.assertThrows(() -> server.accept(DateTime.epoch).await(),
                            new PreConditionFailure("this.clock cannot be null."));
                    }
                });

                runner.test("when disposed",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        test.assertTrue(server.dispose().await());

                        test.assertThrows(() -> server.accept(DateTime.epoch).await(),
                            new SocketClosedException(
                                new java.net.SocketException("Socket is closed")));
                    }
                });

                runner.test("when disposed either before or while accepting",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner(), resources.getClock()),
                    (Test test, AsyncRunner parallelAsyncRunner, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        final Result<Void> serverTask = parallelAsyncRunner.schedule(() ->
                        {
                            server.accept(DateTime.epoch).await();
                        });

                        test.assertTrue(server.dispose().await());

                        test.assertThrows(() -> serverTask.await(),
                            new SocketClosedException(
                                new java.net.SocketException("Socket is closed")));
                    }
                });

                runner.test("with no incoming clients",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    try (final TCPServer server = creator.run(clock))
                    {
                        test.assertThrows(() -> server.accept(clock.getCurrentDateTime().plus(Duration.seconds(0.1))).await(),
                            new TimeoutException());
                    }
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("multiple times", (Test test) ->
                {
                    final TCPServer tcpServer = creator.run(null);

                    test.assertTrue(tcpServer.dispose().await());
                    test.assertTrue(tcpServer.isDisposed());

                    test.assertFalse(tcpServer.dispose().await());
                    test.assertTrue(tcpServer.isDisposed());
                });
            });
        });
    }
}
