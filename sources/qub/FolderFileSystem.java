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
        Result<FolderFileSystem> result = Result.notNull(innerFileSystem, "innerFileSystem");
        if (result == null)
        {
            result = Result.notNull(baseFolderPath, "baseFolderPath");
            if (result == null)
            {
                result = Result.success(new FolderFileSystem(innerFileSystem, baseFolderPath));
            }
        }
        return result;
    }

    public Result<Boolean> create()
    {
        final Result<Folder> createResult = innerFileSystem.createFolder(baseFolderPath);
        return Result.done(!createResult.hasError(), createResult.getError());
    }

    public AsyncFunction<Result<Boolean>> createAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
        return fileSystemAsyncRunner.schedule(new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    return create();
                }
            })
            .thenOn(currentAsyncRunner);
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

    private Result<Path> getInnerPath(Path outerPath)
    {
        Result<Path> result = outerPath.resolve();
        if (!result.hasError())
        {
            result = Result.success(Path.parse(baseFolderPath.toString() + result.getValue().toString()));
        }
        return result;
    }

    private Path getOuterPath(Path innerPath)
    {
        String outerPathString = innerPath.toString().substring(baseFolderPath.toString().length());
        if (outerPathString.isEmpty())
        {
            outerPathString = "/";
        }
        return Path.parse(outerPathString);
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
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        Result<Iterable<FileSystemEntry>> result = validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            Iterable<FileSystemEntry> resultEntries = null;
            Throwable resultError = null;

            final Result<Path> innerFolderPath = getInnerPath(rootedFolderPath);
            if (innerFolderPath.hasError())
            {
                resultError = innerFolderPath.getError();
            }
            else
            {
                final Result<Iterable<FileSystemEntry>> innerResult = innerFileSystem.getFilesAndFolders(innerFolderPath.getValue());

                final Iterable<FileSystemEntry> innerEntries = innerResult.getValue();
                resultEntries = innerEntries == null ? null : innerEntries.map(new Function1<FileSystemEntry, FileSystemEntry>()
                {
                    @Override
                    public FileSystemEntry run(FileSystemEntry innerEntry)
                    {
                        final Path outerEntryPath = FolderFileSystem.this.getOuterPath(innerEntry.getPath());
                        return innerEntry instanceof File ? new File(FolderFileSystem.this, outerEntryPath) : new Folder(FolderFileSystem.this, outerEntryPath);
                    }
                });

                final Throwable innerError = innerResult.getError();
                if (innerError instanceof FolderNotFoundException)
                {
                    final Path outerPath = getOuterPath(((FolderNotFoundException)innerError).getFolderPath());
                    resultError = new FolderNotFoundException(outerPath);
                }
                else
                {
                    resultError = innerError;
                }
            }
            result = Result.done(resultEntries, resultError);
        }
        return result;
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        Result<Boolean> result = validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final Result<Path> innerPath = getInnerPath(rootedFolderPath);
            result = innerPath.hasError() ? Result.<Boolean>error(innerPath.getError()) : innerFileSystem.folderExists(innerPath.getValue());
        }
        return result;
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        Result<Folder> result = FileSystemBase.validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final Result<Path> innerPath = getInnerPath(rootedFolderPath);
            if (innerPath.hasError())
            {
                result = Result.error(innerPath.getError());
            }
            else
            {
                final Result<Folder> innerResult = innerFileSystem.createFolder(innerPath.getValue());

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
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFolder(Path rootedFolderPath)
    {
        Result<Boolean> result = FileSystemBase.validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final Result<Path> innerFolderPath = getInnerPath(rootedFolderPath);
            if (innerFolderPath.hasError())
            {
                result = Result.error(innerFolderPath.getError());
            }
            else if (innerFolderPath.getValue().equals(baseFolderPath))
            {
                result = Result.done(false, new IllegalArgumentException("Cannot delete a root folder (" + rootedFolderPath.resolve().getValue() + ")."));
            }
            else
            {
                result = innerFileSystem.deleteFolder(innerFolderPath.getValue());
                if (result.getError() instanceof FolderNotFoundException)
                {
                    final Path outerFolderPath = getOuterPath(((FolderNotFoundException)result.getError()).getFolderPath());
                    result = Result.done(
                        result.getValue(),
                        new FolderNotFoundException(outerFolderPath));
                }
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
            final Result<Path> innerFilePath = getInnerPath(rootedFilePath);
            if (innerFilePath.hasError())
            {
                result = Result.error(innerFilePath.getError());
            }
            else
            {
                result = innerFileSystem.fileExists(innerFilePath.getValue());
            }
        }
        return result;
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        Result<File> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Result<Path> innerFilePath = getInnerPath(rootedFilePath);
            if (innerFilePath.hasError())
            {
                result = Result.error(innerFilePath.getError());
            }
            else
            {
                final Result<File> innerResult = innerFileSystem.createFile(innerFilePath.getValue());

                final File createdFile = innerResult.getValue() == null ? null : new File(this, getOuterPath(innerResult.getValue().getPath()));
                final Throwable error = innerResult.getError() instanceof FileAlreadyExistsException ? new FileAlreadyExistsException(rootedFilePath) : innerResult.getError();
                result = Result.done(createdFile, error);
            }
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFile(Path rootedFilePath)
    {
        Result<Boolean> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Result<Path> innerFilePath = getInnerPath(rootedFilePath);
            if (innerFilePath.hasError())
            {
                result = Result.error(innerFilePath.getError());
            }
            else
            {
                result = innerFileSystem.deleteFile(innerFilePath.getValue());
                if (result.getError() instanceof FileNotFoundException)
                {
                    result = Result.done(result.getValue(), new FileNotFoundException(getOuterPath(innerFilePath.getValue())));
                }
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
            final Result<Path> innerFilePath = getInnerPath(rootedFilePath);
            if (innerFilePath.hasError())
            {
                result = Result.error(innerFilePath.getError());
            }
            else
            {
                result = innerFileSystem.getFileLastModified(innerFilePath.getValue());
                if (result.getError() instanceof FileNotFoundException)
                {
                    result = Result.done(result.getValue(), new FileNotFoundException(getOuterPath(innerFilePath.getValue())));
                }
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
            final Result<Path> innerFilePath = getInnerPath(rootedFilePath);
            if (innerFilePath.hasError())
            {
                result = Result.error(innerFilePath.getError());
            }
            else
            {
                result = innerFileSystem.getFileContentByteReadStream(innerFilePath.getValue());
                if (result.getError() instanceof FileNotFoundException)
                {
                    result = Result.done(result.getValue(), new FileNotFoundException(getOuterPath(innerFilePath.getValue())));
                }
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
            final Result<Path> innerFilePath = getInnerPath(rootedFilePath);
            if (innerFilePath.hasError())
            {
                result = Result.error(innerFilePath.getError());
            }
            else
            {
                result = innerFileSystem.getFileContentByteWriteStream(innerFilePath.getValue());
            }
        }

        return result;
    }
}
