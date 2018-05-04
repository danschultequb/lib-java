package qub;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * A class for reading characters from a stream.
 */
class InputStreamReaderToCharacterReadStream extends CharacterReadStreamBase
{
    private final ByteReadStream byteReadStream;
    private final CharacterEncoding characterEncoding;
    private final InputStreamReader reader;
    private boolean hasStarted;
    private Character current;

    InputStreamReaderToCharacterReadStream(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        this.byteReadStream = byteReadStream;
        this.characterEncoding = characterEncoding;

        final InputStream inputStream = byteReadStream.asInputStream();
        final Charset charset = characterEncoding.getCharset();
        this.reader = new InputStreamReader(inputStream, charset);
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return byteReadStream.getAsyncRunner();
    }

    @Override
    public boolean isDisposed()
    {
        return byteReadStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return byteReadStream.dispose();
    }

    @Override
    public Result<Character> readCharacter()
    {
        Result<Character> result = CharacterReadStreamBase.validateCharacterReadStream(this);
        if (result == null)
        {
            hasStarted = true;

            Throwable error = null;
            try
            {
                final int characterAsInt = reader.read();
                if (characterAsInt >= 0)
                {
                    current = (char)characterAsInt;
                }
                else
                {
                    current = null;
                }
            }
            catch (Exception e)
            {
                current = null;
                error = e;
            }
            result = Result.done(current, error);
        }
        return result;
    }

    @Override
    public Result<Integer> readCharacters(char[] outputCharacters)
    {
        Result<Integer> result = CharacterReadStreamBase.validateCharacterReadStream(this);
        if (result == null)
        {
            result = CharacterReadStreamBase.validateOutputCharacters(outputCharacters);
            if (result == null)
            {
                hasStarted = true;

                Integer charactersRead = null;
                Throwable error = null;
                try
                {
                    charactersRead = reader.read(outputCharacters);
                }
                catch (Exception e)
                {
                    error = e;
                }

                if (charactersRead != null)
                {
                    if (charactersRead < 0)
                    {
                        current = null;
                        charactersRead = null;
                    }
                    else if (charactersRead > 0)
                    {
                        current = outputCharacters[charactersRead - 1];
                    }
                }

                result = Result.done(charactersRead, error);
            }
        }
        return result;
    }

    @Override
    public Result<Integer> readCharacters(char[] outputCharacters, int startIndex, int length)
    {
        Result<Integer> result = CharacterReadStreamBase.validateCharacterReadStream(this);
        if (result == null)
        {
            result = CharacterReadStreamBase.validateOutputCharacters(outputCharacters);
            if (result == null)
            {
                result = Result.between(0, startIndex, outputCharacters.length - 1, "startIndex");
                if (result == null)
                {
                    result = Result.between(1, length, outputCharacters.length - startIndex, "length");
                    if (result == null)
                    {
                        hasStarted = true;

                        Integer charactersRead = null;
                        Throwable error = null;
                        try
                        {
                            charactersRead = reader.read(outputCharacters, startIndex, length);
                        }
                        catch (Exception e)
                        {
                            error = e;
                        }

                        if (charactersRead != null)
                        {
                            if (charactersRead < 0)
                            {
                                current = null;
                                charactersRead = null;
                            }
                            else if (charactersRead > 0)
                            {
                                current = outputCharacters[charactersRead - 1];
                            }
                        }

                        result = Result.done(charactersRead, error);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public CharacterEncoding getEncoding()
    {
        return characterEncoding;
    }

    @Override
    public ByteReadStream asByteReadStream()
    {
        return byteReadStream;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return current != null;
    }

    @Override
    public Character getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        return readCharacter().getValue() != null;
    }
}
