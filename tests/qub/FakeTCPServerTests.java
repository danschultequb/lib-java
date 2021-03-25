package qub;

public interface FakeTCPServerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeTCPServer.class, () ->
        {
            final IntegerValue port = IntegerValue.create(23358);

            TCPServerTests.test(runner, (Clock clock) ->
            {
                final IPv4Address serverAddress = IPv4Address.localhost;
                final int serverPort = port.incrementAndGet();
                final FakeNetwork network = FakeNetwork.create(clock);
                return clock == null
                    ? FakeTCPServer.create(serverAddress, serverPort, network)
                    : FakeTCPServer.create(serverAddress, serverPort, network, clock);
            });
        });
    }
}
