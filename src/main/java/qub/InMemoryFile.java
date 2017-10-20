package qub;

public class InMemoryFile
{
    private final String name;
    private boolean canDelete;
    private byte[] contents;

    public InMemoryFile(String name, byte[] contents)
    {
        this.name = name;
        this.canDelete = true;
        this.contents = contents;
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
     * Get the contents of this file.
     * @return The contents of this file.
     */
    public byte[] getContents()
    {
        return Array.clone(contents);
    }
}
