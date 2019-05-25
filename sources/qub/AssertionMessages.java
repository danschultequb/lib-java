package qub;

/**
 * A set of messages that are used when an assertion fails.
 */
public interface AssertionMessages
{
    static String nullMessage(String expressionName)
    {
        return expressionName + " must be null.";
    }

    static String notNull(String variableName)
    {
        return variableName + " cannot be null.";
    }

    static String notEmpty(String variableName)
    {
        return variableName + " cannot be empty.";
    }

    static <T> String same(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must be the same object as " + expectedValue + ".";
    }

    static <T> String equal(T expectedValue, T value, String variableName)
    {
        return variableName + " (" + value + ") must be " + expectedValue + ".";
    }

    static <T> String notEqual(T expectedValue, T value, String variableName)
    {
        return variableName + " (" + value + ") must not be " + expectedValue + ".";
    }

    static <T> String lessThan(T value, T upperBound, String variableName)
    {
        return variableName + " (" + value + ") must be less than " + upperBound + ".";
    }

    static <T> String lessThanOrEqualTo(T value, T upperBound, String variableName)
    {
        return variableName + " (" + value + ") must be less than or equal to " + upperBound + ".";
    }

    static <T> String nullOrGreaterThanOrEqualTo(T value, T lowerBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be null or greater than or equal to " + lowerBound + ".";
    }

    static <T> String nullOrGreaterThan(T value, T lowerBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be null or greater than " + lowerBound + ".";
    }

    static <T> String greaterThanOrEqualTo(T value, T lowerBound, String variableName)
    {
        return variableName + " (" + value + ") must be greater than or equal to " + lowerBound + ".";
    }

    static <T> String greaterThan(T value, T lowerBound, String variableName)
    {
        return variableName + " (" + value + ") must be greater than " + lowerBound + ".";
    }

    static <T> String between(T lowerBound, T value, T upperBound, String variableName)
    {
        return Comparer.equal(lowerBound, upperBound)
            ? variableName + " (" + value + ") must be equal to " + lowerBound + "."
            : variableName + " (" + value + ") must be between " + lowerBound + " and " + upperBound + ".";
    }

    static String containsOnly(String value, char[] characters, String variableName)
    {
        return variableName + " (" + value + ") must contain only " + Array.toString(characters) + ".";
    }

    static String instanceOf(Object value, Class<?> type, String variableName)
    {
        return variableName + " (" + Types.getFullTypeName(value) + ") must be of type " + Types.getFullTypeName(type) + ".";
    }

    static String oneOf(char value, char[] values, String variableName)
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

    static String oneOf(int value, int[] values, String variableName)
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

    static String oneOf(long value, long[] values, String variableName)
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

    static <T> String oneOf(T value, T[] values, String variableName)
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

    static <T> String oneOf(T value, Iterable<T> values, String variableName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(variableName + " (" + value + ") must be either");
        for (final T acceptableValue : values.skipLast())
        {
            builder.append(" " + acceptableValue);
            if (values.getCount() > 2)
            {
                builder.append(",");
            }
        }
        builder.append(" or " + values.last() + ".");
        return builder.toString();
    }
}
