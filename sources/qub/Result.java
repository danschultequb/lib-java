package qub;

public interface Result<T>
{
    /**
     * Get whether or not this Result has completed running.
     * @return Whether or not this Result has completed running.
     */
    boolean isCompleted();

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
    default Result<Void> then(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.then((T parentValue) ->
        {
            action.run();
            return null;
        });
    }

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    default Result<Void> then(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.then((T parentValue) ->
        {
            action.run(parentValue);
            return null;
        });
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    default <U> Result<U> then(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.then((T parentValue) ->
        {
            return function.run();
        });
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    <U> Result<U> then(Function1<T,U> function);

    /**
     * If this result doesn't have an error, then run the provided action and return this Result
     * object.
     * @param action The action to run if this result does not have an error.
     * @return This Result object.
     */
    default Result<T> onValue(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.onValue((T parentValue) ->
        {
            action.run();
        });
    }

    /**
     * If this result doesn't have an error, then run the provided action and return this Result
     * object.
     * @param action The action to run if this result does not have an error.
     * @return This Result object.
     */
    default Result<T> onValue(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.then((T parentValue) ->
        {
            action.run(parentValue);
            return parentValue;
        });
    }

    /**
     * If this Result has an error of the provided type, then catch that error and return a
     * successful Result instead.
     */
    default Result<T> catchError()
    {
        return this.catchError(Action0.empty);
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    default Result<T> catchError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.catchError(Throwable.class, action);
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    default Result<T> catchError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.catchError(Throwable.class, (Throwable parentError) ->
        {
            action.run(Exceptions.unwrap(parentError));
        });
    }

    /**
     * If this Result has an error of the provided type, then catch that error and return a
     * successful Result instead.
     * @param errorType The type of error to catch.
     * @param <TError> The type of error to catch.
     * @return This Result if it is successful, or an empty successful Result if this Result
     * contains an error of the provided type.
     */
    default <TError extends Throwable> Result<T> catchError(Class<TError> errorType)
    {
        PreCondition.assertNotNull(errorType, "errorType");

        return this.catchError(errorType, Action0.empty);
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    default <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return this.catchError(errorType, (TError parentError) ->
        {
            action.run();
        });
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    default <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return this.catchError(errorType, (TError parentError) ->
        {
            action.run(parentError);
            return null;
        });
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    default Result<T> catchError(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.catchError(Throwable.class, function);
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    default Result<T> catchError(Function1<Throwable,T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.catchError(Throwable.class, (Throwable parentError) ->
        {
            return function.run(Exceptions.unwrap(parentError));
        });
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    default <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function0<T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return this.catchError(errorType, (TError parentError) -> function.run());
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function1<TError,T> function);

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    default Result<T> onError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.onError((Throwable error) ->
        {
            action.run();
        });
    }

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    default Result<T> onError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.onError(Throwable.class, (Throwable parentError) ->
        {
            action.run(Exceptions.unwrap(parentError));
        });
    }

    /**
     * Run the provided action if this Result has an error of the provided type.
     * @param errorType The type of error to run the provided action for.
     * @param action The action to run if this Result has an error of the provided type.
     * @return This Result with its error.
     */
    default <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return this.onError(errorType, (TError error) ->
        {
            action.run();
        });
    }

    /**
     * Run the provided action if this Result has an error of the provided type.
     * @param errorType The type of error to run the provided action for.
     * @param action The action to run if this Result has an error of the provided type.
     * @return This Result with its error.
     */
    default <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return this.catchError(Throwable.class, (Throwable parentError) ->
        {
            final TError expectedError = Exceptions.getInstanceOf(parentError, errorType);
            if (expectedError != null)
            {
                action.run(expectedError);
            }
            throw Exceptions.asRuntime(parentError);
        });
    }

    /**
     * If this Result has an error, catch it and convert it to the error returned by the provided
     * function.
     * @param function The function that will return the new error.
     * @return The Result with the new error.
     */
    default Result<T> convertError(Function0<? extends Throwable> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.catchError(() ->
        {
            throw Exceptions.asRuntime(function.run());
        });
    }

    /**
     * If this Result has an error, catch it and convert it to the error returned by the provided
     * function.
     * @param function The function that will return the new error.
     * @return The Result with the new error.
     */
    default Result<T> convertError(Function1<Throwable,? extends Throwable> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.catchError((Throwable error) ->
        {
            throw Exceptions.asRuntime(function.run(Exceptions.unwrap(error)));
        });
    }

    /**
     * If this Result has an error of the provided errorType, catch it and convert it to the error
     * returned by the provided function.
     * @param function The function that will return the new error.
     * @return The Result with the new error.
     */
    default Result<T> convertError(Class<? extends Throwable> errorType, Function0<? extends Throwable> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return this.catchError(errorType, () ->
        {
            throw Exceptions.asRuntime(function.run());
        });
    }

    /**
     * If this Result has an error of the provided errorType, catch it and convert it to the error
     * returned by the provided function.
     * @param function The function that will return the new error.
     * @param <TErrorIn> The type of error that was caught.
     * @return The Result with the new error.
     */
    default <TErrorIn extends Throwable> Result<T> convertError(Class<TErrorIn> errorType, Function1<TErrorIn,? extends Throwable> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return this.catchError(errorType, (TErrorIn error) ->
        {
            throw Exceptions.asRuntime(function.run(error));
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
     * Create a new Result by synchronously running the provided action with the provided disposable
     * value. When the action is completed, the disposable will be disposed.
     * @param disposableFunction The function that will provided the disposable to dispose when the
     *                           action is completed.
     * @param action The action to run.
     * @param <T> The type of the disposable.
     * @return The new result.
     */
    static <T extends Disposable> Result<Void> createUsing(Function0<T> disposableFunction, Action1<T> action)
    {
        PreCondition.assertNotNull(disposableFunction, "disposableFunction");
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            try (T disposable = disposableFunction.run())
            {
                action.run(disposable);
            }
        });
    }

    /**
     * Create a new Result by synchronously running the provided action with the provided disposable
     * value. When the action is completed, the disposable will be disposed.
     * @param disposableFunction1 The first function that will provide the disposable to dispose
     *                            when the action is completed.
     * @param disposableFunction2 The second function that will provide the disposable to dispose
     *                            when the action is completed.
     * @param action The action to run.
     * @param <T> The first type of the disposable.
     * @param <U> The second type of the disposable.
     * @return The new result.
     */
    static <T extends Disposable,U extends Disposable> Result<Void> createUsing(Function0<T> disposableFunction1, Function0<U> disposableFunction2, Action2<T,U> action)
    {
        PreCondition.assertNotNull(disposableFunction1, "disposableFunction1");
        PreCondition.assertNotNull(disposableFunction2, "disposableFunction2");
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            try (final T disposable1 = disposableFunction1.run())
            {
                try (final U disposable2 = disposableFunction2.run())
                {
                    action.run(disposable1, disposable2);
                }
            }
        });
    }

    /**
     * Create a new Result by synchronously running the provided action with the provided disposable
     * value. When the action is completed, the disposable will be disposed.
     * @param disposableFunction The function that will provided the disposable to dispose when the
     *                           action is completed.
     * @param function The action to run.
     * @param <T> The type of the disposable.
     * @return The new result.
     */
    static <T extends Disposable,U> Result<U> createUsing(Function0<T> disposableFunction, Function1<T,U> function)
    {
        PreCondition.assertNotNull(disposableFunction, "disposableFunction");
        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            U result;
            try (T disposable = disposableFunction.run())
            {
                result = function.run(disposable);
            }
            return result;
        });
    }

    /**
     * Create a new Result by synchronously running the provided action with the provided disposable
     * value. When the action is completed, the disposable will be disposed.
     * @param disposableFunction1 The first function that will provide the disposable to dispose
     *                            when the action is completed.
     * @param disposableFunction2 The second function that will provide the disposable to dispose
     *                            when the action is completed.
     * @param function The function to run.
     * @param <T> The first type of the disposable.
     * @param <U> The second type of the disposable.
     * @return The new result.
     */
    static <T extends Disposable,U extends Disposable,V> Result<V> createUsing(Function0<T> disposableFunction1, Function0<U> disposableFunction2, Function2<T,U,V> function)
    {
        PreCondition.assertNotNull(disposableFunction1, "disposableFunction1");
        PreCondition.assertNotNull(disposableFunction2, "disposableFunction2");
        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            V result;
            try (final T disposable1 = disposableFunction1.run())
            {
                try (final U disposable2 = disposableFunction2.run())
                {
                    result = function.run(disposable1, disposable2);
                }
            }
            return result;
        });
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

    /**
     * Await all of the provided Result objects.
     * @param resultsToAwait The Result objects to await.
     */
    @SafeVarargs
    static <T,RT extends Result<T>> Iterable<T> await(RT... resultsToAwait)
    {
        PreCondition.assertNotNull(resultsToAwait, "resultsToAwait");

        return Result.await(Iterable.create(resultsToAwait));
    }

    /**
     * Await all of the Result objects in the provided Iterable.
     * @param resultsToAwait The Result objects to await.
     */
    static <T,RT extends Result<T>> Iterable<T> await(Iterable<RT> resultsToAwait)
    {
        PreCondition.assertNotNull(resultsToAwait, "resultsToAwait");

        final List<T> result = List.create();
        for (final Result<T> resultToAwait : resultsToAwait)
        {
            result.add(resultToAwait.await());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultsToAwait.getCount(), result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Await all of the Result objects in the provided Indexable.
     * @param resultsToAwait The Result objects to await.
     */
    static <T,RT extends Result<T>> Indexable<T> await(Indexable<RT> resultsToAwait)
    {
        PreCondition.assertNotNull(resultsToAwait, "resultsToAwait");

        final List<T> result = List.create();
        for (final Result<T> resultToAwait : resultsToAwait)
        {
            result.add(resultToAwait.await());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultsToAwait.getCount(), result.getCount(), "result.getCount()");

        return result;
    }

    static <T> String toString(Result<T> result)
    {
        PreCondition.assertNotNull(result, "result");

        final CharacterList builder = CharacterList.create();
        builder.add('{');

        builder.addAll("\"type\":");
        builder.addAll(Strings.escapeAndQuote(Types.getTypeName(result)));
        builder.add(',');

        final boolean isCompleted = result.isCompleted();
        builder.addAll("\"isCompleted\":");
        builder.addAll(Booleans.toString(isCompleted));
        if (isCompleted)
        {
            builder.add(',');
            result
                .then((T value) ->
                {
                    builder.addAll("\"value\":");
                    builder.addAll(Strings.escapeAndQuote(value));
                })
                .catchError((Throwable error) ->
                {
                    builder.addAll("\"error\":");
                    builder.addAll(Strings.escapeAndQuote(error));
                })
                .await();
        }

        builder.add('}');

        return builder.toString(true);
    }
}
