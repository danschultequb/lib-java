package qub;

/**
 * A FileSystem implementation that is completely stored in the memory of the running application.
 */
public class InMemoryFileSystem implements FileSystem
{
    private final Function0<AsyncRunner> asyncRunnerGetter;
    private final List<InMemoryRoot> roots;
    private final Clock clock;

    public InMemoryFileSystem(Clock clock, Function0<AsyncRunner> asyncRunnerGetter)
    {
        PreCondition.assertNotNull(clock, "clock");
        PreCondition.assertNotNull(asyncRunnerGetter, "asyncRunnerGetter");

        this.asyncRunnerGetter = asyncRunnerGetter;
        roots = List.create();
        this.clock = clock;
    }

    private AsyncRunner getAsyncRunner()
    {
        return asyncRunnerGetter.run();
    }

    private InMemoryRoot getInMemoryRoot(String inMemoryRootPath)
    {
        return getInMemoryRoot(Path.parse(inMemoryRootPath));
    }

    private InMemoryRoot getInMemoryRoot(final Path inMemoryRootPath)
    {
        return roots.first((InMemoryRoot inMemoryRoot) -> inMemoryRoot.getPath().equals(inMemoryRootPath));
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
        return filePath.resolve()
            .thenResult((Path resolvedRootedFilePath) ->
            {
                return getInMemoryFolder(resolvedRootedFilePath.getParent())
                    .catchErrorResult(FolderNotFoundException.class, () -> Result.error(new FileNotFoundException(filePath)))
                    .thenResult((InMemoryFolder inMemoryFolder) ->
                    {
                        final String fileName = filePath.getSegments().last();
                        final InMemoryFile inMemoryFile = inMemoryFolder.getFile(fileName);
                        return inMemoryFile != null
                            ? Result.success(inMemoryFile)
                            : Result.error(new FileNotFoundException(filePath.resolve().await()));
                    });
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
    public Result<Iterable<Root>> getRoots()
    {
        return getAsyncRunner().schedule(() -> roots.map((InMemoryRoot inMemoryRoot) -> new Root(this, inMemoryRoot.getPath())));
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

        return getInMemoryFolder(rootedFolderPath)
            .then(() -> true)
            .catchError(FolderNotFoundException.class, () -> false);
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return rootedFolderPath.resolve()
            .thenResult((Path resolvedRootedFolderPath) ->
            {
                final Path rootPath = rootedFolderPath.getRoot().await();
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

        return getInMemoryFile(rootedFilePath)
            .then(() -> true)
            .catchError(FileNotFoundException.class, () -> false);
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<File> result;
        final Path rootPath = rootedFilePath.getRoot().await();
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

        return rootedFilePath.resolve()
            .thenResult((Path resolvedRootedFilePath) ->
            {
                return getInMemoryFolder(resolvedRootedFilePath.getParent())
                    .thenResult((InMemoryFolder parentFolder) ->
                    {
                        return parentFolder.deleteFile(resolvedRootedFilePath.getSegments().last())
                            ? Result.<Void>success()
                            : Result.error(new FileNotFoundException(resolvedRootedFilePath));
                    });
            });
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return rootedFilePath.resolve()
            .thenResult((Path resolvedRootedFilePath) ->
            {
                return getInMemoryFile(resolvedRootedFilePath)
                    .then(InMemoryFile::getLastModified);
            });
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getInMemoryFile(rootedFilePath)
            .then(InMemoryFile::getContentByteReadStream);
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return rootedFilePath.resolve()
            .thenResult((Path resolvedRootedFilePath) ->
            {
                return getInMemoryFile(resolvedRootedFilePath)
                    .catchErrorResult(FileNotFoundException.class, () ->
                    {
                        final Path parentFolderPath = rootedFilePath.getParent();
                        return createInMemoryFolder(parentFolderPath)
                            .thenResult(() -> getInMemoryFolder(parentFolderPath))
                            .thenResult((InMemoryFolder parentFolder) ->
                            {
                                parentFolder.createFile(resolvedRootedFilePath.getSegments().last());
                                return getInMemoryFile(resolvedRootedFilePath);
                            });
                    })
                    .then(InMemoryFile::getContentByteWriteStream);
            });
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
        rootPath = rootPath.getRoot().await();
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