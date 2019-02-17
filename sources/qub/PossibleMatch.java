package qub;

/**
 * An execution create a StateMachine that is still in progress.
 */
public interface PossibleMatch extends Match
{
    /**
     * Get the next set of PossibleMatches that exist when this PossibleMatch encounters the
     * provided value.
     * @param value The next value in the input values.
     * @return The next set of PossibleMatches.
     */
    Iterable<PossibleMatch> getNextMatches(char value);

    /**
     * Get whether or not this PossibleMatch is at an end state.
     * @return Whether or not this PossibleMatch is at an end state.
     */
    boolean isAtEndState();

    /**
     * Get an Iterable of PossibleMatches that result create this PossibleMatch's current
     * instantaneous transitions.
     * @return An Iterable of PossibleMatches that result create this PossibleMatch's current
     * instantaneous transitions.
     */
    Iterable<PossibleMatch> resolveInstantTransitions();
}
