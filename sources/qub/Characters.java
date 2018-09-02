package qub;

public class Characters
{
    /**
     * Surround the provided character with single quotes and textualize any escaped characters.
     * @param character The character to quote and escape.
     * @return The quoted and escaped character.
     */
    public static String escapeAndQuote(char character)
    {
        return Characters.quote(Characters.escape(character));
    }

    /**
     * Escape the provided character if it is an escaped character (such as '\n' or '\t').
     * @param character The character to escape.
     * @return The escaped character.
     */
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

    /**
     * Surround the provided character with single quotes.
     * @param character The character to quote.
     * @return The quoted text.
     */
    public static String quote(char character)
    {
        return Characters.quote(Character.toString(character));
    }

    /**
     * Surround the provided text with single quotes.
     * @param text The text to quote.
     * @return The quoted text.
     */
    public static String quote(String text)
    {
        return text == null ? null : '\'' + text + '\'';
    }

    /**
     * Get the lower-cased version of the provided character.
     * @param value The character.
     * @return The lower-cased version of the provided character.
     */
    public static char toLowerCase(char value)
    {
        return Character.toLowerCase(value);
    }
}
