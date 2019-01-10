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

    /**
     * Get the property in this JSON object with the provided propertyName.
     * @param propertyName The name of the property to look for.
     * @return The property in this JSON object with the provided propertyName.
     */
    public Result<JSONProperty> getProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        final JSONProperty property = getProperties().first((JSONProperty propertySegment) ->
        {
            return propertySegment.getNameSegment().toUnquotedString().equals(propertyName);
        });
        final Result<JSONProperty> result = property != null
            ? Result.success(property)
            : Result.error(new NotFoundException("No property was found with the name " + Strings.escapeAndQuote(propertyName) + "."));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the value of the property in this JSON object with the provided propertyName.
     * @param propertyName The name of the property to look for.
     * @return The value of the property in this JSON object with the provided propertyName.
     */
    public Result<JSONSegment> getPropertyValue(String propertyName)
    {
        return getProperty(propertyName)
            .then(JSONProperty::getValueSegment);
    }

    /**
     * Get the quoted-string value of the property in this JSON object with the provided
     * propertyName.
     * @param propertyName The name of the property to look for.
     * @return The quoted-string value of the property in this JSON object with the provided
     * propertyName.
     */
    public Result<JSONQuotedString> getQuotedStringPropertyValue(String propertyName)
    {
        return getPropertyValue(propertyName)
            .thenResult((JSONSegment propertyValue) ->
            {
                return propertyValue instanceof JSONQuotedString
                    ? Result.success((JSONQuotedString)propertyValue)
                    : Result.error(new WrongTypeException("Expected the value of the property named " + Strings.escapeAndQuote(propertyName) + " to be a quoted-string."));
            });
    }

    /**
     * Get the quoted-string value of the property in this JSON object with the provided
     * propertyName.
     * @param propertyName The name of the property to look for.
     * @return The quoted-string value of the property in this JSON object with the provided
     * propertyName.
     */
    public Result<String> getUnquotedStringPropertyValue(String propertyName)
    {
        return getQuotedStringPropertyValue(propertyName)
            .then(JSONQuotedString::toUnquotedString);
    }

    /**
     * Get the object value of the property in this JSON object with the provided propertyName.
     * @param propertyName The name of the property to look for.
     * @return The object value of the property in this JSON object with the provided propertyName.
     */
    public Result<JSONObject> getObjectPropertyValue(String propertyName)
    {
        return getPropertyValue(propertyName)
                   .thenResult((JSONSegment propertyValue) ->
                   {
                       return propertyValue instanceof JSONObject
                                  ? Result.success((JSONObject)propertyValue)
                                  : Result.error(new WrongTypeException("Expected the value of the property named " + Strings.escapeAndQuote(propertyName) + " to be an object."));
                   });
    }

    /**
     * Get the array value of the property in this JSON object with the provided propertyName.
     * @param propertyName The name of the property to look for.
     * @return The array value of the property in this JSON object with the provided propertyName.
     */
    public Result<JSONArray> getArrayPropertyValue(String propertyName)
    {
        return getPropertyValue(propertyName)
                   .thenResult((JSONSegment propertyValue) ->
                   {
                       return propertyValue instanceof JSONArray
                                  ? Result.success((JSONArray)propertyValue)
                                  : Result.error(new WrongTypeException("Expected the value of the property named " + Strings.escapeAndQuote(propertyName) + " to be an array."));
                   });
    }

    /**
     * Get the number token of the property in this JSON object with the provided propertyName.
     * @param propertyName The name of the property to look for.
     * @return The number token of the property in this JSON object with the provided propertyName.
     */
    public Result<JSONToken> getNumberTokenPropertyValue(String propertyName)
    {
        return getPropertyValue(propertyName)
            .thenResult((JSONSegment propertyValue) ->
            {
                Result<JSONToken> result = null;
                if (propertyValue instanceof JSONToken)
                {
                    final JSONToken propertyValueToken = (JSONToken)propertyValue;
                    if (propertyValueToken.getType() == JSONTokenType.Number)
                    {
                        result = Result.success(propertyValueToken);
                    }
                }
                if (result == null)
                {
                    result = Result.error(new WrongTypeException("Expected the value of the property named " + Strings.escapeAndQuote(propertyName) + " to be a number."));
                }
                return result;
            });
    }

    /**
     * Get the number value of the property in this JSON object with the provided propertyName.
     * @param propertyName The name of the property to look for.
     * @return The number value of the property in this JSON object with the provided propertyName.
     */
    public Result<Double> getNumberPropertyValue(String propertyName)
    {
        return getNumberTokenPropertyValue(propertyName)
            .then((JSONToken numberToken) -> Double.valueOf(numberToken.toString()));
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
