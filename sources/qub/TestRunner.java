package qub;

/**
 * An object that can run tests and test groups.
 */
public interface TestRunner
{
    /**
     * A method that returns a Skip. This method is really used as a flag to skip a TestGroup or
     * a Test.
     * @return The Skip marker.
     */
    Skip skip();

    /**
     * A method that returns a Skip if the provided boolean is true, and returns null if the
     * provided boolean is false. This method is really used as a flag to skip at TestGroup or a
     * Test.
     * @param toSkip Whether or not to skip.
     * @return A Skip if toSkip is true or null if toSkip is false.
     */
    Skip skip(boolean toSkip);

    /**
     * A method that returns a Skip if the provided boolean is true, and returns null if the
     * provided boolean is false. This method is really used as a flag to skip at TestGroup or a
     * Test.
     * @param toSkip Whether or not to skip.
     * @param message The message to display for why the test or test group is being skipped.
     * @return A Skip if toSkip is true or null if toSkip is false.
     */
    Skip skip(boolean toSkip, String message);

    /**
     * A method that returns a Skip. This method is really used as a flag to skip a TestGroup or
     * a Test.
     * @param message The message to display for why the test or test group is being skipped.
     * @return The Skip marker.
     */
    Skip skip(String message);

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
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(String testGroupName, Action0 testGroupAction);

    /**
     * Create a new test group with the name of the provided class and the provided action.
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(Class<?> testClass, Action0 testGroupAction);

    /**
     * Create a new test group with the provided name and action that will be skipped during
     * execution.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(String testGroupName, Skip skip, Action0 testGroupAction);

    /**
     * Create a new test group with the name of the provided class and the provided action that will
     * be skipped during execution..
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(Class<?> testClass, Skip skip, Action0 testGroupAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    void test(String testName, Action1<Test> testAction);

    /**
     * Skip the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    void test(String testName, Skip skip, Action1<Test> testAction);

    /**
     * Run the provided test and assert that it runs within the provided maximum duration.
     * @param testName The name of the test.
     * @param maximumDuration The maximum amount of time that the test can take. The test will fail
     *                        if it takes longer than this duration.
     * @param testAction The action for the test.
     */
    void speedTest(String testName, Duration maximumDuration, Action1<Test> testAction);

    /**
     * Run the provided test and assert that it runs within the provided maximum duration.
     * @param testName The name of the test.
     * @param maximumDuration The maximum amount of time that the test can take. The test will fail
     *                        if it takes longer than this duration.
     * @param testAction The action for the test.
     */
    void speedTest(String testName, Duration2 maximumDuration, Action1<Test> testAction);

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
