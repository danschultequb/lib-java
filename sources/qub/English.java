package qub;

/**
 * A collection of methods that help with making English strings.
 */
public interface English
{
    /**
     * Combine the provided values into an "and" (a, b, and c) or "or" list (a, b, or c).
     * @param values The values to form into a list.
     * @param listType The joining word in the list. Must be either "and" or "or".
     * @return The list String.
     */
    static String list(Iterable<?> values, String listType)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(listType, "listType");
        PreCondition.assertOneOf(listType.toLowerCase(), Iterable.create("and", "or"), "listType.toLowerCase()");

        final CharacterList builder = CharacterList.create();
        final int valuesCount = values.getCount();
        if (valuesCount == 1)
        {
            builder.addAll(Objects.toString(values.first()));
        }
        else if (valuesCount == 2)
        {
            builder.addAll(Objects.toString(values.first()));
            builder.add(' ');
            builder.addAll(listType);
            builder.add(' ');
            builder.addAll(Objects.toString(values.last()));
        }
        else if (valuesCount >= 3)
        {
            int valueIndex = 0;
            for (final Object value : values)
            {
                if (valueIndex > 0)
                {
                    builder.addAll(", ");
                }
                if (valueIndex == valuesCount - 1)
                {
                    builder.addAll(listType);
                    builder.add(' ');
                }

                builder.addAll(Objects.toString(value));

                ++valueIndex;
            }
        }

        final String result = builder.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Combine the provided values into an "or" list (a, b, or c).
     * @param values The values to form into a list.
     * @return The list String.
     */
    static String orList(Iterable<?> values)
    {
        return English.list(values, "or");
    }

    /**
     * Combine the provided values into an "or" list (a, b, or c).
     * @param values The values to form into a list.
     * @return The list String.
     */
    static String orList(Object... values)
    {
        return English.orList(Iterable.create(values));
    }

    /**
     * Combine the provided values into an "and" list (a, b, and c).
     * @param values The values to form into a list.
     * @return The list String.
     */
    static String andList(Iterable<?> values)
    {
        return English.list(values, "and");
    }

    /**
     * Combine the provided values into an "and" list (a, b, and c).
     * @param values The values to form into a list.
     * @return The list String.
     */
    static String andList(Object... values)
    {
        return English.andList(Iterable.create(values));
    }

    /**
     * Get the String representation of the provided value with its ordinal indicator (-st, -nd, -rd, or -th) added to
     * the end.
     * @param value The value to get the String representation of.
     * @return The String reprensentation of the provided value with its ordinal indocator added to the end.
     */
    static String addOrdinalIndicator(int value)
    {
        String result = Integers.toString(value);
        switch (Math.absoluteValue(value))
        {
            case 1:
                result += "st";
                break;

            case 2:
                result += "nd";
                break;

            case 3:
                result += "rd";
                break;

            default:
                result += "th";
                break;
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }
}
