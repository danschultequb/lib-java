package qub;

/**
 * An interface that runs provided Actions and Functions asynchronously.
 */
public interface AsyncRunner extends Disposable
{
    /**
     * Get the Clock object that this AsyncRunner uses to determine durations.
     * @return The Clock object that this AsyncRunner uses to determine durations.
     */
    Clock getClock();

    /**
     * Get the number of actions that are currently scheduled.
     * @return The number of actions that are current scheduled.
     */
    int getScheduledTaskCount();

    /**
     * Atomically mark the provided Setable to true and decrement the number of scheduled tasks.
     * @param asyncTaskCompleted The Setable object that will be set to true.
     */
    void markCompleted(Setable<Boolean> asyncTaskCompleted);

    /**
     * Block until the provided AsyncTask is completed.
     * @param asyncTask The AsyncTask to wait for.
     */
    void await(AsyncTask asyncTask);

    /**
     * Schedule the provided action to run asynchronously.
     * @param action The action to run asynchronously.
     * @return A reference to the scheduled action.
     */
    default AsyncAction schedule(Action0 action)
    {
        return schedule(null, action);
    }

    /**
     * Schedule the provided action to run asynchronously and then return the asynchronous execution
     * flow back to the current AsyncRunner.
     * @param action The action to run asynchronously.
     * @return A reference to the scheduled action.
     */
    default AsyncAction scheduleSingle(Action0 action)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return schedule(action).thenOn(currentAsyncRunner);
    }

    /**
     * Schedule the provided action to run asynchronously.
     * @param label The label to associate with the action.
     * @param action The action to run asynchronously.
     * @return A reference to the scheduled action.
     */
    AsyncAction schedule(String label, Action0 action);

    /**
     * Schedule the provided action to run asynchronously and then return the asynchronous execution
     * flow back to the current AsyncRunner.
     * @param label The label to associate with the action.
     * @param action The action to run asynchronously.
     * @return A reference to the scheduled action.
     */
    default AsyncAction scheduleSingle(String label, Action0 action)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return schedule(label, action).thenOn(currentAsyncRunner);
    }

    /**
     * Schedule the provided function to run asynchronously.
     * @param function The function to run asynchronously.
     * @param <T> The type that will be returned from the asynchronous function.
     * @return A reference to the scheduled function.
     */
    <T> AsyncFunction<T> schedule(Function0<T> function);

    /**
     * Schedule the provided function to run asynchronously and then return the asynchronous
     * execution flow back to the current AsyncRunner.
     * @param function The function to run asynchronously.
     * @param <T> The type that will be returned from the asynchronous function.
     * @return A reference to the scheduled function.
     */
    default <T> AsyncFunction<T> scheduleSingle(Function0<T> function)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return schedule(function).thenOn(currentAsyncRunner);
    }

    /**
     * Schedule the provided action to run asynchronously.
     * @param function The function that will produce an AsyncAction.
     * @return A reference to the scheduled action.
     */
    default AsyncAction scheduleAsyncAction(Function0<AsyncAction> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(Action0.empty)
                   .thenAsyncAction(function);
    }

    /**
     * Schedule the provided function to run asynchronously.
     * @param function The function to run asynchronously.
     * @param <T> The type that will be returned from the asynchronous function.
     * @return A reference to the scheduled function.
     */
    default <T> AsyncFunction<T> scheduleAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(Action0.empty)
                   .thenAsyncFunction(function);
    }

    /**
     * Schedule the provided AsyncTask so that it will be run.
     * @param asyncTask The AsyncTask to schedule.
     */
    void schedule(PausedAsyncTask asyncTask);

    /**
     * Get an action that will run when the provided asyncActions all complete.
     * @param asyncActions The asyncActions that must complete before the returned AsyncAction will
     *                     be run.
     * @return An AsyncAction that will be run when the provided asyncActions all complete.
     */
    default AsyncAction whenAll(AsyncAction... asyncActions)
    {
        PreCondition.assertNotNullAndNotEmpty(asyncActions, "asyncActions");

        final BasicAsyncAction result = new BasicAsyncAction(new Value<>(this), null);
        final int asyncActionsCount = asyncActions.length;
        final java.util.concurrent.atomic.AtomicInteger completedAsyncActions = new java.util.concurrent.atomic.AtomicInteger(0);
        final List<Throwable> errors = new ArrayList<>();
        final Action0 onCompleted = () ->
        {
            completedAsyncActions.incrementAndGet();
            if (completedAsyncActions.get() == asyncActionsCount)
            {
                if (errors.any())
                {
                    result.setIncomingError(ErrorIterable.from(errors));
                }
                result.schedule();
            }
        };
        for (final AsyncAction asyncAction : asyncActions)
        {
            result.addParentTask(asyncAction);
            asyncAction
                .then(onCompleted)
                .catchError((Throwable error) ->
                {
                    errors.add(error);
                    onCompleted.run();
                });
        }
        return result;
    }

    /**
     * Await all of the provided AsyncActions. The AsyncActions will be awaited in the order that
     * they are iterated over in the provided Iterable.
     * @param asyncActions The AsyncActions to await.
     */
    default void awaitAll(AsyncAction... asyncActions)
    {
        if (asyncActions != null && asyncActions.length > 0)
        {
            final List<Throwable> errors = new ArrayList<>();
            for (final AsyncAction asyncAction : asyncActions)
            {
                try
                {
                    asyncAction.await();
                }
                catch (Throwable error)
                {
                    errors.add(error);
                }
            }
            if (errors.any())
            {
                throw ErrorIterable.from(errors);
            }
        }
    }

    /**
     * Create an AsyncFunction that returns a successful Result.
     * @param <T> The type of Result to return.
     * @return An AsyncFunction that returns a successful Result.
     */
    default <T> AsyncFunction<Result<T>> success()
    {
        return done(null, null);
    }

    /**
     * Create an AsyncFunction that returns a successful Result with the provided value.
     * @param value The value to set in the Result.
     * @param <T> The type of the value.
     * @return An AsyncFunction that returns a successful Result with the provided value.
     */
    default <T> AsyncFunction<Result<T>> success(T value)
    {
        return done(Result.success(value));
    }

    /**
     * Create an AsyncFunction that returns an error Result with the provided error.
     * @param error The error.
     * @param <T> The type of value that the result would contain if it were successful.
     * @return An AsyncFunction that returns an error Result with the provided error.
     */
    default <T> AsyncFunction<Result<T>> error(Throwable error)
    {
        return done(Result.<T>error(error));
    }

    /**
     * Create an AsyncFunction that returns a Result with the provided value and error.
     * @param value The value to set in the Result.
     * @param error The error to set in the Result.
     * @param <T> The type of the value.
     * @return An AsyncFunction that returns a Result with the provided value and error.
     */
    default <T> AsyncFunction<Result<T>> done(T value, Throwable error)
    {
        return done(Result.done(value, error));
    }

    /**
     * Create an AsyncFunction that resolves to the provided Result.
     * @param asyncResult The Result to resolve to.
     * @param <T> The type of the Result.
     * @return An AsyncFunction that resolves to the provided Result.
     */
    default <T> AsyncFunction<Result<T>> done(Result<T> asyncResult)
    {
        final BasicAsyncFunction<Result<T>> result = new BasicAsyncFunction<>(new Value<>(this), null);
        result.markCompleted();
        result.setFunctionResult(asyncResult);
        return result;
    }

    /**
     * Create an AsyncFunction that returns an error Result if the provided value is null.
     * @param value The value to check.
     * @param parameterName The name of the parameter that the value comes from.
     * @param <T> The type of the Result that the return value must match.
     * @return An AsyncFunction if the provided value is null, or null if the provided value is not
     * null.
     */
    default <T> AsyncFunction<Result<T>> notNull(Object value, String parameterName)
    {
        final Result<T> innerResult = Result.notNull(value, parameterName);
        return innerResult == null ? null : done(innerResult);
    }


    /**
     * Create an AsyncFunction that returns an error Result if the provided values are not equal.
     * @param expectedValue The expected value.
     * @param actualValue The actual value.
     * @param parameterName The name of the parameter that contains the provided value.
     * @param <T> The type of the values being compared.
     * @param <U> The type that is contained within the returned Result.
     * @return An AsyncFunction if the provided values are not equal, or null if the provided values
     * are equal.
     */
    default <T,U> AsyncFunction<Result<U>> equal(T expectedValue, T actualValue, String parameterName)
    {
        final Result<U> innerResult = Result.equal(expectedValue, actualValue, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    /**
     * Create an AsyncFunction that returns an error Result if the provided value is not between the
     * provided lowerBound and upperBound (inclusive).
     * @param lowerBound The lower bound value.
     * @param value The value to check.
     * @param upperBound The upper bound value.
     * @param parameterName The name of the parameter that the value comes from.
     * @param <T> The type of the Result that the return value must match.
     * @return An AsyncFunction if the provided value is not between the lower and upper bounds, or
     * null if the provided value is between the lower and upper bounds.
     */
    default <T> AsyncFunction<Result<T>> between(int lowerBound, int value, int upperBound, String parameterName)
    {
        final Result<T> innerResult = Result.between(lowerBound, value, upperBound, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    /**
     * Create an AsyncFunction that returns an error Result if the provided value is not greater
     * than the provided lowerBound.
     * @param lowerBound The lower bound value.
     * @param value The value to check.
     * @param parameterName The name of the parameter that the value comes from.
     * @param <T> The type of the Result that the return value must match.
     * @return An AsyncFunction if the provided value is not greater than the lower bound, or null
     * if the provided value is greater than the lower bound.
     */
    default <T> AsyncFunction<Result<T>> greaterThan(int lowerBound, int value, String parameterName)
    {
        final Result<T> innerResult = Result.greaterThan(lowerBound, value, parameterName);
        return innerResult == null ? null : done(innerResult);
    }
}
