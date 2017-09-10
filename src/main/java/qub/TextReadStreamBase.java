package qub;

import java.io.IOException;

 class TextReadStreamBase extends ByteReadStreamBase implements TextReadStream
{
    private final ByteReadStream byteReadStream;
    private final CharacterEncoding characterEncoding;

    TextReadStreamBase(ByteReadStream byteReadStream)
    {
        this(byteReadStream, CharacterEncoding.ASCII);
    }

    TextReadStreamBase(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        this.byteReadStream = byteReadStream;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public boolean isOpen()
    {
        return byteReadStream.isOpen();
    }

    @Override
    public boolean close()
    {
        return byteReadStream.close();
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length) throws IOException
    {
        return byteReadStream.readBytes(output, startIndex, length);
    }

    @Override
    public char[] readCharacters(int charactersToRead) throws IOException
    {
        if (charactersToRead < 0) {
            charactersToRead = 0;
        }

        char[] result = new char[charactersToRead];

        if (charactersToRead > 0)
        {
            final int charactersRead = readCharacters(result);
            if (charactersRead == -1)
            {
                result = null;
            }
            else if (charactersRead < charactersToRead)
            {
                final char[] newResult = new char[charactersRead];
                if (charactersRead > 0)
                {
                    System.arraycopy(result, 0, newResult, 0, newResult.length);
                }
                result = newResult;
            }
        }

        return result;
    }

    @Override
    public String readString(int charactersToRead) throws IOException
    {
        final char[] characters = readCharacters(charactersToRead);
        return characters == null ? null : String.valueOf(characters);
    }

    @Override
    public int readCharacters(char[] output) throws IOException
    {
        return readCharacters(output, 0, output.length);
    }

    @Override
    public int readCharacters(char[] output, int startIndex, int length) throws IOException
    {
        final byte[] bytes = readBytes(length);
        final char[] characters = characterEncoding.decode(bytes);
        if (characters != null && characters.length > 0)
        {
            for (int i = 0; i < characters.length; ++i)
            {
                output[startIndex + i] = characters[i];
            }
        }
        return characters == null ? -1 : characters.length;
    }

    @Override
    public String readLine() throws IOException
    {
        return readLine(false);
    }

    @Override
    public String readLine(boolean includeNewLineInLine) throws IOException
    {
        String result;
        if (!isOpen())
        {
            result = null;
        }
        else
        {
            final StringBuilder builder = new StringBuilder();

            final char[] characters = new char[1];
            int charactersRead = readCharacters(characters);
            while (charactersRead >= 1)
            {
                builder.append(characters);
                if (characters[0] == '\n')
                {
                    break;
                }

                charactersRead = readCharacters(characters);
            }

            if (builder.length() == 0)
            {
                result = null;
            }
            else
            {
                result = builder.toString();

                if (result.endsWith("\n") && !includeNewLineInLine)
                {
                    if (result.endsWith("\r\n"))
                    {
                        result = result.substring(0, result.length() - 2);
                    }
                    else
                    {
                        result = result.substring(0, result.length() - 1);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get the ByteReadStream that this TextReadStream reads from.
     * @return The ByteReadStream that this TextReadStream reads from.
     */
    protected ByteReadStream getByteReadStream()
    {
        return byteReadStream;
    }

    /**
     * Get the CharacterEncoding that this TextReadStream uses.
     * @return The CharacterEncoding that this TextReadStream uses.
     */
    protected CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }
}
