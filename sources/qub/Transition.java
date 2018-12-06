package qub;

/**
 * A transition from one state to another within a PatternState machine.
 */
public class Transition
{
    private final TransitionCondition condition;
    private final PatternState nextState;

    public Transition(TransitionCondition condition, PatternState nextState)
    {
        this.condition = condition;
        this.nextState = nextState;
    }

    /**
     * Get whether or not the provided character matches this Transition's condition.
     * @param character The next character from the input stream.
     * @return Whether or not the provided character matches this Transition's condition.
     */
    public boolean matchesCondition(char character)
    {
        return condition.matches(character);
    }

    /**
     * Get the state that the state machine will move to if this Transition's condition is
     * satisfied.
     * @return The state that the state machine will move to if this Transition's condition is
     * satisfied.
     */
    public PatternState getNextState()
    {
        return nextState;
    }
}
