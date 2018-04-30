package qub;

public interface CharacterReadStream extends AsyncDisposable, Iterator<Character>
{
    Result<Character> readCharacter();

    Result<char[]> readCharacters(int charactersToRead);

    Result<Integer> readCharacters(char[] characters);

    Result<Integer> readCharacters(char[] characters, int startIndex, int length);

    Result<String> readString(int charactersToRead);

    CharacterEncoding getEncoding();

    ByteReadStream asByteReadStream();

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(boolean includeNewLines);
}
