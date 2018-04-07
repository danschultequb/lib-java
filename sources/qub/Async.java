package qub;

public class Async
{
    Async()
    {
    }

    public static <T> AsyncFunction<Result<T>> success(AsyncRunner asyncRunner)
    {
        return done(asyncRunner, null, null);
    }

    public static <T> AsyncFunction<Result<T>> success(AsyncRunner asyncRunner, final T value)
    {
        return done(asyncRunner, value, null);
    }

    public static <T> AsyncFunction<Result<T>> error(AsyncRunner asyncRunner, final Throwable error)
    {
        return done(asyncRunner, null, error);
    }

    private static <T> AsyncFunction<Result<T>> done(AsyncRunner asyncRunner, final T value, final Throwable error)
    {
        return asyncRunner.schedule(new Function0<Result<T>>()
        {
            @Override
            public Result<T> run()
            {
                return Result.done(value, error);
            }
        });
    }

    /**
     * Get an AsyncFunction that completes when all of the provided tasks are complated.
     * @param asyncRunner The AsyncRunner to run the returned AsyncFunction on when all the provided
     *                    tasks complete.
     * @param tasks The tasks to wait on.
     * @return An AsyncFunction that completes when all of the provided tasks are completed.
     */
    public static <T> AsyncFunction<Result<T>> whenAll(final AsyncRunner asyncRunner, Iterable<AsyncFunction<Result<T>>> tasks)
    {
        AsyncFunction<Result<T>> result;
        if (tasks == null || !tasks.any())
        {
            result = Async.success(asyncRunner);
        }
        else
        {
            final Iterator<AsyncFunction<Result<T>>> tasksIterator = tasks.iterate();
            tasksIterator.ensureHasStarted();

            result = whenAll(asyncRunner, tasksIterator, new ArrayList<Throwable>());
        }
        return result;
    }

    private static <T> AsyncFunction<Result<T>> whenAll(final AsyncRunner asyncRunner, final Iterator<AsyncFunction<Result<T>>> tasksIterator, final List<Throwable> collectedErrors)
    {
        AsyncFunction<Result<T>> result;
        if (!tasksIterator.hasCurrent())
        {
            result = Async.error(asyncRunner, ErrorIterable.from(collectedErrors));
        }
        else
        {
            final AsyncFunction<Result<T>> task = tasksIterator.takeCurrent();
            result = task.thenAsyncFunction(new Function1<Result<T>, AsyncFunction<Result<T>>>()
            {
                @Override
                public AsyncFunction<Result<T>> run(Result<T> arg1)
                {
                    return whenAll(asyncRunner, tasksIterator, collectedErrors);
                }
            });
        }
        return result;
    }

    public static <T> AsyncFunction<Result<Iterable<T>>> merge(AsyncRunner asyncRunner, Iterable<AsyncFunction<Result<Iterable<T>>>> asyncFunctions)
    {
        return Async.merge(asyncRunner, asyncFunctions.iterate());
    }

    public static <T> AsyncFunction<Result<Iterable<T>>> merge(AsyncRunner asyncRunner, Iterator<AsyncFunction<Result<Iterable<T>>>> asyncFunctionsIterator)
    {
        asyncFunctionsIterator.ensureHasStarted();

        return Async.merge(asyncRunner, asyncFunctionsIterator, new ArrayList<T>(), new ArrayList<Throwable>());
    }

    static <T> AsyncFunction<Result<Iterable<T>>> merge(final AsyncRunner asyncRunner, final Iterator<AsyncFunction<Result<Iterable<T>>>> asyncFunctionsIterator, final List<T> collectedValues, final List<Throwable> collectedErrors)
    {
        AsyncFunction<Result<Iterable<T>>> result;
        if (!asyncFunctionsIterator.hasCurrent())
        {
            result = Async.done(asyncRunner, (Iterable<T>)collectedValues, ErrorIterable.from(collectedErrors));
        }
        else
        {
            result = asyncFunctionsIterator.takeCurrent().thenAsyncFunction(new Function1<Result<Iterable<T>>, AsyncFunction<Result<Iterable<T>>>>()
            {
                @Override
                public AsyncFunction<Result<Iterable<T>>> run(Result<Iterable<T>> result)
                {
                    final Throwable error = result.getError();
                    if (error != null)
                    {
                        collectedErrors.add(error);
                    }

                    final Iterable<T> values = result.getValue();
                    if (values != null && values.any())
                    {
                        collectedValues.addAll(values);
                    }

                    return Async.merge(asyncRunner, asyncFunctionsIterator, collectedValues, collectedErrors);
                }
            });
        }
        return result;
    }
}
