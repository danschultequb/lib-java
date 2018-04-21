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
    public AsyncFunction<Result<Iterable<Root>>> getRootsAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return currentAsyncRunner.<Iterable<Root>>success(Array.fromValues(new Root[]
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
    public AsyncFunction<Result<Boolean>> folderExistsAsync(Path folderPath)
    {
        return innerFileSystem.folderExistsAsync(getInnerPath(folderPath));
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(final Path rootedFolderPath)
    {
        AsyncFunction<Result<Folder>> result = validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = innerFileSystem.createFolderAsync(getInnerPath(rootedFolderPath))
                .then(new Function1<Result<Folder>, Result<Folder>>()
                {
                    @Override
                    public Result<Folder> run(Result<Folder> createFolderResult)
                    {
                        final Folder createdFolder = createFolderResult.getValue();
                        final Folder resultFolder = (createdFolder == null ? null : new Folder(FolderFileSystem.this, getOuterPath(createdFolder.getPath())));

                        final Throwable error = createFolderResult.getError();
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

                        return Result.done(resultFolder, resultError);
                    }
                });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(Path rootedFolderPath)
    {
        AsyncFunction<Result<Boolean>> result = validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = innerFileSystem.deleteFolderAsync(getInnerPath(rootedFolderPath))
                .then(new Function1<Result<Boolean>, Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run(Result<Boolean> deleteFolderResult)
                    {
                        Result<Boolean> result = deleteFolderResult;
                        if (result.getError() instanceof FolderNotFoundException)
                        {
                            final Path outerFolderPath = getOuterPath(((FolderNotFoundException)result.getError()).getFolderPath());
                            result = Result.done(
                                deleteFolderResult.getValue(),
                                new FolderNotFoundException(outerFolderPath));
                        }
                        return result;
                    }
                });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(Path rootedFilePath)
    {
        AsyncFunction<Result<Boolean>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.fileExistsAsync(getInnerPath(rootedFilePath));
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(final Path rootedFilePath)
    {
        AsyncFunction<Result<File>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            final Path innerFilePath = getInnerPath(rootedFilePath);
            result = innerFileSystem.createFileAsync(innerFilePath)
                .then(new Function1<Result<File>, Result<File>>()
                {
                    @Override
                    public Result<File> run(Result<File> createFileResult)
                    {
                        final File createdFile = createFileResult.getValue() == null ? null : new File(FolderFileSystem.this, getOuterPath(createFileResult.getValue().getPath()));
                        final Throwable error = createFileResult.getError() instanceof FileAlreadyExistsException ? new FileAlreadyExistsException(rootedFilePath) : createFileResult.getError();
                        return Result.done(createdFile, error);
                    }
                });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(final Path rootedFilePath)
    {
        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.deleteFileAsync(getInnerPath(rootedFilePath))
                .then(new Function1<Result<Boolean>, Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run(Result<Boolean> deleteFileResult)
                    {
                        final Throwable error = deleteFileResult.getError() instanceof FileNotFoundException ? new FileNotFoundException(rootedFilePath) : deleteFileResult.getError();
                        return Result.done(deleteFileResult.getValue(), error);
                    }
                });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(final Path rootedFilePath)
    {
        AsyncFunction<Result<DateTime>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.getFileLastModifiedAsync(getInnerPath(rootedFilePath))
                .then(new Function1<Result<DateTime>, Result<DateTime>>()
                {
                    @Override
                    public Result<DateTime> run(Result<DateTime> getFileLastModifiedResult)
                    {
                        final Throwable error = getFileLastModifiedResult.getError() instanceof FileNotFoundException ? new FileNotFoundException(rootedFilePath) : getFileLastModifiedResult.getError();
                        return Result.done(getFileLastModifiedResult.getValue(), error);
                    }
                });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(final Path rootedFilePath)
    {
        AsyncFunction<Result<ByteReadStream>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.getFileContentByteReadStreamAsync(getInnerPath(rootedFilePath))
                .then(new Function1<Result<ByteReadStream>, Result<ByteReadStream>>()
                {
                    @Override
                    public Result<ByteReadStream> run(Result<ByteReadStream> getFileContentByteReadStreamResult)
                    {
                        final Throwable error = getFileContentByteReadStreamResult.getError() instanceof FileNotFoundException ? new FileNotFoundException(rootedFilePath) : getFileContentByteReadStreamResult.getError();
                        return Result.done(getFileContentByteReadStreamResult.getValue(), error);
                    }
                });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(final Path rootedFilePath)
    {
        AsyncFunction<Result<ByteWriteStream>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = innerFileSystem.getFileContentByteWriteStreamAsync(getInnerPath(rootedFilePath))
                .then(new Function1<Result<ByteWriteStream>, Result<ByteWriteStream>>()
                {
                    @Override
                    public Result<ByteWriteStream> run(Result<ByteWriteStream> getFileContentByteWriteStreamResult)
                    {
                        final Throwable error = getFileContentByteWriteStreamResult.getError() instanceof FileNotFoundException ? new FileNotFoundException(rootedFilePath) : getFileContentByteWriteStreamResult.getError();
                        return Result.done(getFileContentByteWriteStreamResult.getValue(), error);
                    }
                });
        }

        return result;
    }
}
