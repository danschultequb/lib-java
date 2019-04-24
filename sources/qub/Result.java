package qub;

public interface Result<T>
{
    /**
     * Wait for this Result to complete. If this Result contains an error, then the error will be
     * wrapped in an AwaitException and then thrown. If this Result does not contain an error, then
     * the Result's value will be returned.
     * @return The value of this Result.
     */
    T await();

    /**
     * Wait for this Result to complete. If this Result contains an error, then the error will be
     * thrown if it is the same type as the expectedErrorType. If it is not the same type as the
     * expectedErrorType, then it will be thrown as a RuntimeException. If this Result does not
     * contain an error, then the Result's value will be returned.
     * @param expectedErrorType The type of error that is expected to occur from this Result.
     * @return The value of this Result.
     */
    <TError extends Throwable> T await(Class<TError> expectedErrorType) throws TError;

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    Result<Void> then(Action0 action);

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    Result<Void> then(Action1<T> action);

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    <U> Result<U> then(Function0<U> function);

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    <U> Result<U> thenResult(Function0<Result<U>> function);

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    <U> Result<U> then(Function1<T,U> function);

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    <U> Result<U> thenResult(Function1<T,Result<U>> function);

    /**
     * If this result doesn't have an error, then run the provided action and return this Result
     * object.
     * @param action The action to run if this result does not have an error.
     * @return This Result object.
     */
    Result<T> onValue(Action0 action);

    /**
     * If this result doesn't have an error, then run the provided action and return this Result
     * object.
     * @param action The action to run if this result does not have an error.
     * @return This Result object.
     */
    Result<T> onValue(Action1<T> action);

    /**
     * If this Result has an error of the provided type, then catch that error and return a
     * successful Result instead.
     */
    default Result<T> catchError()
    {
        return catchError(() -> {});
    }

    /**
     * If this Result has an error of the provided type, then catch that error and return a
     * successful Result instead.
     * @param errorType The type of error to catch.
     * @param <TError> The type of error to catch.
     * @return This Result if it is successful, or an empty successful Result if this Result
     * contains an error of the provided type.
     */
    <TError extends Throwable> Result<T> catchError(Class<TError> errorType);

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    Result<T> catchError(Action0 action);

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    Result<T> catchError(Action1<Throwable> action);

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action0 action);

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action1<TError> action);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    Result<T> catchError(Function0<T> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    Result<T> catchError(Function1<Throwable,T> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function0<T> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function1<TError,T> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    Result<T> catchErrorResult(Function0<Result<T>> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    Result<T> catchErrorResult(Function1<Throwable,Result<T>> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    <TError extends Throwable> Result<T> catchErrorResult(Class<TError> errorType, Function0<Result<T>> function);

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    <TError extends Throwable> Result<T> catchErrorResult(Class<TError> errorType, Function1<TError,Result<T>> function);

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    Result<T> onError(Action0 action);

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    Result<T> onError(Action1<Throwable> action);

    /**
     * Run the provided action if this Result has an error of the provided type.
     * @param errorType The type of error to run the provided action for.
     * @param action The action to run if this Result has an error of the provided type.
     * @return This Result with its error.
     */
    <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action0 action);

    /**
     * Run the provided action if this Result has an error of the provided type.
     * @param errorType The type of error to run the provided action for.
     * @param action The action to run if this Result has an error of the provided type.
     * @return This Result with its error.
     */
    <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action1<TError> action);

    /**
     * If this Result has an error, catch it and convert it to the error returned by the provided
     * function.
     * @param function The function that will return the new error.
     * @param <TErrorOut> The type of error that will be returned by the function.
     * @return The Result with the new error.
     */
    default <TErrorOut extends Throwable> Result<T> convertError(Function0<TErrorOut> function)
    {
        PreCondition.assertNotNull(function, "function");

        return catchErrorResult(() ->
        {
            return Result.error(function.run());
        });
    }

    /**
     * If this Result has an error, catch it and convert it to the error returned by the provided
     * function.
     * @param function The function that will return the new error.
     * @param <TErrorOut> The type of error that will be returned by the function.
     * @return The Result with the new error.
     */
    default <TErrorOut extends Throwable> Result<T> convertError(Function1<Throwable,TErrorOut> function)
    {
        PreCondition.assertNotNull(function, "function");

        return catchErrorResult((Throwable error) ->
        {
            return Result.error(function.run(error));
        });
    }

    /**
     * If this Result has an error of the provided errorType, catch it and convert it to the error
     * returned by the provided function.
     * @param function The function that will return the new error.
     * @param <TErrorIn> The type of error that was caught.
     * @param <TErrorOut> The type of error that will be returned by the function.
     * @return The Result with the new error.
     */
    default <TErrorIn extends Throwable, TErrorOut extends Throwable> Result<T> convertError(Class<TErrorIn> errorType, Function0<TErrorOut> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return catchErrorResult(errorType, () ->
        {
            return Result.error(function.run());
        });
    }

    /**
     * If this Result has an error of the provided errorType, catch it and convert it to the error
     * returned by the provided function.
     * @param function The function that will return the new error.
     * @param <TErrorIn> The type of error that was caught.
     * @param <TErrorOut> The type of error that will be returned by the function.
     * @return The Result with the new error.
     */
    default <TErrorIn extends Throwable, TErrorOut extends Throwable> Result<T> convertError(Class<TErrorIn> errorType, Function1<TErrorIn,TErrorOut> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return catchErrorResult(errorType, (TErrorIn error) ->
        {
            return Result.error(function.run(error));
        });
    }

    /**
     * Create a new empty successful Result.
     * @param <U> The type of value the Result should contain.
     */
    static <U> SyncResult<U> success()
    {
        return SyncResult.success();
    }

    /**
     * Get a successful Result that contains a 0 Integer value.
     */
    static SyncResult<Integer> successZero()
    {
        return SyncResult.successZero();
    }

    /**
     * Get a successful Result that contains a 1 Integer value.
     */
    static SyncResult<Integer> successOne()
    {
        return SyncResult.successOne();
    }

    /**
     * Get a successful Result that contains a true boolean value.
     */
    static SyncResult<Boolean> successTrue()
    {
        return SyncResult.successTrue();
    }

    /**
     * Get a successful Result that contains a true boolean value.
     */
    static SyncResult<Boolean> successFalse()
    {
        return SyncResult.successFalse();
    }

    /**
     * Create a new successful Result that contains the provided value.
     * @param value The value the Result should contain.
     * @param <U> The type of the value.
     */
    static <U> SyncResult<U> success(U value)
    {
        return SyncResult.success(value);
    }

    /**
     * Create a new Result by synchronously running the provided Action and returning the result.
     * @param action The action to run.
     */
    static <U> SyncResult<U> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return SyncResult.create(action);
    }

    /**
     * Create a new Result by synchronously running the provided Function and returning the result.
     * @param function The function to run.
     * @param <U> The type of value the function will return.
     */
    static <U> SyncResult<U> create(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return SyncResult.create(function);
    }

    /**
     * Create a new Result that contains the provided error.
     * @param error The error that the Result should contain.
     * @param <U> The type of value the Result can contain.
     */
    static <U> SyncResult<U> error(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return SyncResult.error(error);
    }

    /**
     * Create a new Result that contains an end of stream error.
     * @param <U> The type of value that the Result can contain.
     * @return A Result that contains an end of stream error.
     */
    static <U> SyncResult<U> endOfStream()
    {
        return SyncResult.endOfStream();
    }
}
