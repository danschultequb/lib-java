package qub;

public interface JSONTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(JSON.class, () ->
        {
            runner.testGroup("thePropertyNamed(String)", () ->
            {
                final Action2<String,Throwable> thePropertyNamedErrorTest = (String propertyName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.thePropertyNamed(propertyName), expected);
                    });
                };

                thePropertyNamedErrorTest.run(null, new PreConditionFailure("propertyName cannot be null."));
                thePropertyNamedErrorTest.run("", new PreConditionFailure("propertyName cannot be empty."));

                final Action2<String,String> thePropertyNamedTest = (String propertyName, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        test.assertEqual(expected, JSON.thePropertyNamed(propertyName));
                    });
                };

                thePropertyNamedTest.run("blah", "the property named \"blah\"");
            });

            runner.testGroup("getWrongTypeExceptionMessage(JSONSegment,Iterable<Class<T>>,String)", () ->
            {
                final Action4<JSONSegment,Iterable<Class<? extends JSONSegment>>,String,Throwable> getWrongTypeExceptionMessageErrorTest = (JSONSegment value, Iterable<Class<? extends JSONSegment>> expectedTypes, String valueName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value, expectedTypes, Strings.escapeAndQuote(valueName)), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.getWrongTypeExceptionMessage(value, expectedTypes, valueName), expected);
                    });
                };

                getWrongTypeExceptionMessageErrorTest.run(null, Iterable.create(), "a", new PreConditionFailure("value cannot be null."));
                getWrongTypeExceptionMessageErrorTest.run(JSONNull.segment, null, "a", new PreConditionFailure("expectedTypes cannot be null."));
                getWrongTypeExceptionMessageErrorTest.run(JSONNull.segment, Iterable.create(), "a", new PreConditionFailure("expectedTypes cannot be empty."));
                getWrongTypeExceptionMessageErrorTest.run(JSONNull.segment, Iterable.create(JSONString.class), null, new PreConditionFailure("valueName cannot be null."));
                getWrongTypeExceptionMessageErrorTest.run(JSONNull.segment, Iterable.create(JSONString.class), "", new PreConditionFailure("valueName cannot be empty."));

                final Action4<JSONSegment,Iterable<Class<? extends JSONSegment>>,String,String> getWrongTypeExceptionMessageTest = (JSONSegment value, Iterable<Class<? extends JSONSegment>> expectedTypes, String valueName, String expected) ->
                {
                    runner.test("with " + English.andList(value, expectedTypes, Strings.escapeAndQuote(valueName)), (Test test) ->
                    {
                        test.assertEqual(expected, JSON.getWrongTypeExceptionMessage(value, expectedTypes, valueName));
                    });
                };

                getWrongTypeExceptionMessageTest.run(JSONNull.segment, Iterable.create(JSONString.class), "hello", "Expected hello to be a JSONString, but was a JSONNull instead.");
                getWrongTypeExceptionMessageTest.run(JSONBoolean.falseSegment, Iterable.create(JSONNumber.class, JSONObject.class), "hello", "Expected hello to be a JSONNumber or JSONObject, but was a JSONBoolean instead.");
                getWrongTypeExceptionMessageTest.run(JSONNumber.get(7), Iterable.create(JSONBoolean.class, JSONArray.class, JSONObject.class), "hello", "Expected hello to be a JSONBoolean, JSONArray, or JSONObject, but was a JSONNumber instead.");
            });

            runner.testGroup("as(JSONSegment,Class<T>,String)", () ->
            {
                final Action4<JSONSegment,Class<? extends JSONSegment>,String,Throwable> asErrorTest = (JSONSegment value, Class<? extends JSONSegment> type, String valueName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value, type, Strings.escapeAndQuote(valueName)), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.as(value, type, valueName).await(), expected);
                    });
                };

                asErrorTest.run(null, JSONNumber.class, "hello", new PreConditionFailure("value cannot be null."));
                asErrorTest.run(JSONBoolean.falseSegment, null, "hello", new PreConditionFailure("type cannot be null."));
                asErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, null, new PreConditionFailure("valueName cannot be null."));
                asErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, "", new PreConditionFailure("valueName cannot be empty."));
                asErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, "hello", new WrongTypeException("Expected hello to be a JSONNumber, but was a JSONBoolean instead."));
                asErrorTest.run(JSONNull.segment, JSONNumber.class, "hello", new WrongTypeException("Expected hello to be a JSONNumber, but was a JSONNull instead."));

                final Action3<JSONSegment,Class<JSONNumber>,String> asTest = (JSONSegment value, Class<JSONNumber> type, String valueName) ->
                {
                    runner.test("with " + English.andList(value, type, Strings.escapeAndQuote(valueName)), (Test test) ->
                    {
                        final JSONNumber asResult = JSON.as(value, type, valueName).await();
                        test.assertSame(value, asResult);
                    });
                };

                asTest.run(JSONNumber.get(50), JSONNumber.class, "foo");
            });

            runner.testGroup("as(JSONSegment,Class<T>)", () ->
            {
                final Action3<JSONSegment,Class<? extends JSONSegment>,Throwable> asErrorTest = (JSONSegment value, Class<? extends JSONSegment> type, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value, type), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.as(value, type).await(), expected);
                    });
                };

                asErrorTest.run(null, JSONNumber.class, new PreConditionFailure("value cannot be null."));
                asErrorTest.run(JSONBoolean.falseSegment, null, new PreConditionFailure("type cannot be null."));
                asErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, new WrongTypeException("Expected value to be a JSONNumber, but was a JSONBoolean instead."));
                asErrorTest.run(JSONNull.segment, JSONNumber.class, new WrongTypeException("Expected value to be a JSONNumber, but was a JSONNull instead."));

                final Action2<JSONSegment,Class<JSONNumber>> asTest = (JSONSegment value, Class<JSONNumber> type) ->
                {
                    runner.test("with " + English.andList(value, type), (Test test) ->
                    {
                        final JSONNumber asResult = JSON.as(value, type).await();
                        test.assertSame(value, asResult);
                    });
                };

                asTest.run(JSONNumber.get(50), JSONNumber.class);
            });

            runner.testGroup("asOrNull(JSONSegment,Class<T>,String)", () ->
            {
                final Action4<JSONSegment,Class<? extends JSONSegment>,String,Throwable> asOrNullErrorTest = (JSONSegment value, Class<? extends JSONSegment> type, String valueName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value, type, Strings.escapeAndQuote(valueName)), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.asOrNull(value, type, valueName).await(), expected);
                    });
                };

                asOrNullErrorTest.run(null, JSONNumber.class, "hello", new PreConditionFailure("value cannot be null."));
                asOrNullErrorTest.run(JSONBoolean.falseSegment, null, "hello", new PreConditionFailure("type cannot be null."));
                asOrNullErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, null, new PreConditionFailure("valueName cannot be null."));
                asOrNullErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, "", new PreConditionFailure("valueName cannot be empty."));
                asOrNullErrorTest.run(JSONBoolean.falseSegment, JSONNumber.class, "hello", new WrongTypeException("Expected hello to be a JSONNumber or JSONNull, but was a JSONBoolean instead."));

                final Action4<JSONSegment,Class<JSONNumber>,String,JSONNumber> asOrNullTest = (JSONSegment value, Class<JSONNumber> type, String valueName, JSONNumber expected) ->
                {
                    runner.test("with " + English.andList(value, type, Strings.escapeAndQuote(valueName)), (Test test) ->
                    {
                        final JSONNumber asResult = JSON.asOrNull(value, type, valueName).await();
                        test.assertEqual(expected, asResult);
                    });
                };

                asOrNullTest.run(JSONNumber.get(50), JSONNumber.class, "foo", JSONNumber.get(50));
                asOrNullTest.run(JSONNull.segment, JSONNumber.class, "foo", null);
            });

            runner.testGroup("asOrNull(JSONSegment,Class<T>)", () ->
            {
                final Action3<JSONSegment,Class<? extends JSONSegment>,Throwable> asOrNullErrorTest = (JSONSegment value, Class<? extends JSONSegment> type, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value, type), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.asOrNull(value, type).await(), expected);
                    });
                };

                asOrNullErrorTest.run(null, JSONNumber.class, new PreConditionFailure("value cannot be null."));
                asOrNullErrorTest.run(JSONBoolean.falseSegment, null, new PreConditionFailure("type cannot be null."));

                final Action3<JSONSegment,Class<JSONNumber>,JSONNumber> asOrNullTest = (JSONSegment value, Class<JSONNumber> type, JSONNumber expected) ->
                {
                    runner.test("with " + English.andList(value, type), (Test test) ->
                    {
                        final JSONNumber asResult = JSON.asOrNull(value, type).await();
                        test.assertEqual(expected, asResult);
                    });
                };

                asOrNullTest.run(JSONNumber.get(50), JSONNumber.class, JSONNumber.get(50));
                asOrNullTest.run(JSONNull.segment, JSONNumber.class, null);
            });

            runner.testGroup("toBooleanOrNull(JSONBoolean)", () ->
            {
                final Action2<JSONBoolean,Boolean> toBooleanOrNullTest = (JSONBoolean segment, Boolean expected) ->
                {
                    runner.test("with " + segment, (Test test) ->
                    {
                        test.assertEqual(expected, JSON.toBooleanOrNull(segment));
                    });
                };

                toBooleanOrNullTest.run(JSONBoolean.falseSegment, false);
                toBooleanOrNullTest.run(JSONBoolean.trueSegment, true);
                toBooleanOrNullTest.run(null, null);
            });

            runner.testGroup("toNumberOrNull(JSONNumber)", () ->
            {
                final Action2<JSONNumber,Double> toNumberOrNullTest = (JSONNumber segment, Double expected) ->
                {
                    runner.test("with " + segment, (Test test) ->
                    {
                        test.assertEqual(expected, JSON.toNumberOrNull(segment));
                    });
                };

                toNumberOrNullTest.run(JSONNumber.get(3), 3.0);
                toNumberOrNullTest.run(JSONNumber.get(10.5), 10.5);
                toNumberOrNullTest.run(null, null);
            });

            runner.testGroup("toStringOrNull(JSONString)", () ->
            {
                final Action2<JSONString,String> toStringOrNullTest = (JSONString segment, String expected) ->
                {
                    runner.test("with " + segment, (Test test) ->
                    {
                        test.assertEqual(expected, JSON.toStringOrNull(segment));
                    });
                };

                toStringOrNullTest.run(JSONString.get(""), "");
                toStringOrNullTest.run(JSONString.get("blah"), "blah");
                toStringOrNullTest.run(null, null);
            });

            runner.testGroup("parse(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parse((File)null).await(),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.getFile("/file.json").await();
                    test.assertThrows(() -> JSON.parse(file).await(),
                        new FileNotFoundException(file));
                });

                runner.test("with empty file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.setFileContentsAsString("/file.json", "").await();
                    test.assertThrows(() -> JSON.parse(file).await(),
                        new ParseException("No JSON tokens found."));
                });

                runner.test("with empty JSONObject", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.setFileContentsAsString("/file.json", "{}").await();
                    test.assertEqual(JSONObject.create(), JSON.parse(file).await());
                });
            });

            runner.testGroup("parse(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parse((ByteReadStream)null).await(),
                        new PreConditionFailure("bytes cannot be null."));
                });

                final Action2<String,Throwable> parseErrorTest = (String contents, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertThrows(() -> JSON.parse((ByteReadStream)bytes).await(), expected);
                    });
                };

                parseErrorTest.run("", new ParseException("No JSON tokens found."));

                final Action2<String,JSONSegment> parseTest = (String contents, JSONSegment expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertEqual(JSONObject.create(), JSON.parse((ByteReadStream)bytes).await());
                    });
                };

                parseTest.run("{}", JSONObject.create());
            });

            runner.testGroup("parse(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parse((CharacterReadStream)null).await(),
                        new PreConditionFailure("characters cannot be null."));
                });

                final Action2<String,Throwable> parseErrorTest = (String contents, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertThrows(() -> JSON.parse((CharacterReadStream)bytes).await(), expected);
                    });
                };

                parseErrorTest.run("", new ParseException("No JSON tokens found."));

                final Action2<String,JSONSegment> parseTest = (String contents, JSONSegment expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertEqual(JSONObject.create(), JSON.parse((CharacterReadStream)bytes).await());
                    });
                };

                parseTest.run("{}", JSONObject.create());
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.parse(text).await(), expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("", new ParseException("No JSON tokens found."));
                parseErrorTest.run("// hello", new ParseException("No JSON tokens found."));
                parseErrorTest.run("/* hello */", new ParseException("No JSON tokens found."));
                parseErrorTest.run(",", new ParseException("Unexpected JSON token: ,"));

                final Action2<String,JSONSegment> parseTest = (String text, JSONSegment expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, JSON.parse(text).await());
                    });
                };

                parseTest.run("null", JSONNull.segment);
                parseTest.run("false", JSONBoolean.falseSegment);
                parseTest.run("true", JSONBoolean.trueSegment);
                parseTest.run("123", JSONNumber.get("123"));
                parseTest.run("\"hello\"", JSONString.get("hello"));
                parseTest.run("[]", JSONArray.create());
                parseTest.run("{}", JSONObject.create());
            });

            runner.testGroup("parse(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parse((Iterable<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parse(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parse((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parse(JSONTokenizer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parse((JSONTokenizer)null),
                        new PreConditionFailure("tokenizer cannot be null."));
                });

                runner.test("with non-started tokenizer", (Test test) ->
                {
                    final JSONTokenizer tokenizer = JSONTokenizer.create("{}");
                    test.assertEqual(JSONObject.create(), JSON.parse(tokenizer).await());
                });

                runner.test("with started tokenizer on whitespace token", (Test test) ->
                {
                    final JSONTokenizer tokenizer = JSONTokenizer.create(" true");
                    tokenizer.next();
                    test.assertEqual(JSONBoolean.trueSegment, JSON.parse(tokenizer).await());
                });

                runner.test("with started tokenizer on newline token", (Test test) ->
                {
                    final JSONTokenizer tokenizer = JSONTokenizer.create("\r\nfalse");
                    tokenizer.next();
                    test.assertEqual(JSONBoolean.falseSegment, JSON.parse(tokenizer).await());
                });

                runner.test("with started tokenizer on line comment token", (Test test) ->
                {
                    final JSONTokenizer tokenizer = JSONTokenizer.create("// line comment\r\nfalse");
                    tokenizer.next();
                    test.assertEqual(JSONBoolean.falseSegment, JSON.parse(tokenizer).await());
                });

                runner.test("with started tokenizer on block comment token", (Test test) ->
                {
                    final JSONTokenizer tokenizer = JSONTokenizer.create("/* block comment */false");
                    tokenizer.next();
                    test.assertEqual(JSONBoolean.falseSegment, JSON.parse(tokenizer).await());
                });
            });

            runner.testGroup("parseObject(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObject((File)null).await(),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.getFile("/file.json").await();
                    test.assertThrows(() -> JSON.parseObject(file).await(),
                        new FileNotFoundException(file));
                });

                runner.test("with empty file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.setFileContentsAsString("/file.json", "").await();
                    test.assertThrows(() -> JSON.parseObject(file).await(),
                        new ParseException("Missing object left curly bracket ('{')."));
                });

                runner.test("with empty JSONObject", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.setFileContentsAsString("/file.json", "{}").await();
                    test.assertEqual(JSONObject.create(), JSON.parseObject(file).await());
                });
            });

            runner.testGroup("parseObject(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObject((ByteReadStream)null).await(),
                        new PreConditionFailure("bytes cannot be null."));
                });

                final Action2<String,Throwable> parseErrorTest = (String contents, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertThrows(() -> JSON.parseObject((ByteReadStream)bytes).await(), expected);
                    });
                };

                parseErrorTest.run("", new ParseException("Missing object left curly bracket ('{')."));

                final Action2<String,JSONObject> parseTest = (String contents, JSONObject expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertEqual(expected, JSON.parseObject((ByteReadStream)bytes).await());
                    });
                };

                parseTest.run("{}", JSONObject.create());
            });

            runner.testGroup("parseObject(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObject((CharacterReadStream)null).await(),
                        new PreConditionFailure("characters cannot be null."));
                });

                final Action2<String,Throwable> parseErrorTest = (String contents, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertThrows(() -> JSON.parseObject((CharacterReadStream)bytes).await(), expected);
                    });
                };

                parseErrorTest.run("", new ParseException("Missing object left curly bracket ('{')."));

                final Action2<String,JSONObject> parseTest = (String contents, JSONObject expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream characters = InMemoryCharacterToByteStream.create();
                        characters.write(contents).await();
                        characters.endOfStream();
                        test.assertEqual(expected, JSON.parseObject((CharacterReadStream)characters).await());
                    });
                };

                parseTest.run("{}", JSONObject.create());
            });

            runner.testGroup("parseObject(String)", () ->
            {
                final Action2<String,Throwable> parseObjectErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.parseObject(text).await(), expected);
                    });
                };

                parseObjectErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseObjectErrorTest.run("", new ParseException("Missing object left curly bracket ('{')."));
                parseObjectErrorTest.run("null", new ParseException("Expected object left curly bracket ('{')."));
                parseObjectErrorTest.run("false", new ParseException("Expected object left curly bracket ('{')."));
                parseObjectErrorTest.run("true", new ParseException("Expected object left curly bracket ('{')."));
                parseObjectErrorTest.run("// hello", new ParseException("Missing object left curly bracket ('{')."));
                parseObjectErrorTest.run("/* hello */", new ParseException("Missing object left curly bracket ('{')."));
                parseObjectErrorTest.run("123", new ParseException("Expected object left curly bracket ('{')."));
                parseObjectErrorTest.run("\"hello\"", new ParseException("Expected object left curly bracket ('{')."));
                parseObjectErrorTest.run("{", new ParseException("Missing object right curly bracket ('}')."));
                parseObjectErrorTest.run("{   ", new ParseException("Missing object right curly bracket ('}')."));
                parseObjectErrorTest.run("{,", new ParseException("Expected quoted-string object property name or right curly bracket ('}')."));
                parseObjectErrorTest.run("{ null", new ParseException("Expected quoted-string object property name or right curly bracket ('}')."));
                parseObjectErrorTest.run("{ true", new ParseException("Expected quoted-string object property name or right curly bracket ('}')."));
                parseObjectErrorTest.run("{ 50", new ParseException("Expected quoted-string object property name or right curly bracket ('}')."));
                parseObjectErrorTest.run("{ \"a\"", new ParseException("Missing object property name and value separator (':')."));
                parseObjectErrorTest.run("{ \"a\":", new ParseException("Missing object property value."));
                parseObjectErrorTest.run("{ \"a\":0", new ParseException("Missing object right curly bracket ('}')."));
                parseObjectErrorTest.run("{ \"a\":0,", new ParseException("Missing object property."));
                parseObjectErrorTest.run("{ \"a\":0, }", new ParseException("Expected quoted-string object property name."));
                parseObjectErrorTest.run("{ \"a\":0,,", new ParseException("Expected quoted-string object property name."));
                parseObjectErrorTest.run("{ \"a\":0 \"b\":1", new ParseException("Expected object property separator (',') or right curly bracket ('}')."));
                parseObjectErrorTest.run("{ \"a\":0 null", new ParseException("Expected object property separator (',') or right curly bracket ('}')."));
                parseObjectErrorTest.run("{ \"a\":0, null", new ParseException("Expected quoted-string object property name."));

                final Action2<String,JSONObject> parseObjectTest = (String text, JSONObject expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, JSON.parseObject(text).await());
                    });
                };

                parseObjectTest.run("{}", JSONObject.create());
                parseObjectTest.run("{\"b\":null}",
                    JSONObject.create(
                        JSONProperty.create("b", JSONNull.segment)));
                parseObjectTest.run("{\"b\":\"c\"}",
                    JSONObject.create(
                        JSONProperty.create("b", "c")));
                parseObjectTest.run("{\"b\":[1, 2, 3]}",
                    JSONObject.create(
                        JSONProperty.create("b",
                            JSONArray.create(
                                JSONNumber.get(1),
                                JSONNumber.get(2),
                                JSONNumber.get(3)))));
                parseObjectTest.run("{\"c\":{}}",
                    JSONObject.create(
                        JSONProperty.create("c", JSONObject.create())));
                parseObjectTest.run("{\"c\":{},\"d\":50.2}",
                    JSONObject.create(
                        JSONProperty.create("c", JSONObject.create()),
                        JSONProperty.create("d", JSONNumber.get(50.2))));
            });

            runner.testGroup("parseObject(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObject((Iterable<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parseObject(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObject((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parseObject(JSONTokenizer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObject((JSONTokenizer)null),
                        new PreConditionFailure("tokenizer cannot be null."));
                });
            });

            runner.testGroup("parseObjectProperty(String)", () ->
            {
                final Action2<String,Throwable> parseObjectPropertyErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.parseObjectProperty(text).await(), expected);
                    });
                };

                parseObjectPropertyErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseObjectPropertyErrorTest.run("", new ParseException("Missing object property name."));
                parseObjectPropertyErrorTest.run("// hello", new ParseException("Missing object property name."));
                parseObjectPropertyErrorTest.run("/* hello */", new ParseException("Missing object property name."));
                parseObjectPropertyErrorTest.run("   ", new ParseException("Missing object property name."));
                parseObjectPropertyErrorTest.run("null", new ParseException("Expected object property name."));
                parseObjectPropertyErrorTest.run("false", new ParseException("Expected object property name."));
                parseObjectPropertyErrorTest.run("true", new ParseException("Expected object property name."));
                parseObjectPropertyErrorTest.run("123", new ParseException("Expected object property name."));
                parseObjectPropertyErrorTest.run("\"hello\"", new ParseException("Missing object property name and value separator (':')."));
                parseObjectPropertyErrorTest.run("\"hello\"   ", new ParseException("Missing object property name and value separator (':')."));
                parseObjectPropertyErrorTest.run("\"hello\"null  ", new ParseException("Expected object property name and value separator (':')."));
                parseObjectPropertyErrorTest.run("\"hello\" null  ", new ParseException("Expected object property name and value separator (':')."));
                parseObjectPropertyErrorTest.run("\"hello\":", new ParseException("Missing object property value."));
                parseObjectPropertyErrorTest.run("\"hello\":,", new ParseException("Expected object property value."));
                parseObjectPropertyErrorTest.run("\"hello\":}", new ParseException("Unexpected object property value token: \"}\""));
                parseObjectPropertyErrorTest.run("\"hello\":]", new ParseException("Unexpected object property value token: \"]\""));
                parseObjectPropertyErrorTest.run("\"\":true", new ParseException("Expected object property name to be not empty."));

                final Action2<String,JSONProperty> parseObjectPropertyTest = (String text, JSONProperty expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, JSON.parseObjectProperty(text).await());
                    });
                };

                parseObjectPropertyTest.run("\"a\":false", JSONProperty.create("a", false));
                parseObjectPropertyTest.run("\"a\":true", JSONProperty.create("a", true));
                parseObjectPropertyTest.run("\"a\":null", JSONProperty.create("a", JSONNull.segment));
                parseObjectPropertyTest.run("\"b\" : 123", JSONProperty.create("b", 123));
                parseObjectPropertyTest.run("\"b\" : 123.4", JSONProperty.create("b", 123.4));
                parseObjectPropertyTest.run("\"c\"\n:\n\"hello\"", JSONProperty.create("c", "hello"));
                parseObjectPropertyTest.run("\"hello\":{}", JSONProperty.create("hello", JSONObject.create()));
                parseObjectPropertyTest.run("\"d\":[]", JSONProperty.create("d", JSONArray.create()));
                parseObjectPropertyTest.run("\"d\":[true]", JSONProperty.create("d", JSONArray.create(JSONBoolean.trueSegment)));
            });

            runner.testGroup("parseObjectProperty(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObjectProperty((Iterable<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parseObjectProperty(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObjectProperty((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parseObjectProperty(JSONTokenizer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseObjectProperty((JSONTokenizer)null),
                        new PreConditionFailure("tokenizer cannot be null."));
                });
            });

            runner.testGroup("parseArray(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseArray((File)null).await(),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.getFile("/file.json").await();
                    test.assertThrows(() -> JSON.parseArray(file).await(),
                        new FileNotFoundException(file));
                });

                runner.test("with empty file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.setFileContentsAsString("/file.json", "").await();
                    test.assertThrows(() -> JSON.parseArray(file).await(),
                        new ParseException("Missing array left square bracket ('[')."));
                });

                runner.test("with empty JSONArray", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();
                    final File file = fileSystem.setFileContentsAsString("/file.json", "[]").await();
                    test.assertEqual(JSONArray.create(), JSON.parseArray(file).await());
                });
            });

            runner.testGroup("parseArray(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseArray((ByteReadStream)null).await(),
                        new PreConditionFailure("bytes cannot be null."));
                });

                final Action2<String,Throwable> parseErrorTest = (String contents, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertThrows(() -> JSON.parseArray((ByteReadStream)bytes).await(), expected);
                    });
                };

                parseErrorTest.run("", new ParseException("Missing array left square bracket ('[')."));

                final Action2<String,JSONArray> parseTest = (String contents, JSONArray expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream bytes = InMemoryCharacterToByteStream.create();
                        bytes.write(contents).await();
                        bytes.endOfStream();
                        test.assertEqual(expected, JSON.parseArray((ByteReadStream)bytes).await());
                    });
                };

                parseTest.run("[]", JSONArray.create());
            });

            runner.testGroup("parseArray(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseArray((CharacterReadStream)null).await(),
                        new PreConditionFailure("characters cannot be null."));
                });

                final Action2<String,Throwable> parseErrorTest = (String contents, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream characters = InMemoryCharacterToByteStream.create();
                        characters.write(contents).await();
                        characters.endOfStream();
                        test.assertThrows(() -> JSON.parseArray((CharacterReadStream)characters).await(), expected);
                    });
                };

                parseErrorTest.run("", new ParseException("Missing array left square bracket ('[')."));

                final Action2<String,JSONArray> parseTest = (String contents, JSONArray expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(contents), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream characters = InMemoryCharacterToByteStream.create();
                        characters.write(contents).await();
                        characters.endOfStream();
                        test.assertEqual(expected, JSON.parseArray((CharacterReadStream)characters).await());
                    });
                };

                parseTest.run("[]", JSONArray.create());
            });

            runner.testGroup("parseArray(String)", () ->
            {
                final Action2<String,Throwable> parseArrayErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> JSON.parseArray(text).await(), expected);
                    });
                };

                parseArrayErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseArrayErrorTest.run("", new ParseException("Missing array left square bracket ('[')."));
                parseArrayErrorTest.run("null", new ParseException("Expected array left square bracket ('[')."));
                parseArrayErrorTest.run("false", new ParseException("Expected array left square bracket ('[')."));
                parseArrayErrorTest.run("true", new ParseException("Expected array left square bracket ('[')."));
                parseArrayErrorTest.run("// hello", new ParseException("Missing array left square bracket ('[')."));
                parseArrayErrorTest.run("/* hello */", new ParseException("Missing array left square bracket ('[')."));
                parseArrayErrorTest.run("  \t\n\r\n\r  ", new ParseException("Missing array left square bracket ('[')."));
                parseArrayErrorTest.run("123", new ParseException("Expected array left square bracket ('[')."));
                parseArrayErrorTest.run("\"hello\"", new ParseException("Expected array left square bracket ('[')."));
                parseArrayErrorTest.run("[", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[   ", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[\n  \t", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[//hello\n  ", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[  /** hello ***/\n", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[null", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[ true ", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[ true false ", new ParseException("Expected array element separator (',') or right square bracket (']')."));
                parseArrayErrorTest.run("[,", new ParseException("Expected array element."));
                parseArrayErrorTest.run("[true,", new ParseException("Missing array element."));
                parseArrayErrorTest.run("[true,]", new ParseException("Expected array element."));
                parseArrayErrorTest.run("[[]", new ParseException("Missing array right square bracket (']')."));
                parseArrayErrorTest.run("[[][", new ParseException("Expected array element separator (',') or right square bracket (']')."));
                parseArrayErrorTest.run("[:", new ParseException("Unexpected array element token: \":\""));
                parseArrayErrorTest.run("[{", new ParseException("Missing object right curly bracket ('}')."));

                final Action2<String,JSONArray> parseArrayTest = (String text, JSONArray expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, JSON.parseArray(text).await());
                    });
                };

                parseArrayTest.run("[]", JSONArray.create());
                parseArrayTest.run("[    ]", JSONArray.create());
                parseArrayTest.run("[null]", JSONArray.create(JSONNull.segment));
                parseArrayTest.run("[false]", JSONArray.create(JSONBoolean.falseSegment));
                parseArrayTest.run("[true]", JSONArray.create(JSONBoolean.trueSegment));
                parseArrayTest.run("[ null, true ]", JSONArray.create(JSONNull.segment, JSONBoolean.trueSegment));
                parseArrayTest.run("[ null, true, false ]", JSONArray.create(JSONNull.segment, JSONBoolean.trueSegment, JSONBoolean.falseSegment));
                parseArrayTest.run("[[],[],[  ]]", JSONArray.create(JSONArray.create(), JSONArray.create(), JSONArray.create()));
            });

            runner.testGroup("parseArray(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseArray((Iterable<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parseArray(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseArray((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });
            });

            runner.testGroup("parseArray(JSONTokenizer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSON.parseArray((JSONTokenizer)null),
                        new PreConditionFailure("tokenizer cannot be null."));
                });
            });
        });
    }
}
