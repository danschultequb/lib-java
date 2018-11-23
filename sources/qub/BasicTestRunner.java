package qub;

public final class BasicTestRunner implements TestRunner
{
    private static final Skip noMessageSkip = new Skip(null);

    private final Process process;
    private Boolean hasNetworkConnection;

    private int passedTestCount;
    private int failedTestCount;
    private int errorTestCount;
    private int skippedTestCount;
    private final List<TestAssertionFailure> testFailures = new SingleLinkList<>();
    private final List<TestError> testErrors = new SingleLinkList<>();
    private final List<Test> skippedTests = new SingleLinkList<>();

    private Action1<TestGroup> beforeTestGroupAction;
    private Action2<TestGroup,Throwable> afterTestGroupErrorAction;
    private Action1<TestGroup> afterTestGroupSkippedAction;
    private Action1<TestGroup> afterTestGroupAction;
    private Action1<Test> beforeTestAction;
    private Action1<Test> afterTestSuccessAction;
    private Action2<Test,TestAssertionFailure> afterTestFailureAction;
    private Action2<Test,Throwable> afterTestErrorAction;
    private Action1<Test> afterTestSkippedAction;
    private Action1<Test> afterTestAction;
    
    private TestGroup currentTestGroup;

    private final PathPattern testPattern;

    public BasicTestRunner(Process process, PathPattern testPattern)
    {
        PreCondition.assertNotNull(process, "process");

        this.process = process;
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
        PreCondition.assertNotNullAndNotEmpty(message, "message");

        return toSkip ? skip(message) : null;
    }

    @Override
    public Skip skip(String message)
    {
        PreCondition.assertNotNullAndNotEmpty(message, "message");

        return new Skip(message);
    }

    @Override
    public void testGroup(Class<?> testClass, Action0 testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        testGroup(Types.getTypeName(testClass), null, testGroupAction);
    }

    @Override
    public void testGroup(String testGroupName, Action0 testGroupAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testGroupName, "testGroupName");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        testGroup(testGroupName, null, testGroupAction);
    }

    @Override
    public void testGroup(Class<?> testClass, Skip skip, Action0 testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        testGroup(testClass.getSimpleName(), skip, testGroupAction);
    }

    @Override
    public void testGroup(String testGroupName, Skip skip, Action0 testGroupAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testGroupName, "testGroupName");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        final Action1<Test> beforeTestActionBackup = beforeTestAction;
        final Action1<Test> afterTestActionBackup = afterTestAction;
        try
        {
            currentTestGroup = new TestGroup(testGroupName, currentTestGroup, skip);

            if (beforeTestGroupAction != null)
            {
                beforeTestGroupAction.run(currentTestGroup);
            }

            testGroupAction.run();

            if (currentTestGroup.shouldSkip())
            {
                if (afterTestGroupSkippedAction != null)
                {
                    afterTestGroupSkippedAction.run(currentTestGroup);
                }
            }
        }
        catch (Throwable error)
        {
            ++errorTestCount;
            testErrors.add(new TestError(currentTestGroup.getFullName(), error));

            if (afterTestGroupErrorAction != null)
            {
                afterTestGroupErrorAction.run(currentTestGroup, error);
            }
        }

        if (afterTestGroupAction != null)
        {
            afterTestGroupAction.run(currentTestGroup);
        }

        beforeTestAction = beforeTestActionBackup;
        afterTestAction = afterTestActionBackup;
        currentTestGroup = currentTestGroup.getParentTestGroup();
    }

    @Override
    public void test(String testName, Action1<Test> testAction)
    {
        test(testName, null, testAction);
    }

    @Override
    public void test(String testName, Skip skip, Action1<Test> testAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testName, "testName");
        PreCondition.assertNotNull(testAction, "testAction");

        final Test test = new Test(testName, currentTestGroup, skip, process);
        if (test.matches(testPattern))
        {
            try
            {
                if (beforeTestAction != null)
                {
                    beforeTestAction.run(test);
                }

                if (test.shouldSkip())
                {
                    skippedTests.add(test);
                    ++skippedTestCount;

                    if (afterTestSkippedAction != null)
                    {
                        afterTestSkippedAction.run(test);
                    }
                }
                else
                {
                    try
                    {
                        testAction.run(test);
                        test.assertEqual(0, process.getMainAsyncRunner().getScheduledTaskCount(), "The main AsyncRunner should not have any scheduled tasks when a synchronous test completes.");
                        test.assertEqual(0, process.getParallelAsyncRunner().getScheduledTaskCount(), "The parallel AsyncRunner should not have any scheduled tasks when a synchronous test completes.");

                        ++passedTestCount;
                        if (afterTestSuccessAction != null)
                        {
                            afterTestSuccessAction.run(test);
                        }
                    }
                    catch (TestAssertionFailure failure)
                    {
                        testFailures.add(failure);

                        ++failedTestCount;
                        if (afterTestFailureAction != null)
                        {
                            afterTestFailureAction.run(test, failure);
                        }
                    }
                }
            }
            catch (Throwable error)
            {
                ++errorTestCount;
                testErrors.add(new TestError(test.getFullName(), error));

                if (afterTestErrorAction != null)
                {
                    afterTestErrorAction.run(test, error);
                }
            }

            if (afterTestAction != null)
            {
                afterTestAction.run(test);
            }

            AsyncRunnerRegistry.setCurrentThreadAsyncRunner(process.getMainAsyncRunner());
        }
    }

    @Override
    public void beforeTestGroup(Action1<TestGroup> beforeTestGroupAction)
    {
        PreCondition.assertNotNull(beforeTestGroupAction, "beforeTestGroupAction");

        this.beforeTestGroupAction = Action1.sequence(this.beforeTestGroupAction, beforeTestGroupAction);
    }

    @Override
    public void afterTestGroupError(Action2<TestGroup,Throwable> afterTestGroupErrorAction)
    {
        PreCondition.assertNotNull(afterTestGroupErrorAction, "afterTestGroupErrorAction");

        this.afterTestGroupErrorAction = Action2.sequence(this.afterTestGroupErrorAction, afterTestGroupErrorAction);
    }

    @Override
    public void afterTestGroupSkipped(Action1<TestGroup> afterTestGroupSkippedAction)
    {
        PreCondition.assertNotNull(afterTestGroupSkippedAction, "afterTestGroupSkippedAction");

        this.afterTestGroupSkippedAction = Action1.sequence(this.afterTestGroupSkippedAction, afterTestGroupSkippedAction);
    }

    @Override
    public void afterTestGroup(Action1<TestGroup> afterTestGroupAction)
    {
        PreCondition.assertNotNull(afterTestGroupAction, "afterTestGroupAction");

        this.afterTestGroupAction = Action1.sequence(this.afterTestGroupAction, afterTestGroupAction);
    }

    @Override
    public void beforeTest(Action1<Test> beforeTestAction)
    {
        PreCondition.assertNotNull(beforeTestAction, "beforeTestAction");

        this.beforeTestAction = Action1.sequence(this.beforeTestAction, beforeTestAction);
    }

    @Override
    public void afterTestFailure(Action2<Test,TestAssertionFailure> afterTestFailureAction)
    {
        PreCondition.assertNotNull(afterTestFailureAction, "afterTestFailureAction");

        this.afterTestFailureAction = Action2.sequence(this.afterTestFailureAction, afterTestFailureAction);
    }

    @Override
    public void afterTestError(Action2<Test,Throwable> afterTestErrorAction)
    {
        PreCondition.assertNotNull(afterTestErrorAction, "afterTestErrorAction");

        this.afterTestErrorAction = Action2.sequence(this.afterTestErrorAction, afterTestErrorAction);
    }

    @Override
    public void afterTestSuccess(Action1<Test> afterTestSuccessAction)
    {
        PreCondition.assertNotNull(afterTestSuccessAction, "afterTestSuccessAction");

        this.afterTestSuccessAction = Action1.sequence(this.afterTestSuccessAction, afterTestSuccessAction);
    }

    @Override
    public void afterTestSkipped(Action1<Test> afterTestSkippedAction)
    {
        PreCondition.assertNotNull(afterTestSkippedAction, "afterTestSkippedAction");

        this.afterTestSkippedAction = Action1.sequence(this.afterTestSkippedAction, afterTestSkippedAction);
    }

    @Override
    public void afterTest(Action1<Test> afterTestAction)
    {
        PreCondition.assertNotNull(afterTestAction, "afterTestAction");

        this.afterTestAction = Action1.sequence(this.afterTestAction, afterTestAction);
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

    public int getErrorTestCount()
    {
        return errorTestCount;
    }

    /**
     * Get the test errors.
     * @return The test errors.
     */
    public Iterable<TestError> getTestErrors()
    {
        return testErrors;
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
