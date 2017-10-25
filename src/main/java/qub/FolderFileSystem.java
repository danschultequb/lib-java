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
        return outerPath == null || outerPath.isEmpty() || !outerPath.isRooted() ? null : Path.parse(baseFolderPath.toString() + outerPath.toString());
    }

    private Path getOuterPath(Path innerPath)
    {
        return Path.parse(innerPath.toString().substring(baseFolderPath.toString().length()));
    }

    @Override
    public Iterable<Root> getRoots()
    {
        return Array.fromValues(new Root(this, Path.parse("/")));
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        Iterable<FileSystemEntry> result = null;

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

        return result;
    }

    @Override
    public boolean folderExists(Path folderPath)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        return innerFileSystem.folderExists(innerFolderPath);
    }

    @Override
    public boolean createFolder(Path folderPath, Out<Folder> outputFolder)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        final Value<Folder> innerOutputFolder = (outputFolder == null ? null : new Value<Folder>());

        final boolean result = innerFileSystem.createFolder(innerFolderPath, innerOutputFolder);

        if (innerOutputFolder != null && innerOutputFolder.hasValue())
        {
            final Path innerOutputFolderPath = innerOutputFolder.get().getPath();
            final Path outerOutputFolderPath = getOuterPath(innerOutputFolderPath);
            outputFolder.set(getFolder(outerOutputFolderPath));
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
    public boolean createFile(Path filePath, byte[] fileContents, Out<File> outputFile)
    {
        final Path innerFilePath = getInnerPath(filePath);
        final Value<File> innerOutputFile = (outputFile == null ? null : new Value<File>());

        final boolean result = innerFileSystem.createFile(innerFilePath, fileContents, innerOutputFile);

        if (outputFile != null && innerOutputFile != null)
        {
            if (!innerOutputFile.hasValue())
            {
                outputFile.clear();
            }
            else
            {
                final Path innerOutputFilePath = innerOutputFile.get().getPath();
                final Path outerOutputFilePath = getOuterPath(innerOutputFilePath);
                outputFile.set(getFile(outerOutputFilePath));
            }
        }

        return result;
    }

    @Override
    public boolean deleteFile(Path filePath)
    {
        final Path innerFilePath = getInnerPath(filePath);
        return innerFileSystem.deleteFile(innerFilePath);
    }

    @Override
    public byte[] getFileContents(Path rootedFilePath)
    {
        final Path innerFilePath = getInnerPath(rootedFilePath);
        return innerFileSystem.getFileContents(innerFilePath);
    }

    @Override
    public String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding)
    {
        final Path innerFilePath = getInnerPath(rootedFilePath);
        return innerFileSystem.getFileContentsAsString(innerFilePath, encoding);
    }

    @Override
    public Iterable<byte[]> getFileContentBlocks(Path rootedFilePath)
    {
        final Path innerFilePath = getInnerPath(rootedFilePath);
        return innerFileSystem.getFileContentBlocks(innerFilePath);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, byte[] fileContents)
    {
        final Path innerFilePath = getInnerPath(rootedFilePath);
        return innerFileSystem.setFileContents(innerFilePath, fileContents);
    }
}
