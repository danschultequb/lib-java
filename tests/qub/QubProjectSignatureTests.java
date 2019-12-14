package qub;

public interface QubProjectSignatureTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(QubProjectSignature.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with null publisher", (Test test) ->
                {
                    test.assertThrows(() -> new QubProjectSignature(null, "b", "c"),
                        new PreConditionFailure("publisher cannot be null."));
                });

                runner.test("with empty publisher", (Test test) ->
                {
                    test.assertThrows(() -> new QubProjectSignature("", "b", "c"),
                        new PreConditionFailure("publisher cannot be empty."));
                });

                runner.test("with null project", (Test test) ->
                {
                    test.assertThrows(() -> new QubProjectSignature("a", null, "c"),
                        new PreConditionFailure("project cannot be null."));
                });

                runner.test("with empty project", (Test test) ->
                {
                    test.assertThrows(() -> new QubProjectSignature("a", "", "c"),
                        new PreConditionFailure("project cannot be empty."));
                });

                runner.test("with null version", (Test test) ->
                {
                    test.assertThrows(() -> new QubProjectSignature("a", "b", null),
                        new PreConditionFailure("version cannot be null."));
                });

                runner.test("with empty version", (Test test) ->
                {
                    test.assertThrows(() -> new QubProjectSignature("a", "b", ""),
                        new PreConditionFailure("version cannot be empty."));
                });

                runner.test("with all non-empty values", (Test test) ->
                {
                    final QubProjectSignature signature = new QubProjectSignature("a", "b", "c");
                    test.assertEqual("a", signature.getPublisher());
                    test.assertEqual("b", signature.getProject());
                    test.assertEqual("c", signature.getVersion());
                    test.assertEqual("a/b:c", signature.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<QubProjectSignature,Object,Boolean> equalsTest = (QubProjectSignature signature, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + signature + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, signature.equals(rhs));
                    });
                };

                equalsTest.run(new QubProjectSignature("a", "b", "c"), null, false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), "hello", false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("z", "b", "c"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("a", "y", "c"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("a", "b", "x"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("A", "B", "C"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("a", "b", "c"), true);
            });

            runner.testGroup("equals(QubProjectSignature)", () ->
            {
                final Action3<QubProjectSignature,QubProjectSignature,Boolean> equalsTest = (QubProjectSignature signature, QubProjectSignature rhs, Boolean expected) ->
                {
                    runner.test("with " + signature + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, signature.equals(rhs));
                    });
                };

                equalsTest.run(new QubProjectSignature("a", "b", "c"), null, false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("z", "b", "c"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("a", "y", "c"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("a", "b", "x"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("A", "B", "C"), false);
                equalsTest.run(new QubProjectSignature("a", "b", "c"), new QubProjectSignature("a", "b", "c"), true);
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> QubProjectSignature.parse(text).await(),
                            expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("", new PreConditionFailure("text cannot be empty."));
                parseErrorTest.run("a", new ParseException("No project found in \"a\"."));
                parseErrorTest.run("/", new ParseException("No publisher found in \"/\"."));
                parseErrorTest.run("a/", new ParseException("No project found in \"a/\"."));
                parseErrorTest.run("a/b", new ParseException("No version found in \"a/b\"."));
                parseErrorTest.run("a/:", new ParseException("No project found in \"a/:\"."));
                parseErrorTest.run("a/b:", new ParseException("No version found in \"a/b:\"."));

                final Action2<String,QubProjectSignature> parseTest = (String text, QubProjectSignature expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, QubProjectSignature.parse(text).await());
                    });
                };

                parseTest.run("a/b:c", new QubProjectSignature("a", "b", "c"));
                parseTest.run("qub/qub-java:123", new QubProjectSignature("qub", "qub-java", "123"));
            });
        });
    }
}
