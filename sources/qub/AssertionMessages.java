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
        PreCondition.assertNotNullAndNotEmpty(types, "types");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return expressionName + " (" + Types.getFullTypeName(value) + ") must be of type " + English.orList(types.map(Types::getFullTypeName)) + ".";
    }

    static String oneOf(char value, char[] possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return AssertionMessages.oneOf(value, CharacterArray.create(possibleValues), expressionName, Characters::escapeAndQuote);
    }

    static String oneOf(String value, String[] possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return AssertionMessages.oneOf(value, Array.create(possibleValues), expressionName, Strings::escapeAndQuote);
    }

    static String oneOf(int value, int[] possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return AssertionMessages.oneOf(value, IntegerArray.create(possibleValues), expressionName);
    }

    static String oneOf(long value, long[] possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return AssertionMessages.oneOf(value, LongArray.create(possibleValues), expressionName);
    }

    static <T> String oneOf(T value, T[] possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return AssertionMessages.oneOf(value, Iterable.create(possibleValues), expressionName);
    }

    static <T> String oneOf(T value, Iterable<T> possibleValues, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Class<?> valueType = null;
        if (value != null)
        {
            valueType = value.getClass();
        }
        else
        {
            for (final T possibleValue : possibleValues)
            {
                if (possibleValue != null)
                {
                    valueType = possibleValue.getClass();
                    break;
                }
            }
        }

        final Function1<T,String> valueTransform;
        if (valueType == String.class)
        {
            valueTransform = (T string) -> Objects.toString(Strings.escapeAndQuote((String)string));
        }
        else if (valueType == Character.class)
        {
            valueTransform = (T character) -> Objects.toString(Characters.escapeAndQuote((Character)character));
        }
        else
        {
            valueTransform = Objects::toString;
        }

        return AssertionMessages.oneOf(value, possibleValues, expressionName, valueTransform);
    }

    static <T> String oneOf(T value, Iterable<T> possibleValues, String expressionName, Function1<T,String> valueTransform)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");
        PreCondition.assertNotNull(valueTransform, "valueTransform");

        final String result = CharacterList.create()
            .addAll(expressionName)
            .addAll(" (")
            .addAll(valueTransform.run(value))
            .addAll(") must be ")
            .addAll(English.orList(possibleValues.map(valueTransform)))
            .add('.')
            .toString(true);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }
}
