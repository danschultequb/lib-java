package qub;

/**
 * A set of messages that are used when an assertion fails.
 */
public class AssertionMessages
{
    public static String nullMessage(String expressionName)
    {
        return expressionName + " must be null.";
    }

    public static String notNull(String variableName)
    {
        return variableName + " cannot be null.";
    }

    public static String notEmpty(String variableName)
    {
        return variableName + " cannot be empty.";
    }

    public static <T> String same(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must be the same object as " + expectedValue + ".";
    }

    public static <T> String equal(T expectedValue, T value, String variableName)
    {
        return variableName + " (" + value + ") must be " + expectedValue + ".";
    }

    public static <T> String notEqual(T expectedValue, T value, String variableName)
    {
        return variableName + " (" + value + ") must not be " + expectedValue + ".";
    }

    public static <T> String lessThan(T value, T upperBound, String variableName)
    {
        return variableName + " (" + value + ") must be less than " + upperBound + ".";
    }

    public static <T> String lessThanOrEqualTo(T value, T upperBound, String variableName)
    {
        return variableName + " (" + value + ") must be less than or equal to " + upperBound + ".";
    }

    public static <T> String greaterThanOrEqualTo(T value, T lowerBound, String variableName)
    {
        return variableName + " (" + value + ") must be greater than or equal to " + lowerBound + ".";
    }

    public static <T> String greaterThan(T value, T lowerBound, String variableName)
    {
        return variableName + " (" + value + ") must be greater than " + lowerBound + ".";
    }

    public static <T> String between(T lowerBound, T value, T upperBound, String variableName)
    {
        return variableName + " (" + value + ") must be between " + lowerBound + " and " + upperBound + ".";
    }

    public static String containsOnly(String value, char[] characters, String variableName)
    {
        return variableName + " (" + value + ") must contain only " + Array.toString(characters) + ".";
    }

    public static String oneOf(char value, char[] values, String variableName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(variableName + " (" + value + ") must be either");
        for (int i = 0; i < values.length - 1; ++i)
        {
            builder.append(" " + values[i]);
            if (values.length > 2)
            {
                builder.append(",");
            }
        }
        builder.append(" or " + values[values.length - 1] + ".");
        return builder.toString();
    }

    public static String oneOf(int value, int[] values, String variableName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(variableName + " (" + value + ") must be either");
        for (int i = 0; i < values.length - 1; ++i)
        {
            builder.append(" " + values[i]);
            if (values.length > 2)
            {
                builder.append(",");
            }
        }
        builder.append(" or " + values[values.length - 1] + ".");
        return builder.toString();
    }

    public static String oneOf(long value, long[] values, String variableName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(variableName + " (" + value + ") must be either");
        for (int i = 0; i < values.length - 1; ++i)
        {
            builder.append(" " + values[i]);
            if (values.length > 2)
            {
                builder.append(",");
            }
        }
        builder.append(" or " + values[values.length - 1] + ".");
        return builder.toString();
    }

    public static <T> String oneOf(T value, T[] values, String variableName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(variableName + " (" + value + ") must be either");
        for (int i = 0; i < values.length - 1; ++i)
        {
            builder.append(" " + values[i]);
            if (values.length > 2)
            {
                builder.append(",");
            }
        }
        builder.append(" or " + values[values.length - 1] + ".");
        return builder.toString();
    }
}
