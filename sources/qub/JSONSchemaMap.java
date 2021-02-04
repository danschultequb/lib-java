package qub;

public class JSONSchemaMap
{
    private final JSONObject json;
    private final MutableMap<String,JSONSchema> schemaMap;
    private final String elementName;

    private JSONSchemaMap(String elementName)
    {
        PreCondition.assertNotNullAndNotEmpty(elementName, "elementName");

        this.json = JSONObject.create();
        this.schemaMap = Map.create();
        this.elementName = elementName;
    }

    public static JSONSchemaMap create(String elementName)
    {
        return new JSONSchemaMap(elementName);
    }

    public static JSONSchemaMap parse(JSONObject json, String elementName)
    {
        PreCondition.assertNotNullAndNotEmpty(elementName, "elementName");

        JSONSchemaMap result;
        if (json == null)
        {
            result = null;
        }
        else
        {
            result = JSONSchemaMap.create(elementName);
            for (final JSONProperty propertyJson : json.getProperties())
            {
                final String propertySchemaName = propertyJson.getName();
                final JSONObject propertySchemaJson = propertyJson.getObjectOrNullValue()
                    .catchError(WrongTypeException.class)
                    .await();
                if (propertySchemaJson != null)
                {
                    final JSONSchema propertySchema = JSONSchema.parse(propertySchemaJson).await();
                    result.set(propertySchemaName, propertySchema);
                }
            }
        }
        return result;
    }

    public Iterable<String> getElementNames()
    {
        return this.schemaMap.getKeys();
    }

    public Result<JSONSchema> get(String elementName)
    {
        PreCondition.assertNotNullAndNotEmpty(elementName, "elementName");

        return Result.create(() ->
        {
            return this.schemaMap.get(elementName)
                .convertError(NotFoundException.class, () -> new NotFoundException("No JSON Schema found for the " + this.elementName + " " + Strings.escapeAndQuote(elementName) + "."))
                .await();
        });
    }

    public JSONSchemaMap set(String schemaPropertyName, JSONSchema jsonSchema)
    {
        PreCondition.assertNotNullAndNotEmpty(schemaPropertyName, "schemaPropertyName");
        PreCondition.assertNotNull(jsonSchema, "jsonSchema");

        this.json.setObject(schemaPropertyName, jsonSchema.toJson());
        this.schemaMap.set(schemaPropertyName, jsonSchema);

        return this;
    }

    public JSONSchemaMap setAll(Iterable<MapEntry<String,JSONSchema>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        for (final MapEntry<String,JSONSchema> entry : entries)
        {
            this.set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Map<String,JSONSchema> toMap()
    {
        return this.schemaMap;
    }

    public JSONObject toJson()
    {
        return this.json;
    }
}
