package qub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

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
        this(null);
    }

    /**
     * Create a new ConsoleTestRunner with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments for this application.
     */
    public ConsoleTestRunner(String[] commandLineArgumentStrings)
    {
        super(commandLineArgumentStrings);

        testRunner = new TestRunnerBase();
        testRunner.setOnTestGroupStarted(new Action1<String>()
        {
            @Override
            public void run(String testGroupName)
            {
                writeLine(testGroupName);
                increaseIndent();
            }
        });
        testRunner.setOnTestGroupFinished(new Action1<String>()
        {
            @Override
            public void run(String testGroupName)
            {
                decreaseIndent();
            }
        });
        testRunner.setOnTestStarted(new Action1<String>()
        {
            @Override
            public void run(String testName)
            {
                write(testName);
                increaseIndent();
            }
        });
        testRunner.setOnTestPassed(new Action1<String>()
        {
            @Override
            public void run(String testName)
            {
                writeLine(" - Passed");
            }
        });
        testRunner.setOnTestFailed(new Action2<String, TestAssertionFailure>()
        {
            @Override
            public void run(String testName, TestAssertionFailure failure)
            {
                writeLine(" - Failed");
                increaseIndent();

                for (final String messageLine : failure.getMessageLines())
                {
                    if (messageLine != null)
                    {
                        writeLine(messageLine);
                    }
                }

                decreaseIndent();

                writeStackTrace(failure);
            }
        });
        testRunner.setOnTestFinished(new Action1<String>()
        {
            @Override
            public void run(String testName)
            {
                decreaseIndent();
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

    @Override
    public boolean write(String line)
    {
        boolean result = true;

        if (onNewLine)
        {
            result = super.write(currentIndent);
        }

        if (result)
        {
            result = super.write(line);
            onNewLine = line != null && line.endsWith("\n");
        }

        return result;
    }

    @Override
    public boolean writeLine(String line)
    {
        boolean result = true;

        if (onNewLine)
        {
            result = super.write(currentIndent);
        }

        if (result)
        {
            result = super.writeLine(line);
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

    @Override
    public String escapeAndQuote(String text)
    {
        return testRunner.escapeAndQuote(text);
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
        final ConsoleTestRunner console = new ConsoleTestRunner(args);
        final CommandLine commandLine = console.getCommandLine();
        final boolean debug = commandLine.get("debug") != null;

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
    }
}
