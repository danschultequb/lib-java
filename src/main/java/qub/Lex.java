package qub;

public class Lex
{
    public enum Type
    {
        LeftCurlyBracket,
        RightCurlyBracket,
        LeftSquareBracket,
        RightSquareBracket,
        LeftAngleBracket,
        RightAngleBracket,
        LeftParenthesis,
        RightParenthesis,
        Letters,
        SingleQuote,
        DoubleQuote,
        Digits,
        Comma,
        Colon,
        Semicolon,
        ExclamationPoint,
        Backslash,
        ForwardSlash,
        QuestionMark,
        Dash,
        Plus,
        EqualsSign,
        Period,
        Underscore,
        Ampersand,
        VerticalBar,
        Space,
        Tab,
        CarriageReturn,
        NewLine,
        CarriageReturnNewLine,
        Asterisk,
        Percent,
        Hash,
        Unrecognized
    };

    private final String text;
    private final int startIndex;
    private final Type type;

    public Lex(String text, int startIndex, Type type)
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

    public Type getType()
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
        return new Lex("{", startIndex, Type.LeftCurlyBracket);
    }

    public static Lex rightCurlyBracket(int startIndex)
    {
        return new Lex("}", startIndex, Type.RightCurlyBracket);
    }

    public static Lex leftSquareBracket(int startIndex)
    {
        return new Lex("[", startIndex, Type.LeftSquareBracket);
    }

    public static Lex rightSquareBracket(int startIndex)
    {
        return new Lex("]", startIndex, Type.RightSquareBracket);
    }

    public static Lex leftParenthesis(int startIndex)
    {
        return new Lex("(", startIndex, Type.LeftParenthesis);
    }

    public static Lex rightParenthesis(int startIndex)
    {
        return new Lex(")", startIndex, Type.RightParenthesis);
    }

    public static Lex leftAngleBracket(int startIndex)
    {
        return new Lex("<", startIndex, Type.LeftAngleBracket);
    }

    public static Lex rightAngleBracket(int startIndex)
    {
        return new Lex(">", startIndex, Type.RightAngleBracket);
    }

    public static Lex doubleQuote(int startIndex)
    {
        return new Lex("\"", startIndex, Type.DoubleQuote);
    }

    public static Lex singleQuote(int startIndex)
    {
        return new Lex("\'", startIndex, Type.SingleQuote);
    }

    public static Lex dash(int startIndex)
    {
        return new Lex("-", startIndex, Type.Dash);
    }

    public static Lex plus(int startIndex)
    {
        return new Lex("+", startIndex, Type.Plus);
    }

    public static Lex comma(int startIndex)
    {
        return new Lex(",", startIndex, Type.Comma);
    }

    public static Lex colon(int startIndex)
    {
        return new Lex(":", startIndex, Type.Colon);
    }

    public static Lex semicolon(int startIndex)
    {
        return new Lex(";", startIndex, Type.Semicolon);
    }

    public static Lex exclamationPoint(int startIndex)
    {
        return new Lex("!", startIndex, Type.ExclamationPoint);
    }

    public static Lex backslash(int startIndex)
    {
        return new Lex("\\", startIndex, Type.Backslash);
    }

    public static Lex forwardSlash(int startIndex)
    {
        return new Lex("/", startIndex, Type.ForwardSlash);
    }

    public static Lex questionMark(int startIndex)
    {
        return new Lex("?", startIndex, Type.QuestionMark);
    }

    public static Lex equalsSign(int startIndex)
    {
        return new Lex("=", startIndex, Type.EqualsSign);
    }

    public static Lex period(int startIndex)
    {
        return new Lex(".", startIndex, Type.Period);
    }

    public static Lex underscore(int startIndex)
    {
        return new Lex("_", startIndex, Type.Underscore);
    }

    public static Lex ampersand(int startIndex)
    {
        return new Lex("&", startIndex, Type.Ampersand);
    }

    public static Lex space(int startIndex)
    {
        return new Lex(" ", startIndex, Type.Space);
    }

    public static Lex tab(int startIndex)
    {
        return new Lex("\t", startIndex, Type.Tab);
    }

    public static Lex carriageReturn(int startIndex)
    {
        return new Lex("\r", startIndex, Type.CarriageReturn);
    }

    public static Lex carriageReturnNewLine(int startIndex)
    {
        return new Lex("\r\n", startIndex, Type.CarriageReturnNewLine);
    }

    public static Lex newLine(int startIndex)
    {
        return new Lex("\n", startIndex, Type.NewLine);
    }

    public static Lex asterisk(int startIndex)
    {
        return new Lex("*", startIndex, Type.Asterisk);
    }

    public static Lex percent(int startIndex)
    {
        return new Lex("%", startIndex, Type.Percent);
    }

    public static Lex verticalBar(int startIndex)
    {
        return new Lex("|", startIndex, Type.VerticalBar);
    }

    public static Lex hash(int startIndex)
    {
        return new Lex("#", startIndex, Type.Hash);
    }

    public static Lex letters(String letters, int startIndex)
    {
        return new Lex(letters, startIndex, Type.Letters);
    }

    public static Lex digits(String digits, int startIndex)
    {
        return new Lex(digits, startIndex, Type.Digits);
    }

    public static Lex unrecognized(char unrecognized, int startIndex)
    {
        return new Lex(Character.toString(unrecognized), startIndex, Type.Unrecognized);
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