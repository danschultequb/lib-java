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
     * Get a ByteReadStream that reads from the contents of this InMemoryFile.
     * @return A ByteReadStream that reads from the contents of this InMemoryFile.
     */
    public ByteReadStream getContentByteReadStream()
    {
        return new InMemoryByteStream(contents).endOfStream();
    }

    public ByteWriteStream getContentByteWriteStream()
    {
        return new InMemoryByteStream()
        {
            @Override
            public Result<Boolean> dispose()
            {
                final byte[] writtenBytes = getBytes();

                return super.dispose()
                    .then((Boolean disposed) ->
                    {
                        if (Booleans.isTrue(disposed))
                        {
                            InMemoryFile.this.contents = writtenBytes == null ? new byte[0] : writtenBytes;
                            InMemoryFile.this.lastModified = clock.getCurrentDateTime();
                        }
                        return disposed;
                    });
            }
        };
    }

    public DateTime getLastModified()
    {
        return lastModified;
    }
}
