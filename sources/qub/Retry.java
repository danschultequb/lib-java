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

        return Result.create(() ->
        {
            action.run();
        });
    }
}
