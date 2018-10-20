package qub;

public abstract class LineWriteStreamBase implements LineWriteStream
{
    @Override
    public Result<Boolean> write(char toWrite)
    {
        return LineWriteStreamBase.write(this, toWrite);
    }

    @Override
    public Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        return LineWriteStreamBase.write(this, toWrite, formattedStringArguments);
    }

    @Override
    public Result<Boolean> writeLine()
    {
        return LineWriteStreamBase.writeLine(this);
    }

    @Override
    public Result<Boolean> writeLine(String toWrite, Object... formattedStringArguments)
    {
        return LineWriteStreamBase.writeLine(this, toWrite, formattedStringArguments);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return LineWriteStreamBase.getCharacterEncoding(this);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return LineWriteStreamBase.asByteWriteStream(this);
    }

    public static Result<Boolean> write(LineWriteStream lineWriteStream, char character)
    {
        return lineWriteStream.asCharacterWriteStream().write(character);
    }

    public static Result<Boolean> write(LineWriteStream lineWriteStream, String string, Object... formattedStringArguments)
    {
        return lineWriteStream.asCharacterWriteStream().write(string, formattedStringArguments);
    }

    public static Result<Boolean> writeLine(LineWriteStream lineWriteStream)
    {
        return lineWriteStream.write(lineWriteStream.getLineSeparator());
    }

    public static Result<Boolean> writeLine(LineWriteStream lineWriteStream, String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result = lineWriteStream.write(toWrite, formattedStringArguments);
        if (result.getValue() != null && result.getValue())
        {
            result = lineWriteStream.writeLine();
        }
        return result;
    }

    public static CharacterEncoding getCharacterEncoding(LineWriteStream lineWriteStream)
    {
        return lineWriteStream.asCharacterWriteStream().getCharacterEncoding();
    }

    public static ByteWriteStream asByteWriteStream(LineWriteStream lineWriteStream)
    {
        return lineWriteStream.asCharacterWriteStream().asByteWriteStream();
    }
}
