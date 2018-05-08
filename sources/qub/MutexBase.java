package qub;

public abstract class MutexBase implements Mutex
{
    @Override
    public void criticalSection(Action0 action)
    {
        if (action != null)
        {
            acquire();
            try
            {
                action.run();
            }
            finally
            {
                release();
            }
        }
    }

    @Override
    public <T> T criticalSection(Function0<T> function)
    {
        T result = null;
        if (function != null)
        {
            acquire();
            try
            {
                result = function.run();
            }
            finally
            {
                release();
            }
        }
        return result;
    }

    @Override
    public Disposable criticalSection()
    {
        acquire();
        return new DisposableBase()
        {
            private boolean disposed;

            @Override
            public boolean isDisposed()
            {
                return disposed;
            }

            @Override
            public Result<Boolean> dispose()
            {
                Result<Boolean> result;
                if (!disposed)
                {
                    disposed = true;
                    result = Result.successTrue();
                    release();
                }
                else
                {
                    result = Result.successFalse();
                }
                return result;
            }
        };
    }
}
