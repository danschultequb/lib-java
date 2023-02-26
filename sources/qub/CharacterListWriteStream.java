package qub;

public class CharacterListWriteStream implements CharacterWriteStream
{
    private final CharacterList list;
    private boolean disposed;
    private String newLine;

    private CharacterListWriteStream(CharacterList list)
    {
        PreCondition.assertNotNull(list, "list");

        this.list = list;
        this.newLine = "\n";
    }

    public static CharacterListWriteStream create()
    {
        return CharacterListWriteStream.create(CharacterList.create());
    }

    public static CharacterListWriteStream create(CharacterList list)
    {
        return new CharacterListWriteStream(list);
    }

    @Override
    public String getNewLine()
    {
        return this.newLine;
    }

    @Override
    public CharacterListWriteStream setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            this.list.add(toWrite);

            return 1;
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
            final String formattedString = Strings.format(toWrite, formattedStringArguments);
            this.list.addAll(formattedString);
            return formattedString.length();
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = !this.disposed;
            if (result)
            {
                this.disposed = true;
            }
            return result;
        });
    }

    @Override
    public String toString()
    {
        return this.list.toString();
    }

    public String toString(boolean asString)
    {
        return this.list.toString(asString);
    }
}
