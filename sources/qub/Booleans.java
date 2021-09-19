package qub;

/**
 * A collection of functions that relate to boolean values.
 */
public interface Booleans
{
    /**
     * Get whether or not the provided Boolean is true.
     * @param value The value to check.
     * @return Whether or not the provided Boolean is true.
     */
    static boolean isTrue(Boolean value)
    {
        return value != null && value;
    }

    /**
     * Get whether or not the provided Boolean is false. Null will not be considered false.
     * @param value The value to check.
     * @return Whether or not the provided Boolean is false.
     */
    static boolean isFalse(Boolean value)
    {
        return value != null && !value;
    }

    /**
     * Convert the provided boolean value to its String representation.
     * @param value The value to convert to its String representation.
     * @return The String representation of the provided value.
     */
    static String toString(boolean value)
    {
        return value ? "true" : "false";
    }

    /**
     * Convert the provided boolean value to its String representation.
     * @param value The value to convert to its String representation.
     * @return The String representation of the provided value.
     */
    static String toString(Boolean value)
    {
        PreCondition.assertNotNull(value, "value");

        return toString(value.booleanValue());
    }

    /**
     * Parse a boolean value from the provided String value in a case-sensitive way.
     * @param value The text to parse.
     * @return The result of parsing the provided text.
     */
    static Result<Boolean> parse(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");

        final Result<Boolean> result = Booleans.parse(value, true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Parse a boolean value from the provided String value in a case-sensitive way.
     * @param value The text to parse.
     * @param caseSensitive Whether or not to parse the text in a case-sensitive way.
     * @return The result of parsing the provided text.
     */
    static Result<Boolean> parse(String value, boolean caseSensitive)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");

        return Result.create(() ->
        {
            boolean result;

            final String toParse = caseSensitive ? value : value.toLowerCase();
            if ("true".equals(toParse))
            {
                result = true;
            }
            else if ("false".equals(toParse))
            {
                result = false;
            }
            else
            {
                throw new ParseException("Expected the value (" + Strings.escapeAndQuote(value) + ") to be either \"true\" or \"false\".");
            }

            return result;
        });
    }
}
