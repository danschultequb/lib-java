package qub;

/**
 * A class for reading lines from a stream.
 */
public interface LineReadStream extends AsyncDisposable, Iterator<String>
{
    default Result<String> readLine()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        int charactersRead = 0;
        final StringBuilder builder = new StringBuilder();
        final CharacterReadStream characterReadStream = asCharacterReadStream();

        if (getIncludeNewLines())
        {
            while (characterReadStream.next())
            {
                final char currentCharacter = characterReadStream.getCurrent();
                ++charactersRead;
                builder.append(currentCharacter);
                if (currentCharacter == '\n')
                {
                    break;
                }
            }
        }
        else
        {
            boolean previousCharacterWasCarriageReturn = false;
            while (characterReadStream.next())
            {
                final char currentCharacter = characterReadStream.getCurrent();
                ++charactersRead;

                if (currentCharacter == '\r')
                {
                    if (previousCharacterWasCarriageReturn)
                    {
                        builder.append('\r');
                    }
                    else
                    {
                        previousCharacterWasCarriageReturn = true;
                    }
                }
                else if (currentCharacter != '\n')
                {
                    if (previousCharacterWasCarriageReturn)
                    {
                        builder.append('\r');
                    }
                    previousCharacterWasCarriageReturn = false;

                    builder.append(currentCharacter);
                }
                else
                {
                    previousCharacterWasCarriageReturn = false;
                    break;
                }
            }

            if (!characterReadStream.hasCurrent() && previousCharacterWasCarriageReturn)
            {
                builder.append('\r');
            }
        }

        return Result.success(charactersRead == 0 ? null : builder.toString());
    }

    @Override
    default boolean next()
    {
        return readLine().getValue() != null;
    }

    @Override
    default boolean hasStarted()
    {
        return asCharacterReadStream().hasStarted();
    }

    default CharacterEncoding getCharacterEncoding()
    {
        return asCharacterReadStream().getCharacterEncoding();
    }

    @Override
    default boolean isDisposed()
    {
        return asCharacterReadStream().isDisposed();
    }

    boolean getIncludeNewLines();

    void setIncludeNewLines(boolean includeNewLines);

    default ByteReadStream asByteReadStream()
    {
        return asCharacterReadStream().asByteReadStream();
    }

    CharacterReadStream asCharacterReadStream();
}
