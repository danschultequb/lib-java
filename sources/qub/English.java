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
    static String list(Iterable<String> values, String listType)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNullAndNotEmpty(listType, "listType");
        PreCondition.assertOneOf(listType.toLowerCase(), Iterable.create("and", "or"), "listType.toLowerCase()");

        final CharacterList builder = CharacterList.create();
        final int valuesCount = values.getCount();
        if (valuesCount == 1)
        {
            builder.addAll(values.first());
        }
        else if (valuesCount == 2)
        {
            builder.addAll(values.first());
            builder.add(' ');
            builder.addAll(listType);
            builder.add(' ');
            builder.addAll(values.last());
        }
        else if (valuesCount >= 3)
        {
            int valueIndex = 0;
            for (final String value : values)
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

                builder.addAll(value);

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
    static String orList(Iterable<String> values)
    {
        return English.list(values, "or");
    }

    /**
     * Combine the provided values into an "and" list (a, b, and c).
     * @param values The values to form into a list.
     * @return The list String.
     */
    static String andList(Iterable<String> values)
    {
        return English.list(values, "and");
    }
}
