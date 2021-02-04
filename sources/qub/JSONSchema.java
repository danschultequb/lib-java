package qub;

public class JSONSchema
{
    public static final String schemaPropertyName = "$schema";
    public static final String typePropertyName = "type";
    public static final String propertiesPropertyName = "properties";
    public static final String refPropertyName = "$ref";
    public static final String additionalPropertiesPropertyName = "additionalProperties";
    public static final String definitionsPropertyName = "definitions";
    public static final String descriptionPropertyName = "description";
    public static final String enumPropertyName = "enum";
    public static final String itemsPropertyName = "items";
    public static final String requiredPropertyName = "required";
    public static final String minLengthPropertyName = "minLength";
    public static final String oneOfPropertyName = "oneOf";

    private final JSONObject json;
    private String schema;
    private String type;
    private JSONSchemaMap properties;
    private String ref;
    private Object additionalProperties;
    private JSONSchemaMap definitions;
    private String description;
    private List<Object> enums;
    private JSONSchema items;
    private List<String> required;
    private Integer minLength;
    private List<JSONSchema> oneOf;

    private JSONSchema(JSONObject json)
    {
        PreCondition.assertNotNull(json, "json");

        this.json = json;
    }

    public static JSONSchema create()
    {
        return new JSONSchema(JSONObject.create());
    }

    public static Result<JSONSchema> parse(File file)
    {
        PreCondition.assertNotNull(file, "file");

        return Result.createUsing(
            () -> file.getContentsReadStream().await(),
            (CharacterToByteReadStream readStream) ->
            {
                return JSONSchema.parse(readStream).await();
            });
    }

    public static Result<JSONSchema> parse(CharacterReadStream readStream)
    {
        PreCondition.assertNotNull(readStream, "readStream");

        return JSONSchema.parse(CharacterReadStream.iterate(readStream));
    }

    public static Result<JSONSchema> parse(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return JSONSchema.parse(Strings.iterate(text));
    }

    public static Result<JSONSchema> parse(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return Result.create(() ->
        {
            final JSONObject json = JSON.parseObject(characters).await();
            return JSONSchema.parse(json).await();
        });
    }

    public static Result<JSONSchema> parse(JSONObject json)
    {
        PreCondition.assertNotNull(json, "json");

        return Result.create(() ->
        {
            final JSONSchema result = new JSONSchema(json);

            result.schema = json.getStringOrNull(JSONSchema.schemaPropertyName)
                .catchError(NotFoundException.class)
                .await();
            result.type = json.getStringOrNull(JSONSchema.typePropertyName)
                .catchError(NotFoundException.class)
                .await();
            result.properties = json.getObjectOrNull(JSONSchema.propertiesPropertyName)
                .catchError(NotFoundException.class)
                .then((JSONObject propertiesJson) -> JSONSchemaMap.parse(propertiesJson, "property"))
                .await();
            result.ref = json.getStringOrNull(JSONSchema.refPropertyName)
                .catchError(NotFoundException.class)
                .await();
            result.additionalProperties = json.get(JSONSchema.additionalPropertiesPropertyName)
                .then((JSONSegment additionalPropertiesJson) ->
                {
                    Object additionalProperties;
                    if (additionalPropertiesJson instanceof JSONBoolean)
                    {
                        additionalProperties = ((JSONBoolean)additionalPropertiesJson).getValue();
                    }
                    else if (additionalPropertiesJson instanceof JSONObject)
                    {
                        additionalProperties = JSONSchema.parse((JSONObject)additionalPropertiesJson).await();
                    }
                    else
                    {
                        additionalProperties = null;
                    }
                    return additionalProperties;
                })
                .catchError(NotFoundException.class)
                .await();
            result.definitions = json.getObjectOrNull(JSONSchema.definitionsPropertyName)
                .catchError(NotFoundException.class)
                .then((JSONObject propertiesJson) -> JSONSchemaMap.parse(propertiesJson, "definition"))
                .await();
            result.description = json.getStringOrNull(JSONSchema.descriptionPropertyName)
                .catchError(NotFoundException.class)
                .await();
            result.enums = json.getArrayOrNull(JSONSchema.enumPropertyName)
                .catchError(NotFoundException.class)
                .then((JSONArray enumArray) ->
                {
                    List<Object> enums = null;
                    if (enumArray != null)
                    {
                        enums = List.create();
                        for (final JSONSegment enumElement : enumArray)
                        {
                            if (enumElement instanceof JSONNull)
                            {
                                enums.add(null);
                            }
                            else if (enumElement instanceof JSONString)
                            {
                                enums.add(((JSONString)enumElement).getValue());
                            }
                            else if (enumElement instanceof JSONNumber)
                            {
                                enums.add(((JSONNumber)enumElement).getValue());
                            }
                            else if (enumElement instanceof JSONBoolean)
                            {
                                enums.add(((JSONBoolean)enumElement).getValue());
                            }
                        }
                    }
                    return enums;
                })
                .await();
            result.items = json.getObjectOrNull(JSONSchema.itemsPropertyName)
                .catchError(NotFoundException.class)
                .then((JSONObject itemsJson) ->
                {
                    JSONSchema items = null;
                    if (itemsJson != null)
                    {
                        items = JSONSchema.parse(itemsJson).await();
                    }
                    return items;
                })
                .await();
            result.required = json.getArrayOrNull(JSONSchema.requiredPropertyName)
                .catchError(NotFoundException.class)
                .then((JSONArray requiredJson) ->
                {
                    List<String> required = null;
                    if (requiredJson != null)
                    {
                        required = List.create(requiredJson.instanceOf(JSONString.class).map(JSONString::getValue));
                    }
                    return required;
                })
                .await();
            result.minLength = json.getNumberOrNull(JSONSchema.minLengthPropertyName)
                .catchError(NotFoundException.class)
                .then((Double minLength) -> minLength == null ? null : minLength.intValue())
                .await();
            result.oneOf = json.getArrayOrNull(JSONSchema.oneOfPropertyName)
                .catchError(NotFoundException.class)
                .then((JSONArray oneOfJson) ->
                {
                    List<JSONSchema> oneOf = null;
                    if (oneOfJson != null)
                    {
                        oneOf = oneOfJson
                            .instanceOf(JSONObject.class)
                            .map((JSONObject oneOfJsonObjectElement) -> JSONSchema.parse(oneOfJsonObjectElement).await())
                            .toList();
                    }
                    return oneOf;
                })
                .await();

            return result;
        });
    }

    /**
     * Get the value of the "$schema" property.
     * @return The value of the "$schema" property.
     */
    public String getSchema()
    {
        return this.schema;
    }

    /**
     * Set the value of the "$schema" property.
     * @param schema The value of the "$schema" property.
     * @return This object for method chaining.
     */
    public JSONSchema setSchema(String schema)
    {
        this.json.setStringOrNull(JSONSchema.schemaPropertyName, schema);
        this.schema = schema;

        return this;
    }

    /**
     * Remove the "$schema" property from this JSONSchema object.
     * @return The removed "$schema" property value.
     */
    public String removeSchema()
    {
        final String result = this.schema;

        this.json.remove(JSONSchema.schemaPropertyName).catchError(NotFoundException.class).await();
        this.schema = null;

        return result;
    }

    /**
     * Get the expected JSON type that this schema will match.
     * @return The expected JSON type that this schema will match.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Set the expected JSON type that this schema will match.
     * @param type The expected JSON type that this schema will match.
     * @return This object for method chaining.
     */
    public JSONSchema setType(String type)
    {
        this.json.setStringOrNull(JSONSchema.typePropertyName, type);
        this.type = type;

        return this;
    }

    /**
     * Set the expected JSON type that this schema will match.
     * @param type The expected JSON type that this schema will match.
     * @return This object for method chaining.
     */
    public JSONSchema setType(JSONSchemaType type)
    {
        return this.setType(type == null ? null : type.toString().toLowerCase());
    }

    /**
     * Remove the "type" property from this JSONSchema object.
     * @return The removed "type" property value.
     */
    public String removeType()
    {
        final String result = this.type;

        this.json.remove(JSONSchema.typePropertyName).catchError(NotFoundException.class).await();
        this.type = null;

        return result;
    }

    /**
     * Get the properties for this JSON schema.
     * @return The properties for this JSON schema.
     */
    public Map<String,JSONSchema> getProperties()
    {
        return this.properties == null ? null : this.properties.toMap();
    }

    /**
     * Set the properties for this JSON schema.
     * @param properties The properties for this JSON schema.
     * @return This object for method chaining.
     */
    public JSONSchema setProperties(Map<String,JSONSchema> properties)
    {
        if (properties == null)
        {
            this.properties = null;
            this.json.setNull(JSONSchema.propertiesPropertyName);
        }
        else
        {
            this.properties = JSONSchemaMap.create("property").setAll(properties);
            this.json.setObject(JSONSchema.propertiesPropertyName, this.properties.toJson());
        }
        return this;
    }

    /**
     * Get the names of the properties that this JSONSchema has defined.
     * @return The names of the properties that this JSONSchema has defined;
     */
    public Iterable<String> getPropertyNames()
    {
        return this.properties == null
            ? Iterable.create()
            : this.properties.getElementNames();
    }

    /**
     * Get the JSON schema defined for the provided property name.
     * @param propertyName The name of the property to get the defined JSON schema for.
     * @return The JSON schema defined for the provided property name.
     */
    public Result<JSONSchema> getProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        return Result.create(() ->
        {
            if (this.properties == null)
            {
                throw new NotFoundException("No JSON Schema found for the property " + Strings.escapeAndQuote(propertyName) + ".");
            }

            return this.properties.get(propertyName).await();
        });
    }

    /**
     * Add the JSON schema associated with the provided property name.
     * @param propertyName The name of the property.
     * @param propertySchema The JSON schema associated with the provided property name.
     * @return This object for method chaining.
     */
    public JSONSchema addProperty(String propertyName, JSONSchema propertySchema)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertySchema, "propertySchema");

        if (this.properties == null)
        {
            this.properties = JSONSchemaMap.create("property");
            this.json.setObject(JSONSchema.propertiesPropertyName, this.properties.toJson());
        }
        this.properties.set(propertyName, propertySchema);

        return this;
    }

    /**
     * Get the value of the "$ref" property.
     * @return The value of the "$ref" property.
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * Set the value of the "$ref" property.
     * @param ref The value of the "$ref" property.
     * @return This object for method chaining.
     */
    public JSONSchema setRef(String ref)
    {
        this.json.setStringOrNull(JSONSchema.refPropertyName, ref);
        this.ref = ref;

        return this;
    }

    /**
     * Remove the "$ref" property from this JSONSchema object.
     * @return The removed "$ref" property value.
     */
    public String removeRef()
    {
        final String result = this.ref;

        this.json.remove(JSONSchema.refPropertyName).catchError(NotFoundException.class).await();
        this.ref = null;

        return result;
    }

    /**
     * Get the value of the "additionalProperties" property.
     * @return The value of the "additionalProperties" property.
     */
    public Object getAdditionalProperties()
    {
        return this.additionalProperties;
    }

    /**
     * Get the value of the "additionalProperties" property as a boolean.
     * @return The value of the "additionalProperties" property as a boolean.
     */
    public Boolean getAdditionalPropertiesAsBoolean()
    {
        return Types.as(this.additionalProperties, Boolean.class);
    }

    /**
     * Get the value of the "additionalProperties" property as a JSONSchema.
     * @return The value of the "additionalProperties" property as a JSONSchema.
     */
    public JSONSchema getAdditionalPropertiesAsJSONSchema()
    {
        return Types.as(this.additionalProperties, JSONSchema.class);
    }

    /**
     * Set the value of the "additionalProperties" property.
     * @param additionalProperties The value of the "additionalProperties" property.
     * @return This object for method chaining.
     */
    public JSONSchema setAdditionalProperties(Boolean additionalProperties)
    {
        if (!Comparer.equal(this.additionalProperties, additionalProperties))
        {
            this.json.setBooleanOrNull(JSONSchema.additionalPropertiesPropertyName, additionalProperties);
            this.additionalProperties = additionalProperties;
        }
        return this;
    }

    /**
     * Set the value of the "additionalProperties" property.
     * @param additionalProperties The value of the "additionalProperties" property.
     * @return This object for method chaining.
     */
    public JSONSchema setAdditionalProperties(JSONSchema additionalProperties)
    {
        if (!Comparer.equal(this.additionalProperties, additionalProperties))
        {
            this.json.setObjectOrNull(JSONSchema.additionalPropertiesPropertyName, additionalProperties.toJson());
            this.additionalProperties = additionalProperties;
        }
        return this;
    }

    /**
     * Remove the "additionalProperties" property from this JSONSchema object.
     * @return The removed "additionalProperties" property value.
     */
    public Object removeAdditionalProperties()
    {
        final Object result = this.additionalProperties;

        this.json.remove(JSONSchema.additionalPropertiesPropertyName)
            .catchError(NotFoundException.class)
            .await();
        this.additionalProperties = null;

        return result;
    }

    /**
     * Remove the "additionalProperties" property from this JSONSchema object.
     * @return The removed "additionalProperties" property value.
     */
    public Boolean removeAdditionalPropertiesAsBoolean()
    {
        return Types.as(this.removeAdditionalProperties(), Boolean.class);
    }

    /**
     * Remove the "additionalProperties" property from this JSONSchema object.
     * @return The removed "additionalProperties" property value.
     */
    public JSONSchema removeAdditionalPropertiesAsJSONSchema()
    {
        return Types.as(this.removeAdditionalProperties(), JSONSchema.class);
    }

    /**
     * Get the definitions for this JSON schema.
     * @return The definitions for this JSON schema.
     */
    public Map<String,JSONSchema> getDefinitions()
    {
        return this.definitions == null ? null : this.definitions.toMap();
    }

    /**
     * Set the definitions for this JSON schema.
     * @param definitions The definitions for this JSON schema.
     * @return This object for method chaining.
     */
    public JSONSchema setDefinitions(Map<String,JSONSchema> definitions)
    {
        if (definitions == null)
        {
            this.definitions = null;
            this.json.setNull(JSONSchema.definitionsPropertyName);
        }
        else
        {
            this.definitions = JSONSchemaMap.create("definition").setAll(definitions);
            this.json.setObject(JSONSchema.definitionsPropertyName, this.definitions.toJson());
        }
        return this;
    }

    /**
     * Get the names of the definitions that this JSONSchema has defined.
     * @return The names of the definitions that this JSONSchema has defined;
     */
    public Iterable<String> getDefinitionNames()
    {
        return this.definitions == null
            ? Iterable.create()
            : this.definitions.getElementNames();
    }

    /**
     * Get the JSON schema defined for the provided definition name.
     * @param definitionName The name of the definition to get the defined JSON schema for.
     * @return The JSON schema defined for the provided definition name.
     */
    public Result<JSONSchema> getDefinition(String definitionName)
    {
        PreCondition.assertNotNullAndNotEmpty(definitionName, "definitionName");

        return Result.create(() ->
        {
            if (this.definitions == null)
            {
                throw new NotFoundException("No JSON Schema found for the definition " + Strings.escapeAndQuote(definitionName) + ".");
            }

            return this.definitions.get(definitionName).await();
        });
    }

    /**
     * Add the JSON schema associated with the provided definition name.
     * @param definitionName The name of the definition.
     * @param definitionSchema The JSON schema associated with the provided definition name.
     * @return This object for method chaining.
     */
    public JSONSchema addDefinition(String definitionName, JSONSchema definitionSchema)
    {
        PreCondition.assertNotNullAndNotEmpty(definitionName, "definitionName");
        PreCondition.assertNotNull(definitionSchema, "definitionSchema");

        if (this.definitions == null)
        {
            this.definitions = JSONSchemaMap.create("definition");
            this.json.setObject(JSONSchema.definitionsPropertyName, this.definitions.toJson());
        }
        this.definitions.set(definitionName, definitionSchema);

        return this;
    }

    /**
     * Get the description of this JSONSchema.
     * @return The description of this JSONSchema.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Set the description of this JSONSchema.
     * @param description The description of this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema setDescription(String description)
    {
        this.description = description;
        this.json.setStringOrNull(JSONSchema.descriptionPropertyName, description);

        return this;
    }

    /**
     * Remove and return the description of this JSONSchema.
     * @return The removed description.
     */
    public String removeDescription()
    {
        final String result = this.description;

        this.description = null;
        this.json.remove(JSONSchema.descriptionPropertyName).catchError(NotFoundException.class).await();

        return result;
    }

    /**
     * Get the allowed values for this JSONSchema.
     * @return The allowed values for this JSONSchema.
     */
    public Iterable<Object> getEnum()
    {
        return this.enums;
    }

    /**
     * Set the allowed values for this JSONSchema.
     * @param enums The allowed values for this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema setEnum(Object... enums)
    {
        return this.setEnum(enums == null ? null : Iterable.create(enums));
    }

    /**
     * Set the allowed values for this JSONSchema.
     * @param enums The allowed values for this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema setEnum(Iterable<Object> enums)
    {
        if (enums == null)
        {
            this.enums = null;
            this.json.setNull(JSONSchema.enumPropertyName);
        }
        else
        {
            this.json.setArray(JSONSchema.enumPropertyName,
                JSONArray.create(enums.map(JSONSchema::enumValueToJSON).toList()));
            this.enums = List.create(enums);

        }
        return this;
    }

    /**
     * Add an allowed value for this JSONSchema.
     * @param enumValue The allowed value for this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema addEnum(Object enumValue)
    {
        PreCondition.assertInstanceOf(enumValue, Iterable.create(null, Double.class, Float.class, Long.class, Integer.class, Short.class, Byte.class, Character.class, String.class, Boolean.class), "enumValue");

        if (this.enums == null)
        {
            this.enums = List.create();
        }

        this.json.getOrCreateArray(JSONSchema.enumPropertyName).await()
            .add(JSONSchema.enumValueToJSON(enumValue));
        this.enums.add(JSONSchema.simplifyEnumValue(enumValue));

        return this;
    }

    private static Object simplifyEnumValue(Object enumValue)
    {
        Object simplifiedEnumValue = enumValue;
        if (simplifiedEnumValue instanceof Float)
        {
            simplifiedEnumValue = ((Float)simplifiedEnumValue).doubleValue();
        }
        else if (simplifiedEnumValue instanceof Long)
        {
            simplifiedEnumValue = ((Long)simplifiedEnumValue).doubleValue();
        }
        else if (simplifiedEnumValue instanceof Integer)
        {
            simplifiedEnumValue = ((Integer)simplifiedEnumValue).doubleValue();
        }
        else if (simplifiedEnumValue instanceof Short)
        {
            simplifiedEnumValue = ((Short)simplifiedEnumValue).doubleValue();
        }
        else if (simplifiedEnumValue instanceof Byte)
        {
            simplifiedEnumValue = ((Byte)simplifiedEnumValue).doubleValue();
        }
        else if (simplifiedEnumValue instanceof Character)
        {
            simplifiedEnumValue = ((Character)simplifiedEnumValue).toString();
        }
        return simplifiedEnumValue;
    }

    private static JSONSegment enumValueToJSON(Object enumValue)
    {
        PreCondition.assertInstanceOf(enumValue, Iterable.create(null, Double.class, Float.class, Long.class, Integer.class, Short.class, Byte.class, Character.class, String.class, Boolean.class), "enumValue");

        JSONSegment result = null;
        if (enumValue instanceof Double)
        {
            result = JSONNumber.get((Double)enumValue);
        }
        else if (enumValue instanceof Float)
        {
            result = JSONNumber.get((Float)enumValue);
        }
        else if (enumValue instanceof Long)
        {
            result = JSONNumber.get((Long)enumValue);
        }
        else if (enumValue instanceof Integer)
        {
            result = JSONNumber.get((Integer)enumValue);
        }
        else if (enumValue instanceof Short)
        {
            result = JSONNumber.get((Short)enumValue);
        }
        else if (enumValue instanceof Byte)
        {
            result = JSONNumber.get((Byte)enumValue);
        }
        else if (enumValue instanceof Boolean)
        {
            result = JSONBoolean.get((Boolean)enumValue);
        }
        else if (enumValue instanceof Character)
        {
            result = JSONString.get(((Character)enumValue).toString());
        }
        else if (enumValue instanceof String)
        {
            result = JSONString.get((String)enumValue);
        }
        else if (enumValue == null)
        {
            result = JSONNull.segment;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Remove the provided allowed value from this JSONSchema.
     * @param enumValue The allowed value to remove from this JSONSchema.
     * @return The removed enum value.
     */
    public Object removeEnum(Object enumValue)
    {
        PreCondition.assertInstanceOf(enumValue, Iterable.create(null, Double.class, Float.class, Long.class, Integer.class, Short.class, Byte.class, Character.class, String.class, Boolean.class), "enumValue");

        Object result = null;

        if (this.enums != null)
        {
            final Object simplifiedEnumValue = JSONSchema.simplifyEnumValue(enumValue);
            final int index = this.enums.indexOf(simplifiedEnumValue);
            if (index >= 0)
            {
                result = this.enums.removeAt(index);
                this.json.getArray(JSONSchema.enumPropertyName).await().removeAt(index);
            }
        }

        return result;
    }

    /**
     * Remove the allowed values from this JSONSchema.
     * @return The removed allowed values.
     */
    public Iterable<Object> removeEnum()
    {
        final Iterable<Object> result = this.enums;

        this.enums = null;
        this.json.remove(JSONSchema.enumPropertyName).catchError(NotFoundException.class).await();

        return result;
    }

    /**
     * Get the JSONSchema that each item in a matching array must adhere to.
     * @return The JSONSchema that each item in a matching array must adhere to.
     */
    public JSONSchema getItems()
    {
        return this.items;
    }

    /**
     * Set the JSONSchema that each item in a matching array must adhere to.
     * @param items The JSONSchema that each item in a matching array must adhere to.
     * @return This object for method chaining.
     */
    public JSONSchema setItems(JSONSchema items)
    {
        this.items = items;
        this.json.setObjectOrNull(JSONSchema.itemsPropertyName, items == null ? null : items.toJson());

        return this;
    }

    /**
     * Remove the JSONSchema that each item in a matching array must adhere to.
     * @return The removed JSONSchema that each item in a matching array must adhere to.
     */
    public JSONSchema removeItems()
    {
        final JSONSchema result = this.items;

        this.items = null;
        this.json.remove(JSONSchema.itemsPropertyName).catchError(NotFoundException.class).await();

        return result;
    }

    /**
     * Get the names of the properties that are required in this JSONSchema.
     * @return The names of the properties that are required in this JSONSchema.
     */
    public Iterable<String> getRequired()
    {
        return this.required;
    }

    /**
     * Set the names of the properties that are required in this JSONSchema.
     * @param required The names of the properties that are required in this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema setRequired(String... required)
    {
        return this.setRequired(required == null ? null : Iterable.create(required));
    }

    /**
     * Set the names of the properties that are required in this JSONSchema.
     * @param required The names of the properties that are required in this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema setRequired(Iterable<String> required)
    {
        if (required == null)
        {
            this.required = null;
            this.json.setNull(JSONSchema.requiredPropertyName);
        }
        else
        {
            this.required = List.create();
            this.json.setArray(JSONSchema.requiredPropertyName, JSONArray.create());
            for (final String requiredPropertyName : required)
            {
                this.addRequired(requiredPropertyName);
            }
        }

        return this;
    }

    /**
     * Add the provided required property name.
     * @param requiredPropertyName The required property name.
     * @return This object for method chaining.
     */
    public JSONSchema addRequired(String requiredPropertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(requiredPropertyName, "requiredPropertyName");

        if (this.required == null)
        {
            this.required = List.create();
        }

        this.required.add(requiredPropertyName);
        this.json.getOrCreateArray(JSONSchema.requiredPropertyName).await()
            .add(JSONString.get(requiredPropertyName));

        return this;
    }

    /**
     * Remove the names of the properties that are required in this JSONSchema.
     * @return The names of the properties that are required in this JSONSchema.
     */
    public Iterable<String> removeRequired()
    {
        final Iterable<String> result = this.required;

        this.required = null;
        this.json.remove(JSONSchema.requiredPropertyName).catchError(NotFoundException.class).await();

        return result;
    }

    /**
     * Get the minimum length that a matching String must be.
     * @return The minimum length that a matching String must be.
     */
    public Integer getMinLength()
    {
        return this.minLength;
    }

    /**
     * Set the minimum length that a matching String must be.
     * @param minLength The minimum length that a matching String must be.
     * @return This object for method chaining.
     */
    public JSONSchema setMinLength(int minLength)
    {
        PreCondition.assertGreaterThanOrEqualTo(minLength, 0, "minLength");

        this.minLength = minLength;
        this.json.setNumber(JSONSchema.minLengthPropertyName, minLength);

        return this;
    }

    /**
     * Remove the minimum length that a matching String must be.
     * @return The removed minimum length that a matching String must be.
     */
    public Integer removeMinLength()
    {
        final Integer result = this.minLength;

        this.minLength = null;
        this.json.remove(JSONSchema.minLengthPropertyName);

        return result;
    }

    /**
     * Get the collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @return The collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     */
    public Iterable<JSONSchema> getOneOf()
    {
        return this.oneOf;
    }

    /**
     * Set the collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @param oneOf The collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @return This object for method chaining.
     */
    public JSONSchema setOneOf(JSONSchema... oneOf)
    {
        return this.setOneOf(oneOf == null ? null : Iterable.create(oneOf));
    }

    /**
     * Set the collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @param oneOf The collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @return This object for method chaining.
     */
    public JSONSchema setOneOf(Iterable<JSONSchema> oneOf)
    {
        if (oneOf == null)
        {
            this.oneOf = null;
            this.json.setNull(JSONSchema.oneOfPropertyName);
        }
        else
        {
            this.oneOf = List.create();
            this.json.setArray(JSONSchema.oneOfPropertyName, JSONArray.create());
            for (final JSONSchema oneOfSchema : oneOf)
            {
                this.addOneOf(oneOfSchema);
            }
        }

        return this;
    }

    /**
     * Add to the collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @param oneOf The JSONSchema to add to the collection of JSONSchemas that a matching
     *              JSONSegment must match exactly one of.
     * @return This object for method chaining.
     */
    public JSONSchema addOneOf(JSONSchema oneOf)
    {
        PreCondition.assertNotNull(oneOf, "oneOf");

        if (this.oneOf == null)
        {
            this.oneOf = List.create();
        }

        this.oneOf.add(oneOf);
        this.json.getOrCreateArray(JSONSchema.oneOfPropertyName).await()
            .add(oneOf.toJson());

        return this;
    }

    /**
     * Remove the collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @return The removed collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     */
    public Iterable<JSONSchema> removeOneOf()
    {
        final Iterable<JSONSchema> result = this.oneOf;

        this.oneOf = null;
        this.json.remove(JSONSchema.oneOfPropertyName).catchError(NotFoundException.class).await();

        return result;
    }

    /**
     * Get the JSON representation of this JSONSchema.
     * @return The JSON representation of this JSONSchema.
     */
    public JSONObject toJson()
    {
        return this.json;
    }

    @Override
    public String toString()
    {
        return this.json.toString();
    }

    /**
     * Get the String representation of this JSONSchema using the provided format.
     * @param format The format to use when converting this JSONSchema to a string.
     * @return The number of characters that were written.
     */
    public String toString(JSONFormat format)
    {
        return this.json.toString(format);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(CharacterWriteStream stream)
    {
        return this.json.toString(stream);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(IndentedCharacterWriteStream stream)
    {
        return this.json.toString(stream);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @param format The format to use when converting this JSONSchema to a string.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(CharacterWriteStream stream, JSONFormat format)
    {
        return this.json.toString(stream, format);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @param format The format to use when converting this JSONSchema to a string.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(IndentedCharacterWriteStream stream, JSONFormat format)
    {
        return this.json.toString(stream, format);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONSchema && this.equals((JSONSchema)rhs);
    }

    public boolean equals(JSONSchema rhs)
    {
        return rhs != null && this.json.equals(rhs.json);
    }
}
