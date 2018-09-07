package qub;

public class VersionNumber extends ComparableBase<VersionNumber>
{
    private final String text;
    private final Integer major;
    private final Integer minor;
    private final Integer patch;
    private final String suffix;

    public VersionNumber(int major)
    {
        this(major, null);
    }

    public VersionNumber(int major, String suffix)
    {
        this(Strings.concatenate("" + major, suffix), major, null, null, suffix);
    }

    public VersionNumber(int major, int minor)
    {
        this(major, minor, null);
    }

    public VersionNumber(int major, int minor, String suffix)
    {
        this(Strings.concatenate(major + "." + minor, suffix), major, minor, null, suffix);
    }

    public VersionNumber(int major, int minor, int patch)
    {
        this(major, minor, patch, null);
    }

    public VersionNumber(int major, int minor, int patch, String suffix)
    {
        this(Strings.concatenate(major + "." + minor + "." + patch, suffix), major, minor, patch, suffix);
    }

    VersionNumber(String text, Integer major, Integer minor, Integer patch, String suffix)
    {
        this.text = text;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.suffix = suffix;
    }

    public Integer getMajor()
    {
        return major;
    }

    public Integer getMinor()
    {
        return minor;
    }

    public Integer getPatch()
    {
        return patch;
    }

    public String getSuffix()
    {
        return suffix;
    }

    @Override
    public String toString()
    {
        final String result = text;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof VersionNumber && equals((VersionNumber)rhs);
    }

    public boolean equals(VersionNumber rhs)
    {
        return rhs != null && Strings.equal(text, rhs.text);
    }

    @Override
    public int hashCode()
    {
        return text.hashCode();
    }

    public static VersionNumber parse(String versionNumberString)
    {
        return parse(versionNumberString, null);
    }

    public static VersionNumber parse(String versionNumberString, Action1<Issue> onIssue)
    {
        PreCondition.assertNotNull(versionNumberString, "versionNumberString");

        final Lexer lexer = new Lexer(versionNumberString);
        Integer major = null;
        Integer minor = null;
        Integer patch = null;
        String suffix = null;

        if (!lexer.next())
        {
            Issue.onIssue(onIssue, Issue.error("Missing major version digits", new Span(0, 0)));
        }
        else
        {
            final Lex majorLex = lexer.getCurrent();
            if (majorLex.getType() != LexType.Digits)
            {
                Issue.onIssue(onIssue, Issue.error("Expected major version digits.", majorLex.getSpan()));
            }
            else
            {
                major = Integer.parseInt(majorLex.toString());
                if (lexer.next() && lexer.getCurrent().getType() == LexType.Period && lexer.next())
                {
                    final Lex minorLex = lexer.getCurrent();
                    if (minorLex.getType() == LexType.Digits)
                    {
                        minor = Integer.parseInt(minorLex.toString());
                        if (lexer.next() && lexer.getCurrent().getType() == LexType.Period && lexer.next())
                        {
                            final Lex patchLex = lexer.getCurrent();
                            if (patchLex.getType() == LexType.Digits)
                            {
                                patch = Integer.parseInt(patchLex.toString());
                                lexer.next();
                            }
                        }
                    }
                }
            }

            final StringBuilder suffixBuilder = new StringBuilder();
            while (lexer.hasCurrent())
            {
                suffixBuilder.append(lexer.takeCurrent().toString());
            }
            if (suffixBuilder.length() > 0)
            {
                suffix = suffixBuilder.toString();
            }
        }

        final VersionNumber result = new VersionNumber(versionNumberString, major, minor, patch, suffix);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Comparison compareTo(VersionNumber rhs)
    {
        Comparison result;
        if (rhs == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Integers.compare(this.getMajor(), rhs.getMajor());
            if (result == Comparison.Equal)
            {
                result = Integers.compare(this.getMinor(), rhs.getMinor());
                if (result == Comparison.Equal)
                {
                    result = Integers.compare(this.getPatch(), rhs.getPatch());
                    if (result == Comparison.Equal)
                    {
                        result = Strings.compare(this.getSuffix(), rhs.getSuffix());
                    }
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
