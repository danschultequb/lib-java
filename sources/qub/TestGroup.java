package qub;

public class TestGroup
{
    private final String name;
    private final TestGroup parentTestGroup;
    private final Skip skip;

    public TestGroup(String name, TestGroup parentTestGroup, Skip skip)
    {
        this.name = name;
        this.parentTestGroup = parentTestGroup;
        this.skip = skip;
    }

    public String getName()
    {
        return name;
    }

    public TestGroup getParentTestGroup()
    {
        return parentTestGroup;
    }

    public String getFullName()
    {
        return parentTestGroup == null ? name : parentTestGroup.getFullName() + ' ' + name;
    }

    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(getName()) ||
            testPattern.isMatch(getFullName()) ||
            (parentTestGroup != null && parentTestGroup.matches(testPattern));
    }

    public boolean shouldSkip()
    {
        return skip != null || (parentTestGroup != null && parentTestGroup.shouldSkip());
    }

    public String getSkipMessage()
    {
        return skip == null ? null : skip.getMessage();
    }
}
