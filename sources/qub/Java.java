package qub;

public class Java
{
    Java()
    {
    }

    private static void addIssue(Action1<Issue> onIssue, Issue toAdd)
    {
        if (onIssue != null)
        {
            onIssue.run(toAdd);
        }
    }

    public static JavaDocument parse(String text, Action1<Issue> onIssue)
    {
        final Lexer lexer = new Lexer(text);
        lexer.ensureHasStarted();

        final List<JavaSegment> segments = new ArrayList<>();

        while (lexer.hasCurrent())
        {
            final JavaSegment segment = parseSegment(lexer, onIssue);
            if (segment.getType() == JavaSegmentType.Unrecognized)
            {
                if (!segments.any())
                {
                    addIssue(onIssue, JavaIssues.expectedPackageOrTypeDefinition(segment.getSpan()));
                }
                else
                {
                    addIssue(onIssue, JavaIssues.expectedTypeDefinition(segment.getSpan()));
                }
            }
            segments.add(segment);
        }

        final JavaDocument result = new JavaDocument(segments);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private static JavaSegment parseSegment(Lexer lexer, Action1<Issue> onIssue)
    {
        PreCondition.assertNotNull(lexer, "lexer");
        PreCondition.assertTrue(lexer.hasCurrent(), "lexer.hasCurrent()");

        JavaSegment result;
        switch (lexer.getCurrent().getType())
        {
            case Letters:
                if (lexer.getCurrent().toString().equals("package"))
                {
                    result = parsePackage(lexer, onIssue);
                }
                else
                {
                    final Lex unrecognizedSegmentLex = lexer.takeCurrent();
                    result = new JavaSegment(JavaSegmentType.Unrecognized, unrecognizedSegmentLex);
                }
                break;

            default:
                if (isWhitespace(lexer.getCurrent()))
                {
                    final List<Lex> whitespaceLexes = new ArrayList<>();
                    skipWhitespace(lexer, whitespaceLexes);
                    result = new JavaSegment(JavaSegmentType.Whitespace, whitespaceLexes);
                }
                else
                {
                    final Lex unrecognizedSegmentLex = lexer.takeCurrent();
                    result = new JavaSegment(JavaSegmentType.Unrecognized, unrecognizedSegmentLex);
                }
                break;
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertNotNullAndNotEmpty(result.getLexes(), "result.getLexes()");

        return result;
    }

    private static JavaSegment parsePackage(Lexer lexer, Action1<Issue> onIssue)
    {
        PreCondition.assertNotNull(lexer, "lexer");
        PreCondition.assertTrue(lexer.hasCurrent(), "lexer.hasCurrent()");
        PreCondition.assertEqual("package", lexer.getCurrent().toString(), "lexer.getCurrent().toString()");

        final Lex packageLex = lexer.takeCurrent();

        final List<Lex> lexes = new ArrayList<>();
        lexes.add(packageLex);

        if (!lexer.hasCurrent())
        {
            addIssue(onIssue, JavaIssues.missingPackagePathSegment(packageLex.getSpan()));
        }
        else if (!isWhitespace(lexer.getCurrent()))
        {
            addIssue(onIssue, JavaIssues.expectedWhitespaceBetweenPackageAndPackagePath(lexer.getCurrent().getSpan()));
        }
        else
        {
            skipWhitespace(lexer, lexes);
            if (!lexer.hasCurrent())
            {
                addIssue(onIssue, JavaIssues.missingPackagePathSegment(packageLex.getSpan()));
            }
            else if (lexer.getCurrent().getType() != LexType.Letters)
            {
                addIssue(onIssue, JavaIssues.expectedPackagePathIdentifier(lexer.getCurrent().getSpan()));
            }
            else
            {
                lexes.add(lexer.takeCurrent());

                boolean expectedIdentifier = false;
                while (true)
                {
                    skipWhitespace(lexer, lexes);

                    if (!lexer.hasCurrent())
                    {
                        final Span lastNonWhitespaceLexSpan = lexes.last(isNotWhitespace).getSpan();
                        if (expectedIdentifier)
                        {
                            addIssue(onIssue, JavaIssues.missingPackagePathSegment(lastNonWhitespaceLexSpan));
                        }
                        addIssue(onIssue, JavaIssues.missingStatementSemicolon(lastNonWhitespaceLexSpan));
                        break;
                    }
                    else
                    {
                        final LexType type = lexer.getCurrent().getType();
                        final Span span = lexer.getCurrent().getSpan();
                        lexes.add(lexer.takeCurrent());

                        if (type == LexType.Period)
                        {
                            if (expectedIdentifier)
                            {
                                addIssue(onIssue, JavaIssues.expectedPackagePathIdentifier(span));
                            }
                            expectedIdentifier = true;
                        }
                        else if (type == LexType.Letters)
                        {
                            if (!expectedIdentifier)
                            {
                                addIssue(onIssue, JavaIssues.expectedPackagePathSeparatorOrSemicolon(span));
                            }
                            expectedIdentifier = false;
                        }
                        else if (type == LexType.Semicolon)
                        {
                            if (expectedIdentifier)
                            {
                                addIssue(onIssue, JavaIssues.expectedPackagePathIdentifier(span));
                            }
                            break;
                        }
                    }
                }
            }
        }

        final JavaSegment result = new JavaSegment(JavaSegmentType.Package, lexes);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(JavaSegmentType.Package, result.getType(), "result.getType()");
        PostCondition.assertNotNullAndNotEmpty(result.getLexes(), "result.getLexes()");
        PostCondition.assertEqual("package", result.getLexes().first().toString(), "result.getLexes().first().toString()");

        return result;
    }

    private static boolean isWhitespace(Lex lex)
    {
        return lex.isWhitespace() || lex.isNewLine();
    }

    private static final Function1<Lex,Boolean> isNotWhitespace = new Function1<Lex,Boolean>()
    {
        @Override
        public Boolean run(Lex lex)
        {
            return !isWhitespace(lex);
        }
    };

    private static void skipWhitespace(Lexer lexer, List<Lex> lexes)
    {
        while (lexer.hasCurrent() && isWhitespace(lexer.getCurrent()))
        {
            lexes.add(lexer.takeCurrent());
        }
    }
}
