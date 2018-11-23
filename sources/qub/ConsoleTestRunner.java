package qub;

/**
 * A Console object that is used for running unit tests for other applications.
 */
public class ConsoleTestRunner extends Console implements TestRunner
{
    private BasicTestRunner testRunner;

    private final String singleIndent;
    private String currentIndent;
    private boolean onNewLine;

    /**
     * Create a new ConsoleTestRunner with no command line arguments.
     */
    public ConsoleTestRunner()
    {
        this((String[])null);
    }

    /**
     * Create a new ConsoleTestRunner with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments for this application.
     */
    public ConsoleTestRunner(String[] commandLineArgumentStrings)
    {
        this(CommandLine.parse(commandLineArgumentStrings));
    }

    /**
     * Create a new ConsoleTestRunner with the provided command line arguments.
     * @param commandLine The command line arguments for this application.
     */
    public ConsoleTestRunner(CommandLine commandLine)
    {
        super(commandLine);

        final CommandLineArgument debugArgument = commandLine.remove("debug");
        boolean debug = debugArgument != null && (debugArgument.getValue() == null || debugArgument.getValue().equals("true"));

        final CommandLineArgument testPatternArgument = commandLine.remove("pattern");
        PathPattern testPattern = null;
        if (testPatternArgument != null && testPatternArgument.getValue() != null && !testPatternArgument.getValue().isEmpty())
        {
            testPattern = PathPattern.parse(testPatternArgument.getValue());
        }

        if (debug)
        {
            writeLine("TestPattern: " + (testPattern == null ? "null" : "\"" + testPattern + "\""));
        }

        testRunner = new BasicTestRunner(this, testPattern);

        final List<TestGroup> testGroupsWrittenToConsole = new ArrayList<>();
        testRunner.afterTestGroup((TestGroup testGroup) ->
        {
            if (testGroupsWrittenToConsole.remove(testGroup))
            {
                ConsoleTestRunner.this.decreaseIndent();
            }
        });
        testRunner.beforeTest((Test test) ->
        {
            final Stack<TestGroup> testGroupsToWrite = new Stack<>();
            TestGroup currentTestGroup = test.getParentTestGroup();
            while (currentTestGroup != null && !testGroupsWrittenToConsole.contains(currentTestGroup))
            {
                testGroupsToWrite.push(currentTestGroup);
                currentTestGroup = currentTestGroup.getParentTestGroup();
            }

            while (testGroupsToWrite.any())
            {
                final TestGroup testGroupToWrite = testGroupsToWrite.pop();

                final String skipMessage = testGroupToWrite.getSkipMessage();
                ConsoleTestRunner.this.writeLine(testGroupToWrite.getName() + (!testGroupToWrite.shouldSkip() ? "" : " - Skipped" + (Strings.isNullOrEmpty(skipMessage) ? "" : ": " + skipMessage)));
                testGroupsWrittenToConsole.add(testGroupToWrite);
                ConsoleTestRunner.this.increaseIndent();
            }

            write(test.getName());
            ConsoleTestRunner.this.increaseIndent();
        });
        testRunner.afterTestSuccess((Test test) ->
        {
            ConsoleTestRunner.this.writeLine(" - Passed");
        });
        testRunner.afterTestFailure((Test test, TestAssertionFailure failure) ->
        {
            ConsoleTestRunner.this.writeLine(" - Failed");
            writeFailure(failure);
        });
        testRunner.afterTestError((Test test, Throwable error) ->
        {
            ConsoleTestRunner.this.writeLine(" - Error");
            writeFailureCause(error);
        });
        testRunner.afterTestSkipped((Test test) ->
        {
            final String skipMessage = test.getSkipMessage();
            ConsoleTestRunner.this.writeLine(" - Skipped" + (Strings.isNullOrEmpty(skipMessage) ? "" : ": " + skipMessage));
        });
        testRunner.afterTest((Test test) ->
        {
            ConsoleTestRunner.this.decreaseIndent();
        });

        singleIndent = "  ";
        currentIndent = "";
        onNewLine = true;
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

    public int getFailedTestCount()
    {
        return testRunner.getFailedTestCount();
    }

    @Override
    public Result<Boolean> write(String line, Object... formattedStringArguments)
    {
        Result<Boolean> result = Result.successTrue();

        if (onNewLine)
        {
            result = super.write(currentIndent);
        }

        if (Booleans.isTrue(result.getValue()))
        {
            result = super.write(line, formattedStringArguments);
            onNewLine = (line != null && line.endsWith("\n"));
        }

        return result;
    }

    @Override
    public Result<Boolean> writeLine(String line, Object... formattedStringArguments)
    {
        Result<Boolean> result = Result.successTrue();

        if (onNewLine)
        {
            result = super.write(currentIndent);
        }

        if (Booleans.isTrue(result.getValue()))
        {
            result = super.writeLine(line, formattedStringArguments);
            onNewLine = true;
        }

        return result;
    }

    private void writeFailure(TestAssertionFailure failure)
    {
        increaseIndent();
        writeMessageLines(failure);
        writeStackTrace(failure);
        decreaseIndent();

        Throwable cause = failure.getCause();
        if (cause != null)
        {
            writeFailureCause(cause);
        }
    }

    private void writeMessageLines(TestAssertionFailure failure)
    {
        for (final String messageLine : failure.getMessageLines())
        {
            if (messageLine != null)
            {
                writeLine(messageLine);
            }
        }
    }

    private void writeMessage(Throwable throwable)
    {
        if (throwable instanceof TestAssertionFailure)
        {
            writeMessageLines((TestAssertionFailure)throwable);
        }
        else if (!Strings.isNullOrEmpty(throwable.getMessage()))
        {
            writeLine("Message: " + throwable.getMessage());
        }
    }

    private void writeFailureCause(Throwable cause)
    {
        if (cause instanceof ErrorIterable)
        {
            final ErrorIterable errors = (ErrorIterable)cause;

            writeLine("Caused by:");
            int causeNumber = 0;
            for (final Throwable innerCause : errors)
            {
                ++causeNumber;
                write(causeNumber + ") " + innerCause.getClass().getName());

                increaseIndent();
                writeMessage(innerCause);
                writeStackTrace(innerCause);
                decreaseIndent();

                final Throwable nextCause = innerCause.getCause();
                if (nextCause != null && nextCause != innerCause)
                {
                    increaseIndent();
                    writeFailureCause(nextCause);
                    decreaseIndent();
                }
            }
        }
        else
        {
            writeLine("Caused by: " + cause.getClass().getName());

            increaseIndent();
            writeMessage(cause);
            writeStackTrace(cause);
            decreaseIndent();

            final Throwable nextCause = cause.getCause();
            if (nextCause != null && nextCause != cause)
            {
                increaseIndent();
                writeFailureCause(nextCause);
                decreaseIndent();
            }
        }
    }

    @Override
    public Skip skip()
    {
        return testRunner.skip();
    }

    @Override
    public Skip skip(boolean toSkip)
    {
        return testRunner.skip(toSkip);
    }

    @Override
    public Skip skip(boolean toSkip, String message)
    {
        return testRunner.skip(toSkip, message);
    }

    @Override
    public Skip skip(String message)
    {
        return testRunner.skip(message);
    }

    @Override
    public void testGroup(String testGroupName, Action0 testGroupAction)
    {
        testRunner.testGroup(testGroupName, testGroupAction);
    }

    @Override
    public void testGroup(Class<?> testClass, Action0 testGroupAction)
    {
        testRunner.testGroup(testClass, testGroupAction);
    }

    @Override
    public void testGroup(String testGroupName, Skip skip, Action0 testGroupAction)
    {
        testRunner.testGroup(testGroupName, skip, testGroupAction);
    }

    @Override
    public void testGroup(Class<?> testClass, Skip skip, Action0 testGroupAction)
    {
        testRunner.testGroup(testClass, skip, testGroupAction);
    }

    @Override
    public void test(String testName, Action1<Test> testAction)
    {
        testRunner.test(testName, testAction);
    }

    @Override
    public void test(String testName, Skip skip, Action1<Test> testAction)
    {
        testRunner.test(testName, skip, testAction);
    }

    @Override
    public void beforeTestGroup(Action1<TestGroup> beforeTestGroupAction)
    {
        testRunner.beforeTestGroup(beforeTestGroupAction);
    }

    @Override
    public void afterTestGroupError(Action2<TestGroup,Throwable> afterTestGroupErrorAction)
    {
        testRunner.afterTestGroupError(afterTestGroupErrorAction);
    }

    @Override
    public void afterTestGroupSkipped(Action1<TestGroup> afterTestGroupSkipped)
    {
        testRunner.afterTestGroupSkipped(afterTestGroupSkipped);
    }

    @Override
    public void afterTestGroup(Action1<TestGroup> afterTestGroupAction)
    {
        testRunner.afterTestGroup(afterTestGroupAction);
    }

    @Override
    public void beforeTest(Action1<Test> beforeTestAction)
    {
        testRunner.beforeTest(beforeTestAction);
    }

    @Override
    public void afterTestFailure(Action2<Test,TestAssertionFailure> afterTestFailureAction)
    {
        testRunner.afterTestFailure(afterTestFailureAction);
    }

    @Override
    public void afterTestError(Action2<Test,Throwable> afterTestErrorAction)
    {
        testRunner.afterTestError(afterTestErrorAction);
    }

    @Override
    public void afterTestSuccess(Action1<Test> afterTestSuccessAction)
    {
        testRunner.afterTestSuccess(afterTestSuccessAction);
    }

    @Override
    public void afterTestSkipped(Action1<Test> afterTestSkippedAction)
    {
        testRunner.afterTestSkipped(afterTestSkippedAction);
    }

    @Override
    public void afterTest(Action1<Test> afterTestAction)
    {
        testRunner.afterTest(afterTestAction);
    }

    @Override
    public boolean hasNetworkConnection()
    {
        return testRunner.hasNetworkConnection();
    }

    /**
     * Write the stack trace of the provided Throwable to the output stream.
     * @param t The Throwable to writeByte the stack trace of.
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
     * Write the current statistics of this ConsoleTestRunner.
     */
    public void writeSummary()
    {
        final Iterable<Test> skippedTests = testRunner.getSkippedTests();
        if (skippedTests.any())
        {
            writeLine("Skipped Tests:");
            increaseIndent();
            int testSkippedNumber = 1;
            for (final Test skippedTest : skippedTests)
            {
                final String skipMessage = skippedTest.getSkipMessage();
                writeLine(testSkippedNumber + ") " + skippedTest.getFullName() + (Strings.isNullOrEmpty(skipMessage) ? "" : ": " + skipMessage));
                ++testSkippedNumber;
            }
            decreaseIndent();

            writeLine();
        }

        final Iterable<TestAssertionFailure> testFailures = testRunner.getTestFailures();
        if (testFailures.any())
        {
            writeLine("Test failures:");
            increaseIndent();

            int testFailureNumber = 1;
            for (final TestAssertionFailure failure : testFailures)
            {
                writeLine(testFailureNumber + ") " + failure.getFullTestName());
                ++testFailureNumber;
                increaseIndent();
                writeFailure(failure);
                decreaseIndent();

                writeLine();
            }

            decreaseIndent();
        }

        final Iterable<TestError> testErrors = testRunner.getTestErrors();
        if (testErrors.any())
        {
            writeLine("Test errors:");
            increaseIndent();

            int testErrorNumber = 1;
            for (final TestError error : testErrors)
            {
                writeLine(testErrorNumber + ") " + error.getTestScope());
                ++testErrorNumber;
                increaseIndent();
                writeFailureCause(error.getError());
                decreaseIndent();

                writeLine();
            }

            decreaseIndent();
        }

        writeLine("Tests Run:      " + testRunner.getFinishedTestCount());
        writeLine("Tests Passed:   " + testRunner.getPassedTestCount());
        writeLine("Tests Failed:   " + testRunner.getFailedTestCount());
        writeLine("Tests Skipped:  " + testRunner.getSkippedTestCount());
        writeLine("Test Errors:    " + testRunner.getErrorTestCount());
    }

    public static void main(String[] args)
    {
        int testsFailed;

        try (final ConsoleTestRunner console = new ConsoleTestRunner(args))
        {
            final CommandLine commandLine = console.getCommandLine();
            final boolean debug = commandLine.remove("debug") != null;

            final Stopwatch stopwatch = console.getStopwatch();
            stopwatch.start();

            for (final CommandLineArgument argument : commandLine.getArguments())
            {
                if (argument.getName() == null)
                {
                    final String fullClassName = argument.getValue();
                    if (debug)
                    {
                        console.write("Looking for class \"" + fullClassName + "\"...");
                    }

                    Class<?> testClass = null;
                    try
                    {
                        testClass = ConsoleTestRunner.class.getClassLoader().loadClass(fullClassName);
                        if (debug)
                        {
                            console.writeLine("Found!");
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        if (debug)
                        {
                            console.writeLine("Couldn't find.");
                        }
                    }

                    if (testClass != null)
                    {
                        if (debug)
                        {
                            console.write("Looking for static test(TestRunner) method in \"" + fullClassName + "\"...");
                        }

                        java.lang.reflect.Method testMethod = null;
                        try
                        {
                            testMethod = testClass.getMethod("test", TestRunner.class);
                            if (debug)
                            {
                                console.writeLine("Found!");
                            }
                        }
                        catch (NoSuchMethodException e)
                        {
                            if (debug)
                            {
                                console.writeLine("Couldn't find.");
                            }
                        }

                        if (testMethod != null)
                        {
                            try
                            {
                                testMethod.invoke(null, console);
                            }
                            catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            console.writeLine();
            console.writeSummary();

            final Duration totalTestsDuration = stopwatch.stop();
            console.writeLine("Tests Duration: " + totalTestsDuration.toSeconds().toString("0.0"));

            testsFailed = console.getFailedTestCount();
        }
        catch (Exception ignored)
        {
            testsFailed = -1;
        }

        System.exit(testsFailed);
    }
}
