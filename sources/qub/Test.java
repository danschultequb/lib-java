package qub;

/**
 * A single Test objec that provides access to assertion methods.
 */
public class Test
{
    private final String name;
    private final TestGroup parentTestGroup;
    private final Skip skip;
    private final Process process;

    public Test(String name, TestGroup parentTestGroup, Skip skip, Process process)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNull(process, "process");

        this.name = name;
        this.parentTestGroup = parentTestGroup;
        this.skip = skip;
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

    public boolean shouldSkip()
    {
        return skip != null || (parentTestGroup != null && parentTestGroup.shouldSkip());
    }

    public String getSkipMessage()
    {
        return skip == null ? null : skip.getMessage();
    }

    public Process getProcess()
    {
        return process;
    }

    public AsyncRunner getMainAsyncRunner()
    {
        return process.getMainAsyncRunner();
    }

    public AsyncRunner getParallelAsyncRunner()
    {
        return process.getParallelAsyncRunner();
    }

    /**
     * Get the Network that has been assigned to this Test.
     * @return The Network that has been assigned to this Test.
     */
    public Network getNetwork()
    {
        return process.getNetwork();
    }

    public FileSystem getFileSystem()
    {
        return process.getFileSystem();
    }

    /**
     * Get the Clock that has been assigned to this Test.
     * @return The Clock that has been assigned to this Test.
     */
    public Clock getClock()
    {
        return process.getClock();
    }

    /**
     * Get the Displays that have been assigned to this Test.
     * @return The Displays that have been assigned to this Test.
     */
    public Iterable<Display> getDisplays()
    {
        return process.getDisplays();
    }

    public void writeLine(String formattedText, Object... formattedTextArguments)
    {
        PreCondition.assertNotNullAndNotEmpty(formattedText, "formattedText");
        PreCondition.assertNotNull(process.getOutputAsByteWriteStream(), "process.getOutputAsByteWriteStream()");

        process.getOutputAsLineWriteStream().writeLine(formattedText, formattedTextArguments);
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
        assertFalse(value, (String)null);
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
        assertFalse(value, message == null ? null : () -> message);
    }

    /**
     * Assert that the provided value is false. If it is not false, then a TestAssertionFailure will
     * be thrown with the provided message.
     * @param value The value to assert is false.
     * @param message The message to show if the value is not false.
     * @throws TestAssertionFailure if the value is not false.
     */
    public void assertFalse(boolean value, Function0<String> message)
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
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length() != 0
     */
    public void assertNotNullAndNotEmpty(String value)
    {
        if (Strings.isNullOrEmpty(value))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines((String)null, "not null and not empty", value));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length() != 0
     */
    public void assertNotNullAndNotEmpty(Iterable<?> value)
    {
        assertNotNull(value);
        if (!value.any())
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines((String)null, "not null and not empty", value));
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
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(byte expected, Byte actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(int expected, Byte actual)
    {
        assertEqual(expected, actual.intValue());
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(char expected, Character actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Character expected, char actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(short expected, short actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(short expected, Short actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Short expected, short actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(int expected, int actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(int expected, Integer actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Integer expected, int actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(long expected, long actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Long expected, long actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, float actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, float actual, String message)
    {
        if (expected != actual)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, Float actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, Float actual, String message)
    {
        if (expected != actual)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Float expected, float actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Float expected, float actual, String message)
    {
        if (expected != actual)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, double actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, double actual, String message)
    {
        if (expected != actual)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, Double actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, Double actual, String message)
    {
        if (expected != actual)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Double expected, double actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Double expected, double actual, String message)
    {
        if (expected != actual)
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    public void assertEqual(Throwable expected, Throwable actual)
    {
        assertEqual(expected, actual, null);
    }

    public void assertEqual(Throwable expected, Throwable actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
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
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(byte expected, Byte actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(char expected, Character actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Character expected, char actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(short expected, short actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(short expected, Short actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Short expected, short actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(int expected, int actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(int expected, Integer actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Integer expected, int actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(long expected, long actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestAssertionFailure
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Long expected, long actual, String message)
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
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(double expected, double actual, double marginOfError, String message)
    {
        if (!Comparer.equal(expected, actual, marginOfError))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestAssertionFailure
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(byte[] expected, Iterable<Byte> actual)
    {
        assertEqual(expected, actual, null);
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
     * @param message The message to show if the values are equal.
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
     * Assert that the provided Iterable is empty.
     * @param values The Iterable to check.
     */
    public void assertEmpty(Iterable<?> values)
    {
        assertEmpty(values, null);
    }

    /**
     * Assert that the provided Iterable is empty.
     * @param values The Iterable to check.
     * @param message The message to show if the Iterable is not empty.
     */
    public <T> void assertEmpty(Iterable<T> values, String message)
    {
        if (values.any())
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, Iterable.create(), values));
        }
    }

    public <T extends Comparable<T>> void assertLessThan(T value, T upperBound)
    {
        assertLessThan(value, upperBound, null);
    }

    public <T extends Comparable<T>> void assertLessThan(T value, T upperBound, String message)
    {
        if (!Comparer.lessThan(value, upperBound))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "less than " + Objects.toString(upperBound), value));
        }
    }

    public <T extends Comparable<T>> void assertLessThanOrEqualTo(T lhs, T rhs)
    {
        assertLessThanOrEqualTo(lhs, rhs, null);
    }

    public <T extends Comparable<T>> void assertLessThanOrEqualTo(T lhs, T rhs, String message)
    {
        if (!Comparer.lessThanOrEqualTo(lhs, rhs))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "less than or equal to " + Objects.toString(rhs), lhs));
        }
    }

    public <T extends Comparable<T>> void assertGreaterThanOrEqualTo(T value, T lowerBound)
    {
        assertGreaterThanOrEqualTo(value, lowerBound, null);
    }

    public <T extends Comparable<T>> void assertGreaterThanOrEqualTo(T value, T lowerBound, String message)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "greater than or equal to " + Objects.toString(lowerBound), value));
        }
    }

    public <T extends Comparable<T>> void assertGreaterThanOrEqualTo(double value, double lowerBound)
    {
        assertGreaterThanOrEqualTo(value, lowerBound, null);
    }

    public <T extends Comparable<T>> void assertGreaterThanOrEqualTo(double value, double lowerBound, String message)
    {
        if (!(value >= lowerBound))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "greater than or equal to " + Objects.toString(lowerBound), value));
        }
    }

    public <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs)
    {
        assertGreaterThan(lhs, rhs, null);
    }

    public <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs, String message)
    {
        if (!Comparer.greaterThan(lhs, rhs))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "greater than " + Objects.toString(rhs), lhs));
        }
    }

    /**
     * Assert that the provided value is greater than or equal to the provided lowerBound and is
     * less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     */
    public <T extends Comparable<T>> void assertBetween(T lowerBound, T value, T upperBound)
    {
        assertBetween(lowerBound, value, upperBound, null);
    }

    /**
     * Assert that the provided value is greater than or equal to the provided lowerBound and is
     * less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     */
    public <T extends Comparable<T>> void assertBetween(T lowerBound, T value, T upperBound, String message)
    {
        if (!Comparer.between(lowerBound, value, upperBound))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines(message, "between " + Objects.toString(lowerBound) + " and " + Objects.toString(upperBound), value));
        }
    }

    /**
     * Assert that when the provided action is run it throws an exception.
     * @param action The action to run.
     */
    public void assertThrows(Action0 action)
    {
        Throwable exceptionThrown = null;
        try
        {
            action.run();
        }
        catch (RuntimeException e)
        {
            exceptionThrown = e;
        }

        if (exceptionThrown == null)
        {
            throw new TestAssertionFailure(getFullName(), new String[] { "Expected an exception to be thrown." });
        }
    }

    /**
     * Assert that when the provided action is run it throws an exception that is equal to the
     * provided exception.
     * @param action The action to run.
     * @param expectedException The expected exception.
     */
    public void assertThrows(Action0 action, Throwable expectedException)
    {
        Throwable exceptionThrown = null;
        try
        {
            action.run();
        }
        catch (Throwable e)
        {
            exceptionThrown = e;
        }

        if (exceptionThrown == null)
        {
            throw new TestAssertionFailure(getFullName(), new String[] { "Expected a " + expectedException.getClass().getCanonicalName() + " to be thrown with the message \"" + expectedException.getMessage() + "\"." });
        }
        else if (!Comparer.equal(expectedException, exceptionThrown))
        {
            throw new TestAssertionFailure(getFullName(), getMessageLines("Incorrect exception thrown", expectedException, exceptionThrown));
        }
    }

    /**
     * Assert that the provided value starts with the provided prefix.
     * @param value The value to check.
     * @param prefix The prefix to look for at the beginning of the provided value.
     */
    public void assertStartsWith(String value, String prefix)
    {
        if (!Strings.startsWith(value, prefix))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { "Expected " + Strings.escapeAndQuote(value) + " to start with " + Strings.escapeAndQuote(prefix) + "." });
        }
    }

    /**
     * Assert that the provided value starts with the provided prefix.
     * @param value The value to check.
     * @param prefix The prefix to look for at the beginning of the provided value.
     */
    public void assertStartsWith(String value, String prefix, CharacterComparer characterComparer)
    {
        if (!Strings.startsWith(value, prefix, characterComparer))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { "Expected " + Strings.escapeAndQuote(value) + " to start with " + Strings.escapeAndQuote(prefix) + "." });
        }
    }

    /**
     * Assert that the provided value starts with the provided prefix.
     * @param value The value to check.
     * @param prefix The prefix to look for at the beginning of the provided value.
     */
    public void assertStartsWith(String value, String prefix, String message)
    {
        if (!Strings.startsWith(value, prefix))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { message, "Expected " + Strings.escapeAndQuote(value) + " to start with " + Strings.escapeAndQuote(prefix) + "." });
        }
    }

    /**
     * Assert that the provided value ends with the provided suffix.
     * @param value The value to check.
     * @param suffix The suffix to look for at the end of the provided value.
     */
    public void assertEndsWith(String value, String suffix)
    {
        if (!Strings.endsWith(value, suffix))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { "Expected " + Strings.escapeAndQuote(value) + " to end with " + Strings.escapeAndQuote(suffix) + "." });
        }
    }

    /**
     * Assert that the provided value ends with the provided suffix.
     * @param value The value to check.
     * @param suffix The suffix to look for at the end of the provided value.
     */
    public void assertEndsWith(String value, String suffix, String message)
    {
        if (!Strings.endsWith(value, suffix))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { message, "Expected " + Strings.escapeAndQuote(value) + " to end with " + Strings.escapeAndQuote(suffix) + "." });
        }
    }

    public void assertContains(String value, String substring)
    {
        if (!Strings.contains(value, substring))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { "Expected " + Strings.escapeAndQuote(value) + " to contain " + Strings.escapeAndQuote(substring) + "." });
        }
    }

    public void assertContains(String value, String substring, String message)
    {
        if (!Strings.contains(value, substring))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { message, "Expected " + Strings.escapeAndQuote(value) + " to contain " + Strings.escapeAndQuote(substring) + "." });
        }
    }

    public <T> void assertOneOf(T[] possibleValues, T value)
    {
        if (!Array.contains(possibleValues, value))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { AssertionMessages.oneOf(value, possibleValues, "Actual value")});
        }
    }

    public void assertInstanceOf(Object value, Class<?> type)
    {
        if (!Types.instanceOf(value, type))
        {
            throw new TestAssertionFailure(getFullName(), new String[] { AssertionMessages.instanceOf(value, type, "Actual value") });
        }
    }

    /**
     * Assert that the provided Result object is a successful Result.
     * @param result The Result to check.
     */
    public <T> void assertSuccess(Result<T> result)
    {
        assertNotNull(result);
        result.catchError((Throwable error) ->
        {
            if (error instanceof TestAssertionFailure)
            {
                throw (TestAssertionFailure)error;
            }
            else
            {
                assertFalse(result.hasError(), () -> result.getErrorType().getName() + ": " + result.getErrorMessage());
            }
        })
        .throwError();
    }

    /**
     * Assert that the provided Result object is a successful Result.
     * @param result The Result to check.
     */
    public <T> void assertSuccess(Result<T> result, Action1<T> resultAction)
    {
        PreCondition.assertNotNull(resultAction, "resultAction");

        assertSuccess(result);
        assertSuccess(result.then(resultAction));
    }

    /**
     * Assert that the provided Result object is a successful Result.
     * @param result The Result to check.
     */
    public <T> void assertSuccess(Result<T> result, Action0 resultAction)
    {
        PreCondition.assertNotNull(resultAction, "resultAction");

        assertSuccess(result, (T value) -> resultAction.run());
    }

    /**
     * Assert that the provided Result object is a successful Result.
     * @param result The Result to check.
     */
    public <T> void assertSuccessDispose(Result<T> result, Action1<T> resultAction)
    {
        assertSuccess(result, (T value) ->
            {
                try
                {
                    resultAction.run(value);
                }
                finally
                {
                    if (Types.instanceOf(value, Disposable.class))
                    {
                        ((Disposable)value).dispose();
                    }
                }
            });
    }

    /**
     * Assert that the provided Result object is a successful Result.
     * @param result The Result to check.
     */
    public <T> void assertSuccessAsync(AsyncFunction<Result<T>> result)
    {
        assertNotNull(result);
        assertSuccess(result.awaitReturn());
    }

    /**
     * Assert that the provided Result object is a successful Result with the provided value.
     * @param expectedValue The value that we expect the Result to have.
     * @param result The Result to check.
     * @param <T> The type of value that the Result has.
     */
    public <T> void assertSuccess(T expectedValue, Result<T> result)
    {
        assertNotNull(result, "A successful Result should not be null");
        assertNull(result.getError(), "A successful Result should not have an error");
        assertEqual(expectedValue, result.getValue(), "Unexpected successful Result value");
    }

    /**
     * Assert that the provided Result object is a successful Result with the provided value.
     * @param expectedValue The value that we expect the Result to have.
     * @param result The Result to check.
     * @param <T> The type of value that the Result has.
     */
    public <T> void assertSuccessAsync(T expectedValue, AsyncFunction<Result<T>> result)
    {
        assertNotNull(result, "A successful Result should not be null");
        assertSuccess(expectedValue, result.awaitReturn());
    }

    /**
     * Assert that the provided Result object has the provided expected error.
     * @param expectedError The error that the result should have.
     * @param result The result to check.
     */
    public <T> void assertError(Throwable expectedError, Result<T> result)
    {
        assertNotNull(result, "The Result object should not be null.");
        assertEqual(expectedError, result.getError(), "Unexpected error.");
        assertNull(result.getValue(), "The Result's value should be null.");
    }

    /**
     * Assert that the provided Result object has the provided expected error.
     * @param expectedError The error that the result should have.
     * @param result The result to check.
     */
    public <T> void assertErrorAsync(Throwable expectedError, AsyncFunction<Result<T>> result)
    {
        assertNotNull(result, "The Result object should not be null.");
        assertError(expectedError, result.awaitReturn());
    }

    /**
     * Assert that the provided Result object has the provided expected value and error.
     * @param expectedValue The value that the result should have.
     * @param expectedError The error that the reuslt should have.
     * @param result The result to check.
     * @param <T> The type of the value.
     */
    public <T> void assertDone(T expectedValue, Throwable expectedError, Result<T> result)
    {
        assertDone(expectedError, result,
            (T value) ->
            {
                assertEqual(expectedValue, value);
            });
    }

    /**
     * Assert that the provided Result object has the provided expected value and error.
     * @param expectedError The error that the reuslt should have.
     * @param result The result to check.
     * @param valueAction The action to run to validate the result's value.
     * @param <T> The type of the value.
     */
    public <T> void assertDone(Throwable expectedError, Result<T> result, Action1<T> valueAction)
    {
        PreCondition.assertNotNull(valueAction, "valueAction");

        assertNotNull(result, "The Result object should not be null.");
        assertEqual(expectedError, result.getError());
        result.then(valueAction);
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
     * Cause the test to fail instantly with the message create the provided Exception.
     * @param e The Exception that caused the test failure.
     */
    public void fail(Throwable e)
    {
        String failMessage = e.getClass().getSimpleName();
        final String eMessage = e.getMessage();
        if (eMessage != null && !eMessage.isEmpty())
        {
            failMessage += ": " + eMessage;
        }
        fail(failMessage);
    }

    private static String[] getMessageLines(Function0<String> message, Object expected, Object actual)
    {
        return getMessageLines(message == null ? (String)null : message.run(), expected, actual);
    }

    private static String[] getMessageLines(String message, Object expected, Object actual)
    {
        int nextMessageIndex;
        final String[] messageLines;
        if (Strings.isNullOrEmpty(message))
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

        final String expectedString = toString(expected);
        final String actualString = toString(actual);
        messageLines[nextMessageIndex] = "Expected: " + addType(expected, actual, expectedString, actualString);
        ++nextMessageIndex;
        messageLines[nextMessageIndex] = "Actual:   " + addType(actual, expected, actualString, expectedString);

        return messageLines;
    }

    private static String toString(Object value)
    {
        String valueString = Strings.escape(Objects.toString(value));
        if (value instanceof String)
        {
            valueString = Strings.quote(valueString);
        }
        return valueString;
    }

    private static String addType(Object value, Object otherValue, String valueString, String otherValueString)
    {
        return valueString + (value == null || !valueString.equals(otherValueString) ? "" : " (" + value.getClass().getName() + ")");
    }
}
