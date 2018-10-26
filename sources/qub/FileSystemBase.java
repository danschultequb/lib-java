package qub;

public abstract class FileSystemBase implements FileSystem
{
    @Override
    public Result<Boolean> rootExists(String rootPath)
    {
        validateRootedFolderPath(rootPath, "rootPath");

        return rootExists(Path.parse(rootPath));
    }

    @Override
    public Result<Boolean> rootExists(Path rootPath)
    {
        validateRootedFolderPath(rootPath, "rootPath");

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
        validateRootedFolderPath(rootPath, "rootPath");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> rootExists(rootPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> rootExistsAsync(Path rootPath)
    {
        validateRootedFolderPath(rootPath, "rootPath");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> rootExists(rootPath));
    }

    @Override
    public Result<Root> getRoot(String rootPath)
    {
        validateRootedFolderPath(rootPath, "rootPath");

        return getRoot(Path.parse(rootPath));
    }

    @Override
    public Result<Root> getRoot(Path rootPath)
    {
        validateRootedFolderPath(rootPath, "rootPath");

        return Result.success(new Root(this, rootPath.getRoot()));
    }

    @Override
    public AsyncFunction<Result<Iterable<Root>>> getRootsAsync()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::getRoots);
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFolders(Path.parse(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFolders(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFolders(rootedFolderPath));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFoldersRecursively(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

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
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFoldersRecursively(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFilesAndFoldersRecursively(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFolders(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFolders(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFolders(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        final Result<Iterable<FileSystemEntry>> result = getFilesAndFolders(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(Folder.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFolders(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFolders(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFoldersRecursively(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFoldersRecursively(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<Folder>> getFoldersRecursively(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        final Result<Iterable<FileSystemEntry>> result = getFilesAndFoldersRecursively(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(Folder.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFoldersRecursively(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFoldersRecursively(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFiles(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFiles(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFiles(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        Result<Iterable<FileSystemEntry>> result = getFilesAndFolders(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(File.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFilesRecursively(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFilesRecursively(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Iterable<File>> getFilesRecursively(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        final Result<Iterable<FileSystemEntry>> result = getFilesAndFoldersRecursively(rootedFolderPath);
        final Iterable<FileSystemEntry> entries = result.getValue();
        return Result.done(entries == null ? null : entries.instanceOf(File.class), result.getError());
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFiles(rootedFolderPath));
    }

    @Override
    public Result<Folder> getFolder(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return getFolder(Path.parse(rootedFolderPath));
    }

    @Override
    public Result<Folder> getFolder(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

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
        validateRootedFolderPath(rootedFolderPath);

        return folderExists(Path.parse(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> folderExists(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> folderExists(rootedFolderPath));
    }

    @Override
    public Result<Folder> createFolder(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return createFolder(Path.parse(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> createFolder(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> createFolder(rootedFolderPath));
    }

    @Override
    public Result<Boolean> deleteFolder(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        return deleteFolder(Path.parse(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> deleteFolder(rootedFolderPath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> deleteFolder(rootedFolderPath));
    }

    @Override
    public Result<File> getFile(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return getFile(Path.parse(rootedFilePath));
    }

    @Override
    public Result<File> getFile(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        Result<File> result;
        final Result<Path> resolvedRootedFilePath = rootedFilePath.resolve();
        if (resolvedRootedFilePath.hasError())
        {
            result = Result.error(resolvedRootedFilePath.getError());
        }
        else
        {
            result = Result.success(new File(this, resolvedRootedFilePath.getValue()));
        }
        return result;
    }

    @Override
    public Result<Boolean> fileExists(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return fileExists(Path.parse(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> fileExists(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> fileExists(rootedFilePath));
    }

    @Override
    public Result<File> createFile(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return createFile(Path.parse(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> createFile(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> createFile(rootedFilePath));
    }

    @Override
    public Result<Boolean> deleteFile(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return deleteFile(Path.parse(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> deleteFile(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> deleteFile(rootedFilePath));
    }

    @Override
    public Result<DateTime> getFileLastModified(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return getFileLastModified(Path.parse(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileLastModified(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileLastModified(rootedFilePath));
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileContentByteReadStream(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileContentByteReadStream(rootedFilePath));
    }

    @Override
    public Result<byte[]> getFileContent(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return getFileContent(Path.parse(rootedFilePath));
    }

    @Override
    public Result<byte[]> getFileContent(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        Result<byte[]> result;
        final Result<ByteReadStream> byteReadStreamResult = getFileContentByteReadStream(rootedFilePath);
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

        return result;
    }

    @Override
    public AsyncFunction<Result<byte[]>> getFileContentAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileContent(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<byte[]>> getFileContentAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileContent(rootedFilePath));
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return getFileContentByteWriteStream(Path.parse(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(String rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileContentByteWriteStream(rootedFilePath));
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> getFileContentByteWriteStream(rootedFilePath));
    }

    @Override
    public Result<Boolean> setFileContent(String rootedFilePath, byte[] content)
    {
        validateRootedFilePath(rootedFilePath);

        return setFileContent(Path.parse(rootedFilePath), content);
    }

    @Override
    public Result<Boolean> setFileContent(Path rootedFilePath, byte[] content)
    {
        validateRootedFilePath(rootedFilePath);

        Result<Boolean> result;
        final Result<ByteWriteStream> byteWriteStreamResult = getFileContentByteWriteStream(rootedFilePath);
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
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> setFileContentAsync(String rootedFilePath, byte[] content)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> setFileContent(rootedFilePath, content));
    }

    @Override
    public AsyncFunction<Result<Boolean>> setFileContentAsync(Path rootedFilePath, byte[] content)
    {
        validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> setFileContent(rootedFilePath, content));
    }

    public static void validateRootedFolderPath(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath, "rootedFolderPath");
    }

    public static void validateRootedFolderPath(String rootedFolderPath, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, expressionName);
    }

    public static void validateRootedFolderPath(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath, "rootedFolderPath");
    }

    public static void validateRootedFolderPath(Path rootedFolderPath, String expressionName)
    {
        PreCondition.assertNotNull(rootedFolderPath, expressionName);
        PreCondition.assertTrue(rootedFolderPath.isRooted(), expressionName + ".isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(" + expressionName + ")");
    }

    public static void validateRootedFilePath(String rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
    }

    public static void validateRootedFilePath(Path rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertTrue(rootedFilePath.isRooted(), "rootedFilePath.isRooted()");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFilePath), "containsInvalidCharacters(rootedFilePath (" + Strings.escapeAndQuote(rootedFilePath.toString()) + "))");
    }

    private static final Array<Character> invalidCharacters = Array.fromValues(new Character[]
    {
        '\u0000',
        '?',
        '<',
        '>',
        '|',
        '*',
        '\"',
        ':'
    });
    static boolean containsInvalidCharacters(Path path)
    {
        boolean result = false;

        if (path != null)
        {
            final Path pathWithoutRoot = path.withoutRoot();
            if (pathWithoutRoot != null)
            {
                final String pathString = pathWithoutRoot.toString();
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
        }

        return result;
    }
}
