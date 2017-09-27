package qub;

/**
 * A folder within an InMemoryFileSystem.
 */
public class InMemoryFolder
{
    private final String name;
    private final ArrayList<InMemoryFolder> folders;
    private final ArrayList<InMemoryFile> files;

    /**
     * Create a new InMemoryFolder with the provided name.
     * @param name The name of the new InMemoryFolder.
     */
    public InMemoryFolder(String name)
    {
        this.name = name;
        this.folders = new ArrayList<>();
        this.files = new ArrayList<>();
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
     * Get the child folder of this InMemoryFolder that has the provided name. If no child folder
     * has the provided name, then null will be returned.
     * @param folderName The name of the child folder to return.
     * @return The child folder that has the provided name, or null if no child folder has the
     * provided name.
     */
    public InMemoryFolder getFolder(final String folderName)
    {
        return folders.first(new Function1<InMemoryFolder,Boolean>()
        {
            @Override
            public Boolean run(InMemoryFolder folder)
            {
                return folder.getName().equals(folderName);
            }
        });
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
            inMemoryFolder = new InMemoryFolder(folderName);
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
    public boolean deleteFolder(String folderName)
    {
        boolean result = false;

        if (folderName != null && !folderName.isEmpty())
        {
            int folderIndex = 0;
            for (final InMemoryFolder folder : folders)
            {
                if (folder.getName().equals(folderName))
                {
                    break;
                }
                else
                {
                    ++folderIndex;
                }
            }

            if (folderIndex < folders.getCount())
            {
                result = true;
                folders.removeAt(folderIndex);
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
        return files.first(new Function1<InMemoryFile,Boolean>()
        {
            @Override
            public Boolean run(InMemoryFile file)
            {
                return file.getName().equals(fileName);
            }
        });
    }

    public boolean createFile(String fileName)
    {
        InMemoryFile inMemoryFile = getFile(fileName);
        final boolean result = (inMemoryFile == null);
        if (result)
        {
            inMemoryFile = new InMemoryFile(fileName);
            files.add(inMemoryFile);
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
