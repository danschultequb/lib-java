package qub;

class SimplePathPattern extends PathPattern
{
    SimplePathPattern(String patternString, PatternState startState)
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
        final List<PatternState> currentStates = ArrayList.fromValues(new PatternState[] { getStartState() });

        if (pathCharacters != null)
        {
            pathCharacters.ensureHasStarted();

            final List<PatternState> nextStates = new ArrayList<>();
            while (currentStates.any() && pathCharacters.hasCurrent())
            {
                final char currentCharacter = pathCharacters.takeCurrent();

                for (final PatternState currentState : currentStates)
                {
                    nextStates.addAll(currentState.getNextStates(currentCharacter));
                }

                currentStates.clear();
                currentStates.addAll(nextStates);
                nextStates.clear();
            }
        }

        return currentStates.contains(PatternState::isEndState);
    }

    /**
     * Parse the provided text into a PathPattern.
     * @param text The text to parse.
     * @return The parsed PathPattern.
     */
    public static SimplePathPattern parse(String text)
    {
        final PatternState startState = new PatternState();
        PatternState currentState = startState;

        final Iterator<Character> characters = new StringIterator(text);
        characters.next();
        while (characters.hasCurrent())
        {
            PatternState nextState = currentState;

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
                    nextState = new PatternState();
                    currentState.addTransition(new PathSeparatorTransitionCondition(), nextState);
                    characters.next();
                    break;

                default:
                    nextState = new PatternState();
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
