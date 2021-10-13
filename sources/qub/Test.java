package qub;

/**
 * A single Test objec that provides access to assertion methods.
 */
public class Test
{
    private final String name;
    private final TestParent parent;
    private final Skip skip;

    private Test(String name, TestParent parent, Skip skip)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.name = name;
        this.parent = parent;
        this.skip = skip;
    }

    public static Test create(String name, TestParent parent, Skip skip)
    {
        return new Test(name, parent, skip);
    }

    public String getName()
    {
        return name;
    }

    public String getFullName()
    {
        return parent == null ? name : parent.getFullName() + ' ' + name;
    }

    public TestParent getParent()
    {
        return parent;
    }

    /**
     * Get the TestClass that contains this Test object.
     * @return The TestClass that contains this Test object.
     */
    public TestClass getTestClass()
    {
        TestParent parent = getParent();
        while (parent != null && !Types.instanceOf(parent, TestClass.class))
        {
            parent = parent.getParent();
        }
        return Types.as(parent, TestClass.class);
    }

    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(this.getName()) ||
            testPattern.isMatch(this.getFullName()) ||
            (this.parent != null && this.parent.matches(testPattern));
    }

    public boolean shouldSkip()
    {
        return this.skip != null || (this.parent != null && this.parent.shouldSkip());
    }

    public String getSkipMessage()
    {
        return this.skip == null ? null : this.skip.getMessage();
    }

    /**
     * Assert that the provided value is true. If it is not true, then a TestError will
     * be thrown.
     * @param value The value to assert is true.
     * @throws TestError if the value is not true.
     */
    public void assertTrue(boolean value)
    {
        this.assertTrue(value, null);
    }

    /**
     * Assert that the provided value is true. If it is not true, then a TestError will
     * be thrown with the provided message.
     * @param value The value to assert is true.
     * @param message The message to show if the value is not true.
     * @throws TestError if the value is not true.
     */
    public void assertTrue(boolean value, String message)
    {
        if (!value)
        {
            throw new TestError(this.getFullName(), Test.getMessageLines(message, true, false));
        }
    }

    /**
     * Assert that the provided value is false. If it is not false, then a TestError will
     * be thrown.
     * @param value The value to assert is false.
     * @throws TestError if the value is not false.
     */
    public void assertFalse(boolean value)
    {
        this.assertFalse(value, (String)null);
    }

    /**
     * Assert that the provided value is false. If it is not false, then a TestError will
     * be thrown with the provided message.
     * @param value The value to assert is false.
     * @param message The message to show if the value is not false.
     * @throws TestError if the value is not false.
     */
    public void assertFalse(boolean value, String message)
    {
        this.assertFalse(value, message == null ? null : () -> message);
    }

    /**
     * Assert that the provided value is false. If it is not false, then a TestError will
     * be thrown with the provided message.
     * @param value The value to assert is false.
     * @param message The message to show if the value is not false.
     * @throws TestError if the value is not false.
     */
    public void assertFalse(boolean value, Function0<String> message)
    {
        if (value)
        {
            throw new TestError(getFullName(), getMessageLines(message, false, true));
        }
    }

    /**
     * Assert that the provided value is null. If it is not null, then a TestError will
     * be thrown.
     * @param value The value to check.
     */
    public void assertNull(Object value)
    {
        assertNull(value, null);
    }

    /**
     * Assert that the provided value is null. If it is not null, then a TestError will
     * be thrown with the provided message.
     * @param value The value to check.
     * @param message The message to show if the value is not null.
     */
    public void assertNull(Object value, String message)
    {
        if (value != null)
        {
            throw new TestError(getFullName(), getMessageLines(message, null, value));
        }
    }

    /**
     * Assert that the provided value is not null. If it is null, then a TestError will
     * be thrown.
     * @param value The value to check.
     */
    public void assertNotNull(Object value)
    {
        assertNotNull(value, null);
    }

    /**
     * Assert that the provided value is not null. If it is null, then a TestError will
     * be thrown with the provided message.
     * @param value The value to check.
     * @param message The message to show if the value is null.
     */
    public void assertNotNull(Object value, String message)
    {
        if (value == null)
        {
            throw new TestError(getFullName(), getMessageLines(message, "not null", value));
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
            throw new TestError(getFullName(), getMessageLines((String)null, "not null and not empty", value));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length() != 0
     */
    public void assertNotNullAndNotEmpty(String value, String message)
    {
        if (Strings.isNullOrEmpty(value))
        {
            throw new TestError(getFullName(), getMessageLines(message, "not null and not empty", value));
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
            throw new TestError(getFullName(), getMessageLines((String)null, "not null and not empty", value));
        }
    }

    /**
     * Assert that the provided value is not null and not empty.
     * @param value The value to check.
     * @preCondition variableName != null && variableName.length() > 0
     * @postCondition value != null && value.length() != 0
     */
    public void assertNotNullAndNotEmpty(Iterable<?> value, String message)
    {
        assertNotNull(value, message);
        if (!value.any())
        {
            throw new TestError(getFullName(), getMessageLines(message, "not null and not empty", value));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param <T> The type of values to compare.
     */
    public <T> void assertEqual(T expected, T actual)
    {
        this.assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
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
            throw new TestError(this.getFullName(), Test.getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param <T> The type of values to compare.
     */
    public <T> void assertEqual(ComparableWithError<T> expected, T actual, T marginOfError)
    {
        this.assertEqual(expected, actual, marginOfError, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal
     * @param <T> The type of values to compare.
     */
    public <T> void assertEqual(ComparableWithError<T> expected, T actual, T marginOfError, String message)
    {
        if (!Comparer.equal(expected, actual, marginOfError))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(byte expected, Byte actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(byte expected, Byte actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(int expected, Byte actual)
    {
        assertEqual(expected, actual == null ? null : actual.byteValue(), null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(int expected, Byte actual, String message)
    {
        if (actual == null || !Comparer.equal(expected, actual.intValue()))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(char expected, char actual)
    {
        this.assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(char expected, char actual, String message)
    {
        if (expected != actual)
        {
            throw new TestError(this.getFullName(), Test.getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(char expected, Character actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(char expected, Character actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Character expected, char actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Character expected, char actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(short expected, short actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(short expected, short actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(short expected, Short actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(short expected, Short actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Short expected, short actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Short expected, short actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(int expected, int actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(int expected, int actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(int expected, Integer actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(int expected, Integer actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Integer expected, int actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Integer expected, int actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(long expected, long actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(long expected, long actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(Long expected, long actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Long expected, long actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public <T> void assertEqual(long expected, Long actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(long expected, Long actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, float actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, float actual, String message)
    {
        if (expected != actual)
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, Float actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(float expected, Float actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Float expected, float actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Float expected, float actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, double actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, double actual, String message)
    {
        if (expected != actual)
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, Double actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(double expected, Double actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Double expected, double actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are assertEqual. If they are not assertEqual, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Double expected, double actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal with the provided margin of error. If they are not
     * equal with the provided margin of error, then a TestError will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param marginOfError The margin of error to allow when comparing the two values.
     */
    public void assertEqual(double expected, double actual, double marginOfError)
    {
        assertEqual(expected, actual, marginOfError, null);
    }

    /**
     * Assert that the provided values are equal with the provided margin of error. If they are not
     * equal with the provided margin of error, then a TestError will be thrown with the
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
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     */
    public void assertEqual(Throwable expected, Throwable actual)
    {
        assertEqual(expected, actual, null);
    }

    /**
     * Assert that the provided values are equal. If they are not equal, then a TestError
     * will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not equal.
     */
    public void assertEqual(Throwable expected, Throwable actual, String message)
    {
        if (!Comparer.equal(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the lines in the provided stream are equal to the provided expected lines. If
     * they are not equal, then a TestError will be thrown.
     * @param expected The expected lines.
     * @param stream The stream that contains the actual text.
     */
    public void assertLinesEqual(Iterable<String> expected, InMemoryCharacterStream stream)
    {
        PreCondition.assertNotNull(expected, "expected");
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertNotDisposed(stream, "stream");

        this.assertLinesEqual(expected, stream.getText().await());
    }

    /**
     * Assert that the lines in the provided stream are equal to the provided expected lines. If
     * they are not equal, then a TestError will be thrown.
     * @param expected The expected lines.
     * @param stream The stream that contains the actual text.
     * @param message The message to show if the values are not equal.
     */
    public void assertLinesEqual(Iterable<String> expected, InMemoryCharacterStream stream, String message)
    {
        PreCondition.assertNotNull(expected, "expected");
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertNotDisposed(stream, "stream");

        this.assertLinesEqual(expected, stream.getText().await(), message);
    }

    /**
     * Assert that the lines in the provided String are equal to the provided expected lines. If
     * they are not equal, then a TestError will be thrown.
     * @param expected The expected lines.
     * @param text The String that contains the actual text.
     */
    public void assertLinesEqual(Iterable<String> expected, String text)
    {
        PreCondition.assertNotNull(expected, "expected");
        PreCondition.assertNotNull(text, "text");

        this.assertLinesEqual(expected, Strings.iterateLines(text));
    }

    /**
     * Assert that the lines in the provided String are equal to the provided expected lines. If
     * they are not equal, then a TestError will be thrown.
     * @param expected The expected lines.
     * @param text The String that contains the actual text.
     * @param message The message to show if the values are not equal.
     */
    public void assertLinesEqual(Iterable<String> expected, String text, String message)
    {
        PreCondition.assertNotNull(expected, "expected");
        PreCondition.assertNotNull(text, "text");

        this.assertLinesEqual(expected, Strings.iterateLines(text), message);
    }

    /**
     * Assert that the lines in the provided Iterator are equal to the provided expected lines. If
     * they are not equal, then a TestError will be thrown.
     * @param expected The expected lines.
     * @param lines The Iterator that contains the actual lines.
     */
    public void assertLinesEqual(Iterable<String> expected, Iterator<String> lines)
    {
        PreCondition.assertNotNull(expected, "expected");
        PreCondition.assertNotNull(lines, "lines");

        this.assertEqual(expected, lines.toList());
    }

    /**
     * Assert that the lines in the provided Iterator are equal to the provided expected lines. If
     * they are not equal, then a TestError will be thrown.
     * @param expected The expected lines.
     * @param lines The Iterator that contains the actual text.
     * @param message The message to show if the values are not equal.
     */
    public void assertLinesEqual(Iterable<String> expected, Iterator<String> lines, String message)
    {
        PreCondition.assertNotNull(expected, "expected");
        PreCondition.assertNotNull(lines, "lines");

        this.assertEqual(expected, lines.toList(), message);
    }

    /**
     * Assert that the provided values are not equal. If they are equal, then a TestError
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
     * Assert that the provided values are not equal. If they are equal, then a TestError
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
            throw new TestError(getFullName(), getMessageLines(message, "not " + expected, actual));
        }
    }

    /**
     * Assert that the provided values point to the same object. If they don't point to the same
     * object, then a TestError will be thrown.
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
     * object, then a TestError will be thrown with the provided message.
     * @param expected The first value to compare.
     * @param actual The second value to compare.
     * @param message The message to show if the values are not the same.
     * @param <T> The type of values to compare.
     */
    public <T> void assertSame(T expected, T actual, String message)
    {
        if (!Comparer.same(expected, actual))
        {
            throw new TestError(getFullName(), getMessageLines(message, expected, actual));
        }
    }

    /**
     * Assert that the provided values don't point to the same object. If they do point to the same
     * object, then a TestError will be thrown.
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
     * object, then a TestError will be thrown with the provided message.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @param message The message to show if the values are the same.
     * @param <T> The type of values to compare.
     */
    public <T> void assertNotSame(T lhs, T rhs, String message)
    {
        if (Comparer.same(lhs, rhs))
        {
            throw new TestError(getFullName(), getMessageLines(message, "not " + lhs, rhs));
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
            throw new TestError(getFullName(), getMessageLines(message, "less than " + Objects.toString(upperBound), value));
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
            throw new TestError(getFullName(), getMessageLines(message, "less than or equal to " + Objects.toString(rhs), lhs));
        }
    }

    public <T> void assertGreaterThanOrEqualTo(Comparable<T> value, T lowerBound)
    {
        assertGreaterThanOrEqualTo(value, lowerBound, null);
    }

    public <T> void assertGreaterThanOrEqualTo(Comparable<T> value, T lowerBound, String message)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            throw new TestError(getFullName(), getMessageLines(message, "greater than or equal to " + Objects.toString(lowerBound), value));
        }
    }

    public <T> void assertGreaterThanOrEqualTo(ComparableWithError<T> value, T lowerBound, T marginOfError)
    {
        assertGreaterThanOrEqualTo(value, lowerBound, marginOfError, null);
    }

    public <T> void assertGreaterThanOrEqualTo(ComparableWithError<T> value, T lowerBound, T marginOfError, String message)
    {
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound, marginOfError))
        {
            throw new TestError(getFullName(), getMessageLines(message, "greater than or equal to " + Objects.toString(lowerBound) + "(+/- " + marginOfError + ")", value));
        }
    }

    public void assertGreaterThanOrEqualTo(double value, double lowerBound)
    {
        assertGreaterThanOrEqualTo(value, lowerBound, null);
    }

    public void assertGreaterThanOrEqualTo(double value, double lowerBound, String message)
    {
        if (value < lowerBound)
        {
            throw new TestError(getFullName(), getMessageLines(message, "greater than or equal to " + Objects.toString(lowerBound), value));
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
            throw new TestError(getFullName(), getMessageLines(message, "greater than " + Objects.toString(rhs), lhs));
        }
    }

    public void assertGreaterThan(double value, double lowerBound)
    {
        this.assertGreaterThanOrEqualTo(value, lowerBound, null);
    }

    public void assertGreaterThan(double value, double lowerBound, String message)
    {
        if (value <= lowerBound)
        {
            throw new TestError(getFullName(), getMessageLines(message, "greater than " + Objects.toString(lowerBound), value));
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
            throw new TestError(getFullName(), getMessageLines(message, "between " + Objects.toString(lowerBound) + " and " + Objects.toString(upperBound), value));
        }
    }

    /**
     * Assert that when the provided action is run it throws an exception that is equal to the
     * provided exception.
     * @param expectedException The expected exception.
     * @param action The action to run.
     */
    public void assertThrows(Throwable expectedException, Action0 action)
    {
        this.assertThrows(action, expectedException);
    }

    /**
     * Assert that when the provided action is run it throws an exception that is equal to the
     * provided exception.
     * @param action The action to run.
     * @param expectedException The expected exception.
     */
    public void assertThrows(Action0 action, Throwable expectedException)
    {
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(expectedException, "expectedException");

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
            throw new TestError(
                this.getFullName(),
                "Expected a " + Types.getFullTypeName(expectedException) + " to be thrown with " + (Strings.isNullOrEmpty(expectedException.getMessage()) ? "no message" : "the message \"" + expectedException.getMessage() + "\"") + ".");
        }
        else if (!Comparer.equal(expectedException, exceptionThrown, Exceptions.defaultErrorTypesToGoPast))
        {
            throw new TestError(
                this.getFullName(),
                Test.getMessageLines("Incorrect exception thrown", expectedException, exceptionThrown),
                exceptionThrown);
        }
    }

    /**
     * Assert that when the provided action is run it throws an exception that is equal to the
     * provided exception.
     * @param expectedExceptionType The expected exception type.
     * @param action The action to run.
     */
    public <TError extends Throwable> TError assertThrows(Class<TError> expectedExceptionType, Action0 action)
    {
        return this.assertThrows(action, expectedExceptionType);
    }

    /**
     * Assert that when the provided action is run it throws an exception that is equal to the
     * provided exception.
     * @param action The action to run.
     * @param expectedExceptionType The expected exception type.
     */
    public <TError extends Throwable> TError assertThrows(Action0 action, Class<TError> expectedExceptionType)
    {
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(expectedExceptionType, "expectedExceptionType");

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
            throw new TestError(
                this.getFullName(),
                "Expected a " + Types.getFullTypeName(expectedExceptionType) + " to be thrown.");
        }

        final Throwable unwrappedException = Exceptions.unwrap(exceptionThrown);
        if (!expectedExceptionType.equals(Types.getType(unwrappedException)))
        {
            throw new TestError(
                this.getFullName(),
                Test.getMessageLines("Incorrect exception thrown", expectedExceptionType, exceptionThrown),
                exceptionThrown);
        }

        final TError result = Types.as(unwrappedException, expectedExceptionType);

        PostCondition.assertNotNull(result, "result");

        return result;
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
            throw new TestError(
                getFullName(),
                "Expected " + Strings.escapeAndQuote(value) + " to start with " + Strings.escapeAndQuote(prefix) + ".");
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
            throw new TestError(
                getFullName(),
                "Expected " + Strings.escapeAndQuote(value) + " to start with " + Strings.escapeAndQuote(prefix) + ".");
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
            throw new TestError(
                getFullName(),
                "Expected " + Strings.escapeAndQuote(value) + " to end with " + Strings.escapeAndQuote(suffix) + ".");
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
            throw new TestError(
                getFullName(),
                Iterable.create(
                    message,
                    "Expected " + Strings.escapeAndQuote(value) + " to end with " + Strings.escapeAndQuote(suffix) + "."));
        }
    }

    public void assertContains(String value, String substring)
    {
        if (!Strings.contains(value, substring))
        {
            throw new TestError(
                getFullName(),
                "Expected " + Strings.escapeAndQuote(value) + " to contain " + Strings.escapeAndQuote(substring) + ".");
        }
    }

    public void assertContains(String value, String substring, String message)
    {
        if (!Strings.contains(value, substring))
        {
            throw new TestError(
                getFullName(),
                Iterable.create(
                    message,
                    "Expected " + Strings.escapeAndQuote(value) + " to contain " + Strings.escapeAndQuote(substring) + "."));
        }
    }

    public <T> void assertOneOf(T[] possibleValues, T value)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");

        if (!Array.contains(possibleValues, value))
        {
            throw new TestError(
                getFullName(),
                AssertionMessages.oneOf(value, possibleValues, "Actual value"));
        }
    }

    public <T> void assertOneOf(Iterable<T> possibleValues, T value)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleValues, "possibleValues");

        if (!possibleValues.contains(value))
        {
            throw new TestError(
                getFullName(),
                AssertionMessages.oneOf(value, possibleValues, "Actual value"));
        }
    }

    /**
     * Assert that the provided value is an instance of the provided type.
     * @param value The value to check.
     * @param type The type to look for.
     */
    public void assertInstanceOf(Object value, Class<?> type)
    {
        if (!Types.instanceOf(value, type))
        {
            throw new TestError(
                getFullName(),
                AssertionMessages.instanceOf(value, type, "value"));
        }
    }

    /**
     * Assert that the provided value is an instance of one of the provided types.
     * @param value The value to check.
     * @param types The types to look for.
     */
    public void assertInstanceOf(Object value, Iterable<Class<?>> types)
    {
        if (!Types.instanceOf(value, types))
        {
            throw new TestError(
                getFullName(),
                AssertionMessages.instanceOf(value, types, "value"));
        }
    }

    /**
     * Cause the test to fail instantly.
     */
    public void fail()
    {
        throw new TestError(getFullName(), "Unexplained test failure.");
    }

    /**
     * Cause the test to fail instantly with the provided message.
     * @param message The message to display that should explain the failure.
     */
    public void fail(String message)
    {
        PreCondition.assertNotNullAndNotEmpty(message, "message");

        throw new TestError(getFullName(), message);
    }

    /**
     * Cause the test to fail instantly with the message create the provided Exception.
     * @param e The Exception that caused the test failure.
     */
    public void fail(Throwable e)
    {
        PreCondition.assertNotNull(e, "e");

        String failMessage = e.getClass().getSimpleName();
        final String eMessage = e.getMessage();
        if (!Strings.isNullOrEmpty(eMessage))
        {
            failMessage += ": " + eMessage;
        }
        throw new TestError(getFullName(), failMessage);
    }

    private static Iterable<String> getMessageLines(Function0<String> message, Object expected, Object actual)
    {
        return Test.getMessageLines(message == null ? (String)null : message.run(), expected, actual);
    }

    private static Iterable<String> getMessageLines(String message, Object expected, Object actual)
    {
        final List<String> result = List.create();
        if (!Strings.isNullOrEmpty(message))
        {
            result.add("Message:  " + message);
        }

        final String expectedString = Test.toString(expected);
        final String actualString = Test.toString(actual);
        result.add("Expected: " + addType(expected, actual, expectedString, actualString));
        result.add("Actual:   " + addType(actual, expected, actualString, expectedString));

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    private static String toString(Object value)
    {
        final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
        if (value instanceof String)
        {
            stream.write(Strings.escapeAndQuote(Objects.toString(value))).await();
        }
        else if (value instanceof Class<?>)
        {
            stream.write(Types.getFullTypeName((Class<?>)value)).await();
        }
        else
        {
            stream.write(Strings.escape(Objects.toString(value))).await();

            final Throwable error = Types.as(value, Throwable.class);
            if (error != null)
            {
                Throwable cause = error.getCause();
                if (cause != null)
                {
                    final IndentedCharacterWriteStream indentedStream = IndentedCharacterWriteStream.create(stream);
                    boolean indented = false;
                    while (cause != null)
                    {
                        indentedStream.writeLine().await();
                        if (!indented)
                        {
                            indented = true;
                            indentedStream.increaseIndent();
                        }
                        indentedStream.write("Caused by: " + Strings.escape(cause.toString())).await();
                        cause = cause.getCause();
                    }
                }
            }
        }
        final String result = stream.getText().await();

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    private static String addType(Object value, Object otherValue, String valueString, String otherValueString)
    {
        return valueString + (value == null || !valueString.equals(otherValueString) ? "" : " (" + value.getClass().getName() + ")");
    }
}
