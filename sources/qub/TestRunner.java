package qub;

/**
 * An object that can run tests and test groups.
 */
public interface TestRunner
{
    static TestRunner create(DesktopProcess process, PathPattern testPattern)
    {
        return BasicTestRunner.create(process, testPattern);
    }

    /**
     * A method that returns a Skip. This method is really used as a flag to skip a TestGroup or
     * a Test.
     * @return The Skip marker.
     */
    default Skip skip()
    {
        return Skip.create();
    }

    /**
     * A method that returns a Skip. This method is really used as a flag to skip a TestGroup or
     * a Test.
     * @param message The message to display for why the test or test group is being skipped.
     * @return The Skip marker.
     */
    default Skip skip(String message)
    {
        PreCondition.assertNotNullAndNotEmpty(message, "message");

        return Skip.create(message);
    }

    /**
     * A method that returns a Skip if the provided boolean is true, and returns null if the
     * provided boolean is false. This method is really used as a flag to skip at TestGroup or a
     * Test.
     * @param toSkip Whether or not to skip.
     * @return A Skip if toSkip is true or null if toSkip is false.
     */
    default Skip skip(boolean toSkip)
    {
        return toSkip ? this.skip() : null;
    }

    /**
     * A method that returns a Skip if the provided boolean is true, and returns null if the
     * provided boolean is false. This method is really used as a flag to skip at TestGroup or a
     * Test.
     * @param toSkip Whether or not to skip.
     * @param message The message to display for why the test or test group is being skipped.
     * @return A Skip if toSkip is true or null if toSkip is false.
     */
    default Skip skip(boolean toSkip, String message)
    {
        PreCondition.assertNotNullAndNotEmpty(message, "message");

        return toSkip ? this.skip(message) : null;
    }

    /**
     * Attempt to run the tests that are found in the Class associated with the provided
     * fullClassName.
     * @param fullClassName The full name of the class to run the tests of.
     */
    Result<Void> testClass(String fullClassName);

    /**
     * Attempt to run the tests that are found in the provided testClass.
     * @param testClass The class to run the tests of.
     */
    Result<Void> testClass(Class<?> testClass);

    /**
     * Create a new test group with the name of the provided class and the provided action.
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default void testGroup(Class<?> testClass, Action0 testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        this.testGroup(Types.getTypeName(testClass), testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action.
     * @param testClass The class that this test group will be testing.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1> void testGroup(Class<?> testClass, Function1<TestResources,Tuple1<T1>> resourcesFunction, Action1<T1> testGroupAction)
    {
        this.testGroup(Types.getTypeName(testClass), resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action.
     * @param testClass The class that this test group will be testing.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1,T2> void testGroup(Class<?> testClass, Function1<TestResources,Tuple2<T1,T2>> resourcesFunction, Action2<T1,T2> testGroupAction)
    {
        this.testGroup(Types.getTypeName(testClass), resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action.
     * @param testClass The class that this test group will be testing.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1,T2,T3> void testGroup(Class<?> testClass, Function1<TestResources,Tuple3<T1,T2,T3>> resourcesFunction, Action3<T1,T2,T3> testGroupAction)
    {
        this.testGroup(Types.getTypeName(testClass), resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action that will
     * be skipped during execution..
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default void testGroup(Class<?> testClass, Skip skip, Action0 testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        this.testGroup(Types.getTypeName(testClass), skip, testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action that will
     * be skipped during execution..
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1> void testGroup(Class<?> testClass, Skip skip, Function1<TestResources,Tuple1<T1>> resourcesFunction, Action1<T1> testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        this.testGroup(Types.getTypeName(testClass), skip, resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action that will
     * be skipped during execution..
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1,T2> void testGroup(Class<?> testClass, Skip skip, Function1<TestResources,Tuple2<T1,T2>> resourcesFunction, Action2<T1,T2> testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        this.testGroup(Types.getTypeName(testClass), skip, resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the name of the provided class and the provided action that will
     * be skipped during execution..
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1,T2,T3> void testGroup(Class<?> testClass, Skip skip, Function1<TestResources,Tuple3<T1,T2,T3>> resourcesFunction, Action3<T1,T2,T3> testGroupAction)
    {
        PreCondition.assertNotNull(testClass, "testClass");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        this.testGroup(Types.getTypeName(testClass), skip, resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default void testGroup(String testGroupName, Action0 testGroupAction)
    {
        PreCondition.assertNotNullAndNotEmpty(testGroupName, "testGroupName");
        PreCondition.assertNotNull(testGroupAction, "testGroupAction");

        this.testGroup(testGroupName, null, testGroupAction);
    }

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1> void testGroup(String testGroupName, Function1<TestResources,Tuple1<T1>> resourcesFunction, Action1<T1> testGroupAction)
    {
        this.testGroup(testGroupName, null, resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1,T2> void testGroup(String testGroupName, Function1<TestResources,Tuple2<T1,T2>> resourcesFunction, Action2<T1,T2> testGroupAction)
    {
        this.testGroup(testGroupName, null, resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    default <T1,T2,T3> void testGroup(String testGroupName, Function1<TestResources,Tuple3<T1,T2,T3>> resourcesFunction, Action3<T1,T2,T3> testGroupAction)
    {
        this.testGroup(testGroupName, null, resourcesFunction, testGroupAction);
    }

    /**
     * Create a new test group with the provided name and action that will be skipped during
     * execution.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(String testGroupName, Skip skip, Action0 testGroupAction);

    /**
     * Create a new test group with the provided name and action that will be skipped during
     * execution.
     * @param testGroupName The name of the test group.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    <T1> void testGroup(String testGroupName, Skip skip, Function1<TestResources,Tuple1<T1>> resourcesFunction, Action1<T1> testGroupAction);

    /**
     * Create a new test group with the provided name and action that will be skipped during
     * execution.
     * @param testGroupName The name of the test group.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    <T1,T2> void testGroup(String testGroupName, Skip skip, Function1<TestResources,Tuple2<T1,T2>> resourcesFunction, Action2<T1,T2> testGroupAction);

    /**
     * Create a new test group with the provided name and action that will be skipped during
     * execution.
     * @param testGroupName The name of the test group.
     * @param resourcesFunction The function that decides which resources this test group will require.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    <T1,T2,T3> void testGroup(String testGroupName, Skip skip, Function1<TestResources,Tuple3<T1,T2,T3>> resourcesFunction, Action3<T1,T2,T3> testGroupAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    default void test(String testName, Action1<Test> testAction)
    {
        this.test(testName, null, testAction);
    }

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param resourcesFunction The function that decides which resources this test will require.
     * @param testAction The action for the test.
     */
    default <T1> void test(String testName, Function1<TestResources,Tuple1<T1>> resourcesFunction, Action2<Test,T1> testAction)
    {
        this.test(testName, null, resourcesFunction, testAction);
    }

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param resourcesFunction The function that decides which resources this test will require.
     * @param testAction The action for the test.
     */
    default <T1,T2> void test(String testName, Function1<TestResources,Tuple2<T1,T2>> resourcesFunction, Action3<Test,T1,T2> testAction)
    {
        this.test(testName, null, resourcesFunction, testAction);
    }

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param resourcesFunction The function that decides which resources this test will require.
     * @param testAction The action for the test.
     */
    default <T1,T2,T3> void test(String testName, Function1<TestResources,Tuple3<T1,T2,T3>> resourcesFunction, Action4<Test,T1,T2,T3> testAction)
    {
        this.test(testName, null, resourcesFunction, testAction);
    }

    /**
     * Skip the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    void test(String testName, Skip skip, Action1<Test> testAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param resourcesFunction The function that decides which resources this test will require.
     * @param testAction The action for the test.
     */
    <T1> void test(String testName, Skip skip, Function1<TestResources,Tuple1<T1>> resourcesFunction, Action2<Test,T1> testAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param resourcesFunction The function that decides which resources this test will require.
     * @param testAction The action for the test.
     */
    <T1,T2> void test(String testName, Skip skip, Function1<TestResources,Tuple2<T1,T2>> resourcesFunction, Action3<Test,T1,T2> testAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param resourcesFunction The function that decides which resources this test will require.
     * @param testAction The action for the test.
     */
    <T1,T2,T3> void test(String testName, Skip skip, Function1<TestResources,Tuple3<T1,T2,T3>> resourcesFunction, Action4<Test,T1,T2,T3> testAction);

    /**
     * Run the provided test and assert that it runs within the provided maximum duration.
     * @param testName The name of the test.
     * @param maximumDuration The maximum amount of time that the test can take. The test will fail
     *                        if it takes longer than this duration.
     * @param testAction The action for the test.
     */
    void speedTest(String testName, Duration maximumDuration, Action1<Test> testAction);

    /**
     * Set an action that will be run before each test class.
     * @param beforeTestClassAction The action that will be run before each test class.
     */
    void beforeTestClass(Action1<TestClass> beforeTestClassAction);

    /**
     * Set an action that will be run after each test class.
     * @param afterTestClassAction The action that will be run after each test class.
     */
    void afterTestClass(Action1<TestClass> afterTestClassAction);

    /**
     * Set an action that will be run before each test group.
     * @param beforeTestGroupAction The action that will be run before each test group.
     */
    void beforeTestGroup(Action1<TestGroup> beforeTestGroupAction);

    /**
     * Set an action that will be run after a test group has an unexpected error.
     * @param afterTestGroupFailureAction The action that will be run after a test group has an unexpected error.
     */
    void afterTestGroupFailure(Action2<TestGroup,TestError> afterTestGroupFailureAction);

    /**
     * Set an action that will be run after a test group is skipped.
     * @param afterTestGroupSkipped The action that will be run after a test group is skipped.
     */
    void afterTestGroupSkipped(Action1<TestGroup> afterTestGroupSkipped);

    /**
     * Set an action that will be run after each test group.
     * @param afterTestGroupAction The action that will be run after each test group.
     */
    void afterTestGroup(Action1<TestGroup> afterTestGroupAction);

    /**
     * Set an action that will be run before each test within this test group.
     * @param beforeTestAction The action that will be run before each test within this test group.
     */
    void beforeTest(Action1<Test> beforeTestAction);

    /**
     * Set an action that will be run after a test assertion fails.
     * @param afterTestFailureAction The action that will be run after a test assertion fails.
     */
    void afterTestFailure(Action2<Test,TestError> afterTestFailureAction);

    /**
     * Set an action that will be run after a test completes successfully.
     * @param afterTestSuccessAction The action that will be run after a test completes
     *                               successfully.
     */
    void afterTestSuccess(Action1<Test> afterTestSuccessAction);

    /**
     * Set an action that will be run after a test is skipped.
     * @param afterTestSkippedAction The action that will be run after a test is skipped.
     */
    void afterTestSkipped(Action1<Test> afterTestSkippedAction);

    /**
     * Set an action that will be run after each test within this test group.
     * @param afterTestAction The action that will be run after each test within this test group.
     */
    void afterTest(Action1<Test> afterTestAction);
}
