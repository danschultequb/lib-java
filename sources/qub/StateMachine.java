package qub;

/**
 * A Deterministic Finite-state Automaton, also known as a PatternState Machine.
 */
public class StateMachine
{
    private final List<State> states;

    /**
     * Create a new StateMachine.
     */
    public StateMachine()
    {
        this.states = List.create();
    }

    /**
     * Get the number of states in this StateMachine.
     * @return The number of states in this StateMachine.
     */
    public int getCount()
    {
        return states.getCount();
    }

    /**
     * Get the States in this StateMachine that have been marked as start states.
     * @return The States in this StateMachine that have been marked as start states.
     */
    public Iterable<State> getStartStates()
    {
        return states.where(State::isStartState);
    }

    /**
     * Get whether or not a state exists in this StateMachine with the provided stateName.
     * @param stateName The name of the State to look for.
     * @return Whether or not a state exists in this StateMachine with the provided stateName.
     */
    public boolean containsState(String stateName)
    {
        PreCondition.assertNotNullAndNotEmpty(stateName, "stateName");

        return states.contains((State state) -> state.getName().equalsIgnoreCase(stateName));
    }

    /**
     * Create a new State in this StateMachine.
     * @return The newly created State.
     */
    public State createState()
    {
        int value = 1;
        String stateName = Integers.toString(value);
        while (containsState(Integers.toString(value)))
        {
            ++value;
            stateName = Integers.toString(value);
        }
        return createState(stateName);
    }

    /**
     * Create a new state in this StateMachine with the provided name.
     * @param stateName The name of the new state.
     * @return The newly created State.
     */
    public State createState(String stateName)
    {
        PreCondition.assertNotNullAndNotEmpty(stateName, "stateName");

        final State result = new State(stateName);
        states.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get whether or not the provided value is a match for this StateMachine.
     * @param value The input value.
     * @return Whether or not the provided value is a match for this StateMachine.
     */
    public boolean isMatch(String value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return isMatch(Strings.iterate(value));
    }

    /**
     * Get whether or not the provided values are a match for this StateMachine.
     * @param values The input value.
     * @return Whether or not the provided value is a match for this StateMachine.
     */
    public boolean isMatch(Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return isMatch(values.iterate());
    }

    /**
     * Get whether or not this StateMachine isMatch the provided input values.
     * @param values The input values.
     * @return Whether or not this StateMachine isMatch the provided input values.
     */
    public boolean isMatch(Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return getMatches(values).any();
    }

    /**
     * Get the matches create this StateMachine for the provided input values.
     * @param value The input value.
     * @return The matches create this StateMachine for the provided input values.
     */
    public Iterable<Match> getMatches(String value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return getMatches(Strings.iterate(value));
    }

    /**
     * Get the matches create this StateMachine for the provided input values.
     * @param values The input values.
     * @return The matches create this StateMachine for the provided input values.
     */
    public Iterable<Match> getMatches(Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return getMatches(values.iterate());
    }

    private Iterable<PossibleMatch> getStartStateMatches(int startIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(startIndex, 0, "startIndex");

        return getStartStates().map((State startState) -> new StringMatch(startIndex, startState));
    }

    private static Iterable<PossibleMatch> resolveInstantTransitions(Iterable<PossibleMatch> matches)
    {
        PreCondition.assertNotNull(matches, "matches");

        final List<PossibleMatch> result = List.create(matches);
        for (final PossibleMatch match : matches)
        {
            result.addAll(match.resolveInstantTransitions());
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the matches create this StateMachine for the provided input values.
     * @param values The input values.
     * @return The matches create this StateMachine for the provided input values.
     */
    public Iterable<Match> getMatches(Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        values.start();

        Iterable<PossibleMatch> currentMatches = resolveInstantTransitions(getStartStateMatches(0));

        while (currentMatches.any() && values.hasCurrent())
        {
            final char currentValueElement = values.takeCurrent();

            final Set<PossibleMatch> nextMatches = Set.create();
            for (final PossibleMatch currentMatch : currentMatches)
            {
                final Iterable<PossibleMatch> nextMatchesFromCurrent = currentMatch.getNextMatches(currentValueElement);
                final Iterable<PossibleMatch> resolvedNextMatchesFromCurrent = resolveInstantTransitions(nextMatchesFromCurrent);
                nextMatches.addAll(resolvedNextMatchesFromCurrent);
            }

            currentMatches = nextMatches;
        }

        return currentMatches.where(PossibleMatch::isAtEndState).map(possibleMatch -> possibleMatch);
    }

    /**
     * Get whether or not the provided input value contains a match for this StateMachine.
     * @param value The input value.
     * @return Whether or not the provided input value contains a match for this StateMachine.
     */
    public boolean containsMatch(String value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return containsMatch(Strings.iterate(value));
    }

    /**
     * Get whether or not the provided input value contains a match for this StateMachine.
     * @param values The input values.
     * @return Whether or not the provided input value contains a match for this StateMachine.
     */
    public boolean containsMatch(Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        return containsMatch(values.iterate());
    }

    /**
     * Get whether or not the provided input value contains a match for this StateMachine.
     * @param values The input values.
     * @return Whether or not the provided input value contains a match for this StateMachine.
     */
    public boolean containsMatch(Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(getStartStates(), "getStartStates()");

        values.start();

        int currentIndex = 0;
        Iterable<PossibleMatch> currentMatches = getStartStateMatches(currentIndex);

        while (currentMatches.any() && values.hasCurrent() && !currentMatches.contains(PossibleMatch::isAtEndState))
        {
            final char currentValueElement = values.takeCurrent();
            ++currentIndex;

            final List<PossibleMatch> nextMatches = List.create(getStartStateMatches(currentIndex));
            for (final PossibleMatch currentMatch : currentMatches)
            {
                nextMatches.addAll(currentMatch.getNextMatches(currentValueElement));
            }

            currentMatches = nextMatches;
        }

        return currentMatches.contains(PossibleMatch::isAtEndState);
    }

    private static String getStateKey(String stateName)
    {
        return stateName.toLowerCase();
    }
}
