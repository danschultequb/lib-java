package qub;

/**
 * An interface that defines how to compare two characters.
 */
public interface CharacterComparer
{
    /**
     * A CharacterComparer that returns whether or not the provided characters are exactly the same.
     */
    CharacterComparer Exact = (char lhs, char rhs) ->
    {
        return lhs == rhs ? Comparison.Equal :
                   lhs < rhs ? Comparison.LessThan :
                       Comparison.GreaterThan;
    };

    /**
     * A CharacterComparer that does a case-insensitive comparison between the provided characters.
     */
    CharacterComparer CaseInsensitive = (char lhs, char rhs) ->
    {
        final char lhsLower = Characters.toLowerCase(lhs);
        final char rhsLower = Characters.toLowerCase(rhs);
        return CharacterComparer.Exact.compare(lhsLower, rhsLower);
    };

    /**
     * Get the comparison of the first character to the second character.
     * @param lhs The first character.
     * @param rhs The second character.
     * @return The comparison of the first character to the second character.
     */
    Comparison compare(char lhs, char rhs);

    /**
     * Get whether or not the provided characters are equal.
     * @param lhs The first character.
     * @param rhs The second character.
     * @return Whether or not the provided characters are equal.
     */
    default boolean equal(char lhs, char rhs)
    {
        return this.compare(lhs, rhs) == Comparison.Equal;
    }
}
