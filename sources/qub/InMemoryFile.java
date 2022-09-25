package qub;

/**
 * An in-memory representation of a {@link FileSystem} file.
 */
public class InMemoryFile
{
    private final InMemoryFolder parentFolder;
    private final String name;
    private boolean canDelete;
    private byte[] contents;
    private DateTime lastModified;

    private InMemoryFile(InMemoryFolder parentFolder, String name)
    {
        PreCondition.assertNotNull(parentFolder, "parentFolder");
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.parentFolder = parentFolder;
        this.name = name;
        this.canDelete = true;
        this.contents = new byte[0];
        this.lastModified = this.getCurrentDateTime();
    }

    /**
     * Create a new {@link InMemoryFile}.
     * @param parentFolder The {@link InMemoryFolder} that contains the new {@link InMemoryFile}.
     * @param name The name of the new {@link InMemoryFile}.
     */
    public static InMemoryFile create(InMemoryFolder parentFolder, String name)
    {
        return new InMemoryFile(parentFolder, name);
    }

    private InMemoryRoot getRoot()
    {
        final InMemoryRoot result = this.parentFolder.getRoot();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private DateTime getCurrentDateTime()
    {
        return this.getRoot().getCurrentDateTime();
    }

    /**
     * Get the name of this {@link InMemoryFile}.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get this {@link InMemoryFile} can be deleted.
     */
    public boolean canDelete()
    {
        return this.canDelete;
    }

    /**
     * Set whether this {@link InMemoryFile} can be deleted.
     * @param canDelete Whether this {@link InMemoryFile} can be deleted.
     */
    public InMemoryFile setCanDelete(boolean canDelete)
    {
        this.canDelete = canDelete;
        return this;
    }

    /**
     * Get a {@link CharacterToByteReadStream} that reads create the contents of this
     * {@link InMemoryFile}.
     */
    public CharacterToByteReadStream getContentsReadStream()
    {
        return CharacterToByteReadStream.create(ByteReadStream.create(this.contents));
    }

    /**
     * Get a {@link BufferedByteWriteStream} that can be used to write to the contents of this
     * {@link InMemoryFile}.
     * @param openWriteType How the new content that is written will interact with the existing
     *                      (or non-existing) contents of this {@link InMemoryFile}.
     */
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
                    this.contents = writtenBytes == null ? new byte[0] : writtenBytes;
                    break;

                case CreateOrAppend:
                    this.contents = Array.mergeBytes(Iterable.create(this.contents, writtenBytes));
                    break;
            }
            this.lastModified = this.getCurrentDateTime();
        });

        PostCondition.assertNotNull(result, "result");

        return ByteWriteStream.buffer(result);
    }

    /**
     * Get the most recent {@link DateTime} that this {@link InMemoryFile} was modified.
     */
    public DateTime getLastModified()
    {
        return this.lastModified;
    }

    /**
     * Get the {@link DataSize} of the content in this {@link InMemoryFile}.
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
