package qub;

public class Lex
{
    private final String text;
    private final int startIndex;
    private final LexType type;

    public Lex(String text, int startIndex, LexType type)
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

    public int getLength()
    {
        return text == null ? 0 : text.length();
    }

    public int getStartIndex()
    {
        return startIndex;
    }

    public int getAfterEndIndex()
    {
        return Span.getAfterEndIndex(startIndex, getLength());
    }

    public int getEndIndex()
    {
        return Span.getEndIndex(startIndex, getLength());
    }

    public Span getSpan()
    {
        return new Span(startIndex, getLength());
    }

    public LexType getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Lex && equals((Lex)rhs);
    }

    public boolean equals(Lex rhs)
    {
        return rhs != null &&
                text.equals(rhs.text) &&
                startIndex == rhs.startIndex &&
                type == rhs.type;
    }

    public boolean isWhitespace()
    {
        boolean result;

        switch (type)
        {
            case Space:
            case Tab:
            case CarriageReturn:
                result = true;
                break;

            default:
                result = false;
        }

        return result;
    }

    public boolean isNewLine()
    {
        boolean result;

        switch (type)
        {
            case CarriageReturnNewLine:
            case NewLine:
                result = true;
                break;

            default:
                result = false;
                break;
        }

        return result;
    }

    public static Lex leftCurlyBracket(int startIndex)
    {
        return new Lex("{", startIndex, LexType.LeftCurlyBracket);
    }

    public static Lex rightCurlyBracket(int startIndex)
    {
        return new Lex("}", startIndex, LexType.RightCurlyBracket);
    }

    public static Lex leftSquareBracket(int startIndex)
    {
        return new Lex("[", startIndex, LexType.LeftSquareBracket);
    }

    public static Lex rightSquareBracket(int startIndex)
    {
        return new Lex("]", startIndex, LexType.RightSquareBracket);
    }

    public static Lex leftParenthesis(int startIndex)
    {
        return new Lex("(", startIndex, LexType.LeftParenthesis);
    }

    public static Lex rightParenthesis(int startIndex)
    {
        return new Lex(")", startIndex, LexType.RightParenthesis);
    }

    public static Lex leftAngleBracket(int startIndex)
    {
        return new Lex("<", startIndex, LexType.LeftAngleBracket);
    }

    public static Lex rightAngleBracket(int startIndex)
    {
        return new Lex(">", startIndex, LexType.RightAngleBracket);
    }

    public static Lex doubleQuote(int startIndex)
    {
        return new Lex("\"", startIndex, LexType.DoubleQuote);
    }

    public static Lex singleQuote(int startIndex)
    {
        return new Lex("\'", startIndex, LexType.SingleQuote);
    }

    public static Lex dash(int startIndex)
    {
        return new Lex("-", startIndex, LexType.Dash);
    }

    public static Lex plus(int startIndex)
    {
        return new Lex("+", startIndex, LexType.Plus);
    }

    public static Lex comma(int startIndex)
    {
        return new Lex(",", startIndex, LexType.Comma);
    }

    public static Lex colon(int startIndex)
    {
        return new Lex(":", startIndex, LexType.Colon);
    }

    public static Lex semicolon(int startIndex)
    {
        return new Lex(";", startIndex, LexType.Semicolon);
    }

    public static Lex exclamationPoint(int startIndex)
    {
        return new Lex("!", startIndex, LexType.ExclamationPoint);
    }

    public static Lex backslash(int startIndex)
    {
        return new Lex("\\", startIndex, LexType.Backslash);
    }

    public static Lex forwardSlash(int startIndex)
    {
        return new Lex("/", startIndex, LexType.ForwardSlash);
    }

    public static Lex questionMark(int startIndex)
    {
        return new Lex("?", startIndex, LexType.QuestionMark);
    }

    public static Lex equalsSign(int startIndex)
    {
        return new Lex("=", startIndex, LexType.EqualsSign);
    }

    public static Lex period(int startIndex)
    {
        return new Lex(".", startIndex, LexType.Period);
    }

    public static Lex underscore(int startIndex)
    {
        return new Lex("_", startIndex, LexType.Underscore);
    }

    public static Lex ampersand(int startIndex)
    {
        return new Lex("&", startIndex, LexType.Ampersand);
    }

    public static Lex space(int startIndex)
    {
        return new Lex(" ", startIndex, LexType.Space);
    }

    public static Lex tab(int startIndex)
    {
        return new Lex("\t", startIndex, LexType.Tab);
    }

    public static Lex carriageReturn(int startIndex)
    {
        return new Lex("\r", startIndex, LexType.CarriageReturn);
    }

    public static Lex carriageReturnNewLine(int startIndex)
    {
        return new Lex("\r\n", startIndex, LexType.CarriageReturnNewLine);
    }

    public static Lex newLine(int startIndex)
    {
        return new Lex("\n", startIndex, LexType.NewLine);
    }

    public static Lex asterisk(int startIndex)
    {
        return new Lex("*", startIndex, LexType.Asterisk);
    }

    public static Lex percent(int startIndex)
    {
        return new Lex("%", startIndex, LexType.Percent);
    }

    public static Lex verticalBar(int startIndex)
    {
        return new Lex("|", startIndex, LexType.VerticalBar);
    }

    public static Lex hash(int startIndex)
    {
        return new Lex("#", startIndex, LexType.Hash);
    }

    public static Lex letters(String letters, int startIndex)
    {
        return new Lex(letters, startIndex, LexType.Letters);
    }

    public static Lex digits(String digits, int startIndex)
    {
        return new Lex(digits, startIndex, LexType.Digits);
    }

    public static Lex unrecognized(char unrecognized, int startIndex)
    {
        return new Lex(Character.toString(unrecognized), startIndex, LexType.Unrecognized);
    }

    public static boolean isLetter(char character)
    {
        return ('a' <= character && character <= 'z') ||
                ('A' <= character && character <= 'Z');
    }

    public static final Function1<Character,Boolean> isLetter = new Function1<Character, Boolean>()
    {
        @Override
        public Boolean run(Character character)
        {
            return isLetter(character);
        }
    };

    public static boolean isDigit(char character)
    {
        return '0' <= character && character <= '9';
    }

    public static final Function1<Character,Boolean> isDigit = new Function1<Character, Boolean>()
    {
        @Override
        public Boolean run(Character character)
        {
            return isDigit(character);
        }
    };
}