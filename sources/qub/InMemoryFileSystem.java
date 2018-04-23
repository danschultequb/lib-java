package qub;

/**
 * A FileSystem implementation that is completely stored in the memory of the running application.
 */
public class InMemoryFileSystem extends FileSystemBase
{
    private final List<InMemoryRoot> roots;
    private AsyncRunner asyncRunner;

    /**
     * Create a new InMemoryFileSystem.
     */
    public InMemoryFileSystem(AsyncRunner asyncRunner)
    {
        roots = new ArrayList<>();
        this.asyncRunner = asyncRunner;
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

    private InMemoryFolder getInMemoryFolder(Path inMemoryFolderPath)
    {
        final Iterator<String> folderPathSegments = inMemoryFolderPath.getSegments().iterate();
        InMemoryFolder folder = getInMemoryRoot(folderPathSegments.first());
        while (folder != null && folderPathSegments.next())
        {
            folder = folder.getFolder(folderPathSegments.getCurrent());
        }

        return folder;
    }

    private boolean createInMemoryFolder(Path inMemoryFolderPath)
    {
        boolean result = false;

        final Iterator<String> folderPathSegments = inMemoryFolderPath.getSegments().iterate();
        final Value<InMemoryFolder> folder = new Value<InMemoryFolder>(getInMemoryRoot(folderPathSegments.first()));
        while (folderPathSegments.next())
        {
            final String folderName = folderPathSegments.getCurrent();
            result = folder.get().createFolder(folderName, folder);
        }

        return result;
    }

    private InMemoryFile getInMemoryFile(Path filePath)
    {
        final InMemoryFolder parentFolder = getInMemoryFolder(filePath.getParent());
        return parentFolder == null ? null : parentFolder.getFile(filePath.getSegments().last());
    }

    public boolean setFileCanDelete(String filePathString, boolean canDelete)
    {
        return setFileCanDelete(Path.parse(filePathString), canDelete);
    }

    public boolean setFileCanDelete(Path filePath, boolean canDelete)
    {
        boolean result = false;

        final InMemoryFile file = getInMemoryFile(filePath);
        if (file != null)
        {
            file.setCanDelete(canDelete);
            result = true;
        }

        return result;
    }

    public boolean setFolderCanDelete(String folderPathString, boolean canDelete)
    {
        return setFolderCanDelete(Path.parse(folderPathString), canDelete);
    }

    public boolean setFolderCanDelete(Path folderPath, boolean canDelete)
    {
        boolean result = false;

        final InMemoryFolder folder = getInMemoryFolder(folderPath);
        if (folder != null)
        {
            folder.setCanDelete(canDelete);
            result = true;
        }

        return result;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public Result<Iterable<Root>> getRoots()
    {
        return Result.<Iterable<Root>>success(roots.map(new Function1<InMemoryRoot, Root>()
            {
                @Override
                public Root run(InMemoryRoot inMemoryRoot)
                {
                    return new Root(InMemoryFileSystem.this, inMemoryRoot.getPath());
                }
            }));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        Result<Iterable<FileSystemEntry>> result = validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final InMemoryFolder folder = getInMemoryFolder(rootedFolderPath);
            if (folder == null)
            {
                result = Result.error(new FolderNotFoundException(rootedFolderPath));
            }
            else
            {
                final List<FileSystemEntry> entries = new ArrayList<>();

                for (final InMemoryFolder inMemoryFolder : folder.getFolders())
                {
                    final Path childFolderPath = rootedFolderPath.concatenateSegment(inMemoryFolder.getName());
                    entries.add(new Folder(InMemoryFileSystem.this, childFolderPath));
                }

                for (final InMemoryFile inMemoryFile : folder.getFiles())
                {
                    final Path childFilePath = rootedFolderPath.concatenateSegment(inMemoryFile.getName());
                    entries.add(new File(InMemoryFileSystem.this, childFilePath));
                }

                result = Result.<Iterable<FileSystemEntry>>success(entries);
            }
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        return Result.success(getInMemoryFolder(rootedFolderPath) != null);
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Folder>> result = validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Folder>>()
                {
                    @Override
                    public Result<Folder> run()
                    {
                        Throwable resultError = null;
                        Folder resultFolder = null;
                        if (getInMemoryRoot(rootedFolderPath.getRoot()) == null)
                        {
                            resultError = new RootNotFoundException(rootedFolderPath.getRoot());
                        }
                        else
                        {
                            if (!createInMemoryFolder(rootedFolderPath))
                            {
                                resultError = new FolderAlreadyExistsException(rootedFolderPath);
                            }

                            resultFolder = getFolder(rootedFolderPath).getValue();
                        }

                        return Result.done(resultFolder, resultError);
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        Result<Boolean> result;
                        final InMemoryFolder parentFolder = getInMemoryFolder(rootedFolderPath.getParent());
                        if (parentFolder != null && parentFolder.deleteFolder(rootedFolderPath.getSegments().last()))
                        {
                            result = Result.successTrue();
                        }
                        else
                        {
                            result = Result.done(false, new FolderNotFoundException(rootedFolderPath));
                        }
                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        AsyncFunction<Result<Boolean>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        return Result.success(getInMemoryFile(rootedFilePath) != null);
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<File>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<File>>()
                {
                    @Override
                    public Result<File> run()
                    {
                        File file = null;
                        Throwable error = null;
                        if (getInMemoryRoot(rootedFilePath.getRoot()) == null)
                        {
                            error = new RootNotFoundException(rootedFilePath.getRoot());
                        }
                        else
                        {
                            final Path parentFolderPath = rootedFilePath.getParent();
                            createInMemoryFolder(parentFolderPath);

                            final InMemoryFolder parentFolder = getInMemoryFolder(parentFolderPath);

                            if (!parentFolder.createFile(rootedFilePath.getSegments().last()))
                            {
                                error = new FileAlreadyExistsException(rootedFilePath);
                            }
                            file = getFile(rootedFilePath).getValue();
                        }

                        return Result.done(file, error);
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        Result<Boolean> result;

                        final InMemoryFolder parentFolder = getInMemoryFolder(rootedFilePath.getParent());
                        final boolean fileDeleted = (parentFolder != null && parentFolder.deleteFile(rootedFilePath.getSegments().last()));
                        if (!fileDeleted)
                        {
                            result = Result.done(false, new FileNotFoundException(rootedFilePath));
                        }
                        else
                        {
                            result = Result.success(true);
                        }

                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<DateTime>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<DateTime>>()
                {
                    @Override
                    public Result<DateTime> run()
                    {
                        Result<DateTime> result;

                        final InMemoryFile file = getInMemoryFile(rootedFilePath);
                        if (file == null)
                        {
                            result = Result.error(new FileNotFoundException(rootedFilePath));
                        }
                        else
                        {
                            result = Result.success(file.getLastModified());
                        }

                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<ByteReadStream>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<ByteReadStream>>()
                {
                    @Override
                    public Result<ByteReadStream> run()
                    {
                        Result<ByteReadStream> result;

                        final InMemoryFile file = getInMemoryFile(rootedFilePath);
                        if (file == null)
                        {
                            result = Result.error(new FileNotFoundException(rootedFilePath));
                        }
                        else
                        {
                            result = Result.success(file.getContentByteReadStream());
                        }

                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<ByteWriteStream>> result = validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<ByteWriteStream>>()
                {
                    @Override
                    public Result<ByteWriteStream> run()
                    {
                        InMemoryFile file = getInMemoryFile(rootedFilePath);
                        if (file == null)
                        {
                            final Path parentFolderPath = rootedFilePath.getParent();
                            InMemoryFolder parentFolder = getInMemoryFolder(parentFolderPath);
                            if (parentFolder == null)
                            {
                                createInMemoryFolder(parentFolderPath);
                                parentFolder = getInMemoryFolder(parentFolderPath);
                            }

                            parentFolder.createFile(rootedFilePath.getSegments().last());
                            file = getInMemoryFile(rootedFilePath);
                        }

                        return Result.success(file.getContentByteWriteStream());
                    }
                })
                .thenOn(currentAsyncRunner);
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
        Result<Root> result = validateRootPath(rootPath);
        if (result == null)
        {
            rootPath = rootPath.getRoot();
            if (getInMemoryRoot(rootPath) != null)
            {
                result = Result.error(new RootAlreadyExistsException(rootPath));
            }
            else
            {
                roots.add(new InMemoryRoot(rootPath.getSegments().first()));
                result = getRoot(rootPath);
            }
        }
        return result;
    }
}