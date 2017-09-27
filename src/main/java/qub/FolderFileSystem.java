package qub;

/**
 * An implementation of FileSystem that is scoped to a provided folder.
 */
public class FolderFileSystem extends FileSystemBase
{
    private final FileSystem innerFileSystem;
    private final Path baseFolderPath;

    public FolderFileSystem(FileSystem innerFileSystem, String baseFolderPathString)
    {
        this(innerFileSystem, Path.parse(baseFolderPathString));
    }

    public FolderFileSystem(FileSystem innerFileSystem, Path baseFolderPath)
    {
        this.innerFileSystem = innerFileSystem;

        Path normalizedBaseFolderPath = baseFolderPath.normalize();
        if (normalizedBaseFolderPath.endsWith("/"))
        {
            final String normalizedBaseFolderPathString = normalizedBaseFolderPath.toString();
            normalizedBaseFolderPath = Path.parse(normalizedBaseFolderPathString.substring(0, normalizedBaseFolderPathString.length() - 1));
        }
        this.baseFolderPath = normalizedBaseFolderPath;
    }

    public boolean create()
    {
        return innerFileSystem.createFolder(baseFolderPath);
    }

    public boolean delete()
    {
        return innerFileSystem.deleteFolder(baseFolderPath);
    }

    public Path getBaseFolderPath()
    {
        return baseFolderPath;
    }

    private Path getInnerPath(Path outerPath)
    {
        return outerPath == null ? null : Path.parse(baseFolderPath.toString() + outerPath.toString());
    }

    private Path getOuterPath(Path innerPath)
    {
        return Path.parse(innerPath.toString().substring(baseFolderPath.toString().length()));
    }

    @Override
    public Iterable<Root> getRoots()
    {
        return Array.fromValues(new Root(this, "/"));
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        Iterable<FileSystemEntry> result = null;

        if (folderPath != null && folderPath.isRooted() && rootExists(folderPath.getRoot()))
        {
            final Path innerFolderPath = getInnerPath(folderPath);
            final Iterable<FileSystemEntry> innerResult = innerFileSystem.getFilesAndFolders(innerFolderPath);
            if (innerResult != null)
            {
                result = Array.fromValues(innerResult.map(new Function1<FileSystemEntry, FileSystemEntry>()
                {
                    @Override
                    public FileSystemEntry run(FileSystemEntry innerEntry)
                    {
                        final Path outerEntryPath = getOuterPath(innerEntry.getPath());
                        return innerEntry instanceof File ? new File(FolderFileSystem.this, outerEntryPath) : new Folder(FolderFileSystem.this, outerEntryPath);
                    }
                }));
            }
        }

        return result;
    }

    @Override
    public boolean folderExists(Path folderPath)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        return innerFileSystem.folderExists(innerFolderPath);
    }

    @Override
    public boolean createFolder(Path folderPath, Value<Folder> outputFolder)
    {
        boolean result = false;

        if (folderPath == null || !folderPath.isRooted())
        {
            if (outputFolder != null)
            {
                outputFolder.clear();
            }
        }
        else
        {
            final Path innerFolderPath = getInnerPath(folderPath);
            final Value<Folder> innerOutputFolder = (outputFolder == null ? null : new Value<Folder>());

            result = innerFileSystem.createFolder(innerFolderPath, innerOutputFolder);

            if (innerOutputFolder != null && innerOutputFolder.hasValue())
            {
                final Path innerOutputFolderPath = innerOutputFolder.get().getPath();
                final Path outerOutputFolderPath = getOuterPath(innerOutputFolderPath);
                outputFolder.set(getFolder(outerOutputFolderPath));
            }
        }

        return result;
    }

    @Override
    public boolean deleteFolder(Path folderPath)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        return innerFileSystem.deleteFolder(innerFolderPath);
    }

    @Override
    public boolean fileExists(Path filePath)
    {
        final Path innerFilePath = getInnerPath(filePath);
        return innerFileSystem.fileExists(innerFilePath);
    }

    @Override
    public boolean createFile(Path filePath, Value<File> outputFile)
    {
        boolean result = false;

        if (filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") || filePath.endsWith(":") || !rootExists(filePath.getRoot()))
        {
            if (outputFile != null)
            {
                outputFile.clear();
            }
        }
        else
        {
            final Path innerFilePath = getInnerPath(filePath);
            final Value<File> innerOutputFile = (outputFile == null ? null : new Value<File>());

            result = innerFileSystem.createFile(innerFilePath, innerOutputFile);

            if(innerOutputFile != null && innerOutputFile.hasValue())
            {
                final Path innerOutputFilePath = innerOutputFile.get().getPath();
                final Path outerOutputFilePath = getOuterPath(innerOutputFilePath);
                outputFile.set(getFile(outerOutputFilePath));
            }
        }

        return result;
    }
}
