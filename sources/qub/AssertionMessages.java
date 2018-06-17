package qub;

/**
 * A set of messages that are used when an assertion fails.
 */
public class AssertionMessages
{
    public static <T> String equal(T expectedValue, T value, String variableName)
    {
        return variableName + " (" + value + ") must be " + expectedValue + ".";
    }

    public static <T> String greaterThanOrEqualTo(T value, T lowerBound, String variableName)
    {
        return variableName + " (" + value + ") must be greater than or equal to " + lowerBound + ".";
    }

    public static <T> String between(T lowerBound, T value, T upperBound, String variableName)
    {
        return variableName + " (" + value + ") must be between " + lowerBound + " and " + upperBound + ".";
    }
}
