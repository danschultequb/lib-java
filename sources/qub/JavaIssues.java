package qub;

public class JavaIssues
{
    JavaIssues()
    {
    }

    public static Issue missingPackagePathSegment(Span span)
    {
        return Issue.error("Missing package path segment.", span);
    }

    public static Issue expectedWhitespaceBetweenPackageAndPackagePath(Span span)
    {
        return Issue.error("Expected whitespace between the 'package' keyword and the package path.", span);
    }

    public static Issue expectedPackagePathIdentifier(Span span)
    {
        return Issue.error("Expected a package path segment.", span);
    }

    public static Issue expectedPackagePathSeparatorOrSemicolon(Span span)
    {
        return Issue.error("Expected a package path separator ('.') or a semicolon (';').", span);
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
