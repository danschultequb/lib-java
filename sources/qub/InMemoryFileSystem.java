package qub;

/**
 * A {@link FileSystem} implementation that is completely stored in the memory of the running
 * application.
 */
public class InMemoryFileSystem implements FileSystem
{
    private final List<InMemoryRoot> roots;
    private final Clock clock;

    private InMemoryFileSystem(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        this.roots = List.create();
        this.clock = clock;
    }

    public static InMemoryFileSystem create()
    {
        return InMemoryFileSystem.create(ManualClock.create());
    }

    public static InMemoryFileSystem create(Clock clock)
    {
        return new InMemoryFileSystem(clock);
    }

    private Result<InMemoryRoot> getInMemoryRoot(String inMemoryRootPath)
    {
        return this.getInMemoryRoot(Path.parse(inMemoryRootPath));
    }

    private Result<InMemoryRoot> getInMemoryRoot(Path inMemoryRootPath)
    {
        return roots.first((InMemoryRoot inMemoryRoot) -> inMemoryRoot.getPath().equals(inMemoryRootPath))
            .convertError(NotFoundException.class, () -> new RootNotFoundException(inMemoryRootPath));
    }

    private Result<InMemoryFolder> getInMemoryFolder(Path inMemoryFolderPath)
    {
        return inMemoryFolderPath.resolve()
            .then((Path resolvedInMemoryFolderPath) ->
            {
                final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getSegments().iterate();
                final String rootPath = folderPathSegments.first().await();
                InMemoryFolder folder = this.getInMemoryRoot(rootPath).await();

                while (folder != null && folderPathSegments.next())
                {
                    folder = folder.getFolder(folderPathSegments.getCurrent())
                        .catchError(NotFoundException.class)
                        .await();
                }

                if (folder == null)
                {
                    throw new FolderNotFoundException(inMemoryFolderPath);
                }
                return folder;
            });
    }

    private Result<Boolean> createInMemoryFolder(Path inMemoryFolderPath)
    {
        return inMemoryFolderPath.resolve()
            .then((Path resolvedInMemoryFolderPath) ->
            {
                boolean createdFolder = false;
                final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getSegments().iterate();
                InMemoryFolder folder = this.getInMemoryRoot(folderPathSegments.first().await()).await();
                while (folderPathSegments.next())
                {
                    final String folderName = folderPathSegments.getCurrent();
                    createdFolder = folder.createFolder(folderName);
                    folder = folder.getFolder(folderName).await();
                }
                return createdFolder;
            });
    }

    private Result<InMemoryFile> getInMemoryFile(Path filePath)
    {
        return filePath.resolve()
            .then((Path resolvedRootedFilePath) -> this.getInMemoryFolder(resolvedRootedFilePath.getParent().await()).await())
            .convertError(FolderNotFoundException.class, () -> new FileNotFoundException(filePath))
            .then((InMemoryFolder inMemoryFolder) ->
            {
                final String fileName = filePath.getSegments().last().await();
                final InMemoryFile inMemoryFile = inMemoryFolder.getFile(fileName).catchError(NotFoundException.class).await();
                if (inMemoryFile == null)
                {
                    throw new FileNotFoundException(filePath.resolve().await());
                }
                return inMemoryFile;
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
        return Result.success(roots.map((InMemoryRoot inMemoryRoot) -> new Root(this, inMemoryRoot.getPath())));
    }

    @Override
    public Result<DataSize> getRootTotalDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            final InMemoryRoot root = this.getInMemoryRoot(rootPath).await();
            return root.getTotalDataSize();
        });
    }

    @Override
    public Result<DataSize> getRootUnusedDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            final InMemoryRoot root = this.getInMemoryRoot(rootPath).await();
            return root.getUnusedDataSize();
        });
    }

    @Override
    public Iterator<FileSystemEntry> iterateEntries(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return LazyIterator.create(() ->
        {
            final InMemoryFolder folder = this.getInMemoryFolder(rootedFolderPath).await();

            final List<FileSystemEntry> entries = List.create();

            entries.addAll(folder.getFolders()
                .order((InMemoryFolder lhs, InMemoryFolder rhs) ->
                    Strings.lessThan(lhs.getName(), rhs.getName()))
                .map((InMemoryFolder childFolder) ->
                    new Folder(this, rootedFolderPath.concatenateSegments(childFolder.getName()))));

            entries.addAll(folder.getFiles()
                .order((InMemoryFile lhs, InMemoryFile rhs) ->
                    Strings.lessThan(lhs.getName(), rhs.getName()))
                .map((InMemoryFile childFile) ->
                    new File(this, rootedFolderPath.concatenateSegments(childFile.getName()))));

            return entries.iterate();
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

        return Result.create(() ->
        {
            final Path resolvedRootedFolderPath = rootedFolderPath.resolve().await();
            final Path rootPath = resolvedRootedFolderPath.getRoot().await();
            this.getInMemoryRoot(rootPath).await();
            if (!Booleans.isTrue(this.createInMemoryFolder(resolvedRootedFolderPath).await()))
            {
                throw new FolderAlreadyExistsException(resolvedRootedFolderPath);
            }

            return this.getFolder(resolvedRootedFolderPath).await();
        });
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            final Path resolvedRootedFolderPath = rootedFolderPath.resolve().await();
            final Path parentFolderPath = resolvedRootedFolderPath.getParent()
                .convertError(NotFoundException.class, () -> new IllegalArgumentException("Cannot delete a root folder (" + resolvedRootedFolderPath + ")."))
                .await();
            final InMemoryFolder parentFolder = this.getInMemoryFolder(parentFolderPath)
                .convertError(FolderNotFoundException.class, () -> new FolderNotFoundException(resolvedRootedFolderPath))
                .await();
            if (!parentFolder.deleteFolder(resolvedRootedFolderPath.getSegments().last().await()))
            {
                throw new FolderNotFoundException(resolvedRootedFolderPath);
            }
        });
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getInMemoryFile(rootedFilePath)
            .then(() -> true)
            .catchError(FileNotFoundException.class, () -> false);
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final Path rootPath = rootedFilePath.getRoot().await();
            if (this.getInMemoryRoot(rootPath) == null)
            {
                throw new RootNotFoundException(rootPath);
            }

            final Path parentFolderPath = rootedFilePath.getParent().await();
            this.createInMemoryFolder(parentFolderPath).await();

            return this.getInMemoryFolder(parentFolderPath)
                .then((InMemoryFolder parentFolder) ->
                {
                    if (!parentFolder.createFile(rootedFilePath.getSegments().last().await()))
                    {
                        throw new FileAlreadyExistsException(rootedFilePath);
                    }
                    return this.getFile(rootedFilePath).await();
                })
                .await();
        });
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return rootedFilePath.resolve()
            .then((Path resolvedRootedFilePath) ->
            {
                return this.getInMemoryFolder(resolvedRootedFilePath.getParent().await())
                    .then((InMemoryFolder parentFolder) ->
                    {
                        if (!parentFolder.deleteFile(resolvedRootedFilePath.getSegments().last().await()))
                        {
                            throw new FileNotFoundException(resolvedRootedFilePath);
                        }
                    })
                    .await();
            });
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return rootedFilePath.resolve()
            .then((Path resolvedRootedFilePath) ->
            {
                return this.getInMemoryFile(resolvedRootedFilePath)
                    .then(InMemoryFile::getLastModified)
                    .await();
            });
    }

    @Override
    public Result<DataSize> getFileContentDataSize(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return rootedFilePath.resolve()
            .then((Path resolvedRootedFilePath) ->
            {
                return this.getInMemoryFile(resolvedRootedFilePath)
                    .then(InMemoryFile::getContentsDataSize)
                    .await();
            });
    }

    @Override
    public Result<CharacterToByteReadStream> getFileContentReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getInMemoryFile(rootedFilePath)
            .then(InMemoryFile::getContentsReadStream);
    }

    @Override
    public Result<BufferedByteWriteStream> getFileContentsByteWriteStream(Path rootedFilePath, OpenWriteType openWriteType)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return Result.create(() ->
        {
            final Path resolvedRootedFilePath = rootedFilePath.resolve().await();
            final InMemoryFile inMemoryFile = this.getInMemoryFile(resolvedRootedFilePath)
                    .catchError(FileNotFoundException.class, () ->
                    {
                        final Path parentFolderPath = rootedFilePath.getParent().await();
                        this.createInMemoryFolder(parentFolderPath).await();
                        final InMemoryFolder parentFolder = this.getInMemoryFolder(parentFolderPath).await();
                        parentFolder.createFile(resolvedRootedFilePath.getSegments().last().await());
                        return this.getInMemoryFile(resolvedRootedFilePath).await();
                    })
                    .await();
            return inMemoryFile.getContentByteWriteStream(openWriteType);
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

        return this.createRoot(Path.parse(rootPath));
    }

    /**
     * Create a new Root in this FileSystem and returns whether or not this function created the
     * Root.
     * @param rootPath The String representation of the path to the Root to create in this
     *                 FileSystem.
     * @return Whether or not this function created the Root.
     */
    public Result<Root> createRoot(String rootPath, DataSize rootTotalDataSize)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");
        PreCondition.assertNotNull(rootTotalDataSize, "rootTotalDataSize");
        PreCondition.assertGreaterThan(rootTotalDataSize, DataSize.zero, "rootTotalDataSize");

        return this.createRoot(Path.parse(rootPath), rootTotalDataSize);
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

        return this.createRootInner(rootPath, null);
    }

    /**
     * Create a new Root in this FileSystem and returns whether or not this function created the
     * Root.
     * @param rootPath The String representation of the path to the Root to create in this
     *                 FileSystem.
     * @return Whether or not this function created the Root.
     */
    public Result<Root> createRoot(Path rootPath, DataSize rootTotalDataSize)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");
        PreCondition.assertNotNull(rootTotalDataSize, "rootTotalDataSize");
        PreCondition.assertGreaterThan(rootTotalDataSize, DataSize.zero, "rootTotalDataSize");

        return this.createRootInner(rootPath, rootTotalDataSize);
    }

    private Result<Root> createRootInner(Path rootPath, DataSize rootTotalDataSize)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        Result<Root> result;
        rootPath = rootPath.getRoot().await();
        if (this.getInMemoryRoot(rootPath).catchError(RootNotFoundException.class).await() != null)
        {
            result = Result.error(new RootAlreadyExistsException(rootPath));
        }
        else
        {
            roots.add(new InMemoryRoot(rootPath.getRoot().await().toString(), clock, rootTotalDataSize));
            result = getRoot(rootPath);
        }
        return result;
    }
}