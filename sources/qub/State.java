package qub;

/**
 * A single state within a DFA.
 */
public class State
{
    private final String name;
    private boolean startState;
    private boolean endState;
    private boolean trackValues;
    private final List<StateTransition> transitions;
    private final Set<State> instantNextStates;

    /**
     * Create a new State with the provided name.
     * @param name The name of this new State.
     */
    public State(String name)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.name = name;
        this.transitions = List.create();
        this.instantNextStates = Set.create();
    }

    /**
     * Get the name of this State.
     * @return The name of this State.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get whether or not this State is a start state.
     * @return Whether or not this State is a start state.
     */
    public boolean isStartState()
    {
        return startState;
    }

    /**
     * Set whether or not this State is a start state.
     * @param startState Whether or not this State is a start state.
     * @return This State for method chaining.
     */
    public State setStartState(boolean startState)
    {
        this.startState = startState;
        return this;
    }

    /**
     * Get whether or not this State is an end state.
     * @return Whether or not this State is an end state.
     */
    public boolean isEndState()
    {
        return endState;
    }

    /**
     * Set whether or not this State is an end state.
     * @param endState Whether or not this State is an end state.
     * @return This State for method chaining.
     */
    public State setEndState(boolean endState)
    {
        this.endState = endState;
        return this;
    }

    /**
     * Get whether or not the values that occur while in this State should be tracked.
     * @return Whether or not the values that occur while in this State should be tracked.
     */
    public boolean shouldTrackValues()
    {
        return trackValues;
    }

    /**
     * Set whether or not the values that occur while in this State should be tracked.
     * @param trackValues Whether or not the values that occur while in this State should be
     *                    tracked.
     * @return This State for method chaining.
     */
    public State trackValues(boolean trackValues)
    {
        this.trackValues = trackValues;
        return this;
    }

    /**
     * Get the State that this State will transition to when it encounters the provided value.
     * @param value The value that initiates the transition.
     * @return The State that this State will transition to when it encounters the provided value.
     */
    public Iterable<State> getNextStates(char value)
    {
        return transitions
            .where((StateTransition transition) -> transition.matches(value))
            .map(StateTransition::getNextState);
    }

    /**
     * Add a transition from this State to the provided nextState when the provided value is
     * encountered.
     * @param value The value that initiates the transition from this State to the provided
     *              nextState.
     * @param nextState The State to transition to.
     * @return This State for method chaining.
     */
    public State addNextState(char value, State nextState)
    {
        PreCondition.assertNotNull(nextState, "nextState");

        return addNextState(Range.between(value, value), nextState);
    }

    /**
     * Add a transition from this State to the provided nextState when the provided value is
     * encountered.
     * @param valueRange The range of values that initiates the transition from this State to the
     *                   provided nextState.
     * @param nextState The State to transition to.
     * @return This State for method chaining.
     */
    public State addNextState(Range<Character> valueRange, State nextState)
    {
        PreCondition.assertNotNull(valueRange, "valueRange");
        PreCondition.assertNotNull(nextState, "nextState");

        return addNextState(valueRange::contains, nextState);
    }

    /**
     * Add a transition from this State to the provided nextState when the provided condition is
     * encountered.
     * @param condition The condition that initiates the transition from this State to the provided
     *                  nextState.
     * @param nextState The State to transition to.
     * @return This State for method chaining.
     */
    public State addNextState(Function1<Character,Boolean> condition, State nextState)
    {
        PreCondition.assertNotNull(condition, "valueRange");
        PreCondition.assertNotNull(nextState, "nextState");

        transitions.add(new StateTransition(condition, nextState));

        return this;
    }

    /**
     * Get the next States that this State instantly transitions to when this State is visited.
     * @return The next States that this State instantly transitions to when this State is visited.
     */
    public Iterable<State> getInstantNextStates()
    {
        return instantNextStates;
    }

    /**
     * Add a next State that this State will instantly transition to when this State is visited.
     * @param instantNextState The next State that this State will instantly transition to when this
     *                         State is visited.
     * @return This State for method chaining.
     */
    public State addInstantNextState(State instantNextState)
    {
        PreCondition.assertNotNull(instantNextState, "instantNextState");

        instantNextStates.add(instantNextState);
        return this;
    }
}
