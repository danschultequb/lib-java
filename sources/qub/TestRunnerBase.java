package qub;

public class TestRunnerBase implements TestRunner
{
    private int passedTestCount;
    private int failedTestCount;
    private final List<TestAssertionFailure> testFailures = new SingleLinkList<>();

    private Action1<TestGroup> onTestGroupStarted;
    private Action1<TestGroup> onTestGroupFinished;
    private Action1<Test> onTestStarted;
    private Action1<Test> onTestPassed;
    private Action2<Test,TestAssertionFailure> onTestFailed;
    private Action1<Test> onTestFinished;
    
    private Action0 beforeTestAction;
    private Action0 afterTestAction;
    private TestGroup currentTestGroup;

    private final boolean debug;
    private final PathPattern testPattern;

    public TestRunnerBase(boolean debug, PathPattern testPattern)
    {
        this.debug = debug;
        this.testPattern = testPattern;
    }

    @Override
    public void testGroup(Class<?> testClass, Action0 testGroupAction)
    {
        testGroup(testClass.getSimpleName(), testGroupAction);
    }

    @Override
    public void testGroup(String testGroupName, Action0 testGroupAction)
    {
        if (testGroupAction != null)
        {
            final Action0 beforeTestActionBackup = beforeTestAction;
            final Action0 afterTestActionBackup = afterTestAction;

            currentTestGroup = new TestGroup(testGroupName, currentTestGroup);
            if (onTestGroupStarted != null)
            {
                onTestGroupStarted.run(currentTestGroup);
            }
            
            testGroupAction.run();

            if (onTestGroupFinished != null)
            {
                onTestGroupFinished.run(currentTestGroup);
            }

            beforeTestAction = beforeTestActionBackup;
            afterTestAction = afterTestActionBackup;
            currentTestGroup = currentTestGroup.getParentTestGroup();
        }
    }

    @Override
    public void test(String testName, Action1<Test> testAction)
    {
        if (testAction != null)
        {
            final Test test = new Test(testName, currentTestGroup);
            if (test.matches(testPattern))
            {
                if (onTestStarted != null)
                {
                    onTestStarted.run(test);
                }

                try
                {
                    if (beforeTestAction != null)
                    {
                        beforeTestAction.run();
                    }

                    testAction.run(test);

                    if (afterTestAction != null)
                    {
                        afterTestAction.run();
                    }

                    ++passedTestCount;
                    if (onTestPassed != null)
                    {
                        onTestPassed.run(test);
                    }
                }
                catch (Throwable e)
                {
                    TestAssertionFailure failure;
                    if (e instanceof TestAssertionFailure)
                    {
                        failure = (TestAssertionFailure)e;
                    }
                    else
                    {
                        final List<String> messageLines = new SingleLinkList<>();
                        messageLines.add("Unhandled Exception: " + e.getClass().getName());

                        final String message = e.getMessage();
                        if (message != null && !message.isEmpty())
                        {
                            messageLines.add("Message: " + e.getMessage());
                        }
                        failure = new TestAssertionFailure(test.getFullName(), Array.toStringArray(messageLines), e);
                    }

                    testFailures.add(failure);

                    ++failedTestCount;
                    if (onTestFailed != null)
                    {
                        onTestFailed.run(test, failure);
                    }
                }

                if (onTestFinished != null)
                {
                    onTestFinished.run(test);
                }
            }
        }
    }

    @Override
    public void beforeTest(Action0 beforeTestAction)
    {
        if (beforeTestAction != null)
        {
            final Action0 previousBeforeTestAction = this.beforeTestAction;
            final Action0 newBeforeTestAction = beforeTestAction;
            this.beforeTestAction = new Action0()
            {
                @Override
                public void run()
                {
                    if (previousBeforeTestAction != null)
                    {
                        previousBeforeTestAction.run();
                    }
                    newBeforeTestAction.run();
                }
            };
        }
    }

    @Override
    public void afterTest(Action0 afterTestAction)
    {
        if (afterTestAction != null)
        {
            final Action0 previousAfterTestAction = this.afterTestAction;
            final Action0 newAfterTestAction = afterTestAction;
            this.afterTestAction = new Action0()
            {
                @Override
                public void run()
                {
                    newAfterTestAction.run();
                    if (previousAfterTestAction != null)
                    {
                        previousAfterTestAction.run();
                    }
                }
            };
        }
    }

    @Override
    public String escapeAndQuote(String text)
    {
        String result;
        if (text == null)
        {
            result = "null";
        }
        else
        {
            final StringBuilder builder = new StringBuilder();
            builder.append('\"');

            int start;
            int end;
            if (text.startsWith("\"") && text.endsWith("\""))
            {
                start = 1;
                end = text.length() - 1;
            }
            else
            {
                start = 0;
                end = text.length();
            }

            for (int i = start; i < end; ++i)
            {
                final char textCharacter = text.charAt(i);
                switch (textCharacter)
                {
                    case '\b':
                        builder.append("\\b");
                        break;

                    case '\f':
                        builder.append("\\f");
                        break;

                    case '\n':
                        builder.append("\\n");
                        break;

                    case '\r':
                        builder.append("\\r");
                        break;

                    case '\t':
                        builder.append("\\t");
                        break;

                    default:
                        builder.append(textCharacter);
                        break;
                }
            }

            builder.append('\"');
            result = builder.toString();
        }
        return result;
    }

    /**
     * Set the Action that will be run when a TestGroup starts running.
     * @param testGroupStartedAction The Action that will be run when a TestGroup starts running.
     */
    public void setOnTestGroupStarted(Action1<TestGroup> testGroupStartedAction)
    {
        onTestGroupStarted = testGroupStartedAction;
    }

    /**
     * Set the Action that will be run when a TestGroup finishes.
     * @param testGroupFinishedAction The Action that will be run when a TestGroup finishes.
     */
    public void setOnTestGroupFinished(Action1<TestGroup> testGroupFinishedAction)
    {
        onTestGroupFinished = testGroupFinishedAction;
    }

    /**
     * Set the Action that will be run when a Test starts running.
     * @param testStartedAction The Action that will be run when a Test starts running.
     */
    public void setOnTestStarted(Action1<Test> testStartedAction)
    {
        onTestStarted = testStartedAction;
    }

    /**
     * Set the Action that will be run when a Test passes.
     * @param testPassedAction The Action that will be run when a Test passes.
     */
    public void setOnTestPassed(Action1<Test> testPassedAction)
    {
        onTestPassed = testPassedAction;
    }

    /**
     * Set the Action that will be run when a Test fails.
     * @param testFailedAction The Action that will be run when a Test fails.
     */
    public void setOnTestFailed(Action2<Test, TestAssertionFailure> testFailedAction)
    {
        onTestFailed = testFailedAction;
    }

    /**
     * Set the Action that will be run when a Test finishes.
     * @param testFinishedAction The Action that will be run when a Test finishes.
     */
    public void setOnTestFinished(Action1<Test> testFinishedAction)
    {
        onTestFinished = testFinishedAction;
    }

    /**
     * Get the number of tests that have been run.
     * @return The number of tests that have been run.
     */
    public int getFinishedTestCount()
    {
        return getPassedTestCount() + getFailedTestCount();
    }

    /**
     * Get the number of tests that have passed.
     * @return The number of tests that have passed.
     */
    public int getPassedTestCount()
    {
        return passedTestCount;
    }

    /**
     * Get the number of tests that have failed.
     * @return The number of tests that have failed.
     */
    public int getFailedTestCount()
    {
        return failedTestCount;
    }

    /**
     * Get the test failures.
     * @return The test failures.
     */
    public Iterable<TestAssertionFailure> getTestFailures()
    {
        return testFailures;
    }
}
