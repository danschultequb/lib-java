package qub;

public abstract class FileSystemBase implements FileSystem
{
    @Override
    public Result<Boolean> rootExists(String rootPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootPath, "rootPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootPath), "containsInvalidCharacters(rootPath)");

        return rootExists(Path.parse(rootPath));
    }

    @Override
    public Result<Boolean> rootExists(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");
        PreCondition.assertTrue(rootPath.isRooted(), "rootPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootPath), "containsInvalidCharacters(rootPath)");

        Result<Boolean> rootExistsResult;
        final Result<Iterable<Root>> roots = getRoots();
        if (roots.hasError())
        {
            rootExistsResult = Result.error(roots.getError());
        }
        else
        {
            final Path onlyRootPath = rootPath.getRoot();
            rootExistsResult = Result.success(roots.getValue().contains((Root root) -> root.getPath().equals(onlyRootPath)));
        }
        return rootExistsResult;
    }

    @Override
    public AsyncFunction<Result<Boolean>> rootExistsAsync(String rootPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootPath, "rootPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootPath), "containsInvalidCharacters(rootPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> rootExists(rootPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> rootExistsAsync(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");
        PreCondition.assertTrue(rootPath.isRooted(), "rootPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootPath), "containsInvalidCharacters(rootPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> rootExists(rootPath));
    }

    @Override
    public Result<Root> getRoot(String rootPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootPath, "rootPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootPath), "containsInvalidCharacters(rootPath)");

        return getRoot(Path.parse(rootPath));
    }

    @Override
    public Result<Root> getRoot(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");
        PreCondition.assertTrue(rootPath.isRooted(), "rootPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootPath), "containsInvalidCharacters(rootPath)");

        return Result.success(new Root(this, rootPath.getRoot()));
    }

    @Override
    public abstract Result<Iterable<Root>> getRoots();

    @Override
    public AsyncFunction<Result<Iterable<Root>>> getRootsAsync()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::getRoots);
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFilesAndFolders(Path.parse(rootedFolderPath));
    }

    @Override
    public abstract Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath);

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFolders(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFolders(rootedFolderPath));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFilesAndFoldersRecursively(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Iterable<FileSystemEntry>> result;
        final Result<Path> resolvedRootedFolderPath = rootedFolderPath.resolve();
        if (resolvedRootedFolderPath.hasError())
        {
            result = Result.error(resolvedRootedFolderPath.getError());
        }
        else
        {
            final List<Throwable> resultErrors = new ArrayList<>();
            List<FileSystemEntry> resultEntries = null;

            final Folder folder = getFolder(resolvedRootedFolderPath.getValue()).getValue();
            final Result<Iterable<FileSystemEntry>> folderEntriesResult = folder.getFilesAndFolders();

            boolean folderExists = true;
            if (folderEntriesResult.hasError())
            {
                final Throwable error = folderEntriesResult.getError();
                folderExists = !(error instanceof FolderNotFoundException);
                resultErrors.add(error);
            }

            if (folderExists)
            {
                resultEntries = new ArrayList<>();

                final Queue<Folder> foldersToVisit = new ArrayListQueue<>();
                foldersToVisit.enqueue(folder);

                while (foldersToVisit.any())
                {
                    final Folder currentFolder = foldersToVisit.dequeue();
                    final Result<Iterable<FileSystemEntry>> getFilesAndFoldersResult = currentFolder.getFilesAndFolders();
                    if (getFilesAndFoldersResult.hasError())
                    {
                        resultErrors.add(getFilesAndFoldersResult.getError());
                    }
                    else
                    {
                        final Iterable<FileSystemEntry> currentFolderEntries = getFilesAndFoldersResult.getValue();
                        for (final FileSystemEntry entry : currentFolderEntries)
                        {
                            resultEntries.add(entry);

                            if (entry instanceof Folder)
                            {
                                foldersToVisit.enqueue((Folder)entry);
                            }
                        }
                    }
                }
            }

            result = Result.done(resultEntries, ErrorIterable.from(resultErrors));
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFoldersRecursively(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFoldersRecursively(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFolders(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFolders(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFolders(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        final Result<Iterable<FileSystemEntry>> result = getFilesAndFolders(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(Folder.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFolders(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFolders(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFoldersRecursively(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFoldersRecursively(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFoldersRecursively(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        final Result<Iterable<FileSystemEntry>> result = getFilesAndFoldersRecursively(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(Folder.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFoldersRecursively(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFoldersRecursively(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFiles(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFiles(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFiles(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Iterable<FileSystemEntry>> result = getFilesAndFolders(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(File.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFilesRecursively(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFilesRecursively(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFilesRecursively(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        final Result<Iterable<FileSystemEntry>> result = getFilesAndFoldersRecursively(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(File.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public Result<Folder> getFolder(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return getFolder(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Folder> getFolder(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        Result<Folder> result;
        final Result<Path> resolvedRootedFolderPath = rootedFolderPath.resolve();
        if (resolvedRootedFolderPath.hasError())
        {
            result = Result.error(resolvedRootedFolderPath.getError());
        }
        else
        {
            result = Result.success(new Folder(this, resolvedRootedFolderPath.getValue()));
        }
        return result;
    }

    @Override
    public Result<Boolean> folderExists(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return folderExists(Path.parse(rootedFolderPath));
    }

    @Override
    public abstract Result<Boolean> folderExists(Path rootedFolderPath);

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> folderExists(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(Path rootedFolderPath)
    {
        PreCondition.assertNotNull(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertTrue(rootedFolderPath.isRooted(), "rootedFolderPath.isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> folderExists(rootedFolderPath));
    }

    @Override
    public Result<Folder> createFolder(String rootedFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, "rootedFolderPath");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(rootedFolderPath)");

        return createFolder(Path.parse(rootedFolderPath));
    }

    @Override
    public abstract Result<Folder> createFolder(Path rootedFolderPath);

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(String rootedFolderPath)
    {
        return FileSystemBase.createFolderAsync(this, rootedFolderPath);
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(Path rootedFolderPath)
    {
        return FileSystemBase.createFolderAsync(this, rootedFolderPath);
    }

    @Override
    public Result<Boolean> deleteFolder(String rootedFolderPath)
    {
        return FileSystemBase.deleteFolder(this, rootedFolderPath);
    }

    @Override
    public abstract Result<Boolean> deleteFolder(Path rootedFolderPath);

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(String rootedFolderPath)
    {
        return FileSystemBase.deleteFolderAsync(this, rootedFolderPath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(Path rootedFolderPath)
    {
        return FileSystemBase.deleteFolderAsync(this, rootedFolderPath);
    }

    @Override
    public Result<File> getFile(String rootedFilePath)
    {
        return FileSystemBase.getFile(this, rootedFilePath);
    }

    @Override
    public Result<File> getFile(Path rootedFilePath)
    {
        return FileSystemBase.getFile(this, rootedFilePath);
    }

    @Override
    public Result<Boolean> fileExists(String rootedFilePath)
    {
        return FileSystemBase.fileExists(this, rootedFilePath);
    }

    @Override
    public abstract Result<Boolean> fileExists(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(String rootedFilePath)
    {
        return FileSystemBase.fileExistsAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(Path rootedFilePath)
    {
        return FileSystemBase.fileExistsAsync(this, rootedFilePath);
    }

    @Override
    public Result<File> createFile(String rootedFilePath)
    {
        return FileSystemBase.createFile(this, rootedFilePath);
    }

    @Override
    public abstract Result<File> createFile(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<File>> createFileAsync(String rootedFilePath)
    {
        return FileSystemBase.createFileAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(Path rootedFilePath)
    {
        return FileSystemBase.createFileAsync(this, rootedFilePath);
    }

    @Override
    public Result<Boolean> deleteFile(String rootedFilePath)
    {
        return FileSystemBase.deleteFile(this, rootedFilePath);
    }

    @Override
    public abstract Result<Boolean> deleteFile(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(String rootedFilePath)
    {
        return FileSystemBase.deleteFileAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(Path rootedFilePath)
    {
        return FileSystemBase.deleteFileAsync(this, rootedFilePath);
    }

    @Override
    public Result<DateTime> getFileLastModified(String rootedFilePath)
    {
        return FileSystemBase.getFileLastModified(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(String rootedFilePath)
    {
        return FileSystemBase.getFileLastModifiedAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(Path rootedFilePath)
    {
        return FileSystemBase.getFileLastModifiedAsync(this, rootedFilePath);
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteReadStream(this, rootedFilePath);
    }

    @Override
    public abstract Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteReadStreamAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentByteReadStreamAsync(this, rootedFilePath);
    }

    @Override
    public Result<byte[]> getFileContent(String rootedFilePath)
    {
        return FileSystemBase.getFileContent(this, rootedFilePath);
    }

    @Override
    public Result<byte[]> getFileContent(Path rootedFilePath)
    {
        return FileSystemBase.getFileContent(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<byte[]>> getFileContentAsync(String rootedFilePath)
    {
        return FileSystemBase.getFileContentAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<byte[]>> getFileContentAsync(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentAsync(this, rootedFilePath);
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteWriteStream(this, rootedFilePath);
    }

    @Override
    public abstract Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteWriteStreamAsync(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentByteWriteStreamAsync(this, rootedFilePath);
    }

    @Override
    public Result<Boolean> setFileContent(String rootedFilePath, byte[] content)
    {
        return FileSystemBase.setFileContent(this, rootedFilePath, content);
    }

    @Override
    public Result<Boolean> setFileContent(Path rootedFilePath, byte[] content)
    {
        return FileSystemBase.setFileContent(this, rootedFilePath, content);
    }

    @Override
    public AsyncFunction<Result<Boolean>> setFileContentAsync(String rootedFilePath, byte[] content)
    {
        return FileSystemBase.setFileContentAsync(this, rootedFilePath, content);
    }

    @Override
    public AsyncFunction<Result<Boolean>> setFileContentAsync(Path rootedFilePath, byte[] content)
    {
        return FileSystemBase.setFileContentAsync(this, rootedFilePath, content);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param rootedFolderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Result<Folder>> createFolderAsync(FileSystem fileSystem, String rootedFolderPath)
    {
        return fileSystem.createFolderAsync(Path.parse(rootedFolderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param rootedFolderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Result<Folder>> createFolderAsync(final FileSystem fileSystem, final Path rootedFolderPath)
    {
        AsyncFunction<Result<Folder>> result = FileSystemBase.validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<Folder>>()
            {
                @Override
                public Result<Folder> run()
                {
                    return fileSystem.createFolder(rootedFolderPath);
                }
            });
        }
        return result;
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param rootedFolderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static Result<Boolean> deleteFolder(FileSystem fileSystem, String rootedFolderPath)
    {
        return fileSystem.deleteFolder(Path.parse(rootedFolderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param rootedFolderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Result<Boolean>> deleteFolderAsync(FileSystem fileSystem, String rootedFolderPath)
    {
        return fileSystem.deleteFolderAsync(Path.parse(rootedFolderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param rootedFolderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Result<Boolean>> deleteFolderAsync(final FileSystem fileSystem, final Path rootedFolderPath)
    {
        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    return fileSystem.deleteFolder(rootedFolderPath);
                }
            });
        }
        return result;
    }

    /**
     * Get a reference to the File at the provided rootedFolderPath.
     * @param rootedFilePath The path to the file.
     * @return A reference to the File at the provided rootedFilePath.
     */
    public static Result<File> getFile(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFile(Path.parse(rootedFilePath));
    }

    /**
     * Get a reference to the File at the provided rootedFolderPath.
     * @param rootedFilePath The path to the file.
     * @return A reference to the File at the provided rootedFilePath.
     */
    public static Result<File> getFile(FileSystem fileSystem, Path rootedFilePath)
    {
        Result<File> result = validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Result<Path> resolvedRootedFilePath = rootedFilePath.resolve();
            if (resolvedRootedFilePath.hasError())
            {
                result = Result.error(resolvedRootedFilePath.getError());
            }
            else
            {
                result = Result.success(new File(fileSystem, resolvedRootedFilePath.getValue()));
            }
        }
        return result;
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param rootedFilePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static Result<Boolean> fileExists(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.fileExists(Path.parse(rootedFilePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param rootedFilePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Result<Boolean>> fileExistsAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.fileExistsAsync(Path.parse(rootedFilePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param rootedFilePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Result<Boolean>> fileExistsAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    return fileSystem.fileExists(rootedFilePath);
                }
            });
        }
        return result;
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param rootedFilePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static Result<File> createFile(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.createFile(Path.parse(rootedFilePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param rootedFilePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Result<File>> createFileAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.createFileAsync(Path.parse(rootedFilePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param rootedFilePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Result<File>> createFileAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<File>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<File>>()
            {
                @Override
                public Result<File> run()
                {
                    return fileSystem.createFile(rootedFilePath);
                }
            });
        }
        return result;
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param rootedFilePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static Result<Boolean> deleteFile(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.deleteFile(Path.parse(rootedFilePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param rootedFilePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Result<Boolean>> deleteFileAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.deleteFileAsync(Path.parse(rootedFilePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param rootedFilePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Result<Boolean>> deleteFileAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    return fileSystem.deleteFile(rootedFilePath);
                }
            });
        }
        return result;
    }

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param rootedFilePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    public static Result<DateTime> getFileLastModified(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileLastModified(Path.parse(rootedFilePath));
    }

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param rootedFilePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    public static AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileLastModifiedAsync(Path.parse(rootedFilePath));
    }

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param rootedFilePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    public static AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<DateTime>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<DateTime>>()
            {
                @Override
                public Result<DateTime> run()
                {
                    return fileSystem.getFileLastModified(rootedFilePath);
                }
            });
        }
        return result;
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static Result<ByteReadStream> getFileContentByteReadStream(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteReadStreamAsync(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<ByteReadStream>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<ByteReadStream>>()
            {
                @Override
                public Result<ByteReadStream> run()
                {
                    return fileSystem.getFileContentByteReadStream(rootedFilePath);
                }
            });
        }
        return result;
    }

    public static Result<byte[]> getFileContent(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContent(Path.parse(rootedFilePath));
    }

    public static Result<byte[]> getFileContent(FileSystem fileSystem, Path rootedFilePath)
    {
        Result<byte[]> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Result<ByteReadStream> byteReadStreamResult = fileSystem.getFileContentByteReadStream(rootedFilePath);
            if (byteReadStreamResult.hasError())
            {
                result = Result.error(byteReadStreamResult.getError());
            }
            else
            {
                try (final ByteReadStream byteReadStream = byteReadStreamResult.getValue())
                {
                    result = byteReadStream.readAllBytes();
                }
                catch (Exception e)
                {
                    result = Result.error(e);
                }
            }
        }

        return result;
    }

    public static AsyncFunction<Result<byte[]>> getFileContentAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentAsync(Path.parse(rootedFilePath));
    }

    public static AsyncFunction<Result<byte[]>> getFileContentAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<byte[]>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<byte[]>>()
            {
                @Override
                public Result<byte[]> run()
                {
                    return fileSystem.getFileContent(rootedFilePath);
                }
            });
        }
        return result;
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static Result<ByteWriteStream> getFileContentByteWriteStream(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteWriteStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteWriteStreamAsync(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(final FileSystem fileSystem, final Path rootedFilePath)
    {
        AsyncFunction<Result<ByteWriteStream>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<ByteWriteStream>>()
            {
                @Override
                public Result<ByteWriteStream> run()
                {
                    return fileSystem.getFileContentByteWriteStream(rootedFilePath);
                }
            });
        }
        return result;
    }

    public static Result<Boolean> setFileContent(FileSystem fileSystem, String rootedFilePath, byte[] content)
    {
        return fileSystem.setFileContent(Path.parse(rootedFilePath), content);
    }

    public static Result<Boolean> setFileContent(FileSystem fileSystem, Path rootedFilePath, byte[] content)
    {
        Result<Boolean> result = FileSystemBase.validateRootedFilePath(rootedFilePath);
        if (result == null)
        {
            final Result<ByteWriteStream> byteWriteStreamResult = fileSystem.getFileContentByteWriteStream(rootedFilePath);
            if (byteWriteStreamResult.hasError())
            {
                result = Result.error(byteWriteStreamResult.getError());
            }
            else
            {
                try (final ByteWriteStream byteWriteStream = byteWriteStreamResult.getValue())
                {
                    if (content == null || content.length == 0)
                    {
                        // If we want to set the file to have no/empty contents.
                        result = Result.successTrue();
                    }
                    else
                    {
                        result = byteWriteStream.write(content);
                    }
                }
                catch (Exception e)
                {
                    result = Result.error(e);
                }
            }
        }
        return result;
    }

    public static AsyncFunction<Result<Boolean>> setFileContentAsync(FileSystem fileSystem, String rootedFilePath, byte[] content)
    {
        return fileSystem.setFileContentAsync(Path.parse(rootedFilePath), content);
    }

    public static AsyncFunction<Result<Boolean>> setFileContentAsync(final FileSystem fileSystem, final Path rootedFilePath, final byte[] content)
    {
        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = async(fileSystem, new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    return fileSystem.setFileContent(rootedFilePath, content);
                }
            });
        }
        return result;
    }

    public static <T> Result<T> validateRootPath(Path rootPath)
    {
        Result<T> result = null;

        if (rootPath == null)
        {
            result = Result.<T>error(new IllegalArgumentException("rootPath cannot be null."));
        }
        else if (!rootPath.isRooted())
        {
            result = Result.<T>error(new IllegalArgumentException("rootPath must be rooted."));
        }
        else if (containsInvalidCharacters(rootPath))
        {
            result = Result.<T>error(new IllegalArgumentException("rootPath cannot contain invalid characters " + invalidCharacters + "."));
        }

        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateRootPathAsync(Path rootPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = validateRootPath(rootPath);
        return result == null ? null : currentAsyncRunner.<T>error(result.getError());
    }

    public static <T> Result<T> validateRootedFolderPath(Path rootedFolderPath)
    {
        Result<T> result = null;

        if (rootedFolderPath == null)
        {
            result = Result.<T>error(new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Result.<T>error(new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else if (containsInvalidCharacters(rootedFolderPath))
        {
            result = Result.<T>error(new IllegalArgumentException("rootedFolderPath cannot contain invalid characters " + invalidCharacters + "."));
        }

        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateRootedFolderPathAsync(Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = validateRootedFolderPath(rootedFolderPath);
        return result == null ? null : currentAsyncRunner.<T>error(result.getError());
    }

    public static <T> Result<T> validateRootedFilePath(Path rootedFilePath)
    {
        Result<T> result = null;

        if (rootedFilePath == null)
        {
            result = Result.error(new IllegalArgumentException("rootedFilePath cannot be null."));
        }
        else if (!rootedFilePath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("rootedFilePath must be rooted."));
        }
        else if (rootedFilePath.endsWith("/"))
        {
            result = Result.error(new IllegalArgumentException("rootedFilePath cannot end with '/'."));
        }
        else if (rootedFilePath.endsWith("\\"))
        {
            result = Result.error(new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
        }
        else if (containsInvalidCharacters(rootedFilePath))
        {
            result = Result.<T>error(new IllegalArgumentException("rootedFilePath cannot contain invalid characters " + invalidCharacters + "."));
        }

        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateRootedFilePathAsync(Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = validateRootedFilePath(rootedFilePath);
        return result == null ? null : currentAsyncRunner.<T>error(result.getError());
    }

    public static boolean containsInvalidCharacters(Path path)
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

    private static <T> AsyncFunction<Result<T>> async(FileSystem fileSystem, Function0<Result<T>> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner fileSystemAsyncRunner = fileSystem.getAsyncRunner();
        return fileSystemAsyncRunner.schedule(function)
            .thenOn(currentAsyncRunner);
    }
}
