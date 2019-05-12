package qub;

/**
 * A BlockingQueue implementation that uses a built-in Java10 implementation.
 * @param <T> The type of value stored in the Queue.
 */
public class JavaBlockingQueue<T> implements BlockingQueue<T>
{
    private final java.util.concurrent.BlockingQueue<T> javaQueue;

    public JavaBlockingQueue()
    {
        this(null);
    }

    public JavaBlockingQueue(Integer capacity)
    {
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
}
