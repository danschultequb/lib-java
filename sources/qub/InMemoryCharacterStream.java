package qub;

public interface InMemoryCharacterStream extends CharacterWriteStream, CharacterReadStream
{
    static InMemoryCharacterStream create()
    {
        return InMemoryCharacterStream.create("");
    }

    static InMemoryCharacterStream create(char... characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return InMemoryCharacterStream.create(java.lang.String.valueOf(characters));
    }

    static InMemoryCharacterStream create(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return InMemoryCharacterToByteStream.create(text);
    }

    default InMemoryCharacterStream setNewLine(char newLine)
    {
        return (InMemoryCharacterStream)CharacterWriteStream.super.setNewLine(newLine);
    }

    default InMemoryCharacterStream setNewLine(char[] newLine)
    {
        return (InMemoryCharacterStream)CharacterWriteStream.super.setNewLine(newLine);
    }

    InMemoryCharacterStream setNewLine(String newLine);

    Result<String> getText();

    InMemoryCharacterStream endOfStream();
}
