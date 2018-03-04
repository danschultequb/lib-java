package qub;

public abstract class LineReadStreamBase extends IteratorBase<String> implements LineReadStream
{
    @Override
    public final String readLine()
    {
        return LineReadStreamBase.readLine(this);
    }

    @Override
    public String readLine(boolean includeNewLines)
    {
        return LineReadStreamBase.readLine(this, includeNewLines);
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
    public final boolean isOpen()
    {
        return LineReadStreamBase.isOpen(this);
    }

    @Override
    public final ByteReadStream asByteReadStream()
    {
        return LineReadStreamBase.asByteReadStream(this);
    }

    public static String readLine(LineReadStream lineReadStream)
    {
        return lineReadStream.readLine(lineReadStream.getIncludeNewLines());
    }

    public static String readLine(LineReadStream lineReadStream, boolean includeNewLines)
    {
        String result = null;

        if (lineReadStream.isOpen())
        {
            final StringBuilder builder = new StringBuilder();
            final CharacterReadStream characterReadStream = lineReadStream.asCharacterReadStream();
            characterReadStream.ensureHasStarted();

            if (includeNewLines)
            {
                while (characterReadStream.hasCurrent())
                {
                    final char currentCharacter = characterReadStream.takeCurrent();
                    builder.append(currentCharacter);
                    if (currentCharacter == '\n')
                    {
                        break;
                    }
                }
            }
            else
            {
                int carriageReturnCount = 0;
                while (characterReadStream.hasCurrent())
                {
                    final char currentCharacter = characterReadStream.takeCurrent();
                    if (currentCharacter == '\r')
                    {
                        ++carriageReturnCount;
                    }
                    else if (currentCharacter == '\n')
                    {
                        while (carriageReturnCount > 1)
                        {
                            builder.append('\r');
                            --carriageReturnCount;
                        }
                        break;
                    }
                    else
                    {
                        while (carriageReturnCount > 0)
                        {
                            builder.append('\r');
                            --carriageReturnCount;
                        }
                        builder.append(currentCharacter);
                    }
                }
            }

            result = builder.length() == 0 ? null : builder.toString();
        }

        return result;
    }

    public static boolean next(LineReadStream lineReadStream)
    {
        return lineReadStream.readLine() != null;
    }

    public static boolean hasStarted(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().hasStarted();
    }

    public static CharacterEncoding getCharacterEncoding(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().getEncoding();
    }

    public static boolean isOpen(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().isOpen();
    }

    public static ByteReadStream asByteReadStream(LineReadStream lineReadStream)
    {
        return lineReadStream.asCharacterReadStream().asByteReadStream();
    }
}
