package qub;

/**
 * A BlockingQueue implementation that uses a built-in Java10 implementation.
 * @param <T> The type of value stored in the Queue.
 */
public class JavaBlockingQueue<T> implements BlockingQueue<T>
{
    private final AsyncRunner asyncRunner;
    private final java.util.concurrent.BlockingQueue<T> javaQueue;

    public JavaBlockingQueue(AsyncRunner asyncRunner)
    {
        this(null, asyncRunner);
    }

    public JavaBlockingQueue(Integer capacity, AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        javaQueue = capacity == null
            ? new java.util.concurrent.LinkedBlockingQueue<T>()
            : new java.util.concurrent.LinkedBlockingQueue<T>(capacity);
    }

    @Override
    public int getCount()
    {
        return javaQueue.size();
    }

    @Override
    public boolean any()
    {
        return !javaQueue.isEmpty();
    }

    @Override
    public Result<Boolean> enqueue(T value)
    {
        Result<Boolean> result;
        try
        {
            javaQueue.put(value);
            result = Result.successTrue();
        }
        catch (InterruptedException e)
        {
            result = Result.done(false, e);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> enqueueAsync(final T value)
    {
        return async(asyncRunner, new Function0<Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run()
            {
                return enqueue(value);
            }
        });
    }

    @Override
    public Result<T> dequeue()
    {
        T resultValue = null;
        Throwable error = null;
        try
        {
            resultValue = javaQueue.take();
        }
        catch (InterruptedException e)
        {
            error = e;
        }
        return Result.done(resultValue, error);
    }

    @Override
    public AsyncFunction<Result<T>> dequeueAsync()
    {
        return async(asyncRunner, new Function0<Result<T>>()
        {
            @Override
            public Result<T> run()
            {
                return dequeue();
            }
        });
    }

    private static <U> AsyncFunction<Result<U>> async(AsyncRunner asyncRunner, Function0<Result<U>> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return asyncRunner.schedule(function).thenOn(currentAsyncRunner);
    }
}
