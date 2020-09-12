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

    static String trueMessage(String expressionName)
    {
        return expressionName + " cannot be false.";
    }

    static String falseMessage(String expressionName)
    {
        return expressionName + " cannot be true.";
    }

    static String notNull(String expressionName)
    {
        return expressionName + " cannot be null.";
    }

    static String notEmpty(String expressionName)
    {
        return expressionName + " cannot be empty.";
    }

    static String nullOrNotEmpty(String value, String expressionName)
    {
        return expressionName + " (" + Strings.escapeAndQuote(value) + ") must be either null or not empty.";
    }

    static <T> String same(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must be the same object as " + expectedValue + ".";
    }

    static <T> String equal(T expectedValue, T value, String expressionName)
    {
        return expressionName + " (" + value + ") must be " + expectedValue + ".";
    }

    static <T> String equal(T expectedValue, T value, T marginOfError, String expressionName)
    {
        return expressionName + " (" + value + ") must be " + expectedValue + " (+/- " + marginOfError + ").";
    }

    /**
     * Get an error message similar to "value1 (abc) must not be def."
     * @param expectedValue The expected value (abc).
     * @param value The value that should not be equal to the expectedValue (def).
     * @param expressionName The name of the expression that produced the value (value1).
     * @param <T> The type of values being compared.
     * @return The error message.
     */
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

    static String startsWith(String value, String prefix, String expressionName)
    {
        return "Expected " + expressionName + " (" + Strings.escapeAndQuote(value) + ") to start with " + Strings.escapeAndQuote(prefix) + ".";
    }

    static String endsWith(String value, String suffix, String expressionName)
    {
        return "Expected " + expressionName + " (" + Strings.escapeAndQuote(value) + ") to end with " + Strings.escapeAndQuote(suffix) + ".";
    }

    static String contains(String value, String substring, String expressionName)
    {
        return "Expected " + expressionName + " (" + Strings.escapeAndQuote(value) + ") to contain " + Strings.escapeAndQuote(substring) + ".";
    }

    static String containsOnly(String value, char[] characters, String expressionName)
    {
        return expressionName + " (" + value + ") must contain only " + Array.toString(characters) + ".";
    }

    static String instanceOf(Object value, Class<?> type, String expressionName)
    {
        return expressionName + " (" + Types.getFullTypeName(value) + ") must be of type " + Types.getFullTypeName(type) + ".";
    }

    static String instanceOf(Object value, Iterable<Class<?>> types, String expressionName)
    {
        return expressionName + " (" + Types.getFullTypeName(value) + ") must be of type " + English.orList(types.map(Types::getFullTypeName)) + ".";
    }

    static String oneOf(char value, char[] values, String expressionName)
    {
        final CharacterList list = CharacterList.create();
        list.addAll(expressionName + " (" + value + ") must be either");
        for (int i = 0; i < values.length - 1; ++i)
        {
            list.addAll(" " + values[i]);
            if (values.length > 2)
            {
                list.add(',');
            }
        }
        list.addAll(" or " + values[values.length - 1] + ".");
        return list.toString(true);
    }

    static String oneOf(int value, int[] values, String expressionName)
    {
        final CharacterList list = CharacterList.create();
        list.addAll(expressionName + " (" + value + ") must be either");
        for (int i = 0; i < values.length - 1; ++i)
        {
            list.addAll(" " + values[i]);
            if (values.length > 2)
            {
                list.add(',');
            }
        }
        list.addAll(" or " + values[values.length - 1] + ".");
        return list.toString(true);
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

    static <T> String oneOf(T value, Iterable<T> possibleValues, String expressionName)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(expressionName + " (" + Objects.toString(value) + ") must be ");
        final int possibleValuesCount = possibleValues.getCount();
        final T lastPossibleValue = possibleValues.last();
        if (possibleValuesCount == 1)
        {
            builder.append(lastPossibleValue);
        }
        else
        {
            builder.append("either");
            for (final T possibleValue : possibleValues.skipLast())
            {
                builder.append(" " + possibleValue);
                if (possibleValuesCount > 2)
                {
                    builder.append(",");
                }
            }
            builder.append(" or " + lastPossibleValue);
        }
        builder.append(".");
        return builder.toString();
    }
}
