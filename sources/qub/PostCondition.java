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
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
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
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
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
}
