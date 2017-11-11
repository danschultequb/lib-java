package qub;

public class CharacterReadStreamToLineReadStream extends IteratorBase<String> implements LineReadStream
{
    private final CharacterReadStream readStream;
    private final boolean includeNewLines;
    private boolean hasStarted;
    private String current;

    public CharacterReadStreamToLineReadStream(CharacterReadStream readStream)
    {
        this(readStream, false);
    }

    public CharacterReadStreamToLineReadStream(CharacterReadStream readStream, boolean includeNewLines)
    {
        this.readStream = readStream;
        this.includeNewLines = includeNewLines;
    }

    @Override
    public String readLine()
    {
        return readLine(includeNewLines);
    }

    @Override
    public String readLine(boolean includeNewLine)
    {
        hasStarted = true;

        String result = null;

        Character currentCharacter = readStream.readCharacter();
        if (currentCharacter != null)
        {
            final StringBuilder builder = new StringBuilder();

            do
            {
                builder.append(currentCharacter);
                if (currentCharacter == '\n')
                {
                    break;
                }
                currentCharacter = readStream.readCharacter();
            }
            while (currentCharacter != null);

            if (!includeNewLine)
            {
                final int length = builder.length();
                if (builder.charAt(length - 1) == '\n')
                {
                    int charactersToRemove = 1;
                    if (length >= 2 && builder.charAt(length - 2) == '\r')
                    {
                        charactersToRemove = 2;
                    }

                    builder.delete(length - charactersToRemove, length);
                }
            }

            result = builder.toString();
        }

        current = result;

        return result;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return readStream.getEncoding();
    }

    @Override
    public boolean getIncludeNewLines()
    {
        return includeNewLines;
    }

    @Override
    public boolean isOpen()
    {
        return readStream.isOpen();
    }

    @Override
    public boolean close()
    {
        current = null;

        return readStream.close();
    }

    @Override
    public ByteReadStream asByteReadStream()
    {
        return readStream.asByteReadStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return readStream;
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
    public String getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        return readLine() != null;
    }
}
