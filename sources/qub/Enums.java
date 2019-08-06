package qub;

/**
 * A collection of helper methods for interacting with enumerated types.
 */
public interface Enums
{
    /**
     * Parse the provided String into the provided enumerated type.
     * @param enumType The type of Enum to parse from the text.
     * @param text The text to parse.
     * @param <T> The enumerated type to return.
     * @return The result of parsing the text.
     */
    static <T extends Enum<T>> Result<T> parse(Class<T> enumType, String text)
    {
        PreCondition.assertNotNull(enumType, "enumType");
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        T enumResult = null;
        for (final T possibleEnumValue : enumType.getEnumConstants())
        {
            if (possibleEnumValue.toString().equalsIgnoreCase(text))
            {
                enumResult = possibleEnumValue;
                break;
            }
        }
        return enumResult != null
            ? Result.success(enumResult)
            : Result.error(new NotFoundException("No enum value for " + Types.getFullTypeName(enumType) + " matches " + Strings.escapeAndQuote(text) + "."));
    }
}
