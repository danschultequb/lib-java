package qub;

public class FakeTCPServer implements TCPServer
{
    private final Clock clock;
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final FakeNetwork network;
    private final Mutex mutex;
    private final MutexCondition hasClientsToAccept;
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
        hasClientsToAccept = mutex.createCondition();
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
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return mutex.criticalSectionResult(() ->
        {
            while (!disposed && !clientsToAccept.any())
            {
                hasClientsToAccept.watch().await();
            }

            return isDisposed()
                ? Result.error(new IllegalStateException("isDisposed() cannot be true."))
                : Result.success(clientsToAccept.removeFirst());
        });
    }

    @Override
    public Result<TCPClient> accept(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(timeout);
        return accept(dateTimeTimeout);
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return mutex.criticalSectionResult(timeout, () ->
        {
            while (!isDisposed() && !clientsToAccept.any())
            {
                hasClientsToAccept.watch(timeout).await();
            }

            return isDisposed()
                ? Result.error(new IllegalStateException("isDisposed() cannot be true."))
                : Result.success(clientsToAccept.removeFirst());
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
