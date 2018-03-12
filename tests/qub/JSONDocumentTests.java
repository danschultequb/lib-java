package qub;

public class JSONDocumentTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JSONDocument.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                final Action2<String,JSONSegment> constructorTest = (String text, JSONSegment expectedRoot) ->
                {
                    runner.test("with \"" + text + "\"", (Test test) ->
                    {
                        final JSONDocument document = JSON.parse(text);
                        test.assertEqual(expectedRoot, document.getRoot());
                        test.assertEqual(text, document.toString());
                        test.assertEqual(text.length(), document.getLength());
                    });
                };

                constructorTest.run("", null);
                constructorTest.run("// hello", null);
                constructorTest.run("/* there */", null);
                constructorTest.run("\"fever\"", JSONToken.quotedString("\"fever\"", 0, true));
                constructorTest.run("20", JSONToken.number("20"));
                constructorTest.run("[0, 1, 2, 3, 4]", JSON.parseArray("[0, 1, 2, 3, 4]"));
                constructorTest.run("{}", JSON.parseObject("{}"));
            });
        });
    }
}
