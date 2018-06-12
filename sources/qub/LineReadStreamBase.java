package qub;

public abstract class LineReadStreamBase extends IteratorBase<String> implements LineReadStream
{
    @Override
    public Result<String> readLine()
    {
        return LineReadStreamBase.readLine(this);
    }

    @Override
    public final boolean next()
    {
        return LineReadStreamBase.next(this);
    }

    @Override
    public final boolean hasStarted()
    {
        return LineReadStreamBase.hasStarted(this);
    }

    @Override
    public final CharacterEncoding getCharacterEncoding()
    {
        return LineReadStreamBase.getCharacterEncoding(this);
    }

    @Override
    public final AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return AsyncDisposableBase.disposeAsync(this);
    }

    @Override
    public final void close()
    {
        DisposableBase.close(this);
    }

    @Override
    public final boolean isDisposed()
    {
        return LineReadStreamBase.isDisposed(this);
    }

    @Override
    public final ByteReadStream asByteReadStream()
    {
        return LineReadStreamBase.asByteReadStream(this);
    }

    public static Result<String> readLine(LineReadStream lineReadStream)
    {
        Result<String> result = Result.notNull(lineReadStream, "lineReadStream");
        if (result == null)
        {
            result = Result.notDisposed(lineReadStream, "lineReadStream");
            if (result == null)
            {
                int charactersRead = 0;
                final StringBuilder builder = new StringBuilder();
                final CharacterReadStream characterReadStream = lineReadStream.asCharacterReadStream();

                if (lineReadStream.getIncludeNewLines())
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

                result = Result.success(charactersRead == 0 ? null : builder.toString());
            }
        }
        return result;
    }

    public static boolean next(LineReadStream lineReadStream)
    {
        return lineReadStream.readLine().getValue() != null;
    }

    public static boolean hasStarted(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().hasStarted();
    }

    public static CharacterEncoding getCharacterEncoding(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().getCharacterEncoding();
    }

    public static boolean isDisposed(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().isDisposed();
    }

    public static ByteReadStream asByteReadStream(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().asByteReadStream();
    }
}
