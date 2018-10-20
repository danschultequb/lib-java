package qub;

public final class BasicTestRunner implements TestRunner
{
    private static final Skip noMessageSkip = new Skip(null);

    private final Process process;
    private Boolean hasNetworkConnection;

    private int passedTestCount;
    private int failedTestCount;
    private int skippedTestCount;
    private final List<TestAssertionFailure> testFailures = new SingleLinkList<>();
    private final List<Test> skippedTests = new SingleLinkList<>();

    private Action1<TestGroup> onTestGroupStarted;
    private Action1<TestGroup> onTestGroupFinished;
    private Action1<Test> onTestStarted;
    private Action1<Test> onTestPassed;
    private Action2<Test,TestAssertionFailure> onTestFailed;
    private Action1<Test> onTestSkipped;
    private Action1<Test> onTestFinished;
    
    private Action0 beforeTestAction;
    private Action0 afterTestAction;
    private TestGroup currentTestGroup;

    private final boolean debug;
    private final PathPattern testPattern;

    public BasicTestRunner(Process process, boolean debug, PathPattern testPattern)
    {
        this.process = process;
        this.debug = debug;
        this.testPattern = testPattern;
    }

    @Override
    public Skip skip()
    {
        return noMessageSkip;
    }

    @Override
    public Skip skip(boolean toSkip)
    {
        return toSkip ? skip() : null;
    }

    @Override
    public Skip skip(boolean toSkip, String message)
    {
        return toSkip ? skip(message) : null;
    }

    @Override
    public Skip skip(String message)
    {
        return new Skip(message);
    }

    @Override
    public void testGroup(Class<?> testClass, Action0 testGroupAction)
    {
        testGroup(testClass.getSimpleName(), null, testGroupAction);
    }

    @Override
    public void testGroup(String testGroupName, Action0 testGroupAction)
    {
        testGroup(testGroupName, null, testGroupAction);
    }

    @Override
    public void testGroup(Class<?> testClass, Skip skip, Action0 testGroupAction)
    {
        testGroup(testClass.getSimpleName(), skip, testGroupAction);
    }

    @Override
    public void testGroup(String testGroupName, Skip skip, Action0 testGroupAction)
    {
        if (testGroupAction != null)
        {
            final Action0 beforeTestActionBackup = beforeTestAction;
            final Action0 afterTestActionBackup = afterTestAction;

            currentTestGroup = new TestGroup(testGroupName, currentTestGroup, skip);
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
        test(testName, null, testAction);
    }

    @Override
    public void test(String testName, Skip skip, Action1<Test> testAction)
    {
        if (testAction != null)
        {
            final Test test = new Test(testName, currentTestGroup, skip, process);
            if (test.matches(testPattern))
            {
                if (onTestStarted != null)
                {
                    onTestStarted.run(test);
                }

                try
                {
                    if (test.shouldSkip())
                    {
                        skippedTests.add(test);
                        ++skippedTestCount;

                        if (onTestSkipped != null)
                        {
                            onTestSkipped.run(test);
                        }
                    }
                    else
                    {
                        if (beforeTestAction != null)
                        {
                            beforeTestAction.run();
                        }

                        testAction.run(test);
                        test.assertEqual(0, process.getMainAsyncRunner().getScheduledTaskCount(), "The main AsyncRunner should not have any scheduled tasks when a synchronous test completes.");
                        test.assertEqual(0, process.getParallelAsyncRunner().getScheduledTaskCount(), "The parallel AsyncRunner should not have any scheduled tasks when a synchronous test completes.");

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
                        if (!Strings.isNullOrEmpty(message))
                        {
                            messageLines.add("Message: " + message);
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
                finally
                {
                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(process.getMainAsyncRunner());
                }

                if (onTestFinished != null)
                {
                    onTestFinished.run(test);
                }
            }
        }
    }

    @Override
    public void test(String testName, boolean shouldRun, Action1<Test> testAction)
    {
        final Skip skip = shouldRun ? null : skip();
        test(testName, skip, testAction);
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
    public boolean hasNetworkConnection()
    {
        if (hasNetworkConnection == null)
        {
            hasNetworkConnection = process.getNetwork().isConnected().getValue();
        }
        return hasNetworkConnection;
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
     * Set the Action that will be run when a Test is skipped.
     * @param testSkippedAction The Action that will be run when a Test is skipped.
     */
    public void setOnTestSkipped(Action1<Test> testSkippedAction)
    {
        onTestSkipped = testSkippedAction;
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
        return getPassedTestCount() + getFailedTestCount() + getSkippedTestCount();
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

    /**
     * Get the number of tests that were skipped.
     * @return The number of tests that were skipped.
     */
    public int getSkippedTestCount()
    {
        return skippedTestCount;
    }

    public Iterable<Test> getSkippedTests()
    {
        return skippedTests;
    }
}
