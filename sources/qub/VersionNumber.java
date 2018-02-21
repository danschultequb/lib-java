package qub;

public class VersionNumber
{
    private final Integer major;
    private final Integer minor;
    private final Integer patch;

    VersionNumber(Integer major, Integer minor, Integer patch)
    {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
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

    public String toString()
    {
        String result = major.toString();
        if (minor != null)
        {
            result += '.' + minor.toString();
            if (patch != null)
            {
                result += '.' + patch.toString();
            }
        }
        return result;
    }

    public static VersionNumber parse(String versionNumberString)
    {
        final Lexer lexer = new Lexer(versionNumberString);
        Integer major = null;
        Integer minor = null;
        Integer patch = null;

        if (lexer.next())
        {
            final Lex majorLex = lexer.getCurrent();
            if (majorLex.getType() == LexType.Digits)
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
                            }
                        }
                    }
                }
            }
        }

        return major == null ? null : new VersionNumber(major, minor, patch);
    }
}
