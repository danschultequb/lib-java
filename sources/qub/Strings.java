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

    public static boolean startsWith(String text, String prefix)
    {
        return !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(prefix) && text.startsWith(prefix);
    }

    public static boolean startsWith(String text, String prefix, CharacterComparer characterComparer)
    {
        boolean result = false;
        if (!Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(text))
        {
            final int textLength = text.length();
            final int prefixLength = prefix.length();
            if (prefixLength <= textLength)
            {
                result = true;
                for (int i = 0; i < prefixLength; ++i)
                {
                    if (!characterComparer.equal(text.charAt(i), prefix.charAt(i)))
                    {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean endsWith(String text, String suffix)
    {
        return !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(suffix) && text.endsWith(suffix);
    }

    public static boolean contains(String text, String substring)
    {
        return !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(substring) && text.contains(substring);
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

    /**
     * Concatenate the two strings together into a single string. Null values will be treated as an
     * empty string.
     * @param lhs The first string.
     * @param rhs The second string.
     * @return The concatenated string.
     */
    public static String concatenate(String lhs, String rhs)
    {
        String result = "";
        if (lhs != null)
        {
            result += lhs;
        }
        if (rhs != null)
        {
            result += rhs;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided String values with the provided separator character between them.
     * @param separator The character to insert between the string values.
     * @param values The String values to join together.
     * @return The joined string values.
     */
    public static String join(char separator, java.lang.Iterable<String> values)
    {
        final StringBuilder builder = new StringBuilder();
        if (values != null)
        {
            for (final String value : values)
            {
                if (builder.length() > 0)
                {
                    builder.append(separator);
                }
                if (value != null)
                {
                    builder.append(value);
                }
            }
        }
        return builder.toString();
    }

    public static boolean equal(String lhs, String rhs)
    {
        return Comparer.equal(lhs, rhs);
    }

    public static Comparison compare(String lhs, String rhs)
    {
        Comparison result;
        if (lhs == rhs)
        {
            result = Comparison.Equal;
        }
        else if (lhs == null)
        {
            result = Comparison.LessThan;
        }
        else if (rhs == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Comparison.from(lhs.compareTo(rhs));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
