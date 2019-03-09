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
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

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
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        return get(innerFileSystem, Path.parse(baseFolderPath));
    }

    public static Result<FolderFileSystem> get(FileSystem innerFileSystem, Path baseFolderPath)
    {
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        return Result.success(new FolderFileSystem(innerFileSystem, baseFolderPath));
    }

    public Result<Void> create()
    {
        return innerFileSystem.createFolder(baseFolderPath)
            .then(() -> {});
    }

    public AsyncFunction<Result<Void>> createAsync()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::create);
    }

    public Result<Void> delete()
    {
        return innerFileSystem.deleteFolder(baseFolderPath);
    }

    public AsyncFunction<Result<Void>> deleteAsync()
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

    private Result<Path> getInnerPath(Path outerPath, boolean isFolderPath)
    {
        FileSystem.validateRootedFolderPath(outerPath, "outerPath");

        return outerPath.resolve()
            .thenResult((Path resolvedOuterPath) ->
            {
                return resolvedOuterPath.getRoot()
                    .thenResult((Path outerRootPath) ->
                    {
                        Result<Path> innerResult;
                        if (!outerRootPath.equals(Path.parse("/")))
                        {
                            if (isFolderPath)
                            {
                                innerResult = Result.error(new FolderNotFoundException(outerPath));
                            }
                            else
                            {
                                innerResult = Result.error(new FileNotFoundException(outerPath));
                            }
                        }
                        else
                        {
                            innerResult = resolvedOuterPath.withoutRoot()
                                .thenResult((Function1<Path,Result<Path>>)baseFolderPath::resolve)
                                .catchError(NotFoundException.class, () -> baseFolderPath);
                        }
                        return innerResult;
                    });
            });
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
        return Result.success(Iterable.create(new Root(this, Path.parse("/"))));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getInnerPath(rootedFolderPath, true)
            .thenResult((Path innerPath) ->
            {
                return innerFileSystem.getFilesAndFolders(innerPath)
                    .then((Iterable<FileSystemEntry> innerEntries) ->
                    {
                        return innerEntries == null ? null : innerEntries.map((FileSystemEntry innerEntry) ->
                        {
                            final Path outerEntryPath = getOuterPath(innerEntry.getPath());
                            return innerEntry instanceof File
                                ? new File(this, outerEntryPath)
                                : new Folder(this, outerEntryPath);
                        });
                    })
                    .catchErrorResult(FolderNotFoundException.class, (FolderNotFoundException error) ->
                    {
                        return Result.error(new FolderNotFoundException(getOuterPath(error.getFolderPath())));
                    });
            });
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getInnerPath(rootedFolderPath, true)
            .thenResult(innerFileSystem::folderExists);
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getInnerPath(rootedFolderPath, true)
            .thenResult((Path innerPath) ->
            {
                return innerFileSystem.createFolder(innerPath)
                    .then((Folder createdInnerFolder) ->
                    {
                        return new Folder(this, getOuterPath(createdInnerFolder.getPath()));
                    })
                    .catchErrorResult(FolderAlreadyExistsException.class, (FolderAlreadyExistsException error) ->
                    {
                        return Result.error(new FolderAlreadyExistsException(getOuterPath(error.getFolderPath())));
                    });
            });
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getInnerPath(rootedFolderPath, true)
            .thenResult((Path innerFolderPath) ->
            {
                return innerFolderPath.equals(baseFolderPath)
                    ? Result.error(new IllegalArgumentException("Cannot delete a root folder (" + rootedFolderPath.resolve().awaitError() + ")."))
                    : innerFileSystem.deleteFolder(innerFolderPath)
                        .catchErrorResult(FolderNotFoundException.class, (FolderNotFoundException error) ->
                        {
                            return Result.error(new FolderNotFoundException(getOuterPath(error.getFolderPath())));
                        });
            });
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInnerPath(rootedFilePath, false)
            .thenResult(innerFileSystem::fileExists);
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInnerPath(rootedFilePath, false)
            .thenResult((Path innerFilePath) ->
            {
                return innerFileSystem.createFile(innerFilePath)
                    .then((File createdFile) -> new File(this, getOuterPath(createdFile.getPath())))
                    .catchErrorResult(FileAlreadyExistsException.class, (FileAlreadyExistsException error) ->
                    {
                        return Result.error(new FileAlreadyExistsException(rootedFilePath));
                    });
            });
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInnerPath(rootedFilePath, false)
            .thenResult((Path innerFilePath) ->
            {
                return innerFileSystem.deleteFile(innerFilePath)
                    .catchErrorResult(FileNotFoundException.class, (FileNotFoundException error) ->
                    {
                        return Result.error(new FileNotFoundException(getOuterPath(innerFilePath)));
                    });
            });
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInnerPath(rootedFilePath, false)
            .thenResult((Path innerFilePath) ->
            {
                return innerFileSystem.getFileLastModified(innerFilePath)
                    .catchErrorResult(FileNotFoundException.class, (FileNotFoundException error) ->
                    {
                        return Result.error(new FileNotFoundException(getOuterPath(error.getFilePath())));
                    });
            });
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInnerPath(rootedFilePath, false)
            .thenResult((Path innerFilePath) ->
            {
                return innerFileSystem.getFileContentByteReadStream(innerFilePath)
                    .catchErrorResult(FileNotFoundException.class, (FileNotFoundException error) ->
                    {
                        return Result.error(new FileNotFoundException(getOuterPath(error.getFilePath())));
                    });
            });
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInnerPath(rootedFilePath, false)
            .thenResult(innerFileSystem::getFileContentByteWriteStream);
    }
}
