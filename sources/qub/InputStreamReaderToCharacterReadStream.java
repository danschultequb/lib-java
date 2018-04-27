package qub;

import java.io.IOException;
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
        byteReadStream.setExceptionHandler(new Action1<IOException>()
        {
            @Override
            public void run(IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        this.characterEncoding = characterEncoding;

        final InputStream inputStream = byteReadStream.asInputStream();
        final Charset charset = characterEncoding.getCharset();
        this.reader = new InputStreamReader(inputStream, charset);
    }

    @Override
    public void close()
    {
        DisposableBase.close(this);
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
        hasStarted = true;

        Throwable error = null;
        try
        {
            final int characterAsInt = reader.read();
            if (characterAsInt >= 0)
            {
                current = (char) characterAsInt;
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

        return Result.done(current, error);
    }

    @Override
    public Result<Integer> readCharacters(char[] characters)
    {
        Result<Integer> result = Result.notNull(characters, "characters");
        if (result == null)
        {
            result = Result.greaterThan(0, characters.length, "characters.length");
            if (result == null)
            {
                hasStarted = true;

                Integer charactersRead = null;
                Throwable error = null;
                try
                {
                    charactersRead = reader.read(characters);
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
                        current = characters[charactersRead - 1];
                    }
                }

                result = Result.done(charactersRead, error);
            }
        }
        return result;
    }

    @Override
    public Result<Integer> readCharacters(char[] characters, int startIndex, int length)
    {
        Result<Integer> result = Result.notNull(characters, "characters");
        if (result == null)
        {
            result = Result.between(0, startIndex, characters.length - 1, "startIndex");
            if (result == null)
            {
                result = Result.between(1, length, characters.length - startIndex, "length");
                if (result == null)
                {
                    hasStarted = true;

                    int charactersRead = -1;
                    try
                    {
                        charactersRead = reader.read(characters, startIndex, length);
                        result = Result.success(charactersRead);
                    }
                    catch (Exception e)
                    {
                        result = Result.error(e);
                    }

                    if (charactersRead < 0)
                    {
                        current = null;
                    }
                    else if (charactersRead > 0)
                    {
                        current = characters[startIndex + charactersRead - 1];
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
