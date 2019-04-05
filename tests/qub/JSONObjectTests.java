package qub;

public class JSONObjectTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JSONObject.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                final Action6<String,JSONToken,JSONProperty[],JSONToken,Integer,Integer> constructorTest = (String text, JSONToken leftCurlyBracket, JSONProperty[] propertySegments, JSONToken rightCurlyBracket, Integer afterEndIndex, Integer length) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final JSONObject objectSegment = JSON.parseObject(text);
                        test.assertEqual(leftCurlyBracket, objectSegment.getLeftCurlyBracket());
                        test.assertEqual(rightCurlyBracket, objectSegment.getRightCurlyBracket());
                        test.assertEqual(Array.create(propertySegments), objectSegment.getProperties());
                        test.assertEqual(0, objectSegment.getStartIndex());
                        test.assertEqual(afterEndIndex, objectSegment.getAfterEndIndex());
                        test.assertEqual(length, objectSegment.getLength());
                        test.assertEqual(text, objectSegment.toString());
                    });
                };

                constructorTest.run("{",
                    JSONToken.leftCurlyBracket(0),
                    new JSONProperty[0],
                    null,
                    1,
                    1);

                constructorTest.run("{}",
                    JSONToken.leftCurlyBracket(0),
                    new JSONProperty[0],
                    JSONToken.rightCurlyBracket(1),
                    2,
                    2);

                constructorTest.run("{\"a\":\"b\"}",
                    JSONToken.leftCurlyBracket(0),
                    new JSONProperty[]
                        {
                            JSON.parseProperty("\"a\":\"b\"", 1)
                        },
                    JSONToken.rightCurlyBracket(8),
                    9,
                    9);

                constructorTest.run("{\"1\":\"2\", \"3\":\"4\"}",
                    JSONToken.leftCurlyBracket(0),
                    new JSONProperty[]
                        {
                            JSON.parseProperty("\"1\":\"2\"", 1),
                            JSON.parseProperty("\"3\":\"4\"", 10)
                        },
                    JSONToken.rightCurlyBracket(17),
                    18,
                    18);
            });

            runner.testGroup("getProperty(String)", () ->
            {
                runner.test("with " + Strings.escapeAndQuote("{}") + " and null", (Test test) ->
                {
                    final JSONObject jsonObject = JSON.parseObject("{}");
                    test.assertThrows(() -> jsonObject.getProperty(null), new PreConditionFailure("propertyName cannot be null."));
                });

                runner.test("with " + Strings.escapeAndQuote("{}") + " and " + Strings.escapeAndQuote(""), (Test test) ->
                {
                    final JSONObject jsonObject = JSON.parseObject("{}");
                    test.assertThrows(() -> jsonObject.getProperty(""), new PreConditionFailure("propertyName cannot be empty."));
                });

                final Action4<String,String,JSONProperty,Throwable> getPropertyTest = (String objectText, String propertyName, JSONProperty expectedProperty, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<JSONProperty> propertyResult = jsonObject.getProperty(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(propertyResult::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedProperty, propertyResult.await());
                        }
                    });
                };

                getPropertyTest.run("{ \"a\":1, \"b\": 2 }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getPropertyTest.run("{ \"a\":1, \"b\": 2 }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getPropertyTest.run("{ \"a\":1, \"b\": 2 }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getPropertyTest.run("{ \"a\":1, \"b\": 2 }", "a", JSON.parseProperty("\"a\":1", 2), null);
                getPropertyTest.run("{ \"a\":1, \"b\": 2 }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getPropertyTest.run("{ \"a\":1, \"b\": 2 }", "b", JSON.parseProperty("\"b\": 2", 9), null);
            });

            runner.testGroup("getPropertyValue(String)", () ->
            {
                final Action4<String,String,JSONSegment,Throwable> getPropertyValueTest = (String objectText, String propertyName, JSONSegment expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<JSONSegment> result = jsonObject.getPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getPropertyValueTest.run("{ \"a\":1, \"b\": 2 }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getPropertyValueTest.run("{ \"a\":1, \"b\": 2 }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getPropertyValueTest.run("{ \"a\":1, \"b\": 2 }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getPropertyValueTest.run("{ \"a\":1, \"b\": 2 }", "a", JSONToken.number("1", 6), null);
                getPropertyValueTest.run("{ \"a\":1, \"b\": 2 }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getPropertyValueTest.run("{ \"a\":1, \"b\": 2 }", "b", JSONToken.number("2", 14), null);
            });

            runner.testGroup("getQuotedStringPropertyValue(String)", () ->
            {
                final Action4<String,String,JSONQuotedString,Throwable> getQuotedStringPropertyValueTest = (String objectText, String propertyName, JSONQuotedString expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<JSONQuotedString> result = jsonObject.getQuotedStringPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getQuotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getQuotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getQuotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getQuotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be a quoted-string."));
                getQuotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getQuotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "b", JSONToken.quotedString("\"2\"", 14, true), null);
            });

            runner.testGroup("getUnquotedStringPropertyValue(String)", () ->
            {
                final Action4<String,String,String,Throwable> getUnquotedStringPropertyValueTest = (String objectText, String propertyName, String expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<String> result = jsonObject.getUnquotedStringPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getUnquotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getUnquotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getUnquotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getUnquotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be a quoted-string."));
                getUnquotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getUnquotedStringPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "b", "2", null);
            });

            runner.testGroup("getObjectPropertyValue(String)", () ->
            {
                final Action4<String,String,JSONObject,Throwable> getObjectPropertyValueTest = (String objectText, String propertyName, JSONObject expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<JSONObject> result = jsonObject.getObjectPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getObjectPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getObjectPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getObjectPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getObjectPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be an object."));
                getObjectPropertyValueTest.run("{ \"a\":1, \"b\": {} }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getObjectPropertyValueTest.run("{ \"a\":1, \"b\": {} }", "b", JSON.parseObject("{}", 14), null);
            });

            runner.testGroup("getArrayPropertyValue(String)", () ->
            {
                final Action4<String,String,JSONArray,Throwable> getArrayPropertyValueTest = (String objectText, String propertyName, JSONArray expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<JSONArray> result = jsonObject.getArrayPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getArrayPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getArrayPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":1, \"b\": \"2\" }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be an array."));
                getArrayPropertyValueTest.run("{ \"a\":1, \"b\": [] }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":1, \"b\": [] }", "b", JSON.parseArray("[]", 14), null);
            });

            runner.testGroup("getNumberTokenPropertyValue(String)", () ->
            {
                final Action4<String,String,JSONToken,Throwable> getArrayPropertyValueTest = (String objectText, String propertyName, JSONToken expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<JSONToken> result = jsonObject.getNumberTokenPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be a number."));
                getArrayPropertyValueTest.run("{ \"a\":[], \"b\": 2 }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be a number."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "b", JSONToken.number("2", 16), null);
            });

            runner.testGroup("getNumberPropertyValue(String)", () ->
            {
                final Action4<String,String,Double,Throwable> getArrayPropertyValueTest = (String objectText, String propertyName, Double expectedPropertyValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(objectText) + " and " + Strings.escapeAndQuote(propertyName), (Test test) ->
                    {
                        final JSONObject jsonObject = JSON.parseObject(objectText);
                        final Result<Double> result = jsonObject.getNumberPropertyValue(propertyName);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedPropertyValue, result.await());
                        }
                    });
                };

                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "c", null, new NotFoundException("No property was found with the name \"c\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "\"c\"", null, new NotFoundException("No property was found with the name \"\\\"c\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "\"a\"", null, new NotFoundException("No property was found with the name \"\\\"a\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be a number."));
                getArrayPropertyValueTest.run("{ \"a\":[], \"b\": 2 }", "a", null, new WrongTypeException("Expected the value of the property named \"a\" to be a number."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "\"b\"", null, new NotFoundException("No property was found with the name \"\\\"b\\\"\"."));
                getArrayPropertyValueTest.run("{ \"a\":\"1\", \"b\": 2 }", "b", 2.0, null);
            });
        });
    }
}
