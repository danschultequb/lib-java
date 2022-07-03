package qub;

public class CatchErrorIterator<T,TError extends Throwable> extends IteratorWrapper<T>
{
    private final Class<TError> errorType;
    private final Action1<TError> catchErrorAction;

    protected CatchErrorIterator(Iterator<T> innerIterator, Class<TError> errorType, Action1<TError> catchErrorAction)
    {
        super(innerIterator);

        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(catchErrorAction, "catchErrorAction");

        this.errorType = errorType;
        this.catchErrorAction = catchErrorAction;
    }

    public static <T> CatchErrorIterator<T,Throwable> create(Iterator<T> innerIterator)
    {
        return CatchErrorIterator.create(innerIterator, Throwable.class, Action0.empty);
    }

    public static <T> CatchErrorIterator<T,Throwable> create(Iterator<T> innerIterator, Action0 catchErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(catchErrorAction, "catchErrorAction");

        return CatchErrorIterator.create(innerIterator, Throwable.class, catchErrorAction);
    }

    public static <T> CatchErrorIterator<T,Throwable> create(Iterator<T> innerIterator, Action1<Throwable> catchErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(catchErrorAction, "catchErrorAction");

        return CatchErrorIterator.create(innerIterator, Throwable.class, (Throwable error) ->
        {
            catchErrorAction.run(Exceptions.unwrap(error));
        });
    }

    public static <T,TError extends Throwable> CatchErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");

        return CatchErrorIterator.create(innerIterator, errorType, Action0.empty);
    }

    public static <T,TError extends Throwable> CatchErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType, Action0 catchErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(catchErrorAction, "catchErrorAction");

        return CatchErrorIterator.create(innerIterator, errorType, (TError error) -> { catchErrorAction.run(); });
    }

    public static <T,TError extends Throwable> CatchErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType, Action1<TError> catchErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(catchErrorAction, "catchErrorAction");

        return new CatchErrorIterator<>(innerIterator, errorType, catchErrorAction);
    }

    @Override
    public boolean next()
    {
        boolean result;
        try
        {
            result = super.next();
        }
        catch (Throwable e)
        {
            final TError error = Exceptions.getInstanceOf(e, errorType);
            if (error == null)
            {
                throw e;
            }
            else
            {
                this.catchErrorAction.run(error);
                result = super.hasCurrent();
            }
        }
        return result;
    }
}
