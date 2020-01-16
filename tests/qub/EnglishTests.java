package qub;

public interface EnglishTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(English.class, () ->
        {
            runner.testGroup("list(Iterable<?>,String)", () ->
            {
                final Action3<Iterable<String>,String,Throwable> listErrorTest = (Iterable<String> values, String listType, Throwable expected) ->
                {
                    runner.test("with " + values + " and " + Strings.escapeAndQuote(listType), (Test test) ->
                    {
                        test.assertThrows(() -> English.list(values, listType), expected);
                    });
                };

                listErrorTest.run(null, "or", new PreConditionFailure("values cannot be null."));
                listErrorTest.run(Iterable.create(), null, new PreConditionFailure("listType cannot be null."));
                listErrorTest.run(Iterable.create(), "", new PreConditionFailure("listType cannot be empty."));
                listErrorTest.run(Iterable.create(), "apples", new PreConditionFailure("listType.toLowerCase() (apples) must be either and or or."));

                final Action3<Iterable<String>,String,String> listTest = (Iterable<String> values, String listType, String expected) ->
                {
                    runner.test("with " + values + " and " + Strings.escapeAndQuote(listType), (Test test) ->
                    {
                        test.assertEqual(expected, English.list(values, listType));
                    });
                };

                listTest.run(Iterable.create(), "or", "");
                listTest.run(Iterable.create("a"), "and", "a");
                listTest.run(Iterable.create("a", "b"), "Or", "a Or b");
                listTest.run(Iterable.create("a", "b", "c"), "AND", "a, b, AND c");
                listTest.run(Iterable.create("a", "b", "c", "d"), "OR", "a, b, c, OR d");
            });

            runner.testGroup("andList(Iterable<?>)", () ->
            {
                final Action2<Iterable<String>,Throwable> andListErrorTest = (Iterable<String> values, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> English.andList(values), expected);
                    });
                };

                andListErrorTest.run(null, new PreConditionFailure("values cannot be null."));

                final Action2<Iterable<String>,String> andListTest = (Iterable<String> values, String expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertEqual(expected, English.andList(values));
                    });
                };

                andListTest.run(Iterable.create(), "");
                andListTest.run(Iterable.create("a"), "a");
                andListTest.run(Iterable.create("a", "b"), "a and b");
                andListTest.run(Iterable.create("a", "b", "c"), "a, b, and c");
                andListTest.run(Iterable.create("a", "b", "c", "d"), "a, b, c, and d");
            });

            runner.testGroup("andList(Object...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    test.assertEqual("", English.andList());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    test.assertEqual("a", English.andList("a"));
                });

                runner.test("with \"a\" and \"b\"", (Test test) ->
                {
                    test.assertEqual("a and b", English.andList("a", "b"));
                });

                runner.test("with \"a\", \"b\", and 5", (Test test) ->
                {
                    test.assertEqual("a, b, and 5", English.andList("a", "b", 5));
                });
            });

            runner.testGroup("orList(Iterable<?>)", () ->
            {
                final Action2<Iterable<String>,Throwable> orListErrorTest = (Iterable<String> values, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> English.orList(values), expected);
                    });
                };

                orListErrorTest.run(null, new PreConditionFailure("values cannot be null."));

                final Action2<Iterable<String>,String> orListTest = (Iterable<String> values, String expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertEqual(expected, English.orList(values));
                    });
                };

                orListTest.run(Iterable.create(), "");
                orListTest.run(Iterable.create("a"), "a");
                orListTest.run(Iterable.create("a", "b"), "a or b");
                orListTest.run(Iterable.create("a", "b", "c"), "a, b, or c");
                orListTest.run(Iterable.create("a", "b", "c", "d"), "a, b, c, or d");
            });

            runner.testGroup("orList(Object...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    test.assertEqual("", English.orList());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    test.assertEqual("a", English.orList("a"));
                });

                runner.test("with \"a\" and \"b\"", (Test test) ->
                {
                    test.assertEqual("a or b", English.orList("a", "b"));
                });

                runner.test("with \"a\", \"b\", and 5", (Test test) ->
                {
                    test.assertEqual("a, b, or 5", English.orList("a", "b", 5));
                });
            });
        });
    }
}
