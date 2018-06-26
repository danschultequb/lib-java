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
     * Get the number of characters in the provided String.
     * @param value The value.
     * @return The number of characters.
     */
    public static int getLength(String value)
    {
        return value == null ? 0 : value.length();
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
                builder.append(Characters.escape(text.charAt(i)));
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

    /**
     * Repeat the provided value String a specified number of times.
     * @param value The value to repeat.
     * @param repetitions The number of times to repeat the value.
     * @return The repeated string.
     */
    public static String repeat(String value, int repetitions)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertGreaterThanOrEqualTo(repetitions, 0, "repetitions");

        final StringBuilder builder = new StringBuilder(value.length() * repetitions);
        if (!Strings.isNullOrEmpty(value))
        {
            for (int i = 0; i < repetitions; ++i)
            {
                builder.append(value);
            }
        }
        final String result = builder.toString();

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Strings.getLength(value) * repetitions, result.length(), "result.length()");

        return builder.toString();
    }

    /**
     * Repeat the provided value String a specified number of times.
     * @param value The value to repeat.
     * @param repetitions The number of times to repeat the value.
     * @return The repeated string.
     */
    public static String repeat(char value, int repetitions)
    {
        PreCondition.assertGreaterThanOrEqualTo(0, repetitions, "repetitions");

        final StringBuilder builder = new StringBuilder(repetitions);
        for (int i = 0; i < repetitions; ++i)
        {
            builder.append(value);
        }
        final String result = builder.toString();

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(repetitions, result.length(), "result.length()");

        return builder.toString();
    }
}
