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
        return getNameSegment().getUnquotedString();
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
        return segments
                .skip(1)
                .skipUntil(new Function1<JSONSegment, Boolean>()
                {
                    @Override
                    public Boolean run(JSONSegment segment)
                    {
                        return segment instanceof JSONToken &&
                                ((JSONToken)segment).getType() == JSONTokenType.Colon;
                    }
                })
                .first(new Function1<JSONSegment, Boolean>()
                {
                    @Override
                    public Boolean run(JSONSegment segment)
                    {
                        boolean result = true;
                        if (segment instanceof JSONToken)
                        {
                            final JSONToken token = (JSONToken)segment;
                            result = token.getType() != JSONTokenType.Colon &&
                                    token.getType() != JSONTokenType.Whitespace;
                        }
                        return result;
                    }
                });

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