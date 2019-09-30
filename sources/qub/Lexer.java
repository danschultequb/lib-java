package qub;

public class Lexer implements Iterator<Lex>
{
    private final Iterator<Character> characters;
    private boolean hasStarted;
    private Lex current;

    public Lexer(String text)
    {
        this(new StringIterator(text));
    }

    public Lexer(Iterator<Character> characters)
    {
        this.characters = characters;
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
    public Lex getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        int lexStartIndex = 0;
        if (!hasStarted)
        {
            hasStarted = true;
            if (!characters.hasStarted())
            {
                characters.next();
            }
        }
        else if (current != null)
        {
            lexStartIndex = current.getAfterEndIndex();
        }

        if (!characters.hasCurrent())
        {
            current = null;
        }
        else
        {
            switch (characters.getCurrent())
            {
                case '{':
                    current = Lex.leftCurlyBracket(lexStartIndex);
                    characters.next();
                    break;

                case '}':
                    current = Lex.rightCurlyBracket(lexStartIndex);
                    characters.next();
                    break;

                case '[':
                    current = Lex.leftSquareBracket(lexStartIndex);
                    characters.next();
                    break;

                case ']':
                    current = Lex.rightSquareBracket(lexStartIndex);
                    characters.next();
                    break;

                case '(':
                    current = Lex.leftParenthesis(lexStartIndex);
                    characters.next();
                    break;

                case ')':
                    current = Lex.rightParenthesis(lexStartIndex);
                    characters.next();
                    break;

                case '<':
                    current = Lex.leftAngleBracket(lexStartIndex);
                    characters.next();
                    break;

                case '>':
                    current = Lex.rightAngleBracket(lexStartIndex);
                    characters.next();
                    break;

                case '\"':
                    current = Lex.doubleQuote(lexStartIndex);
                    characters.next();
                    break;

                case '\'':
                    current = Lex.singleQuote(lexStartIndex);
                    characters.next();
                    break;

                case '-':
                    current = Lex.dash(lexStartIndex);
                    characters.next();
                    break;

                case '+':
                    current = Lex.plus(lexStartIndex);
                    characters.next();
                    break;

                case ',':
                    current = Lex.comma(lexStartIndex);
                    characters.next();
                    break;

                case ':':
                    current = Lex.colon(lexStartIndex);
                    characters.next();
                    break;

                case ';':
                    current = Lex.semicolon(lexStartIndex);
                    characters.next();
                    break;

                case '!':
                    current = Lex.exclamationPoint(lexStartIndex);
                    characters.next();
                    break;

                case '\\':
                    current = Lex.backslash(lexStartIndex);
                    characters.next();
                    break;

                case '/':
                    current = Lex.forwardSlash(lexStartIndex);
                    characters.next();
                    break;

                case '?':
                    current = Lex.questionMark(lexStartIndex);
                    characters.next();
                    break;

                case '=':
                    current = Lex.equalsSign(lexStartIndex);
                    characters.next();
                    break;

                case '.':
                    current = Lex.period(lexStartIndex);
                    characters.next();
                    break;

                case '_':
                    current = Lex.underscore(lexStartIndex);
                    characters.next();
                    break;

                case '&':
                    current = Lex.ampersand(lexStartIndex);
                    characters.next();
                    break;

                case ' ':
                    current = Lex.space(lexStartIndex);
                    characters.next();
                    break;

                case '\t':
                    current = Lex.tab(lexStartIndex);
                    characters.next();
                    break;

                case '\r':
                    if(!characters.next() || characters.getCurrent() != '\n')
                    {
                        current = Lex.carriageReturn(lexStartIndex);
                    }
                    else
                    {
                        current = Lex.carriageReturnNewLine(lexStartIndex);
                        characters.next();
                    }
                    break;

                case '\n':
                    current = Lex.newLine(lexStartIndex);
                    characters.next();
                    break;

                case '*':
                    current = Lex.asterisk(lexStartIndex);
                    characters.next();
                    break;

                case '%':
                    current = Lex.percent(lexStartIndex);
                    characters.next();
                    break;

                case '|':
                    current = Lex.verticalBar(lexStartIndex);
                    characters.next();
                    break;

                case '#':
                    current = Lex.hash(lexStartIndex);
                    characters.next();
                    break;

                default:
                    if(Lex.isLetter(characters.getCurrent()))
                    {
                        current = Lex.letters(readWhile(characters, Lex::isLetter), lexStartIndex);
                    }
                    else if(Lex.isDigit(characters.getCurrent()))
                    {
                        current = Lex.digits(readWhile(characters, Lex::isDigit), lexStartIndex);
                    }
                    else
                    {
                        current = Lex.unrecognized(characters.getCurrent(), lexStartIndex);
                        characters.next();
                    }
                    break;
            }
        }

        return hasCurrent();
    }

    private static String readWhile(Iterator<Character> characters, Function1<Character,Boolean> condition)
    {
        final StringBuilder result = new StringBuilder();
        result.append(characters.getCurrent().charValue());

        while (characters.next() && condition.run(characters.getCurrent())) {
            result.append(characters.getCurrent().charValue());
        }

        return result.toString();
    }
}
