package qub;

public class TestRunnerBase implements TestRunner
{
    private int passedTestCount;
    private int failedTestCount;

    private Action1<String> onTestGroupStarted;
    private Action1<String> onTestGroupFinished;
    private Action1<String> onTestStarted;
    private Action1<String> onTestPassed;
    private Action2<String,Throwable> onTestFailed;
    private Action1<String> onTestFinished;

    @Override
    public void testGroup(String testGroupName, Action0 testGroupAction)
    {
        if (testGroupAction != null)
        {
            if (onTestGroupStarted != null)
            {
                onTestGroupStarted.run(testGroupName);
            }

            testGroupAction.run();

            if (onTestGroupFinished != null)
            {
                onTestGroupFinished.run(testGroupName);
            }
        }
    }

    @Override
    public void test(String testName, Action1<Test> testAction)
    {
        if (testAction != null)
        {
            final Test test = new Test();

            if (onTestStarted != null)
            {
                onTestStarted.run(testName);
            }

            try
            {
                testAction.run(test);

                ++passedTestCount;
                if (onTestPassed != null)
                {
                    onTestPassed.run(testName);
                }
            }
            catch (Throwable e)
            {
                ++failedTestCount;
                if (onTestFailed != null)
                {
                    onTestFailed.run(testName, e);
                }
            }

            if (onTestFinished != null)
            {
                onTestFinished.run(testName);
            }
        }
    }

    /**
     * Set the Action that will be run when a TestGroup starts running.
     * @param testGroupStartedAction The Action that will be run when a TestGroup starts running.
     */
    public void setOnTestGroupStarted(Action1<String> testGroupStartedAction)
    {
        onTestGroupStarted = testGroupStartedAction;
    }

    /**
     * Set the Action that will be run when a TestGroup finishes.
     * @param testGroupFinishedAction The Action that will be run when a TestGroup finishes.
     */
    public void setOnTestGroupFinished(Action1<String> testGroupFinishedAction)
    {
        onTestGroupFinished = testGroupFinishedAction;
    }

    /**
     * Set the Action that will be run when a Test starts running.
     * @param testStartedAction The Action that will be run when a Test starts running.
     */
    public void setOnTestStarted(Action1<String> testStartedAction)
    {
        onTestStarted = testStartedAction;
    }

    /**
     * Set the Action that will be run when a Test passes.
     * @param testPassedAction The Action that will be run when a Test passes.
     */
    public void setOnTestPassed(Action1<String> testPassedAction)
    {
        onTestPassed = testPassedAction;
    }

    /**
     * Set the Action that will be run when a Test fails.
     * @param testFailedAction The Action that will be run when a Test fails.
     */
    public void setOnTestFailed(Action2<String, Throwable> testFailedAction)
    {
        onTestFailed = testFailedAction;
    }

    /**
     * Set the Action that will be run when a Test finishes.
     * @param testFinishedAction The Action that will be run when a Test finishes.
     */
    public void setOnTestFinished(Action1<String> testFinishedAction)
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
}
