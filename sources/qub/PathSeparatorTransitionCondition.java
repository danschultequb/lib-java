package qub;

/**
 * A TransitionCondition that matches either a forward slash or a backslash.
 */
public class PathSeparatorTransitionCondition implements TransitionCondition
{
    @Override
    public boolean matches(char character)
    {
        return character == '/' || character == '\\';
    }
}
