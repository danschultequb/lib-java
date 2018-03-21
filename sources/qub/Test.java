package qub;

/**
 * A single Test objec that provides access to assertion methods.
 */
public class Test
{
    private final String name;
    private final TestGroup parentTestGroup;
    private final boolean shouldSkip;
    private final Process process;

    public Test(String name, TestGroup parentTestGroup, boolean shouldSkip, Process process)
    {
        this.name = name;
        this.parentTestGroup = parentTestGroup;
        this.shouldSkip = shouldSkip || (parentTestGroup != null && parentTestGroup.getShouldSkip());
        this.process = process;
    }

    public String getName()
    {
        return name;
    }

    public String getFullName()
    {
        return parentTestGroup == null ? name : parentTestGroup.getFullName() + ' ' + name;
    }

    public TestGroup getParentTestGroup()
    {
        return parentTestGroup;
    }

    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(getName()) ||
            testPattern.isMatch(getFullName()) ||
            (parentTestGroup != null && parentTestGroup.matches(testPattern));
    }

    public boolean getShouldSkip()
    {
        return shouldSkip;
    }

    public AsyncRunner getMainRunner()
    {
        return process.getMainRunner();
    }

    public AsyncRunner getParallelRunner()
    {
        return process.getParallelRunner();
    }

    public void await()
    {
        final AsyncRunner mainRunner = process.getMainRunner();
        final AsyncRunner parallelRunner = process.getParallelRunner();
        while (mainRunner.getScheduledTaskCount() != 0 || parallelRunner.getScheduledTaskCount() != 0)
        {
            mainRunner.await();
            parallelRunner.await();
        }
    }

    /**
     * Assert that the provided value is true. If it is not true, then a TestAssertionFailure will
     * be thrown.
     * @param value The value to assert is true.
     * @throws TestAssertionFailure if the value is not true.
     */
    public void assertTrue(boolean value)
    {
        assertTrue(value, null);
    }

    /**
     * Assert that the provided value is true. If it is not true, then a TestAssertionFailure will
     * be thrown with the provided message.
     * @param value The value to assert is true.
     * @param message The message to show if the value is not true.
     * @throws TestAssertionFailure if the value is not true.
     */
    public void assertTrue(boolean value, String message)
    {
        if (!value)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, true, false));
        }
    }

    /**
     * Assert that the provided value is false. If it is not false, then a TestAssertionFailure will
     * be thrown.
     * @param value The value to assert is false.
     * @throws TestAssertionFailure if the value is not false.
     */
    public void assertFalse(boolean value)
    {
        assertFalse(value, null);
    }

    /**
     * Assert that the provided value is false. If it is not false, then a TestAssertionFailure will
     * be thrown with the provided message.
     * @param value The value to assert is false.
     * @param message The message to show if the value is not false.
     * @throws TestAssertionFailure if the value is not false.
     */
    public void assertFalse(boolean value, String message)
    {
        if (value)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, false, true));
        }
    }

    /**
     * Assert that the provided value is null. If it is not null, then a TestAssertionFailure will
     * be thrown.
     * @param value The value to check.
     */
    public void assertNull(Object value)
    {
        assertNull(value, null);
    }

    /**
     * Assert that the provided value is null. If it is not null, then a TestAssertionFailure will
     * be thrown with the provided message.
     * @param value The value to check.
     * @param message The message to show if the value is not null.
     */
    public void assertNull(Object value, String message)
    {
        if (value != null)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, null, value));
        }
    }

    /**
     * Assert that the provided value is not null. If it is null, then a TestAssertionFailure will
     * be thrown.
     * @param value The value to check.
     */
    public void assertNotNull(Object value)
    {
        assertNotNull(value, null);
    }

    /**
     * Assert that the provided value is not null. If it is null, then a TestAssertionFailure will
     * be thrown with the provided message.
     * @param value The value to check.
     * @param message The message to show if the value is null.
     */
    public void assertNotNull(Object value, String message)
    {
        if (value == null)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "not null", value));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param <T> The type of values to compare.
     */
    public <T> void assertEqual(T expected, T actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal with the provided margin of error. If they are not
     * equal with the provided margin of error, then a TestAssertionFailure will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param marginOfError The margin of error to allow when comparing the two values.
     */
    public void assertEqual(double expected, double actual, double marginOfError)
    {
        assertEqual(expected, actual, marginOfError, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal
     * @param <T> The type of values to compare.
     */
    public <T> void assertEqual(T expected, T actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal with the provided margin of error. If they are not
     * equal with the provided margin of error, then a TestAssertionFailure will be thrown with the
     * provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param marginOfError The margin of error to allow when comparing the two values.
     * @param message The message to show if the values are not equal
     */
    public void assertEqual(double expected, double actual, double marginOfError, String message)
    {
        if (!Comparer.equal(expected, actual, marginOfError))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are not equal. If they are equal, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param <T> The type of values to compare.
     */
    public <T> void assertNotEqual(T expected, T actual)
    {
        assertNotEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are not equal. If they are equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are equal
     * @param <T> The type of values to compare.
     */
    public <T> void assertNotEqual(T expected, T actual, String message)
    {
        if (Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "not " + expected, actual));
        }
    }

    /**
     * Assert that the provided values point to the same object. If they don't point to the same
     * object, then a TestAssertionFailure will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param <T> The type of values to compare.
     */
    public <T> void assertSame(T expected, T actual)
    {
        assertSame(expected, actual, null);
    }

    /**
     * Assert that the provided values point to the same object. If they don't point to the same
     * object, then a TestAssertionFailure will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not the same.
     * @param <T> The type of values to compare.
     */
    public <T> void assertSame(T expected, T actual, String message)
    {
        if (!Comparer.same(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values don't point to the same object. If they do point to the same
     * object, then a TestAssertionFailure will be thrown.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @param <T> The type of values to compare.
     */
    public <T> void assertNotSame(T lhs, T rhs)
    {
        assertNotSame(lhs, rhs, null);
    }

    /**
     * Assert that the provided values don't point to the same object. If they do point to the same
     * object, then a TestAssertionFailure will be thrown with the provided message.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @param message The message to show if the values are the same.
     * @param <T> The type of values to compare.
     */
    public <T> void assertNotSame(T lhs, T rhs, String message)
    {
        if (Comparer.same(lhs, rhs))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, lhs, rhs));
        }
    }

    /**
     * Cause the test to fail instantly.
     */
    public void fail()
    {
        fail("Unexplained test failure.");
    }

    /**
     * Cause the test to fail instantly with the provided message.
     * @param message The message to display that should explain the failure.
     */
    public void fail(String message)
    {
        throw new TestAssertionFailure(getFullName(), new String[] { message });
    }

    /**
     * Cause the test to fail instantly with the message from the provided Exception.
     * @param e The Exception that caused the test failure.
     */
    public void fail(Exception e)
    {
        String failMessage = e.getClass().getSimpleName();
        final String eMessage = e.getMessage();
        if (eMessage != null && !eMessage.isEmpty())
        {
            failMessage += ": " + eMessage;
        }
        fail(failMessage);
    }

    private static String[] getMessageLines(String message, Object expected, Object actual)
    {
        int nextMessageIndex;
        final String[] messageLines;
        if (message == null)
        {
            messageLines = new String[2];
            nextMessageIndex = 0;
        }
        else
        {
            messageLines = new String[3];
            messageLines[0] = "Message:  " + message;
            nextMessageIndex = 1;
        }

        messageLines[nextMessageIndex] = "Expected: " + expected;
        ++nextMessageIndex;
        messageLines[nextMessageIndex] = "Actual:   " + actual;

        return messageLines;
    }
}
