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

    /**
     * Get the content blocks of this file.
     * @return The content blocks of this file.
     */
    public Iterable<byte[]> getContentBlocks()
    {
        final List<byte[]> result = new ArrayList<>();

        if (contents.length > 0)
        {
            final int blockSize = 10;
            final double blockCount = Math.ceiling((double)contents.length / blockSize);
            for (int i = 0; i < blockCount; ++i)
            {
                final int blockLength = Math.minimum(blockSize, contents.length - (i * blockSize));
                final byte[] block = new byte[blockLength];
                Array.copy(contents, i * blockSize, block, 0, blockLength);
                result.add(block);
            }
        }

        return result;
    }

    /**
     * Set this file's contents.
     * @param contents The new contents of this file.
     */
    public void setContents(byte[] contents)
    {
        this.contents = contents == null ? new byte[0] : contents;
    }
}
