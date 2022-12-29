package qub;

/**
 * A set of pre-conditions that a method must satisfy before it can be run.
 */
public class PreCondition
{
    /**
     * Throw a PreConditionFailure with the provided message.
     * @param message The message to use.
     */
    public static void fail(String message)
    {
        throw new PreConditionFailure(message);
    }

    /**
     * Assert that the provided value is true.
     * @param value The value that must be true.
     * @param message The message to show if the provided value is not true.
     */
    public static void check(boolean value, String message)
    {
        if (!value)
        {
            throw new PreConditionFailure(message);
        }
    }

    /**
     * Assert that the provided value is false.
     * @param value The value that must be false.
     * @param expressionName The name of the expression that produced the boolean value.
     */
    public static void assertFalse(boolean value, String expressionName)
    {
        if (value)
        {
            throw new PreConditionFailure(AssertionMessages.falseMessage(expressionName));
        }
    }

    /**
     * Assert that the provided value is false.
     * @param value The value that must be false.
     * @param expressionName The name of the expression that produced the boolean value.
     */
    public static void assertFalse(boolean value, String expressionName, String message)
    {
        if (value)
        {
            throw new PreConditionFailure(message);
        }
    }

    /**
     * Assert that the provided value is true.
     * @param value The value that must be true.
     * @param expressionName The name of the expression that produced the boolean value.
     */
    public static void assertTrue(boolean value, String expressionName)
    {
        if (!value)
        {
            throw new PreConditionFailure(AssertionMessages.trueMessage(expressionName));
        }
    }

    /**
     * Assert that the provided value is null.
     * @param value The value to check.
     * @param expressionName The name of the variable that contains value.
     * @param <T> The type of value.
     * @preCondition expressionName != null && expressionName.length() > 0
     * @postCondition value == null
     */
    public static <T> void assertNull(T value, String expressionName)
    {
        if (value != null)
        {
            throw new PreConditionFailure(AssertionMessages.nullMessage(expressionName));
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
     * Assert that the provided value is either null or not empty.
     * @param value The value to check.
     * @param expressionName The name of the expression that produced the value.
     */
    public static void assertNullOrNotEmpty(String value, String expressionName)
    {
        if (value != null && value.length() == 0)
        {
            throw new PreConditionFailure(AssertionMessages.nullOrNotEmpty(value, expressionName));
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
        PreCondition.assertNotNull(value, variableName);
        if (value.length() == 0)
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     */
    public static void assertNotNullAndNotEmpty(VersionNumber value, String variableName)
    {
        PreCondition.assertNotNull(value, variableName);
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
     * @postCondition value != null && value.length != 0
     */
    public static void assertNotNullAndNotEmpty(byte[] value, String variableName)
    {
        PreCondition.assertNotNull(value, variableName);
        if (value.length == 0)
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
        PreCondition.assertNotNull(value, variableName);
        if (value.length == 0)
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param expressionName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length != 0
     */
    public static void assertNotNullAndNotEmpty(int[] value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");
        PreCondition.assertNotNull(value, expressionName);
        if (value.length == 0)
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(expressionName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param expressionName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length != 0
     */
    public static void assertNotNullAndNotEmpty(long[] value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");
        PreCondition.assertNotNull(value, expressionName);
        if (value.length == 0)
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(expressionName));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @param variableName The name of the variable that contains value.
     * @preCondition variableName != null && variableName.length > 0
     * @postCondition value != null && value.length > 0
     */
    public static <T> void assertNotNullAndNotEmpty(T[] value, String variableName)
    {
        assertNotNull(value, variableName);
        if (value.length == 0)
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
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
        PreCondition.assertNotNull(value, variableName);
        if (!value.any())
        {
            throw new PreConditionFailure(AssertionMessages.notEmpty(variableName));
        }
    }

    /**
     * Assert that the two provided references point to the same object.
     * @param expectedValue The first reference.
     * @param value The second reference.
     * @param expressionName The name of the expression that produced the second reference.
     * @param <T> The type of values that the references point to.
     */
    public static <T> void assertSame(T expectedValue, T value, String expressionName)
    {
        if (expectedValue != value)
        {
            throw new PreConditionFailure(AssertionMessages.same(expectedValue, value, expressionName));
        }
    }

    /**
     * Assert that the two provided references do not point to the same object.
     * @param expectedValue The first reference.
     * @param value The second reference.
     * @param expressionName The name of the expression that produced the second reference.
     * @param <T> The type of values that the references point to.
     */
    public static <T> void assertNotSame(T expectedValue, T value, String expressionName)
    {
        if (expectedValue == value)
        {
            throw new PreConditionFailure(AssertionMessages.notSame(expectedValue, value, expressionName));
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
    public static void assertEqual(char expectedValue, char value, String variableName)
    {
        if (expectedValue != value)
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
    public static void assertEqual(char expectedValue, Character value, String variableName)
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
    public static void assertEqual(Character expectedValue, char value, String variableName)
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
        if (expectedValue != value)
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
        if (expectedValue != value)
        {
            throw new PreConditionFailure(AssertionMessages.equal(expectedValue, value, variableName));
        }
    }

    /**
     * Assert that value is not equal to the provided expectedValue.
     * @param expectedValue The expected value that value should not be equal to.
     * @param value The value that should not equal expectedValue.
     * @param expressionName The name of the variable that contains the value.
     */
    public static <T> void assertNotEqual(T expectedValue, T value, String expressionName)
    {
        if (Comparer.equal(expectedValue, value))
        {
            throw new PreConditionFailure(AssertionMessages.notEqual(expectedValue, value, expressionName));
        }
    }

    /**
     * Assert that value is not equal to the provided expectedValue.
     * @param expectedValue The expected value that value should not be equal to.
     * @param value The value that should not equal expectedValue.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertNotEqual(boolean expectedValue, boolean value, String variableName)
    {
        if (expectedValue == value)
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
    public static void assertNotEqual(boolean expectedValue, java.lang.Boolean value, String variableName)
    {
        if (expectedValue == value)
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
    public static void assertNotEqual(java.lang.Boolean expectedValue, boolean value, String variableName)
    {
        if (expectedValue == value)
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
    public static void assertNotEqual(int expectedValue, int value, String variableName)
    {
        if (expectedValue == value)
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
        if (expectedValue == value)
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
    public static void assertNotEqual(double expectedValue, double value, String variableName)
    {
        if (expectedValue == value)
        {
            throw new PreConditionFailure(AssertionMessages.notEqual(expectedValue, value, variableName));
        }
    }

    public static void assertOneOf(char value, char[] allowedValues, String variableName)
    {
        PreCondition.assertNotNull(allowedValues, "allowedValues");

        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static void assertOneOf(int value, int[] allowedValues, String variableName)
    {
        PreCondition.assertNotNull(allowedValues, "allowedValues");

        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static void assertOneOf(long value, long[] allowedValues, String variableName)
    {
        PreCondition.assertNotNull(allowedValues, "allowedValues");

        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static void assertOneOf(String value, String[] allowedValues, String variableName)
    {
        PreCondition.assertNotNull(allowedValues, "allowedValues");

        if (!Array.contains(allowedValues, value))
        {
            throw new PreConditionFailure(AssertionMessages.oneOf(value, allowedValues, variableName));
        }
    }

    public static <T> void assertOneOf(T value, Iterable<T> allowedValues, String variableName)
    {
        PreCondition.assertNotNull(allowedValues, "allowedValues");

        if (!allowedValues.contains(value))
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
            throw new PreConditionFailure(AssertionMessages.lessThanOrEqualTo(value, upperBound, variableName));
        }
    }

    /**
     * Assert that value is less than or equal to upperBound.
     * @param value The value to ensure is less than or equal to upperBound.
     * @param upperBound The upper bound to ensure that the value is less than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertLessThanOrEqualTo(long value, long upperBound, String variableName)
    {
        if (!Comparer.lessThanOrEqualTo(value, upperBound))
        {
            throw new PreConditionFailure(AssertionMessages.lessThanOrEqualTo(value, upperBound, variableName));
        }
    }

    /**
     * Assert that value is less than or equal to upperBound.
     * @param value The value to ensure is less than or equal to upperBound.
     * @param upperBound The upper bound to ensure that the value is less than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertLessThanOrEqualTo(T value, T upperBound, String variableName)
    {
        if (!Comparer.lessThanOrEqualTo(value, upperBound))
        {
            throw new PreConditionFailure(AssertionMessages.lessThanOrEqualTo(value, upperBound, variableName));
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
     * Assert that value is greater than or equal to lowerBound.
     * @param value The value to ensure is greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertGreaterThanOrEqualTo(long value, long lowerBound, String variableName)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PreConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
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
            throw new PreConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
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
            throw new PreConditionFailure(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is null or greater than or equal to lowerBound.
     * @param value The value to ensure is null or greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param expressionName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertNullOrGreaterThanOrEqualTo(T value, T lowerBound, String expressionName)
    {
        if (value != null && !Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PreConditionFailure(AssertionMessages.nullOrGreaterThanOrEqualTo(value, lowerBound, expressionName));
        }
    }

    /**
     * Assert that value is null or greater than or equal to lowerBound.
     * @param value The value to ensure is null or greater than or equal to lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than or equal to.
     * @param expressionName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertNullOrGreaterThanOrEqualTo(Integer value, int lowerBound, String expressionName)
    {
        if (value != null && !Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new PreConditionFailure(AssertionMessages.nullOrGreaterThanOrEqualTo(value, lowerBound, expressionName));
        }
    }

    /**
     * Assert that value is null or greater than lowerBound.
     * @param value The value to ensure is null or greater than lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than.
     * @param expressionName The name of the variable that contains the value.
     */
    public static <T extends Comparable<T>> void assertNullOrGreaterThan(T value, T lowerBound, String expressionName)
    {
        if (value != null && !Comparer.greaterThan(value, lowerBound))
        {
            throw new PreConditionFailure(AssertionMessages.nullOrGreaterThan(value, lowerBound, expressionName));
        }
    }

    /**
     * Assert that value is greater than lowerBound.
     * @param value The value to ensure is greater than lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertGreaterThan(int value, int lowerBound, String variableName)
    {
        if (!Comparer.greaterThan(value, lowerBound))
        {
            throw new PreConditionFailure(AssertionMessages.greaterThan(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that value is greater than lowerBound.
     * @param value The value to ensure is greater than lowerBound.
     * @param lowerBound The lower bound to ensure that the value is greater than.
     * @param variableName The name of the variable that contains the value.
     */
    public static void assertGreaterThan(double value, double lowerBound, String variableName)
    {
        if (!Comparer.greaterThan(value, lowerBound))
        {
            throw new PreConditionFailure(AssertionMessages.greaterThan(value, lowerBound, variableName));
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
            throw new PreConditionFailure(AssertionMessages.greaterThan(value, lowerBound, variableName));
        }
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     */
    public static void assertStartIndex(int value, int arrayLength)
    {
        PreCondition.assertStartIndex(value, arrayLength, "startIndex");
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     * @param expressionName The name of the expression value.
     */
    public static void assertStartIndex(int value, int arrayLength, String expressionName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 0, "arrayLength");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        PreCondition.assertBetween(0, value, arrayLength == 0 ? 0 : arrayLength - 1, expressionName);
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     */
    public static void assertNonEmptyStartIndex(int value, int arrayLength)
    {
        PreCondition.assertNonEmptyStartIndex(value, arrayLength, "startIndex");
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     * @param expressionName The name of the expression value.
     */
    public static void assertNonEmptyStartIndex(int value, int arrayLength, String expressionName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 1, "arrayLength");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        PreCondition.assertBetween(0, value, arrayLength - 1, expressionName);
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     */
    public static void assertStartIndex(long value, long arrayLength)
    {
        PreCondition.assertStartIndex(value, arrayLength, "startIndex");
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     * @param expressionName The name of the expression value.
     */
    public static void assertStartIndex(long value, long arrayLength, String expressionName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 0, "arrayLength");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        PreCondition.assertBetween(0, value, arrayLength == 0 ? 0 : arrayLength - 1, expressionName);
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     */
    public static void assertNonEmptyStartIndex(long value, long arrayLength)
    {
        PreCondition.assertNonEmptyStartIndex(value, arrayLength, "startIndex");
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param value The index value.
     * @param arrayLength The length of the array to index into.
     * @param expressionName The name of the expression value.
     */
    public static void assertNonEmptyStartIndex(long value, long arrayLength, String expressionName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 1, "arrayLength");
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        PreCondition.assertBetween(0, value, arrayLength - 1, expressionName);
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param index The index value.
     * @param arrayLength The length of the array to index into.
     */
    public static void assertIndexAccess(int index, int arrayLength)
    {
        PreCondition.assertIndexAccess(index, arrayLength, "index");
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param index The index value.
     * @param arrayLength The length of the array to index into.
     * @param variableName The name of the expression value.
     */
    public static void assertIndexAccess(int index, int arrayLength, String variableName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 1, "Indexable length");
        PreCondition.assertBetween(0, index, arrayLength - 1, variableName);
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param index The index value.
     * @param arrayLength The length of the array to index into.
     */
    public static void assertIndexAccess(long index, long arrayLength)
    {
        PreCondition.assertIndexAccess(index, arrayLength, "index");
    }

    /**
     * Assert that the index value is greater than or equal to 0 and less than the array length.
     * @param index The index value.
     * @param arrayLength The length of the array to index into.
     * @param variableName The name of the expression value.
     */
    public static void assertIndexAccess(long index, long arrayLength, String variableName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 1, "Indexable length");
        PreCondition.assertBetween(0, index, arrayLength - 1, variableName);
    }

    /**
     * Assert that the provided value is a valid length for the provided startIndex and array
     * length.
     * @param length The length.
     * @param startIndex The start index into an array.
     * @param arrayLength The length of the array.
     */
    public static void assertLength(int length, int startIndex, int arrayLength)
    {
        PreCondition.assertLength(length, startIndex, arrayLength, "length");
    }

    /**
     * Assert that the provided value is a valid length for the provided startIndex and array
     * length.
     * @param length The length.
     * @param startIndex The start index into an array.
     * @param arrayLength The length of the array.
     * @param expressionName The name of the expression that produced the value.
     */
    public static void assertLength(int length, int startIndex, int arrayLength, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 0, "arrayLength");
        PreCondition.assertStartIndex(startIndex, arrayLength, "startIndex");

        PreCondition.assertBetween(0, length, arrayLength - startIndex, expressionName);
    }

    /**
     * Assert that the provided value is a valid length for the provided startIndex and array
     * length.
     * @param length The length.
     * @param startIndex The start index into an array.
     * @param arrayLength The length of the array.
     */
    public static void assertLength(long length, long startIndex, long arrayLength)
    {
        PreCondition.assertLength(length, startIndex, arrayLength, "length");
    }

    /**
     * Assert that the provided value is a valid length for the provided startIndex and array
     * length.
     * @param length The length.
     * @param startIndex The start index into an array.
     * @param arrayLength The length of the array.
     * @param expressionName The name of the expression that produced the value.
     */
    public static void assertLength(long length, long startIndex, long arrayLength, String expressionName)
    {
        PreCondition.assertGreaterThanOrEqualTo(arrayLength, 0, "arrayLength");
        PreCondition.assertStartIndex(startIndex, arrayLength);
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        PreCondition.assertBetween(0, length, arrayLength - startIndex, expressionName);
    }

    /**
     * Assert that the provided value is a valid length for the provided startIndex and array
     * length.
     * @param length The length.
     * @param startIndex The start index into an array.
     * @param arrayLength The length of the array.
     */
    public static void assertNonEmptyLength(int length, int startIndex, int arrayLength)
    {
        PreCondition.assertNonEmptyLength(length, startIndex, arrayLength, "length");
    }

    /**
     * Assert that the provided value is a valid length for the provided startIndex and array
     * length.
     * @param length The length.
     * @param startIndex The start index into an array.
     * @param arrayLength The length of the array.
     * @param expressionName The name of the expression that produced the value.
     */
    public static void assertNonEmptyLength(int length, int startIndex, int arrayLength, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");
        PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength);

        PreCondition.assertBetween(1, length, arrayLength - startIndex, expressionName);
    }

    /**
     * Assert that the int value is either 0 or 1.
     * @param value The int value to check.
     * @param expressionName The name of the expression value.
     */
    public static void assertBit(int value, String expressionName)
    {
        PreCondition.assertBetween(Bits.minimum, value, Bits.maximum, expressionName);
    }

    /**
     * Assert that the int value is in the range of a byte.
     * @param value The int value to check.
     * @param expressionName The name of the expression value.
     */
    public static void assertByte(int value, String expressionName)
    {
        assertBetween(Bytes.minimum, value, Bytes.maximum, expressionName);
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
     * Assert that the provided value is greater than or equal to the provided lowerBound and is
     * less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     * @param variableName The name of variable that produced the value.
     * @postCondition lowerBound <= value <= upperBound
     */
    public static void assertBetween(double lowerBound, double value, double upperBound, String variableName)
    {
        if (!Comparer.between(lowerBound, value, upperBound))
        {
            throw new PreConditionFailure(AssertionMessages.between(lowerBound, value, upperBound, variableName));
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
    public static <T extends Comparable<T>> void assertBetween(T lowerBound, T value, T upperBound, String variableName)
    {
        if (!Comparer.between(lowerBound, value, upperBound))
        {
            throw new PreConditionFailure(AssertionMessages.between(lowerBound, value, upperBound, variableName));
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
            throw new PreConditionFailure(AssertionMessages.startsWith(value, prefix, expressionName));
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
            throw new PreConditionFailure(AssertionMessages.startsWith(value, prefix, expressionName));
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
            throw new PreConditionFailure(AssertionMessages.endsWith(value, suffix, expressionName));
        }
    }

    public static void assertContains(String value, String substring, String expressionName)
    {
        if (!Strings.contains(value, substring))
        {
            throw new PreConditionFailure(AssertionMessages.contains(value, substring, expressionName));
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

    public static void assertInstanceOf(Object value, Class<?> type, String variableName)
    {
        if (!Types.instanceOf(value, type))
        {
            throw new PreConditionFailure(AssertionMessages.instanceOf(value, type, variableName));
        }
    }

    public static void assertInstanceOf(Object value, Iterable<Class<?>> types, String variableName)
    {
        if (!Types.instanceOf(value, types))
        {
            throw new PreConditionFailure(AssertionMessages.instanceOf(value, types, variableName));
        }
    }

    /**
     * Assert that the provided value is not disposed.
     * @param value The value to check.
     * @param expressionName The expression that created the value.
     */
    public static void assertNotDisposed(Disposable value, String expressionName)
    {
        PreCondition.assertFalse(value.isDisposed(), expressionName + ".isDisposed()");
    }
}
