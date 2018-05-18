package qub;

public class FakeTCPServer extends AsyncDisposableBase implements TCPServer
{
    private final AsyncRunner asyncRunner;
    private final Mutex mutex;
    private final MutexCondition hasConnectionRequests;
    private final Queue<FakeConnectionRequest> connectionRequests;
    private final List<Integer> localPortsInUse;
    private volatile boolean disposed;

    public FakeTCPServer(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        mutex = new SpinMutex();
        hasConnectionRequests = mutex.createCondition();
        connectionRequests = new ArrayListQueue<FakeConnectionRequest>();
        localPortsInUse = new ArrayList<Integer>();
    }

    @Override
    public Result<TCPClient> accept()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            while (!disposed && !connectionRequests.any())
            {
                hasConnectionRequests.await();
            }

            return null;
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
                result = Result.successTrue();
            }
            return result;
        }
    }
}
