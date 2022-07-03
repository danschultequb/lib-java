package qub;

public class OnErrorIterator<T,TError extends Throwable> extends CatchErrorIterator<T,TError>
{
    private OnErrorIterator(Iterator<T> innerIterator, Class<TError> errorType, Action1<TError> catchErrorAction)
    {
        super(innerIterator, errorType, catchErrorAction);
    }

    public static <T> OnErrorIterator<T,Throwable> create(Iterator<T> innerIterator, Action0 onErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(onErrorAction, "onErrorAction");

        return OnErrorIterator.create(innerIterator, Throwable.class, onErrorAction);
    }

    public static <T> OnErrorIterator<T,Throwable> create(Iterator<T> innerIterator, Action1<Throwable> onErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(onErrorAction, "onErrorAction");

        return OnErrorIterator.create(innerIterator, Throwable.class, onErrorAction);
    }

    public static <T,TError extends Throwable> OnErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType, Action0 onErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(onErrorAction, "onErrorAction");

        return OnErrorIterator.create(innerIterator, errorType, (TError error) -> { onErrorAction.run(); });
    }

    public static <T,TError extends Throwable> OnErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType, Action1<TError> onErrorAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(onErrorAction, "onErrorAction");

        return new OnErrorIterator<>(innerIterator, errorType, (TError error) ->
        {
            final TError expectedError = Exceptions.getInstanceOf(error, errorType);
            if (expectedError != null)
            {
                onErrorAction.run(expectedError);
            }
            throw Exceptions.asRuntime(error);
        });
    }
}
