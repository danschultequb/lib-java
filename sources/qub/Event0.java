package qub;

/**
 * An event that can be triggered.
 */
public interface Event0
{
    /**
     * Add the provided action to this event so that when the event is triggered the action will be
     * run.
     * @param action The action to run when the event is triggered.
     */
    void subscribe(Action0 action);
}
