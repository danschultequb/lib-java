package qub;

public class Lexer extends IteratorBase<Lex>
{
    private final Iterator<Character> characters;
    private int currentCharacterStartIndex;
    private boolean hasStarted;
    private Lex currentLex;

    public Lexer(String text)
    {
        this(new StringIterator(text));
    }

    public Lexer(Iterator<Character> characters)
    {
        this(characters, 0);
    }

    public Lexer(Iterator<Character> characters, int currentCharacterStartIndex)
    {
        this.characters = characters;
        this.currentCharacterStartIndex = currentCharacterStartIndex - 1;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return currentLex != null;
    }

    @Override
    public Lex getCurrent()
    {
        return currentLex;
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
            nextCharacter();
        }

        if (hasCurrentCharacter())
        {
            final int currentLexStartIndex = getCurrentCharacterStartIndex();
            switch (this.getCurrentCharacter())
            {
                case '{':
                    currentLex = Lex.leftCurlyBracket(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '}':
                    currentLex = Lex.rightCurlyBracket(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '[':
                    currentLex = Lex.leftSquareBracket(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case ']':
                    currentLex = Lex.rightSquareBracket(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '(':
                    currentLex = Lex.leftParenthesis(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case ')':
                    currentLex = Lex.rightParenthesis(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '<':
                    currentLex = Lex.leftAngleBracket(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '>':
                    currentLex = Lex.rightAngleBracket(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '\"':
                    currentLex = Lex.doubleQuote(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '\'':
                    currentLex = Lex.singleQuote(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '-':
                    currentLex = Lex.dash(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '+':
                    currentLex = Lex.plus(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case ',':
                    currentLex = Lex.comma(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case ':':
                    currentLex = Lex.colon(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case ';':
                    currentLex = Lex.semicolon(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '!':
                    currentLex = Lex.exclamationPoint(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '\\':
                    currentLex = Lex.backslash(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '/':
                    currentLex = Lex.forwardSlash(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '?':
                    currentLex = Lex.questionMark(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '=':
                    currentLex = Lex.equalsSign(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '.':
                    currentLex = Lex.period(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '_':
                    currentLex = Lex.underscore(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '&':
                    currentLex = Lex.ampersand(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case ' ':
                    currentLex = Lex.space(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '\t':
                    currentLex = Lex.tab(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '\r':
                    if(!this.nextCharacter() || this.getCurrentCharacter() != '\n')
                    {
                        currentLex = Lex.carriageReturn(currentLexStartIndex);
                    }
                    else
                    {
                        currentLex = Lex.carriageReturnNewLine(currentLexStartIndex);
                        this.nextCharacter();
                    }
                    break;

                case '\n':
                    currentLex = Lex.newLine(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '*':
                    currentLex = Lex.asterisk(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '%':
                    currentLex = Lex.percent(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '|':
                    currentLex = Lex.verticalBar(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                case '#':
                    currentLex = Lex.hash(currentLexStartIndex);
                    this.nextCharacter();
                    break;

                default:
                    if(Lex.isLetter(this.getCurrentCharacter()))
                    {
                        currentLex = Lex.letters(readLetters(characters), currentLexStartIndex);
                    }
                    else if(Lex.isDigit(this.getCurrentCharacter()))
                    {
                        currentLex = Lex.digits(readDigits(characters), currentLexStartIndex);
                    }
                    else
                    {
                        currentLex = Lex.unrecognized(this.getCurrentCharacter(), currentLexStartIndex);
                        this.nextCharacter();
                    }
                    break;
            }
        }
        else
        {
            currentLex = null;
        }

        return hasCurrent();
    }

    private int getCurrentCharacterStartIndex()
    {
        return currentCharacterStartIndex;
    }

    private boolean hasCurrentCharacter()
    {
        return characters.hasCurrent();
    }

    private Character getCurrentCharacter()
    {
        return characters.getCurrent();
    }

    private boolean nextCharacter()
    {
        ++currentCharacterStartIndex;
        return characters.next();
    }

    private static String readLetters(Iterator<Character> characters)
    {
        return readWhile(characters, Lex.isLetter);
    }

    private static String readDigits(Iterator<Character> characters)
    {
        return readWhile(characters, Lex.isDigit);
    }

    private static String readWhile(Iterator<Character> characters, Function1<Character,Boolean> condition)
    {
        String result = "";

        if (characters.hasCurrent() && condition.run(characters.getCurrent())) {
            result += characters.getCurrent();
        }

        while (characters.next() && condition.run(characters.getCurrent())) {
            result += characters.getCurrent();
        }

        return result;
    }
}
