package qub;

public class JSONPropertyTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JSONProperty.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                final Action6<String,JSONQuotedString,String,JSONToken,JSONSegment,Integer> constructorTest = (String text, JSONQuotedString nameSegment, String name, JSONToken colonSegment, JSONSegment valueSegment, Integer afterEndIndex) ->
                {
                    runner.test("with \"" + text + "\"", (Test test) ->
                    {
                        final JSONProperty propertySegment = JSON.parseProperty(text);

                        test.assertEqual(nameSegment, propertySegment.getNameSegment());
                        test.assertEqual(name, propertySegment.getName());

                        test.assertEqual(colonSegment, propertySegment.getColonSegment());

                        test.assertEqual(valueSegment, propertySegment.getValueSegment());

                        test.assertEqual(text, propertySegment.toString());

                        test.assertEqual(nameSegment.getStartIndex(), propertySegment.getStartIndex());

                        test.assertEqual(afterEndIndex, propertySegment.getAfterEndIndex());

                        test.assertEqual(afterEndIndex - nameSegment.getStartIndex(), propertySegment.getLength());
                    });
                };

                constructorTest.run("\"",
                    JSONToken.quotedString("\"", 0, false),
                    "",
                    null,
                    null,
                    1);
                constructorTest.run("\"\"",
                    JSONToken.quotedString("\"\"", 0, true),
                    "",
                    null,
                    null,
                    2);

                constructorTest.run("\"test",
                    JSONToken.quotedString("\"test", 0, false),
                    "test",
                    null,
                    null,
                    5);
                constructorTest.run("\"test\"",
                    JSONToken.quotedString("\"test\"", 0, true),
                    "test",
                    null,
                    null,
                    6);

                constructorTest.run("\"a\" ",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    null,
                    null,
                    4);
                constructorTest.run("\"a\":",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    JSONToken.colon(3),
                    null,
                    4);
                constructorTest.run("\"a\" :",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    JSONToken.colon(4),
                    null,
                    5);

                constructorTest.run("\"a\":\"b\"",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    JSONToken.colon(3),
                    JSONToken.quotedString("\"b\"", 4, true),
                    7);
                constructorTest.run("\"a\":  ",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    JSONToken.colon(3),
                    null,
                    6);
                constructorTest.run("\"a\":// comment",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    JSONToken.colon(3),
                    null,
                    14);
                constructorTest.run("\"a\":/* comment",
                    JSONToken.quotedString("\"a\"", 0, true),
                    "a",
                    JSONToken.colon(3),
                    null,
                    14);
                constructorTest.run("\"apples\":{}",
                    JSONToken.quotedString("\"apples\"", 0, true),
                    "apples",
                    JSONToken.colon(8),
                    JSON.parseObject("{}", 9),
                    11);
            });
            
            runner.test("equals()", (Test test) ->
            {
                final JSONProperty propertySegment = JSON.parseProperty("\"a\":\"b\"");
                test.assertFalse(propertySegment.equals((Object)null));
                test.assertFalse(propertySegment.equals((JSONProperty)null));

                test.assertFalse(propertySegment.equals((Object)"test"));

                test.assertTrue(propertySegment.equals(propertySegment));
                test.assertTrue(propertySegment.equals(JSON.parseProperty("\"a\":\"b\"")));
                test.assertFalse(propertySegment.equals(JSON.parseProperty("\"a\":50")));
            });
        });
    }
}
