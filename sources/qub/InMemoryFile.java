package qub;

public class InMemoryFile
{
    private final Clock clock;
    private final String name;
    private boolean canDelete;
    private byte[] contents;
    private DateTime lastModified;

    public InMemoryFile(String name, Clock clock)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNull(clock, "clock");

        this.name = name;
        this.clock = clock;
        this.canDelete = true;
        this.contents = new byte[0];
        this.lastModified = clock.getCurrentDateTime();
    }

    public String getName()
    {
        return name;
    }

    /**
     * Get whether or not this file can be deleted.
     * @return Whether or not this file can be deleted.
     */
    public boolean canDelete()
    {
        return canDelete;
    }

    /**
     * Set whether or not this file can be deleted.
     * @param canDelete Whether or not this file can be deleted.
     */
    public void setCanDelete(boolean canDelete)
    {
        this.canDelete = canDelete;
    }

    /**
     * Get a ByteReadStream that reads create the contents of this InMemoryFile.
     * @return A ByteReadStream that reads create the contents of this InMemoryFile.
     */
    public CharacterToByteReadStream getContentsReadStream()
    {
        return CharacterToByteReadStream.create(ByteReadStream.create(this.contents));
    }

    public BufferedByteWriteStream getContentsByteWriteStream()
    {
        return this.getContentByteWriteStream(OpenWriteType.CreateOrOverwrite);
    }

    public BufferedByteWriteStream getContentByteWriteStream(OpenWriteType openWriteType)
    {
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        final InMemoryByteStream result = InMemoryByteStream.create();
        result.onDisposed(() ->
        {
            final byte[] writtenBytes = result.getBytes();
            switch (openWriteType)
            {
                case CreateOrOverwrite:
                    this.contents =  writtenBytes == null ? new byte[0] : writtenBytes;
                    break;

                case CreateOrAppend:
                    this.contents = Array.mergeBytes(Iterable.create(this.contents, writtenBytes));
                    break;
            }
            this.lastModified = clock.getCurrentDateTime();
        });

        PostCondition.assertNotNull(result, "result");

        return ByteWriteStream.buffer(result);
    }

    public DateTime getLastModified()
    {
        return lastModified;
    }

    /**
     * Get the amount of data contained by this file.
     * @return The amount of data contained by this file.
     */
    public DataSize getContentsDataSize()
    {
        return DataSize.bytes(this.contents.length);
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
