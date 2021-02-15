package qub;

public class JSONNumber implements JSONSegment
{
    private final String text;

    private JSONNumber(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.text = text;
    }

    public static JSONNumber create(long value)
    {
        return JSONNumber.create(Longs.toString(value));
    }

    public static JSONNumber create(double value)
    {
        return JSONNumber.create(Doubles.toString(value));
    }

    public static JSONNumber create(Number value)
    {
        PreCondition.assertNotNull(value, "value");

        return JSONNumber.create(value.toString());
    }

    public static JSONNumber create(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return new JSONNumber(text);
    }

    public Number getNumberValue()
    {
        Number result = this.getLongValue().catchError().await();
        if (result == null)
        {
            result = this.getDoubleValue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Result<Integer> getIntegerValue()
    {
        return Integers.parse(this.text);
    }

    public Result<Long> getLongValue()
    {
        return Longs.parse(this.text);
    }

    public Double getDoubleValue()
    {
        return Doubles.parse(this.text).await();
    }

    @Override
    public String toString()
    {
        return JSONSegment.toString(this);
    }

    @Override
    public Result<Integer> toString(IndentedCharacterWriteStream stream, JSONFormat format)
    {
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertNotNull(format, "format");

        return stream.write(this.text);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof JSONNumber && this.equals((JSONNumber)rhs);
    }

    public boolean equals(JSONNumber rhs)
    {
        return rhs != null &&
            this.text.equals(rhs.text);
    }
}
