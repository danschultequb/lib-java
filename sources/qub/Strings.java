package qub;

public class Strings
{
    /**
     * Check if the provided text String contains any of the provided characters.
     * @param text The text to search in.
     * @param characters The characters to search for.
     * @return Whether or not the provided text String contains any of the provided characters.
     */
    public static boolean containsAny(String text, char[] characters)
    {
        boolean result = false;
        if (text != null && !text.isEmpty() && characters != null && characters.length > 0)
        {
            final int textLength = text.length();
            search: for (int i = 0; i < textLength; ++i)
            {
                final char currentCharacter = text.charAt(i);
                for (final char searchCharacter : characters)
                {
                    if (currentCharacter == searchCharacter)
                    {
                        result = true;
                        break search;
                    }
                }
            }
        }
        return result;
    }

    public static String escape(char character)
    {
        String result;
        switch (character)
        {
            case '\b':
                result = "\\b";
                break;

            case '\f':
                result = "\\f";
                break;

            case '\n':
                result = "\\n";
                break;

            case '\r':
                result = "\\r";
                break;

            case '\t':
                result = "\\t";
                break;

            case '\'':
                result = "\\\'";
                break;

            case '\"':
                result = "\\\"";
                break;

            default:
                result = Character.toString(character);
                break;
        }
        return result;
    }

    public static String escape(String text)
    {
        String result;
        if (text == null || text.isEmpty())
        {
            result = text;
        }
        else
        {
            final StringBuilder builder = new StringBuilder();
            final int textLength = text.length();
            for (int i = 0; i < textLength; ++i)
            {
                builder.append(Strings.escape(text.charAt(i)));
            }
            result = builder.toString();
        }
        return result;
    }

    public static String quote(String text)
    {
        return text == null ? null : '\"' + text + '\"';
    }

    /**
     * Surround the provided text with quotes and textualize any escaped characters.
     * @param text The text to quote and escape.
     * @return The quoted and escaped text.
     */
    public static String escapeAndQuote(String text)
    {
        return quote(escape(text));
    }
}
