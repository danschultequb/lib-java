package qub;

public class TestClass implements TestParent
{
    private final Class<?> testClass;
    private int skippedTestCount;
    private int passedTestCount;
    private int failedTestCount;

    public TestClass(Class<?> testClass)
    {
        PreCondition.assertNotNull(testClass, "testClass");

        this.testClass = testClass;
    }

    @Override
    public String getName()
    {
        return Types.getTypeName(testClass);
    }

    @Override
    public String getFullName()
    {
        return Types.getFullTypeName(testClass);
    }

    @Override
    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(getName()) ||
            testPattern.isMatch(getFullName());
    }

    @Override
    public boolean shouldSkip()
    {
        return false;
    }

    @Override
    public String getSkipMessage()
    {
        return null;
    }

    @Override
    public TestParent getParent()
    {
        return null;
    }

    public void incrementSkippedTestCount()
    {
        ++skippedTestCount;
    }

    public int getSkippedTestCount()
    {
        return skippedTestCount;
    }

    public void incrementFailedTestCount()
    {
        ++failedTestCount;
    }

    public int getFailedTestCount()
    {
        return failedTestCount;
    }

    public void incrementPassedTestCount()
    {
        ++passedTestCount;
    }

    public int getPassedTestCount()
    {
        return passedTestCount;
    }

    public int getTestCount()
    {
        return skippedTestCount + failedTestCount + passedTestCount;
    }
}
