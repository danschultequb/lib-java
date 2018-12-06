package qub;

/**
 * A parsed path pattern/file filter for matching files and folders against.
 */
public abstract class PathPattern
{
    private final String patternString;
    private final PatternState startState;

    protected PathPattern(String patternString, PatternState startState)
    {
        this.patternString = patternString;
        this.startState = startState;
    }

    /**
     * Get the start state of this PathPattern.
     * @return The start state of this PathPattern.
     */
    protected PatternState getStartState()
    {
        return startState;
    }

    /**
     * Get whether or not the provided path matches this PathPattern.
     * @param path The path to compare against this PathPattern.
     * @return Whether or not the provided path matches this PathPattern.
     */
    public boolean isMatch(Path path)
    {
        return isMatch(path == null ? null : path.toString());
    }

    /**
     * Get whether or not the provided path string matches this PathPattern.
     * @param pathString The path string to compare against this PathPattern.
     * @return Whether or not the provided path string matches this PathPattern.
     */
    public boolean isMatch(String pathString)
    {
        return isMatch(pathString == null ? null : new StringIterator(pathString));
    }

    /**
     * Get whether or not the provided path characters match this PathPattern.
     * @param pathCharacters The path characters to compare against this PathPattern.
     * @return Whether or not the provided path characters matches this PathPattern.
     */
    public abstract boolean isMatch(Iterator<Character> pathCharacters);

    @Override
    public String toString()
    {
        return patternString;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof PathPattern && equals((PathPattern)rhs);
    }

    public boolean equals(PathPattern rhs)
    {
        return rhs != null && patternString.equals(rhs.patternString);
    }

    /**
     * Parse the provided path into a PathPattern.
     * @param path The path to parse.
     * @return The parsed PathPattern.
     */
    public static PathPattern parse(Path path)
    {
        return PathPattern.parse(path == null ? null : path.toString());
    }

    /**
     * Parse the provided text into a PathPattern.
     * @param text The text to parse.
     * @return The parsed PathPattern.
     */
    public static PathPattern parse(String text)
    {
        return SimplePathPattern.parse(text);
    }
}
