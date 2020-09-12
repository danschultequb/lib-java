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

    public FakeTCPServer(IPv4Address localIPAddress, int localPort, FakeNetwork network, Clock clock)
    {
        PreCondition.assertNotNull(localIPAddress, "localIPAddress");
        PreCondition.assertBetween(1, localPort, 65535, "localPort");
        PreCondition.assertNotNull(network, "network");
        PreCondition.assertNotNull(clock, "clock");

        this.clock = clock;
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.network = network;
        clientsToAccept = List.create();
        mutex = new SpinMutex(clock);
        hasClientsToAccept = mutex.createCondition(() -> isDisposed() || clientsToAccept.any());
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        return localIPAddress;
    }

    @Override
    public int getLocalPort()
    {
        return localPort;
    }

    @Override
    public Result<TCPClient> accept()
    {
        PreCondition.assertNotDisposed(this, "this");

        return mutex.criticalSection(() ->
        {
            hasClientsToAccept.watch().await();

            if (this.isDisposed())
            {
                throw new IllegalStateException("this.isDisposed() cannot be true.");
            }
            return clientsToAccept.removeFirst();
        });
    }

    @Override
    public Result<TCPClient> accept(Duration2 timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration2.zero, "timeout");
        PreCondition.assertNotDisposed(this, "this");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(timeout);
        return accept(dateTimeTimeout);
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotDisposed(this, "this");

        return mutex.criticalSection(timeout, () ->
        {
            hasClientsToAccept.watch(timeout).await();

            if (this.isDisposed())
            {
                throw new IllegalStateException("this.isDisposed() cannot be true.");
            }
            return clientsToAccept.removeFirst();
        });
    }

    @Override
    public boolean isDisposed()
    {
        return mutex.criticalSection(() -> disposed).await();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return mutex.criticalSection(() ->
        {
            final boolean result = !disposed;
            if (result)
            {
                disposed = true;
                network.serverDisposed(getLocalIPAddress(), getLocalPort());
            }
            return result;
        });
    }

    public void addIncomingClient(FakeTCPClient incomingClient)
    {
        PreCondition.assertNotNull(incomingClient, "incomingClient");

        mutex.criticalSection(() ->
        {
            clientsToAccept.add(incomingClient);
            hasClientsToAccept.signalAll();
        }).await();
    }
}
