package qub;

public abstract class CharacterComparer
{
    public static final CharacterComparer Exact = new CharacterComparer()
    {
        @Override
        public Comparison compare(char lhs, char rhs)
        {
            return lhs == rhs ? Comparison.Equal :
                       lhs < rhs ? Comparison.LessThan :
                           Comparison.GreaterThan;
        }
    };

    public static final CharacterComparer CaseInsensitive = new CharacterComparer()
    {
        @Override
        public Comparison compare(char lhs, char rhs)
        {
            final char lhsLower = Characters.toLowerCase(lhs);
            final char rhsLower = Characters.toLowerCase(rhs);
            return Exact.compare(lhsLower, rhsLower);
        }
    };

    /**
     * Get the comparison of the first character to the second character.
     * @param lhs The first character.
     * @param rhs The second character.
     * @return The comparison of the first character to the second character.
     */
    public abstract Comparison compare(char lhs, char rhs);

    /**
     * Get whether or not the provided characters are equal.
     * @param lhs The first character.
     * @param rhs The second character.
     * @return Whether or not the provided characters are equal.
     */
    public boolean equal(char lhs, char rhs)
    {
        return compare(lhs, rhs) == Comparison.Equal;
    }
}
