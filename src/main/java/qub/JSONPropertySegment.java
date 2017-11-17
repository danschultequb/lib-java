package qub;

public class JSONPropertySegment extends JSONSegment
{
    private final Iterable<JSONSegment> segments;

    public JSONPropertySegment(Iterable<JSONSegment> segments)
    {
        this.segments = segments;
    }

    public String getName()
    {
        return getNameSegment().toUnquotedString();
    }

    public JSONQuotedString getNameSegment()
    {
        return (JSONQuotedString)segments.first();
    }

    public JSONToken getColonSegment()
    {
        return segments
                .instanceOf(JSONToken.class)
                .first(new Function1<JSONToken, Boolean>()
                {
                    @Override
                    public Boolean run(JSONToken JSONToken)
                    {
                        return JSONToken.getType() == JSONTokenType.Colon;
                    }
                });
    }

    public JSONSegment getValueSegment()
    {
        JSONSegment result = null;
        if (segments.getCount() > 2)
        {
            final JSONSegment lastSegment = segments.last();
            if (lastSegment instanceof JSONToken)
            {
                final JSONToken lastToken = (JSONToken)lastSegment;
                switch (lastToken.getType())
                {
                    case False:
                    case Null:
                    case Number:
                    case QuotedString:
                    case True:
                        result = lastToken;
                        break;
                }
            }
            else
            {
                result = lastSegment;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONPropertySegment && equals((JSONPropertySegment)rhs);
    }

    public boolean equals(JSONPropertySegment rhs)
    {
        return rhs != null && segments.equals(rhs.segments);
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