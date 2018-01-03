package qub;

/**
 * A TransitionCondition that matches all characters.
 */
public class DoubleStarTransitionCondition implements TransitionCondition
{
    @Override
    public boolean matches(char character)
    {
        return true;
    }
}
