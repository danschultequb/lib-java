package qub;

public interface Characters
{
    /**
     * The minimum value that a character can have.
     */
    char minimumValue = java.lang.Character.MIN_VALUE;

    /**
     * The maximum value that a character can have.
     */
    char maximumValue = java.lang.Character.MAX_VALUE;

    /**
     * Get the Range of characters that includes all characters.
     */
    Range<Character> all = Range.between(minimumValue, maximumValue);

    /**
     * Get the String representation of the provided character.
     * @param character The character to change to a String.
     * @return The String representation of the provided character.
     */
    static String toString(char character)
    {
        return java.lang.Character.toString(character);
    }

    /**
     * Get whether or not the provided character is a quote character.
     * @param character The character to check.
     * @return Whether or not the provided character is a quote character.
     */
    static boolean isQuote(char character)
    {
        return character == '\'' || character == '\"';
    }

    /**
     * Get whether or not the provided character is a whitespace character.
     * @param character The character to check.
     * @return Whether or not the provided character is a whitespace character.
     */
    static boolean isWhitespace(char character)
    {
        return character == ' ' ||
            character == '\r' ||
            character == '\n' ||
            character == '\t';
    }

    /**
     * Surround the provided character with single quotes and textualize any escaped characters.
     * @param character The character to quote and escape.
     * @return The quoted and escaped character.
     */
    static String escapeAndQuote(char character)
    {
        return Strings.quote(Characters.escape(character));
    }

    /**
     * Escape the provided character if it is an escaped character (such as '\n' or '\t').
     * @param character The character to escape.
     * @return The escaped character.
     */
    static String escape(char character)
    {
        return Characters.escape(character, CharacterArray.create('\''));
    }

    /**
     * Escape the provided character if it is an escaped character (such as '\n' or '\t').
     * @param character The character to escape.
     * @param dontEscape Escape sequences that will not be escaped.
     * @return The escaped character.
     */
    static String escape(char character, Iterable<Character> dontEscape)
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
                    if (0xD800 <= character && character <= 0xDFFF)
                    {
                        result = "\\u+" + Integers.toHexString(character);
                    }
                    else
                    {
                        result = Characters.toString(character);
                    }
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
    static String quote(char character)
    {
        return Strings.quote(java.lang.Character.toString(character));
    }

    /**
     * Get the lower-cased version of the provided character.
     * @param value The character.
     * @return The lower-cased version of the provided character.
     */
    static char toLowerCase(char value)
    {
        return Character.toLowerCase(value);
    }

    /**
     * Get whether or not the provided value is a letter.
     * @param value The value to check.
     * @return Whether or not the provided value is a letter.
     */
    static boolean isLetter(char value)
    {
        return java.lang.Character.isLetter(value);
    }

    /**
     * Get whether or not the provided value is a digit.
     * @param value The value to check.
     * @return Whether or not the provided value is a digit.
     */
    static boolean isDigit(char value)
    {
        return java.lang.Character.isDigit(value);
    }

    /**
     * Get whether or not the provided value is a letter or a digit.
     * @param value The value to check.
     * @return Whether or not the provided value is a letter or a digit.
     */
    static boolean isLetterOrDigit(char value)
    {
        return java.lang.Character.isLetterOrDigit(value);
    }

    /**
     * Join the provided characters into a single String.
     * @param values The characters to joinStrings.
     * @return The joined characters.
     */
    static String join(Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        final String result = Characters.join(values.iterate());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided characters into a single String.
     * @param values The characters to joinStrings.
     * @return The joined characters.
     */
    static String join(Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        final String result = CharacterList.create(values).toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided characters into a single String.
     * @param separator The separator character that should be put between the values.
     * @param values The characters to joinStrings.
     * @return The joined characters.
     */
    static String join(char separator, Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Characters.join(separator, values.iterate());
    }

    /**
     * Join the provided characters into a single String.
     * @param separator The separator character that should be put between the values.
     * @param values The characters to joinStrings.
     * @return The joined characters.
     */
    static String join(char separator, Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        final CharacterList builder = CharacterList.create();

        values.start();
        if (values.hasCurrent())
        {
            builder.add(values.takeCurrent());
        }
        while (values.hasCurrent())
        {
            builder.add(separator);
            builder.add(values.takeCurrent());
        }
        final String result = builder.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided characters into a single String.
     * @param separator The separator that should be put between the values.
     * @param values The characters to joinStrings.
     * @return The joined characters.
     */
    static String join(String separator, Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        final String result = Characters.join(separator, values.iterate());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Join the provided characters into a single String.
     * @param separator The separator that should be put between the values.
     * @param values The characters to joinStrings.
     * @return The joined characters.
     */
    static String join(String separator, Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        final CharacterList builder = CharacterList.create();

        values.start();
        if (values.hasCurrent())
        {
            builder.add(values.takeCurrent());
        }
        while (values.hasCurrent())
        {
            builder.addAll(separator);
            builder.add(values.takeCurrent());
        }
        final String result = builder.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
