package qub;

/**
 * A FileSystem implementation that is completely stored in the memory of the running application.
 */
public class InMemoryFileSystem implements FileSystem
{
    private final List<InMemoryRoot> roots;
    private final AsyncRunner asyncRunner;
    private final Clock clock;

    public InMemoryFileSystem(AsyncRunner asyncRunner)
    {
        this(asyncRunner, asyncRunner.getClock());
    }

    public InMemoryFileSystem(Clock clock)
    {
        this(null, clock);
    }

    /**
     * Create a new InMemoryFileSystem.
     */
    public InMemoryFileSystem(AsyncRunner asyncRunner, Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        roots = new ArrayList<>();
        this.asyncRunner = asyncRunner;
        this.clock = clock;
    }

    private InMemoryRoot getInMemoryRoot(String inMemoryRootPath)
    {
        return getInMemoryRoot(Path.parse(inMemoryRootPath));
    }

    private InMemoryRoot getInMemoryRoot(final Path inMemoryRootPath)
    {
        return roots.first(new Function1<InMemoryRoot, Boolean>()
        {
            @Override
            public Boolean run(InMemoryRoot inMemoryRoot)
            {
                return inMemoryRoot.getPath().equals(inMemoryRootPath);
            }
        });
    }

    private Result<InMemoryFolder> getInMemoryFolder(Path inMemoryFolderPath)
    {
        return inMemoryFolderPath.resolve()
            .thenResult((Path resolvedInMemoryFolderPath) ->
            {
                final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getSegments().iterate();
                InMemoryFolder folder = getInMemoryRoot(folderPathSegments.first());
                while (folder != null && folderPathSegments.next())
                {
                    folder = folder.getFolder(folderPathSegments.getCurrent());
                }
                return folder != null ? Result.success(folder) : Result.error(new FolderNotFoundException(inMemoryFolderPath));
            });
    }

    private Result<Boolean> createInMemoryFolder(Path inMemoryFolderPath)
    {
        return inMemoryFolderPath.resolve()
            .thenResult((Path resolvedInMemoryFolderPath) ->
            {
                boolean createdFolder = false;
                final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getSegments().iterate();
                final Value<InMemoryFolder> folder = Value.create(getInMemoryRoot(folderPathSegments.first()));
                while (folderPathSegments.next())
                {
                    final String folderName = folderPathSegments.getCurrent();
                    createdFolder = folder.get().createFolder(folderName, folder);
                }
                return createdFolder ? Result.successTrue() : Result.successFalse();
            });
    }

    private Result<InMemoryFile> getInMemoryFile(Path filePath)
    {
        return getInMemoryFolder(filePath.getParent())
            .catchErrorResult(FolderNotFoundException.class, () -> Result.error(new FileNotFoundException(filePath)))
            .thenResult((InMemoryFolder inMemoryFolder) ->
            {
                final String fileName = filePath.getSegments().last();
                final InMemoryFile inMemoryFile = inMemoryFolder.getFile(fileName);
                return inMemoryFile != null
                    ? Result.success(inMemoryFile)
                    : Result.error(new FileNotFoundException(filePath.resolve().awaitError()));
            });
    }

    public Result<Void> setFileCanDelete(String rootedFilePath, boolean canDelete)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return setFileCanDelete(Path.parse(rootedFilePath), canDelete);
    }

    public Result<Void> setFileCanDelete(Path rootedFilePath, boolean canDelete)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInMemoryFile(rootedFilePath)
            .then((InMemoryFile inMemoryFile) -> inMemoryFile.setCanDelete(canDelete));
    }

    public Result<Void> setFolderCanDelete(String rootedFolderPath, boolean canDelete)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return setFolderCanDelete(Path.parse(rootedFolderPath), canDelete);
    }

    public Result<Void> setFolderCanDelete(Path rootedFolderPath, boolean canDelete)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getInMemoryFolder(rootedFolderPath)
            .then((InMemoryFolder inMemoryFolder) -> inMemoryFolder.setCanDelete(canDelete));
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public Result<Iterable<Root>> getRoots()
    {
        return Result.success(roots.map((InMemoryRoot inMemoryRoot) -> new Root(this, inMemoryRoot.getPath())));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getInMemoryFolder(rootedFolderPath)
            .then((InMemoryFolder folder) ->
            {
                final List<FileSystemEntry> entries = List.create();

                entries.addAll(folder.getFolders()
                    .order((InMemoryFolder lhs, InMemoryFolder rhs) ->
                        Strings.lessThan(lhs.getName(), rhs.getName()))
                    .map((InMemoryFolder childFolder) ->
                        new Folder(this, rootedFolderPath.concatenateSegment(childFolder.getName()))));

                entries.addAll(folder.getFiles()
                    .order((InMemoryFile lhs, InMemoryFile rhs) ->
                        Strings.lessThan(lhs.getName(), rhs.getName()))
                    .map((InMemoryFile childFile) ->
                        new File(this, rootedFolderPath.concatenateSegment(childFile.getName()))));

                return entries;
            });
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        Result<Boolean> result;
        final Result<InMemoryFolder> inMemoryFolder = getInMemoryFolder(rootedFolderPath);
        if (inMemoryFolder.hasError())
        {
            if (inMemoryFolder.getError() instanceof FolderNotFoundException)
            {
                result = Result.successFalse();
            }
            else
            {
                result = Result.error(inMemoryFolder.getError());
            }
        }
        else
        {
            result = Result.successTrue();
        }
        return result;
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return rootedFolderPath.resolve()
            .thenResult((Path resolvedRootedFolderPath) ->
            {
                final Path rootPath = rootedFolderPath.getRoot().awaitError();
                return getInMemoryRoot(rootPath) == null
                    ? Result.error(new RootNotFoundException(rootPath))
                    : createInMemoryFolder(resolvedRootedFolderPath)
                        .thenResult((Boolean folderCreated) ->
                        {
                            return !Booleans.isTrue(folderCreated)
                                ? Result.error(new FolderAlreadyExistsException(resolvedRootedFolderPath))
                                : getFolder(resolvedRootedFolderPath);
                        });
            });
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return rootedFolderPath.resolve()
            .thenResult((Path resolvedRootedFolderPath) ->
            {
                final Path parentFolderPath = resolvedRootedFolderPath.getParent();
                return parentFolderPath == null
                    ? Result.error(new IllegalArgumentException("Cannot delete a root folder (" + resolvedRootedFolderPath + ")."))
                    : getInMemoryFolder(parentFolderPath)
                        .catchErrorResult(FolderNotFoundException.class, () -> Result.error(new FolderNotFoundException(resolvedRootedFolderPath)))
                        .thenResult((InMemoryFolder parentFolder) ->
                        {
                            return parentFolder.deleteFolder(resolvedRootedFolderPath.getSegments().last())
                                ? Result.success()
                                : Result.error(new FolderNotFoundException(resolvedRootedFolderPath));
                        });
            });
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<Boolean> result;
        final Result<InMemoryFile> inMemoryFile = getInMemoryFile(rootedFilePath);
        if (inMemoryFile.hasError())
        {
            if (inMemoryFile.getError() instanceof FileNotFoundException)
            {
                result = Result.successFalse();
            }
            else
            {
                result = Result.error(inMemoryFile.getError());
            }
        }
        else
        {
            result = Result.successTrue();
        }
        return result;
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<File> result;
        final Path rootPath = rootedFilePath.getRoot().awaitError();
        if (getInMemoryRoot(rootPath) == null)
        {
            result = Result.error(new RootNotFoundException(rootPath));
        }
        else
        {
            final Path parentFolderPath = rootedFilePath.getParent();
            createInMemoryFolder(parentFolderPath);

            result = getInMemoryFolder(parentFolderPath)
                .thenResult((InMemoryFolder parentFolder) ->
                {
                    return parentFolder.createFile(rootedFilePath.getSegments().last())
                        ? getFile(rootedFilePath)
                        : Result.error(new FileAlreadyExistsException(rootedFilePath));
                });
        }
        return result;
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<Void> result;
        final Result<Path> resolvedRootedFilePath = rootedFilePath.resolve();
        if (resolvedRootedFilePath.hasError())
        {
            result = Result.error(resolvedRootedFilePath.getError());
        }
        else
        {
            final Result<InMemoryFolder> parentFolder = getInMemoryFolder(resolvedRootedFilePath.getValue().getParent());
            if (parentFolder.hasError())
            {
                result = Result.error(parentFolder.getError());
            }
            else if (!parentFolder.getValue().deleteFile(resolvedRootedFilePath.getValue().getSegments().last()))
            {
                result = Result.error(new FileNotFoundException(resolvedRootedFilePath.getValue()));
            }
            else
            {
                result = Result.success();
            }
        }
        return result;
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<DateTime> result;
        final Result<Path> resolvedRootedFilePath = rootedFilePath.resolve();
        if (resolvedRootedFilePath.hasError())
        {
            result = Result.error(resolvedRootedFilePath.getError());
        }
        else
        {
            final Result<InMemoryFile> file = getInMemoryFile(resolvedRootedFilePath.getValue());
            if (file.hasError())
            {
                result = Result.error(file.getError());
            }
            else
            {
                result = Result.success(file.getValue().getLastModified());
            }
        }

        return result;
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<ByteReadStream> result;
        final Result<InMemoryFile> file = getInMemoryFile(rootedFilePath);
        if (file.hasError())
        {
            result = Result.error(file.getError());
        }
        else
        {
            result = Result.success(file.getValue().getContentByteReadStream());
        }

        return result;
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<ByteWriteStream> result;
        final Result<Path> resolvedRootedFilePath = rootedFilePath.resolve();
        if (resolvedRootedFilePath.hasError())
        {
            result = Result.error(resolvedRootedFilePath.getError());
        }
        else
        {
            Result<InMemoryFile> file = getInMemoryFile(rootedFilePath);
            if (file.hasError())
            {
                final Path parentFolderPath = rootedFilePath.getParent();
                Result<InMemoryFolder> parentFolder = getInMemoryFolder(parentFolderPath);
                if (parentFolder.hasError() && parentFolder.getError() instanceof FolderNotFoundException)
                {
                    createInMemoryFolder(parentFolderPath);
                    parentFolder = getInMemoryFolder(parentFolderPath);
                }

                parentFolder.getValue().createFile(rootedFilePath.getSegments().last());
                file = getInMemoryFile(rootedFilePath);
            }
            result = Result.success(file.getValue().getContentByteWriteStream());
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
    public Result<Root> createRoot(String rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return createRoot(Path.parse(rootPath));
    }

    /**
     * Create a new Root in this FileSystem and returns whether or not this function created the
     * Root.
     * @param rootPath The String representation of the path to the Root to create in this
     *                 FileSystem.
     * @return Whether or not this function created the Root.
     */
    public Result<Root> createRoot(Path rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        Result<Root> result;
        rootPath = rootPath.getRoot().throwErrorOrGetValue();
        if (getInMemoryRoot(rootPath) != null)
        {
            result = Result.error(new RootAlreadyExistsException(rootPath));
        }
        else
        {
            roots.add(new InMemoryRoot(rootPath.getSegments().first(), clock));
            result = getRoot(rootPath);
        }
        return result;
    }
}