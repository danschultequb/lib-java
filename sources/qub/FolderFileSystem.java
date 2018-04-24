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

    public Result<Boolean> exists()
    {
        return innerFileSystem.folderExists(baseFolderPath);
    }

    public AsyncFunction<Result<Boolean>> existsAsync()
    {
        return innerFileSystem.folderExistsAsync(baseFolderPath);
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
    public Result<Iterable<Root>> getRoots()
    {
        return Result.<Iterable<Root>>success(Array.fromValues(new Root[]
        {
            new Root(this, Path.parse("/"))
        }));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path folderPath)
    {
        final Path innerFolderPath = getInnerPath(folderPath);
        final Result<Iterable<FileSystemEntry>> innerResult = innerFileSystem.getFilesAndFolders(innerFolderPath);

        final Iterable<FileSystemEntry> innerEntries = innerResult.getValue();
        final Iterable<FileSystemEntry> resultEntries = innerEntries == null ? null : innerEntries.map(new Function1<FileSystemEntry,FileSystemEntry>()
        {
            @Override
            public FileSystemEntry run(FileSystemEntry innerEntry)
            {
                final Path outerEntryPath = FolderFileSystem.this.getOuterPath(innerEntry.getPath());
                return innerEntry instanceof File ? new File(FolderFileSystem.this, outerEntryPath) : new Folder(FolderFileSystem.this, outerEntryPath);
            }
        });

        final Throwable innerError = innerResult.getError();
        Throwable resultError;
        if (innerError instanceof FolderNotFoundException)
        {
            final Path outerPath = getOuterPath(((FolderNotFoundException)innerError).getFolderPath());
            resultError = new FolderNotFoundException(outerPath);
        }
        else
        {
            resultError = innerError;
        }

        return Result.done(resultEntries, resultError);
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        return innerFileSystem.folderExists(getInnerPath(rootedFolderPath));
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        Result<Folder> result = FileSystemBase.validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final Result<Folder> innerResult = innerFileSystem.createFolder(getInnerPath(rootedFolderPath));

            final Folder createdFolder = innerResult.getValue();
            final Folder resultFolder = (createdFolder == null ? null : new Folder(FolderFileSystem.this, getOuterPath(createdFolder.getPath())));

            final Throwable error = innerResult.getError();
            Throwable resultError;
            if (error instanceof FolderAlreadyExistsException)
            {
                final Path outerPath = getOuterPath(((FolderAlreadyExistsException)error).getFolderPath());
                resultError = new FolderAlreadyExistsException(outerPath);
            }
            else
            {
                resultError = error;
            }

            result = Result.done(resultFolder, resultError);
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFolder(Path rootedFolderPath)
    {
        Result<Boolean> result = FileSystemBase.validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            result = innerFileSystem.deleteFolder(getInnerPath(rootedFolderPath));
            if (result.getError() instanceof FolderNotFoundException)
            {
                final Path outerFolderPath = getOuterPath(((FolderNotFoundException)result.getError()).getFolderPath());
                result = Result.done(
                    result.getValue(),
                    new FolderNotFoundException(outerFolderPath));
            }
        }

        return result;
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        Result<Boolean> result = validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.fileExists(getInnerPath(rootedFilePath));
        }
        return result;
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        Result<File> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Path innerFilePath = getInnerPath(rootedFilePath);
            final Result<File> innerResult = innerFileSystem.createFile(innerFilePath);

            final File createdFile = innerResult.getValue() == null ? null : new File(this, getOuterPath(innerResult.getValue().getPath()));
            final Throwable error = innerResult.getError() instanceof FileAlreadyExistsException ? new FileAlreadyExistsException(rootedFilePath) : innerResult.getError();
            result = Result.done(createdFile, error);
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFile(Path rootedFilePath)
    {
        Result<Boolean> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.deleteFile(getInnerPath(rootedFilePath));
            if (result.getError() instanceof FileNotFoundException)
            {
                result = Result.done(result.getValue(), new FileNotFoundException(rootedFilePath));
            }
        }

        return result;
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        Result<DateTime> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.getFileLastModified(getInnerPath(rootedFilePath));
            if (result.getError() instanceof FileNotFoundException)
            {
                result = Result.done(result.getValue(), new FileNotFoundException(rootedFilePath));
            }
        }

        return result;
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        Result<ByteReadStream> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.getFileContentByteReadStream(getInnerPath(rootedFilePath));
            if (result.getError() instanceof FileNotFoundException)
            {
                result = Result.done(result.getValue(), new FileNotFoundException(rootedFilePath));
            }
        }

        return result;
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        Result<ByteWriteStream> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.getFileContentByteWriteStream(getInnerPath(rootedFilePath));
        }

        return result;
    }
}
