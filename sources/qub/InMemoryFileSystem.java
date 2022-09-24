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
        return this.roots.first((InMemoryRoot inMemoryRoot) -> inMemoryRoot.getPath().equals(inMemoryRootPath))
            .convertError(NotFoundException.class, () -> new RootNotFoundException(inMemoryRootPath));
    }

    private Result<InMemoryFolder> getInMemoryFolder(Path inMemoryFolderPath)
    {
        return Result.create(() ->
        {
            final Path resolvedInMemoryFolderPath = inMemoryFolderPath.resolve().await();
            final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getSegments().iterate();
            final String rootPath = folderPathSegments.first().await();
            InMemoryFolder folder = this.getInMemoryRoot(rootPath).await();

            while (folderPathSegments.next())
            {
                folder = folder.getFolder(folderPathSegments.getCurrent())
                    .convertError(NotFoundException.class, () -> new FolderNotFoundException(inMemoryFolderPath))
                    .await();
            }

            return folder;
        });
    }

    private Result<InMemoryFolder> createInMemoryFolder(Path inMemoryFolderPath)
    {
        return Result.create(() ->
        {
            final Path resolvedInMemoryFolderPath = inMemoryFolderPath.resolve().await();

            final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getSegments().iterate();
            folderPathSegments.start();

            final String rootName = folderPathSegments.takeCurrent();
            InMemoryFolder result = this.getInMemoryRoot(rootName).await();

            if (!folderPathSegments.hasCurrent())
            {
                throw new FolderAlreadyExistsException(inMemoryFolderPath);
            }

            while (folderPathSegments.hasCurrent())
            {
                final String folderName = folderPathSegments.takeCurrent();
                InMemoryFolder childFolder = result.createFolder(folderName)
                    .catchError(FolderAlreadyExistsException.class)
                    .await();
                if (childFolder == null)
                {
                    if (folderPathSegments.hasCurrent())
                    {
                        // If the folderPathSegments has a current segment, then we're not at the
                        // last folder segment to create. This means that we can just get the next
                        // folder segment and continue on.
                        childFolder = result.getFolder(folderName).await();
                    }
                    else
                    {
                        // If we don't have another segment to create, then that means that the
                        // folder we intended to create already exists.
                        throw new FolderAlreadyExistsException(inMemoryFolderPath);
                    }
                }
                result = childFolder;
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    private Result<InMemoryFile> getInMemoryFile(Path filePath)
    {
        return Result.create(() ->
        {
            final Path resolvedRootedFilePath = filePath.resolve().await();
            final InMemoryFolder inMemoryFolder = this.getInMemoryFolder(resolvedRootedFilePath.getParent().await())
                .convertError(FolderNotFoundException.class, () -> new FileNotFoundException(filePath))
                .await();
            final String fileName = resolvedRootedFilePath.getSegments().last().await();
            return inMemoryFolder.getFile(fileName)
                .convertError(NotFoundException.class, () -> new FileNotFoundException(filePath))
                .await();
        });
    }

    public Result<Void> setFileCanDelete(String rootedFilePath, boolean canDelete)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.setFileCanDelete(Path.parse(rootedFilePath), canDelete);
    }

    public Result<Void> setFileCanDelete(Path rootedFilePath, boolean canDelete)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final InMemoryFile inMemoryFile = this.getInMemoryFile(rootedFilePath).await();
            inMemoryFile.setCanDelete(canDelete);
        });
    }

    public Result<Void> setFolderCanDelete(String rootedFolderPath, boolean canDelete)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.setFolderCanDelete(Path.parse(rootedFolderPath), canDelete);
    }

    public Result<Void> setFolderCanDelete(Path rootedFolderPath, boolean canDelete)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            final InMemoryFolder inMemoryFolder = this.getInMemoryFolder(rootedFolderPath).await();
            inMemoryFolder.setCanDelete(canDelete);
        });
    }

    @Override
    public Result<Iterable<Root>> getRoots()
    {
        return Result.create(() ->
        {
            return this.roots.map((InMemoryRoot inMemoryRoot) -> Root.create(this, inMemoryRoot.getPath()));
        });
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
                    Folder.create(this, rootedFolderPath.concatenateSegments(childFolder.getName()))));

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

        return this.getInMemoryFolder(rootedFolderPath)
            .then(() -> true)
            .catchError(FolderNotFoundException.class, () -> false);
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            this.createInMemoryFolder(rootedFolderPath).await();
            return this.getFolder(rootedFolderPath).await();
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
                .convertError(FolderNotFoundException.class, () -> new FolderNotFoundException(rootedFolderPath))
                .await();
            if (!parentFolder.deleteFolder(resolvedRootedFolderPath.getSegments().last().await()))
            {
                throw new FolderNotFoundException(rootedFolderPath);
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
            final Path parentFolderPath = rootedFilePath.getParent().await();
            final InMemoryFolder parentFolder = this.createInMemoryFolder(parentFolderPath)
                .catchError(FolderAlreadyExistsException.class, () -> this.getInMemoryFolder(parentFolderPath).await())
                .await();

            final String fileName = rootedFilePath.getSegments().last().await();
            parentFolder.createFile(fileName)
                .convertError(FileAlreadyExistsException.class, () -> new FileAlreadyExistsException(rootedFilePath))
                .await();

            return this.getFile(rootedFilePath).await();
        });
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final Path resolvedRootedFilePath = rootedFilePath.resolve().await();

            final Path parentFolderPath = resolvedRootedFilePath.getParent().await();
            final InMemoryFolder parentFolder = this.getInMemoryFolder(parentFolderPath).await();

            final String fileName = resolvedRootedFilePath.getName();
            parentFolder.deleteFile(fileName)
                .convertError(FileNotFoundException.class, () -> new FileNotFoundException(rootedFilePath))
                .await();
        });
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final InMemoryFile inMemoryFile = this.getInMemoryFile(rootedFilePath).await();
            return inMemoryFile.getLastModified();
        });
    }

    @Override
    public Result<DataSize> getFileContentDataSize(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final InMemoryFile inMemoryFile = this.getInMemoryFile(rootedFilePath).await();
            return inMemoryFile.getContentsDataSize();
        });
    }

    @Override
    public Result<CharacterToByteReadStream> getFileContentReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final InMemoryFile inMemoryFile = this.getInMemoryFile(rootedFilePath).await();
            return inMemoryFile.getContentsReadStream();
        });
    }

    @Override
    public Result<BufferedByteWriteStream> getFileContentsByteWriteStream(Path rootedFilePath, OpenWriteType openWriteType)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return Result.create(() ->
        {
            final Path resolvedRootedFilePath = rootedFilePath.resolve().await();
            InMemoryFile inMemoryFile = this.getInMemoryFile(resolvedRootedFilePath)
                .catchError(FileNotFoundException.class)
                .await();
            if (inMemoryFile == null)
            {
                final Path parentFolderPath = rootedFilePath.getParent().await();
                final InMemoryFolder parentFolder = this.getInMemoryFolder(parentFolderPath)
                    .catchError(FolderNotFoundException.class, () -> this.createInMemoryFolder(parentFolderPath).await())
                    .await();
                final String fileName = resolvedRootedFilePath.getSegments().last().await();
                inMemoryFile = parentFolder.createFile(fileName).await();
            }
            return inMemoryFile.getContentByteWriteStream(openWriteType);
        });
    }

    /**
     * Create and return a new {@link Root} in this {@link InMemoryFileSystem}.
     * @param rootPath The path {@link String} to the {@link Root} to create.
     */
    public Result<Root> createRoot(String rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return this.createRoot(Path.parse(rootPath));
    }

    /**
     * Create and return a new {@link Root} in this {@link InMemoryFileSystem} with the provided
     * total {@link DataSize}.
     * @param rootPath The path {@link String} to the {@link Root} to create.
     * @param rootTotalDataSize The total {@link DataSize} of the {@link Root} to create.
     */
    public Result<Root> createRoot(String rootPath, DataSize rootTotalDataSize)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");
        PreCondition.assertNotNull(rootTotalDataSize, "rootTotalDataSize");
        PreCondition.assertGreaterThan(rootTotalDataSize, DataSize.zero, "rootTotalDataSize");

        return this.createRoot(Path.parse(rootPath), rootTotalDataSize);
    }

    /**
     * Create and return a new {@link Root} in this {@link InMemoryFileSystem}.
     * @param rootPath The {@link Path} to the {@link Root} to create.
     */
    public Result<Root> createRoot(Path rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return this.createRootInner(rootPath, null);
    }

    /**
     * Create and return a new {@link Root} in this {@link InMemoryFileSystem} with the provided
     * total {@link DataSize}.
     * @param rootPath The {@link Path} to the {@link Root} to create.
     * @param rootTotalDataSize The total {@link DataSize} of the {@link Root} to create.
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

        return Result.create(() ->
        {
            final Path trimmedRootPath = rootPath.getRoot().await();
            final InMemoryRoot inMemoryRoot = this.getInMemoryRoot(trimmedRootPath)
                .catchError(RootNotFoundException.class)
                .await();
            if (inMemoryRoot != null)
            {
                throw new RootAlreadyExistsException(rootPath);
            }

            this.roots.add(InMemoryRoot.create(trimmedRootPath.toString(), this.clock, rootTotalDataSize));

            return this.getRoot(trimmedRootPath).await();
        });
    }
}