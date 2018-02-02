package qub;

public class Span
{
    private final int startIndex;
    private final int length;

    public Span(int startIndex, int length)
    {
        this.startIndex = startIndex;
        this.length = length;
    }

    public int getStartIndex()
    {
        return startIndex;
    }

    public int getLength()
    {
        return length;
    }

    public int getAfterEndIndex()
    {
        return getAfterEndIndex(startIndex, length);
    }

    public int getEndIndex()
    {
        return getEndIndex(startIndex, length);
    }

    @Override
    public String toString()
    {
        return toString(startIndex, length);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Span && equals((Span)rhs);
    }

    public boolean equals(Span rhs)
    {
        return rhs != null &&
                startIndex == rhs.startIndex &&
                length == rhs.length;
    }

    public static int getAfterEndIndex(int startIndex, int length)
    {
        return startIndex + length;
    }

    public static int getEndIndex(int startIndex, int length)
    {
        final int afterEndIndex = getAfterEndIndex(startIndex, length);
        return length <= 0 ? afterEndIndex : afterEndIndex - 1;
    }

    public static String toString(int startIndex, int length)
    {
        return "[" + startIndex + ", " + (startIndex + length) + ")";
    }
}
