package qub;

/**
 * A folder within an InMemoryFileSystem.
 */
public class InMemoryFolder
{
    private final String name;
    private final Clock clock;
    private final ArrayList<InMemoryFolder> folders;
    private final ArrayList<InMemoryFile> files;

    private boolean canDelete;

    /**
     * Create a new InMemoryFolder with the provided name.
     * @param name The name of the new InMemoryFolder.
     */
    public InMemoryFolder(String name, Clock clock)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNull(clock, "clock");

        this.name = name;
        this.clock = clock;
        this.folders = new ArrayList<>();
        this.files = new ArrayList<>();
        this.canDelete = true;
    }

    /**
     * Get the name of this folder.
     * @return The name of this folder.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get whether or not this folder can be deleted.
     * @return Whether or not this folder can be deleted.
     */
    public boolean canDelete()
    {
        return canDelete;
    }

    /**
     * Get whether or not this folder can be deleted based on its own canDelete property as well as
     * the canDelete properties of all of its child entries.
     * @return Whether or not this folder can be deleted based on its own canDelete property as well
     * as the canDelete properties of all of its child entries.
     */
    public boolean canDeleteRecursively()
    {
        boolean result = canDelete();

        if (result)
        {
            for (final InMemoryFile file : files)
            {
                if (!file.canDelete())
                {
                    result = false;
                    break;
                }
            }

            if (result)
            {
                for (final InMemoryFolder folder : folders)
                {
                    if (!folder.canDeleteRecursively())
                    {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Set whether or not this folder is canDelete.
     * @param canDelete Whether or not this folder is canDelete.
     */
    public void setCanDelete(boolean canDelete)
    {
        this.canDelete = canDelete;
    }

    /**
     * Get the child folder of this InMemoryFolder that has the provided name. If no child folder
     * has the provided name, then null will be returned.
     * @param folderName The name of the child folder to return.
     * @return The child folder that has the provided name, or null if no child folder has the
     * provided name.
     */
    public InMemoryFolder getFolder(String folderName)
    {
        return folders.first(folder -> folder.getName().equals(folderName));
    }

    /**
     * Create a folder within this folder with the provided name. Return whether or not this
     * function created the folder.
     * @param folderName The name of the child folder to create.
     * @param outputFolder The output argument where the folder will be set.
     * @return Whether or not the child folder was created by this function.
     */
    public boolean createFolder(String folderName, Value<InMemoryFolder> outputFolder)
    {
        InMemoryFolder inMemoryFolder = getFolder(folderName);
        final boolean result = (inMemoryFolder == null);
        if (result)
        {
            inMemoryFolder = new InMemoryFolder(folderName, clock);
            folders.add(inMemoryFolder);
        }

        if (outputFolder != null)
        {
            outputFolder.set(inMemoryFolder);
        }

        return result;
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
                if (folder.canDeleteRecursively())
                {
                    result = true;
                    folders.removeAt(folderIndex);
                }
            }
        }

        return result;
    }

    /**
     * Get the file of this InMemoryFolder that has the provided name. If no file has the provided
     * name, then null will be returned.
     * @param fileName The name of the file to return.
     * @return The file that has the provide dname, or null if no file has the provided name.
     */
    public InMemoryFile getFile(final String fileName)
    {
        return files.first((InMemoryFile file) -> file.getName().equals(fileName));
    }

    public boolean createFile(String fileName)
    {
        InMemoryFile inMemoryFile = getFile(fileName);
        final boolean result = (inMemoryFile == null);
        if (result)
        {
            inMemoryFile = new InMemoryFile(fileName, clock);
            files.add(inMemoryFile);
        }

        return result;
    }

    public boolean deleteFile(final String fileName)
    {
        final int indexToRemove = files.indexOf(new Function1<InMemoryFile,Boolean>()
        {
            @Override
            public Boolean run(InMemoryFile file)
            {
                return file.getName().equalsIgnoreCase(fileName);
            }
        });

        boolean result = false;
        if (indexToRemove != -1)
        {
            final InMemoryFile file = files.get(indexToRemove);
            if (file.canDelete())
            {
                files.removeAt(indexToRemove);
                result = true;
            }
        }

        return result;
    }

    public Iterable<InMemoryFolder> getFolders()
    {
        return folders;
    }

    public Iterable<InMemoryFile> getFiles()
    {
        return files;
    }
}
