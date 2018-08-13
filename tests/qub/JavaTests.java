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
                        JavaIssues.missingPackagePathSegment(new Span(0, 7))
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
                        JavaIssues.missingPackagePathSegment(new Span(0, 7))
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
                        JavaIssues.expectedPackagePathIdentifier(new Span(8, 1)),
                        JavaIssues.expectedTypeDefinition(new Span(8, 1))
                    });
                parseTest.run("package qub",
                    new JavaSegment[]
                    {
                        new JavaSegment(JavaSegmentType.Package,
                            Lex.letters("package", 0),
                            Lex.space(7),
                            Lex.letters("qub", 8))
                    },
                    new Issue[]
                    {
                        JavaIssues.missingStatementSemicolon(new Span(8, 3))
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
                parseTest.run("package\nqub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.newLine(7),
                                Lex.letters("qub", 8),
                                Lex.semicolon(11))
                        },
                    null);
                parseTest.run("package my.",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.period(10))
                        },
                    new Issue[]
                        {
                            JavaIssues.missingPackagePathSegment(new Span(10, 1)),
                            JavaIssues.missingStatementSemicolon(new Span(10, 1))
                        });
                parseTest.run("package my.;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.period(10),
                                Lex.semicolon(11))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedPackagePathIdentifier(new Span(11, 1))
                        });
                parseTest.run("package my..;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.period(10),
                                Lex.period(11),
                                Lex.semicolon(12))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedPackagePathIdentifier(new Span(11, 1)),
                            JavaIssues.expectedPackagePathIdentifier(new Span(12, 1))
                        });
                parseTest.run("package my123;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.digits("123", 10),
                                Lex.semicolon(13))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedPackagePathSeparatorOrSemicolon(new Span(10, 3))
                        });
                parseTest.run("package my123qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.digits("123", 10),
                                Lex.letters("qub", 13),
                                Lex.semicolon(16))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedPackagePathSeparatorOrSemicolon(new Span(10, 3)),
                            JavaIssues.expectedPackagePathSeparatorOrSemicolon(new Span(13, 3))
                        });
                parseTest.run("package my qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.space(10),
                                Lex.letters("qub", 11),
                                Lex.semicolon(14))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedPackagePathSeparatorOrSemicolon(new Span(11, 3))
                        });
                parseTest.run("package my.123;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.period(10),
                                Lex.digits("123", 11),
                                Lex.semicolon(14))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedPackagePathIdentifier(new Span(11, 3)),
                            JavaIssues.expectedPackagePathIdentifier(new Span(14, 1))
                        });
                parseTest.run("package my.qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.period(10),
                                Lex.letters("qub", 11),
                                Lex.semicolon(14))
                        },
                    null);
                parseTest.run("package my  .   qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Package,
                                Lex.letters("package", 0),
                                Lex.space(7),
                                Lex.letters("my", 8),
                                Lex.space(10),
                                Lex.space(11),
                                Lex.period(12),
                                Lex.space(13),
                                Lex.space(14),
                                Lex.space(15),
                                Lex.letters("qub", 16),
                                Lex.semicolon(19))
                        },
                    null);

                parseTest.run("import",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0))
                        },
                    new Issue[]
                        {
                            JavaIssues.missingImportPathIdentifier(new Span(0, 6))
                        });
                parseTest.run("import ",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6))
                        },
                    new Issue[]
                        {
                            JavaIssues.missingImportPathIdentifier(new Span(0, 6))
                        });
                parseTest.run("import;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.semicolon(6))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathIdentifier(new Span(6, 1))
                        });
                parseTest.run("import static",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("static", 7))
                        },
                    new Issue[]
                        {
                            JavaIssues.missingImportPathIdentifier(new Span(7, 6)),
                            JavaIssues.missingStatementSemicolon(new Span(7, 6))
                        });
                parseTest.run("import toads",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("toads", 7))
                        },
                    new Issue[]
                        {
                            JavaIssues.missingStatementSemicolon(new Span(7, 5))
                        });
                parseTest.run("import toads;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("toads", 7),
                                Lex.semicolon(12))
                        },
                    null);
                parseTest.run("import toads.;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("toads", 7),
                                Lex.period(12),
                                Lex.semicolon(13))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathIdentifier(new Span(13, 1))
                        });
                parseTest.run("import .;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.period(7),
                                Lex.semicolon(8))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathIdentifier(new Span(7, 1)),
                            JavaIssues.expectedImportPathIdentifier(new Span(8, 1))
                        });
                parseTest.run("import my qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.space(9),
                                Lex.letters("qub", 10),
                                Lex.semicolon(13))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathSeparatorOrSemicolon(new Span(10, 3))
                        });
                parseTest.run("import my123qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.digits("123", 9),
                                Lex.letters("qub", 12),
                                Lex.semicolon(15))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathSeparatorOrSemicolon(new Span(9, 3)),
                            JavaIssues.expectedImportPathSeparatorOrSemicolon(new Span(12, 3))
                        });
                parseTest.run("import my.123;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.digits("123", 10),
                                Lex.semicolon(13))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathIdentifier(new Span(10, 3)),
                            JavaIssues.expectedImportPathIdentifier(new Span(13, 1))
                        });
                parseTest.run("import my.qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.Import,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.letters("qub", 10),
                                Lex.semicolon(13))
                        },
                    null);
                parseTest.run("import static my.qub;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("static", 7),
                                Lex.space(13),
                                Lex.letters("my", 14),
                                Lex.period(16),
                                Lex.letters("qub", 17),
                                Lex.semicolon(20))
                        },
                    null);
                parseTest.run("import my.*;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.asterisk(10),
                                Lex.semicolon(11))
                        },
                    null);
                parseTest.run("import static my.*;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("static", 7),
                                Lex.space(13),
                                Lex.letters("my", 14),
                                Lex.period(16),
                                Lex.asterisk(17),
                                Lex.semicolon(18))
                        },
                    null);
                parseTest.run("import my.*",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.asterisk(10))
                        },
                    new Issue[]
                        {
                            JavaIssues.missingStatementSemicolon(new Span(10, 1))
                        });
                parseTest.run("import my.*.",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.asterisk(10),
                                Lex.period(11))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedStatementSemicolon(new Span(11, 1))
                        });
                parseTest.run("import my.*.abc",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.asterisk(10),
                                Lex.period(11),
                                Lex.letters("abc", 12))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedStatementSemicolon(new Span(11, 1)),
                            JavaIssues.expectedStatementSemicolon(new Span(12, 3))
                        });
                parseTest.run("import my.*abc",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.period(9),
                                Lex.asterisk(10),
                                Lex.letters("abc", 11))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedStatementSemicolon(new Span(11, 3))
                        });
                parseTest.run("import my*;",
                    new JavaSegment[]
                        {
                            new JavaSegment(JavaSegmentType.StaticImport,
                                Lex.letters("import", 0),
                                Lex.space(6),
                                Lex.letters("my", 7),
                                Lex.asterisk(9),
                                Lex.semicolon(10))
                        },
                    new Issue[]
                        {
                            JavaIssues.expectedImportPathSeparatorOrSemicolon(new Span(9, 1))
                        });
            });
        });
    }
}
