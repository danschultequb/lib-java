package qub;

public class InMemoryFile
{
    private final String name;
    private boolean canDelete;
    private byte[] contents;
    private DateTime lastModified;

    public InMemoryFile(String name)
    {
        this.name = name;
        this.canDelete = true;
        this.contents = new byte[0];
        this.lastModified = DateTime.localNow();
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
        return new InMemoryByteReadStream(contents);
    }

    public ByteWriteStream getContentByteWriteStream()
    {
        return new InMemoryByteWriteStream()
        {
            @Override
            public Result<Boolean> dispose()
            {
                final byte[] writtenBytes = getBytes();

                final Result<Boolean> result = super.dispose();
                if (!result.hasError() && result.getValue())
                {
                    InMemoryFile.this.contents = writtenBytes == null ? new byte[0] : writtenBytes;
                    InMemoryFile.this.lastModified = DateTime.localNow();
                }
                return result;
            }
        };
    }

    public DateTime getLastModified()
    {
        return lastModified;
    }
}
