package qub;

/**
 * A property within a JSON object.
 */
public class JSONProperty implements MapEntry<String,JSONSegment>
{
    private final String name;
    private final JSONSegment value;

    private JSONProperty(String name, JSONSegment value)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNull(value, "value");

        this.name = name;
        this.value = value;
    }

    public static JSONProperty create(String name, boolean value)
    {
        return JSONProperty.create(name, JSONBoolean.get(value));
    }

    public static JSONProperty create(String name, long value)
    {
        return JSONProperty.create(name, JSONNumber.create(value));
    }

    public static JSONProperty create(String name, double value)
    {
        return JSONProperty.create(name, JSONNumber.create(value));
    }

    public static JSONProperty create(String name, String value)
    {
        PreCondition.assertNotNull(value, "value");

        return JSONProperty.create(name, JSONString.get(value));
    }

    public static JSONProperty create(String name, JSONSegment value)
    {
        return new JSONProperty(name, value);
    }

    /**
     * Get the name of this {@link JSONProperty}.
     * @return The name of this {@link JSONProperty}.
     */
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getKey()
    {
        return this.getName();
    }

    @Override
    public JSONSegment getValue()
    {
        return this.value;
    }

    public <T extends JSONSegment> Result<T> getValueAsOrNull(java.lang.Class<T> type)
    {
        PreCondition.assertNotNull(type, "type");

        return JSON.asOrNull(this.value, type, JSON.thePropertyNamed(this.name));
    }

    /**
     * Get this property's value as the provided {@link JSONSegment} type.
     * @param type The {@link JSONSegment} type to get this property's value as.
     * @param <T> The {@link JSONSegment} type to get this property's value as.
     * @return This property's value as the provided {@link JSONSegment} type.
     */
    public <T extends JSONSegment> Result<T> getValueAs(java.lang.Class<T> type)
    {
        PreCondition.assertNotNull(type, "type");

        return JSON.as(this.value, type, JSON.thePropertyNamed(this.name));
    }

    public Result<JSONNull> getNullValueSegment()
    {
        return this.getValueAs(JSONNull.class);
    }

    public Result<Void> getNullValue()
    {
        return this.getNullValueSegment()
            .then(() -> {});
    }

    public Result<JSONBoolean> getBooleanValueSegment()
    {
        return this.getValueAs(JSONBoolean.class);
    }

    public Result<Boolean> getBooleanValue()
    {
        return this.getBooleanValueSegment().then(JSONBoolean::getValue);
    }

    public Result<Boolean> getBooleanOrNullValue()
    {
        return this.getValueAsOrNull(JSONBoolean.class).then(JSON::toBooleanOrNull);
    }

    public Result<JSONNumber> getNumberValueSegment()
    {
        return this.getValueAs(JSONNumber.class);
    }

    public Result<Number> getNumberValue()
    {
        return this.getNumberValueSegment().then(JSONNumber::getNumberValue);
    }

    public Result<Number> getNumberOrNullValue()
    {
        return this.getValueAsOrNull(JSONNumber.class).then(JSON::toNumberOrNull);
    }

    public Result<Double> getDoubleValue()
    {
        return this.getNumberValueSegment().then(JSONNumber::getDoubleValue);
    }

    public Result<Double> getDoubleOrNullValue()
    {
        return this.getValueAsOrNull(JSONNumber.class).then(JSON::toDoubleOrNull);
    }

    public Result<Long> getLongValue()
    {
        return this.getNumberValueSegment().then((JSONNumber jsonNumber) -> jsonNumber.getLongValue().await());
    }

    public Result<Long> getLongOrNullValue()
    {
        return this.getValueAsOrNull(JSONNumber.class).then((JSONNumber jsonNumber) -> JSON.toLongOrNull(jsonNumber).await());
    }

    public Result<Integer> getIntegerValue()
    {
        return this.getNumberValueSegment().then((JSONNumber jsonNumber) -> jsonNumber.getIntegerValue().await());
    }

    public Result<Integer> getIntegerOrNullValue()
    {
        return this.getValueAsOrNull(JSONNumber.class).then((JSONNumber jsonNumber) -> JSON.toIntegerOrNull(jsonNumber).await());
    }

    public Result<JSONString> getStringValueSegment()
    {
        return this.getValueAs(JSONString.class);
    }

    public Result<String> getStringValue()
    {
        return this.getStringValueSegment().then(JSONString::getValue);
    }

    public Result<String> getStringOrNullValue()
    {
        return this.getValueAsOrNull(JSONString.class).then(JSON::toStringOrNull);
    }

    public Result<JSONObject> getObjectValue()
    {
        return this.getValueAs(JSONObject.class);
    }

    public Result<JSONObject> getObjectOrNullValue()
    {
        return this.getValueAsOrNull(JSONObject.class);
    }

    public Result<JSONArray> getArrayValue()
    {
        return this.getValueAs(JSONArray.class);
    }

    public Result<JSONArray> getArrayOrNullValue()
    {
        return this.getValueAsOrNull(JSONArray.class);
    }

    @Override
    public String toString()
    {
        return JSONSegment.toString((Function2<IndentedCharacterWriteStream,JSONFormat,Result<Integer>>)this::toString);
    }

    /**
     * Get the String representation of this {@link JSONProperty} using the provided format.
     * @param format The format to use when converting this {@link JSONProperty} to a string.
     * @return The number of characters that were written.
     */
    public String toString(JSONFormat format)
    {
        PreCondition.assertNotNull(format, "format");

        return JSONSegment.toString((IndentedCharacterWriteStream stream) -> this.toString(stream, format));
    }

    /**
     * Write the String representation of this {@link JSONProperty} to the provided stream.
     * @param stream The stream to write the String representation of this {@link JSONProperty} to.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(CharacterWriteStream stream)
    {
        PreCondition.assertNotNull(stream, "stream");

        return this.toString(IndentedCharacterWriteStream.create(stream));
    }

    /**
     * Write the String representation of this {@link JSONProperty} to the provided stream.
     * @param stream The stream to write the String representation of this {@link JSONProperty} to.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(IndentedCharacterWriteStream stream)
    {
        return this.toString(stream, JSONFormat.consise);
    }

    /**
     * Write the String representation of this {@link JSONProperty} to the provided stream.
     * @param stream The stream to write the String representation of this {@link JSONProperty} to.
     * @param format The format to use when converting this {@link JSONProperty} to a string.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(CharacterWriteStream stream, JSONFormat format)
    {
        PreCondition.assertNotNull(stream, "stream");

        return this.toString(IndentedCharacterWriteStream.create(stream), format);
    }

    public Result<Integer> toString(IndentedCharacterWriteStream stream, JSONFormat format)
    {
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertNotDisposed(stream, "stream");
        PreCondition.assertNotNull(format, "format");

        return Result.create(() ->
        {
            int result = 0;

            result += stream.write(Strings.quote(this.name)).await();
            result += stream.write(':').await();
            final String afterPropertySeparator = format.getAfterPropertySeparator();
            if (!Strings.isNullOrEmpty(afterPropertySeparator))
            {
                result += stream.write(afterPropertySeparator).await();
            }
            result += this.value.toString(stream, format).await();

            PostCondition.assertGreaterThanOrEqualTo(result, 5, "result");

            return result;
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONProperty && this.equals((JSONProperty)rhs);
    }

    public boolean equals(JSONProperty rhs)
    {
        return rhs != null &&
            this.name.equals(rhs.name) &&
            this.value.equals(rhs.value);
    }
}
