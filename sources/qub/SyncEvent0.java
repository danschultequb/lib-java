package qub;

public class SyncEvent0 implements Event0
{
    private final List<Action0> actions;

    public SyncEvent0()
    {
        actions = new ArrayList<>();
    }


    @Override
    public void subscribe(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        actions.add(action);
    }

    /**
     * Run the actions that have subscribed to this event.
     */
    public void run()
    {
        for (final Action0 action : actions)
        {
            action.run();
        }
    }
}
