package qub;

public class JSONObjectSegment extends JSONSegment
{
    private final Iterable<JSONSegment> segments;

    public JSONObjectSegment(Iterable<JSONSegment> segments)
    {
        this.segments = segments;
    }

    public JSONToken getLeftCurlyBracket()
    {
        return (JSONToken)segments.first();
    }

    public JSONToken getRightCurlyBracket()
    {
        JSONToken result = null;

        final JSONSegment lastSegment = segments.last();
        if (lastSegment instanceof JSONToken)
        {
            final JSONToken lastToken = (JSONToken)lastSegment;
            if (lastToken.getType() == JSONTokenType.RightCurlyBracket)
            {
                result = lastToken;
            }
        }

        return result;
    }

    public Iterable<JSONPropertySegment> getPropertySegments()
    {
        return segments.instanceOf(JSONPropertySegment.class);
    }

    public JSONPropertySegment getPropertySegment(final String propertyName)
    {
        JSONPropertySegment result = null;
        if (propertyName != null && !propertyName.isEmpty())
        {
            result = getPropertySegments()
                .first(new Function1<JSONPropertySegment, Boolean>()
                {
                    @Override
                    public Boolean run(JSONPropertySegment propertySegment)
                    {
                        final JSONQuotedString nameSegment = propertySegment.getNameSegment();
                        return nameSegment.toString().equals(propertyName) ||
                            nameSegment.toUnquotedString().equals(propertyName);
                    }
                });
        }
        return result;
    }

    public JSONSegment getPropertyValueSegment(String propertyName)
    {
        final JSONPropertySegment propertySegment = getPropertySegment(propertyName);
        return propertySegment == null ? null : propertySegment.getValueSegment();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONObjectSegment && equals((JSONObjectSegment)rhs);
    }

    public boolean equals(JSONObjectSegment rhs)
    {
        return rhs != null &&
            segments.equals(rhs.segments);
    }

    @Override
    public String toString()
    {
        return getCombinedText(segments);
    }

    @Override
    public int getStartIndex()
    {
        return getStartIndex(segments);
    }

    @Override
    public int getAfterEndIndex()
    {
        return getAfterEndIndex(segments);
    }
}
