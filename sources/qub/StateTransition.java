package qub;

public class StateTransition
{
    private final Function1<Character,Boolean> condition;
    private final State nextState;

    public StateTransition(Function1<Character,Boolean> condition, State nextState)
    {
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertNotNull(nextState, "nextState");

        this.condition = condition;
        this.nextState = nextState;
    }

    public State getNextState()
    {
        return nextState;
    }

    public boolean matches(char value)
    {
        return condition.run(value);
    }
}
