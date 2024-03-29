package qub;

/**
 * A {@link FileSystem} implementation that interacts with a typical Windows, Linux, or MacOS
 * device.
 */
public class JavaFileSystem implements FileSystem
{
    private JavaFileSystem()
    {
    }

    public static JavaFileSystem create()
    {
        return new JavaFileSystem();
    }

    @Override
    public Iterator<Root> iterateRoots()
    {
        final List<Root> rootList = List.create();

        final java.lang.Iterable<java.nio.file.Path> rootPaths = java.nio.file.FileSystems.getDefault().getRootDirectories();
        for (final java.nio.file.Path rootPath : rootPaths)
        {
            final String rootPathString = rootPath.toString();
            final String trimmedRootPathString = rootPathString.equals("/")
                ? rootPathString
                : rootPathString.substring(0, rootPathString.length() - 1);
            rootList.add(this.getRoot(trimmedRootPathString).await());
        }

        final Iterator<Root> result = rootList.iterate();

        PostCondition.assertNotNull(result, "result");

        return result;
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
    public Iterator<FileSystemEntry> iterateEntries(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return LazyIterator.create(() ->
        {
            final java.io.File javaFolder = new java.io.File(rootedFolderPath.toString());
            final java.io.File[] javaFolderFiles = javaFolder.listFiles();
            if (javaFolderFiles == null)
            {
                throw new FolderNotFoundException(rootedFolderPath);
            }

            final List<Folder> folders = List.create();
            final List<File> files = List.create();
            for (final java.io.File javaFolderFile : javaFolderFiles)
            {
                final String containerEntryPathString = javaFolderFile.getAbsolutePath();
                final Path containerEntryPath = Path.parse(containerEntryPathString).normalize();
                if (javaFolderFile.isFile())
                {
                    files.add(getFile(containerEntryPath).await());
                }
                else if (javaFolderFile.isDirectory())
                {
                    folders.add(getFolder(containerEntryPath).await());
                }
            }

            final List<FileSystemEntry> entries = List.create();
            entries.addAll(folders);
            entries.addAll(files);

            final Iterator<FileSystemEntry> result = entries.iterate();

            PostCondition.assertNotNull(result, "result");
            PostCondition.assertFalse(result.hasStarted(), "result.hasStarted()");

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
            for (final FileSystemEntry entry : this.iterateEntries(rootedFolderPath))
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
                try
                {
                    java.nio.file.Files.delete(filePath);
                }
                catch (java.nio.file.AccessDeniedException e)
                {
                    final java.io.File file = new java.io.File(rootedFilePathString);
                    if (file.canWrite())
                    {
                        throw e;
                    }
                    else
                    {
                        file.setWritable(true);
                        java.nio.file.Files.delete(filePath);
                    }
                }
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
    public Result<CharacterToByteReadStream> getFileContentReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            CharacterToByteReadStream result;
            try
            {
                final String rootedFilePathString = rootedFilePath.toString();
                final java.nio.file.Path filePath = java.nio.file.Paths.get(rootedFilePathString);
                final java.io.InputStream fileContentsInputStream = java.nio.file.Files.newInputStream(filePath);
                result = CharacterToByteReadStream.create(InputStreamToByteReadStream.create(fileContentsInputStream));
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
    public Result<BufferedByteWriteStream> getFileContentsByteWriteStream(Path rootedFilePath, OpenWriteType openWriteType)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return Result.create(() ->
        {
            BufferedByteWriteStream result;
            final java.nio.file.StandardOpenOption openWriteOption = (openWriteType == OpenWriteType.CreateOrOverwrite
                ? java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
                : java.nio.file.StandardOpenOption.APPEND);
            final java.nio.file.StandardOpenOption[] openWriteOptions = new java.nio.file.StandardOpenOption[]
            {
                java.nio.file.StandardOpenOption.CREATE,
                openWriteOption
            };
            try
            {
                result = BufferedByteWriteStream.create(
                            OutputStreamToByteWriteStream.create(
                                java.nio.file.Files.newOutputStream(
                                    java.nio.file.Paths.get(rootedFilePath.toString()),
                                    openWriteOptions)));
            }
            catch (java.nio.file.NoSuchFileException e1)
            {
                try
                {
                    this.createFolder(rootedFilePath.getParent().await()).await();
                    result = BufferedByteWriteStream.create(
                                OutputStreamToByteWriteStream.create(
                                    java.nio.file.Files.newOutputStream(
                                        java.nio.file.Paths.get(rootedFilePath.toString()),
                                        openWriteOptions)));
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
