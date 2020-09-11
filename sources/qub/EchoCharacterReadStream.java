package qub;

public class EchoCharacterReadStream implements CharacterReadStream
{
    private final CharacterReadStream readStream;
    private final CharacterWriteStream writeStream;

    private EchoCharacterReadStream(CharacterReadStream readStream, CharacterWriteStream writeStream)
    {
        PreCondition.assertNotNull(readStream, "readStream");
        PreCondition.assertNotNull(writeStream, "writeStream");

        this.readStream = readStream;
        this.writeStream = writeStream;
    }

    public static EchoCharacterReadStream create(CharacterReadStream readStream, CharacterWriteStream writeStream)
    {
        return new EchoCharacterReadStream(readStream, writeStream);
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
