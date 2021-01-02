package qub;

public interface JSONPropertyTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(JSONProperty.class, () ->
        {
            runner.testGroup("create(String,boolean)", () ->
            {
                final Action3<String,Boolean,Throwable> createErrorTest = (String propertyName, Boolean propertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName) + " and " + propertyValue, (Test test) ->
                    {
                        test.assertThrows(() -> JSONProperty.create(propertyName, propertyValue),
                            expectedError);
                    });
                };

                createErrorTest.run(null, false, new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", false, new PreConditionFailure("name cannot be empty."));

                final Action2<String,Boolean> createTest = (String propertyName, Boolean propertyValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName) + " and " + propertyValue, (Test test) ->
                    {
                        final JSONProperty property = JSONProperty.create(propertyName, propertyValue);
                        test.assertEqual(propertyName, property.getName());
                        test.assertEqual(propertyName, property.getKey());
                        test.assertEqual(JSONBoolean.get(propertyValue), property.getValue());
                        test.assertEqual(Strings.quote(propertyName) + ":" + propertyValue, property.toString());
                    });
                };

                createTest.run("a", false);
                createTest.run("bats", true);
            });

            runner.testGroup("create(String,long)", () ->
            {
                final Action3<String,Long,Throwable> createErrorTest = (String propertyName, Long propertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName) + " and " + propertyValue, (Test test) ->
                    {
                        test.assertThrows(() -> JSONProperty.create(propertyName, propertyValue),
                            expectedError);
                    });
                };

                createErrorTest.run(null, 10L, new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", 30L, new PreConditionFailure("name cannot be empty."));

                final Action2<String,Long> createTest = (String propertyName, Long propertyValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName) + " and " + propertyValue, (Test test) ->
                    {
                        final JSONProperty property = JSONProperty.create(propertyName, propertyValue);
                        test.assertEqual(propertyName, property.getName());
                        test.assertEqual(propertyName, property.getKey());
                        test.assertEqual(JSONNumber.get(propertyValue), property.getValue());
                        test.assertEqual(Strings.quote(propertyName) + ":" + propertyValue, property.toString());
                    });
                };

                createTest.run("a", 700L);
                createTest.run("bats", -20L);
            });

            runner.testGroup("create(String,double)", () ->
            {
                final Action3<String,Double,Throwable> createErrorTest = (String propertyName, Double propertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName) + " and " + propertyValue, (Test test) ->
                    {
                        test.assertThrows(() -> JSONProperty.create(propertyName, propertyValue),
                            expectedError);
                    });
                };

                createErrorTest.run(null, 15.0, new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", 0.001, new PreConditionFailure("name cannot be empty."));

                final Action2<String,Double> createTest = (String propertyName, Double propertyValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(propertyName) + " and " + propertyValue, (Test test) ->
                    {
                        final JSONProperty property = JSONProperty.create(propertyName, propertyValue);
                        test.assertEqual(propertyName, property.getName());
                        test.assertEqual(propertyName, property.getKey());
                        test.assertEqual(JSONNumber.get(propertyValue), property.getValue());
                        test.assertEqual(Strings.quote(propertyName) + ":" + propertyValue, property.toString());
                    });
                };

                createTest.run("a", 1.23);
                createTest.run("bats", 0.0);
            });

            runner.testGroup("create(String,String)", () ->
            {
                final Action3<String,String,Throwable> createErrorTest = (String propertyName, String propertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + English.andList(Iterable.create(propertyName, propertyValue).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> JSONProperty.create(propertyName, propertyValue),
                            expectedError);
                    });
                };

                createErrorTest.run(null, "b", new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", "b", new PreConditionFailure("name cannot be empty."));
                createErrorTest.run("a", null, new PreConditionFailure("value cannot be null."));

                final Action2<String,String> createTest = (String propertyName, String propertyValue) ->
                {
                    runner.test("with " + English.andList(Iterable.create(propertyName, propertyValue).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final JSONProperty property = JSONProperty.create(propertyName, propertyValue);
                        test.assertEqual(propertyName, property.getName());
                        test.assertEqual(propertyName, property.getKey());
                        test.assertEqual(JSONString.get(propertyValue), property.getValue());
                        test.assertEqual(Strings.quote(propertyName) + ":" + Strings.quote(propertyValue), property.toString());
                    });
                };

                createTest.run("a", "");
                createTest.run("bats", "yup");
            });

            runner.testGroup("create(String,JSONSegment)", () ->
            {
                final Action3<String,JSONSegment,Throwable> createErrorTest = (String propertyName, JSONSegment propertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(propertyName), propertyValue), (Test test) ->
                    {
                        test.assertThrows(() -> JSONProperty.create(propertyName, propertyValue),
                            expectedError);
                    });
                };

                createErrorTest.run(null, JSONNull.segment, new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", JSONNull.segment, new PreConditionFailure("name cannot be empty."));
                createErrorTest.run("a", null, new PreConditionFailure("value cannot be null."));

                final Action2<String,JSONSegment> createTest = (String propertyName, JSONSegment propertyValue) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(propertyName), propertyValue), (Test test) ->
                    {
                        final JSONProperty property = JSONProperty.create(propertyName, propertyValue);
                        test.assertEqual(propertyName, property.getName());
                        test.assertEqual(propertyName, property.getKey());
                        test.assertSame(propertyValue, property.getValue());
                    });
                };

                createTest.run("a", JSONNull.segment);
                createTest.run("bats", JSONString.get("yup"));
            });

            runner.testGroup("getValueAsOrNull(Class<T>)", () ->
            {
                final Action3<JSONProperty,Class<? extends JSONSegment>,Throwable> getValueAsOrNullErrorTest = (JSONProperty property, Class<? extends JSONSegment> type, Throwable expected) ->
                {
                    runner.test("with " + English.andList(property, type), (Test test) ->
                    {
                        test.assertThrows(() -> property.getValueAsOrNull(type).await(), expected);
                    });
                };

                getValueAsOrNullErrorTest.run(JSONProperty.create("a", "b"), null, new PreConditionFailure("type cannot be null."));
                getValueAsOrNullErrorTest.run(JSONProperty.create("a", "b"), JSONNumber.class, new WrongTypeException("Expected the property named \"a\" to be a JSONNumber or JSONNull, but was a JSONString instead."));

                final Action3<JSONProperty,Class<? extends JSONSegment>,JSONSegment> getValueAsOrNullTest = (JSONProperty property, Class<? extends JSONSegment> type, JSONSegment expected) ->
                {
                    runner.test("with " + English.andList(property, type), (Test test) ->
                    {
                        test.assertEqual(expected, property.getValueAsOrNull(type).await());
                    });
                };

                getValueAsOrNullTest.run(JSONProperty.create("a", "b"), JSONString.class, JSONString.get("b"));
                getValueAsOrNullTest.run(JSONProperty.create("a", "b"), JSONSegment.class, JSONString.get("b"));
                getValueAsOrNullTest.run(JSONProperty.create("a", JSONNull.segment), JSONString.class, null);
                getValueAsOrNullTest.run(JSONProperty.create("a", JSONNull.segment), JSONNumber.class, null);
                getValueAsOrNullTest.run(JSONProperty.create("a", JSONNull.segment), JSONSegment.class, JSONNull.segment);
            });

            runner.testGroup("getValueAs(Class<T>)", () ->
            {
                final Action3<JSONProperty,Class<? extends JSONSegment>,Throwable> getValueAsErrorTest = (JSONProperty property, Class<? extends JSONSegment> type, Throwable expected) ->
                {
                    runner.test("with " + English.andList(property, type), (Test test) ->
                    {
                        test.assertThrows(() -> property.getValueAs(type).await(), expected);
                    });
                };

                getValueAsErrorTest.run(JSONProperty.create("a", "b"), null, new PreConditionFailure("type cannot be null."));
                getValueAsErrorTest.run(JSONProperty.create("a", "b"), JSONNumber.class, new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONString instead."));
                getValueAsErrorTest.run(JSONProperty.create("a", JSONNull.segment), JSONNumber.class, new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONNull instead."));

                final Action3<JSONProperty,Class<? extends JSONSegment>,JSONSegment> getValueAsOrNullTest = (JSONProperty property, Class<? extends JSONSegment> type, JSONSegment expected) ->
                {
                    runner.test("with " + English.andList(property, type), (Test test) ->
                    {
                        test.assertEqual(expected, property.getValueAs(type).await());
                    });
                };

                getValueAsOrNullTest.run(JSONProperty.create("a", "b"), JSONString.class, JSONString.get("b"));
                getValueAsOrNullTest.run(JSONProperty.create("a", "b"), JSONSegment.class, JSONString.get("b"));
                getValueAsOrNullTest.run(JSONProperty.create("a", JSONNull.segment), JSONNull.class, JSONNull.segment);
                getValueAsOrNullTest.run(JSONProperty.create("a", JSONNull.segment), JSONSegment.class, JSONNull.segment);
            });

            runner.testGroup("getNullValueSegment()", () ->
            {
                final Action2<JSONProperty,Throwable> getNullValueSegmentErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getNullValueSegment().await(), expected);
                    });
                };

                getNullValueSegmentErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONNull, but was a JSONString instead."));
                getNullValueSegmentErrorTest.run(JSONProperty.create("a", 50), new WrongTypeException("Expected the property named \"a\" to be a JSONNull, but was a JSONNumber instead."));

                final Action2<JSONProperty,JSONNull> getNullValueSegmentTest = (JSONProperty property, JSONNull expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getNullValueSegment().await());
                    });
                };

                getNullValueSegmentTest.run(JSONProperty.create("a", JSONNull.segment), JSONNull.segment);
            });

            runner.testGroup("getNullValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getNullValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getNullValue().await(), expected);
                    });
                };

                getNullValueErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONNull, but was a JSONString instead."));
                getNullValueErrorTest.run(JSONProperty.create("a", 50), new WrongTypeException("Expected the property named \"a\" to be a JSONNull, but was a JSONNumber instead."));

                final Action1<JSONProperty> getNullValueTest = (JSONProperty property) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(null, property.getNullValue().await());
                    });
                };

                getNullValueTest.run(JSONProperty.create("a", JSONNull.segment));
            });

            runner.testGroup("getBooleanValueSegment()", () ->
            {
                final Action2<JSONProperty,Throwable> getBooleanValueSegmentErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getBooleanValueSegment().await(), expected);
                    });
                };

                getBooleanValueSegmentErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean, but was a JSONString instead."));
                getBooleanValueSegmentErrorTest.run(JSONProperty.create("a", 50), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean, but was a JSONNumber instead."));
                getBooleanValueSegmentErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean, but was a JSONNull instead."));

                final Action2<JSONProperty,JSONBoolean> getBooleanValueSegmentTest = (JSONProperty property, JSONBoolean expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getBooleanValueSegment().await());
                    });
                };

                getBooleanValueSegmentTest.run(JSONProperty.create("a", JSONBoolean.falseSegment), JSONBoolean.falseSegment);
                getBooleanValueSegmentTest.run(JSONProperty.create("a", JSONBoolean.trueSegment), JSONBoolean.trueSegment);
            });

            runner.testGroup("getBooleanValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getBooleanValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getBooleanValue().await(), expected);
                    });
                };

                getBooleanValueErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean, but was a JSONString instead."));
                getBooleanValueErrorTest.run(JSONProperty.create("a", 50), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean, but was a JSONNumber instead."));
                getBooleanValueErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean, but was a JSONNull instead."));

                final Action2<JSONProperty,Boolean> getBooleanValueTest = (JSONProperty property, Boolean expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getBooleanValue().await());
                    });
                };

                getBooleanValueTest.run(JSONProperty.create("a", JSONBoolean.falseSegment), false);
                getBooleanValueTest.run(JSONProperty.create("a", JSONBoolean.trueSegment), true);
            });

            runner.testGroup("getBooleanOrNullValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getBooleanOrNullValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getBooleanOrNullValue().await(), expected);
                    });
                };

                getBooleanOrNullValueErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean or JSONNull, but was a JSONString instead."));
                getBooleanOrNullValueErrorTest.run(JSONProperty.create("a", 50), new WrongTypeException("Expected the property named \"a\" to be a JSONBoolean or JSONNull, but was a JSONNumber instead."));

                final Action2<JSONProperty,Boolean> getBooleanOrNullValueTest = (JSONProperty property, Boolean expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getBooleanOrNullValue().await());
                    });
                };

                getBooleanOrNullValueTest.run(JSONProperty.create("a", JSONNull.segment), null);
                getBooleanOrNullValueTest.run(JSONProperty.create("a", JSONBoolean.falseSegment), false);
                getBooleanOrNullValueTest.run(JSONProperty.create("a", JSONBoolean.trueSegment), true);
            });

            runner.testGroup("getNumberValueSegment()", () ->
            {
                final Action2<JSONProperty,Throwable> getNumberValueSegmentErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getNumberValueSegment().await(), expected);
                    });
                };

                getNumberValueSegmentErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONString instead."));
                getNumberValueSegmentErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONBoolean instead."));
                getNumberValueSegmentErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONNull instead."));

                final Action2<JSONProperty,JSONNumber> getNumberValueSegmentTest = (JSONProperty property, JSONNumber expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getNumberValueSegment().await());
                    });
                };

                getNumberValueSegmentTest.run(JSONProperty.create("a", JSONNumber.get(10)), JSONNumber.get(10));
            });

            runner.testGroup("getNumberValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getNumberValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getNumberValue().await(), expected);
                    });
                };

                getNumberValueErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONString instead."));
                getNumberValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONBoolean instead."));
                getNumberValueErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber, but was a JSONNull instead."));

                final Action2<JSONProperty,Double> getNumberValueTest = (JSONProperty property, Double expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getNumberValue().await());
                    });
                };

                getNumberValueTest.run(JSONProperty.create("a", JSONNumber.get(11)), 11.0);
            });

            runner.testGroup("getNumberOrNullValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getNumberOrNullValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getNumberOrNullValue().await(), expected);
                    });
                };

                getNumberOrNullValueErrorTest.run(JSONProperty.create("a", "b"), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber or JSONNull, but was a JSONString instead."));
                getNumberOrNullValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONNumber or JSONNull, but was a JSONBoolean instead."));

                final Action2<JSONProperty,Double> getNumberOrNullValueTest = (JSONProperty property, Double expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getNumberOrNullValue().await());
                    });
                };

                getNumberOrNullValueTest.run(JSONProperty.create("a", JSONNull.segment), null);
                getNumberOrNullValueTest.run(JSONProperty.create("a", JSONNumber.get(11)), 11.0);
            });

            runner.testGroup("getStringValueSegment()", () ->
            {
                final Action2<JSONProperty,Throwable> getStringValueSegmentErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getStringValueSegment().await(), expected);
                    });
                };

                getStringValueSegmentErrorTest.run(JSONProperty.create("a", 3), new WrongTypeException("Expected the property named \"a\" to be a JSONString, but was a JSONNumber instead."));
                getStringValueSegmentErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONString, but was a JSONBoolean instead."));
                getStringValueSegmentErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONString, but was a JSONNull instead."));

                final Action2<JSONProperty,JSONString> getStringValueSegmentTest = (JSONProperty property, JSONString expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getStringValueSegment().await());
                    });
                };

                getStringValueSegmentTest.run(JSONProperty.create("a", "b"), JSONString.get("b"));
            });

            runner.testGroup("getStringValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getStringValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getStringValue().await(), expected);
                    });
                };

                getStringValueErrorTest.run(JSONProperty.create("a", 7), new WrongTypeException("Expected the property named \"a\" to be a JSONString, but was a JSONNumber instead."));
                getStringValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONString, but was a JSONBoolean instead."));
                getStringValueErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONString, but was a JSONNull instead."));

                final Action2<JSONProperty,String> getStringValueTest = (JSONProperty property, String expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getStringValue().await());
                    });
                };

                getStringValueTest.run(JSONProperty.create("a", "b"), "b");
            });

            runner.testGroup("getStringOrNullValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getStringOrNullValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getStringOrNullValue().await(), expected);
                    });
                };

                getStringOrNullValueErrorTest.run(JSONProperty.create("a", 7), new WrongTypeException("Expected the property named \"a\" to be a JSONString or JSONNull, but was a JSONNumber instead."));
                getStringOrNullValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONString or JSONNull, but was a JSONBoolean instead."));

                final Action2<JSONProperty,String> getStringOrNullValueTest = (JSONProperty property, String expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getStringOrNullValue().await());
                    });
                };

                getStringOrNullValueTest.run(JSONProperty.create("a", JSONNull.segment), null);
                getStringOrNullValueTest.run(JSONProperty.create("a", "b"), "b");
            });

            runner.testGroup("getObjectValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getObjectValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getObjectValue().await(), expected);
                    });
                };

                getObjectValueErrorTest.run(JSONProperty.create("a", 7), new WrongTypeException("Expected the property named \"a\" to be a JSONObject, but was a JSONNumber instead."));
                getObjectValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONObject, but was a JSONBoolean instead."));
                getObjectValueErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONObject, but was a JSONNull instead."));

                final Action2<JSONProperty,JSONObject> getObjectValueTest = (JSONProperty property, JSONObject expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getObjectValue().await());
                    });
                };

                getObjectValueTest.run(JSONProperty.create("a", JSONObject.create()), JSONObject.create());
                getObjectValueTest.run(JSONProperty.create("a", JSONObject.create().setString("b", "c")), JSONObject.create().setString("b", "c"));
            });

            runner.testGroup("getObjectOrNullValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getObjectOrNullValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getObjectOrNullValue().await(), expected);
                    });
                };

                getObjectOrNullValueErrorTest.run(JSONProperty.create("a", 7), new WrongTypeException("Expected the property named \"a\" to be a JSONObject or JSONNull, but was a JSONNumber instead."));
                getObjectOrNullValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONObject or JSONNull, but was a JSONBoolean instead."));

                final Action2<JSONProperty,JSONObject> getObjectValueTest = (JSONProperty property, JSONObject expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getObjectOrNullValue().await());
                    });
                };

                getObjectValueTest.run(JSONProperty.create("a", JSONNull.segment), null);
                getObjectValueTest.run(JSONProperty.create("a", JSONObject.create()), JSONObject.create());
                getObjectValueTest.run(JSONProperty.create("a", JSONObject.create().setString("b", "c")), JSONObject.create().setString("b", "c"));
            });

            runner.testGroup("getArrayValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getArrayValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getArrayValue().await(), expected);
                    });
                };

                getArrayValueErrorTest.run(JSONProperty.create("a", 7), new WrongTypeException("Expected the property named \"a\" to be a JSONArray, but was a JSONNumber instead."));
                getArrayValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONArray, but was a JSONBoolean instead."));
                getArrayValueErrorTest.run(JSONProperty.create("a", JSONNull.segment), new WrongTypeException("Expected the property named \"a\" to be a JSONArray, but was a JSONNull instead."));

                final Action2<JSONProperty,JSONArray> getArrayValueTest = (JSONProperty property, JSONArray expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getArrayValue().await());
                    });
                };

                getArrayValueTest.run(JSONProperty.create("a", JSONArray.create()), JSONArray.create());
                getArrayValueTest.run(JSONProperty.create("a", JSONArray.create().addString("b")), JSONArray.create().addString("b"));
            });

            runner.testGroup("getArrayOrNullValue()", () ->
            {
                final Action2<JSONProperty,Throwable> getArrayOrNullValueErrorTest = (JSONProperty property, Throwable expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertThrows(() -> property.getArrayOrNullValue().await(), expected);
                    });
                };

                getArrayOrNullValueErrorTest.run(JSONProperty.create("a", 7), new WrongTypeException("Expected the property named \"a\" to be a JSONArray or JSONNull, but was a JSONNumber instead."));
                getArrayOrNullValueErrorTest.run(JSONProperty.create("a", false), new WrongTypeException("Expected the property named \"a\" to be a JSONArray or JSONNull, but was a JSONBoolean instead."));

                final Action2<JSONProperty,JSONArray> getArrayOrNullValueTest = (JSONProperty property, JSONArray expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.getArrayOrNullValue().await());
                    });
                };

                getArrayOrNullValueTest.run(JSONProperty.create("a", JSONNull.segment), null);
                getArrayOrNullValueTest.run(JSONProperty.create("a", JSONArray.create()), JSONArray.create());
                getArrayOrNullValueTest.run(JSONProperty.create("a", JSONArray.create().addString("b")), JSONArray.create().addString("b"));
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<JSONProperty,String> toStringTest = (JSONProperty property, String expected) ->
                {
                    runner.test("with " + property, (Test test) ->
                    {
                        test.assertEqual(expected, property.toString());
                    });
                };

                toStringTest.run(JSONProperty.create("a", "b"), "\"a\":\"b\"");
                toStringTest.run(JSONProperty.create("a", JSONNull.segment), "\"a\":null");
                toStringTest.run(JSONProperty.create("a", false), "\"a\":false");
                toStringTest.run(JSONProperty.create("a", 5), "\"a\":5");
                toStringTest.run(JSONProperty.create("a", 6.7), "\"a\":6.7");
                toStringTest.run(JSONProperty.create("a", JSONObject.create()), "\"a\":{}");
                toStringTest.run(JSONProperty.create("a", JSONArray.create()), "\"a\":[]");
            });

            runner.testGroup("toString(JSONFormat)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONProperty property = JSONProperty.create("a", "b");
                    test.assertThrows(() -> property.toString(null),
                        new PreConditionFailure("format cannot be null."));
                });

                final Action3<JSONProperty,JSONFormat,String> toStringTest = (JSONProperty property, JSONFormat format, String expected) ->
                {
                    runner.test("with " + English.andList(property, format), (Test test) ->
                    {
                        test.assertEqual(expected, property.toString(format));
                    });
                };

                toStringTest.run(JSONProperty.create("a", "b"), JSONFormat.consise, "\"a\":\"b\"");
                toStringTest.run(JSONProperty.create("a", JSONNull.segment), JSONFormat.consise, "\"a\":null");
                toStringTest.run(JSONProperty.create("a", false), JSONFormat.consise, "\"a\":false");
                toStringTest.run(JSONProperty.create("a", 5), JSONFormat.consise, "\"a\":5");
                toStringTest.run(JSONProperty.create("a", 6.7), JSONFormat.consise, "\"a\":6.7");
                toStringTest.run(JSONProperty.create("a", JSONObject.create()), JSONFormat.consise, "\"a\":{}");
                toStringTest.run(JSONProperty.create("a", JSONArray.create()), JSONFormat.consise, "\"a\":[]");

                toStringTest.run(JSONProperty.create("a", "b"), JSONFormat.pretty, "\"a\": \"b\"");
                toStringTest.run(JSONProperty.create("a", JSONNull.segment), JSONFormat.pretty, "\"a\": null");
                toStringTest.run(JSONProperty.create("a", false), JSONFormat.pretty, "\"a\": false");
                toStringTest.run(JSONProperty.create("a", 5), JSONFormat.pretty, "\"a\": 5");
                toStringTest.run(JSONProperty.create("a", 6.7), JSONFormat.pretty, "\"a\": 6.7");
                toStringTest.run(JSONProperty.create("a", JSONObject.create()), JSONFormat.pretty, "\"a\": {}");
                toStringTest.run(JSONProperty.create("a", JSONArray.create()), JSONFormat.pretty, "\"a\": []");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<JSONProperty,Object,Boolean> equalsTest = (JSONProperty property, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + property + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, property.equals(rhs));
                    });
                };

                equalsTest.run(JSONProperty.create("a", "b"), null, false);
                equalsTest.run(JSONProperty.create("a", "b"), "hello", false);
                equalsTest.run(JSONProperty.create("a", "b"), JSONProperty.create("c", "b"), false);
                equalsTest.run(JSONProperty.create("a", "b"), JSONProperty.create("a", "c"), false);
                equalsTest.run(JSONProperty.create("a", "b"), JSONProperty.create("a", "b"), true);
            });

            runner.testGroup("equals(JSONProperty)", () ->
            {
                final Action3<JSONProperty,JSONProperty,Boolean> equalsTest = (JSONProperty property, JSONProperty rhs, Boolean expected) ->
                {
                    runner.test("with " + property + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, property.equals(rhs));
                    });
                };

                equalsTest.run(JSONProperty.create("a", "b"), null, false);
                equalsTest.run(JSONProperty.create("a", "b"), JSONProperty.create("c", "b"), false);
                equalsTest.run(JSONProperty.create("a", "b"), JSONProperty.create("a", "c"), false);
                equalsTest.run(JSONProperty.create("a", "b"), JSONProperty.create("a", "b"), true);
            });
        });
    }
}
