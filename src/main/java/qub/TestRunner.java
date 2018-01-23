package qub;

/**
 * An object that can run tests and test groups.
 */
public interface TestRunner
{
    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(String testGroupName, Action0 testGroupAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    void test(String testName, Action1<Test> testAction);

    /**
     * Set an action that will be run before each test within this test group.
     * @param beforeTestAction The action that will be run before each test within this test group.
     */
    void beforeTest(Action0 beforeTestAction);

    /**
     * Set an action that will be run after each test within this test group.
     * @param afterTestAction The action that will be run after each test within this test group.
     */
    void afterTest(Action0 afterTestAction);

    /**
     * Surround the provided value's text with quotes and textualize any escaped characters.
     * @param value The value's text to quote and escape.
     * @return The quoted and escaped text.
     */
    String escapeAndQuote(String value);
}
