package qub;

/**
 * A FileSystem implementation that is completely stored in the memory of the running application.
 */
public class InMemoryFileSystem extends FileSystemBase
{
    private final ArrayList<InMemoryRoot> roots;

    /**
     * Create a new InMemoryFileSystem.
     */
    public InMemoryFileSystem()
    {
        roots = new ArrayList<>();
    }

    private InMemoryRoot getInMemoryRoot(String inMemoryRootPath)
    {
        return getInMemoryRoot(Path.parse(inMemoryRootPath));
    }

    private InMemoryRoot getInMemoryRoot(final Path inMemoryRootPath)
    {
        return roots.first(new Function1<InMemoryRoot,Boolean>()
        {
            @Override
            public Boolean run(InMemoryRoot inMemoryRoot)
            {
                return inMemoryRoot.getPath().equals(inMemoryRootPath);
            }
        });
    }

    private InMemoryFolder getInMemoryFolder(Path inMemoryFolderPath)
    {
        final Iterator<String> folderPathSegments = inMemoryFolderPath.getSegments().iterate();
        InMemoryFolder folder = getInMemoryRoot(folderPathSegments.first());
        while (folder != null && folderPathSegments.next())
        {
            folder = folder.getFolder(folderPathSegments.getCurrent());
        }

        return folder;
    }

    private boolean createInMemoryFolder(Path inMemoryFolderPath, Value<InMemoryFolder> outputFolder)
    {
        boolean result = false;

        final Iterator<String> folderPathSegments = inMemoryFolderPath.getSegments().iterate();
        final Value<InMemoryFolder> folder = new Value<InMemoryFolder>(getInMemoryRoot(folderPathSegments.first()));
        while (folderPathSegments.next())
        {
            final String folderName = folderPathSegments.getCurrent();
            result = folder.get().createFolder(folderName, folder);
        }

        if (outputFolder != null)
        {
            outputFolder.set(folder.get());
        }

        return result;
    }

    private InMemoryFile getInMemoryFile(Path filePath)
    {
        InMemoryFile result = null;

        if (filePath != null && filePath.isRooted())
        {
            final Path parentFolderPath = filePath.getParentPath();
            final InMemoryFolder parentFolder = getInMemoryFolder(parentFolderPath);
            if (parentFolder != null)
            {
                result = parentFolder.getFile(filePath.getSegments().last());
            }
        }

        return result;
    }

    public boolean setFileCanDelete(String filePathString, boolean canDelete)
    {
        return setFileCanDelete(Path.parse(filePathString), canDelete);
    }

    public boolean setFileCanDelete(Path filePath, boolean canDelete)
    {
        boolean result = false;

        final InMemoryFile file = getInMemoryFile(filePath);
        if (file != null)
        {
            file.setCanDelete(canDelete);
            result = true;
        }

        return result;
    }

    public boolean setFolderCanDelete(String folderPathString, boolean canDelete)
    {
        return setFolderCanDelete(Path.parse(folderPathString), canDelete);
    }

    public boolean setFolderCanDelete(Path folderPath, boolean canDelete)
    {
        boolean result = false;

        final InMemoryFolder folder = getInMemoryFolder(folderPath);
        if (folder != null)
        {
            folder.setCanDelete(canDelete);
            result = true;
        }

        return result;
    }

    @Override
    public Iterable<Root> getRoots()
    {
        return Array.fromValues(roots.map(new Function1<InMemoryRoot, Root>()
        {
            @Override
            public Root run(InMemoryRoot inMemoryRoot)
            {
                return new Root(InMemoryFileSystem.this, inMemoryRoot.getPath());
            }
        }));
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        ArrayList<FileSystemEntry> result = null;

        if (folderPath != null && folderPath.isRooted())
        {
            final InMemoryFolder folder = getInMemoryFolder(folderPath);
            if (folder != null)
            {
                result = new ArrayList<>();

                for (final InMemoryFolder inMemoryFolder : folder.getFolders())
                {
                    final Path childFolderPath = folderPath.concatenateSegment(inMemoryFolder.getName());
                    result.add(new Folder(this, childFolderPath));
                }

                for (final InMemoryFile inMemoryFile : folder.getFiles())
                {
                    final Path childFilePath = folderPath.concatenateSegment(inMemoryFile.getName());
                    result.add(new File(this, childFilePath));
                }
            }
        }

        return result;
    }

    @Override
    public boolean folderExists(Path folderPath)
    {
        boolean result = false;

        if (folderPath != null && folderPath.isRooted())
        {
            result = (getInMemoryFolder(folderPath) != null);
        }

        return result;
    }

    @Override
    public boolean createFolder(Path folderPath, Value<Folder> outputFolder)
    {
        boolean result = false;

        if (folderPath == null || !folderPath.isRooted() || !rootExists(folderPath.getRoot()))
        {
            if (outputFolder != null)
            {
                outputFolder.clear();
            }
        }
        else
        {
            result = createInMemoryFolder(folderPath, null);

            if (outputFolder != null)
            {
                outputFolder.set(getFolder(folderPath));
            }
        }

        return result;
    }

    @Override
    public boolean deleteFolder(Path folderPath)
    {
        boolean result = false;

        if (folderPath != null && folderPath.isRooted() && rootExists(folderPath.getRoot()))
        {
            final InMemoryFolder parentFolder = getInMemoryFolder(folderPath.getParentPath());
            if (parentFolder != null)
            {
                result = parentFolder.deleteFolder(folderPath.getSegments().last());
            }
        }

        return result;
    }

    @Override
    public boolean fileExists(Path filePath)
    {
        boolean result = false;

        if (filePath != null && !filePath.normalize().endsWith("/"))
        {
            final InMemoryFolder parentFolder = getInMemoryFolder(filePath.getParentPath());
            if (parentFolder != null)
            {
                result = parentFolder.getFile(filePath.getSegments().last()) != null;
            }
        }

        return result;
    }

    @Override
    public boolean createFile(Path filePath, Value<File> outputFile)
    {
        boolean result = false;

        if (filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") || !rootExists(filePath.getRoot()) || containsInvalidCharacters(filePath))
        {
            if (outputFile != null)
            {
                outputFile.clear();
            }
        }
        else
        {
            final Value<InMemoryFolder> parentFolder = new Value<>();
            createInMemoryFolder(filePath.getParentPath(), parentFolder);

            final String fileName = filePath.getSegments().last();
            result = parentFolder.get().createFile(fileName);

            if (outputFile != null)
            {
                outputFile.set(getFile(filePath));
            }
        }

        return result;
    }

    @Override
    public boolean deleteFile(Path filePath)
    {
        boolean result = false;

        if (filePath != null && filePath.isRooted() && rootExists(filePath.getRoot()))
        {
            final InMemoryFolder parentFolder = getInMemoryFolder(filePath.getParentPath());
            if (parentFolder != null)
            {
                result = parentFolder.deleteFile(filePath.getSegments().last());
            }
        }

        return result;
    }

    /**
     * Create a new Root in this FileSystem and returns whether or not this function created the
     * Root.
     * @param rootPath The String representation of the path to the Root to create in this
     *                 FileSystem.
     * @return Whether or not this function created the Root.
     */
    public boolean createRoot(String rootPath)
    {
        return createRoot(Path.parse(rootPath));
    }

    /**
     * Create a new Root in this FileSystem and returns whether or not this function created the
     * Root.
     * @param rootPath The String representation of the path to the Root to create in this
     *                 FileSystem.
     * @return Whether or not this function created the Root.
     */
    public boolean createRoot(Path rootPath)
    {
        boolean result = false;

        if (rootPath != null && rootPath.isRooted())
        {
            final String rootName = rootPath.getSegments().first();
            result = !rootExists(rootName);
            if (result)
            {
                roots.add(new InMemoryRoot(rootName));
            }
        }

        return result;
    }

    protected static boolean containsInvalidCharacters(Path path)
    {
        return path != null && containsInvalidCharacters(path.toString());
    }

    protected static boolean containsInvalidCharacters(String pathString)
    {
        boolean result = false;

        if (pathString != null && !pathString.isEmpty())
        {
            final int pathStringLength = pathString.length();
            for (int i = 0; i < pathStringLength; ++i)
            {
                final char currentCharacter = pathString.charAt(i);
                if (invalidCharacters.contains(currentCharacter))
                {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    private static final Array<Character> invalidCharacters = Array.fromValues('@', '#', '?');
}