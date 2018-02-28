package qub;

public class InMemoryFile
{
    private final String name;
    private boolean canDelete;
    private byte[] contents;
    private DateTime lastModified;

    public InMemoryFile(String name, byte[] contents)
    {
        this.name = name;
        this.canDelete = true;
        this.contents = contents;
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

    /**
     * Set this file's contents.
     * @param contents The new contents of this file.
     */
    public void setContents(byte[] contents)
    {
        this.contents = contents == null ? new byte[0] : contents;
        lastModified = DateTime.localNow();
    }

    public DateTime getLastModified()
    {
        return lastModified;
    }
}
