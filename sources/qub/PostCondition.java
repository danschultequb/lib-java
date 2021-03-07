package qub;

/**
 * A set of post-conditions that a method must satisfy before it can return.
 */
public class PostCondition
{
    /**
     * Assert that the provided value is false.
     * @param value The value that must be false.
     * @param message The error message if value is not false.
     */
    public static void assertFalse(boolean value, String message)
    {
        if (value)
        {
            throw new PostConditionFailure(message);
        }
    }

    /**
     * Assert that the provided value is true.
     * @param value The value that must be true.
     * @param message The error message if value is not true.
     */
    public static void assertTrue(boolean value, String message)
    {
        if (!value)
        {
            throw new PostConditionFailure(message);
        }
    }

    /**
     * Assert that the provided value is null.
     * @param value The value to check.
     * @param expressionName The name of the expression that produced the value.
     */
    public static void assertNull(Object value, String expressionName)
    {
        if (value != null)
        {
            throw new PostConditionFailure(AssertionMessages.nullMessage(expressionName));
        }
    }

    /**
     * Assert that the provided value is not null.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     */
    public static void assertNotNull(Object value, String variableName)
    {
        if (value == null)
        {
            throw new PostConditionFailure(AssertionMessages.notNull(variableName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length > 0
     * @postCondition value != null && value.length > 0
     */
    public static <T> void assertNotNullAndNotEmpty(Iterable<T> value, String variableName)
    {
        assertNotNull(value, variableName);
        if (!value.any())
        {
            throw new PostConditionFailure(AssertionMessages.notEmpty(variableName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length() > 0
     */
    public static <T> void assertNotNullAndNotEmpty(String value, String variableName)
    {
        assertNotNull(value, variableName);
        if (value.length() == 0)
        {
            throw new PostConditionFailure(AssertionMessages.notEmpty(variableName));
        }
    }

    public static <T> void assertSame(T expectedValue, T value, String expressionName)
    {
        if (expectedValue != value)
        {
            throw new PostConditionFailure(AssertionMessages.same(expectedValue, value, expressionName));
        }
    }

    /**
     * Assert that value is equal to the provided expectedValue.
     * @param expectedValue The expected value that value should be equal to.
     * @param value The value that should equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static <T> void assertEqual(T expectedValue, T value, String variableName)
    {
        if (!Comparer.equal(expectedValue, value))
        {
            throw new PostConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
        }
    }

    /**
     * Assert that value is equal to the provided expectedValue.
     * @param expectedValue The expected value that value should be equal to.
     * @param value The value that should equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static <T> void assertEqual(ComparableWithError<T> expectedValue, T value, T marginOfError, String variableName)
    {
        if (!Comparer.equal(expectedValue, value, marginOfError))
        {
            throw new PostConditionFailure(AssertionMessages.equal(expectedValue, value, marginOfError, variableName));
        }
    }

    /**
     * Assert that value is equal to the provided expectedValue.
     * @param expectedValue The expected value that value should be equal to.
     * @param value The value that should equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertEqual(int expectedValue, int value, String variableName)
    {
        if (!Comparer.equal(expectedValue, value))
        {
            throw new PostConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
        }
    }

    /**
     * Assert that value is equal to the provided expectedValue.
     * @param expectedValue The expected value that value should be equal to.
     * @param value The value that should equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertEqual(long expectedValue, long value, String variableName)
    {
        if (!Comparer.equal(expectedValue, value))
        {
            throw new PostConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
        }
    }

    public static void assertOneOf(int value, int[] allowedValues, String variableName)
    {
        if (!Array.contains(allowedValues, value))
        {
            throw new PostConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static void assertOneOf(long value, long[] allowedValues, String variableName)
    {
        if (!Array.contains(allowedValues, value))
        {
            throw new PostConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static <T> void assertOneOf(T value, T[] allowedValues, String variableName)
    {
        if (!Array.contains(allowedValues, value))
        {
            throw new PostConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static <T> void assertOneOf(T value, Iterable<T> allowedValues, String variableName)
    {
        PreCondition.assertNotNull(allowedValues, "allowedValues");

        if (!allowedValues.contains(value))
        {
            throw new PostConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    /**
     * Assert that value is greater than or equal to lowerBound.
     * @param value The value to ensure is greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertGreaterThanOrEqualTo(int value, int lowerBound, String variableName)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PostConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is greater than or equal to lowerBound.
     * @param value The value to ensure is greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertGreaterThanOrEqualTo(long value, long lowerBound, String variableName)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PostConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is greater than or equal to lowerBound.
     * @param value The value to ensure is greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertGreaterThanOrEqualTo(double value, double lowerBound, String variableName)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PostConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is greater than or equal to lowerBound.
     * @param value The value to ensure is greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertGreaterThanOrEqualTo(T value, T lowerBound, String variableName)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PostConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is null or greater than or equal to lowerBound.
     * @param value The value to ensure is null or greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertNullOrGreaterThanOrEqualTo(T value, T lowerBound, String variableName)
    {
        if (!Comparer.nullOrGreaterThanOrEqualTo(value, lowerBound))
        {
            throw new PostConditionFailure(AssertionMessages.nullOrGreaterThanOrEqualTo(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is greater than lowerBound.
     * @param value The value to ensure is greater than lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than.
     * @param variableName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertGreaterThan(T value, T lowerBound, String variableName)
    {
        if (!Comparer.greaterThan(value, lowerBound))
        {
            throw new PostConditionFailure(AssertionMessages.greaterThan(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that the provided value is greater than or equal to the provided lowerBound and is
     * less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     * @param variableName The name of variable that produced the value.
     * @postCondition lowerBound <= value <= upperBound
     */
    public static void assertBetween(long lowerBound, long value, long upperBound, String variableName)
    {
        if (upperBound < lowerBound)
        {
            assertBetween(upperBound, value, lowerBound, variableName);
        }
        else if (!Comparer.between(lowerBound, value, upperBound))
        {
            throw new PostConditionFailure(AssertionMessages.between(lowerBound, value, upperBound, variableName));
        }
    }

    /**
     * Assert that the provided value starts with the provided prefix.
     * @param value The value to check.
     * @param prefix The prefix to look for at the beginning of the provided value.
     */
    public static void assertStartsWith(String value, String prefix, String expressionName)
    {
        if (!Strings.startsWith(value, prefix))
        {
            throw new PostConditionFailure(AssertionMessages.startsWith(value, prefix, expressionName));
        }
    }

    /**
     * Assert that the provided value starts with the provided prefix.
     * @param value The value to check.
     * @param prefix The prefix to look for at the beginning of the provided value.
     */
    public static void assertStartsWith(String value, String prefix, CharacterComparer characterComparer, String expressionName)
    {
        PreCondition.assertNotNull(characterComparer, "characterComparer");

        if (!Strings.startsWith(value, prefix, characterComparer))
        {
            throw new PostConditionFailure(AssertionMessages.startsWith(value, prefix, expressionName));
        }
    }

    /**
     * Assert that the provided value ends with the provided suffix.
     * @param value The value to check.
     * @param suffix The suffix to look for at the end of the provided value.
     */
    public static void assertEndsWith(String value, String suffix, String expressionName)
    {
        if (!Strings.endsWith(value, suffix))
        {
            throw new PostConditionFailure(AssertionMessages.endsWith(value, suffix, expressionName));
        }
    }

    public static void assertContains(String value, String substring, String expressionName)
    {
        if (!Strings.contains(value, substring))
        {
            throw new PostConditionFailure(AssertionMessages.contains(value, substring, expressionName));
        }
    }

    /**
     * Assert that the provided value contains only the provided characters. It doesn't have to
     * contain all of the characters and it can contain multiple instances of each character, but
     * each character in the provided value must be contained in the provided set of characters.
     * @param value The value to check.
     * @param characters The characters to allow.
     * @param variableName The name of the variable that produced value.
     * @preCondition value != null
     * @preCondition characters != null && characters.length > 0
     * @preCondition variableName != null && variableName.length() > 0
     */
    public static void assertContainsOnly(String value, char[] characters, String variableName)
    {
        if (!Comparer.containsOnly(value, characters))
        {
            throw new PostConditionFailure(AssertionMessages.containsOnly(value, characters, variableName));
        }
    }

    public static void assertInstanceOf(Object value, Class<?> type, String variableName)
    {
        if (!Types.instanceOf(value, type))
        {
            throw new PostConditionFailure(AssertionMessages.instanceOf(value, type, variableName));
        }
    }
    
    public static void assertInstanceOf(Object value, Iterable<Class<?>> types, String variableName)
    {
        if (!Types.instanceOf(value, types))
        {
            throw new PostConditionFailure(AssertionMessages.instanceOf(value, types, variableName));
        }
    }

    /**
     * Assert that the provided value is not disposed.
     * @param value The value to check.
     * @param expressionName The expression that created the value.
     */
    public static void assertNotDisposed(Disposable value, String expressionName)
    {
        PostCondition.assertFalse(value.isDisposed(), expressionName + ".isDisposed()");
    }
}
