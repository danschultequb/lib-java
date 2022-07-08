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
        return StringIterator.create(text);
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
        PreCondition.assertNotNull(characterComparer, "characterComparer");

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

    /**
     * Get whether or not the provided text ends with the provided suffix.
     * @param text The text to check.
     * @param suffix The suffix to check for.
     * @return Whether or not the provided text ends with the provided suffix.
     */
    static boolean endsWith(String text, char suffix)
    {
        return Strings.endsWith(text, Characters.toString(suffix));
    }

    /**
     * Get whether or not the provided text ends with the provided suffix.
     * @param text The text to check.
     * @param suffix The suffix to check for.
     * @return Whether or not the provided text ends with the provided suffix.
     */
    static boolean endsWith(String text, String suffix)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNullAndNotEmpty(suffix, "suffix");

        return text.endsWith(suffix);
    }

    /**
     * Ensure that the provided text ends with the provided suffix. If it does, then return the
     * provided text. If it doesn't, then return the text with the provided suffix appended at the
     * end.
     * @param text The text to check.
     * @param suffix The suffix to check for.
     * @return The text with the provided suffix at the end.
     */
    static String ensureEndsWith(String text, char suffix)
    {
        return Strings.ensureEndsWith(text, Characters.toString(suffix));
    }

    /**
     * Ensure that the provided text ends with the provided suffix. If it does, then return the
     * provided text. If it doesn't, then return the text with the provided suffix appended at the
     * end.
     * @param text The text to check.
     * @param suffix The suffix to check for.
     * @return The text with the provided suffix at the end.
     */
    static String ensureEndsWith(String text, String suffix)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNullAndNotEmpty(suffix, "suffix");

        return text.endsWith(suffix) ? text : text + suffix;
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
     * Unescape any escaped characters (such as '\n' or '\t') in the provided text.
     * @param text The text to unescape.
     * @return The unescaped text.
     */
    static String unescape(String text)
    {
        String result;
        if (text == null || text.isEmpty())
        {
            result = text;
        }
        else
        {
            final SaveableIterator<Character> characters = SaveableIterator.create(Strings.iterate(text).start());
            final CharacterList builder = CharacterList.create();
            while (characters.hasCurrent())
            {
                builder.add(Characters.unescapeNextCharacter(characters));
            }
            result = builder.toString();
        }
        return result;
    }

    /**
     * Get whether or not the provided text is surrounded by quotes.
     * @param text The text to check.
     * @return Whether or not the provided text is surrounded by quotes.
     */
    static boolean isQuoted(String text)
    {
        boolean result = false;

        if (text != null)
        {
            final int textLength = text.length();
            if (textLength >= 2)
            {
                final char firstCharacter = text.charAt(0);
                if (Characters.isQuote(firstCharacter) && firstCharacter == text.charAt(textLength - 1))
                {
                    if (textLength == 2)
                    {
                        result = true;
                    }
                    else
                    {
                        int escapeSlashCount = 0;
                        while (text.charAt(textLength - 2 - escapeSlashCount) == '\\')
                        {
                            ++escapeSlashCount;
                        }
                        result = Math.isEven(escapeSlashCount);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Unquote the provided text if it is quoted. If it isn't quoted, then return it as it is.
     * @param text The text to unquote.
     * @return The unquoted text.
     */
    static String unquote(String text)
    {
        return Strings.isQuoted(text) ? text.substring(1, text.length() - 1) : text;
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

    static String unescapeAndUnquote(String text)
    {
        return Strings.unquote(Strings.unescape(text));
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
     * Repeat the provided value String a specified number of times.
     * @param value The value to repeat.
     * @param repetitions The number of times to repeat the value.
     * @return The repeated string.
     */
    static String repeat(String value, int repetitions)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertGreaterThanOrEqualTo(repetitions, 0, "repetitions");

        final CharacterList list = CharacterList.create();
        if (!Strings.isNullOrEmpty(value))
        {
            for (int i = 0; i < repetitions; ++i)
            {
                list.addAll(value);
            }
        }
        final String result = list.toString();

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Strings.getLength(value) * repetitions, result.length(), "result.length()");

        return result;
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

        final CharacterList list = CharacterList.create();
        for (int i = 0; i < repetitions; ++i)
        {
            list.add(value);
        }
        final String result = list.toString();

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(repetitions, result.length(), "result.length()");

        return result;
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
        final CharacterList list = CharacterList.create();
        if (lhs != null)
        {
            list.addAll(lhs);
        }
        if (rhs != null)
        {
            list.addAll(rhs);
        }
        final String result = list.toString(true);

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

        final CharacterList list = CharacterList.create();
        for (final String value : values)
        {
            if (!Strings.isNullOrEmpty(value))
            {
                list.addAll(value);
            }
        }
        final String result = list.toString();

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

        final CharacterList list = CharacterList.create();
        for (final String value : values)
        {
            if (list.any())
            {
                list.add(separator);
            }
            if (value != null)
            {
                list.addAll(value);
            }
        }
        final String result = list.toString();

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

        final CharacterList list = CharacterList.create();
        for (final String value : values)
        {
            if (list.any())
            {
                list.addAll(separator);
            }
            if (value != null)
            {
                list.addAll(value);
            }
        }
        final String result = list.toString();

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
            result = java.lang.String.format(formattedString, formattedStringArguments);
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
        return Strings.isOneOf(value, Iterable.create(possibilities));
    }

    static boolean isOneOf(String value, Iterable<String> possibilities)
    {
        PreCondition.assertNotNull(possibilities, "possibilities");

        return possibilities.contains(value);
    }

    static Iterator<String> iterateWords(String value)
    {
        return Strings.iterateWords(Strings.iterate(value));
    }

    static Iterator<String> iterateWords(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        final CharacterList list = CharacterList.create();
        return Iterator.create((IteratorActions<String> actions) ->
        {
            characters.start();

            while (characters.hasCurrent() && !Characters.isLetterOrDigit(characters.getCurrent()))
            {
                characters.next();
            }

            while (characters.hasCurrent() && Characters.isLetterOrDigit(characters.getCurrent()))
            {
                list.add(characters.getCurrent());
                characters.next();
            }

            if (list.any())
            {
                actions.returnValue(list.toString());
                list.clear();
            }
        });
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
            result = Comparison.create(lhs.compareTo(rhs));
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

    static Iterator<String> iterateLines(String value)
    {
        return Strings.iterateLines(Strings.iterate(value));
    }

    static Iterator<String> iterateLines(String value, boolean includeNewLines)
    {
        return Strings.iterateLines(Strings.iterate(value), includeNewLines);
    }

    static Iterator<String> iterateLines(Iterator<Character> characters)
    {
        return Strings.iterateLines(characters, false);
    }

    static Iterator<String> iterateLines(Iterator<Character> characters, boolean includeNewLines)
    {
        PreCondition.assertNotNull(characters, "characters");

        final CharacterList list = CharacterList.create();
        return Iterator.create((IteratorActions<String> actions) ->
        {
            characters.start();

            boolean foundLine = false;
            while (characters.hasCurrent())
            {
                foundLine = true;

                list.add(characters.takeCurrent());

                if (list.endsWith('\n'))
                {
                    if (!includeNewLines)
                    {
                        list.removeLast();
                        if (list.endsWith('\r'))
                        {
                            list.removeLast();
                        }
                    }
                    break;
                }
            }

            if (foundLine)
            {
                actions.returnValue(list.toString());
                list.clear();
            }
        });
    }

    /**
     * Get an {@link Iterator} that returns the substrings of the provided value that are separated by the provided separator characters.
     * @param value
     * @param separators
     * @return
     */
    public static Iterator<String> iterateSubstrings(String value, char... separators)
    {
        return Strings.iterateSubstrings(value, IterateSubstringsOptions.create().setSeparators(separators));
    }

    public static Iterator<String> iterateSubstrings(String value, String... separators)
    {
        return Strings.iterateSubstrings(value, IterateSubstringsOptions.create().setSeparators(separators));
    }

    public static Iterator<String> iterateSubstrings(String value, Iterable<String> separators)
    {
        return Strings.iterateSubstrings(value, IterateSubstringsOptions.create().setSeparators(separators));
    }

    public static Iterator<String> iterateSubstrings(String value, IterateSubstringsOptions options)
    {
        PreCondition.assertNotNull(options, "options");

        Iterator<String> result;
        if (Strings.isNullOrEmpty(value))
        {
            result = Iterator.create();
        }
        else
        {
            final Iterable<String> separators = options.getSeparators()
                .where((String separator) -> !Strings.isNullOrEmpty(separator))
                .toSet();
            if (Iterable.isNullOrEmpty(separators))
            {
                result = Iterator.create(value);
            }
            else
            {
                final String[] sortedSeparators = new String[separators.getCount()];
                Array.toArray(separators.order((String lhs, String rhs) -> lhs.length() > rhs.length()), sortedSeparators);

                final int valueLength = value.length();
                final boolean includeEmptySubstrings = options.getIncludeEmptySubstrings();
                final boolean includeSeparators = options.getIncludeSeparators();
                final IntegerValue nextStartIndex = IntegerValue.create(0);
                result = Iterator.create((IteratorActions<String> actions) ->
                {
                    int startIndex = nextStartIndex.getAsInt();
                    if (startIndex < valueLength)
                    {
                        int afterEndIndex = startIndex;
                        int separatorAfterEndIndex = -1;
                        while (afterEndIndex < valueLength)
                        {
                            for (final String separator : sortedSeparators)
                            {
                                if (value.startsWith(separator, afterEndIndex))
                                {
                                    separatorAfterEndIndex = afterEndIndex + separator.length();
                                    break;
                                }
                            }

                            if (separatorAfterEndIndex == -1)
                            {
                                afterEndIndex++;
                            }
                            else if (startIndex == afterEndIndex && !includeEmptySubstrings)
                            {
                                startIndex = separatorAfterEndIndex;
                                afterEndIndex = separatorAfterEndIndex;
                                separatorAfterEndIndex = -1;
                            }
                            else
                            {
                                break;
                            }
                        }

                        if (startIndex < valueLength)
                        {
                            String substring;
                            if (separatorAfterEndIndex == -1)
                            {
                                substring = value.substring(startIndex);
                                nextStartIndex.set(valueLength);
                            }
                            else
                            {
                                if (includeSeparators)
                                {
                                    afterEndIndex = separatorAfterEndIndex;
                                }
                                substring = value.substring(startIndex, afterEndIndex);
                                nextStartIndex.set(separatorAfterEndIndex);
                            }

                            actions.returnValue(substring);

                            if (includeEmptySubstrings && separatorAfterEndIndex == valueLength)
                            {
                                actions.returnValue("");
                            }
                        }
                        else
                        {
                            nextStartIndex.set(valueLength);
                        }
                    }
                });
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
