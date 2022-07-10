package qub;

/**
 * A JSON object.
 */
public class JSONObject implements JSONSegment, MutableMap<String,JSONSegment>
{
    private final MutableMap<String,JSONSegment> properties;

    private JSONObject(MutableMap<String,JSONSegment> properties)
    {
        PreCondition.assertNotNull(properties, "properties");

        this.properties = properties;
    }

    public static JSONObject create(JSONProperty... properties)
    {
        PreCondition.assertNotNull(properties, "properties");

        return JSONObject.create(Iterable.create(properties));
    }

    public static JSONObject create(Iterable<JSONProperty> properties)
    {
        PreCondition.assertNotNull(properties, "properties");

        return JSONObject.create(properties.iterate());
    }

    public static JSONObject create(Iterator<JSONProperty> properties)
    {
        PreCondition.assertNotNull(properties, "properties");

        return JSONObject.create(
            properties.toMap(
                JSONProperty::getName,
                JSONProperty::getValue));
    }

    public static JSONObject create(Map<String,JSONSegment> properties)
    {
        PreCondition.assertNotNull(properties, "properties");

        return new JSONObject(JavaConcurrentHashMap.create(properties));
    }

    /**
     * Get whether or not this JSONObject contains a property with the provided name.
     * @param propertyName The name of the property to look for.
     * @return Whether or not this JSONObject contains a property with the provided name.
     */
    public boolean contains(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        return this.properties.containsKey(propertyName);
    }

    @Override
    public boolean containsKey(String propertyName)
    {
        return this.contains(propertyName);
    }

    @Override
    public Result<JSONSegment> get(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        return this.properties.get(propertyName)
            .convertError(NotFoundException.class, () -> new NotFoundException("No property found with the name: " + Strings.escapeAndQuote(propertyName)));
    }

    public <T extends JSONSegment> Result<T> get(String propertyName, java.lang.Class<T> propertyValueType)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValueType, "propertyValueType");

        return this.get(propertyName)
            .then((JSONSegment propertyValue) -> JSON.as(propertyValue, propertyValueType, JSON.thePropertyNamed(propertyName)).await());
    }

    private <T extends JSONSegment> Result<T> getOrNull(String propertyName, java.lang.Class<T> propertyValueType)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValueType, "propertyValueType");

        return this.get(propertyName)
            .then((JSONSegment propertyValue) -> JSON.asOrNull(propertyValue, propertyValueType, JSON.thePropertyNamed(propertyName)).await());
    }

    private <T extends JSONSegment> Result<T> getOrCreate(String propertyName, java.lang.Class<T> propertyValueType, Function0<T> creator)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValueType, "propertyValueType");
        PreCondition.assertNotNull(creator, "creator");

        return this.get(propertyName, propertyValueType)
            .catchError(NotFoundException.class, () ->
            {
                final T propertyValue = creator.run();
                this.set(propertyName, propertyValue);
                return propertyValue;
            });
    }

    public Result<JSONObject> getObject(String propertyName)
    {
        return this.get(propertyName, JSONObject.class);
    }

    public Result<JSONObject> getOrCreateObject(String propertyName)
    {
        return this.getOrCreate(propertyName, JSONObject.class, JSONObject::create);
    }

    public Result<JSONObject> getObjectOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONObject.class);
    }

    public Result<JSONArray> getArray(String propertyName)
    {
        return this.get(propertyName, JSONArray.class);
    }

    public Result<JSONArray> getArrayOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONArray.class);
    }

    public Result<JSONArray> getOrCreateArray(String propertyName)
    {
        return this.getOrCreate(propertyName, JSONArray.class, JSONArray::create);
    }

    public Result<JSONBoolean> getBooleanSegment(String propertyName)
    {
        return this.get(propertyName, JSONBoolean.class);
    }

    public Result<Boolean> getBoolean(String propertyName)
    {
        return this.getBooleanSegment(propertyName)
            .then(JSONBoolean::getValue);
    }

    public Result<Boolean> getBooleanOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONBoolean.class)
            .then(JSON::toBooleanOrNull);
    }

    public Result<JSONNumber> getNumberSegment(String propertyName)
    {
        return this.get(propertyName, JSONNumber.class);
    }

    public Result<Number> getNumber(String propertyName)
    {
        return this.getNumberSegment(propertyName)
            .then(JSONNumber::getNumberValue);
    }

    public Result<Number> getNumberOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONNumber.class)
            .then(JSON::toNumberOrNull);
    }

    public Result<Double> getDouble(String propertyName)
    {
        return this.getNumberSegment(propertyName)
            .then(JSONNumber::getDoubleValue);
    }

    public Result<Double> getDoubleOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONNumber.class)
            .then(JSON::toDoubleOrNull);
    }

    public Result<Long> getLong(String propertyName)
    {
        return this.getNumberSegment(propertyName)
            .then((JSONNumber jsonNumber) -> jsonNumber.getLongValue().await());
    }

    public Result<Long> getLongOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONNumber.class)
            .then((JSONNumber jsonNumber) -> JSON.toLongOrNull(jsonNumber).await());
    }

    public Result<Integer> getInteger(String propertyName)
    {
        return this.getNumberSegment(propertyName)
            .then((JSONNumber jsonNumber) -> jsonNumber.getIntegerValue().await());
    }

    public Result<Integer> getIntegerOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONNumber.class)
            .then((JSONNumber jsonNumber) -> JSON.toIntegerOrNull(jsonNumber).await());
    }

    public Result<Void> getNull(String propertyName)
    {
        return this.get(propertyName, JSONNull.class)
            .then(() -> null);
    }

    public Result<JSONString> getStringSegment(String propertyName)
    {
        return this.get(propertyName, JSONString.class);
    }

    public Result<String> getString(String propertyName)
    {
        return this.getStringSegment(propertyName)
            .then(JSONString::getValue);
    }

    public Result<String> getStringOrNull(String propertyName)
    {
        return this.getOrNull(propertyName, JSONString.class)
            .then(JSON::toStringOrNull);
    }

    public Result<JSONProperty> getProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        return this.get(propertyName)
            .then((JSONSegment propertyValue) -> JSONProperty.create(propertyName, propertyValue));
    }

    /**
     * Get the names of the properties in this {@link JSONObject}.
     */
    public Iterator<String> iteratePropertyNames()
    {
        return this.properties.iterateKeys();
    }

    /**
     * Get the values of the properties in this {@link JSONObject}.
     */
    public Iterator<JSONSegment> iteratePropertyValues()
    {
        return this.properties.iterateValues();
    }

    /**
     * Get the properties in this JSONObject.
     * @return The properties in this JSONObject.
     */
    public Iterable<JSONProperty> getProperties()
    {
        return this.properties.map((MapEntry<String,JSONSegment> entry) -> JSONProperty.create(entry.getKey(), entry.getValue()));
    }

    @Override
    public Iterator<MapEntry<String,JSONSegment>> iterate()
    {
        return this.properties.iterate();
    }

    @Override
    public JSONObject clear()
    {
        this.properties.clear();
        return this;
    }

    @Override
    public JSONObject set(String propertyName, JSONSegment propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        this.properties.set(propertyName, propertyValue);

        return this;
    }

    @Override
    public JSONObject setAll(Iterable<? extends MapEntry<String,JSONSegment>> properties)
    {
        PreCondition.assertNotNull(properties, "properties");

        return (JSONObject)MutableMap.super.setAll(properties);
    }

    public JSONObject set(MapEntry<String,JSONSegment> property)
    {
        PreCondition.assertNotNull(property, "property");

        return this.set(property.getKey(), property.getValue());
    }

    public JSONObject setObject(String propertyName, JSONObject propertyValue)
    {
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        return this.set(propertyName, propertyValue);
    }

    public JSONObject setObjectOrNull(String propertyName, JSONObject propertyValue)
    {
        return this.set(propertyName, propertyValue == null ? JSONNull.segment : propertyValue);
    }

    public JSONObject setArray(String propertyName, JSONArray propertyValue)
    {
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        return this.set(propertyName, propertyValue);
    }

    public JSONObject setArrayOrNull(String propertyName, JSONArray propertyValue)
    {
        return this.set(propertyName, propertyValue == null ? JSONNull.segment : propertyValue);
    }

    public JSONObject setArray(String propertyName, Iterable<JSONSegment> arrayElements)
    {
        PreCondition.assertNotNull(arrayElements, "arrayElements");

        return this.setArray(propertyName, JSONArray.create(arrayElements));
    }

    public JSONObject setArrayOrNull(String propertyName, Iterable<JSONSegment> arrayElements)
    {
        return this.set(propertyName, arrayElements == null ? JSONNull.segment : JSONArray.create(arrayElements));
    }

    public JSONObject setNull(String propertyName)
    {
        return this.set(propertyName, JSONNull.segment);
    }

    public JSONObject setBoolean(String propertyName, boolean propertyValue)
    {
        return this.set(propertyName, JSONBoolean.get(propertyValue));
    }

    public JSONObject setBooleanOrNull(String propertyName, Boolean propertyValue)
    {
        return this.set(propertyName, propertyValue == null ? JSONNull.segment : JSONBoolean.get(propertyValue));
    }

    public JSONObject setNumber(String propertyName, long propertyValue)
    {
        return this.set(propertyName, JSONNumber.create(propertyValue));
    }

    public JSONObject setNumber(String propertyName, double propertyValue)
    {
        return this.set(propertyName, JSONNumber.create(propertyValue));
    }

    public JSONObject setNumber(String propertyName, Number propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        return this.set(propertyName, JSONNumber.create(propertyValue));
    }

    public JSONObject setNumberOrNull(String propertyName, long propertyValue)
    {
        return this.setNumber(propertyName, propertyValue);
    }

    public JSONObject setNumberOrNull(String propertyName, double propertyValue)
    {
        return this.setNumber(propertyName, propertyValue);
    }

    public JSONObject setNumberOrNull(String propertyName, Number propertyValue)
    {
        return this.set(propertyName, propertyValue == null ? JSONNull.segment : JSONNumber.create(propertyValue));
    }

    public JSONObject setString(String propertyName, String propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        return this.set(propertyName, JSONString.get(propertyValue));
    }

    public JSONObject setStringOrNull(String propertyName, String propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        return this.set(propertyName, propertyValue == null ? JSONNull.segment : JSONString.get(propertyValue));
    }

    @Override
    public Result<JSONSegment> remove(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        return this.properties.remove(propertyName)
            .convertError(NotFoundException.class, () -> new NotFoundException("No property exists in this JSONObject with the name: " + Strings.escapeAndQuote(propertyName)));
    }

    @Override
    public String toString()
    {
        return JSONSegment.toString(this);
    }

    @Override
    public Result<Integer> toString(IndentedCharacterWriteStream stream, JSONFormat format)
    {
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertNotDisposed(stream, "stream");
        PreCondition.assertNotNull(format, "format");

        stream.setSingleIndent(format.getSingleIndent());

        final String newLine = format.getNewLine();
        final boolean hasNewLine = !Strings.isNullOrEmpty(newLine);

        return Result.create(() ->
        {
            int result = 0;

            result += stream.write('{').await();
            stream.increaseIndent();
            try
            {
                boolean wroteProperty = false;
                for (final JSONProperty property : this.getProperties())
                {
                    if (!wroteProperty)
                    {
                        wroteProperty = true;
                    }
                    else
                    {
                        result += stream.write(',').await();
                    }
                    if (hasNewLine)
                    {
                        result += stream.write(newLine).await();
                    }
                    result += property.toString(stream, format).await();
                }
                if (hasNewLine && wroteProperty)
                {
                    result += stream.write(newLine).await();
                }
            }
            finally
            {
                stream.decreaseIndent();
            }
            result += stream.write('}').await();

            PostCondition.assertGreaterThanOrEqualTo(result, 2, "result");

            return result;
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONObject && this.equals((JSONObject)rhs);
    }

    public boolean equals(JSONObject rhs)
    {
        return rhs != null &&
            this.properties.equals(rhs.properties);
    }
}
