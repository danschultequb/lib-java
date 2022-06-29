package qub;

public class FakeTCPServer implements TCPServer
{
    private final Clock clock;
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final FakeNetwork network;
    private final SpinMutex mutex;
    private final SpinMutexCondition hasClientsToAccept;
    private final List<FakeTCPClient> clientsToAccept;
    private volatile boolean disposed;

    private FakeTCPServer(IPv4Address localIPAddress, int localPort, FakeNetwork network, Clock clock)
    {
        PreCondition.assertNotNull(localIPAddress, "localIPAddress");
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(network, "network");

        this.clock = clock;
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.network = network;
        this.clientsToAccept = List.create();
        this.mutex = (clock == null ? SpinMutex.create() : SpinMutex.create(clock));
        this.hasClientsToAccept = this.mutex.createCondition(() -> this.isDisposed() || this.clientsToAccept.any());
    }

    public static FakeTCPServer create(IPv4Address localIPAddress, int localPort, FakeNetwork network)
    {
        PreCondition.assertNotNull(localIPAddress, "localIPAddress");
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(network, "network");

        return new FakeTCPServer(localIPAddress, localPort, network, null);
    }

    public static FakeTCPServer create(IPv4Address localIPAddress, int localPort, FakeNetwork network, Clock clock)
    {
        PreCondition.assertNotNull(localIPAddress, "localIPAddress");
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(network, "network");
        PreCondition.assertNotNull(clock, "clock");

        return new FakeTCPServer(localIPAddress, localPort, network, clock);
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        return this.localIPAddress;
    }

    @Override
    public int getLocalPort()
    {
        return this.localPort;
    }

    private Result<TCPClient> accept(Action0 watchAction)
    {
        PreCondition.assertNotNull(watchAction, "watchAction");

        return this.mutex.criticalSection(() ->
        {
            watchAction.run();

            if (this.isDisposed())
            {
                throw new SocketClosedException(new java.net.SocketException("Socket is closed"));
            }

            return this.clientsToAccept.removeFirst().await();
        });
    }

    @Override
    public Result<TCPClient> accept()
    {
        return this.accept(() -> this.hasClientsToAccept.watch().await());
    }

    @Override
    public Result<TCPClient> accept(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(this.clock, "this.clock");

        final DateTime dateTimeTimeout = this.clock.getCurrentDateTime().plus(timeout);
        return this.accept(dateTimeTimeout);
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(this.clock, "this.clock");

        return this.accept(() -> this.hasClientsToAccept.watch(timeout).await());
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.mutex.criticalSection(() ->
        {
            final boolean result = !this.disposed;
            if (result)
            {
                this.disposed = true;
                this.network.serverDisposed(this.getLocalIPAddress(), this.getLocalPort());
                this.hasClientsToAccept.signalAll();
            }
            return result;
        });
    }

    public void addIncomingClient(FakeTCPClient incomingClient)
    {
        PreCondition.assertNotNull(incomingClient, "incomingClient");
        PreCondition.assertNotDisposed(incomingClient, "incomingClient");

        this.mutex.criticalSection(() ->
        {
            this.clientsToAccept.add(incomingClient);
            this.hasClientsToAccept.signalAll();
        }).await();
    }
}
