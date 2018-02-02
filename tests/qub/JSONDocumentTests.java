package qub;

public class JSONDocumentTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JSONDocument", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("constructor", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<String,JSONSegment> constructorTest = new Action2<String, JSONSegment>()
                        {
                            @Override
                            public void run(final String text, final JSONSegment expectedRoot)
                            {
                                runner.test("with \"" + text + "\"", new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final JSONDocument document = JSON.parse(text);
                                        test.assertEqual(expectedRoot, document.getRoot());
                                        test.assertEqual(text, document.toString());
                                        test.assertEqual(text.length(), document.getLength());
                                    }
                                });
                            }
                        };

                        constructorTest.run("", null);
                        constructorTest.run("// hello", null);
                        constructorTest.run("/* there */", null);
                        constructorTest.run("\"fever\"", JSONToken.quotedString("\"fever\"", 0, true));
                        constructorTest.run("20", JSONToken.number("20"));
                        constructorTest.run("[0, 1, 2, 3, 4]", JSON.parseArray("[0, 1, 2, 3, 4]"));
                        constructorTest.run("{}", JSON.parseObject("{}"));
                    }
                });
            }
        });
    }
}
