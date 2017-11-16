package qub;

public class JSONDocument
{
    private final Iterable<JSONSegment> segments;

    public JSONDocument(Iterable<JSONSegment> segments)
    {
        this.segments = segments;
    }

    public Iterable<JSONSegment> getSegments()
    {
        return segments;
    }

    public JSONSegment getRoot()
    {
        return segments.first(new Function1<JSONSegment, Boolean>()
        {
            @Override
            public Boolean run(JSONSegment segment)
            {
                boolean result = segment instanceof JSONObjectSegment || segment instanceof JSONArraySegment;
                if (!result && segment instanceof JSONToken)
                {
                    final JSONToken token = (JSONToken)segment;
                    switch (token.getType())
                    {
                        case False:
                        case Null:
                        case Number:
                        case QuotedString:
                        case True:
                            result = true;
                            break;
                    }
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

    public int getLength()
    {
        return JSONSegment.getAfterEndIndex(segments);
    }
}
