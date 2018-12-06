package qub;

/**
 * An individual state within a larger Pattern.
 */
public class PatternState
{
    private final List<Transition> transitions;
    private boolean isEndState;

    public PatternState()
    {
        transitions = new SingleLinkList<>();
    }

    /**
     * Get whether or not this PatternState is an end state.
     * @return Whether or not this PatternState is an end state.
     */
    public boolean isEndState()
    {
        return isEndState;
    }

    /**
     * Set whether or not this PatternState is an end state.
     * @param isEndState Whether or not this PatternState is an end state.
     */
    public void setIsEndState(boolean isEndState)
    {
        this.isEndState = isEndState;
    }

    /**
     * Add a transition with the provided condition and next PatternState.
     * @param condition The condition for the transition.
     * @param nextState The next state to go to if the condition is satisfied.
     */
    public void addTransition(TransitionCondition condition, PatternState nextState)
    {
        transitions.add(new Transition(condition, nextState));
    }

    /**
     * Get an iterator to the next states after the provided character is encountered.
     * @param character The next character in the input stream.
     * @return An iterator to the next states that the state machine is in.
     */
    public Iterable<PatternState> getNextStates(final char character)
    {
        return transitions.where(new Function1<Transition, Boolean>()
        {
            @Override
            public Boolean run(Transition transition)
            {
                return transition.matchesCondition(character);
            }
        })
        .map(new Function1<Transition,PatternState>()
        {
            @Override
            public PatternState run(Transition transition)
            {
                return transition.getNextState();
            }
        });
    }
}
