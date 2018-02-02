package qub;

/**
 * A TransitionCondition that matches all characters except for path separators ('/' and '\\').
 */
public class StarTransitionCondition implements TransitionCondition
{
    @Override
    public boolean matches(char character)
    {
        return character != '/' && character != '\\';
    }
}
