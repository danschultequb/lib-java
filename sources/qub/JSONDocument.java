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

    /**
     * Get the root of this JSON document.
     * @return The root of this JSON document.
     */
    public Result<JSONSegment> getRoot()
    {
        final JSONSegment rootSegment = segments.first((JSONSegment segment) ->
            {
                boolean result = segment instanceof JSONObject || segment instanceof JSONArray;
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
            });
        return rootSegment != null ? Result.success(rootSegment) : Result.error(new NotFoundException("No root was found in the JSON document."));
    }

    /**
     * Get the root of this JSON document if it is a JSON array.
     * @return The root of this JSON document if it is a JSON array.
     */
    public Result<JSONArray> getRootArray()
    {
        return getRoot()
            .thenResult((JSONSegment root) ->
            {
                return root instanceof JSONArray
                   ? Result.success((JSONArray)root)
                   : Result.error(new WrongTypeException("Expected the root of the JSON document to be an array."));
            });
    }

    /**
     * Get the root of this JSON document if it is a JSON object.
     * @return The root of this JSON document if it is a JSON object.
     */
    public Result<JSONObject> getRootObject()
    {
        return getRoot()
            .thenResult((JSONSegment root) ->
            {
                return root instanceof JSONObject
                    ? Result.success((JSONObject)root)
                    : Result.error(new WrongTypeException("Expected the root of the JSON document to be an object."));
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
