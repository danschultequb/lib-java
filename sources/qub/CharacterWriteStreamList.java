package qub;

/**
 * A list of CharacterWriteStreams that can be treated as a single CharacterWriteStream.
 */
public class CharacterWriteStreamList implements CharacterWriteStream, List<CharacterWriteStream>
{
    private final List<CharacterWriteStream> innerStreams;
    private boolean isDisposed;
    private String newLine;

    private CharacterWriteStreamList()
    {
        this.innerStreams = List.create();
        this.newLine = "\n";
    }

    /**
     * Create a new CharacterWriteStreamList from the provided CharacterWriteStreams.
     * @param characterWriteStreams The CharacterWriteStreams to add to the resulting CharacterWriteStreamList.
     * @return A new CharacterWriteStreamList that contains the provided CharacterWriteStreams.
     */
    public static CharacterWriteStreamList create(CharacterWriteStream... characterWriteStreams)
    {
        return new CharacterWriteStreamList()
            .addAll(characterWriteStreams);
    }

    public static CharacterWriteStreamList create(Iterable<? extends CharacterWriteStream> characterWriteStreams)
    {
        return new CharacterWriteStreamList()
            .addAll(characterWriteStreams);
    }

    @Override
    public String getNewLine()
    {
        return this.newLine;
    }

    @Override
    public CharacterWriteStreamList setNewLine(char newLine)
    {
        return (CharacterWriteStreamList)CharacterWriteStream.super.setNewLine(newLine);
    }

    @Override
    public CharacterWriteStreamList setNewLine(char[] newLine)
    {
        return (CharacterWriteStreamList)CharacterWriteStream.super.setNewLine(newLine);
    }

    @Override
    public CharacterWriteStreamList setNewLine(String newLine)
    {
        PreCondition.assertNotNullAndNotEmpty(newLine, "newLine");
        PreCondition.assertNotDisposed(this, "this");

        this.newLine = newLine;
        for (final CharacterWriteStream stream : this.innerStreams)
        {
            if (stream != null && !stream.isDisposed())
            {
                stream.setNewLine(newLine);
            }
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
                if (stream != null && !stream.isDisposed())
                {
                    result += stream.write(toWrite).await();
                }
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
                if (stream != null && !stream.isDisposed())
                {
                    result += stream.write(toWrite, formattedStringArguments).await();
                }
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
                    if (stream != null && !stream.isDisposed())
                    {
                        stream.dispose().await();
                    }
                }
            }
            return result;
        });
    }

    @Override
    public CharacterWriteStreamList set(int index, CharacterWriteStream value)
    {
        PreCondition.assertNotDisposed(this, "this");

        this.innerStreams.set(index, value);
        return this;
    }

    @Override
    public CharacterWriteStreamList insert(int insertIndex, CharacterWriteStream value)
    {
        PreCondition.assertNotDisposed(this, "this");

        this.innerStreams.insert(insertIndex, value);
        return this;
    }

    @Override
    public CharacterWriteStreamList add(CharacterWriteStream value)
    {
        return (CharacterWriteStreamList)List.super.add(value);
    }

    @Override
    public CharacterWriteStreamList addAll(CharacterWriteStream... values)
    {
        return (CharacterWriteStreamList)List.super.addAll(values);
    }

    @Override
    public CharacterWriteStreamList addAll(Iterator<? extends CharacterWriteStream> values)
    {
        return (CharacterWriteStreamList)List.super.addAll(values);
    }

    @Override
    public CharacterWriteStreamList addAll(Iterable<? extends CharacterWriteStream> values)
    {
        return (CharacterWriteStreamList)List.super.addAll(values);
    }

    @Override
    public CharacterWriteStreamList clear()
    {
        this.innerStreams.clear();
        return this;
    }

    @Override
    public CharacterWriteStream removeAt(int index)
    {
        return this.innerStreams.removeAt(index);
    }

    @Override
    public CharacterWriteStream get(int index)
    {
        return this.innerStreams.get(index);
    }

    @Override
    public Iterator<CharacterWriteStream> iterate()
    {
        return this.innerStreams.iterate();
    }
}
