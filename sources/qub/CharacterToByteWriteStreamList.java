package qub;

/**
 * A list of CharacterToByteWriteStreams that can be treated as a single CharacterWriteStream.
 */
public class CharacterToByteWriteStreamList implements CharacterToByteWriteStream, List<CharacterToByteWriteStream>
{
    private final List<CharacterToByteWriteStream> innerStreams;
    private boolean isDisposed;
    private String newLine;

    private CharacterToByteWriteStreamList()
    {
        this.innerStreams = List.create();
        this.newLine = "\n";
    }

    /**
     * Create a new CharacterWriteStreamList from the provided CharacterWriteStreams.
     * @param characterWriteStreams The CharacterWriteStreams to add to the resulting CharacterWriteStreamList.
     * @return A new CharacterWriteStreamList that contains the provided CharacterWriteStreams.
     */
    public static CharacterToByteWriteStreamList create(CharacterToByteWriteStream... characterWriteStreams)
    {
        return new CharacterToByteWriteStreamList()
            .addAll(characterWriteStreams);
    }

    public static CharacterToByteWriteStreamList create(Iterable<? extends CharacterToByteWriteStream> characterWriteStreams)
    {
        return new CharacterToByteWriteStreamList()
            .addAll(characterWriteStreams);
    }

    @Override
    public String getNewLine()
    {
        return this.newLine;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        CharacterEncoding result = null;
        if (this.innerStreams.any())
        {
            result = this.innerStreams.first().getCharacterEncoding();
        }
        return result;
    }

    @Override
    public CharacterToByteWriteStreamList setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        for (final CharacterToByteWriteStream innerStream : this.innerStreams)
        {
            innerStream.setCharacterEncoding(characterEncoding);
        }

        return this;
    }

    @Override
    public CharacterToByteWriteStreamList setNewLine(char newLine)
    {
        return (CharacterToByteWriteStreamList)CharacterToByteWriteStream.super.setNewLine(newLine);
    }

    @Override
    public CharacterToByteWriteStreamList setNewLine(char[] newLine)
    {
        return (CharacterToByteWriteStreamList)CharacterToByteWriteStream.super.setNewLine(newLine);
    }

    @Override
    public CharacterToByteWriteStreamList setNewLine(String newLine)
    {
        PreCondition.assertNotNullAndNotEmpty(newLine, "newLine");
        PreCondition.assertNotDisposed(this, "this");

        this.newLine = newLine;
        for (final CharacterWriteStream stream : this.innerStreams)
        {
            stream.setNewLine(newLine);
        }

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            int result = 0;
            for (final CharacterWriteStream stream : this.innerStreams)
            {
                result += stream.write(toWrite).await();
            }
            return result;
        });
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotNull(formattedStringArguments, "formattedStringArguments");
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            int result = 0;
            for (final CharacterWriteStream stream : this.innerStreams)
            {
                result += stream.write(toWrite, formattedStringArguments).await();
            }
            return result;
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = !this.isDisposed;
            if (result)
            {
                this.isDisposed = true;
                for (final CharacterWriteStream stream : this.innerStreams)
                {
                    stream.dispose().await();
                }
            }
            return result;
        });
    }

    @Override
    public CharacterToByteWriteStreamList set(int index, CharacterToByteWriteStream value)
    {
        PreCondition.assertNotDisposed(this, "this");

        this.innerStreams.set(index, value);
        return this;
    }

    @Override
    public CharacterToByteWriteStreamList insert(int insertIndex, CharacterToByteWriteStream value)
    {
        PreCondition.assertNotDisposed(this, "this");

        this.innerStreams.insert(insertIndex, value);
        return this;
    }

    @Override
    public CharacterToByteWriteStreamList add(CharacterToByteWriteStream value)
    {
        return (CharacterToByteWriteStreamList)List.super.add(value);
    }

    @Override
    public CharacterToByteWriteStreamList addAll(CharacterToByteWriteStream... values)
    {
        return (CharacterToByteWriteStreamList)List.super.addAll(values);
    }

    @Override
    public CharacterToByteWriteStreamList addAll(Iterator<? extends CharacterToByteWriteStream> values)
    {
        return (CharacterToByteWriteStreamList)List.super.addAll(values);
    }

    @Override
    public CharacterToByteWriteStreamList addAll(Iterable<? extends CharacterToByteWriteStream> values)
    {
        return (CharacterToByteWriteStreamList)List.super.addAll(values);
    }

    @Override
    public CharacterToByteWriteStreamList clear()
    {
        this.innerStreams.clear();
        return this;
    }

    @Override
    public CharacterToByteWriteStream removeAt(int index)
    {
        return this.innerStreams.removeAt(index);
    }

    @Override
    public CharacterToByteWriteStream get(int index)
    {
        return this.innerStreams.get(index);
    }

    @Override
    public Iterator<CharacterToByteWriteStream> iterate()
    {
        return this.innerStreams.iterate();
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return Result.create(() ->
        {
            int result = 0;
            for (final CharacterToByteWriteStream innerStream : this.innerStreams)
            {
                result += innerStream.write(toWrite).await();
            }
            return result;
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return Result.create(() ->
        {
            int result = 0;
            for (final CharacterToByteWriteStream innerStream : this.innerStreams)
            {
                result += innerStream.write(toWrite, startIndex, length).await();
            }
            return result;
        });
    }
}
