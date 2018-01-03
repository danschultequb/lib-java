package qub;

public interface TransitionCondition
{
    /**
     * Get whether or not the provided character satisfies this condition.
     * @param character The character to check.
     * @return Whether or not the provided character satisfies this condition.
     */
    boolean matches(char character);
}
