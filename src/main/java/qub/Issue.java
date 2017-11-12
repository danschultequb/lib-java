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
}
