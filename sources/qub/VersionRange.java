package qub;

/**
 * An expression that can match a range of version numbers.
 */
public abstract class VersionRange
{
    private final String text;

    VersionRange(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.text = text;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof VersionRange && equals((VersionRange)rhs);
    }

    public boolean equals(VersionRange rhs)
    {
        return rhs != null && text.equals(rhs.text);
    }

    @Override
    public String toString()
    {
        return text;
    }

    @Override
    public int hashCode()
    {
        return text.hashCode();
    }

    public boolean matches(String versionNumber)
    {
        return matches(VersionNumber.parse(versionNumber));
    }

    /**
     * Get whether or not the provided VersionNumber isMatch this VersionRange.
     * @param number The VersionNumber to check.
     * @return Whether or not the provided VersionNumber isMatch this VersionRange.
     */
    public abstract boolean matches(VersionNumber number);

    /**
     * Parse a VersionRange from the provided text.
     * @param text The text to parse.
     * @return The parsed VersionRange.
     */
    public static VersionRange parse(String text)
    {
        return parse(text, null);
    }

    /**
     * Parse a VersionRange from the provided text.
     * @param text The text to parse.
     * @param onIssue The action to run if an issue is encountered while parsing the text.
     * @return The parsed VersionRange.
     */
    public static VersionRange parse(String text, Action1<Issue> onIssue)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        VersionRange result = null;
        if (text.equals("*"))
        {
            result = new AnyVersionRange();
        }
        else if (text.startsWith("^"))
        {
            result = new MinimumMinorVersionRange(text, VersionNumber.parse(text.substring(1), onIssue));
        }
        else if (text.startsWith("~"))
        {
            result = new MinimumPatchVersionRange(text, VersionNumber.parse(text.substring(1), onIssue));
        }
        else
        {
            result = new ExactVersionRange(text);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(text, result.toString(), "result.toString()");

        return result;
    }
}