package qub;

/**
 * A set of pre-conditions that a method must satisfy before it can be run.
 */
public class PreCondition
{
    /**
     * Assert that the provided value is true.
     * @param value The value that must be true.
     * @param message The error message if value is not true.
     */
    public static void assertTrue(boolean value, String message)
    {
        if (!value)
        {
            throw new PreConditionFailure(message);
        }
    }

    /**
     * Assert that the provided value is not null.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     * @param <T> The type of value.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null
     */
    public static <T> void assertNotNull(T value, String variableName)
    {
        if (value == null)
        {
            throw new PreConditionFailure(AssertionMessages.notNull(variableName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length() != 0
     */
    public static void assertNotNullAndNotEmpty(String value, String variableName)
    {
        assertNotNull(value, variableName);
        if (value.length() == 0)
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length != 0
     */
    public static void assertNotNullAndNotEmpty(char[] value, String variableName)
    {
        assertNotNull(value, variableName);
        if (value.length == 0)
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
            throw new PreConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
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
            throw new PreConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
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
            throw new PreConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
        }
    }

    /**
     * Assert that value is not equal to the provided expectedValue.
     * @param expectedValue The expected value that value should not be equal to.
     * @param value The value that should not equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertNotEqual(int expectedValue, int value, String variableName)
    {
        if (Comparer.equal(expectedValue, value))
        {
            throw new PreConditionFailure(AssertionMessages.notEqual(expectedValue, value, variableName));
        }
    }

    /**
     * Assert that value is not equal to the provided expectedValue.
     * @param expectedValue The expected value that value should not be equal to.
     * @param value The value that should not equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertNotEqual(long expectedValue, long value, String variableName)
    {
        if (Comparer.equal(expectedValue, value))
        {
            throw new PreConditionFailure(AssertionMessages.notEqual(expectedValue, value, variableName));
        }
    }

    public static void assertOneOf(char value, char[] allowedValues, String variableName)
    {
        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static void assertOneOf(int value, int[] allowedValues, String variableName)
    {
        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static void assertOneOf(long value, long[] allowedValues, String variableName)
    {
        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    /**
     * Assert that value is less than or equal to upperBound.
     * @param value The value to ensure is less than or equal to upperBound.
     * @param upperBound The upper bound to ensure that the value is less than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertLessThanOrEqualTo(int value, int upperBound, String variableName)
    {
        if (!Comparer.lessThanOrEqualTo(value, upperBound))
        {
            throw new PreConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, upperBound, variableName));
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
            throw new PreConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
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
        if (!Comparer.between(lowerBound, value, upperBound))
        {
            throw new PreConditionFailure(AssertionMessages.between(lowerBound, value, upperBound, variableName));
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
            throw new PreConditionFailure(AssertionMessages.containsOnly(value, characters, variableName));
        }
    }
}