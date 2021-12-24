package qub;

public class TestClass implements TestParent
{
    private final String fullTypeName;
    private final Class<?> testClass;
    private int skippedTestCount;
    private int passedTestCount;
    private int failedTestCount;

    private TestClass(String fullTypeName, Class<?> testClass)
    {
        this.fullTypeName = fullTypeName;
        this.testClass = testClass;
    }

    public static TestClass create(String fullTypeName, Class<?> testClass)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNull(testClass, "testClass");

        return new TestClass(fullTypeName, testClass);
    }

    public static TestClass create(Class<?> testClass)
    {
        PreCondition.assertNotNull(testClass, "testClass");

        return TestClass.create(Types.getFullTypeName(testClass), testClass);
    }

    @Override
    public String getName()
    {
        return Types.getTypeNameFromFullTypeName(this.fullTypeName);
    }

    @Override
    public String getFullName()
    {
        return this.fullTypeName;
    }

    @Override
    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(this.getName()) ||
            testPattern.isMatch(this.getFullName());
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
        ++this.skippedTestCount;
    }

    public int getSkippedTestCount()
    {
        return this.skippedTestCount;
    }

    public void incrementFailedTestCount()
    {
        ++this.failedTestCount;
    }

    public int getFailedTestCount()
    {
        return this.failedTestCount;
    }

    public void incrementPassedTestCount()
    {
        ++this.passedTestCount;
    }

    public int getPassedTestCount()
    {
        return this.passedTestCount;
    }

    public int getTestCount()
    {
        return this.skippedTestCount + this.failedTestCount + this.passedTestCount;
    }
}
