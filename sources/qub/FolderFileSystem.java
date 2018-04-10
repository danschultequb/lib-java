package qub;

/**
 * An implementation of FileSystem that is scoped to a provided folder.
 */
public class FolderFileSystem extends FileSystemBase
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

    public static Result<FolderFileSystem> get(FileSystem innerFileSystem, String baseFolderPath)
    {
        return get(innerFileSystem, Path.parse(baseFolderPath));
    }

    public static Result<FolderFileSystem> get(FileSystem innerFileSystem, Path baseFolderPath)
    {
        Result<FolderFileSystem> result;
        if (innerFileSystem == null)
        {
            result = Result.error(new IllegalArgumentException("innerFileSystem cannot be null."));
        }
        else if (baseFolderPath == null)
        {
            result = Result.error(new IllegalArgumentException("baseFolderPath cannot be null."));
        }
        else
        {
            result = Result.success(new FolderFileSystem(innerFileSystem, baseFolderPath));
        }
        return result;
    }

    public Result<Boolean> create()
    {
        return createAsync().awaitReturn();
    }

    public AsyncFunction<Result<Boolean>> createAsync()
    {
        return innerFileSystem.createFolderAsync(baseFolderPath)
            .then(new Function1<Result<Folder>, Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run(Result<Folder> createFolderResult)
                {
                    return Result.done(!createFolderResult.hasError(), createFolderResult.getError());
                }
            });
    }

    public Result<Boolean> delete()
    {
        return innerFileSystem.deleteFolder(baseFolderPath);
    }

    public AsyncFunction<Result<Boolean>> deleteAsync()
    {
        return innerFileSystem.deleteFolderAsync(baseFolderPath);
    }

    public AsyncFunction<Result<Boolean>> exists()
    {
        return innerFileSystem.folderExists(baseFolderPath);
    }

    public Path getBaseFolderPath()
    {
        return baseFolderPath;
    }

    private Path getInnerPath(Path outerPath)
    {
        return outerPath == null || !outerPath.isRooted() ? null : Path.parse(baseFolderPath.toString() + outerPath.toString());
    }

    private Path getOuterPath(Path innerPath)
    {
        return Path.parse(innerPath.toString().substring(baseFolderPath.toString().length()));
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return innerFileSystem.getAsyncRunner();
    }

    @Override
    public AsyncFunction<Result<Iterable<Root>>> getRootsAsync()
    {
        return Async.<Iterable<Root>>success(getAsyncRunner(), Array.fromValues(new Root[]
        {
            new Root(this, Path.parse("/"))
        }));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(Path folderPath)
    {
        return innerFileSystem.getFilesAndFoldersAsync(getInnerPath(folderPath))
            .then(new Function1<Result<Iterable<FileSystemEntry>>, Result<Iterable<FileSystemEntry>>>()
            {
                @Override
                public Result<Iterable<FileSystemEntry>> run(Result<Iterable<FileSystemEntry>> getFilesAndFoldersResult)
                {
                    final Iterable<FileSystemEntry> entries = getFilesAndFoldersResult.getValue();
                    final Iterable<FileSystemEntry> resultEntries = entries == null ? null : entries.map(new Function1<FileSystemEntry,FileSystemEntry>()
                    {
                        @Override
                        public FileSystemEntry run(FileSystemEntry innerEntry)
                        {
                            final Path outerEntryPath = FolderFileSystem.this.getOuterPath(innerEntry.getPath());
                            return innerEntry instanceof File ? new File(FolderFileSystem.this, outerEntryPath) : new Folder(FolderFileSystem.this, outerEntryPath);
                        }
                    });

                    final Throwable error = getFilesAndFoldersResult.getError();
                    Throwable resultError;
                    if (error instanceof FolderNotFoundException)
                    {
                        final Path outerPath = getOuterPath(((FolderNotFoundException)error).getFolderPath());
                        resultError = new FolderNotFoundException(outerPath);
                    }
                    else
                    {
                        resultError = error;
                    }

                    return Result.done(resultEntries, resultError);
                }
            });
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExists(Path folderPath)
    {
        return innerFileSystem.folderExists(getInnerPath(folderPath));
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(Path folderPath)
    {
        return innerFileSystem.createFolderAsync(getInnerPath(folderPath))
            .then(new Function1<Result<Folder>, Result<Folder>>()
            {
                @Override
                public Result<Folder> run(Result<Folder> createFolderResult)
                {
                    final Folder createdFolder = createFolderResult.getValue();
                    return Result.done(
                        createdFolder == null ? null : new Folder(FolderFileSystem.this, getOuterPath(createdFolder.getPath())),
                        createFolderResult.getError());
                }
            });
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(Path folderPath)
    {
        return innerFileSystem.deleteFolderAsync(getInnerPath(folderPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExists(Path filePath)
    {
        return innerFileSystem.fileExists(getInnerPath(filePath));
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(Path filePath)
    {
        final Path innerFilePath = getInnerPath(filePath);
        return innerFileSystem.createFileAsync(innerFilePath)
            .then(new Function1<Result<File>, Result<File>>()
            {
                @Override
                public Result<File> run(Result<File> createFileResult)
                {
                    final File createdFile = createFileResult.getValue();
                    return Result.done(
                        createdFile == null ? null : new File(FolderFileSystem.this, getOuterPath(createdFile.getPath())),
                        createFileResult.getError());
                }
            });
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFile(Path filePath)
    {
        return innerFileSystem.deleteFile(getInnerPath(filePath));
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModified(Path filePath)
    {
        return innerFileSystem.getFileLastModified(getInnerPath(filePath));
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(Path rootedFilePath)
    {
        return innerFileSystem.getFileContentByteReadStream(getInnerPath(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(Path rootedFilePath)
    {
        return innerFileSystem.getFileContentByteWriteStream(getInnerPath(rootedFilePath));
    }
}
