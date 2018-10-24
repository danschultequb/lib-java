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

    private Result<InMemoryFolder> getInMemoryFolder(Path inMemoryFolderPath)
    {
        Result<InMemoryFolder> result;

        final Result<Path> resolvedInMemoryFolderPath = inMemoryFolderPath.resolve();
        if (resolvedInMemoryFolderPath.hasError())
        {
            result = Result.error(resolvedInMemoryFolderPath.getError());
        }
        else
        {
            final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getValue().getSegments().iterate();
            InMemoryFolder folder = getInMemoryRoot(folderPathSegments.first());
            while (folder != null && folderPathSegments.next())
            {
                folder = folder.getFolder(folderPathSegments.getCurrent());
            }
            result = folder != null ? Result.success(folder) : Result.error(new FolderNotFoundException(inMemoryFolderPath));
        }

        return result;
    }

    private Result<Boolean> createInMemoryFolder(Path inMemoryFolderPath)
    {
        Result<Boolean> result;

        final Result<Path> resolvedInMemoryFolderPath = inMemoryFolderPath.resolve();
        if (resolvedInMemoryFolderPath.hasError())
        {
            result = Result.error(resolvedInMemoryFolderPath.getError());
        }
        else
        {
            boolean createdFolder = false;
            final Iterator<String> folderPathSegments = resolvedInMemoryFolderPath.getValue().getSegments().iterate();
            final Value<InMemoryFolder> folder = new Value<InMemoryFolder>(getInMemoryRoot(folderPathSegments.first()));
            while (folderPathSegments.next())
            {
                final String folderName = folderPathSegments.getCurrent();
                createdFolder = folder.get().createFolder(folderName, folder);
            }
            result = createdFolder ? Result.successTrue() : Result.successFalse();
        }

        return result;
    }

    private Result<InMemoryFile> getInMemoryFile(Path filePath)
    {
        Result<InMemoryFile> result;

        final Result<InMemoryFolder> inMemoryFolder = getInMemoryFolder(filePath.getParent());
        if (inMemoryFolder.hasError())
        {
            if (inMemoryFolder.getError() instanceof FolderNotFoundException)
            {
                result = Result.error(new FileNotFoundException(filePath));
            }
            else
            {
                result = Result.error(inMemoryFolder.getError());
            }
        }
        else
        {
            final String fileName = filePath.getSegments().last();
            final InMemoryFile inMemoryFile = inMemoryFolder.getValue().getFile(fileName);
            result = inMemoryFile != null ? Result.success(inMemoryFile) : Result.<InMemoryFile>error(new FileNotFoundException(filePath.resolve().getValue()));
        }

        return result;
    }

    public Result<Boolean> setFileCanDelete(String filePathString, boolean canDelete)
    {
        return setFileCanDelete(Path.parse(filePathString), canDelete);
    }

    public Result<Boolean> setFileCanDelete(Path filePath, boolean canDelete)
    {
        Result<Boolean> result;

        final Result<InMemoryFile> file = getInMemoryFile(filePath);
        if (file.hasError())
        {
            result = Result.error(file.getError());
        }
        else
        {
            file.getValue().setCanDelete(canDelete);
            result = Result.successTrue();
        }

        return result;
    }

    public Result<Boolean> setFolderCanDelete(String folderPathString, boolean canDelete)
    {
        return setFolderCanDelete(Path.parse(folderPathString), canDelete);
    }

    public Result<Boolean> setFolderCanDelete(Path folderPath, boolean canDelete)
    {
        Result<Boolean> result;

        final Result<InMemoryFolder> folder = getInMemoryFolder(folderPath);
        if (folder.hasError())
        {
            result = Result.error(folder.getError());
        }
        else
        {
            folder.getValue().setCanDelete(canDelete);
            result = Result.successTrue();
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
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Iterable<FileSystemEntry>> result = validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final Result<InMemoryFolder> folder = getInMemoryFolder(rootedFolderPath);
            if (folder.hasError())
            {
                result = Result.error(folder.getError());
            }
            else
            {
                final List<FileSystemEntry> entries = new ArrayList<>();

                for (final InMemoryFolder inMemoryFolder : folder.getValue().getFolders())
                {
                    final Path childFolderPath = rootedFolderPath.concatenateSegment(inMemoryFolder.getName());
                    entries.add(new Folder(InMemoryFileSystem.this, childFolderPath));
                }

                for (final InMemoryFile inMemoryFile : folder.getValue().getFiles())
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
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Boolean> result = validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
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
        }
        return result;
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Folder> result = validateRootedFolderPath(rootedFolderPath);
        if (result == null)
        {
            final Result<Path> resolvedRootedFolderPath = rootedFolderPath.resolve();
            if (resolvedRootedFolderPath.hasError())
            {
                result = Result.error(resolvedRootedFolderPath.getError());
            }
            else
            {
                Throwable resultError = null;
                Folder resultFolder = null;
                if (getInMemoryRoot(rootedFolderPath.getRoot()) == null)
                {
                    resultError = new RootNotFoundException(rootedFolderPath.getRoot());
                }
                else
                {
                    final Result<Boolean> createdFolder = createInMemoryFolder(resolvedRootedFolderPath.getValue());
                    if (createdFolder.hasError())
                    {
                        resultError = createdFolder.getError();
                    }
                    else if (!createdFolder.getValue())
                    {
                        resultError = new FolderAlreadyExistsException(resolvedRootedFolderPath.getValue());
                    }

                    resultFolder = getFolder(resolvedRootedFolderPath.getValue()).getValue();
                }

                result = Result.done(resultFolder, resultError);
            }
        }
        return result;
    }

    @Override
    public Result<Boolean> deleteFolder(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Boolean> result;
        final Result<Path> resolvedRootedFolderPath = rootedFolderPath.resolve();
        if (resolvedRootedFolderPath.hasError())
        {
            result = Result.error(resolvedRootedFolderPath.getError());
        }
        else
        {
            final Path parentFolderPath = resolvedRootedFolderPath.getValue().getParent();
            if (parentFolderPath == null)
            {
                result = Result.done(false, new IllegalArgumentException("Cannot delete a root folder (" + resolvedRootedFolderPath.getValue() + ")."));
            }
            else
            {
                final Result<InMemoryFolder> parentFolder = getInMemoryFolder(parentFolderPath);
                if (parentFolder.hasError())
                {
                    if (parentFolder.getError() instanceof FolderNotFoundException)
                    {
                        result = Result.done(false, new FolderNotFoundException(resolvedRootedFolderPath.getValue()));
                    }
                    else
                    {
                        result = Result.error(parentFolder.getError());
                    }
                }
                else if (parentFolder.getValue().deleteFolder(resolvedRootedFolderPath.getValue().getSegments().last()))
                {
                    result = Result.successTrue();
                }
                else
                {
                    result = Result.done(false, new FolderNotFoundException(resolvedRootedFolderPath.getValue()));
                }
            }
        }

        return result;
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertTrue(rootedFilePath.isRooted(), "rootedFilePath.isRooted()");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFilePath), "containsInvalidCharacters(rootedFilePath)");

        Result<Boolean> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
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
        }
        return result;
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertTrue(rootedFilePath.isRooted(), "rootedFilePath.isRooted()");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFilePath), "containsInvalidCharacters(rootedFilePath)");

        Result<File> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
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

                final Result<InMemoryFolder> parentFolder = getInMemoryFolder(parentFolderPath);
                if (parentFolder.hasError())
                {
                    error = parentFolder.getError();
                }
                else if (!parentFolder.getValue().createFile(rootedFilePath.getSegments().last()))
                {
                    error = new FileAlreadyExistsException(rootedFilePath);
                }
                file = getFile(rootedFilePath).getValue();
            }
            result = Result.done(file, error);
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFile(Path rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertTrue(rootedFilePath.isRooted(), "rootedFilePath.isRooted()");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFilePath), "containsInvalidCharacters(rootedFilePath)");

        Result<Boolean> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
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
                    result = Result.done(false, new FileNotFoundException(resolvedRootedFilePath.getValue()));
                }
                else
                {
                    result = Result.successTrue();
                }
            }
        }
        return result;
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertTrue(rootedFilePath.isRooted(), "rootedFilePath.isRooted()");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFilePath), "containsInvalidCharacters(rootedFilePath)");

        Result<DateTime> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
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
        }

        return result;
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        Result<ByteReadStream> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Result<InMemoryFile> file = getInMemoryFile(rootedFilePath);
            if (file.hasError())
            {
                result = Result.error(file.getError());
            }
            else
            {
                result = Result.success(file.getValue().getContentByteReadStream());
            }
        }

        return result;
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        Result<ByteWriteStream> result = validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
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