package qub;

public class Characters
{
    /**
     * The minimum value that a character can have.
     */
    public static final char minimumValue = java.lang.Character.MIN_VALUE;

    /**
     * The maximum value that a character can have.
     */
    public static final char maximumValue = java.lang.Character.MAX_VALUE;

    /**
     * Get the Range of characters that includes all characters.
     */
    public static final Range<Character> all = Range.between(minimumValue, maximumValue);

    /**
     * Get the String representation of the provided character.
     * @param character The character to change to a String.
     * @return The String representation of the provided character.
     */
    public static String toString(char character)
    {
        return java.lang.Character.toString(character);
    }

    /**
     * Get whether or not the provided character is a quote character.
     * @param character The character to check.
     * @return Whether or not the provided character is a quote character.
     */
    public static boolean isQuote(char character)
    {
        return character == '\'' || character == '\"';
    }

    /**
     * Surround the provided character with single quotes and textualize any escaped characters.
     * @param character The character to quote and escape.
     * @return The quoted and escaped character.
     */
    public static String escapeAndQuote(char character)
    {
        return Strings.quote(Characters.escape(character));
    }

    /**
     * Escape the provided character if it is an escaped character (such as '\n' or '\t').
     * @param character The character to escape.
     * @return The escaped character.
     */
    public static String escape(char character)
    {
        return Characters.escape(character, Array.createCharacter('\''));
    }

    /**
     * Escape the provided character if it is an escaped character (such as '\n' or '\t').
     * @param character The character to escape.
     * @param dontEscape Escape sequences that will not be escaped.
     * @return The escaped character.
     */
    public static String escape(char character, Iterable<Character> dontEscape)
    {
        String result;
        if (!Iterable.isNullOrEmpty(dontEscape) && dontEscape.contains(character))
        {
            result = Characters.toString(character);
        }
        else
        {
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
                    result = Characters.toString(character);
                    break;
            }
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Surround the provided character with single quotes.
     * @param character The character to quote.
     * @return The quoted text.
     */
    public static String quote(char character)
    {
        return Strings.quote(java.lang.Character.toString(character));
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

    /**
     * Get whether or not the provided value is a letter or a digit.
     * @param value The value to check.
     * @return Whether or not the provided value is a letter or a digit.
     */
    public static boolean isLetterOrDigit(char value)
    {
        return java.lang.Character.isLetterOrDigit(value);
    }
}
