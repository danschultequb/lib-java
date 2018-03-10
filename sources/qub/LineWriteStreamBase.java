package qub;

public abstract class LineWriteStreamBase implements LineWriteStream
{
    @Override
    public boolean write(char toWrite)
    {
        return LineWriteStreamBase.write(this, toWrite);
    }

    @Override
    public boolean write(String toWrite, Object... formattedStringArguments)
    {
        return LineWriteStreamBase.write(this, toWrite, formattedStringArguments);
    }

    @Override
    public boolean writeLine()
    {
        return LineWriteStreamBase.writeLine(this);
    }

    @Override
    public boolean writeLine(String toWrite, Object... formattedStringArguments)
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

    public static boolean write(LineWriteStream lineWriteStream, char character)
    {
        return lineWriteStream.asCharacterWriteStream().write(character);
    }

    public static boolean write(LineWriteStream lineWriteStream, String string, Object... formattedStringArguments)
    {
        return lineWriteStream.asCharacterWriteStream().write(string, formattedStringArguments);
    }

    public static boolean writeLine(LineWriteStream lineWriteStream)
    {
        return lineWriteStream.write(lineWriteStream.getLineSeparator());
    }

    public static boolean writeLine(LineWriteStream lineWriteStream, String toWrite, Object... formattedStringArguments)
    {
        return lineWriteStream.write(toWrite, formattedStringArguments) && lineWriteStream.writeLine();
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
