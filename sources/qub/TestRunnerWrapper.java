package qub;

public abstract class TestRunnerWrapper implements TestRunner
{
    private final TestRunner innerRunner;

    protected TestRunnerWrapper(TestRunner innerRunner)
    {
        PreCondition.assertNotNull(innerRunner, "innerRunner");

        this.innerRunner = innerRunner;
    }

    @Override
    public Result<Void> testClass(String fullClassName)
    {
        return this.innerRunner.testClass(fullClassName);
    }

    @Override
    public Result<Void> testClass(Class<?> testClass)
    {
        return this.innerRunner.testClass(testClass);
    }

    @Override
    public void testGroup(String testGroupName, Skip skip, Action0 testGroupAction)
    {
        this.innerRunner.testGroup(testGroupName, skip, testGroupAction);
    }

    @Override
    public <T1> void testGroup(String testGroupName, Skip skip, Function1<TestResources, Tuple1<T1>> resourcesFunction, Action1<T1> testGroupAction)
    {
        this.innerRunner.testGroup(testGroupName, skip, resourcesFunction, testGroupAction);
    }

    @Override
    public <T1, T2> void testGroup(String testGroupName, Skip skip, Function1<TestResources, Tuple2<T1, T2>> resourcesFunction, Action2<T1, T2> testGroupAction)
    {
        this.innerRunner.testGroup(testGroupName, skip, resourcesFunction, testGroupAction);
    }

    @Override
    public <T1, T2, T3> void testGroup(String testGroupName, Skip skip, Function1<TestResources, Tuple3<T1, T2, T3>> resourcesFunction, Action3<T1, T2, T3> testGroupAction)
    {
        this.innerRunner.testGroup(testGroupName, skip, resourcesFunction, testGroupAction);
    }

    @Override
    public void test(String testName, Skip skip, Action1<Test> testAction)
    {
        this.innerRunner.test(testName, skip, testAction);
    }

    @Override
    public <T1> void test(String testName, Skip skip, Function1<TestResources, Tuple1<T1>> resourcesFunction, Action2<Test, T1> testAction)
    {
        this.innerRunner.test(testName, skip, resourcesFunction, testAction);
    }

    @Override
    public <T1, T2> void test(String testName, Skip skip, Function1<TestResources, Tuple2<T1, T2>> resourcesFunction, Action3<Test, T1, T2> testAction)
    {
        this.innerRunner.test(testName, skip, resourcesFunction, testAction);
    }

    @Override
    public <T1, T2, T3> void test(String testName, Skip skip, Function1<TestResources, Tuple3<T1, T2, T3>> resourcesFunction, Action4<Test, T1, T2, T3> testAction)
    {
        this.innerRunner.test(testName, skip, resourcesFunction, testAction);
    }

    @Override
    public void speedTest(String testName, Duration maximumDuration, Action1<Test> testAction)
    {
        this.innerRunner.speedTest(testName, maximumDuration, testAction);
    }

    @Override
    public void beforeTestClass(Action1<TestClass> beforeTestClassAction)
    {
        this.innerRunner.beforeTestClass(beforeTestClassAction);
    }

    @Override
    public void afterTestClass(Action1<TestClass> afterTestClassAction)
    {
        this.innerRunner.afterTestClass(afterTestClassAction);
    }

    @Override
    public void beforeTestGroup(Action1<TestGroup> beforeTestGroupAction)
    {
        this.innerRunner.beforeTestGroup(beforeTestGroupAction);
    }

    @Override
    public void afterTestGroupFailure(Action2<TestGroup, TestError> afterTestGroupFailureAction)
    {
        this.innerRunner.afterTestGroupFailure(afterTestGroupFailureAction);
    }

    @Override
    public void afterTestGroupSkipped(Action1<TestGroup> afterTestGroupSkipped)
    {
        this.innerRunner.afterTestGroupSkipped(afterTestGroupSkipped);
    }

    @Override
    public void afterTestGroup(Action1<TestGroup> afterTestGroupAction)
    {
        this.innerRunner.afterTestGroup(afterTestGroupAction);
    }

    @Override
    public void beforeTest(Action1<Test> beforeTestAction)
    {
        this.innerRunner.beforeTest(beforeTestAction);
    }

    @Override
    public void afterTestFailure(Action2<Test, TestError> afterTestFailureAction)
    {
        this.innerRunner.afterTestFailure(afterTestFailureAction);
    }

    @Override
    public void afterTestSuccess(Action1<Test> afterTestSuccessAction)
    {
        this.innerRunner.afterTestSuccess(afterTestSuccessAction);
    }

    @Override
    public void afterTestSkipped(Action1<Test> afterTestSkippedAction)
    {
        this.innerRunner.afterTestSkipped(afterTestSkippedAction);
    }

    @Override
    public void afterTest(Action1<Test> afterTestAction)
    {
        this.innerRunner.afterTest(afterTestAction);
    }
}
