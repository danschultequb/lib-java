package qub;

/**
 * A type that can be used to attempt a function multiple times.
 */
public class Retry
{
    private Function2<Integer,Throwable,Boolean> shouldRetryFunction;

    private Retry()
    {
        this.setShouldRetryFunction(3);
    }

    /**
     * Create a new {@link Retry} object.
     * @return The new {@link Retry} object.
     */
    public static Retry create()
    {
        return new Retry();
    }

    /**
     * Set the function that determines whether another attempt should be made based on whether the
     * number of failed attempts is less than the provided maximum attempts.
     * @param maximumAttempts The maximum number of attempts that can be made.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(int maximumAttempts)
    {
        PreCondition.assertGreaterThanOrEqualTo(maximumAttempts, 1, "maximumAttempts");

        return this.setShouldRetryFunction(maximumAttempts, Action0.empty);
    }

    /**
     * Set the function that determines whether another attempt should be made based on whether the
     * number of failed attempts is less than the provided maximum attempts.
     * @param maximumAttempts The maximum number of attempts that can be made.
     * @param betweenAttemptsAction The action that will be run between each attempts.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(int maximumAttempts, Action0 betweenAttemptsAction)
    {
        PreCondition.assertGreaterThanOrEqualTo(maximumAttempts, 1, "maximumAttempts");
        PreCondition.assertNotNull(betweenAttemptsAction, "betweenAttemptsAction");

        return this.setShouldRetryFunction(maximumAttempts, (Integer failedAttempts) -> betweenAttemptsAction.run());
    }

    /**
     * Set the function that determines whether another attempt should be made based on whether the
     * number of failed attempts is less than the provided maximum attempts.
     * @param maximumAttempts The maximum number of attempts that can be made.
     * @param betweenAttemptsAction The action that will be run between each attempts.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(int maximumAttempts, Action1<Integer> betweenAttemptsAction)
    {
        PreCondition.assertGreaterThanOrEqualTo(maximumAttempts, 1, "maximumAttempts");
        PreCondition.assertNotNull(betweenAttemptsAction, "betweenAttemptsAction");

        return this.setShouldRetryFunction(maximumAttempts, (Integer failedAttempts, Throwable error) -> betweenAttemptsAction.run(failedAttempts));
    }

    /**
     * Set the function that determines whether another attempt should be made based on whether the
     * number of failed attempts is less than the provided maximum attempts.
     * @param maximumAttempts The maximum number of attempts that can be made.
     * @param betweenAttemptsAction The action that will be run between each attempts.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(int maximumAttempts, Action2<Integer,Throwable> betweenAttemptsAction)
    {
        PreCondition.assertGreaterThanOrEqualTo(maximumAttempts, 1, "maximumAttempts");
        PreCondition.assertNotNull(betweenAttemptsAction, "betweenAttemptsAction");

        return this.setShouldRetryFunction((Integer failedAttempts, Throwable error) ->
            {
                final boolean result = (failedAttempts < maximumAttempts);
                if (result)
                {
                    betweenAttemptsAction.run(failedAttempts, error);
                }
                return result;
            });
    }

    /**
     * Set the function that determines whether another attempt should be made.
     * @param shouldRetryFunction The function that determines whether another attempt should be
     *                            made.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(Function0<Boolean> shouldRetryFunction)
    {
        PreCondition.assertNotNull(shouldRetryFunction, "shouldRetryFunction");

        return this.setShouldRetryFunction((Integer failedAttempts) -> shouldRetryFunction.run());
    }

    /**
     * Set the function that determines whether another attempt should be made.
     * @param shouldRetryFunction The function that determines whether another attempt should be
     *                            made.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(Function1<Integer,Boolean> shouldRetryFunction)
    {
        PreCondition.assertNotNull(shouldRetryFunction, "shouldRetryFunction");

        return this.setShouldRetryFunction((Integer failedAttempts, Throwable error) -> shouldRetryFunction.run(failedAttempts));
    }

    /**
     * Set the function that determines whether another attempt should be made.
     * @param shouldRetryFunction The function that determines whether another attempt should be
     *                            made.
     * @return This object for method chaining.
     */
    public Retry setShouldRetryFunction(Function2<Integer,Throwable,Boolean> shouldRetryFunction)
    {
        PreCondition.assertNotNull(shouldRetryFunction, "shouldRetryFunction");

        this.shouldRetryFunction = shouldRetryFunction;

        return this;
    }

    /**
     * Run the provided action. If the action throws an exception, the exception will be caught and
     * the action will be run again up to 2 additional times.
     * @param action The action to run and possibly retry.
     * @return The result of running the provided action.
     */
    public Result<Void> run(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.run(() ->
        {
            action.run();
            return null;
        });
    }

    /**
     * Run the provided function. If the function throws an exception, the exception will be caught
     * and the function will be run again up to 2 additional times.
     * @param function The function to run and possibly retry.
     * @return The result of running the provided function.
     */
    public <T> Result<T> run(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            T result;

            List<Throwable> exceptions = null;
            while (true)
            {
                try
                {
                    result = function.run();
                    break;
                }
                catch (Throwable e)
                {
                    if (exceptions == null)
                    {
                        exceptions = List.create();
                    }
                    exceptions.add(e);

                    final Boolean shouldRetry = this.shouldRetryFunction.run(exceptions.getCount(), e);
                    if (!Booleans.isTrue(shouldRetry))
                    {
                        throw ErrorIterable.create(exceptions);
                    }
                }
            }

            return result;
        });
    }
}
