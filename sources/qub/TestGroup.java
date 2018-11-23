package qub;

public class TestGroup
{
    private final String name;
    private final TestGroup parentTestGroup;
    private final Skip skip;

    public TestGroup(String name, TestGroup parentTestGroup, Skip skip)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

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

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(getFullName());
        if (shouldSkip())
        {
            builder.append(" (Skipped");
            final String skipMessage = getSkipMessage();
            if (!Strings.isNullOrEmpty(skipMessage))
            {
                builder.append(": ");
                builder.append(skipMessage);
            }
            builder.append(")");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof TestGroup && equals((TestGroup)rhs);
    }

    /**
     * Get whether or not this TestGroup equals the provided TestGroup.
     * @param rhs The TestGroup to compare against this TestGroup.
     * @return Whether or not this TestGroup equals the provided TestGroup.
     */
    public boolean equals(TestGroup rhs)
    {
        return rhs != null &&
                   Comparer.equal(name, rhs.name) &&
                   Comparer.equal(parentTestGroup, rhs.parentTestGroup) &&
                   Comparer.equal(skip, rhs.skip);
    }
}
