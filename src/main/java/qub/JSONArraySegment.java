package qub;

public class JSONArraySegment extends JSONSegment
{
    private final Iterable<JSONSegment> segments;

    public JSONArraySegment(Iterable<JSONSegment> segments)
    {
        this.segments = segments;
    }

    public JSONToken getLeftSquareBracket()
    {
        return (JSONToken)segments.first();
    }

    public JSONToken getRightSquareBracket()
    {
        JSONToken result = null;

        final JSONSegment lastSegment = segments.last();
        if (lastSegment instanceof JSONToken)
        {
            final JSONToken lastToken = (JSONToken)lastSegment;
            if (lastToken.getType() == JSONTokenType.RightSquareBracket)
            {
                result = lastToken;
            }
        }

        return result;
    }

    public Indexable<JSONSegment> getElementSegments()
    {
        final List<JSONSegment> result = new ArrayList<>();

        Iterable<JSONSegment> innerSegments = segments.skip(1);
        if (getRightSquareBracket() != null)
        {
            innerSegments = innerSegments.skipLast(1);
        }

        boolean expectingElement = true;
        for (final JSONSegment segment : innerSegments)
        {
            if (segment instanceof JSONToken)
            {
                final JSONToken token = (JSONToken)segment;
                switch (token.getType())
                {
                    case False:
                    case Null:
                    case Number:
                    case QuotedString:
                    case True:
                        result.add(token);
                        expectingElement = false;
                        break;

                    case Comma:
                        if (expectingElement)
                        {
                            result.add(null);
                        }
                        else
                        {
                            expectingElement = true;
                        }
                        break;
                }
            }
            else
            {
                result.add(segment);
                expectingElement = false;
            }
        }

        if (result.any() && expectingElement)
        {
            result.add(null);
        }

        return result;
    }

    public int getElementCount()
    {
        return getElementSegments().getCount();
    }

    public JSONSegment getElementSegment(int index)
    {
        return getElementSegments().get(index);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONArraySegment && equals((JSONArraySegment)rhs);
    }

    public boolean equals(JSONArraySegment rhs)
    {
        return rhs != null &&
            segments.equals(rhs.segments);
    }

    @Override
    public String toString()
    {
        return JSONSegment.getCombinedText(segments);
    }

    @Override
    public int getStartIndex()
    {
        return JSONSegment.getStartIndex(segments);
    }

    @Override
    public int getAfterEndIndex()
    {
        return JSONSegment.getAfterEndIndex(segments);
    }
}
