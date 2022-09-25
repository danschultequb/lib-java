package qub;

/**
 * An {@link InMemoryFolder} within an {@link InMemoryFileSystem}.
 */
public class InMemoryFolder
{
    private final InMemoryFolder parentFolder;
    private final String name;
    private final List<InMemoryFolder> folders;
    private final List<InMemoryFile> files;

    private boolean canDelete;

    protected InMemoryFolder(InMemoryFolder parentFolder, String name)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.parentFolder = parentFolder;
        this.name = name;
        this.folders = List.create();
        this.files = List.create();
        this.canDelete = true;
    }

    /**
     * Create a new {@link InMemoryFolder} with the provided name.
     * @param parentFolder The parent {@link InMemoryFolder} of the new {@link InMemoryFolder}, or
     *                     null if the new {@link InMemoryFolder} is an {@link InMemoryRoot}.
     * @param name The name of the new {@link InMemoryFolder}.
     * {@link InMemoryFile}s are created and modified.
     */
    public static InMemoryFolder create(InMemoryFolder parentFolder, String name)
    {
        return new InMemoryFolder(parentFolder, name);
    }

    /**
     * Get the parent {@link InMemoryFolder} of this {@link InMemoryFolder}.
     */
    public InMemoryFolder getParentFolder()
    {
        return this.parentFolder;
    }

    /**
     * Get the {@link InMemoryRoot} that contains this {@link InMemoryFolder}.
     */
    public InMemoryRoot getRoot()
    {
        InMemoryRoot result = null;

        InMemoryFolder current = this;
        while (current != null)
        {
            if (current instanceof InMemoryRoot)
            {
                result = (InMemoryRoot)current;
                break;
            }
            else
            {
                current = current.getParentFolder();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Path getPath()
    {
        final List<String> pathParts = List.create();

        InMemoryFolder current = this;
        while (current != null)
        {
            final String name = current.getName();
            if (!name.endsWith("/") && !name.endsWith("\\"))
            {
                pathParts.insert(0, "/");
            }
            pathParts.insert(0, name);

            current = current.getParentFolder();
        }

        final String pathString = Strings.join(pathParts);
        final Path result = Path.parse(pathString);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the name of this {@link InMemoryFolder}.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get whether this {@link InMemoryFolder} can be deleted.
     */
    public boolean canDelete()
    {
        return this.canDelete &&
            !this.files.contains((InMemoryFile file) -> !file.canDelete()) &&
            !this.folders.contains((InMemoryFolder folder) -> !folder.canDelete());
    }

    /**
     * Set whether this {@link InMemoryFolder} can be deleted.
     */
    public void setCanDelete(boolean canDelete)
    {
        this.canDelete = canDelete;
    }

    /**
     * Get the child folder of this InMemoryFolder that has the provided name. If no child folder
     * has the provided name, then null will be returned.
     * @param folderName The name of the child folder to return.
     */
    public Result<InMemoryFolder> getFolder(String folderName)
    {
        return this.folders.first(folder -> folder.getName().equals(folderName))
            .convertError(NotFoundException.class, () -> new FolderNotFoundException(this.getPath().concatenateSegments(folderName)));
    }

    /**
     * Create and return an {@link InMemoryFolder} within this {@link InMemoryFolder} with the
     * provided name.
     * @param folderName The name of the child folder to create.
     */
    public Result<InMemoryFolder> createFolder(String folderName)
    {
        return Result.create(() ->
        {
            final InMemoryFolder existingFolder = this.getFolder(folderName).catchError(NotFoundException.class).await();
            if (existingFolder != null)
            {
                throw new FolderAlreadyExistsException(this.getPath().concatenateSegments(folderName));
            }

            final InMemoryFolder result = InMemoryFolder.create(this, folderName);
            this.folders.add(result);

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Delete a folder within this folder with the provided name. Return whether or not this
     * function deleted the folder.
     * @param folderName The name of the child folder to delete.
     * @return Whether or not the child folder was deleted by this function.
     */
    public boolean deleteFolder(final String folderName)
    {
        boolean result = false;

        if (!Strings.isNullOrEmpty(folderName))
        {
            final int folderIndex = folders.indexOf((InMemoryFolder folder) -> folder.getName().equals(folderName));
            if (folderIndex != -1)
            {
                final InMemoryFolder folder = folders.get(folderIndex);
                if (folder.canDelete())
                {
                    result = true;
                    folders.removeAt(folderIndex);
                }
            }
        }

        return result;
    }

    /**
     * Get the {@link InMemoryFile} of this {@link InMemoryFolder} that has the provided name.
     * @param fileName The name of the file to return.
     */
    public Result<InMemoryFile> getFile(String fileName)
    {
        return this.files.first((InMemoryFile file) -> file.getName().equals(fileName));
    }

    /**
     * Create a new {@link InMemoryFile} in this {@link InMemoryFolder} with the provided name.
     * @param fileName The name of the {@link InMemoryFile} to create.
     */
    public Result<InMemoryFile> createFile(String fileName)
    {
        return Result.create(() ->
        {
            final InMemoryFile existingFile = this.getFile(fileName).catchError(NotFoundException.class).await();
            if (existingFile != null)
            {
                throw new FileAlreadyExistsException(this.getPath().concatenateSegments(fileName));
            }

            final InMemoryFile result = InMemoryFile.create(this, fileName);
            this.files.add(result);

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Delete the {@link InMemoryFile} in this {@link InMemoryFolder} with the provided name.
     * @param fileName The name of the {@link InMemoryFile} to delete.
     */
    public Result<Void> deleteFile(String fileName)
    {
        return Result.create(() ->
        {
            boolean deletedFile = false;

            final int indexToRemove = this.files.indexOf((InMemoryFile file) -> file.getName().equalsIgnoreCase(fileName));
            if (indexToRemove != -1)
            {
                final InMemoryFile file = this.files.get(indexToRemove);
                if (file.canDelete())
                {
                    this.files.removeAt(indexToRemove);
                    deletedFile = true;
                }
            }

            if (!deletedFile)
            {
                throw new FileNotFoundException(this.getPath().concatenateSegments(fileName));
            }
        });
    }

    /**
     * Get the {@link InMemoryFolder}s in this {@link InMemoryFolder}.
     */
    public Iterable<InMemoryFolder> getFolders()
    {
        return this.folders;
    }

    /**
     * Get the {@link InMemoryFile}s in this {@link InMemoryFolder}.
     */
    public Iterable<InMemoryFile> getFiles()
    {
        return this.files;
    }

    /**
     * Get the total {@link DataSize} that is contained by the {@link InMemoryFile}s in this
     * {@link InMemoryFolder} and in any subfolders.
     */
    public DataSize getUsedDataSize()
    {
        DataSize result = DataSize.zero;

        for (final InMemoryFile file : this.files)
        {
            result = result.plus(file.getContentsDataSize());
        }

        for (final InMemoryFolder folder : this.folders)
        {
            result = result.plus(folder.getUsedDataSize());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, DataSize.zero, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
