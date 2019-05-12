package qub;

/**
 * A FileSystem implementation that interacts with a typical Windows, Linux, or MacOS device.
 */
public class JavaFileSystem implements FileSystem
{
    @Override
    public Result<Iterable<Root>> getRoots()
    {
        return Result.create(() ->
        {
            return Iterable.create(java.io.File.listRoots())
                .map((java.io.File root) ->
                {
                    final String rootPathString = root.getAbsolutePath();
                    final String trimmedRootPathString = rootPathString.equals("/") ? rootPathString : rootPathString.substring(0, rootPathString.length() - 1);
                    return getRoot(trimmedRootPathString).await();
                });
        });
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        Result<Iterable<FileSystemEntry>> result;
        Array<FileSystemEntry> filesAndFolders;

        final java.io.File containerFile = new java.io.File(rootedFolderPath.toString());
        final java.io.File[] containerEntryFiles = containerFile.listFiles();
        if (containerEntryFiles == null)
        {
            result = Result.error(new FolderNotFoundException(rootedFolderPath));
        }
        else
        {
            final List<Folder> folders = List.create();
            final List<File> files = List.create();
            for (final java.io.File containerEntryFile : containerEntryFiles)
            {
                final String containerEntryPathString = containerEntryFile.getAbsolutePath();
                final Path containerEntryPath = Path.parse(containerEntryPathString).normalize();
                if (containerEntryFile.isFile())
                {
                    files.add(getFile(containerEntryPath).await());
                }
                else if (containerEntryFile.isDirectory())
                {
                    folders.add(getFolder(containerEntryPath).await());
                }
            }

            filesAndFolders = Array.createWithLength(containerEntryFiles.length);
            final int foldersCount = folders.getCount();
            for (int i = 0; i < foldersCount; ++i)
            {
                filesAndFolders.set(i, folders.get(i));
            }
            for (int i = 0; i < files.getCount(); ++i)
            {
                filesAndFolders.set(i + foldersCount, files.get(i));
            }

            result = Result.success(filesAndFolders);
        }
        return result;
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        final String folderPathString = rootedFolderPath.toString();
        final java.io.File folderFile = new java.io.File(folderPathString);
        return Result.success(folderFile.exists() && folderFile.isDirectory());
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        Result<Folder> result;
        try
        {
            final Path parentFolderPath = rootedFolderPath.getParent();
            if (parentFolderPath != null)
            {
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get(parentFolderPath.toString()));
            }
            java.nio.file.Files.createDirectory(java.nio.file.Paths.get(rootedFolderPath.toString()));
            result = getFolder(rootedFolderPath);
        }
        catch (java.nio.file.FileAlreadyExistsException e)
        {
            result = Result.error(new FolderAlreadyExistsException(rootedFolderPath));
        }
        catch (Throwable e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFolders(rootedFolderPath)
            .then((Iterable<FileSystemEntry> entries) ->
            {
                for (final FileSystemEntry entry : entries)
                {
                    entry.delete().await();
                }
                try
                {
                    java.nio.file.Files.delete(java.nio.file.Paths.get(rootedFolderPath.toString()));
                }
                catch (java.io.FileNotFoundException e)
                {
                    throw new FolderNotFoundException(rootedFolderPath);
                }
                catch (Throwable error)
                {
                    throw Exceptions.asRuntime(error);
                }
            });
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.success(java.nio.file.Files.isRegularFile(java.nio.file.Paths.get(rootedFilePath.toString())));
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return createFolder(rootedFilePath.getParent())
            .catchError(FolderAlreadyExistsException.class)
            .thenResult(() ->
            {
                Result<File> createFileResult;
                try
                {
                    java.nio.file.Files.createFile(java.nio.file.Paths.get(rootedFilePath.toString()));
                    createFileResult = getFile(rootedFilePath);
                }
                catch (java.nio.file.FileAlreadyExistsException e)
                {
                    createFileResult = Result.error(new FileAlreadyExistsException(rootedFilePath));
                }
                catch (Throwable e)
                {
                    createFileResult = Result.error(e);
                }
                return createFileResult;
            });
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<Void> result;
        try
        {
            java.nio.file.Files.delete(java.nio.file.Paths.get(rootedFilePath.toString()));
            result = Result.success();
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            result = Result.error(new FileNotFoundException(rootedFilePath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<DateTime> result;
        try
        {
            final java.nio.file.attribute.FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(java.nio.file.Paths.get(rootedFilePath.toString()));
            result = Result.success(DateTime.local(lastModifiedTime.toMillis()));
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            result = Result.error(new FileNotFoundException(rootedFilePath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        Result<ByteReadStream> result;
        try
        {
            final java.io.InputStream fileContentsInputStream = java.nio.file.Files.newInputStream(
                java.nio.file.Paths.get(rootedFilePath.toString()));
            result = Result.success(new InputStreamToByteReadStream(fileContentsInputStream));
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            result = Result.error(new FileNotFoundException(rootedFilePath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<ByteWriteStream> result;
        try
        {
            result = Result.success(
                new BufferedByteWriteStream(
                    new OutputStreamToByteWriteStream(
                        java.nio.file.Files.newOutputStream(
                            java.nio.file.Paths.get(rootedFilePath.toString()),
                            java.nio.file.StandardOpenOption.CREATE,
                            java.nio.file.StandardOpenOption.TRUNCATE_EXISTING))));
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            try
            {
                createFolder(rootedFilePath.getParent()).await();
                result = Result.success(
                    new BufferedByteWriteStream(
                        new OutputStreamToByteWriteStream(
                            java.nio.file.Files.newOutputStream(
                                java.nio.file.Paths.get(rootedFilePath.toString()),
                                java.nio.file.StandardOpenOption.CREATE,
                                java.nio.file.StandardOpenOption.TRUNCATE_EXISTING))));
            }
            catch (Throwable e1)
            {
                result = Result.error(e1);
            }
        }
        catch (Throwable e)
        {
            result = Result.error(e);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
