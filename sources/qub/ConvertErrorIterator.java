package qub;

public class ConvertErrorIterator<T,TError extends Throwable> extends CatchErrorIterator<T,TError>
{
    private ConvertErrorIterator(Iterator<T> innerIterator, Class<TError> errorType, Action1<TError> catchErrorAction)
    {
        super(innerIterator, errorType, catchErrorAction);
    }

    public static <T> ConvertErrorIterator<T,Throwable> create(Iterator<T> innerIterator, Function0<? extends Throwable> convertErrorFunction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(convertErrorFunction, "convertErrorFunction");

        return ConvertErrorIterator.create(innerIterator, Throwable.class, convertErrorFunction);
    }

    public static <T> ConvertErrorIterator<T,Throwable> create(Iterator<T> innerIterator, Function1<Throwable,? extends Throwable> convertErrorFunction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(convertErrorFunction, "convertErrorFunction");

        return ConvertErrorIterator.create(innerIterator, Throwable.class, (Throwable error) ->
        {
            return convertErrorFunction.run(Exceptions.unwrap(error));
        });
    }

    public static <T,TError extends Throwable> ConvertErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType, Function0<? extends Throwable> convertErrorFunction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(convertErrorFunction, "convertErrorFunction");

        return ConvertErrorIterator.create(innerIterator, errorType, (TError error) -> { return convertErrorFunction.run(); });
    }

    public static <T,TError extends Throwable> ConvertErrorIterator<T,TError> create(Iterator<T> innerIterator, Class<TError> errorType, Function1<TError,? extends Throwable> convertErrorFunction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(convertErrorFunction, "convertErrorFunction");

        return new ConvertErrorIterator<>(innerIterator, errorType, (TError error) ->
        {
            throw Exceptions.asRuntime(convertErrorFunction.run(error));
        });
    }
}
