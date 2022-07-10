package qub;

public class PathPattern
{
    private final String originalString;
    private final StateMachine stateMachine;

    public PathPattern(String originalString, StateMachine stateMachine)
    {
        PreCondition.assertNotNull(originalString, "originalString");
        PreCondition.assertNotNull(stateMachine, "stateMachine");

        this.originalString = originalString;
        this.stateMachine = stateMachine;
    }

    /**
     * Get whether or not the provided path isMatch this PathPattern.
     * @param path The path to compare against this PathPattern.
     * @return Whether or not the provided path isMatch this PathPattern.
     */
    public boolean isMatch(Path path)
    {
        PreCondition.assertNotNull(path, "path");

        return stateMachine.isMatch(path.toString());
    }

    /**
     * Get whether or not the provided path string isMatch this PathPattern.
     * @param pathString The path string to compare against this PathPattern.
     * @return Whether or not the provided path string isMatch this PathPattern.
     */
    public boolean isMatch(String pathString)
    {
        return stateMachine.isMatch(pathString);
    }

    /**
     * Get whether or not the provided path characters match this PathPattern.
     * @param pathCharacters The path characters to compare against this PathPattern.
     * @return Whether or not the provided path characters isMatch this PathPattern.
     */
    public boolean isMatch(Iterator<Character> pathCharacters)
    {
        return stateMachine.isMatch(pathCharacters);
    }

    /**
     * Get the matches create the provided path characters for this PathPattern.
     * @param path The path characters to compare against this PathPattern.
     * @return The matches create the provided path characters for this PathPattern.
     */
    public Iterable<Match> getMatches(Path path)
    {
        PreCondition.assertNotNull(path, "path");

        return stateMachine.getMatches(path.toString());
    }

    /**
     * Get the matches create the provided path characters for this PathPattern.
     * @param pathString The path characters to compare against this PathPattern.
     * @return The matches create the provided path characters for this PathPattern.
     */
    public Iterable<Match> getMatches(String pathString)
    {
        return stateMachine.getMatches(pathString);
    }

    /**
     * Get the matches create the provided path characters for this PathPattern.
     * @param pathCharacters The path characters to compare against this PathPattern.
     * @return The matches create the provided path characters for this PathPattern.
     */
    public Iterable<Match> getMatches(Iterator<Character> pathCharacters)
    {
        return stateMachine.getMatches(pathCharacters);
    }

    @Override
    public String toString()
    {
        return originalString;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof PathPattern && equals((PathPattern)rhs);
    }

    public boolean equals(PathPattern rhs)
    {
        return rhs != null && originalString.equals(rhs.originalString);
    }

    @Override
    public int hashCode()
    {
        return this.originalString.hashCode();
    }

    /**
     * Parse the provided path into a PathPattern.
     * @param path The path to parse.
     * @return The parsed PathPattern.
     */
    public static PathPattern parse(Path path)
    {
        PreCondition.assertNotNull(path, "path");

        return parse(path.toString());
    }

    /**
     * Parse the provided text into a PathPattern.
     * @param text The text to parse.
     * @return The parsed PathPattern.
     */
    public static PathPattern parse(String text)
    {
        PreCondition.assertNotNull(text, "text");

        final StateMachine stateMachine = new StateMachine();
        State currentState = stateMachine.createState().setStartState(true);

        final Iterator<Character> characters = Strings.iterate(text).start();
        while (characters.hasCurrent())
        {
            final State nextState = stateMachine.createState();

            switch (characters.getCurrent())
            {
                case '*':
                    nextState.trackValues(true);
                    if (characters.next() && characters.getCurrent() == '*')
                    {
                        currentState.addInstantNextState(nextState);
                        nextState.addNextState(Characters.all, nextState);
                        characters.next();
                    }
                    else
                    {
                        currentState.addInstantNextState(nextState);
                        nextState.addNextState((Character value) -> value != '/' && value != '\\', nextState);
                    }
                    break;

                case '/':
                case '\\':
                    currentState.addNextState('/', nextState);
                    currentState.addNextState('\\', nextState);
                    characters.next();
                    break;

                default:
                    currentState.addNextState(characters.getCurrent(), nextState);
                    characters.next();
                    break;
            }

            currentState = nextState;
        }

        currentState.setEndState(true);

        return new PathPattern(text, stateMachine);
    }
}
