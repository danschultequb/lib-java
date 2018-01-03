package qub;

public class JSONObject extends JSONSegment
{
    private final Iterable<JSONSegment> segments;

    public JSONObject(Iterable<JSONSegment> segments)
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

    public Iterable<JSONProperty> getProperties()
    {
        return segments.instanceOf(JSONProperty.class);
    }

    public JSONProperty getProperty(final String propertyName)
    {
        JSONProperty result = null;
        if (propertyName != null && !propertyName.isEmpty())
        {
            result = getProperties()
                .first(new Function1<JSONProperty, Boolean>()
                {
                    @Override
                    public Boolean run(JSONProperty propertySegment)
                    {
                        final JSONQuotedString nameSegment = propertySegment.getNameSegment();
                        return nameSegment.toString().equals(propertyName) ||
                            nameSegment.toUnquotedString().equals(propertyName);
                    }
                });
        }
        return result;
    }

    public JSONSegment getPropertyValue(String propertyName)
    {
        final JSONProperty propertySegment = getProperty(propertyName);
        return propertySegment == null ? null : propertySegment.getValueSegment();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONObject && equals((JSONObject)rhs);
    }

    public boolean equals(JSONObject rhs)
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
