package qub;

public abstract class JSONSegment
{
    public abstract String toString();

    public abstract int getStartIndex();

    public abstract int getAfterEndIndex();

    public int getLength()
    {
        return getAfterEndIndex() - getStartIndex();
    }

    public Span getSpan()
    {
        return new Span(getStartIndex(), getLength());
    }

    protected static int getStartIndex(Iterable<JSONSegment> segments)
    {
        return segments.first().getStartIndex();
    }

    protected static int getAfterEndIndex(Iterable<JSONSegment> segments)
    {
        return segments.last().getAfterEndIndex();
    }

    protected static String getCombinedText(Iterable<JSONSegment> segments)
    {
        final StringBuilder builder = new StringBuilder();
        for (final JSONSegment segment : segments)
        {
            builder.append(segment.toString());
        }
        return builder.toString();
    }
}