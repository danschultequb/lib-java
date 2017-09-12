package qub;

import java.io.IOException;

/**
 * A Console platform that can be used to write Console applications.
 */
public class Console implements TextWriteStream, TextReadStream, Random
{
    final Value<TextWriteStream> writeStream;
    final Value<TextReadStream> readStream;
    final Value<Random> random;

    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console()
    {
        writeStream = new Value<>();
        readStream = new Value<>();
        random = new Value<>();
    }

    /**
     * Set the TextWriteStream that is assigned to this Console.
     * @param writeStream The TextWriteStream that is assigned to this Console.
     */
    public void setWriteStream(TextWriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    /**
     * Get the TextWriteStream that is assigned to this Console.
     * @return
     */
    TextWriteStream getWriteStream()
    {
        if (!writeStream.hasValue())
        {
            writeStream.set(new StandardOutputTextWriteStream());
        }
        return writeStream.get();
    }

    /**
     * Set the TextReadStream that is assigned to this Console.
     * @param readStream The TextReadStream that is assigned to this Console.
     */
    public void setReadStream(TextReadStream readStream)
    {
        this.readStream.set(readStream);
    }

    /**
     * Get the TextReadStream that is assigned to this Console.
     * @return The TextReadStream that is assigned to this Console.
     */
    TextReadStream getReadStream()
    {
        if (!readStream.hasValue())
        {
            readStream.set(new StandardInputTextReadStream());
        }
        return readStream.get();
    }

    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public void write(byte toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite, startIndex, length);
        }
    }

    @Override
    public void write(String toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void writeLine()
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine();
        }
    }

    @Override
    public void writeLine(String toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine(toWrite);
        }
    }

    @Override
    public byte[] readBytes(int bytesToRead) throws IOException
    {
        byte[] result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(bytesToRead);
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(output);
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(output, startIndex, length);
        }

        return result;
    }

    @Override
    public char[] readCharacters(int charactersToRead) throws IOException
    {
        char[] result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(charactersToRead);
        }

        return result;
    }

    @Override
    public int readCharacters(char[] output) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(output);
        }

        return result;
    }

    @Override
    public int readCharacters(char[] output, int startIndex, int length) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(output, startIndex, length);
        }

        return result;
    }

    @Override
    public String readString(int stringLength) throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readString(stringLength);
        }

        return result;
    }

    @Override
    public String readLine() throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readLine();
        }

        return result;
    }

    @Override
    public String readLine(boolean includeNewLineInLine) throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readLine(includeNewLineInLine);
        }

        return result;
    }

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    void setRandom(Random random)
    {
        this.random.set(random);
    }

    /**
     * Get the Random number generator assigned to this Console.
     * @return The Random number generator assigned to this Console.
     */
    public Random getRandom()
    {
        if (!random.hasValue())
        {
            random.set(new JavaRandom());
        }
        return random.get();
    }

    @Override
    public int getRandomInteger()
    {
        int result = 0;

        final Random random = getRandom();
        if (random != null)
        {
            result = random.getRandomInteger();
        }

        return result;
    }

    @Override
    public int getRandomIntegerBetween(int lowerBound, int upperBound)
    {
        int result = lowerBound;

        final Random random = getRandom();
        if (random != null)
        {
            result = random.getRandomIntegerBetween(lowerBound, upperBound);
        }

        return result;
    }

    @Override
    public boolean getRandomBoolean()
    {
        boolean result = false;

        final Random random = getRandom();
        if (random != null)
        {
            result = random.getRandomBoolean();
        }

        return result;
    }
}
