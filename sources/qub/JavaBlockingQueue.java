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
            ? new java.util.concurrent.LinkedBlockingQueue<>()
            : new java.util.concurrent.LinkedBlockingQueue<>(capacity);
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
    public Result<Void> enqueue(T value)
    {
        Result<Void> result;
        try
        {
            javaQueue.put(value);
            result = Result.success();
        }
        catch (InterruptedException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Void>> enqueueAsync(T value)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        return asyncRunner.scheduleSingle(() -> enqueue(value));
    }

    @Override
    public Result<T> dequeue()
    {
        Result<T> result;
        try
        {
            result = Result.success(javaQueue.take());
        }
        catch (InterruptedException e)
        {
            result = Result.error(e);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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
