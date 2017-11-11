package qub;

public class Lex
{
    private final String text;
    private final int startIndex;
    private final LexType type;

    public Lex(String text, int startIndex, LexType type)
    {
        this.text = text;
        this.startIndex = startIndex;
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public int getLength()
    {
        return text == null ? 0 : text.length();
    }

    public int getStartIndex()
    {
        return startIndex;
    }

    public int getAfterEndIndex()
    {
        return Span.getAfterEndIndex(startIndex, getLength());
    }

    public int getEndIndex()
    {
        return Span.getEndIndex(startIndex, getLength());
    }

    public Span getSpan()
    {
        return new Span(startIndex, getLength());
    }

    public LexType getType()
    {
        return type;
    }

    public boolean isWhitespace()
    {
        boolean result;

        switch (type)
        {
            case Space:
            case Tab:
            case CarriageReturn:
                result = true;
                break;

            default:
                result = false;
        }

        return result;
    }

    public boolean isNewLine()
    {
        boolean result;

        switch (type)
        {
            case CarriageReturnNewLine:
            case NewLine:
                result = true;
                break;

            default:
                result = false;
                break;
        }

        return result;
    }
}