package qub;

/**
 * A class for reading lines from a stream.
 */
public interface LineReadStream extends AsyncDisposable, Iterator<String>
{
    Result<String> readLine();

    CharacterEncoding getCharacterEncoding();

    boolean getIncludeNewLines();

    void setIncludeNewLines(boolean includeNewLines);

    ByteReadStream asByteReadStream();

    CharacterReadStream asCharacterReadStream();
}
