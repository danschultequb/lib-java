package qub;

public class TestGroup
{
    private final String name;
    private final TestGroup parentTestGroup;
    private final boolean shouldSkip;

    public TestGroup(String name, TestGroup parentTestGroup, boolean shouldSkip)
    {
        this.name = name;
        this.parentTestGroup = parentTestGroup;
        this.shouldSkip = shouldSkip || (parentTestGroup != null && parentTestGroup.getShouldSkip());
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

    public boolean getShouldSkip()
    {
        return shouldSkip;
    }
}
