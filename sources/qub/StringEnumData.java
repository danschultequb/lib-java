package qub;

public class StringEnumData<T extends StringEnum<T>>
{
    private final Class<T> enumType;
    private final MutableMap<String,T> enumValues;
    private Function1<String,T> createEnumValueFunction;
    private Function1<String,String> getValueKeyFunction;

    private StringEnumData(Class<T> enumType)
    {
        PreCondition.assertNotNull(enumType, "enumType");

        this.enumType = enumType;
        this.enumValues = ListMap.create();

        this.setCreateEnumValueFunction(this::defaultCreateEnumValue);
        this.setGetValueKeyFunction(StringEnumData::defaultGetValueKey);
    }

    public static <T extends StringEnum<T>> StringEnumData<T> create(Class<T> enumType)
    {
        return new StringEnumData<>(enumType);
    }

    private T defaultCreateEnumValue(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");

        T result;
        try
        {
            result = this.enumType.getDeclaredConstructor().newInstance();
            result.value = value;
        }
        catch (Throwable e)
        {
            throw Exceptions.asRuntime(e);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the {@link Function1} that will be used to create new {@link StringEnum} values.
     * @param createEnumValueFunction The {@link Function1} that will be used to create new
     * {@link StringEnum} values.
     * @return This object for method chaining.
     */
    public StringEnumData<T> setCreateEnumValueFunction(Function1<String,T> createEnumValueFunction)
    {
        PreCondition.assertNotNull(createEnumValueFunction, "createEnumValueFunction");

        this.createEnumValueFunction = createEnumValueFunction;

        return this;
    }

    private static String defaultGetValueKey(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");

        return value;
    }

    /**
     * Set the {@link Function1} that will be used to generate unique keys for each enum value.
     * @param getValueKeyFunction The {@link Function1} that will be used to generate unique keys
     *                            for each enum value.
     * @return This object for method chaining.
     */
    public StringEnumData<T> setGetValueKeyFunction(Function1<String,String> getValueKeyFunction)
    {
        PreCondition.assertNotNull(getValueKeyFunction, "getValueKeyFunction");

        this.getValueKeyFunction = getValueKeyFunction;

        return this;
    }

    /**
     * Get the {@link StringEnum} value associated with the provided {@link String} value. If no
     * value is found, then a {@link NotFoundException} will be returned.
     * @param value The {@link String} value to look up.
     */
    public Result<T> get(String value)
    {
        return Result.create(() ->
        {
            final String valueKey = this.getValueKeyFunction.run(value);
            return this.enumValues.get(valueKey)
                .convertError(NotFoundException.class, () ->
                {
                    return new NotFoundException("No " + Types.getTypeName(this.enumType) + " enum value found for " + Strings.escapeAndQuote(value) + ".");
                })
                .await();
        });
    }

    /**
     * Get the {@link StringEnum} value associated with the provided {@link String} value. If no
     * value is found, then a new value will be created, added to this {@link StringEnumData}, and
     * then returned.
     * @param value The {@link String} value to look up.
     */
    public Result<T> getOrCreate(String value)
    {
        PreCondition.assertNotNull(this.createEnumValueFunction, "this.createEnumValueFunction");

        return Result.create(() ->
        {
            final String valueKey = this.getValueKeyFunction.run(value);
            return this.enumValues.getOrSet(valueKey, () -> this.createEnumValueFunction.run(value)).await();
        });
    }

    /**
     * Iterate over the {@link StringEnum} values that have been created in this
     * {@link StringEnumData}.
     */
    public Iterator<T> iterateValues()
    {
        return this.enumValues.iterateValues();
    }
}
