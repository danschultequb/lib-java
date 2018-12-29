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
                    runner.test("with \"" + text + "\"", (Test test) ->
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
            
            runner.test("getPropertySegment()", (Test test) ->
            {
                final JSONObject objectSegment = JSON.parseObject("{ \"a\":1, \"b\": 2 }");
                test.assertNull(objectSegment.getProperty(null));
                test.assertNull(objectSegment.getProperty(""));
                test.assertNull(objectSegment.getProperty("c"));
                test.assertNull(objectSegment.getProperty("\"c\""));
                test.assertEqual(JSON.parseProperty("\"a\":1", 2), objectSegment.getProperty("\"a\""));
                test.assertEqual(JSON.parseProperty("\"a\":1", 2), objectSegment.getProperty("a"));
                test.assertEqual(JSON.parseProperty("\"b\": 2", 9), objectSegment.getProperty("\"b\""));
                test.assertEqual(JSON.parseProperty("\"b\": 2", 9), objectSegment.getProperty("b"));
            });

            runner.test("getPropertyValue()", (Test test) ->
            {
                final JSONObject objectSegment = JSON.parseObject("{ \"a\":1, \"b\": 2 }");
                test.assertNull(objectSegment.getPropertyValue(null));
                test.assertNull(objectSegment.getPropertyValue(""));
                test.assertNull(objectSegment.getPropertyValue("c"));
                test.assertNull(objectSegment.getPropertyValue("\"c\""));
                test.assertEqual(JSONToken.number("1", 6), objectSegment.getPropertyValue("\"a\""));
                test.assertEqual(JSONToken.number("1", 6), objectSegment.getPropertyValue("a"));
                test.assertEqual(JSONToken.number("2", 14), objectSegment.getPropertyValue("\"b\""));
                test.assertEqual(JSONToken.number("2", 14), objectSegment.getPropertyValue("b"));
            });
        });
    }
}
