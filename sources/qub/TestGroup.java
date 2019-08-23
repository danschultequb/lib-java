package qub;

public class TestGroup implements TestParent
{
    private final String name;
    private final TestParent parent;
    private final Skip skip;

    public TestGroup(String name, TestParent parent, Skip skip)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.name = name;
        this.parent = parent;
        this.skip = skip;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public TestParent getParent()
    {
        return parent;
    }

    @Override
    public String getFullName()
    {
        return parent == null ? name : parent.getFullName() + ' ' + name;
    }

    @Override
    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(getName()) ||
            testPattern.isMatch(getFullName()) ||
            (parent != null && parent.matches(testPattern));
    }

    @Override
    public boolean shouldSkip()
    {
        return skip != null || (parent != null && parent.shouldSkip());
    }

    @Override
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
           Comparer.equal(parent, rhs.parent) &&
           Comparer.equal(skip, rhs.skip);
    }
}
