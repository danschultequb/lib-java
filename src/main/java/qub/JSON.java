package qub;

public class JSON
{
    public static abstract class Segment
    {
        public abstract String toString();

        public abstract int getStartIndex();

        public abstract int getLength();

        public abstract int getAfterEndIndex();

        public Span getSpan()
        {
            return new Span(getStartIndex(), getLength());
        }
    }

    public static class Token extends Segment
    {
        private final String text;
        private final int startIndex;
        private final Type type;

        public Token(String text, int startIndex, Type type)
        {
            this.text = text;
            this.startIndex = startIndex;
            this.type = type;
        }

        @Override
        public String toString()
        {
            return text;
        }

        @Override
        public int getStartIndex()
        {
            return startIndex;
        }

        @Override
        public int getLength()
        {
            return text == null ? 0 : text.length();
        }

        @Override
        public int getAfterEndIndex()
        {
            return getStartIndex() + getLength();
        }

        public Type getType()
        {
            return type;
        }

        @Override
        public boolean equals(Object rhs)
        {
            return rhs instanceof Token && equals((Token)rhs);
        }

        public boolean equals(Token rhs)
        {
            return rhs != null &&
                    text.equals(rhs.text) &&
                    startIndex == rhs.startIndex &&
                    type == rhs.type;
        }

        public enum Type
        {
            LeftCurlyBracket,
            RightCurlyBracket,
            LeftSquareBracket,
            RightSquareBracket,
            Colon,
            Comma,
            True,
            False,
            Null,
            NewLine,
            QuotedString,
            Number,
            Whitespace,
            LineComment,
            BlockComment,
            Unrecognized
        }

        public static Token leftCurlyBracket(int startIndex)
        {
            return new Token("{", startIndex, Type.LeftCurlyBracket);
        }

        public static Token rightCurlyBracket(int startIndex)
        {
            return new Token("}", startIndex, Type.RightCurlyBracket);
        }

        public static Token leftSquareBracket(int startIndex)
        {
            return new Token("[", startIndex, Type.LeftSquareBracket);
        }

        public static Token rightSquareBracket(int startIndex)
        {
            return new Token("]", startIndex, Type.RightSquareBracket);
        }

        public static Token colon(int startIndex)
        {
            return new Token(":", startIndex, Type.Colon);
        }

        public static Token comma(int startIndex)
        {
            return new Token(",", startIndex, Type.Comma);
        }

        public static Token trueToken(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.True);
        }

        public static Token falseToken(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.False);
        }

        public static Token nullToken(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.Null);
        }

        public static Token newLine(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.NewLine);
        }

        public static CloseableToken quotedString(String text, int startIndex, boolean closed)
        {
            return new CloseableToken(text, startIndex, Type.QuotedString, closed);
        }

        public static Token number(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.Number);
        }

        public static Token whitespace(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.Whitespace);
        }

        public static Token lineComment(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.LineComment);
        }

        public static CloseableToken blockComment(String text, int startIndex, boolean closed)
        {
            return new CloseableToken(text, startIndex, Type.BlockComment, closed);
        }

        public static Token unrecognized(String text, int startIndex)
        {
            return new Token(text, startIndex, Type.Unrecognized);
        }
    }

    public static class CloseableToken extends Token
    {
        private final boolean closed;

        public CloseableToken(String text, int startIndex, Type type, boolean closed)
        {
            super(text, startIndex, type);

            this.closed = closed;
        }

        public boolean isClosed()
        {
            return closed;
        }
    }

    public static class Tokenizer extends IteratorBase<Token>
    {
        private final Lexer lexer;
        private final Action1<Issue> onIssue;
        private boolean hasStarted;
        private Token current;

        public Tokenizer(String text)
        {
            this(text, (List<Issue>)null);
        }

        public Tokenizer(String text, final List<Issue> issues)
        {
            this(text, issues == null ? null : new Action1<Issue>()
            {
                @Override
                public void run(Issue issue)
                {
                    issues.add(issue);
                }
            });
        }

        public Tokenizer(String text, Action1<Issue> onIssue)
        {
            this(new StringIterator(text), onIssue);
        }

        public Tokenizer(Iterator<Character> characters, Action1<Issue> onIssue)
        {
            this(new Lexer(characters), onIssue);
        }

        public Tokenizer(Lexer lexer, Action1<Issue> onIssue)
        {
            this.lexer = lexer;
            this.onIssue = onIssue;
        }

        private void addIssue(Issue issue)
        {
            if (onIssue != null)
            {
                onIssue.run(issue);
            }
        }

        @Override
        public boolean hasStarted()
        {
            return hasStarted;
        }

        @Override
        public boolean hasCurrent()
        {
            return current != null;
        }

        @Override
        public Token getCurrent()
        {
            return current;
        }

        @Override
        public boolean next()
        {
            if (!hasStarted)
            {
                hasStarted = true;

                if (!lexer.hasStarted())
                {
                    lexer.next();
                }
            }

            if (lexer.hasCurrent())
            {
                int tokenStartIndex = hasCurrent() ? getCurrent().getAfterEndIndex() : 0;
                switch (lexer.getCurrent().getType())
                {
                    case LeftCurlyBracket:
                        current = Token.leftCurlyBracket(tokenStartIndex);
                        lexer.next();
                        break;

                    case RightCurlyBracket:
                        current = Token.rightCurlyBracket(tokenStartIndex);
                        lexer.next();
                        break;

                    case LeftSquareBracket:
                        current = Token.leftSquareBracket(tokenStartIndex);
                        lexer.next();
                        break;

                    case RightSquareBracket:
                        current = Token.rightSquareBracket(tokenStartIndex);
                        lexer.next();
                        break;

                    case Colon:
                        current = Token.colon(tokenStartIndex);
                        lexer.next();
                        break;

                    case Comma:
                        current = Token.comma(tokenStartIndex);
                        lexer.next();
                        break;

                    case Letters:
                        switch (lexer.getCurrent().toString().toLowerCase())
                        {
                            case "true":
                                if (!lexer.getCurrent().toString().equals("true"))
                                {
                                    addIssue(Issues.shouldBeLowercased(tokenStartIndex, 4));
                                }
                                current = Token.trueToken(lexer.getCurrent().toString(), tokenStartIndex);
                                break;

                            case "false":
                                if (!lexer.getCurrent().toString().equals("false"))
                                {
                                    addIssue(Issues.shouldBeLowercased(tokenStartIndex, 5));
                                }
                                current = Token.falseToken(lexer.getCurrent().toString(), tokenStartIndex);
                                break;

                            case "null":
                                if (!lexer.getCurrent().toString().equals("null"))
                                {
                                    addIssue(Issues.shouldBeLowercased(tokenStartIndex, 4));
                                }
                                current = Token.nullToken(lexer.getCurrent().toString(), tokenStartIndex);
                                break;

                            default:
                                current = Token.unrecognized(lexer.getCurrent().toString(), tokenStartIndex);
                                break;
                        }
                        lexer.next();
                        break;

                    case SingleQuote:
                    case DoubleQuote:
                        final Lex startQuote = lexer.takeCurrent();
                        final StringBuilder quotedStringText = new StringBuilder(startQuote.toString());

                        boolean foundEndQuote = false;
                        boolean escaped = false;

                        while (!foundEndQuote && lexer.hasCurrent())
                        {
                            quotedStringText.append(lexer.getCurrent().toString());
                            if (escaped)
                            {
                                escaped = false;
                            }
                            else if (lexer.getCurrent().getType() == Lex.Type.Backslash)
                            {
                                escaped = true;
                            }
                            else if (lexer.getCurrent().getType() == startQuote.getType())
                            {
                                foundEndQuote = true;
                            }
                            lexer.next();
                        }

                        if (!foundEndQuote) {
                            addIssue(Issues.missingEndQuote(startQuote.toString(), tokenStartIndex, quotedStringText.length()));
                        }

                        current = Token.quotedString(quotedStringText.toString(), tokenStartIndex, foundEndQuote);
                        break;

                    case Space:
                    case Tab:
                    case CarriageReturn:
                        final StringBuilder whitespaceText = new StringBuilder(lexer.takeCurrent().toString());
                        while (lexer.hasCurrent() && lexer.getCurrent().isWhitespace())
                        {
                            whitespaceText.append(lexer.takeCurrent().toString());
                        }
                        current = Token.whitespace(whitespaceText.toString(), tokenStartIndex);
                        break;

                    case NewLine:
                    case CarriageReturnNewLine:
                        current = Token.newLine(lexer.takeCurrent().toString(), tokenStartIndex);
                        break;

                    case Dash:
                    case Digits:
                    case Period:
                        final StringBuilder numberText = new StringBuilder();

                        if (lexer.getCurrent().getType() == Lex.Type.Dash)
                        {
                            // Negative sign
                            numberText.append(lexer.takeCurrent().toString());
                        }

                        if (!lexer.hasCurrent())
                        {
                            addIssue(Issues.missingWholeNumberDigits(tokenStartIndex, numberText.length()));
                        }
                        else if (lexer.getCurrent().getType() != Lex.Type.Digits)
                        {
                            addIssue(Issues.expectedWholeNumberDigits(tokenStartIndex + numberText.length(), lexer.getCurrent().getLength()));
                        }
                        else
                        {
                            numberText.append(lexer.takeCurrent().toString());
                        }

                        if (lexer.hasCurrent() && lexer.getCurrent().getType() == Lex.Type.Period)
                        {
                            // Decimal point
                            final int decimalPointStartIndex = tokenStartIndex + numberText.length();
                            numberText.append(lexer.takeCurrent().toString());

                            if (!lexer.hasCurrent())
                            {
                                addIssue(Issues.missingFractionalNumberDigits(decimalPointStartIndex, 1));
                            }
                            else if (lexer.getCurrent().getType() != Lex.Type.Digits)
                            {
                                addIssue(Issues.expectedFractionalNumberDigits(lexer.getCurrent().getStartIndex(), lexer.getCurrent().getLength()));
                            }
                            else
                            {
                                // Fractional number digits
                                numberText.append(lexer.takeCurrent().toString());
                            }
                        }

                        if (lexer.hasCurrent() && lexer.getCurrent().toString().equalsIgnoreCase("e"))
                        {
                            // e
                            final int eStartIndex = tokenStartIndex + numberText.length();
                            if (lexer.getCurrent().toString().equals("E"))
                            {
                                addIssue(Issues.shouldBeLowercased(eStartIndex, 1));
                            }
                            numberText.append(lexer.takeCurrent().toString());

                            if (!lexer.hasCurrent())
                            {
                                addIssue(Issues.missingExponentNumberDigits(eStartIndex, 1));
                            }
                            else
                            {
                                final int exponentSignOrDigitsStartIndex = tokenStartIndex + numberText.length();
                                if (lexer.getCurrent().getType() == Lex.Type.Dash || lexer.getCurrent().getType() == Lex.Type.Plus)
                                {
                                    // Exponent number sign
                                    numberText.append(lexer.takeCurrent().toString());
                                }

                                if (!lexer.hasCurrent())
                                {
                                    addIssue(Issues.missingExponentNumberDigits(exponentSignOrDigitsStartIndex, 1));
                                }
                                else if (lexer.getCurrent().getType() != Lex.Type.Digits)
                                {
                                    addIssue(Issues.expectedExponentNumberDigits(tokenStartIndex + numberText.length(), lexer.getCurrent().getLength()));
                                }
                                else
                                {
                                    // Exponent number digits
                                    numberText.append(lexer.takeCurrent().toString());
                                }
                            }
                        }

                        current = Token.number(numberText.toString(), tokenStartIndex);
                        break;

                    case ForwardSlash:
                        final StringBuilder commentText = new StringBuilder(lexer.takeCurrent().toString());
                        if (!lexer.hasCurrent())
                        {
                            addIssue(Issues.missingCommentSlashOrAsterisk(tokenStartIndex, 1));
                            current = Token.unrecognized(commentText.toString(), tokenStartIndex);
                        }
                        else
                        {
                            switch (lexer.getCurrent().getType())
                            {
                                case ForwardSlash:
                                    do
                                    {
                                        commentText.append(lexer.takeCurrent().toString());
                                    }
                                    while (lexer.hasCurrent() && !lexer.getCurrent().isNewLine());
                                    current = Token.lineComment(commentText.toString(), tokenStartIndex);
                                    break;

                                case Asterisk:
                                    commentText.append(lexer.takeCurrent().toString());
                                    boolean previousCharacterWasAsterisk = false;
                                    boolean commentClosed = false;
                                    while (lexer.hasCurrent() && !commentClosed)
                                    {
                                        switch (lexer.getCurrent().getType())
                                        {
                                            case Asterisk:
                                                previousCharacterWasAsterisk = true;
                                                break;

                                            case ForwardSlash:
                                                commentClosed = previousCharacterWasAsterisk;
                                                break;

                                            default:
                                                previousCharacterWasAsterisk = false;
                                                break;
                                        }
                                        commentText.append(lexer.takeCurrent().toString());
                                    }
                                    current = Token.blockComment(commentText.toString(), tokenStartIndex, commentClosed);
                                    break;

                                default:
                                    addIssue(Issues.expectedCommentSlashOrAsterisk(lexer.getCurrent().getStartIndex(), lexer.getCurrent().getLength()));
                                    current = Token.unrecognized(commentText.toString(), tokenStartIndex);
                                    break;
                            }
                        }

                        break;

                    default:
                        current = Token.unrecognized(lexer.takeCurrent().toString(), tokenStartIndex);
                        break;
                }
            }
            else
            {
                current = null;
            }

            return hasCurrent();
        }
    }

    public static class Issues
    {
        public static Issue missingEndQuote(String quote, int startIndex, int length)
        {
            return Issue.error("Missing end quote (" + quote + ").", startIndex, length);
        }

        public static Issue missingWholeNumberDigits(int startIndex, int length)
        {
            return Issue.error("Missing whole number digits.", startIndex, length);
        }

        public static Issue expectedWholeNumberDigits(int startIndex, int length)
        {
            return Issue.error("Expected whole number digits.", startIndex, length);
        }

        public static Issue missingFractionalNumberDigits(int startIndex, int length)
        {
            return Issue.error("Missing fractional number digits.", startIndex, length);
        }

        public static Issue expectedFractionalNumberDigits(int startIndex, int length)
        {
            return Issue.error("Expected fractional number digits.", startIndex, length);
        }

        public static Issue shouldBeLowercased(int startIndex, int length)
        {
            return Issue.error("Should be lowercased.", startIndex, length);
        }

        public static Issue missingExponentNumberDigits(int startIndex, int length)
        {
            return Issue.error("Missing exponent number digits.", startIndex, length);
        }

        public static Issue expectedExponentNumberDigits(int startIndex, int length)
        {
            return Issue.error("Expected exponent number digits.", startIndex, length);
        }

        public static Issue missingCommentSlashOrAsterisk(int startIndex, int length)
        {
            return Issue.error("Missing comment forward slash ('/') or asterisk ('*').", startIndex, length);
        }

        public static Issue expectedCommentSlashOrAsterisk(int startIndex, int length)
        {
            return Issue.error("Expected comment forward slash ('/') or asterisk ('*').", startIndex, length);
        }
    }
}
