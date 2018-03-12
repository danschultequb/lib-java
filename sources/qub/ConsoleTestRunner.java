package qub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A Console object that is used for running unit tests for other applications.
 */
public class ConsoleTestRunner extends Console implements TestRunner
{
    private TestRunnerBase testRunner;

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

        testRunner = new TestRunnerBase(debug, testPattern);

        final List<TestGroup> testGroupsWrittenToConsole = new ArrayList<>();
        testRunner.setOnTestGroupFinished(new Action1<TestGroup>()
        {
            @Override
            public void run(TestGroup testGroup)
            {
                if (testGroupsWrittenToConsole.remove(testGroup))
                {
                    ConsoleTestRunner.this.decreaseIndent();
                }
            }
        });
        testRunner.setOnTestStarted(new Action1<Test>()
        {
            @Override
            public void run(Test test)
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
                    ConsoleTestRunner.this.writeLine(testGroupToWrite.getName());
                    testGroupsWrittenToConsole.add(testGroupToWrite);
                    ConsoleTestRunner.this.increaseIndent();
                }

                write(test.getName());
                ConsoleTestRunner.this.increaseIndent();
            }
        });
        testRunner.setOnTestPassed(new Action1<Test>()
        {
            @Override
            public void run(Test test)
            {
                ConsoleTestRunner.this.writeLine(" - Passed");
            }
        });
        testRunner.setOnTestFailed(new Action2<Test, TestAssertionFailure>()
        {
            @Override
            public void run(Test test, TestAssertionFailure failure)
            {
                ConsoleTestRunner.this.writeLine(" - Failed");
                ConsoleTestRunner.this.increaseIndent();

                for (final String messageLine : failure.getMessageLines())
                {
                    if (messageLine != null)
                    {
                        ConsoleTestRunner.this.writeLine(messageLine);
                    }
                }

                ConsoleTestRunner.this.decreaseIndent();

                ConsoleTestRunner.this.writeStackTrace(failure);
            }
        });
        testRunner.setOnTestFinished(new Action1<Test>()
        {
            @Override
            public void run(Test test)
            {
                ConsoleTestRunner.this.decreaseIndent();
            }
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
    public boolean write(String line, Object... formattedStringArguments)
    {
        boolean result = true;

        if (onNewLine)
        {
            result = super.write(currentIndent);
        }

        if (result)
        {
            result = super.write(line, formattedStringArguments);
            onNewLine = line != null && line.endsWith("\n");
        }

        return result;
    }

    @Override
    public boolean writeLine(String line, Object... formattedStringArguments)
    {
        boolean result = true;

        if (onNewLine)
        {
            result = super.write(currentIndent);
        }

        if (result)
        {
            result = super.writeLine(line, formattedStringArguments);
            onNewLine = true;
        }

        return result;
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
    public void test(String testName, Action1<Test> testAction)
    {
        testRunner.test(testName, testAction);
    }

    @Override
    public void beforeTest(Action0 beforeTestAction)
    {
        testRunner.beforeTest(beforeTestAction);
    }

    @Override
    public void afterTest(Action0 afterTestAction)
    {
        testRunner.afterTest(afterTestAction);
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
     * Write the current statistics of this ConsoleTestRunner.
     */
    public void writeSummary()
    {
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
                increaseIndent();
                for (final String messageLine : failure.getMessageLines())
                {
                    if (messageLine != null)
                    {
                        writeLine(messageLine);
                    }
                }
                writeStackTrace(failure);
                decreaseIndent();
                decreaseIndent();

                writeLine();
            }

            decreaseIndent();
        }

        writeLine("Tests Run:      " + testRunner.getFinishedTestCount());
        writeLine("Tests Passed:   " + testRunner.getPassedTestCount());
        writeLine("Tests Failed:   " + testRunner.getFailedTestCount());
    }

    public static void main(String[] args)
    {
        int testsFailed = 0;

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

                        Method testMethod = null;
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
                            catch (IllegalAccessException | InvocationTargetException e)
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

        System.exit(testsFailed);
    }
}
