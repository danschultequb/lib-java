package qub;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AsyncRunnerBase extends DisposableBase implements AsyncRunner
{
    @Override
    public AsyncAction schedule(Action0 action)
    {
        return schedule(null, action);
    }

    @Override
    public AsyncAction scheduleAsyncAction(Function0<AsyncAction> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(Actions.empty)
            .thenAsyncAction(function);
    }

    @Override
    public <T> AsyncFunction<T> scheduleAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(Actions.empty)
            .thenAsyncFunction(function);
    }

    @Override
    public AsyncAction whenAll(AsyncAction... asyncActions)
    {
        PreCondition.assertNotNullAndNotEmpty(asyncActions, "asyncActions");

        final BasicAsyncAction result = new BasicAsyncAction(new Value<AsyncRunner>(this), null);
        final int asyncActionsCount = asyncActions.length;
        final AtomicInteger completedAsyncActions = new AtomicInteger(0);
        final List<Throwable> errors = new ArrayList<>();
        final Action0 onCompleted = new Action0()
        {
            @Override
            public void run()
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
            }
        };
        for (final AsyncAction asyncAction : asyncActions)
        {
            result.addParentTask(asyncAction);
            asyncAction
                .then(onCompleted)
                .catchError(new Action1<Throwable>()
                {
                    @Override
                    public void run(Throwable error)
                    {
                        errors.add(error);
                        onCompleted.run();
                    }
                });
        }
        return result;
    }

    @Override
    public void awaitAll(AsyncAction... asyncActions)
    {
        if (asyncActions != null && asyncActions.length > 0)
        {
            final List<Throwable> errors = new ArrayList<Throwable>();
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

    @Override
    public <T> AsyncFunction<Result<T>> success()
    {
        return done(null, null);
    }

    @Override
    public <T> AsyncFunction<Result<T>> success(T value)
    {
        return done(Result.success(value));
    }

    @Override
    public <T> AsyncFunction<Result<T>> error(Throwable error)
    {
        return done(Result.<T>error(error));
    }

    @Override
    public <T> AsyncFunction<Result<T>> done(T value, Throwable error)
    {
        return done(Result.done(value, error));
    }

    @Override
    public <T> AsyncFunction<Result<T>> done(Result<T> asyncResult)
    {
        final BasicAsyncFunction<Result<T>> result = new BasicAsyncFunction<>(new Value<AsyncRunner>(this), null);
        result.markCompleted();
        result.setFunctionResult(asyncResult);
        return result;
    }

    @Override
    public <T> AsyncFunction<Result<T>> notNull(Object value, String parameterName)
    {
        final Result<T> innerResult = Result.notNull(value, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    @Override
    public <T,U> AsyncFunction<Result<U>> equal(T expectedValue, T actualValue, String parameterName)
    {
        final Result<U> innerResult = Result.equal(expectedValue, actualValue, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    @Override
    public <T> AsyncFunction<Result<T>> between(int lowerBound, int value, int upperBound, String parameterName)
    {
        final Result<T> innerResult = Result.between(lowerBound, value, upperBound, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    @Override
    public <T> AsyncFunction<Result<T>> greaterThan(int lowerBound, int value, String parameterName)
    {
        final Result<T> innerResult = Result.greaterThan(lowerBound, value, parameterName);
        return innerResult == null ? null : done(innerResult);
    }
}
