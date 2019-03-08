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
     * thrown as a RuntimeException. If this Result does not contain an error, then the Result's
     * value will be returned.
     * @return The value of this Result.
     */
    T awaitError();

    /**
     * Wait for this Result to complete. If this Result contains an error, then the error will be
     * thrown if it is the same type as the expectedErrorType. If it is not the same type as the
     * expectedErrorType, then it will be thrown as a RuntimeException. If this Result does not
     * contain an error, then the Result's value will be returned.
     * @param expectedErrorType The type of error that is expected to occur from this Result.
     * @return The value of this Result.
     */
    <TError extends Throwable> T awaitError(Class<TError> expectedErrorType) throws TError;

    @Deprecated
    T getValue();

    @Deprecated
    boolean hasError();

    @Deprecated
    Throwable getError();

    @Deprecated
    Class<? extends Throwable> getErrorType();

    @Deprecated
    String getErrorMessage();

    @Deprecated
    void throwError();

    @Deprecated
    T throwErrorOrGetValue();

    @Deprecated
    <U> Result<U> convertError();

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
    Result<T> catchResultError(Action1<Result<T>> action);

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
     * Get whether or not the provided Result is equal to this Result.
     * @param rhs The provided Result.
     */
    boolean equals(Result<?> rhs);

    /**
     * Create a new empty successful Result.
     * @param <U> The type of value the Result should contain.
     */
    static <U> Result<U> success()
    {
        return SyncResult.success();
    }

    /**
     * Get a successful Result that contains a true boolean value.
     */
    static Result<Boolean> successTrue()
    {
        return SyncResult.successTrue();
    }

    /**
     * Get a successful Result that contains a true boolean value.
     */
    static Result<Boolean> successFalse()
    {
        return SyncResult.successFalse();
    }

    /**
     * Create a new successful Result that contains the provided value.
     * @param value The value the Result should contain.
     * @param <U> The type of the value.
     */
    static <U> Result<U> success(U value)
    {
        return SyncResult.success(value);
    }

    /**
     * Create a new Result by synchronously running the provided Action and returning the result.
     * @param action The action to run.
     */
    static Result<Void> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            action.run();
            return null;
        });
    }

    /**
     * Create a new Result by synchronously running the provided Function and returning the result.
     * @param function The function to run.
     * @param <U> The type of value the function will return.
     */
    static <U> Result<U> create(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<U> result;
        try
        {
            result = Result.success(function.run());
        }
        catch (Throwable error)
        {
            result = Result.error(error);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Create a new Result that contains the provided error.
     * @param error The error that the Result should contain.
     * @param <U> The type of value the Result can contain.
     */
    static <U> Result<U> error(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return SyncResult.error(error);
    }

    /**
     * Run the provided body action as long as the provided condition function returns true. If
     * condition or body thrown an error, then the error will be returned as a Result.
     * @param condition The condition that will determine whether or not the body action is run.
     * @param body The body of the while loop.
     * @return The result of the while loop.
     */
    @Deprecated
    static Result<Void> runWhile(Function0<Boolean> condition, Action0 body)
    {
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertNotNull(body, "body");

        Result<Void> result;
        try
        {
            Boolean conditionResult = condition.run();
            while (conditionResult != null && conditionResult)
            {
                body.run();

                conditionResult = condition.run();
            }
            result = Result.success();
        }
        catch (Throwable error)
        {
            result = error(error);
        }

        return result;
    }

    @Deprecated
    static <U> Result<U> isFalse(boolean value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return Result.equal(false, value, expressionName);
    }

    @Deprecated
    static <U> Result<U> isTrue(boolean value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return Result.equal(true, value, expressionName);
    }

    @Deprecated
    static <T,U> Result<U> equal(T expectedValue, T value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (!Comparer.equal(expectedValue, value))
        {
            result = Result.error(SyncResult.createError(expressionName + " (" + value + ") must be " + expectedValue + "."));
        }
        return result;
    }

    @Deprecated
    static <U> Result<U> notNull(Object value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (value == null)
        {
            result = Result.error(SyncResult.createError(expressionName + " cannot be null."));
        }
        return result;
    }

    @Deprecated
    static <U> Result<U> greaterThan(int value, int lowerBound, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (value <= lowerBound)
        {
            result = Result.error(SyncResult.createError(expressionName + " (" + value + ") must be greater than " + lowerBound + "."));
        }
        return result;
    }

    @Deprecated
    static <U> Result<U> greaterThanOrEqualTo(int value, int lowerBound, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            result = Result.error(SyncResult.createError(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, expressionName)));
        }
        return result;
    }

    @Deprecated
    static <U> Result<U> between(int lowerBound, int value, int upperBound, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (lowerBound == upperBound)
        {
            result = Result.equal(lowerBound, value, expressionName);
        }
        else if (upperBound < lowerBound)
        {
            result = Result.between(upperBound, value, lowerBound, expressionName);
        }
        else if (value < lowerBound || upperBound < value)
        {
            result = Result.error(SyncResult.createError(AssertionMessages.between(lowerBound, value, upperBound, expressionName)));
        }
        return result;
    }
}
