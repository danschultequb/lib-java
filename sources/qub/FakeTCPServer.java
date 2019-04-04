package qub;

public class FakeTCPServer implements TCPServer
{
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final FakeNetwork network;
    private final AsyncRunner asyncRunner;
    private final Mutex mutex;
    private final MutexCondition hasClientsToAccept;
    private final List<FakeTCPClient> clientsToAccept;
    private volatile boolean disposed;

    public FakeTCPServer(IPv4Address localIPAddress, int localPort, FakeNetwork network, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(localIPAddress, "localIPAddress");
        PreCondition.assertBetween(1, localPort, 65535, "localPort");
        PreCondition.assertNotNull(network, "network");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.network = network;
        this.asyncRunner = asyncRunner;
        mutex = new SpinMutex(asyncRunner.getClock());
        hasClientsToAccept = mutex.createCondition();
        clientsToAccept = new ArrayList<>();
    }

    @Override
    public Clock getClock()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return asyncRunner.getClock();
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

        return mutex.criticalSection(() ->
        {
            while (!disposed && !clientsToAccept.any())
            {
                hasClientsToAccept.await();
            }

            return isDisposed()
                ? Result.error(new IllegalStateException("isDisposed() cannot be true."))
                : Result.success(clientsToAccept.removeFirst());
        });
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return mutex.criticalSection(timeout, () ->
        {
            while (!isDisposed() && !clientsToAccept.any())
            {
                hasClientsToAccept.await(timeout).await();
            }

            if (isDisposed())
            {
                throw new IllegalStateException("isDisposed() cannot be true.");
            }

            return clientsToAccept.removeFirst();
        });
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public boolean isDisposed()
    {
        return mutex.criticalSection(() -> disposed);
    }

    @Override
    public Result<Boolean> dispose()
    {
        return mutex.criticalSection(() ->
        {
            Result<Boolean> result;
            if (disposed)
            {
                result = Result.successFalse();
            }
            else
            {
                disposed = true;
                network.serverDisposed(getLocalIPAddress(), getLocalPort());
                result = Result.successTrue();
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
        });
    }
}
