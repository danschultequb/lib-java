package qub;

public class JavaIssues
{
    JavaIssues()
    {
    }

    public static Issue missingPackagePath(Span span)
    {
        return Issue.error("Missing package path.", span);
    }

    public static Issue expectedWhitespaceBetweenPackageAndPackagePath(Span span)
    {
        return Issue.error("Expected whitespace between the 'package' keyword and the package path.", span);
    }

    public static Issue expectedPackagePathLetters(Span span)
    {
        return Issue.error("Expected the package's path to consist only of letters and periods.", span);
    }

    public static Issue missingStatementSemicolon(Span span)
    {
        return Issue.error("Missing statement terminating semi-colon (';').", span);
    }

    public static Issue expectedStatementSemicolon(Span span)
    {
        return Issue.error("Expected statement terminating semi-colon (';').", span);
    }

    public static Issue expectedPackageOrTypeDefinition(Span span)
    {
        return Issue.error("Expected package or type definition.", span);
    }

    public static Issue expectedTypeDefinition(Span span)
    {
        return Issue.error("Expected type definition.", span);
    }
}
