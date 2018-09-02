package qub;

public class Issue
{
    private final String message;
    private final Span span;
    private final Type type;

    public Issue(String message, Span span, Type type)
    {
        this.message = message;
        this.span = span;
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public Span getSpan()
    {
        return span;
    }

    public Type getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Issue && equals((Issue)rhs);
    }

    public boolean equals(Issue rhs)
    {
        return rhs != null &&
                message.equals(rhs.message) &&
                span.equals(rhs.span) &&
                type == rhs.type;
    }

    @Override
    public String toString()
    {
        return type + " " + span + ": \"" + message + "\"";
    }

    public static Issue warning(String message, int startIndex, int length)
    {
        return warning(message, new Span(startIndex, length));
    }

    public static Issue warning(String message, Span span)
    {
        return new Issue(message, span, Type.Warning);
    }

    public static Issue error(String message, int startIndex, int length)
    {
        return error(message, new Span(startIndex, length));
    }

    public static Issue error(String message, Span span)
    {
        return new Issue(message, span, Type.Error);
    }

    public enum Type
    {
        Warning,
        Error
    }

    /**
     * If the provided Action is not null, then invoke it with the provided issue.
     * @param onIssue The Action to invoke if it is not null.
     * @param issue The issue.
     */
    public static void onIssue(Action1<Issue> onIssue, Issue issue)
    {
        if (onIssue != null)
        {
            onIssue.run(issue);
        }
    }
}
