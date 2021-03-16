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
        this.clientsToAccept = List.create();
        this.mutex = SpinMutex.create(clock);
        this.hasClientsToAccept = this.mutex.createCondition(() -> this.isDisposed() || this.clientsToAccept.any());
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

    @Override
    public Result<TCPClient> accept()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.mutex.criticalSection(() ->
        {
            this.hasClientsToAccept.watch().await();

            if (this.isDisposed())
            {
                throw new IllegalStateException("this.isDisposed() cannot be true.");
            }
            return this.clientsToAccept.removeFirst();
        });
    }

    @Override
    public Result<TCPClient> accept(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotDisposed(this, "this");

        final DateTime dateTimeTimeout = this.clock.getCurrentDateTime().plus(timeout);
        return this.accept(dateTimeTimeout);
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotDisposed(this, "this");

        return this.mutex.criticalSection(timeout, () ->
        {
            this.hasClientsToAccept.watch(timeout).await();

            if (this.isDisposed())
            {
                throw new IllegalStateException("this.isDisposed() cannot be true.");
            }
            return this.clientsToAccept.removeFirst();
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.mutex.criticalSection(() -> this.disposed).await();
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
            }
            return result;
        });
    }

    public void addIncomingClient(FakeTCPClient incomingClient)
    {
        PreCondition.assertNotNull(incomingClient, "incomingClient");

        this.mutex.criticalSection(() ->
        {
            this.clientsToAccept.add(incomingClient);
            this.hasClientsToAccept.signalAll();
        }).await();
    }
}
