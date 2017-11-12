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

        public static Token trueToken(int startIndex)
        {
            return new Token("true", startIndex, Type.True);
        }

        public static Token falseToken(int startIndex)
        {
            return new Token("false", startIndex, Type.False);
        }

        public static Token nullToken(int startIndex)
        {
            return new Token("null", startIndex, Type.Null);
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
        private final List<Issue> issues;
        private boolean hasStarted;
        private Token current;

        public Tokenizer(String text)
        {
            this(new StringIterator(text));
        }

        public Tokenizer(Iterator<Character> characters)
        {
            this(new Lexer(characters));
        }

        public Tokenizer(Lexer lexer)
        {
            this.lexer = lexer;
            this.issues = null;
        }

        private void addIssue(Issue issue)
        {
            if (issues != null)
            {
                issues.add(issue);
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
                        switch (lexer.getCurrent().toString())
                        {
                            case "true":
                                current = Token.trueToken(tokenStartIndex);
                                break;

                            case "false":
                                current = Token.falseToken(tokenStartIndex);
                                break;

                            case "null":
                                current = Token.nullToken(tokenStartIndex);
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
                                addIssue(Issues.expectedFractionalNumberDigits(decimalPointStartIndex, lexer.getCurrent().getLength()));
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
                                addIssue(Issues.shouldBeLowercasedE(eStartIndex, 1));
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
                }
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

        public static Issue shouldBeLowercasedE(int startIndex, int length)
        {
            return Issue.error("'E' should be 'e'.", startIndex, length);
        }

        public static Issue missingExponentNumberDigits(int startIndex, int length)
        {
            return Issue.error("Missing exponent number digits.", startIndex, length);
        }

        public static Issue expectedExponentNumberDigits(int startIndex, int length)
        {
            return Issue.error("Expected exponent number digits.", startIndex, length);
        }
    }
}
