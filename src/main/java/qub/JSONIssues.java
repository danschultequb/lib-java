package qub;

public class JSONIssues
{
    public static Issue missingEndQuote(String quote, int startIndex, int length)
    {
        return Issue.error("Missing end quote (" + quote + ").", startIndex, length);
    }

    public static Issue missingWholeNumberDigits(int startIndex, int length)
    {
        return Issue.error("Missing whole number digits.", startIndex, length);
    }

    public static Issue expectedWholeNumberDigits(int startIndex, int length)
    {
        return Issue.error("Expected whole number digits.", startIndex, length);
    }

    public static Issue missingFractionalNumberDigits(int startIndex, int length)
    {
        return Issue.error("Missing fractional number digits.", startIndex, length);
    }

    public static Issue expectedFractionalNumberDigits(int startIndex, int length)
    {
        return Issue.error("Expected fractional number digits.", startIndex, length);
    }

    public static Issue shouldBeLowercased(int startIndex, int length)
    {
        return Issue.error("Should be lowercased.", startIndex, length);
    }

    public static Issue missingExponentNumberDigits(int startIndex, int length)
    {
        return Issue.error("Missing exponent number digits.", startIndex, length);
    }

    public static Issue expectedExponentNumberDigits(int startIndex, int length)
    {
        return Issue.error("Expected exponent number digits.", startIndex, length);
    }

    public static Issue missingCommentSlashOrAsterisk(int startIndex, int length)
    {
        return Issue.error("Missing comment forward slash ('/') or asterisk ('*').", startIndex, length);
    }

    public static Issue expectedCommentSlashOrAsterisk(int startIndex, int length)
    {
        return Issue.error("Expected comment forward slash ('/') or asterisk ('*').", startIndex, length);
    }
}