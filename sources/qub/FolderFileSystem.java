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
        PreCondition.assertNotNull(innerFileSystem, "innerFileSystem");
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        this.innerFileSystem = innerFileSystem;

        this.baseFolderPath = baseFolderPath.normalize();
    }

    public static FolderFileSystem get(Folder baseFolder)
    {
        return FolderFileSystem.get(baseFolder.getFileSystem(), baseFolder.getPath());
    }

    public static FolderFileSystem get(FileSystem innerFileSystem, String baseFolderPath)
    {
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        return FolderFileSystem.get(innerFileSystem, Path.parse(baseFolderPath));
    }

    public static FolderFileSystem get(FileSystem innerFileSystem, Path baseFolderPath)
    {
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        return new FolderFileSystem(innerFileSystem, baseFolderPath);
    }

    public Result<?> create()
    {
        return this.innerFileSystem.createFolder(this.baseFolderPath);
    }

    public Result<Void> delete()
    {
        return this.innerFileSystem.deleteFolder(this.baseFolderPath);
    }

    public Result<Boolean> exists()
    {
        return this.innerFileSystem.folderExists(this.baseFolderPath);
    }

    public Path getBaseFolderPath()
    {
        return this.baseFolderPath;
    }

    private Result<Path> getInnerPath(Path outerPath)
    {
        FileSystem.validateRootedFolderPath(outerPath, "outerPath");

        return Result.create(() ->
        {
            Path result;
            final Path resolvedOuterPath = outerPath.resolve().await();
            if (resolvedOuterPath.equals("/"))
            {
                result = baseFolderPath;
            }
            else
            {
                final Path outerRootPath = resolvedOuterPath.getRoot().await();
                if (!outerRootPath.equals("/"))
                {
                    throw new RootNotFoundException(outerRootPath);
                }
                else
                {
                    final Path relativeOuterPath = resolvedOuterPath.withoutRoot().await();
                    result = baseFolderPath.resolve(relativeOuterPath).await();
                }
            }
            return result;
        });
    }

    private Path getOuterPath(Path innerPath)
    {
        String outerPathString = innerPath.toString().substring(this.baseFolderPath.toString().length());
        if (outerPathString.isEmpty())
        {
            outerPathString = "/";
        }
        return Path.parse(outerPathString);
    }

    @Override
    public Iterator<Root> iterateRoots()
    {
        return Iterator.create(Root.create(this, Path.parse("/")));
    }

    @Override
    public Result<DataSize> getRootTotalDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            if (!rootPath.equals(Path.parse("/")))
            {
                throw new RootNotFoundException(rootPath);
            }

            final Path baseFolderRootPath = this.baseFolderPath.getRoot().await();
            return this.innerFileSystem.getRootTotalDataSize(baseFolderRootPath).await();
        });
    }

    @Override
    public Result<DataSize> getRootUnusedDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            if (!rootPath.equals(Path.parse("/")))
            {
                throw new RootNotFoundException(rootPath);
            }

            final Path baseFolderRootPath = this.baseFolderPath.getRoot().await();
            return this.innerFileSystem.getRootUnusedDataSize(baseFolderRootPath).await();
        });
    }

    @Override
    public Iterator<FileSystemEntry> iterateEntries(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        final Path innerRootedFolderPath = this.getInnerPath(rootedFolderPath).await();
        return this.innerFileSystem.iterateEntries(innerRootedFolderPath)
            .convertError(FolderNotFoundException.class, () -> new FolderNotFoundException(rootedFolderPath))
            .map((FileSystemEntry innerEntry) ->
            {
                final Path outerEntryPath = this.getOuterPath(innerEntry.getPath());
                return innerEntry instanceof File
                    ? new File(this, outerEntryPath)
                    : Folder.create(this, outerEntryPath);
            });
    }

    @Override
    public Iterator<FileSystemEntry> iterateEntries(Path rootedFolderPath, Traversal<Folder,FileSystemEntry> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        final Path innerRootedFolderPath = this.getInnerPath(rootedFolderPath).await();
        return this.innerFileSystem.iterateEntries(innerRootedFolderPath, traversal)
            .convertError(FolderNotFoundException.class, (FolderNotFoundException error) ->
            {
                return new FolderNotFoundException(this.getOuterPath(error.getFolderPath()));
            })
            .map((FileSystemEntry innerEntry) ->
            {
                final Path outerEntryPath = this.getOuterPath(innerEntry.getPath());
                return innerEntry instanceof File
                    ? new File(this, outerEntryPath)
                    : Folder.create(this, outerEntryPath);
            });
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.getInnerPath(rootedFolderPath)
            .then((Path innerPath) -> innerFileSystem.folderExists(innerPath).await());
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.getInnerPath(rootedFolderPath)
            .then((Path innerPath) -> innerFileSystem.createFolder(innerPath).await())
            .then((Folder createdInnerFolder) ->
            {
                return getFolder(getOuterPath(createdInnerFolder.getPath())).await();
            })
            .convertError(FolderAlreadyExistsException.class, () -> new FolderAlreadyExistsException(rootedFolderPath));
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.getInnerPath(rootedFolderPath)
            .then((Path innerFolderPath) ->
            {
                if (innerFolderPath.equals(baseFolderPath))
                {
                    throw new IllegalArgumentException("Cannot delete a root folder (" + rootedFolderPath.resolve().await() + ").");
                }
                return innerFileSystem.deleteFolder(innerFolderPath)
                    .convertError(FolderNotFoundException.class, () -> new FolderNotFoundException(rootedFolderPath))
                    .await();
            });
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getInnerPath(rootedFilePath)
            .then((Path innerPath) -> innerFileSystem.fileExists(innerPath).await());
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getInnerPath(rootedFilePath)
            .then((Path innerFilePath) ->
            {
                return innerFileSystem.createFile(innerFilePath)
                    .then((File createdFile) -> new File(this, getOuterPath(createdFile.getPath())))
                    .convertError(FileAlreadyExistsException.class, () -> new FileAlreadyExistsException(rootedFilePath))
                    .await();
            });
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getInnerPath(rootedFilePath)
            .then((Path innerFilePath) ->
            {
                return innerFileSystem.deleteFile(innerFilePath)
                    .convertError(FileNotFoundException.class, () -> new FileNotFoundException(rootedFilePath))
                    .await();
            });
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getInnerPath(rootedFilePath)
            .then((Path innerFilePath) ->
            {
                return innerFileSystem.getFileLastModified(innerFilePath)
                    .convertError(FileNotFoundException.class, () -> new FileNotFoundException(rootedFilePath))
                    .await();
            });
    }

    @Override
    public Result<DataSize> getFileContentDataSize(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final Path innerFilePath = this.getInnerPath(rootedFilePath).await();
            return this.innerFileSystem.getFileContentDataSize(innerFilePath)
                .convertError(FileNotFoundException.class, () -> new FileNotFoundException(rootedFilePath))
                .await();
        });
    }

    @Override
    public Result<CharacterToByteReadStream> getFileContentReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final Path innerPath = this.getInnerPath(rootedFilePath).await();
            return this.innerFileSystem.getFileContentReadStream(innerPath)
                .convertError(FileNotFoundException.class, () -> new FileNotFoundException(rootedFilePath))
                .await();
        });
    }

    @Override
    public Result<BufferedByteWriteStream> getFileContentsByteWriteStream(Path rootedFilePath, OpenWriteType openWriteType)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return Result.create(() ->
        {
            final Path innerPath = this.getInnerPath(rootedFilePath).await();
            return this.innerFileSystem.getFileContentsByteWriteStream(innerPath, openWriteType).await();
        });
    }
}
