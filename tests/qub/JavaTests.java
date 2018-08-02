package qub;

public class JavaTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Java.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Java parser = new Java();
                test.assertNotNull(parser);
            });

            runner.testGroup("parse(String,Action1<Issue>)", () ->
            {
                final Action3<String,JavaSegment[],Issue[]> parseTest = (String text, JavaSegment[] expectedSegments, Issue[] expectedIssues) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " text and no issues", (Test test) ->
                    {
                        final JavaDocument document = Java.parse(text, null);
                        test.assertNotNull(document);
                        test.assertEqual(Array.fromValues(expectedSegments), document.getSegments());
                    });

                    runner.test("with " + Strings.escapeAndQuote(text) + " text and issues", (Test test) ->
                    {
                        final List<Issue> issues = new ArrayList<>();
                        final JavaDocument document = Java.parse(text, issues::add);
                        test.assertNotNull(document);

                        test.assertEqual(Array.fromValues(expectedSegments), document.getSegments());
                        test.assertEqual(Array.fromValues(expectedIssues), issues);
                    });
                };

                parseTest.run(null, null, null);
                parseTest.run("", null, null);
                parseTest.run("    ",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Whitespace,
                            Lex.space(0),
                            Lex.space(1),
                            Lex.space(2),
                            Lex.space(3))
                    },
                    null);
                parseTest.run("\n",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Whitespace, Lex.newLine(0))
                    },
                    null);

                parseTest.run("abc",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Unrecognized, Lex.letters("abc", 0))
                    },
                    new Issue[]
                    {
                        JavaIssues.expectedPackageOrTypeDefinition(new Span(0, 3))
                    });

                parseTest.run("package",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Package, Lex.letters("package", 0))
                    },
                    new Issue[]
                    {
                        JavaIssues.missingPackagePath(new Span(0, 7))
                    });
                parseTest.run("package123",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Package, Lex.letters("package", 0)),
                        new JavaSegment(JavaSegmentType.Unrecognized, Lex.digits("123", 7))
                    },
                    new Issue[]
                    {
                        JavaIssues.expectedWhitespaceBetweenPackageAndPackagePath(new Span(7, 3)),
                        JavaIssues.expectedTypeDefinition(new Span(7, 3))
                    });
                parseTest.run("package ",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Package,
                            Lex.letters("package", 0),
                            Lex.space(7))
                    },
                    new Issue[]
                    {
                        JavaIssues.missingPackagePath(new Span(0, 7))
                    });
                parseTest.run("package 0",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Package,
                            Lex.letters("package", 0),
                            Lex.space(7)),
                        new JavaSegment(JavaSegmentType.Unrecognized, Lex.digits("0", 8))
                    },
                    new Issue[]
                    {
                        JavaIssues.expectedPackagePathLetters(new Span(8, 1)),
                        JavaIssues.expectedTypeDefinition(new Span(8, 1))
                    });
                parseTest.run("package qub;",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Package,
                            Lex.letters("package", 0),
                            Lex.space(7),
                            Lex.letters("qub", 8),
                            Lex.semicolon(11))
                    },
                    null);
            });
        });
    }
}
