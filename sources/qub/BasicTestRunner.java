package qub;

public final class BasicTestRunner implements TestRunner
{
    private static final Skip noMessageSkip = new Skip(null);

    private final DesktopProcess process;

    private int passedTestCount;
    private int failedTestCount;
    private int skippedTestCount;
    private final List<TestError> testFailures = List.create();
    private final List<Test> skippedTests = List.create();

    private Action1<TestClass> beforeTestClassAction;
    private Action1<TestClass> afterTestClassAction;
    private Action1<TestGroup> beforeTestGroupAction;
    private Action2<TestGroup,TestError> afterTestGroupFailureAction;
    private Action1<TestGroup> afterTestGroupSkippedAction;
    private Action1<TestGroup> afterTestGroupAction;
    private Action1<Test> beforeTestAction;
    private Action1<Test> afterTestSuccessAction;
    private Action2<Test,TestError> afterTestFailureAction;
    private Action1<Test> afterTestSkippedAction;
    private Action1<Test> afterTestAction;

    private TestClass currentTestClass;
    private TestGroup currentTestGroup;

    private final PathPattern testPattern;

    /**
     * Create a new BasicTestRunner object.
     * @param process The Process that is running the tests.
     * @param testPattern The pattern that tests must match in order to be run.
     */
    @Deprecated
    public BasicTestRunner(Process process, PathPattern testPattern)
    {
        this((DesktopProcess)process, testPattern);
    }

    /**
     * Create a new BasicTestRunner object.
     * @param process The Process that is running the tests.
     * @param testPattern The pattern that tests must match in order to be run.
     */
    private BasicTestRunner(DesktopProcess process, PathPattern testPattern)
    {
        PreCondition.assertNotNull(process, "process");

        this.process = process;
        this.testPattern = testPattern;
    }

    public static BasicTestRunner create(DesktopProcess process, PathPattern testPattern)
    {
        return new BasicTestRunner(process, testPattern);
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
    public Result<Void> testClass(String fullClassName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullClassName, "fullClassName");

        return Result.create(() ->
        {
            final TypeLoader typeLoader = this.process.getTypeLoader();
            final Class<?> classWithTests = typeLoader.getType(fullClassName).await();
            this.testClass(classWithTests).await();
        });
    }

    @Override
    public Result<Void> testClass(Class<?> testClass)
    {
        PreCondition.assertNotNull(testClass, "testClass");

        return Result.create(() ->
        {
            currentTestClass = new TestClass(testClass);
            try
            {
                if (beforeTestClassAction != null)
                {
                    beforeTestClassAction.run(currentTestClass);
                }

                try
                {
                    final StaticMethod1<?,TestRunner,?> testMethod = Types.getStaticMethod1(testClass, "test", TestRunner.class).await();
                    testMethod.run(this);
                }
                finally
                {
                    if (afterTestClassAction != null)
                    {
                        afterTestClassAction.run(currentTestClass);
                    }
                }
            }
            finally
            {
                currentTestClass = null;
            }
        });
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
            final TestParent parent = currentTestGroup == null ? currentTestClass : currentTestGroup;
            currentTestGroup = TestGroup.create(testGroupName, parent, skip);

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
            final String testGroupFullName = currentTestGroup.getFullName();
            final TestError testError = new TestError(testGroupFullName, "An unexpected error occurred during " + Strings.escapeAndQuote(testGroupFullName) + ".", error);
            addFailedTest(testError);

            if (afterTestGroupFailureAction != null)
            {
                afterTestGroupFailureAction.run(currentTestGroup, testError);
            }
        }

        if (this.afterTestGroupAction != null)
        {
            this.afterTestGroupAction.run(this.currentTestGroup);
        }

        this.beforeTestAction = beforeTestActionBackup;
        this.afterTestAction = afterTestActionBackup;
        this.currentTestGroup = Types.as(this.currentTestGroup.getParent(), TestGroup.class);
    }

    @Override
    public void test(String testName, Action1<Test> testAction)
    {
        this.test(testName, null, testAction);
    }

    @Override
    public <T1> void test(String testName, Function1<TestResources, Tuple1<T1>> resourcesFunction, Action2<Test, T1> testAction)
    {
        this.test(testName, null, resourcesFunction, testAction);
    }

    @Override
    public <T1, T2> void test(String testName, Function1<TestResources, Tuple2<T1, T2>> resourcesFunction, Action3<Test, T1, T2> testAction)
    {
        this.test(testName, null, resourcesFunction, testAction);
    }

    @Override
    public <T1, T2, T3> void test(String testName, Function1<TestResources, Tuple3<T1, T2, T3>> resourcesFunction, Action4<Test, T1, T2, T3> testAction)
    {
        this.test(testName, null, resourcesFunction, testAction);
    }

    @Override
    public void test(String testName, Skip skip, Action1<Test> testAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testName, "testName");
        PreCondition.assertNotNull(testAction, "testAction");

        final Test test = Test.create(testName, this.currentTestGroup, skip);
        if (test.matches(this.testPattern))
        {
            final AsyncScheduler currentThreadAsyncScheduler = CurrentThread.getAsyncRunner().await();
            try
            {
                try
                {
                    if (this.beforeTestAction != null)
                    {
                        this.beforeTestAction.run(test);
                    }

                    if (test.shouldSkip())
                    {
                        addSkippedTest(test);
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

                            incrementPassedTestCount();
                            if (afterTestSuccessAction != null)
                            {
                                afterTestSuccessAction.run(test);
                            }
                        }
                        catch (TestError failure)
                        {
                            addFailedTest(failure);
                            if (afterTestFailureAction != null)
                            {
                                afterTestFailureAction.run(test, failure);
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    final String testFullName = test.getFullName();
                    final TestError testError = new TestError(testFullName, "An unexpected error occurred during " + Strings.escapeAndQuote(testFullName) + ".", error);
                    addFailedTest(testError);

                    if (afterTestFailureAction != null)
                    {
                        afterTestFailureAction.run(test, testError);
                    }
                }

                if (afterTestAction != null)
                {
                    afterTestAction.run(test);
                }
            }
            finally
            {
                CurrentThread.setAsyncRunner(currentThreadAsyncScheduler);
            }
        }
    }

    @Override
    public <T1> void test(String testName, Skip skip, Function1<TestResources, Tuple1<T1>> resourcesFunction, Action2<Test, T1> testAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testName, "testName");
        PreCondition.assertNotNull(resourcesFunction, "resourcesFunction");
        PreCondition.assertNotNull(testAction, "testAction");

        this.test(testName, skip, (Test test) ->
        {
            try (final TestResources resources = TestResources.create(this.process))
            {
                final Tuple1<T1> requestedResources = resourcesFunction.run(resources);
                testAction.run(test, requestedResources.getValue1());
            }
        });
    }

    @Override
    public <T1, T2> void test(String testName, Skip skip, Function1<TestResources, Tuple2<T1, T2>> resourcesFunction, Action3<Test, T1, T2> testAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testName, "testName");
        PreCondition.assertNotNull(resourcesFunction, "resourcesFunction");
        PreCondition.assertNotNull(testAction, "testAction");

        this.test(testName, skip, (Test test) ->
        {
            try (final TestResources resources = TestResources.create(this.process))
            {
                final Tuple2<T1, T2> requestedResources = resourcesFunction.run(resources);
                testAction.run(test, requestedResources.getValue1(), requestedResources.getValue2());
            }
        });
    }

    @Override
    public <T1, T2, T3> void test(String testName, Skip skip, Function1<TestResources, Tuple3<T1, T2, T3>> resourcesFunction, Action4<Test, T1, T2, T3> testAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testName, "testName");
        PreCondition.assertNotNull(resourcesFunction, "resourcesFunction");
        PreCondition.assertNotNull(testAction, "testAction");

        this.test(testName, skip, (Test test) ->
        {
            try (final TestResources resources = TestResources.create(this.process))
            {
                final Tuple3<T1, T2, T3> requestedResources = resourcesFunction.run(resources);
                testAction.run(test, requestedResources.getValue1(), requestedResources.getValue2(), requestedResources.getValue3());
            }
        });
    }

    @Override
    public void speedTest(String testName, Duration maximumDuration, Action1<Test> testAction)
    {
        test(testName, (Test test) ->
        {
            final Clock clock = process.getClock();
            final List<Duration> failedDurations = List.create();
            final int maximumAttempts = 3;
            for (int i = 0; i < maximumAttempts; ++i)
            {
                final DateTime startTime = clock.getCurrentDateTime();

                testAction.run(test);

                final DateTime endTime = clock.getCurrentDateTime();
                final Duration duration = endTime.minus(startTime);
                if (duration.lessThanOrEqualTo(maximumDuration))
                {
                    break;
                }
                else
                {
                    failedDurations.add(duration);
                }
            }
            if (failedDurations.getCount() == maximumAttempts)
            {
                test.fail("Expected test to complete in less than " + maximumDuration + ", but ran in " + failedDurations.toString() + ".");
            }
        });
    }

    @Override
    public void beforeTestClass(Action1<TestClass> beforeTestClassAction)
    {
        PreCondition.assertNotNull(beforeTestClassAction, "beforeTestClassAction");

        this.beforeTestClassAction = Action1.sequence(this.beforeTestClassAction, beforeTestClassAction);
    }

    @Override
    public void afterTestClass(Action1<TestClass> afterTestClassAction)
    {
        PreCondition.assertNotNull(afterTestClassAction, "afterTestClassAction");

        this.afterTestClassAction = Action1.sequence(this.afterTestClassAction, afterTestClassAction);
    }

    @Override
    public void beforeTestGroup(Action1<TestGroup> beforeTestGroupAction)
    {
        PreCondition.assertNotNull(beforeTestGroupAction, "beforeTestGroupAction");

        this.beforeTestGroupAction = Action1.sequence(this.beforeTestGroupAction, beforeTestGroupAction);
    }

    @Override
    public void afterTestGroupFailure(Action2<TestGroup,TestError> afterTestGroupFailureAction)
    {
        PreCondition.assertNotNull(afterTestGroupFailureAction, "afterTestGroupFailureAction");

        this.afterTestGroupFailureAction = Action2.sequence(this.afterTestGroupFailureAction, afterTestGroupFailureAction);
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
    public void afterTestFailure(Action2<Test,TestError> afterTestFailureAction)
    {
        PreCondition.assertNotNull(afterTestFailureAction, "afterTestFailureAction");

        this.afterTestFailureAction = Action2.sequence(this.afterTestFailureAction, afterTestFailureAction);
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

    /**
     * Get the number of tests that have been run.
     * @return The number of tests that have been run.
     */
    public int getFinishedTestCount()
    {
        return getPassedTestCount() + getFailedTestCount() + getSkippedTestCount();
    }

    private void incrementPassedTestCount()
    {
        ++passedTestCount;
        if (currentTestClass != null)
        {
            currentTestClass.incrementPassedTestCount();
        }
    }

    /**
     * Get the number of tests that have passed.
     * @return The number of tests that have passed.
     */
    public int getPassedTestCount()
    {
        return passedTestCount;
    }

    private void addFailedTest(TestError error)
    {
        PreCondition.assertNotNull(error, "error");

        testFailures.add(error);
        ++failedTestCount;
        if (currentTestClass != null)
        {
            currentTestClass.incrementFailedTestCount();
        }
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
    public Iterable<TestError> getTestFailures()
    {
        return testFailures;
    }

    private void addSkippedTest(Test test)
    {
        PreCondition.assertNotNull(test, "test");

        skippedTests.add(test);
        ++skippedTestCount;
        if (currentTestClass != null)
        {
            currentTestClass.incrementSkippedTestCount();
        }
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
