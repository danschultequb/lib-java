package qub;

/**
 * A Console object that is used for running unit tests for other applications.
 */
public class TestConsole extends Console
{
    private final String singleIndent;
    private String currentIndent;

    private int testsPassed;
    private int testsFailed;

    /**
     * Create a new TestConsole with no command line arguments.
     */
    public TestConsole()
    {
        this(null);
    }

    /**
     * Create a new TestConsole with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments for this application.
     */
    public TestConsole(String[] commandLineArgumentStrings)
    {
        super(commandLineArgumentStrings);

        singleIndent = "  ";
        currentIndent = "";

        testsPassed = 0;
        testsFailed = 0;
    }

    /**
     * Increase the indent of the Console output.
     */
    private void increaseIndent()
    {
        currentIndent += singleIndent;
    }

    /**
     * Decrease the indent of the Console output.
     */
    private void decreaseIndent()
    {
        currentIndent = currentIndent.substring(0, currentIndent.length() - singleIndent.length());
    }

    /**
     * Get the total number of tests that this TestConsole has run.
     * @return The total number of tests that this TestConsole has run.
     */
    public int getTestsRun()
    {
        return testsPassed + testsFailed;
    }

    /**
     * Increment the number of tests that have passed.
     */
    void incrementTestsPassed()
    {
        ++testsPassed;
    }

    /**
     * Increment the number of tests that have failed.
     */
    void incrementTestsFailed()
    {
        ++testsFailed;
    }

    /**
     * Get the number of passing tests that this TestConsole has run.
     * @return The number of passing tests that this TestConsole has run.
     */
    public int getTestsPassed()
    {
        return testsPassed;
    }

    /**
     * Get the number of failing tests that this TestConsole has run.
     * @return The number of failing tests that this TestConsole has run.
     */
    public int getTestsFailed()
    {
        return testsFailed;
    }



    @Override
    public boolean writeLine(String line)
    {
        return write(currentIndent) && super.writeLine(line);
    }

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    public void testGroup(String testGroupName, final Action0 testGroupAction)
    {
        testGroup(testGroupName, testGroupAction == null ? null : new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole testConsole)
            {
                testGroupAction.run();
            }
        });
    }

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    public void testGroup(String testGroupName, Action1<TestConsole> testGroupAction)
    {
        writeLine(testGroupName);
        increaseIndent();

        if (testGroupAction != null)
        {
            testGroupAction.run(this);
        }

        decreaseIndent();
    }

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    public void test(String testName, final Action0 testAction)
    {
        test(testName, testAction == null ? null : new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole arg1)
            {
                testAction.run();
            }
        });
    }

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    public void test(String testName, Action1<TestConsole> testAction)
    {
        writeLine(testName);
        increaseIndent();

        if (testAction != null)
        {
            try
            {
                testAction.run(this);
                incrementTestsPassed();
            }
            catch (TestAssertionFailure e)
            {
                incrementTestsFailed();

                writeLine("Test Failure:");
                increaseIndent();
                for (final String messageLine : e.getMessageLines())
                {
                    if (messageLine != null)
                    {
                        writeLine(messageLine);
                    }
                }
                decreaseIndent();

                writeStackTrace(e);
            }
            catch (Throwable t)
            {
                incrementTestsFailed();

                writeLine("Unhandled Exception: " + t.getClass().getName());

                final String message = t.getMessage();
                if (message != null && !message.isEmpty())
                {
                    writeLine("Message: " + t.getMessage());
                }
                writeStackTrace(t);
            }
        }

        decreaseIndent();
    }

    /**
     * Write the stack trace of the provided Throwable to the output stream.
     * @param t The Throwable to write the stack trace of.
     */
    private void writeStackTrace(Throwable t)
    {
        final StackTraceElement[] stackTraceElements = t.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0)
        {
            writeLine("Stack Trace:");
            increaseIndent();
            for (StackTraceElement stackTraceElement : stackTraceElements)
            {
                writeLine("at " + stackTraceElement.toString());
            }
            decreaseIndent();
        }
    }

    /**
     * Write the current statistics of this TestConsole.
     */
    public void writeSummary()
    {
        writeLine("Tests Run:    " + (testsPassed + testsFailed));
        writeLine("Tests Passed: " + getTestsPassed());
        writeLine("Tests Failed: " + getTestsFailed());
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
            messageLines[0] = message;
            nextMessageIndex = 1;
        }

        messageLines[nextMessageIndex] = "Expected: " + expected;
        ++nextMessageIndex;
        messageLines[nextMessageIndex] = "Actual:   " + actual;

        return messageLines;
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
            throw new TestAssertionFailure(getMessageLines(message, true, false));
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
            throw new TestAssertionFailure(getMessageLines(message, false, true));
        }
    }
}
