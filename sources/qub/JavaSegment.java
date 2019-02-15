package qub;

public class JavaSegment
{
    private final JavaSegmentType type;
    private final Iterable<Lex> lexes;

    public JavaSegment(JavaSegmentType type, Lex... lexes)
    {
        this(type, Iterable.create(lexes));
    }

    public JavaSegment(JavaSegmentType type, Iterable<Lex> lexes)
    {
        this.type = type;
        this.lexes = lexes;
    }

    public JavaSegmentType getType()
    {
        return type;
    }

    public Span getSpan()
    {
        final int startIndex = lexes.first().getStartIndex();
        return new Span(startIndex, lexes.last().getAfterEndIndex() - startIndex);
    }

    public Iterable<Lex> getLexes()
    {
        return this.lexes;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for (final Lex lex : lexes)
        {
            builder.append(lex.toString());
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JavaSegment && equals((JavaSegment)rhs);
    }

    public boolean equals(JavaSegment rhs)
    {
        return rhs != null && lexes.equals(rhs.lexes);
    }
}
