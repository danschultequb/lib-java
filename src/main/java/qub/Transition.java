package qub;

/**
 * A transition from one state to another within a State machine.
 */
public class Transition
{
    private final TransitionCondition condition;
    private final State nextState;

    public Transition(TransitionCondition condition, State nextState)
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
    public State getNextState()
    {
        return nextState;
    }
}
