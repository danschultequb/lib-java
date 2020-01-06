package qub;

/**
 * A collection of functions that help when interacting with Strings.
 */
public interface Strings
{
    /**
     * Get an Iterable for the characters in the provided text.
     * @param text The text to create an Iterable for.
     * @return The Iterable over the characters in the provided text.
     */
    static Iterable<Character> iterable(String text)
    {
        final CharacterList result = CharacterList.create();
        if (!Strings.isNullOrEmpty(text))
        {
            result.addAll(text);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get an Iterator for the characters in the provided text.
     * @param text The text to iterate.
     * @return The Iterator over the characters in the provided text.
     */
    static Iterator<Character> iterate(String text)
    {
        return new StringIterator(text);
    }

    /**
     * Get the number of characters in the provided String.
     * @param value The value.
     * @return The number of characters.
     */
    static int getLength(String value)
    {
        return value == null ? 0 : value.length();
    }

    static boolean startsWith(String text, String prefix)
    {
        return !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(prefix) && text.startsWith(prefix);
    }

    static boolean startsWith(String text, String prefix, CharacterComparer characterComparer)
    {
        boolean result = false;
        if (!Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(prefix))
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

    static boolean endsWith(String text, String suffix)
    {
        return !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(suffix) && text.endsWith(suffix);
    }

    static boolean contains(String text, String substring)
    {
        return !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(substring) && text.contains(substring);
    }

    /**
     * Check if the provided text String contains any of the provided characters.
     * @param text The text to traverse in.
     * @param characters The characters to traverse for.
     * @return Whether or not the provided text String contains any of the provided characters.
     */
    static boolean containsAny(String text, char[] characters)
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
    static String escape(String text)
    {
        String result;
        if (text == null || text.isEmpty())
        {
            result = text;
        }
        else
        {
            final CharacterList builder = CharacterList.create();
            final int textLength = text.length();
            for (int i = 0; i < textLength; ++i)
            {
                builder.addAll(Characters.escape(text.charAt(i)));
            }
            result = builder.toString(true);
        }
        return result;
    }

    /**
     * Surround the provided text with double quotes.
     * @param text The text to quote.
     * @return The quoted text.
     */
    static String quote(String text)
    {
        return text == null ? null : '\"' + text + '\"';
    }

    /**
     * Surround the String representation of the provided value with quotes and textualize any
     * escaped characters.
     * @param value The object to quote and escape.
     * @return The quoted and escaped String representation of the provided value.
     */
    static String escapeAndQuote(Object value)
    {
        return escapeAndQuote(value == null ? null : value.toString());
    }

    /**
     * Surround the provided text with quotes and textualize any escaped characters.
     * @param text The text to quote and escape.
     * @return The quoted and escaped text.
     */
    static String escapeAndQuote(String text)
    {
        return Strings.quote(Strings.escape(text));
    }

    /**
     * Get whether or not the provided text is null or empty.
     * @param text The text to check.
     * @return Whether or not the provided text is null or empty.
     */
    static boolean isNullOrEmpty(String text)
    {
        return text == null || text.isEmpty();
    }

    /**
     * Get whether or not the provided text is null or empty.
     * @param text The text to check.
     * @return Whether or not the provided text is null or empty.
     */
    static boolean isNullOrEmpty(StringBuilder text)
    {
        return text == null || text.length() == 0;
    }

    /**
     * Repeat the provided value String a specified number of times.
     * @param value The value to repeat.
     * @param repetitions The number of times to repeat the value.
     * @return The repeated string.
     */
    static String repeat(String value, int repetitions)
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
    static String repeat(char value, int repetitions)
    {
        PreCondition.assertGreaterThanOrEqualTo(repetitions, 0, "repetitions");

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
    static String concatenate(String lhs, String rhs)
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
     * @param values The String values to join together.
     * @return The joined string values.
     */
    static String join(java.lang.Iterable<String> values)
    {
        PreCondition.assertNotNull(values, "values");

        final StringBuilder builder = new StringBuilder();
        for (final String value : values)
        {
            if (!Strings.isNullOrEmpty(value))
            {
                builder.append(value);
            }
        }
        final String result = builder.toString();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided String values with the provided separator character between them.
     * @param separator The character to insert between the string values.
     * @param values The String values to join together.
     * @return The joined string values.
     */
    static String join(char separator, java.lang.Iterable<String> values)
    {
        PreCondition.assertNotNull(values, "values");

        final StringBuilder builder = new StringBuilder();
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
        final String result = builder.toString();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided String values with the provided separator String between them.
     * @param separator The String to insert between the string values.
     * @param values The String values to join together.
     * @return The joined string values.
     */
    static String join(String separator, java.lang.Iterable<String> values)
    {
        PreCondition.assertNotNull(separator, "separator");
        PreCondition.assertNotNull(values, "values");

        final StringBuilder builder = new StringBuilder();
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
        final String result = builder.toString();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static boolean equal(String lhs, String rhs)
    {
        return Comparer.equal(lhs, rhs);
    }

    static String format(String formattedString, Object... formattedStringArguments)
    {
        String result;
        if (formattedString != null && !formattedString.isEmpty() && formattedStringArguments != null && formattedStringArguments.length > 0)
        {
            result = String.format(formattedString, formattedStringArguments);
        }
        else
        {
            result = formattedString;
        }
        return result;
    }

    /**
     * Pad on the left side the provided String value with the provided padCharacter until it is at
     * least the provided minimumLength long.
     * @param value The value to pad.
     * @param minimumLength The minimum number of characters that the resulting String will be.
     * @param padCharacter The character to use to pad the provided value.
     * @return The padded String that is at least the provided minimumLength long.
     */
    static String padLeft(Object value, int minimumLength, char padCharacter)
    {
        String result = Objects.toString(value == null ? "" : value);
        final int charactersToAdd = minimumLength - Strings.getLength(result);
        if (charactersToAdd > 0)
        {
            result = Strings.concatenate(Strings.repeat(padCharacter, charactersToAdd), result);
        }
        return result;
    }

    /**
     * Pad on the right side the provided String value with the provided padCharacter until it is at
     * least the provided minimumLength long.
     * @param value The value to pad.
     * @param minimumLength The minimum number of characters that the resulting String will be.
     * @param padCharacter The character to use to pad the provided value.
     * @return The padded String that is at least the provided minimumLength long.
     */
    static String padRight(Object value, int minimumLength, char padCharacter)
    {
        String result = Objects.toString(value == null ? "" : value);
        final int charactersToAdd = minimumLength - Strings.getLength(result);
        if (charactersToAdd > 0)
        {
            result = Strings.concatenate(result, Strings.repeat(padCharacter, charactersToAdd));
        }
        return result;
    }

    static boolean isOneOf(String value, String... possibilities)
    {
        return isOneOf(value, Iterable.create(possibilities));
    }

    static boolean isOneOf(String value, Iterable<String> possibilities)
    {
        PreCondition.assertNotNull(possibilities, "possibilities");

        return possibilities.contains(value);
    }

    /**
     * Return the set of words that are used in the provided String.
     * @param value The String to find words in.
     * @return The set of words that are used in the provided String.
     */
    static Set<String> getWords(String value)
    {
        final Set<String> result = Set.create();

        if (!Strings.isNullOrEmpty(value))
        {
            final StringBuilder builder = new StringBuilder();
            for (char character : value.toCharArray())
            {
                if (Characters.isLetterOrDigit(character))
                {
                    builder.append(character);
                }
                else if (builder.length() > 0)
                {
                    result.add(builder.toString());
                    builder.setLength(0);
                }
            }
            if (builder.length() > 0)
            {
                result.add(builder.toString());
            }
        }

        return result;
    }

    static Comparison compare(String lhs, String rhs)
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
        return result;
    }

    static boolean lessThan(String lhs, String rhs)
    {
        return Strings.compare(lhs, rhs) == Comparison.LessThan;
    }

    static boolean greaterThan(String lhs, String rhs)
    {
        return Strings.compare(lhs, rhs) == Comparison.GreaterThan;
    }

    /**
     * Get the lines that make up the provided String value.
     * @param value The value to get the lines create.
     * @return The lines that make up the provided String value.
     */
    static Iterable<String> getLines(String value)
    {
        return getLines(value, false);
    }

    /**
     * Get the lines that make up the provided String value.
     * @param value The value to get the lines create.
     * @param includeNewLineCharacters Whether or not to include new line sequences (\n and \r\n).
     * @return The lines that make up the provided String value.
     */
    static Iterable<String> getLines(String value, boolean includeNewLineCharacters)
    {
        PreCondition.assertNotNull(value, "value");

        final List<String> result = List.create();
        int lineStart = 0;
        int lineLength = 0;
        final int valueLength = value.length();
        boolean previousCharacterWasCarriageReturn = false;
        for (int i = 0; i < valueLength; ++i)
        {
            final char currentCharacter = value.charAt(i);
            ++lineLength;

            if (currentCharacter == '\r')
            {
                previousCharacterWasCarriageReturn = true;
            }
            else
            {
                if (currentCharacter == '\n')
                {
                    int lineEnd = lineStart + lineLength;
                    if (!includeNewLineCharacters)
                    {
                        --lineEnd;
                        if (previousCharacterWasCarriageReturn)
                        {
                            --lineEnd;
                        }
                    }
                    result.add(value.substring(lineStart, lineEnd));
                    lineStart = i + 1;
                    lineLength = 0;
                }
                previousCharacterWasCarriageReturn = false;
            }
        }
        if (lineLength > 0)
        {
            result.add(value.substring(lineStart));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
