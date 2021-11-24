package qub;

/**
 * A decorator type for {@link CharacterToByteWriteStream} types.
 * @param <T> The type that is extending this decorator.
 */
public abstract class CharacterToByteWriteStreamDecorator<T extends CharacterToByteWriteStream> implements CharacterToByteWriteStream
{
    private final CharacterToByteWriteStream innerStream;

    protected CharacterToByteWriteStreamDecorator(CharacterToByteWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return this.innerStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.innerStream.write(toWrite, startIndex, length);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.innerStream.getCharacterEncoding();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        this.innerStream.setCharacterEncoding(characterEncoding);

        return (T)this;
    }

    @Override
    public String getNewLine()
    {
        return this.innerStream.getNewLine();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setNewLine(String newLine)
    {
        this.innerStream.setNewLine(newLine);

        return (T)this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        return this.innerStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return this.innerStream.write(toWrite, formattedStringArguments);
    }

    @Override
    public boolean isDisposed()
    {
        return this.innerStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.innerStream.dispose();
    }
}
