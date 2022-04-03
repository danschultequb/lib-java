package qub;

public class TestRunnerParameters
{
    private PathPattern testPattern;

    protected TestRunnerParameters()
    {
    }

    public static TestRunnerParameters create()
    {
        return new TestRunnerParameters();
    }

    public TestRunnerParameters setTestPattern(PathPattern testPattern)
    {
        PreCondition.assertNotNull(testPattern, "testPattern");

        this.testPattern = testPattern;

        return this;
    }

    public PathPattern getTestPattern()
    {
        return this.testPattern;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof TestRunnerParameters && this.equals((TestRunnerParameters)rhs);
    }

    public boolean equals(TestRunnerParameters rhs)
    {
        return rhs != null &&
            Comparer.equal(this.getTestPattern(), rhs.getTestPattern());
    }
}
