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
        return async(this, new Function1<AsyncRunner, Result<Iterable<Root>>>()
        {
            @Override
            public Result<Iterable<Root>> run(AsyncRunner asyncRunner)
            {
                return Result.<Iterable<Root>>success(Array.fromValues(roots.map(new Function1<InMemoryRoot, Root>()
                {
                    @Override
                    public Root run(InMemoryRoot inMemoryRoot)
                    {
                        return new Root(InMemoryFileSystem.this, inMemoryRoot.getPath());
                    }
                })));
            }
        });
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(final Path folderPath)
    {
        AsyncFunction<Result<Iterable<FileSystemEntry>>> result;

        if (folderPath == null)
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("folderPath cannot be null."));
        }
        else if (!folderPath.isRooted())
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("folderPath must be rooted."));
        }
        else
        {
            result = async(this, new Function1<AsyncRunner, Result<Iterable<FileSystemEntry>>>()
            {
                @Override
                public Result<Iterable<FileSystemEntry>> run(AsyncRunner asyncRunner)
                {
                    List<FileSystemEntry> entries = new ArrayList<>();

                    final InMemoryFolder folder = getInMemoryFolder(folderPath);
                    if (folder != null)
                    {
                        for (final InMemoryFolder inMemoryFolder : folder.getFolders())
                        {
                            final Path childFolderPath = folderPath.concatenateSegment(inMemoryFolder.getName());
                            entries.add(new Folder(InMemoryFileSystem.this, childFolderPath));
                        }

                        for (final InMemoryFile inMemoryFile : folder.getFiles())
                        {
                            final Path childFilePath = folderPath.concatenateSegment(inMemoryFile.getName());
                            entries.add(new File(InMemoryFileSystem.this, childFilePath));
                        }
                    }

                    return Result.<Iterable<FileSystemEntry>>success(entries);
                }
            });
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExists(final Path folderPath)
    {
        return async(this, new Function1<AsyncRunner, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(AsyncRunner asyncRunner)
            {
                return Result.success(getInMemoryFolder(folderPath) != null);
            }
        });
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(final Path folderPath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<Folder>>>()
        {
            @Override
            public AsyncFunction<Result<Folder>> run(AsyncRunner asyncRunner)
            {
                return rootExists(folderPath)
                    .then(new Function1<Result<Boolean>, Result<Folder>>()
                    {
                        @Override
                        public Result<Folder> run(Result<Boolean> rootExistsResult)
                        {
                            Result<Folder> createFolderResult;
                            if (rootExistsResult.hasError())
                            {
                                createFolderResult = Result.error(rootExistsResult.getError());
                            }
                            else if (!rootExistsResult.getValue())
                            {
                                createFolderResult = Result.error(new RootNotFoundException(folderPath.getRoot()));
                            }
                            else
                            {
                                if (!createInMemoryFolder(folderPath))
                                {
                                    createFolderResult = Result.error(new FolderAlreadyExistsException(folderPath));
                                }
                                else
                                {
                                    createFolderResult = getFolder(folderPath);
                                }
                            }
                            return createFolderResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(final Path folderPath)
    {
        return async(this, new Function1<AsyncRunner, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(AsyncRunner asyncRunner)
            {
                Result<Boolean> deleteFolderResult;

                final InMemoryFolder parentFolder = getInMemoryFolder(folderPath.getParent());

                if (parentFolder.deleteFolder(folderPath.getSegments().last()))
                {
                    deleteFolderResult = Result.success(true);
                }
                else
                {
                    deleteFolderResult = Result.error(new FolderNotFoundException(folderPath));
                }

                return deleteFolderResult;
            }
        });
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExists(final Path filePath)
    {
        return async(this, new Function1<AsyncRunner, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(AsyncRunner asyncRunner)
            {
                return Result.success(getInMemoryFile(filePath) != null);
            }
        });
    }

    @Override
    public AsyncFunction<Result<File>> createFile(final Path filePath)
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