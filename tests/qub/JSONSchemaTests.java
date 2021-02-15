package qub;

public interface JSONSchemaTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(JSONSchema.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final JSONSchema schema = JSONSchema.create();
                test.assertNotNull(schema);
                test.assertNull(schema.getSchema());
                test.assertNull(schema.getType());
                test.assertNull(schema.getProperties());
            });

            runner.testGroup("create(JSONObject)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.create((JSONObject)null),
                        new PreConditionFailure("json cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONObject json = JSONObject.create();
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());

                    schema.setSchema("hello there");
                    test.assertEqual("hello there", schema.getSchema());
                    test.assertEqual("hello there", json.getString("$schema").await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setString("$schema", "hello there")
                        .setString("type", "array");
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertEqual("hello there", schema.getSchema());
                    test.assertEqual("array", schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with properties", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setObject("properties", JSONObject.create()
                            .setObject("a", JSONObject.create().setString("type", "string"))
                            .setObject("b", JSONObject.create().setString("type", "integer")));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNotNull(schema.getProperties());
                    test.assertEqual(
                        JSONSchema.create().setType(JSONSchemaType.String),
                        schema.getProperty("a").await());
                    test.assertEqual(
                        JSONSchema.create().setType(JSONSchemaType.Integer),
                        schema.getProperty("b").await());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with $ref", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setString("$ref", "hello");
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertEqual("hello", schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with boolean additionalProperties", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setBoolean("additionalProperties", false);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertEqual(false, schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with definitions", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setObject("definitions", JSONObject.create()
                            .setObject("a", JSONObject.create().setString("type", "string"))
                            .setObject("b", JSONObject.create().setString("type", "integer")));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNotNull(schema.getDefinitions());
                    test.assertEqual(
                        JSONSchema.create().setType(JSONSchemaType.String),
                        schema.getDefinition("a").await());
                    test.assertEqual(
                        JSONSchema.create().setType(JSONSchemaType.Integer),
                        schema.getDefinition("b").await());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with description", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setString("description", "hello");
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertEqual("hello", schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with empty enum", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArray("enum", JSONArray.create());
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertEqual(Set.create(), schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with null enum", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArray("enum", JSONArray.create(JSONNull.segment));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertEqual(Set.create((Object)null), schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with string enum", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArray("enum", JSONArray.create(JSONString.get("hello")));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertEqual(Set.create("hello"), schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with number enum", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArray("enum", JSONArray.create(JSONNumber.create(123)));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertEqual(Iterable.create(123L), schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with boolean enum", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArray("enum", JSONArray.create(JSONBoolean.get(true)));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertEqual(Set.create(true), schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with null items", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setObjectOrNull("items", null);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with empty items", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setObjectOrNull("items", JSONObject.create());
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertEqual(JSONSchema.create(), schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with null required", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArrayOrNull("required", null);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with empty required", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArrayOrNull("required", JSONArray.create());
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertEqual(Iterable.create(), schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with non-empty required", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArrayOrNull("required", JSONArray.create(JSONString.get("a")));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertEqual(Iterable.create("a"), schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with null minLength", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setNumberOrNull("minLength", (Long)null);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with negative minLength", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setNumberOrNull("minLength", -1);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertEqual(-1, schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with zero minLength", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setNumberOrNull("minLength", 0);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertEqual(0, schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with positive minLength", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setNumberOrNull("minLength", 10);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertEqual(10, schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with null oneOf", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArrayOrNull("oneOf", null);
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertNull(schema.getOneOf());
                });

                runner.test("with empty oneOf", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArrayOrNull("oneOf", JSONArray.create());
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertEqual(Iterable.create(), schema.getOneOf());
                });

                runner.test("with non-empty oneOf", (Test test) ->
                {
                    final JSONObject json = JSONObject.create()
                        .setArrayOrNull("oneOf", JSONArray.create(JSONObject.create()));
                    final JSONSchema schema = JSONSchema.create(json);
                    test.assertNotNull(schema);
                    test.assertSame(json, schema.toJson());
                    test.assertNull(schema.getSchema());
                    test.assertNull(schema.getType());
                    test.assertNull(schema.getProperties());
                    test.assertNull(schema.getRef());
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getDefinitions());
                    test.assertNull(schema.getDescription());
                    test.assertNull(schema.getEnum());
                    test.assertNull(schema.getItems());
                    test.assertNull(schema.getRequired());
                    test.assertNull(schema.getMinLength());
                    test.assertEqual(Iterable.create(JSONSchema.create()), schema.getOneOf());
                });
            });

            runner.testGroup("parse(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse((File)null),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.getFile("/schema.json").await();
                    test.assertThrows(() -> JSONSchema.parse(file).await(),
                        new FileNotFoundException("/schema.json"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.createFile("/schema.json").await();
                    test.assertThrows(() -> JSONSchema.parse(file).await(),
                        new ParseException("Missing object left curly bracket ('{')."));
                });

                runner.test("with existing non-json file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.createFile("/schema.json").await();
                    file.setContentsAsString("hello there!").await();
                    test.assertThrows(() -> JSONSchema.parse(file).await(),
                        new ParseException("Unrecognized JSONToken literal: hello"));
                });

                runner.test("with existing empty JSON object file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.createFile("/schema.json").await();
                    file.setContentsAsString("{}").await();
                    test.assertEqual(JSONSchema.create(), JSONSchema.parse(file).await());
                });

                runner.test("with existing non-empty JSON object file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.createFile("/schema.json").await();
                    file.setContentsAsString("{\"$schema\":\"hello there\"}").await();
                    test.assertEqual(
                        JSONSchema.create()
                            .setSchema("hello there"),
                        JSONSchema.parse(file).await());
                });
            });

            runner.testGroup("parse(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse((CharacterReadStream)null),
                        new PreConditionFailure("readStream cannot be null."));
                });

                runner.test("with disposed stream", (Test test) ->
                {
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create();
                    stream.dispose().await();
                    test.assertThrows(() -> JSONSchema.parse(stream).await(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("with empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create().endOfStream();
                    test.assertThrows(() -> JSONSchema.parse(stream).await(),
                        new ParseException("Missing object left curly bracket ('{')."));
                });

                runner.test("with non-JSON stream", (Test test) ->
                {
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create("a b c d").endOfStream();
                    test.assertThrows(() -> JSONSchema.parse(stream).await(),
                        new ParseException("Unrecognized JSONToken literal: a"));
                });

                runner.test("with existing empty JSON object stream", (Test test) ->
                {
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create("{}").endOfStream();
                    test.assertEqual(JSONSchema.create(), JSONSchema.parse(stream).await());
                });

                runner.test("with existing non-empty JSON object file", (Test test) ->
                {
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create("{\"$schema\":\"hello there\"}").endOfStream();
                    test.assertEqual(
                        JSONSchema.create()
                            .setSchema("hello there"),
                        JSONSchema.parse(stream).await());
                });
            });

            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse((String)null),
                        new PreConditionFailure("text cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse("").await(),
                        new ParseException("Missing object left curly bracket ('{')."));
                });

                runner.test("with non-JSON stream", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse("a b c d").await(),
                        new ParseException("Unrecognized JSONToken literal: a"));
                });

                runner.test("with existing empty JSON object stream", (Test test) ->
                {
                    test.assertEqual(JSONSchema.create(), JSONSchema.parse("{}").await());
                });

                runner.test("with existing non-empty JSON object file", (Test test) ->
                {
                    test.assertEqual(
                        JSONSchema.create()
                            .setSchema("hello there"),
                        JSONSchema.parse("{\"$schema\":\"hello there\"}").await());
                });

                runner.test("with null properties object", (Test test) ->
                {
                    test.assertEqual(
                        JSONSchema.create()
                            .setProperties(null),
                        JSONSchema.parse("{\"properties\":null}").await());
                });

                runner.test("with empty properties object", (Test test) ->
                {
                    test.assertEqual(
                        JSONSchema.create()
                            .setProperties(Map.create()),
                        JSONSchema.parse("{\"properties\":{}}").await());
                });

                runner.test("with empty property schema name", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse("{\"properties\":{\"\":{}}}").await(),
                        new ParseException("Expected object property name to be not empty."));
                });

                runner.test("with non-object property schema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.parse("{\"properties\":{\"a\":false}}").await();
                    test.assertThrows(() -> schema.getProperty("a").await(),
                        new NotFoundException("No JSON Schema found for the property \"a\"."));
                    test.assertEqual(
                        JSONObject.create()
                            .set("properties", JSONObject.create()
                                .setBoolean("a", false)
                            ),
                        schema.toJson());
                });

                runner.test("with two property schemas", (Test test) ->
                {
                    test.assertEqual(
                        JSONSchema.create()
                            .addProperty("a", JSONSchema.create().setSchema("b"))
                            .addProperty("c", JSONSchema.create().setType("d")),
                        JSONSchema.parse("{\"properties\":{\"a\":{\"$schema\":\"b\"},\"c\":{\"type\":\"d\"}}}").await());
                });
            });

            runner.testGroup("parse(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse(Iterator.create()).await(),
                        new ParseException("Missing object left curly bracket ('{')."));
                });

                runner.test("with non-JSON stream", (Test test) ->
                {
                    test.assertThrows(() -> JSONSchema.parse(Strings.iterate("a b c d")).await(),
                        new ParseException("Unrecognized JSONToken literal: a"));
                });

                runner.test("with existing empty JSON object stream", (Test test) ->
                {
                    test.assertEqual(JSONSchema.create(), JSONSchema.parse(Strings.iterate("{}")).await());
                });

                runner.test("with existing non-empty JSON object file", (Test test) ->
                {
                    test.assertEqual(
                        JSONSchema.create()
                            .setSchema("hello there"),
                        JSONSchema.parse(Strings.iterate("{\"$schema\":\"hello there\"}")).await());
                });
            });

            runner.testGroup("setSchema(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setSchemaResult = schema.setSchema(null);
                    test.assertSame(schema, setSchemaResult);
                    test.assertNull(schema.getSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("$schema"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setSchemaResult = schema.setSchema("");
                    test.assertSame(schema, setSchemaResult);
                    test.assertEqual("", schema.getSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("$schema", ""),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setSchemaResult = schema.setSchema("abcd");
                    test.assertSame(schema, setSchemaResult);
                    test.assertEqual("abcd", schema.getSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("$schema", "abcd"),
                        schema.toJson());
                });
            });

            runner.testGroup("removeSchema()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getSchema());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeSchema());

                    test.assertNull(schema.getSchema());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("$schema"));
                    test.assertNull(schema.getSchema());
                    test.assertEqual(JSONObject.create().setNull("$schema"), schema.toJson());

                    test.assertNull(schema.removeSchema());

                    test.assertNull(schema.getSchema());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is not-null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setString("$schema", "hello"));
                    test.assertEqual("hello", schema.getSchema());
                    test.assertEqual(JSONObject.create().setString("$schema", "hello"), schema.toJson());

                    test.assertEqual("hello", schema.removeSchema());

                    test.assertNull(schema.getSchema());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("setType(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setTypeResult = schema.setType((String)null);
                    test.assertSame(schema, setTypeResult);
                    test.assertNull(schema.getType());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("type"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setTypeResult = schema.setType("");
                    test.assertSame(schema, setTypeResult);
                    test.assertEqual("", schema.getType());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("type", ""),
                        schema.toJson());
                });

                runner.test("with non-empty unrecognized", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setTypeResult = schema.setType("abcd");
                    test.assertSame(schema, setTypeResult);
                    test.assertEqual("abcd", schema.getType());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("type", "abcd"),
                        schema.toJson());
                });

                runner.test("with non-empty recognized", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setTypeResult = schema.setType("integer");
                    test.assertSame(schema, setTypeResult);
                    test.assertEqual("integer", schema.getType());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("type", "integer"),
                        schema.toJson());
                });
            });

            runner.testGroup("setType(JSONSchemaType)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setTypeResult = schema.setType((JSONSchemaType)null);
                    test.assertSame(schema, setTypeResult);
                    test.assertNull(schema.getType());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("type"),
                        schema.toJson());
                });

                runner.test("with non-null recognized", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setTypeResult = schema.setType(JSONSchemaType.Boolean);
                    test.assertSame(schema, setTypeResult);
                    test.assertEqual("boolean", schema.getType());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("type", "boolean"),
                        schema.toJson());
                });
            });

            runner.testGroup("removeType()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getType());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeType());

                    test.assertNull(schema.getType());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("type"));
                    test.assertNull(schema.getType());
                    test.assertEqual(JSONObject.create().setNull("type"), schema.toJson());

                    test.assertNull(schema.removeType());

                    test.assertNull(schema.getType());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is not-null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setString("type", "hello"));
                    test.assertEqual("hello", schema.getType());
                    test.assertEqual(JSONObject.create().setString("type", "hello"), schema.toJson());

                    test.assertEqual("hello", schema.removeType());

                    test.assertNull(schema.getType());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("setProperties(Map<String,JSONSchema)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    schema.setProperties(null);

                    test.assertNull(schema.getProperties());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("properties"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final MutableMap<String,JSONSchema> propertySchemas = Map.create();
                    schema.setProperties(propertySchemas);

                    test.assertEqual(propertySchemas, schema.getProperties());
                    test.assertNotSame(propertySchemas, schema.getProperties());
                    test.assertEqual(
                        JSONObject.create()
                            .setObject("properties", JSONObject.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final MutableMap<String,JSONSchema> propertySchemas = Map.create();
                    propertySchemas.set("a", JSONSchema.create());

                    schema.setProperties(propertySchemas);

                    test.assertEqual(propertySchemas, schema.getProperties());
                    test.assertNotSame(propertySchemas, schema.getProperties());
                    test.assertEqual(
                        JSONObject.create()
                            .setObject("properties", JSONObject.create()
                                .setObject("a", JSONObject.create())),
                        schema.toJson());
                });
            });

            runner.testGroup("getPropertyNames()", () ->
            {
                runner.test("with no properties set", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertEqual(Iterable.create(), schema.getPropertyNames());
                });

                runner.test("with empty properties set", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create()
                        .setProperties(Map.create());
                    test.assertEqual(Iterable.create(), schema.getPropertyNames());
                });

                runner.test("with properties set", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create()
                        .addProperty("a", JSONSchema.create().setType(JSONSchemaType.Array))
                        .addProperty("b", JSONSchema.create().setType(JSONSchemaType.Integer));
                    test.assertEqual(Iterable.create("a", "b"), schema.getPropertyNames());
                });
            });

            runner.testGroup("getProperty(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.getProperty(null),
                        new PreConditionFailure("propertyName cannot be null."));
                    test.assertNull(schema.getProperties());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.getProperty(""),
                        new PreConditionFailure("propertyName cannot be empty."));
                    test.assertNull(schema.getProperties());
                });

                runner.test("with not found property name", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.getProperty("abc").await(),
                        new NotFoundException("No JSON Schema found for the property \"abc\"."));
                    test.assertNull(schema.getProperties());
                });
            });

            runner.testGroup("setProperty(String,JSONSchema)", () ->
            {
                runner.test("with null property name", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String propertyName = null;
                    final JSONSchema propertySchema = JSONSchema.create();
                    test.assertThrows(() -> schema.addProperty(propertyName, propertySchema),
                        new PreConditionFailure("propertyName cannot be null."));
                    test.assertNull(schema.getProperties());
                });

                runner.test("with empty property name", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String propertyName = "";
                    final JSONSchema propertySchema = JSONSchema.create();
                    test.assertThrows(() -> schema.addProperty(propertyName, propertySchema),
                        new PreConditionFailure("propertyName cannot be empty."));
                    test.assertNull(schema.getProperties());
                });

                runner.test("with null property schema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String propertyName = "hello";
                    final JSONSchema propertySchema = null;
                    test.assertThrows(() -> schema.addProperty(propertyName, propertySchema),
                        new PreConditionFailure("propertySchema cannot be null."));
                    test.assertNull(schema.getProperties());
                });

                runner.test("with empty property schema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String propertyName = "hello";
                    final JSONSchema propertySchema = JSONSchema.create();
                    final JSONSchema setPropertyResult = schema.addProperty(propertyName, propertySchema);
                    test.assertSame(setPropertyResult, schema);
                    test.assertEqual(propertySchema, schema.getProperty(propertyName).await());
                });
            });

            runner.testGroup("setRef(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRefResult = schema.setRef(null);
                    test.assertSame(schema, setRefResult);
                    test.assertNull(schema.getRef());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("$ref"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRefResult = schema.setRef("");
                    test.assertSame(schema, setRefResult);
                    test.assertEqual("", schema.getRef());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("$ref", ""),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRefResult = schema.setRef("abcd");
                    test.assertSame(schema, setRefResult);
                    test.assertEqual("abcd", schema.getRef());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("$ref", "abcd"),
                        schema.toJson());
                });
            });

            runner.testGroup("removeRef()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeRef());

                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("$ref"));
                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create().setNull("$ref"), schema.toJson());

                    test.assertNull(schema.removeRef());

                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is not-null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setString("$ref", "hello"));
                    test.assertEqual("hello", schema.getRef());
                    test.assertEqual(JSONObject.create().setString("$ref", "hello"), schema.toJson());

                    test.assertEqual("hello", schema.removeRef());

                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("setAdditionalProperties(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setAdditionalPropertiesResult = schema.setAdditionalProperties((Boolean)null);
                    test.assertSame(schema, setAdditionalPropertiesResult);
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertNull(schema.getAdditionalPropertiesAsBoolean());
                    test.assertNull(schema.getAdditionalPropertiesAsJSONSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("additionalProperties"),
                        schema.toJson());
                });

                runner.test("with false", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setAdditionalPropertiesResult = schema.setAdditionalProperties(false);
                    test.assertSame(schema, setAdditionalPropertiesResult);
                    test.assertEqual(false, schema.getAdditionalProperties());
                    test.assertEqual(false, schema.getAdditionalPropertiesAsBoolean());
                    test.assertEqual(null, schema.getAdditionalPropertiesAsJSONSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setBoolean("additionalProperties", false),
                        schema.toJson());
                });

                runner.test("with true", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setAdditionalPropertiesResult = schema.setAdditionalProperties(true);
                    test.assertSame(schema, setAdditionalPropertiesResult);
                    test.assertEqual(true, schema.getAdditionalProperties());
                    test.assertEqual(true, schema.getAdditionalPropertiesAsBoolean());
                    test.assertEqual(null, schema.getAdditionalPropertiesAsJSONSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setBoolean("additionalProperties", true),
                        schema.toJson());
                });
            });

            runner.testGroup("setAdditionalProperties(JSONSchema)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setAdditionalPropertiesResult = schema.setAdditionalProperties((JSONSchema)null);
                    test.assertSame(schema, setAdditionalPropertiesResult);
                    test.assertSame(null, schema.getAdditionalProperties());
                    test.assertSame(null, schema.getAdditionalPropertiesAsBoolean());
                    test.assertSame(null, schema.getAdditionalPropertiesAsJSONSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("additionalProperties"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema additionalPropertiesSchema = JSONSchema.create();
                    final JSONSchema setAdditionalPropertiesResult = schema.setAdditionalProperties(additionalPropertiesSchema);
                    test.assertSame(schema, setAdditionalPropertiesResult);
                    test.assertEqual(additionalPropertiesSchema, schema.getAdditionalProperties());
                    test.assertSame(null, schema.getAdditionalPropertiesAsBoolean());
                    test.assertEqual(additionalPropertiesSchema, schema.getAdditionalPropertiesAsJSONSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setObject("additionalProperties", JSONObject.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema additionalPropertiesSchema = JSONSchema.create().setType(JSONSchemaType.String);
                    final JSONSchema setAdditionalPropertiesResult = schema.setAdditionalProperties(additionalPropertiesSchema);
                    test.assertSame(schema, setAdditionalPropertiesResult);
                    test.assertEqual(additionalPropertiesSchema, schema.getAdditionalProperties());
                    test.assertSame(null, schema.getAdditionalPropertiesAsBoolean());
                    test.assertEqual(additionalPropertiesSchema, schema.getAdditionalPropertiesAsJSONSchema());
                    test.assertEqual(
                        JSONObject.create()
                            .setObject("additionalProperties", JSONObject.create()
                                .setString("type", "string")),
                        schema.toJson());
                });
            });

            runner.testGroup("removeAdditionalProperties()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeAdditionalProperties());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("additionalProperties"));
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setNull("additionalProperties"), schema.toJson());

                    test.assertNull(schema.removeAdditionalProperties());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is a boolean", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setBoolean("additionalProperties", false));
                    test.assertEqual(false, schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setBoolean("additionalProperties", false), schema.toJson());

                    test.assertEqual(false, schema.removeAdditionalProperties());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is a JSONSchema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setObject("additionalProperties", JSONObject.create()));
                    test.assertEqual(JSONSchema.create(), schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setObject("additionalProperties", JSONObject.create()), schema.toJson());

                    test.assertEqual(JSONSchema.create(), schema.removeAdditionalProperties());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("removeAdditionalPropertiesAsBoolean()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeAdditionalPropertiesAsBoolean());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("additionalProperties"));
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setNull("additionalProperties"), schema.toJson());

                    test.assertNull(schema.removeAdditionalPropertiesAsBoolean());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is a boolean", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setBoolean("additionalProperties", false));
                    test.assertEqual(false, schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setBoolean("additionalProperties", false), schema.toJson());

                    test.assertEqual(false, schema.removeAdditionalPropertiesAsBoolean());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is a JSONSchema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setObject("additionalProperties", JSONObject.create()));
                    test.assertEqual(JSONSchema.create(), schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setObject("additionalProperties", JSONObject.create()), schema.toJson());

                    test.assertEqual(null, schema.removeAdditionalPropertiesAsBoolean());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("removeAdditionalPropertiesAsJSONSchema()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getRef());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeAdditionalPropertiesAsJSONSchema());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("additionalProperties"));
                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setNull("additionalProperties"), schema.toJson());

                    test.assertNull(schema.removeAdditionalPropertiesAsJSONSchema());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is a boolean", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setBoolean("additionalProperties", false));
                    test.assertEqual(false, schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setBoolean("additionalProperties", false), schema.toJson());

                    test.assertEqual(null, schema.removeAdditionalPropertiesAsJSONSchema());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is a JSONSchema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setObject("additionalProperties", JSONObject.create()));
                    test.assertEqual(JSONSchema.create(), schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create().setObject("additionalProperties", JSONObject.create()), schema.toJson());

                    test.assertEqual(JSONSchema.create(), schema.removeAdditionalPropertiesAsJSONSchema());

                    test.assertNull(schema.getAdditionalProperties());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("setDefinitions(Map<String,JSONSchema)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    schema.setDefinitions(null);

                    test.assertNull(schema.getDefinitions());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("definitions"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final MutableMap<String,JSONSchema> definitions = Map.create();
                    schema.setDefinitions(definitions);

                    test.assertEqual(definitions, schema.getDefinitions());
                    test.assertNotSame(definitions, schema.getDefinitions());
                    test.assertEqual(
                        JSONObject.create()
                            .setObject("definitions", JSONObject.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final MutableMap<String,JSONSchema> definitions = Map.create();
                    definitions.set("a", JSONSchema.create());

                    schema.setDefinitions(definitions);

                    test.assertEqual(definitions, schema.getDefinitions());
                    test.assertNotSame(definitions, schema.getDefinitions());
                    test.assertEqual(
                        JSONObject.create()
                            .setObject("definitions", JSONObject.create()
                                .setObject("a", JSONObject.create())),
                        schema.toJson());
                });
            });

            runner.testGroup("getDefinitionNames()", () ->
            {
                runner.test("with no definitions set", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertEqual(Iterable.create(), schema.getDefinitionNames());
                });

                runner.test("with empty definitions set", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create()
                        .setDefinitions(Map.create());
                    test.assertEqual(Iterable.create(), schema.getDefinitionNames());
                });

                runner.test("with definitions set", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create()
                        .addDefinition("a", JSONSchema.create().setType(JSONSchemaType.Array))
                        .addDefinition("b", JSONSchema.create().setType(JSONSchemaType.Integer));
                    test.assertEqual(Iterable.create("a", "b"), schema.getDefinitionNames());
                });
            });

            runner.testGroup("getDefinition(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.getDefinition(null),
                        new PreConditionFailure("definitionName cannot be null."));
                    test.assertNull(schema.getDefinitions());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.getDefinition(""),
                        new PreConditionFailure("definitionName cannot be empty."));
                    test.assertNull(schema.getDefinitions());
                });

                runner.test("with not found definition name", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.getDefinition("abc").await(),
                        new NotFoundException("No JSON Schema found for the definition \"abc\"."));
                    test.assertNull(schema.getDefinitions());
                });
            });

            runner.testGroup("setDefinition(String,JSONSchema)", () ->
            {
                runner.test("with null definition name", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String definitionName = null;
                    final JSONSchema definitionSchema = JSONSchema.create();
                    test.assertThrows(() -> schema.addDefinition(definitionName, definitionSchema),
                        new PreConditionFailure("definitionName cannot be null."));
                    test.assertNull(schema.getDefinitions());
                });

                runner.test("with empty definition name", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String definitionName = "";
                    final JSONSchema definitionSchema = JSONSchema.create();
                    test.assertThrows(() -> schema.addDefinition(definitionName, definitionSchema),
                        new PreConditionFailure("definitionName cannot be empty."));
                    test.assertNull(schema.getDefinitions());
                });

                runner.test("with null definition schema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String definitionName = "hello";
                    final JSONSchema definitionSchema = null;
                    test.assertThrows(() -> schema.addDefinition(definitionName, definitionSchema),
                        new PreConditionFailure("definitionSchema cannot be null."));
                    test.assertNull(schema.getDefinitions());
                });

                runner.test("with empty definition schema", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();

                    final String definitionName = "hello";
                    final JSONSchema definitionSchema = JSONSchema.create();
                    final JSONSchema setDefinitionResult = schema.addDefinition(definitionName, definitionSchema);
                    test.assertSame(setDefinitionResult, schema);
                    test.assertEqual(definitionSchema, schema.getDefinition(definitionName).await());
                });
            });

            runner.testGroup("setDescription(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setDescriptionResult = schema.setDescription(null);
                    test.assertSame(schema, setDescriptionResult);
                    test.assertNull(schema.getDescription());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("description"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setDescriptionResult = schema.setDescription("");
                    test.assertSame(schema, setDescriptionResult);
                    test.assertEqual("", schema.getDescription());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("description", ""),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setDescriptionResult = schema.setDescription("abcd");
                    test.assertSame(schema, setDescriptionResult);
                    test.assertEqual("abcd", schema.getDescription());
                    test.assertEqual(
                        JSONObject.create()
                            .setString("description", "abcd"),
                        schema.toJson());
                });
            });

            runner.testGroup("removeDescription()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.getDescription());
                    test.assertEqual(JSONObject.create(), schema.toJson());

                    test.assertNull(schema.removeDescription());

                    test.assertNull(schema.getDescription());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setNull("description"));
                    test.assertNull(schema.getDescription());
                    test.assertEqual(JSONObject.create().setNull("description"), schema.toJson());

                    test.assertNull(schema.removeDescription());

                    test.assertNull(schema.getDescription());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });

                runner.test("when it is not-null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create(JSONObject.create().setString("description", "hello"));
                    test.assertEqual("hello", schema.getDescription());
                    test.assertEqual(JSONObject.create().setString("description", "hello"), schema.toJson());

                    test.assertEqual("hello", schema.removeDescription());

                    test.assertNull(schema.getDescription());
                    test.assertEqual(JSONObject.create(), schema.toJson());
                });
            });

            runner.testGroup("setEnum(Object...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((Object[])null);
                    test.assertSame(schema, setEnumResult);

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("enum"),
                        schema.toJson());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum();
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()),
                        schema.toJson());
                });

                runner.test("with null value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((Object)null);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((Object)null), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNull.segment)),
                        schema.toJson());
                });

                runner.test("with string value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum("hello");
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create("hello"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("hello"))),
                        schema.toJson());
                });

                runner.test("with character value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum('m');
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create("m"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("m"))),
                        schema.toJson());
                });

                runner.test("with boolean value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(false);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(false), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONBoolean.get(false))),
                        schema.toJson());
                });

                runner.test("with byte value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((byte)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with short value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((short)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with integer value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with long value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(15L);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with float value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((float)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15.0), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15.0))),
                        schema.toJson());
                });

                runner.test("with double value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((double)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((double)15), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15.0))),
                        schema.toJson());
                });

                runner.test("with boolean, null, and string values", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(false, null, "abc");
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(false, null, "abc"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONBoolean.get(false))
                                .add(JSONNull.segment)
                                .add(JSONString.get("abc"))),
                        schema.toJson());
                });

                runner.test("with invalid type", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.setEnum(test),
                        new PreConditionFailure("enumValue (qub.Test) must be of type null, java.lang.Double, java.lang.Float, java.lang.Long, java.lang.Integer, java.lang.Short, java.lang.Byte, java.lang.Character, java.lang.String, or java.lang.Boolean."));

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("setEnum(Iterable<Object>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum((Iterable<Object>)null);
                    test.assertSame(schema, setEnumResult);

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("enum"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create());
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()),
                        schema.toJson());
                });

                runner.test("with null value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create((Object)null));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((Object)null), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNull.segment)),
                        schema.toJson());
                });

                runner.test("with string value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create("hello"));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create("hello"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("hello"))),
                        schema.toJson());
                });

                runner.test("with character value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create('m'));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create("m"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("m"))),
                        schema.toJson());
                });

                runner.test("with boolean value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create(false));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(false), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONBoolean.get(false))),
                        schema.toJson());
                });

                runner.test("with byte value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create((byte)15));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with short value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create((short)15));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with integer value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create(15));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with long value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create(15L));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with float value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create((float)15));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15.0), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15.0))),
                        schema.toJson());
                });

                runner.test("with double value", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create((double)15));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((double)15), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15.0))),
                        schema.toJson());
                });

                runner.test("with boolean, null, and string values", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.setEnum(Iterable.create(false, null, "abc"));
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(false, null, "abc"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONBoolean.get(false))
                                .add(JSONNull.segment)
                                .add(JSONString.get("abc"))),
                        schema.toJson());
                });

                runner.test("with invalid type", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.setEnum(Iterable.create(test)),
                        new PreConditionFailure("enumValue (qub.Test) must be of type null, java.lang.Double, java.lang.Float, java.lang.Long, java.lang.Integer, java.lang.Short, java.lang.Byte, java.lang.Character, java.lang.String, or java.lang.Boolean."));

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("addEnum(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum(null);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((Object)null), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNull.segment)),
                        schema.toJson());
                });

                runner.test("with string", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum("hello");
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create("hello"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("hello"))),
                        schema.toJson());
                });

                runner.test("with character", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum('m');
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create("m"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("m"))),
                        schema.toJson());
                });

                runner.test("with boolean", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum(false);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(false), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONBoolean.get(false))),
                        schema.toJson());
                });

                runner.test("with byte", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum((byte)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with short", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum((short)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with integer", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum(15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with long", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum(15L);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create(15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with float", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum((float)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((double)15.0), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15.0))),
                        schema.toJson());
                });

                runner.test("with double", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setEnumResult = schema.addEnum((double)15);
                    test.assertSame(schema, setEnumResult);

                    test.assertEqual(Iterable.create((double)15), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15.0))),
                        schema.toJson());
                });

                runner.test("with boolean, null, and string values", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema addEnumResult = schema.addEnum(false).addEnum(null).addEnum("abc");
                    test.assertSame(schema, addEnumResult);

                    test.assertEqual(Iterable.create(false, null, "abc"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONBoolean.get(false))
                                .add(JSONNull.segment)
                                .add(JSONString.get("abc"))),
                        schema.toJson());
                });

                runner.test("with existing number of different type", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    schema.addEnum((byte)15);
                    schema.addEnum((short)15);

                    test.assertEqual(Iterable.create(15L, 15L), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONNumber.create(15))
                                .add(JSONNumber.create(15))),
                        schema.toJson());
                });

                runner.test("with existing string of different type", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    schema.addEnum("a");
                    schema.addEnum('a');

                    test.assertEqual(Iterable.create("a", "a"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("a"))
                                .add(JSONString.get("a"))),
                        schema.toJson());
                });

                runner.test("with existing string", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    schema.addEnum("abc");
                    schema.addEnum("abc");

                    test.assertEqual(Iterable.create("abc", "abc"), schema.getEnum());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("enum", JSONArray.create()
                                .add(JSONString.get("abc"))
                                .add(JSONString.get("abc"))),
                        schema.toJson());
                });

                runner.test("with invalid type", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.addEnum(test),
                        new PreConditionFailure("enumValue (qub.Test) must be of type null, java.lang.Double, java.lang.Float, java.lang.Long, java.lang.Integer, java.lang.Short, java.lang.Byte, java.lang.Character, java.lang.String, or java.lang.Boolean."));

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("removeEnum(Object)", () ->
            {
                final Action4<JSONSchema,Object,Object,JSONObject> removeEnumTest = (JSONSchema schema, Object toRemove, Object expected, JSONObject expectedJson) ->
                {
                    runner.test("with " + English.andList(schema, toRemove), (Test test) ->
                    {
                        final Object removeEnumResult = schema.removeEnum(toRemove);
                        test.assertEqual(expected, removeEnumResult);
                        test.assertEqual(expectedJson, schema.toJson());
                    });
                };

                removeEnumTest.run(
                    JSONSchema.create(),
                    null,
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(null),
                    null,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create(),
                    "a",
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum("a"),
                    "b",
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONString.get("a"))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum("a"),
                    "a",
                    "a",
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create(),
                    'm',
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum('a'),
                    'b',
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONString.get("a"))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum('a'),
                    "a",
                    "a",
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create(),
                    false,
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(true),
                    false,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONBoolean.trueSegment)));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(true),
                    true,
                    true,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create(),
                    (byte)15,
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum((byte)15),
                    (byte)14,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum((byte)15),
                    (byte)15,
                    15L,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum((byte)15),
                    15,
                    15L,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum((byte)15),
                    15L,
                    15L,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum((byte)15),
                    15f,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum((byte)15),
                    15.0,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15))));

                removeEnumTest.run(
                    JSONSchema.create(),
                    15,
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15),
                    14,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15),
                    15,
                    15L,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15),
                    15L,
                    15L,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15),
                    15f,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15),
                    15.0,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15))));

                removeEnumTest.run(
                    JSONSchema.create(),
                    15.0,
                    null,
                    JSONObject.create());

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15.0),
                    14.0,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15.0))));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15.0),
                    15.0,
                    15.0,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15.0),
                    15L,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15.0))));

                removeEnumTest.run( //
                    JSONSchema.create()
                        .addEnum(15.0),
                    15f,
                    15.0,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create()));

                removeEnumTest.run(
                    JSONSchema.create()
                        .addEnum(15.0),
                    15,
                    null,
                    JSONObject.create()
                        .setArray("enum", JSONArray.create(
                            JSONNumber.create(15.0))));

                runner.test("with invalid type", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.removeEnum(test),
                        new PreConditionFailure("enumValue (qub.Test) must be of type null, java.lang.Double, java.lang.Float, java.lang.Long, java.lang.Integer, java.lang.Short, java.lang.Byte, java.lang.Character, java.lang.String, or java.lang.Boolean."));

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("removeEnum()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.removeEnum());

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when it is empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setEnum(Iterable.create());
                    test.assertEqual(Iterable.create(), schema.removeEnum());

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when it is non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setEnum(Iterable.create(1, 2, 3));
                    test.assertEqual(Iterable.create(1L, 2L, 3L), schema.removeEnum());

                    test.assertNull(schema.getEnum());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("setItems(JSONSchema)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setItemsResult = schema.setItems(null);
                    test.assertSame(schema, setItemsResult);

                    test.assertNull(schema.getItems());
                    test.assertEqual(
                        JSONObject.create()
                            .setObjectOrNull("items", null),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setItemsResult = schema.setItems(JSONSchema.create());
                    test.assertSame(schema, setItemsResult);

                    test.assertEqual(JSONSchema.create(), schema.getItems());
                    test.assertEqual(
                        JSONObject.create()
                            .setObjectOrNull("items", JSONObject.create()),
                        schema.toJson());
                });
            });

            runner.testGroup("removeItems()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.removeItems());

                    test.assertNull(schema.getItems());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when it is null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setItems(null);
                    test.assertNull(schema.removeItems());

                    test.assertNull(schema.getItems());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when it is empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setItems(JSONSchema.create());
                    test.assertEqual(JSONSchema.create(), schema.removeItems());

                    test.assertNull(schema.getItems());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("setRequired(String...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRequiredResult = schema.setRequired((String[])null);
                    test.assertSame(schema, setRequiredResult);

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("required"),
                        schema.toJson());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRequiredResult = schema.setRequired();
                    test.assertSame(schema, setRequiredResult);

                    test.assertEqual(Iterable.create(), schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("required", JSONArray.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRequiredResult = schema.setRequired("a", "b");
                    test.assertSame(schema, setRequiredResult);

                    test.assertEqual(Iterable.create("a", "b"), schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("required", JSONArray.create(JSONString.get("a"), JSONString.get("b"))),
                        schema.toJson());
                });
            });

            runner.testGroup("setRequired(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRequiredResult = schema.setRequired((Iterable<String>)null);
                    test.assertSame(schema, setRequiredResult);

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("required"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRequiredResult = schema.setRequired(Iterable.create());
                    test.assertSame(schema, setRequiredResult);

                    test.assertEqual(Iterable.create(), schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("required", JSONArray.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setRequiredResult = schema.setRequired(Iterable.create("a", "b"));
                    test.assertSame(schema, setRequiredResult);

                    test.assertEqual(Iterable.create("a", "b"), schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("required", JSONArray.create(JSONString.get("a"), JSONString.get("b"))),
                        schema.toJson());
                });
            });

            runner.testGroup("addRequired(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.addRequired(null),
                        new PreConditionFailure("requiredPropertyName cannot be null."));

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.addRequired(""),
                        new PreConditionFailure("requiredPropertyName cannot be empty."));

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema addRequiredResult = schema.addRequired("a");
                    test.assertSame(schema, addRequiredResult);

                    test.assertEqual(Iterable.create("a"), schema.getRequired());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("required", JSONArray.create(JSONString.get("a"))),
                        schema.toJson());
                });
            });

            runner.testGroup("removeRequired()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.removeRequired());

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setRequired((Iterable<String>)null);
                    test.assertNull(schema.removeRequired());

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setRequired(Iterable.create());
                    test.assertEqual(Iterable.create(), schema.removeRequired());

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setRequired(Iterable.create("a"));
                    test.assertEqual(Iterable.create("a"), schema.removeRequired());

                    test.assertNull(schema.getRequired());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("setMinLength(long)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.setMinLength(-1),
                        new PreConditionFailure("minLength (-1) must be greater than or equal to 0."));

                    test.assertNull(schema.getMinLength());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with zero", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setMinLengthResult = schema.setMinLength(0);
                    test.assertSame(schema, setMinLengthResult);

                    test.assertEqual(0, schema.getMinLength());
                    test.assertEqual(
                        JSONObject.create()
                            .setNumber("minLength", 0),
                        schema.toJson());
                });

                runner.test("with positive", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setMinLengthResult = schema.setMinLength(1);
                    test.assertSame(schema, setMinLengthResult);

                    test.assertEqual(1, schema.getMinLength());
                    test.assertEqual(
                        JSONObject.create()
                            .setNumber("minLength", 1),
                        schema.toJson());
                });
            });

            runner.testGroup("removeMinLength()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.removeMinLength());

                    test.assertNull(schema.getMinLength());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when it exists", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setMinLength(2);
                    test.assertEqual(2, schema.removeMinLength());

                    test.assertNull(schema.getMinLength());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("setOneOf(JSONSchema...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setOneOfResult = schema.setOneOf((JSONSchema[])null);
                    test.assertSame(schema, setOneOfResult);

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("oneOf"),
                        schema.toJson());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setOneOfResult = schema.setOneOf();
                    test.assertSame(schema, setOneOfResult);

                    test.assertEqual(Iterable.create(), schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("oneOf", JSONArray.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setOneOfResult = schema.setOneOf(JSONSchema.create());
                    test.assertSame(schema, setOneOfResult);

                    test.assertEqual(Iterable.create(JSONSchema.create()), schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("oneOf", JSONArray.create()
                                .add(JSONObject.create())),
                        schema.toJson());
                });
            });

            runner.testGroup("setOneOf(Iterable<JSONSchema>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setOneOfResult = schema.setOneOf((Iterable<JSONSchema>)null);
                    test.assertSame(schema, setOneOfResult);

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setNull("oneOf"),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setOneOfResult = schema.setOneOf(Iterable.create());
                    test.assertSame(schema, setOneOfResult);

                    test.assertEqual(Iterable.create(), schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("oneOf", JSONArray.create()),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema setOneOfResult = schema.setOneOf(Iterable.create(JSONSchema.create()));
                    test.assertSame(schema, setOneOfResult);

                    test.assertEqual(Iterable.create(JSONSchema.create()), schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("oneOf", JSONArray.create()
                                .add(JSONObject.create())),
                        schema.toJson());
                });
            });

            runner.testGroup("addOneOf(JSONSchema)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertThrows(() -> schema.addOneOf(null),
                        new PreConditionFailure("oneOf cannot be null."));

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    final JSONSchema addOneOfResult = schema.addOneOf(JSONSchema.create());
                    test.assertSame(schema, addOneOfResult);

                    test.assertEqual(Iterable.create(JSONSchema.create()), schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create()
                            .setArray("oneOf", JSONArray.create().add(JSONObject.create())),
                        schema.toJson());
                });
            });

            runner.testGroup("removeOneOf()", () ->
            {
                runner.test("when it doesn't exist", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create();
                    test.assertNull(schema.removeOneOf());

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("when null", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setOneOf((Iterable<JSONSchema>)null);
                    test.assertNull(schema.removeOneOf());

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setOneOf(Iterable.create());
                    test.assertEqual(Iterable.create(), schema.removeOneOf());

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONSchema schema = JSONSchema.create().setOneOf(Iterable.create(JSONSchema.create()));
                    test.assertEqual(Iterable.create(JSONSchema.create()), schema.removeOneOf());

                    test.assertNull(schema.getOneOf());
                    test.assertEqual(
                        JSONObject.create(),
                        schema.toJson());
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with no properties", (Test test) ->
                {
                    test.assertEqual("{}", JSONSchema.create().toString());
                });

                runner.test("with schema", (Test test) ->
                {
                    test.assertEqual("{\"$schema\":\"hello\"}", JSONSchema.create().setSchema("hello").toString());
                });

                runner.test("with type", (Test test) ->
                {
                    test.assertEqual("{\"type\":\"hello\"}", JSONSchema.create().setType("hello").toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<JSONSchema,Object,Boolean> equalsTest = (JSONSchema schema, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(schema, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, schema.equals(rhs));
                    });
                };

                equalsTest.run(JSONSchema.create(), null, false);
                equalsTest.run(JSONSchema.create(), "abcd", false);
                equalsTest.run(JSONSchema.create(), JSONSchema.create(), true);
                equalsTest.run(JSONSchema.create().setSchema("a"), JSONSchema.create().setSchema("b"), false);
                equalsTest.run(JSONSchema.create().setSchema("a"), JSONSchema.create().setSchema("a"), true);
                equalsTest.run(JSONSchema.create().setType("a"), JSONSchema.create().setType("b"), false);
                equalsTest.run(JSONSchema.create().setType("a"), JSONSchema.create().setType("a"), true);
            });

            runner.testGroup("equals(JSONSchema)", () ->
            {
                final Action3<JSONSchema,JSONSchema,Boolean> equalsTest = (JSONSchema schema, JSONSchema rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(schema, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, schema.equals(rhs));
                    });
                };

                equalsTest.run(JSONSchema.create(), null, false);
                equalsTest.run(JSONSchema.create(), JSONSchema.create(), true);
                equalsTest.run(JSONSchema.create().setSchema("a"), JSONSchema.create().setSchema("b"), false);
                equalsTest.run(JSONSchema.create().setSchema("a"), JSONSchema.create().setSchema("a"), true);
                equalsTest.run(JSONSchema.create().setType("a"), JSONSchema.create().setType("b"), false);
                equalsTest.run(JSONSchema.create().setType("a"), JSONSchema.create().setType("a"), true);
            });
        });
    }
}
