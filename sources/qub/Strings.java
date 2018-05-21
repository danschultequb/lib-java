package qub;

public class Strings
{
    /**
     * Get an Iterator for the characters in the provided text.
     * @param text The text to iterate.
     * @return The Iterator over the characters in the provided text.
     */
    public static Iterator<Character> iterate(String text)
    {
        return new StringIterator(text);
    }

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
     * Escape any escaped characters (such as '\n' or '\t') in the provided text.
     * @param text The text to escape.
     * @return The escaped text.
     */
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

    /**
     * Surround the provided text with double quotes.
     * @param text The text to quote.
     * @return The quoted text.
     */
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

    /**
     * Get whether or not the provided text is null or empty.
     * @param text The text to check.
     * @return Whether or not the provided text is null or empty.
     */
    public static boolean isNullOrEmpty(String text)
    {
        return text == null || text.isEmpty();
    }
}
