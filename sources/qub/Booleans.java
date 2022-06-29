package qub;

/**
 * A collection of functions that relate to boolean values.
 */
public interface Booleans
{
    /**
     * Get whether the provided {@link Boolean} is true.
     * @param value The value to check.
     */
    public static boolean isTrue(Boolean value)
    {
        return value != null && value;
    }

    /**
     * Get whether the provided {@link Boolean} is false. Null will not be considered false.
     * @param value The value to check.
     */
    public static boolean isFalse(Boolean value)
    {
        return value != null && !value;
    }

    /**
     * Convert the provided boolean value to its {@link String} representation.
     * @param value The value to convert to its {@link String} representation.
     * @return The {@link String} representation of the provided value.
     */
    public static String toString(boolean value)
    {
        return value ? "true" : "false";
    }

    /**
     * Convert the provided {@link Boolean} value to its {@link String} representation.
     * @param value The value to convert to its {@link String} representation.
     */
    public static String toString(Boolean value)
    {
        return value == null ? "null" : Booleans.toString(value.booleanValue());
    }

    /**
     * Parse a boolean value from the provided {@link String} value in a case-sensitive way.
     * @param value The text to parse.
     */
    public static Result<Boolean> parse(String value)
    {
        return Booleans.parse(value, true);
    }

    /**
     * Parse a boolean value from the provided {@link String} value in a case-sensitive way.
     * @param value The text to parse.
     * @param caseSensitive Whether to parse the text in a case-sensitive way.
     */
    public static Result<Boolean> parse(String value, boolean caseSensitive)
    {
        return Result.create(() ->
        {
            boolean result;

            final String toParse = (value == null || caseSensitive ? value : value.toLowerCase());
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
