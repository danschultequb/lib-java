package qub;

public class EchoCharacterToByteReadStream implements CharacterToByteReadStream
{
    private final CharacterToByteReadStream readStream;
    private final CharacterToByteWriteStream writeStream;

    private EchoCharacterToByteReadStream(CharacterToByteReadStream readStream, CharacterToByteWriteStream writeStream)
    {
        PreCondition.assertNotNull(readStream, "readStream");
        PreCondition.assertNotNull(writeStream, "writeStream");

        this.readStream = readStream;
        this.writeStream = writeStream;
    }

    public static EchoCharacterToByteReadStream create(CharacterToByteReadStream readStream, CharacterToByteWriteStream writeStream)
    {
        return new EchoCharacterToByteReadStream(readStream, writeStream);
    }

    @Override
    public EchoCharacterToByteReadStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.readStream.setCharacterEncoding(characterEncoding);
        
        return this;
    }

    @Override
    public Result<Byte> readByte()
    {
        return Result.create(() ->
        {
            final byte result = this.readStream.readByte().await();
            this.writeStream.write(result).await();

            return result;
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        
        return Result.create(() ->
        {
            final Integer result = this.readStream.readBytes(outputBytes, startIndex, length).await();
            this.writeStream.writeAll(outputBytes, startIndex, result).await();
            
            return result;
        });
    }

    @Override
    public Result<Character> readCharacter()
    {
        return Result.create(() ->
        {
            final char result = this.readStream.readCharacter().await();
            this.writeStream.write(result).await();

            return result;
        });
    }

    @Override
    public Result<Integer> readCharacters(char[] outputCharacters, int startIndex, int length)
    {
        return Result.create(() ->
        {
            final int result = this.readStream.readCharacters(outputCharacters, startIndex, length).await();
            this.writeStream.write(outputCharacters, startIndex, result).await();

            return result;
        });
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.readStream.getCharacterEncoding();
    }

    @Override
    public boolean isDisposed()
    {
        return this.readStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.readStream.dispose();
    }
}
