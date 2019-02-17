package qub;

/**
 * An execution create a StateMachine that matched a subset of the input values.
 */
public interface Match
{
    /**
     * Get the index into the original input values that this Match begins at.
     * @return The index into the original input values that this Match begins at.
     */
    int getStartIndex();

    /**
     * Get the number of input values that make up this Match.
     * @return The number of input values that make up this Match.
     */
    int getCount();

    /**
     * Get the index into the original input values that this Match ends at.
     * @return The index into the original input values that this Match ends at.
     */
    int getEndIndex();

    /**
     * Get the index into the original input values after this Match ends.
     * @return The index into the original input values after this Match ends.
     */
    int getAfterEndIndex();

    /**
     * Get the input values that make up this Match.
     * @return The input values that make up this Match.
     */
    Iterable<Character> getValues();

    /**
     * Get the input values that were tracked during this Match.
     * @return
     */
    Iterable<Iterable<Character>> getTrackedValues();
}
