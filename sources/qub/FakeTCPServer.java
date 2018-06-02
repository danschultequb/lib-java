package qub;

public class FakeTCPServer extends AsyncDisposableBase implements TCPServer
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
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.network = network;
        this.asyncRunner = asyncRunner;
        mutex = new SpinMutex();
        hasClientsToAccept = mutex.createCondition();
        clientsToAccept = new ArrayList<FakeTCPClient>();
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
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            while (!disposed && !clientsToAccept.any())
            {
                hasClientsToAccept.await();
            }

            Result<TCPClient> result = Result.equal(false, disposed, "tcpServer.isDisposed()");
            if (result == null)
            {
                result = Result.<TCPClient>success(clientsToAccept.removeFirst());
            }
            return result;
        }
    }

    @Override
    public AsyncFunction<Result<TCPClient>> acceptAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPClient>> result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
        if (result == null)
        {
            result = currentAsyncRunner.equal(false, asyncRunner.isDisposed(), "asyncRunner.isDisposed()");
            if (result == null)
            {
                result = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                    {
                        @Override
                        public Result<TCPClient> run()
                        {
                            return accept();
                        }
                    })
                    .thenOn(currentAsyncRunner);
            }
        }
        return result;
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
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            clientsToAccept.add(incomingClient);
            hasClientsToAccept.signalAll();
        }
    }
}
