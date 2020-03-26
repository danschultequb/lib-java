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
                    final String trimmedRootPathString = rootPathString.equals("/")
                        ? rootPathString
                        : rootPathString.substring(0, rootPathString.length() - 1);
                    return getRoot(trimmedRootPathString).await();
                });
        });
    }

    @Override
    public Result<DataSize> getRootTotalDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            final java.io.File root = new java.io.File(rootPath.toString());
            final long totalSpaceBytes = root.getTotalSpace();
            if (totalSpaceBytes == 0)
            {
                throw new RootNotFoundException(rootPath);
            }
            return DataSize.bytes(totalSpaceBytes);
        });
    }

    @Override
    public Result<DataSize> getRootUnusedDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            final java.io.File root = new java.io.File(rootPath.toString());
            final long totalFreeSpace = root.getFreeSpace();
            if (totalFreeSpace == 0)
            {
                throw new RootNotFoundException(rootPath);
            }
            return DataSize.bytes(totalFreeSpace);
        });
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            final java.io.File containerFile = new java.io.File(rootedFolderPath.toString());
            final java.io.File[] containerEntryFiles = containerFile.listFiles();
            if (containerEntryFiles == null)
            {
                throw new FolderNotFoundException(rootedFolderPath);
            }

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

            final List<FileSystemEntry> result = List.create();
            result.addAll(folders);
            result.addAll(files);

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            final String folderPathString = rootedFolderPath.toString();
            final java.io.File folderFile = new java.io.File(folderPathString);
            return folderFile.exists() && folderFile.isDirectory();
        });
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            Folder result;
            try
            {
                final Path parentFolderPath = rootedFolderPath.getParent().await();
                if (parentFolderPath != null)
                {
                    java.nio.file.Files.createDirectories(java.nio.file.Paths.get(parentFolderPath.toString()));
                }
                java.nio.file.Files.createDirectory(java.nio.file.Paths.get(rootedFolderPath.toString()));
                result = this.getFolder(rootedFolderPath).await();
            }
            catch (java.nio.file.FileAlreadyExistsException e)
            {
                throw new FolderAlreadyExistsException(rootedFolderPath);
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return Result.create(() ->
        {
            final Iterable<FileSystemEntry> entries = this.getFilesAndFolders(rootedFolderPath).await();
            for (final FileSystemEntry entry : entries)
            {
                entry.delete().await();
            }

            try
            {
                final String rootedFolderPathString = rootedFolderPath.toString();
                final java.nio.file.Path folderPath = java.nio.file.Paths.get(rootedFolderPathString);
                java.nio.file.Files.delete(folderPath);
            }
            catch (java.io.FileNotFoundException e)
            {
                throw new FolderNotFoundException(rootedFolderPath);
            }
            catch (java.io.IOException error)
            {
                throw Exceptions.asRuntime(error);
            }
        });
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final String rootedFilePathString = rootedFilePath.toString();
            final java.nio.file.Path path = java.nio.file.Paths.get(rootedFilePathString);
            return java.nio.file.Files.isRegularFile(path);
        });
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            this.createFolder(rootedFilePath.getParent().await())
                .catchError(FolderAlreadyExistsException.class)
                .await();

            File result;
            try
            {
                java.nio.file.Files.createFile(java.nio.file.Paths.get(rootedFilePath.toString()));
                result = this.getFile(rootedFilePath).await();
            }
            catch (java.nio.file.FileAlreadyExistsException e)
            {
                throw new FileAlreadyExistsException(rootedFilePath);
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            try
            {
                final String rootedFilePathString = rootedFilePath.toString();
                final java.nio.file.Path filePath = java.nio.file.Paths.get(rootedFilePathString);
                java.nio.file.Files.delete(filePath);
            }
            catch (java.nio.file.NoSuchFileException e)
            {
                throw new FileNotFoundException(rootedFilePath);
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
        });
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            DateTime result;
            try
            {
                final String rootedFilePathString = rootedFilePath.toString();
                final java.nio.file.Path filePath = java.nio.file.Paths.get(rootedFilePathString);
                final java.nio.file.attribute.FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(filePath);
                final java.time.Instant lastModifiedInstant = lastModifiedTime.toInstant();
                final long secondsSinceEpoch = lastModifiedInstant.getEpochSecond();
                final int nanosecondAdjustment = lastModifiedInstant.getNano();
                final Duration durationSinceEpoch = Duration.seconds(secondsSinceEpoch).plus(Duration.nanoseconds(nanosecondAdjustment));
                result = DateTime.createFromDurationSinceEpoch(durationSinceEpoch);
            }
            catch (java.nio.file.NoSuchFileException e)
            {
                throw new FileNotFoundException(rootedFilePath);
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
            return result;
        });
    }

    @Override
    public Result<DataSize> getFileContentDataSize(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final String rootedFilePathString = rootedFilePath.toString();
            final java.io.File file = new java.io.File(rootedFilePathString);
            final long fileContentDataSizeInBytes = file.length();
            if (fileContentDataSizeInBytes == 0 && !file.isFile())
            {
                throw new FileNotFoundException(rootedFilePath);
            }
            final DataSize result = DataSize.bytes(fileContentDataSizeInBytes);

            PostCondition.assertNotNull(result, "result");
            PostCondition.assertGreaterThanOrEqualTo(result, DataSize.zero, "result");

            return result;
        });
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            ByteReadStream result;
            try
            {
                final String rootedFilePathString = rootedFilePath.toString();
                final java.nio.file.Path filePath = java.nio.file.Paths.get(rootedFilePathString);
                final java.io.InputStream fileContentsInputStream = java.nio.file.Files.newInputStream(filePath);
                result = new InputStreamToByteReadStream(fileContentsInputStream);
            }
            catch (java.nio.file.NoSuchFileException e)
            {
                throw new FileNotFoundException(rootedFilePath);
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
            return result;
        });
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            ByteWriteStream result;
            try
            {
                result = new BufferedByteWriteStream(
                            new OutputStreamToByteWriteStream(
                                java.nio.file.Files.newOutputStream(
                                    java.nio.file.Paths.get(rootedFilePath.toString()),
                                    java.nio.file.StandardOpenOption.CREATE,
                                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)));
            }
            catch (java.nio.file.NoSuchFileException e1)
            {
                try
                {
                    this.createFolder(rootedFilePath.getParent().await()).await();
                    result = new BufferedByteWriteStream(
                                new OutputStreamToByteWriteStream(
                                    java.nio.file.Files.newOutputStream(
                                        java.nio.file.Paths.get(rootedFilePath.toString()),
                                        java.nio.file.StandardOpenOption.CREATE,
                                        java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)));
                }
                catch (java.io.IOException e2)
                {
                    throw Exceptions.asRuntime(e2);
                }
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }
}
