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
        mutex = new SpinMutex();
        hasClientsToAccept = mutex.createCondition();
        clientsToAccept = new ArrayList<>();
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

        try (final Disposable criticalSection = mutex.criticalSection())
        {
            while (!disposed && !clientsToAccept.any())
            {
                hasClientsToAccept.await();
            }

            Result<TCPClient> result = Result.equal(false, disposed, "tcpServer.isDisposed()");
            if (result == null)
            {
                result = Result.success(clientsToAccept.removeFirst());
            }
            return result;
        }
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public boolean isDisposed()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return disposed;
        }
    }

    @Override
    public Result<Boolean> dispose()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
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
        }
    }

    public void addIncomingClient(FakeTCPClient incomingClient)
    {
        PreCondition.assertNotNull(incomingClient, "incomingClient");

        try (final Disposable criticalSection = mutex.criticalSection())
        {
            clientsToAccept.add(incomingClient);
            hasClientsToAccept.signalAll();
        }
    }
}
