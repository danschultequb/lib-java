package qub;

public class JSONSchema extends JSONObjectWrapperBase
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

    private JSONSchema(JSONObject json)
    {
        super(json);
    }

    public static JSONSchema create()
    {
        return JSONSchema.create(JSONObject.create());
    }

    public static JSONSchema create(JSONObject json)
    {
        return new JSONSchema(json);
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
            return JSONSchema.create(json);
        });
    }

    /**
     * Get the value of the "$schema" property.
     * @return The value of the "$schema" property.
     */
    public String getSchema()
    {
        return this.json.getString(JSONSchema.schemaPropertyName)
            .catchError()
            .await();
    }

    /**
     * Set the value of the "$schema" property.
     * @param schema The value of the "$schema" property.
     * @return This object for method chaining.
     */
    public JSONSchema setSchema(String schema)
    {
        this.json.setStringOrNull(JSONSchema.schemaPropertyName, schema);

        return this;
    }

    /**
     * Remove the "$schema" property from this JSONSchema object.
     * @return The removed "$schema" property value.
     */
    public String removeSchema()
    {
        final String result = this.getSchema();

        this.json.remove(JSONSchema.schemaPropertyName)
            .catchError()
            .await();

        return result;
    }

    /**
     * Get the expected JSON type that this schema will match.
     * @return The expected JSON type that this schema will match.
     */
    public String getType()
    {
        return json.getStringOrNull(JSONSchema.typePropertyName)
            .catchError()
            .await();

    }

    /**
     * Set the expected JSON type that this schema will match.
     * @param type The expected JSON type that this schema will match.
     * @return This object for method chaining.
     */
    public JSONSchema setType(String type)
    {
        this.json.setStringOrNull(JSONSchema.typePropertyName, type);

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
        final String result = this.getType();

        this.json.remove(JSONSchema.typePropertyName)
            .catchError()
            .await();

        return result;
    }

    /**
     * Get the properties for this JSON schema.
     * @return The properties for this JSON schema.
     */
    public Map<String,JSONSchema> getProperties()
    {
        MutableMap<String,JSONSchema> result = null;

        final JSONObject propertiesJson = this.json.getObject(JSONSchema.propertiesPropertyName)
            .catchError()
            .await();
        if (propertiesJson != null)
        {
            result = Map.create();
            for (final String propertyName : propertiesJson.getPropertyNames())
            {
                final JSONObject propertyJson = propertiesJson.getObject(propertyName)
                    .catchError()
                    .await();
                if (propertyJson != null)
                {
                    result.set(propertyName, JSONSchema.create(propertyJson));
                }
            }
        }

        return result;
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
            this.json.setNull(JSONSchema.propertiesPropertyName);
        }
        else
        {
            final JSONObject propertiesJson = JSONObject.create();
            for (final MapEntry<String,JSONSchema> property : properties)
            {
                final JSONSchema propertyValue = property.getValue();
                propertiesJson.setObjectOrNull(property.getKey(), propertyValue == null ? null : propertyValue.toJson());
            }
            this.json.setObject(JSONSchema.propertiesPropertyName, propertiesJson);
        }
        return this;
    }

    /**
     * Get the names of the properties that this JSONSchema has defined.
     * @return The names of the properties that this JSONSchema has defined;
     */
    public Iterable<String> getPropertyNames()
    {
        final JSONObject propertiesJson = this.json.getObject(JSONSchema.propertiesPropertyName)
            .catchError()
            .await();
        return propertiesJson == null ? Iterable.create() : propertiesJson.getPropertyNames();
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
            JSONSchema result = null;

            final JSONObject propertiesJson = this.json.getObject(JSONSchema.propertiesPropertyName)
                .catchError()
                .await();
            if (propertiesJson != null)
            {
                final JSONObject propertyJson = propertiesJson.getObject(propertyName)
                    .catchError()
                    .await();
                if (propertyJson != null)
                {
                    result = JSONSchema.create(propertyJson);
                }
            }

            if (result == null)
            {
                throw new NotFoundException("No JSON Schema found for the property " + Strings.escapeAndQuote(propertyName) + ".");
            }

            PostCondition.assertNotNull(result, "result");

            return result;
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

        final JSONObject propertiesJson = this.json.getOrCreateObject(JSONSchema.propertiesPropertyName)
            .catchError()
            .await();
        propertiesJson.setObject(propertyName, propertySchema.toJson());

        return this;
    }

    /**
     * Get the value of the "$ref" property.
     * @return The value of the "$ref" property.
     */
    public String getRef()
    {
        return this.json.getString(JSONSchema.refPropertyName)
            .catchError()
            .await();

    }

    /**
     * Set the value of the "$ref" property.
     * @param ref The value of the "$ref" property.
     * @return This object for method chaining.
     */
    public JSONSchema setRef(String ref)
    {
        this.json.setStringOrNull(JSONSchema.refPropertyName, ref);

        return this;
    }

    /**
     * Remove the "$ref" property from this JSONSchema object.
     * @return The removed "$ref" property value.
     */
    public String removeRef()
    {
        final String result = this.getRef();

        this.json.remove(JSONSchema.refPropertyName)
            .catchError()
            .await();

        return result;
    }

    /**
     * Get the value of the "additionalProperties" property.
     * @return The value of the "additionalProperties" property.
     */
    public Object getAdditionalProperties()
    {
        return this.json.get(JSONSchema.additionalPropertiesPropertyName)
            .then((JSONSegment additionalPropertiesJson) ->
            {
                Object additionalProperties;
                if (additionalPropertiesJson instanceof JSONBoolean)
                {
                    additionalProperties = ((JSONBoolean)additionalPropertiesJson).getValue();
                }
                else if (additionalPropertiesJson instanceof JSONObject)
                {
                    additionalProperties = JSONSchema.create((JSONObject)additionalPropertiesJson);
                }
                else
                {
                    additionalProperties = null;
                }
                return additionalProperties;
            })
            .catchError(NotFoundException.class)
            .await();
    }

    /**
     * Get the value of the "additionalProperties" property as a boolean.
     * @return The value of the "additionalProperties" property as a boolean.
     */
    public Boolean getAdditionalPropertiesAsBoolean()
    {
        return Types.as(this.getAdditionalProperties(), Boolean.class);
    }

    /**
     * Get the value of the "additionalProperties" property as a JSONSchema.
     * @return The value of the "additionalProperties" property as a JSONSchema.
     */
    public JSONSchema getAdditionalPropertiesAsJSONSchema()
    {
        return Types.as(this.getAdditionalProperties(), JSONSchema.class);
    }

    /**
     * Set the value of the "additionalProperties" property.
     * @param additionalProperties The value of the "additionalProperties" property.
     * @return This object for method chaining.
     */
    public JSONSchema setAdditionalProperties(Boolean additionalProperties)
    {
        this.json.setBooleanOrNull(JSONSchema.additionalPropertiesPropertyName, additionalProperties);

        return this;
    }

    /**
     * Set the value of the "additionalProperties" property.
     * @param additionalProperties The value of the "additionalProperties" property.
     * @return This object for method chaining.
     */
    public JSONSchema setAdditionalProperties(JSONSchema additionalProperties)
    {
        this.json.setObjectOrNull(JSONSchema.additionalPropertiesPropertyName, additionalProperties == null ? null : additionalProperties.toJson());

        return this;
    }

    /**
     * Remove the "additionalProperties" property from this JSONSchema object.
     * @return The removed "additionalProperties" property value.
     */
    public Object removeAdditionalProperties()
    {
        final Object result = this.getAdditionalProperties();

        this.json.remove(JSONSchema.additionalPropertiesPropertyName)
            .catchError()
            .await();

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
        MutableMap<String,JSONSchema> result = null;

        final JSONObject definitionsJson = this.json.getObject(JSONSchema.definitionsPropertyName)
            .catchError()
            .await();
        if (definitionsJson != null)
        {
            result = Map.create();
            for (final String definitionName : definitionsJson.getPropertyNames())
            {
                final JSONObject definitionJson = definitionsJson.getObject(definitionName)
                    .catchError()
                    .await();
                if (definitionJson != null)
                {
                    result.set(definitionName, JSONSchema.create(definitionJson));
                }
            }
        }

        return result;
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
            this.json.setNull(JSONSchema.definitionsPropertyName);
        }
        else
        {
            final JSONObject definitionsJson = JSONObject.create();
            for (final MapEntry<String,JSONSchema> definition : definitions)
            {
                final JSONSchema definitionValue = definition.getValue();
                definitionsJson.setObjectOrNull(definition.getKey(), definitionValue == null ? null : definitionValue.toJson());
            }
            this.json.setObject(JSONSchema.definitionsPropertyName, definitionsJson);
        }
        return this;
    }

    /**
     * Get the names of the definitions that this JSONSchema has defined.
     * @return The names of the definitions that this JSONSchema has defined;
     */
    public Iterable<String> getDefinitionNames()
    {
        final JSONObject definitionsJson = this.json.getObject(JSONSchema.definitionsPropertyName)
            .catchError()
            .await();
        return definitionsJson == null ? Iterable.create() : definitionsJson.getPropertyNames();
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
            JSONSchema result = null;

            final JSONObject definitionsJson = this.json.getObject(JSONSchema.definitionsPropertyName)
                .catchError()
                .await();
            if (definitionsJson != null)
            {
                final JSONObject definitionJson = definitionsJson.getObject(definitionName)
                    .catchError()
                    .await();
                if (definitionJson != null)
                {
                    result = JSONSchema.create(definitionJson);
                }
            }

            if (result == null)
            {
                throw new NotFoundException("No JSON Schema found for the definition " + Strings.escapeAndQuote(definitionName) + ".");
            }

            PostCondition.assertNotNull(result, "result");

            return result;
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

        final JSONObject definitionsJson = this.json.getOrCreateObject(JSONSchema.definitionsPropertyName)
            .catchError()
            .await();
        definitionsJson.setObject(definitionName, definitionSchema.toJson());

        return this;
    }

    /**
     * Get the description of this JSONSchema.
     * @return The description of this JSONSchema.
     */
    public String getDescription()
    {
        return this.json.getStringOrNull(JSONSchema.descriptionPropertyName)
            .catchError()
            .await();
    }

    /**
     * Set the description of this JSONSchema.
     * @param description The description of this JSONSchema.
     * @return This object for method chaining.
     */
    public JSONSchema setDescription(String description)
    {
        this.json.setStringOrNull(JSONSchema.descriptionPropertyName, description);

        return this;
    }

    /**
     * Remove and return the description of this JSONSchema.
     * @return The removed description.
     */
    public String removeDescription()
    {
        final String result = this.getDescription();

        this.json.remove(JSONSchema.descriptionPropertyName)
            .catchError()
            .await();

        return result;
    }

    /**
     * Get the allowed values for this JSONSchema.
     * @return The allowed values for this JSONSchema.
     */
    public Iterable<Object> getEnum()
    {
        return this.json.getArray(JSONSchema.enumPropertyName)
            .then((JSONArray enumArray) ->
            {
                final List<Object> enums = List.create();
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
                        enums.add(((JSONNumber)enumElement).getNumberValue());
                    }
                    else if (enumElement instanceof JSONBoolean)
                    {
                        enums.add(((JSONBoolean)enumElement).getValue());
                    }
                }
                return enums;
            })
            .catchError()
            .await();
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
            this.json.setNull(JSONSchema.enumPropertyName);
        }
        else
        {
            this.json.setArray(JSONSchema.enumPropertyName,
                JSONArray.create(enums.map(JSONSchema::enumValueToJSON).toList()));
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

        this.json.getOrCreateArray(JSONSchema.enumPropertyName).await()
            .add(JSONSchema.enumValueToJSON(enumValue));

        return this;
    }

    private static Object simplifyEnumValue(Object enumValue)
    {
        Object simplifiedEnumValue = enumValue;
        if (simplifiedEnumValue instanceof Float)
        {
            simplifiedEnumValue = ((Float)simplifiedEnumValue).doubleValue();
        }
        else if (simplifiedEnumValue instanceof Integer)
        {
            simplifiedEnumValue = ((Integer)simplifiedEnumValue).longValue();
        }
        else if (simplifiedEnumValue instanceof Short)
        {
            simplifiedEnumValue = ((Short)simplifiedEnumValue).longValue();
        }
        else if (simplifiedEnumValue instanceof Byte)
        {
            simplifiedEnumValue = ((Byte)simplifiedEnumValue).longValue();
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
            result = JSONNumber.create((Double)enumValue);
        }
        else if (enumValue instanceof Float)
        {
            result = JSONNumber.create((Float)enumValue);
        }
        else if (enumValue instanceof Long)
        {
            result = JSONNumber.create((Long)enumValue);
        }
        else if (enumValue instanceof Integer)
        {
            result = JSONNumber.create((Integer)enumValue);
        }
        else if (enumValue instanceof Short)
        {
            result = JSONNumber.create((Short)enumValue);
        }
        else if (enumValue instanceof Byte)
        {
            result = JSONNumber.create((Byte)enumValue);
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

        final Iterable<Object> enumValues = this.getEnum();
        if (!Iterable.isNullOrEmpty(enumValues))
        {
            final Object simplifiedEnumValue = JSONSchema.simplifyEnumValue(enumValue);
            int index = 0;
            boolean found = false;
            for (final Object existingEnumValue : enumValues)
            {
                if (Comparer.equal(simplifiedEnumValue, existingEnumValue))
                {
                    found = true;
                    break;
                }
                else
                {
                    ++index;
                }
            }

            if (found)
            {
                this.json.getArray(JSONSchema.enumPropertyName).await().removeAt(index);
                result = simplifiedEnumValue;
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
        final Iterable<Object> result = this.getEnum();

        this.json.remove(JSONSchema.enumPropertyName)
            .catchError()
            .await();

        return result;
    }

    /**
     * Get the JSONSchema that each item in a matching array must adhere to.
     * @return The JSONSchema that each item in a matching array must adhere to.
     */
    public JSONSchema getItems()
    {
        return this.json.getObject(JSONSchema.itemsPropertyName)
            .then((JSONObject itemsJson) -> { return JSONSchema.create(itemsJson); })
            .catchError()
            .await();
    }

    /**
     * Set the JSONSchema that each item in a matching array must adhere to.
     * @param items The JSONSchema that each item in a matching array must adhere to.
     * @return This object for method chaining.
     */
    public JSONSchema setItems(JSONSchema items)
    {
        this.json.setObjectOrNull(JSONSchema.itemsPropertyName, items == null ? null : items.toJson());

        return this;
    }

    /**
     * Remove the JSONSchema that each item in a matching array must adhere to.
     * @return The removed JSONSchema that each item in a matching array must adhere to.
     */
    public JSONSchema removeItems()
    {
        final JSONSchema result = this.getItems();

        this.json.remove(JSONSchema.itemsPropertyName).catchError().await();

        return result;
    }

    /**
     * Get the names of the properties that are required in this JSONSchema.
     * @return The names of the properties that are required in this JSONSchema.
     */
    public Iterable<String> getRequired()
    {
        return this.json.getArray(JSONSchema.requiredPropertyName)
            .then((JSONArray requiredJson) ->
            {
                return requiredJson.instanceOf(JSONString.class).map(JSONString::getValue);
            })
            .catchError()
            .await();

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
            this.json.setNull(JSONSchema.requiredPropertyName);
        }
        else
        {
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
        final Iterable<String> result = this.getRequired();

        this.json.remove(JSONSchema.requiredPropertyName).catchError().await();

        return result;
    }

    /**
     * Get the minimum length that a matching String must be.
     * @return The minimum length that a matching String must be.
     */
    public Integer getMinLength()
    {
        return this.json.getIntegerOrNull(JSONSchema.minLengthPropertyName)
            .catchError()
            .await();
    }

    /**
     * Set the minimum length that a matching String must be.
     * @param minLength The minimum length that a matching String must be.
     * @return This object for method chaining.
     */
    public JSONSchema setMinLength(int minLength)
    {
        PreCondition.assertGreaterThanOrEqualTo(minLength, 0, "minLength");

        this.json.setNumber(JSONSchema.minLengthPropertyName, minLength);

        return this;
    }

    /**
     * Remove the minimum length that a matching String must be.
     * @return The removed minimum length that a matching String must be.
     */
    public Integer removeMinLength()
    {
        final Integer result = this.getMinLength();

        this.json.remove(JSONSchema.minLengthPropertyName).catchError().await();

        return result;
    }

    /**
     * Get the collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     * @return The collection of JSONSchemas that a matching JSONSegment must match exactly one of.
     */
    public Iterable<JSONSchema> getOneOf()
    {
        return this.json.getArray(JSONSchema.oneOfPropertyName)
            .then((JSONArray oneOfJson) ->
            {
                return oneOfJson.instanceOf(JSONObject.class).map(JSONSchema::create);
            })
            .catchError()
            .await();
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
            this.json.setNull(JSONSchema.oneOfPropertyName);
        }
        else
        {
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
        final Iterable<JSONSchema> result = this.getOneOf();

        this.json.remove(JSONSchema.oneOfPropertyName).catchError().await();

        return result;
    }
}
