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
    public AsyncFunction<Result<Iterable<Root>>> getRootsAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return Async.<Iterable<Root>>success(currentAsyncRunner, Array.fromValues(Array.fromValues(roots.map(new Function1<InMemoryRoot, Root>()
        {
            @Override
            public Root run(InMemoryRoot inMemoryRoot)
            {
                return new Root(InMemoryFileSystem.this, inMemoryRoot.getPath());
            }
        }))));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Iterable<FileSystemEntry>>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final InMemoryFolder folder = getInMemoryFolder(rootedFolderPath);
            if (folder == null)
            {
                result = Async.error(currentAsyncRunner, new FolderNotFoundException(rootedFolderPath));
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

                result = Async.<Iterable<FileSystemEntry>>success(currentAsyncRunner, entries);
            }
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Boolean>>()
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

        AsyncFunction<Result<Folder>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            Throwable resultError = null;
            if (!createInMemoryFolder(rootedFolderPath))
            {
                resultError = new FolderAlreadyExistsException(rootedFolderPath);
            }
            Folder resultFolder = getFolder(rootedFolderPath).getValue();
            result = Async.done(currentAsyncRunner, resultFolder, resultError);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final InMemoryFolder parentFolder = getInMemoryFolder(rootedFolderPath.getParent());
            if (parentFolder != null && parentFolder.deleteFolder(rootedFolderPath.getSegments().last()))
            {
                result = Async.success(currentAsyncRunner, true);
            }
            else
            {
                result = Async.done(currentAsyncRunner, false, new FolderNotFoundException(rootedFolderPath));
            }
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        AsyncFunction<Result<Boolean>> result;
        if (rootedFilePath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFilePath cannot be null."));
        }
        else if (!rootedFilePath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFilePath must be rooted."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Boolean>>()
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
    public AsyncFunction<Result<File>> createFileAsync(final Path filePath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<File>>>()
        {
            @Override
            public AsyncFunction<Result<File>> run(AsyncRunner asyncRunner)
            {
                return rootExists(filePath)
                    .then(new Function1<Result<Boolean>, Result<File>>()
                    {
                        @Override
                        public Result<File> run(Result<Boolean> rootExistsResult)
                        {
                            Result<File> createFileResult;

                            if (rootExistsResult.hasError())
                            {
                                createFileResult = Result.error(rootExistsResult.getError());
                            }
                            else if (!rootExistsResult.getValue())
                            {
                                createFileResult = Result.error(new RootNotFoundException(filePath.getRoot()));
                            }
                            else
                            {
                                final Path parentFolderPath = filePath.getParent();
                                createInMemoryFolder(parentFolderPath);
                                final InMemoryFolder parentFolder = getInMemoryFolder(parentFolderPath);
                                if (!parentFolder.createFile(filePath.getSegments().last()))
                                {
                                    createFileResult = Result.error(new FileAlreadyExistsException(filePath));
                                }
                                else
                                {
                                    createFileResult = getFile(filePath);
                                }
                            }
                            return createFileResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFile(final Path filePath)
    {
        return async(this, new Function1<AsyncRunner, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(AsyncRunner asyncRunner)
            {
                Result<Boolean> result;

                final InMemoryFolder parentFolder = getInMemoryFolder(filePath.getParent());
                final boolean fileDeleted = (parentFolder != null && parentFolder.deleteFile(filePath.getSegments().last()));
                if (!fileDeleted)
                {
                    result = Result.error(new FileNotFoundException(filePath));
                }
                else
                {
                    result = Result.success(true);
                }

                return result;
            }
        });
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModified(final Path filePath)
    {
        return async(this, new Function1<AsyncRunner, Result<DateTime>>()
        {
            @Override
            public Result<DateTime> run(AsyncRunner asyncRunner)
            {
                Result<DateTime> result;

                final InMemoryFile file = getInMemoryFile(filePath);
                if (file == null)
                {
                    result = Result.error(new FileNotFoundException(filePath));
                }
                else
                {
                    result = Result.success(file.getLastModified());
                }

                return result;
            }
        });
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(final Path rootedFilePath)
    {
        return async(this, new Function1<AsyncRunner, Result<ByteReadStream>>()
        {
            @Override
            public Result<ByteReadStream> run(AsyncRunner arg1)
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
        });
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(final Path rootedFilePath)
    {
        return async(this, new Function1<AsyncRunner, Result<ByteWriteStream>>()
        {
            @Override
            public Result<ByteWriteStream> run(AsyncRunner asyncRunner)
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
        Result<Root> result;

        if (rootPath == null)
        {
            result = Result.error(new IllegalArgumentException("rootPath cannot be null."));
        }
        else if (!rootPath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("rootPath is not rooted."));
        }
        else
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

    private static boolean containsInvalidCharacters(Path path)
    {
        return path != null && containsInvalidCharacters(path.toString());
    }

    private static boolean containsInvalidCharacters(String pathString)
    {
        boolean result = false;

        if (pathString != null && !pathString.isEmpty())
        {
            final int pathStringLength = pathString.length();
            for (int i = 0; i < pathStringLength; ++i)
            {
                final char currentCharacter = pathString.charAt(i);
                if (invalidCharacters.contains(currentCharacter))
                {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    private static final Array<Character> invalidCharacters = Array.fromValues(new Character[] { '@', '#', '?' });
}