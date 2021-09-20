package qub;

/**
 * A type that can be used to run an action.
 */
public interface Retry
{
    /**
     * Run the provided action. If the action throws an exception, the exception will be caught and
     * the action will be run again up to 2 additional times.
     * @param action The action to run and possibly retry.
     * @return The result of running the provided action.
     */
    static Result<Void> run(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return Retry.run(() ->
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
    static <T> Result<T> run(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            T result = null;

            boolean done = false;
            final List<Throwable> exceptions = List.create();
            while (!done && exceptions.getCount() < 3)
            {
                try
                {
                    result = function.run();
                    done = true;
                }
                catch (Throwable e)
                {
                    exceptions.add(e);
                }
            }

            if (!done)
            {
                throw ErrorIterable.create(exceptions);
            }

            return result;
        });
    }
}
