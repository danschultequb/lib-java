package qub;

class SimplePathPattern extends PathPattern
{
    SimplePathPattern(String patternString, State startState)
    {
        super(patternString, startState);
    }

    /**
     * Get whether or not the provided path characters match this PathPattern.
     * @param pathCharacters The path characters to compare against this PathPattern.
     * @return Whether or not the provided path characters matches this PathPattern.
     */
    public boolean isMatch(Iterator<Character> pathCharacters)
    {
        final List<State> currentStates = ArrayList.fromValues(new State[] { getStartState() });

        if (pathCharacters != null)
        {
            pathCharacters.ensureHasStarted();

            final List<State> nextStates = new ArrayList<>();
            while (currentStates.any() && pathCharacters.hasCurrent())
            {
                final char currentCharacter = pathCharacters.takeCurrent();

                for (final State currentState : currentStates)
                {
                    nextStates.addAll(currentState.getNextStates(currentCharacter));
                }

                currentStates.clear();
                currentStates.addAll(nextStates);
                nextStates.clear();
            }
        }

        return currentStates.contains(new Function1<State, Boolean>()
        {
            @Override
            public Boolean run(State currentState)
            {
                return currentState.isEndState();
            }
        });
    }

    /**
     * Parse the provided text into a PathPattern.
     * @param text The text to parse.
     * @return The parsed PathPattern.
     */
    public static SimplePathPattern parse(String text)
    {
        final State startState = new State();
        State currentState = startState;

        final Iterator<Character> characters = new StringIterator(text);
        characters.next();
        while (characters.hasCurrent())
        {
            State nextState = currentState;

            switch (characters.getCurrent())
            {
                case '*':
                    if (characters.next() && characters.getCurrent() == '*')
                    {
                        currentState.addTransition(new DoubleStarTransitionCondition(), currentState);
                        characters.next();
                    }
                    else
                    {
                        currentState.addTransition(new StarTransitionCondition(), currentState);
                    }
                    break;

                case '/':
                case '\\':
                    nextState = new State();
                    currentState.addTransition(new PathSeparatorTransitionCondition(), nextState);
                    characters.next();
                    break;

                default:
                    nextState = new State();
                    currentState.addTransition(new CharacterTransitionCondition(characters.getCurrent()), nextState);
                    characters.next();
                    break;
            }

            currentState = nextState;
        }

        currentState.setIsEndState(true);

        return new SimplePathPattern(text, startState);
    }
}
