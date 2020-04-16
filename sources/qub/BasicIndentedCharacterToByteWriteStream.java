package qub;

public class BasicIndentedCharacterToByteWriteStream extends BasicIndentedCharacterWriteStream implements IndentedCharacterToByteWriteStream
{
    private final CharacterToByteWriteStream characterToByteWriteStream;

    protected BasicIndentedCharacterToByteWriteStream(CharacterToByteWriteStream characterToByteWriteStream)
    {
        super(characterToByteWriteStream);

        this.characterToByteWriteStream = characterToByteWriteStream;
    }

    public static BasicIndentedCharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return BasicIndentedCharacterToByteWriteStream.create(CharacterToByteWriteStream.create(byteWriteStream));
    }

    public static BasicIndentedCharacterToByteWriteStream create(CharacterToByteWriteStream characterToByteWriteStream)
    {
        PreCondition.assertNotNull(characterToByteWriteStream, "characterToByteWriteStream");

        return new BasicIndentedCharacterToByteWriteStream(characterToByteWriteStream);
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream setCurrentIndent(Function0<String> currentIndentFunction)
    {
        return (BasicIndentedCharacterToByteWriteStream)super.setCurrentIndent(currentIndentFunction);
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream setCurrentIndent(String currentIndent)
    {
        return (BasicIndentedCharacterToByteWriteStream)super.setCurrentIndent(currentIndent);
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream setSingleIndent(String singleIndent)
    {
        return (BasicIndentedCharacterToByteWriteStream)super.setSingleIndent(singleIndent);
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream increaseIndent()
    {
        return (BasicIndentedCharacterToByteWriteStream)super.increaseIndent();
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream decreaseIndent()
    {
        return (BasicIndentedCharacterToByteWriteStream)super.decreaseIndent();
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.characterToByteWriteStream.getCharacterEncoding();
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterToByteWriteStream.setCharacterEncoding(characterEncoding);

        return this;
    }

    @Override
    public BasicIndentedCharacterToByteWriteStream setNewLine(String newLine)
    {
        return (BasicIndentedCharacterToByteWriteStream)super.setNewLine(newLine);
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return this.characterToByteWriteStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.characterToByteWriteStream.write(toWrite, startIndex, length);
    }
}
