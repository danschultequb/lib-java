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

    static String notNull(String expressionName)
    {
        return expressionName + " cannot be null.";
    }

    static String notEmpty(String expressionName)
    {
        return expressionName + " cannot be empty.";
    }

    static <T> String same(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must be the same object as " + expectedValue + ".";
    }

    static <T> String equal(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must be " + expectedValue + ".";
    }

    static <T> String notEqual(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must not be " + expectedValue + ".";
    }

    static <T> String lessThan(T value, T upperBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be less than " + upperBound + ".";
    }

    static <T> String lessThanOrEqualTo(T value, T upperBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be less than or equal to " + upperBound + ".";
    }

    static <T> String nullOrGreaterThanOrEqualTo(T value, T lowerBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be null or greater than or equal to " + lowerBound + ".";
    }

    static <T> String nullOrGreaterThan(T value, T lowerBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be null or greater than " + lowerBound + ".";
    }

    static <T> String greaterThanOrEqualTo(T value, T lowerBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be greater than or equal to " + lowerBound + ".";
    }

    static <T> String greaterThan(T value, T lowerBound, String expressionName)
    {
        return expressionName + " (" + value + ") must be greater than " + lowerBound + ".";
    }

    static <T> String between(T lowerBound, T value, T upperBound, String expressionName)
    {
        return Comparer.equal(lowerBound, upperBound)
            ? expressionName + " (" + value + ") must be equal to " + lowerBound + "."
            : expressionName + " (" + value + ") must be between " + lowerBound + " and " + upperBound + ".";
    }

    static String containsOnly(String value, char[] characters, String expressionName)
    {
        return expressionName + " (" + value + ") must contain only " + Array.toString(characters) + ".";
    }

    static String instanceOf(Object value, Class<?> type, String expressionName)
    {
        return expressionName + " (" + Types.getFullTypeName(value) + ") must be of type " + Types.getFullTypeName(type) + ".";
    }

    static String oneOf(char value, char[] values, String expressionName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(expressionName + " (" + value + ") must be either");
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

    static String oneOf(int value, int[] values, String expressionName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(expressionName + " (" + value + ") must be either");
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

    static String oneOf(long value, long[] values, String expressionName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(expressionName + " (" + value + ") must be either");
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

    static <T> String oneOf(T value, T[] possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");

        final StringBuilder builder = new StringBuilder();
        builder.append(expressionName + " (" + Objects.toString(value) + ") must be ");
        if (possibleValues.length == 1)
        {
            builder.append(possibleValues[0]);
        }
        else
        {
            builder.append("either");
            for (int i = 0; i < possibleValues.length - 1; ++i)
            {
                builder.append(" " + possibleValues[i]);
                if (possibleValues.length > 2)
                {
                    builder.append(",");
                }
            }
            builder.append(" or " + possibleValues[possibleValues.length - 1]);
        }
        builder.append(".");
        return builder.toString();
    }

    static <T> String oneOf(T value, Iterable<T> values, String expressionName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(expressionName + " (" + value + ") must be either");
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
