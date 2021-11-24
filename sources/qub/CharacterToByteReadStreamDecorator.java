package qub;

public class CharacterToByteReadStreamDecorator<T extends CharacterToByteReadStream> implements CharacterToByteReadStream
{
    private final CharacterToByteReadStream innerStream;

    protected CharacterToByteReadStreamDecorator(CharacterToByteReadStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
    }

    @Override
    public Result<Byte> readByte()
    {
        return this.innerStream.readByte();
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return this.innerStream.readBytes(outputBytes, startIndex, length);
    }

    @Override
    public Result<Character> readCharacter()
    {
        return this.innerStream.readCharacter();
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
