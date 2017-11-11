package qub;

public interface CharacterReadStream extends Stream, Iterator<Character>
{
    Character readCharacter();

    char[] readCharacters(int charactersToRead);

    int readCharacters(char[] characters);

    int readCharacters(char[] characters, int startIndex, int length);

    String readString(int charactersToRead);

    CharacterEncoding getEncoding();

    ByteReadStream asByteReadStream();

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(boolean includeNewLines);
}
