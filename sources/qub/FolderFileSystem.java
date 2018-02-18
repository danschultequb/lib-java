package qub;

/**
 * An implementation of FileSystem that is scoped to a provided folder.
 */
public class FolderFileSystem implements FileSystem
{
    private final FileSystem innerFileSystem;
    private final Path baseFolderPath;

    private FolderFileSystem(FileSystem innerFileSystem, Path baseFolderPath)
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

    public static FolderFileSystem create(FileSystem innerFileSystem, String baseFolderPath)
    {
        return create(innerFileSystem, Path.parse(baseFolderPath));
    }

    public static FolderFileSystem create(FileSystem innerFileSystem, Path baseFolderPath)
    {
        FolderFileSystem result = null;

        if (baseFolderPath != null)
        {
            result = new FolderFileSystem(innerFileSystem, baseFolderPath);
        }

        return result;
    }

    public boolean create()
    {
        return innerFileSystem.createFolder(baseFolderPath);
    }

    public boolean delete()
    {
        return innerFileSystem.deleteFolder(baseFolderPath);
    }

    public boolean exists()
    {
        return innerFileSystem.folderExists(baseFolderPath);
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
    public void setAsyncRunner(AsyncRunner asyncRunner)
    {
        innerFileSystem.setAsyncRunner(asyncRunner);
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return innerFileSystem.getAsyncRunner();
    }

    @Override
    public Iterable<Root> getRoots(Action1<String> onError)
    {
        return Array.fromValues(new Root[]
        {
            new Root(this, Path.parse("/"))
        });
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath, Action1<String> onError)
    {
        Iterable<FileSystemEntry> result = new Array<>(0);

        final Path innerFolderPath = getInnerPath(folderPath);
        final Iterable<FileSystemEntry> innerResult = innerFileSystem.getFilesAndFolders(innerFolderPath, onError);
        if (innerResult != null)
        {
            result = Array.fromValues(innerResult.map((FileSystemEntry innerEntry) ->
            {
                final Path outerEntryPath = getOuterPath(innerEntry.getPath());
                return innerEntry instanceof File ? new File(FolderFileSystem.this, outerEntryPath) : new Folder(FolderFileSystem.this, outerEntryPath);
            }));
        }

        return result;
    }

    @Override
    public boolean folderExists(Path folderPath, Action1<String> onError)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        return innerFileSystem.folderExists(innerFolderPath, onError);
    }

    @Override
    public boolean createFolder(Path folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        final Value<Folder> innerOutputFolder = (outputFolder == null ? null : new Value<>());

        final boolean result = innerFileSystem.createFolder(innerFolderPath, innerOutputFolder, onError);

        if (innerOutputFolder != null && innerOutputFolder.hasValue())
        {
            final Path innerOutputFolderPath = innerOutputFolder.get().getPath();
            final Path outerOutputFolderPath = getOuterPath(innerOutputFolderPath);
            outputFolder.set(getFolder(outerOutputFolderPath));
        }

        return result;
    }

    @Override
    public boolean deleteFolder(Path folderPath, Action1<String> onError)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        return innerFileSystem.deleteFolder(innerFolderPath, onError);
    }

    @Override
    public boolean fileExists(Path filePath, Action1<String> onError)
    {
        final Path innerFilePath = getInnerPath(filePath);
        return innerFileSystem.fileExists(innerFilePath, onError);
    }

    @Override
    public boolean createFile(Path filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError)
    {
        final Path innerFilePath = getInnerPath(filePath);
        final Value<File> innerOutputFile = (outputFile == null ? null : new Value<>());

        final boolean result = innerFileSystem.createFile(innerFilePath, fileContents, innerOutputFile, onError);

        if (innerOutputFile != null)
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
    public boolean deleteFile(Path filePath, Action1<String> onError)
    {
        final Path innerFilePath = getInnerPath(filePath);
        return innerFileSystem.deleteFile(innerFilePath, onError);
    }

    @Override
    public ByteReadStream getFileContentByteReadStream(Path rootedFilePath, Action1<String> onError)
    {
        final Path innerFilePath = getInnerPath(rootedFilePath);
        return innerFileSystem.getFileContentByteReadStream(innerFilePath, onError);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, byte[] fileContents, Action1<String> onError)
    {
        final Path innerFilePath = getInnerPath(rootedFilePath);
        return innerFileSystem.setFileContents(innerFilePath, fileContents, onError);
    }
}
