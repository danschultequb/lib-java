package qub;

public class StringMatch implements PossibleMatch
{
    private final int startIndex;
    private final StringBuilder matchString;
    private State currentState;
    private final List<TrackedValue> trackedValues;

    public StringMatch(int startIndex, State currentState)
    {
        this(startIndex, currentState, null, null);
    }

    private StringMatch(int startIndex, State currentState, StringBuilder matchString, Iterable<TrackedValue> trackedValues)
    {
        PreCondition.assertGreaterThanOrEqualTo(startIndex, 0, "startIndex");
        PreCondition.assertNotNull(currentState, "currentState");

        this.startIndex = startIndex;
        this.matchString = new StringBuilder();
        if (!Strings.isNullOrEmpty(matchString))
        {
            this.matchString.append(matchString);
        }
        this.trackedValues = List.create();
        if (trackedValues != null)
        {
            this.trackedValues.addAll(trackedValues.map(TrackedValue::clone));
        }
        setCurrentState(currentState);
    }

    @Override
    public int getStartIndex()
    {
        return startIndex;
    }

    @Override
    public int getCount()
    {
        return matchString.length();
    }

    @Override
    public int getEndIndex()
    {
        return getCount() < 2 ? startIndex : getAfterEndIndex() - 1;
    }

    @Override
    public int getAfterEndIndex()
    {
        return startIndex + getCount();
    }

    @Override
    public Iterable<Character> getValues()
    {
        return Strings.iterable(matchString);
    }

    @Override
    public Iterable<Iterable<Character>> getTrackedValues()
    {
        return trackedValues.map(TrackedValue::getValues);
    }

    private void add(char value)
    {
        matchString.append(value);
        if (currentState.shouldTrackValues())
        {
            if (!trackedValues.any() || !trackedValues.last().isOpen())
            {
                trackedValues.add(new TrackedValue(currentState));
            }
            trackedValues.last().add(value);
        }
    }

    private void setCurrentState(State currentState)
    {
        PreCondition.assertNotNull(currentState, "currentState");

        this.currentState = currentState;
        if (trackedValues.any())
        {
            final TrackedValue lastTrackedValue = trackedValues.last();
            if (lastTrackedValue.getState() != currentState)
            {
                lastTrackedValue.close();
            }
        }
    }

    @Override
    public Iterable<PossibleMatch> getNextMatches(char value)
    {
        final List<PossibleMatch> result = List.create();

        final Iterable<State> nextStates = currentState.getNextStates(value);
        if (nextStates.any())
        {
            for (final State nextState : nextStates.skipLast())
            {
                final StringMatch newMatch = new StringMatch(startIndex, nextState, matchString, trackedValues);
                newMatch.add(value);
                result.add(newMatch);
            }

            setCurrentState(nextStates.last());
            add(value);
            result.add(this);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean isAtEndState()
    {
        return currentState.isEndState();
    }

    @Override
    public Iterable<PossibleMatch> resolveInstantTransitions()
    {
        final Iterable<PossibleMatch> result = Algorithms
            .traverse(currentState.getInstantNextStates(), State::getInstantNextStates)
            .map((State state) -> new StringMatch(startIndex, state, matchString, trackedValues));

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
