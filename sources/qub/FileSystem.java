package qub;

/**
 * The {@link FileSystem} class provides access to {@link File}s, {@link Folder}s, and {@link Root}s
 * within a device's file system.
 */
public interface FileSystem
{
    /**
     * Get whether a {@link Root} exists in this {@link FileSystem} with the provided path.
     * @param rootPath The path to the {@link Root}.
     */
    public default Result<Boolean> rootExists(String rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return this.rootExists(Path.parse(rootPath));
    }

    /**
     * Get whether a {@link Root} exists in this {@link FileSystem} with the provided {@link Path}.
     * @param rootPath The {@link Path} to the {@link Root}.
     */
    public default Result<Boolean> rootExists(Path rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return Result.create(() ->
        {
            final Path realRootPath = rootPath.getRoot().await();
            return this.iterateRoots()
                .map(Root::getPath)
                .contains(realRootPath)
                .await();
        });
    }

    /**
     * Get a reference to a {@link Root} with the provided path {@link String}. The returned
     * {@link Root} may or may not exist.
     * @param rootPath The path {@link String} to the {@link Root} to return.
     */
    public default Result<Root> getRoot(String rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return this.getRoot(Path.parse(rootPath));
    }

    /**
     * Get a reference to a {@link Root} with the provided {@link Path}. The returned {@link Root}
     * may or may not exist.
     * @param rootPath The {@link Path} to the {@link Root} to return.
     */
    public default Result<Root> getRoot(Path rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return Result.create(() ->
        {
            return Root.create(this, rootPath.getRoot().await());
        });
    }

    /**
     * Get an {@link Iterator} that will iterate over the {@link Root}s of this {@link FileSystem}.
     */
    public Iterator<Root> iterateRoots();

    /**
     * Get the total {@link DataSize} that the {@link Root} at the provided path can contain.
     * @param rootPath The path {@link String} to the {@link Root}.
     */
    public default Result<DataSize> getRootTotalDataSize(String rootPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootPath, "rootPath");

        return this.getRootTotalDataSize(Path.parse(rootPath));
    }

    /**
     * Get the total {@link DataSize} that the {@link Root} at the provided {@link Path} can
     * contain.
     * @param rootPath The {@link Path} to the {@link Root}.
     */
    public Result<DataSize> getRootTotalDataSize(Path rootPath);

    /**
     * Get the {@link DataSize} that the {@link Root} at the provided path {@link String} is not
     * using.
     * @param rootPath The path {@link String} to the {@link Root}.
     */
    public default Result<DataSize> getRootUnusedDataSize(String rootPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootPath, "rootPath");

        return this.getRootUnusedDataSize(Path.parse(rootPath));
    }

    /**
     * Get the amount of data that the root at the provided path is not using.
     * @param rootPath The path to the root.
     * @return The amount of data size that the root at the provided path is not using.
     */
    Result<DataSize> getRootUnusedDataSize(Path rootPath);

    /**
     * Get the amount of data that the root at the provided path is using.
     * @param rootPath The path to the root.
     * @return The amount of data size that the root at the provided path is using.
     */
    default Result<DataSize> getRootUsedDataSize(String rootPath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootPath, "rootPath");

        return this.getRootUsedDataSize(Path.parse(rootPath));
    }

    /**
     * Get the amount of data that the root at the provided path is using.
     * @param rootPath The path to the root.
     * @return The amount of data size that the root at the provided path is using.
     */
    default Result<DataSize> getRootUsedDataSize(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");

        return Result.create(() ->
        {
            final DataSize totalDataSize = this.getRootTotalDataSize(rootPath).await();
            final DataSize unusedDataSize = this.getRootUnusedDataSize(rootPath).await();
            return totalDataSize.minus(unusedDataSize);
        });
    }

    /**
     * Iterate the entries (files and folders) at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The entries (files and folders) at the provided folder path.
     */
    default Iterator<FileSystemEntry> iterateEntries(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntries(Path.parse(rootedFolderPath));
    }

    /**
     * Iterate the entries (files and folders) at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The entries (files and folders) at the provided folder path.
     */
    Iterator<FileSystemEntry> iterateEntries(Path rootedFolderPath);

    /**
     * Iterate the files at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided folder path.
     */
    default Iterator<File> iterateFiles(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateFiles(Path.parse(rootedFolderPath));
    }

    /**
     * Iterate the files at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided folder path.
     */
    default Iterator<File> iterateFiles(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntries(rootedFolderPath)
            .instanceOf(File.class);
    }

    /**
     * Iterate the folders at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided folder path.
     */
    default Iterator<Folder> iterateFolders(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateFolders(Path.parse(rootedFolderPath));
    }

    /**
     * Iterate the folders at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided folder path.
     */
    default Iterator<Folder> iterateFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntries(rootedFolderPath)
            .instanceOf(Folder.class);
    }

    /**
     * Iterate the entries (files and folders) at the provided folder path recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The entries (files and folders) at the provided folder path.
     */
    default Iterator<FileSystemEntry> iterateEntriesRecursively(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntriesRecursively(Path.parse(rootedFolderPath));
    }

    /**
     * Iterate the entries (files and folders) at the provided folder path recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The entries (files and folders) at the provided folder path.
     */
    default Iterator<FileSystemEntry> iterateEntriesRecursively(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntries(rootedFolderPath, Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal));
    }

    /**
     * Iterate the files at the provided folder path recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided folder path.
     */
    default Iterator<File> iterateFilesRecursively(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateFilesRecursively(Path.parse(rootedFolderPath));
    }

    /**
     * Iterate the files at the provided folder path recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided folder path.
     */
    default Iterator<File> iterateFilesRecursively(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntriesRecursively(rootedFolderPath)
            .instanceOf(File.class);
    }

    /**
     * Iterate the folders at the provided folder path recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided folder path.
     */
    default Iterator<Folder> iterateFoldersRecursively(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateFoldersRecursively(Path.parse(rootedFolderPath));
    }

    /**
     * Iterate the folders at the provided folder path recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided folder path.
     */
    default Iterator<Folder> iterateFoldersRecursively(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.iterateEntriesRecursively(rootedFolderPath)
            .instanceOf(Folder.class);
    }

    /**
     * Iterate the entries (files and folders) at the provided folder path with the provided
     * Traversal.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @param traversal The traversal to use to iterate over the entries (files and folders).
     * @return The entries (files and folders) at the provided folder path.
     */
    default Iterator<FileSystemEntry> iterateEntries(String rootedFolderPath, Traversal<Folder,FileSystemEntry> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        return this.iterateEntries(Path.parse(rootedFolderPath), traversal);
    }

    /**
     * Iterate the entries (files and folders) at the provided folder path with the provided
     * Traversal.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @param traversal The traversal to use to iterate over the entries (files and folders).
     * @return The entries (files and folders) at the provided folder path.
     */
    default Iterator<FileSystemEntry> iterateEntries(Path rootedFolderPath, Traversal<Folder,FileSystemEntry> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        final Folder folder = this.getFolder(rootedFolderPath).await();
        return traversal.iterate(folder);
    }

    /**
     * Iterate the files at the provided folder path with the provided Traversal.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @param traversal The traversal to use to iterate over the files.
     * @return The files at the provided folder path.
     */
    default Iterator<File> iterateFiles(String rootedFolderPath, Traversal<Folder,File> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        return this.iterateFiles(Path.parse(rootedFolderPath), traversal);
    }

    /**
     * Iterate the files at the provided folder path with the provided Traversal.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @param traversal The traversal to use to iterate over the files.
     * @return The files at the provided folder path.
     */
    default Iterator<File> iterateFiles(Path rootedFolderPath, Traversal<Folder,File> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        final Folder folder = this.getFolder(rootedFolderPath).await();
        return traversal.iterate(folder);
    }

    /**
     * Iterate the folders at the provided folder path with the provided Traversal.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @param traversal The traversal to use to iterate over the folders.
     * @return The folders at the provided folder path.
     */
    default Iterator<Folder> iterateFolders(String rootedFolderPath, Traversal<Folder,Folder> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        return this.iterateFolders(Path.parse(rootedFolderPath), traversal);
    }

    /**
     * Iterate the folders at the provided folder path with the provided Traversal.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @param traversal The traversal to use to iterate over the folders.
     * @return The folders at the provided folder path.
     */
    default Iterator<Folder> iterateFolders(Path rootedFolderPath, Traversal<Folder,Folder> traversal)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);
        PreCondition.assertNotNull(traversal, "traversal");

        final Folder folder = this.getFolder(rootedFolderPath).await();
        return traversal.iterate(folder);
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param rootedFolderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    default Result<Folder> getFolder(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.getFolder(Path.parse(rootedFolderPath));
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param rootedFolderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    default Result<Folder> getFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return rootedFolderPath.resolve()
            .then((Path resolvedRootedFolderPath) -> Folder.create(this, resolvedRootedFolderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param rootedFolderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default Result<Boolean> folderExists(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.folderExists(Path.parse(rootedFolderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param rootedFolderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public Result<Boolean> folderExists(Path rootedFolderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param rootedFolderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public default Result<Folder> createFolder(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.createFolder(Path.parse(rootedFolderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param rootedFolderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public Result<Folder> createFolder(Path rootedFolderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param rootedFolderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    default Result<Void> deleteFolder(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return this.deleteFolder(Path.parse(rootedFolderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param rootedFolderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    Result<Void> deleteFolder(Path rootedFolderPath);

    /**
     * Get a reference to the File at the provided folderPath.
     * @param rootedFilePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    default Result<File> getFile(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getFile(Path.parse(rootedFilePath));
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param rootedFilePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    default Result<File> getFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return rootedFilePath.resolve()
            .then((Path resolvedRootedFilePath) -> new File(this, resolvedRootedFilePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param rootedFilePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default Result<Boolean> fileExists(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.fileExists(Path.parse(rootedFilePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param rootedFilePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    Result<Boolean> fileExists(Path rootedFilePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param rootedFilePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    default Result<File> createFile(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.createFile(Path.parse(rootedFilePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param rootedFilePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    Result<File> createFile(Path rootedFilePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param rootedFilePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    default Result<Void> deleteFile(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.deleteFile(Path.parse(rootedFilePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param rootedFilePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    Result<Void> deleteFile(Path rootedFilePath);

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param rootedFilePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    default Result<DateTime> getFileLastModified(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileLastModified(Path.parse(rootedFilePath));
    }

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param rootedFilePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    Result<DateTime> getFileLastModified(Path rootedFilePath);

    /**
     * Get the content data size of the file at the provided path.
     * @param rootedFilePath The rooted path to the file.
     * @return The content data size of the file at the provided path.
     */
    default Result<DataSize> getFileContentDataSize(String rootedFilePath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFilePath, "rootedFilePath");

        return this.getFileContentDataSize(Path.parse(rootedFilePath));
    }

    /**
     * Get the content data size of the file at the provided path.
     * @param rootedFilePath The rooted path to the file.
     * @return The content data size of the file at the provided path.
     */
    Result<DataSize> getFileContentDataSize(Path rootedFilePath);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    default Result<CharacterToByteReadStream> getFileContentReadStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getFileContentReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    Result<CharacterToByteReadStream> getFileContentReadStream(Path rootedFilePath);

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The byte[] contents of the file at the provided rootedFilePath.
     */
    default Result<byte[]> getFileContent(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getFileContent(Path.parse(rootedFilePath));
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The byte[] contents of the file at the provided rootedFilePath.
     */
    default Result<byte[]> getFileContent(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.createUsing(
            () -> this.getFileContentReadStream(rootedFilePath).await(),
            (ByteReadStream byteReadStream) ->
            {
                return byteReadStream.readAllBytes()
                        .catchError(EmptyException.class, () -> new byte[0])
                        .await();
            });
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The String contents of the file at the provided rootedFilePath.
     */
    default Result<String> getFileContentsAsString(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getFileContentsAsString(Path.parse(rootedFilePath));
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The String contents of the file at the provided rootedFilePath.
     */
    default Result<String> getFileContentsAsString(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return Result.create(() ->
        {
            final byte[] fileContent = this.getFileContent(rootedFilePath).await();
            return fileContent.length == 0
                ? ""
                : CharacterEncoding.UTF_8.decodeAsString(fileContent).await();
        });
    }

    /**
     * Get a ByteWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteWriteStream to the contents of the file.
     */
    default Result<BufferedByteWriteStream> getFileContentsByteWriteStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getFileContentsByteWriteStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    default Result<BufferedByteWriteStream> getFileContentsByteWriteStream(Path rootedFilePath)
    {
        return this.getFileContentsByteWriteStream(rootedFilePath, OpenWriteType.CreateOrOverwrite);
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param openWriteType The manner in which the file will be opened for writing.
     * @return A ByteReadStream to the contents of the file.
     */
    default Result<BufferedByteWriteStream> getFileContentsByteWriteStream(String rootedFilePath, OpenWriteType openWriteType)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return this.getFileContentsByteWriteStream(Path.parse(rootedFilePath), openWriteType);
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param openWriteType The manner in which the file will be opened for writing.
     * @return A ByteReadStream to the contents of the file.
     */
    Result<BufferedByteWriteStream> getFileContentsByteWriteStream(Path rootedFilePath, OpenWriteType openWriteType);

    /**
     * Get a CharacterWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterWriteStream to the contents of the file.
     */
    default Result<CharacterToByteWriteStream> getFileContentsCharacterWriteStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.getFileContentsCharacterWriteStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a CharacterWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterWriteStream to the contents of the file.
     */
    default Result<CharacterToByteWriteStream> getFileContentsCharacterWriteStream(Path rootedFilePath)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");

        return this.getFileContentsCharacterWriteStream(rootedFilePath, OpenWriteType.CreateOrOverwrite);
    }

    /**
     * Get a CharacterWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterWriteStream to the contents of the file.
     */
    default Result<CharacterToByteWriteStream> getFileContentsCharacterWriteStream(String rootedFilePath, OpenWriteType openWriteType)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return this.getFileContentsCharacterWriteStream(Path.parse(rootedFilePath), openWriteType);
    }

    /**
     * Get a CharacterWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterWriteStream to the contents of the file.
     */
    default Result<CharacterToByteWriteStream> getFileContentsCharacterWriteStream(Path rootedFilePath, OpenWriteType openWriteType)
    {
        PreCondition.assertNotNull(rootedFilePath, "rootedFilePath");
        PreCondition.assertNotNull(openWriteType, "openWriteType");

        return Result.create(() ->
        {
            final BufferedByteWriteStream byteWriteStream = this.getFileContentsByteWriteStream(rootedFilePath, openWriteType).await();
            return CharacterWriteStream.create(byteWriteStream);
        });
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param contents The byte[] contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<File> setFileContents(String rootedFilePath, byte[] contents)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(contents, "content");

        return this.setFileContents(Path.parse(rootedFilePath), contents);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param contents The byte[] contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<File> setFileContents(Path rootedFilePath, byte[] contents)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(contents, "content");

        return Result.createUsing(
            () -> { return this.getFileContentsByteWriteStream(rootedFilePath).await(); },
            (BufferedByteWriteStream byteWriteStream) ->
            {
                if (contents.length > 0)
                {
                    byteWriteStream.writeAll(contents).await();
                }
                return this.getFile(rootedFilePath).await();
            });
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param contents The String contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<File> setFileContentsAsString(String rootedFilePath, String contents)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return this.setFileContentsAsString(Path.parse(rootedFilePath), contents);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param contents The String contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<File> setFileContentsAsString(Path rootedFilePath, String contents)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);
        PreCondition.assertNotNull(contents, "content");

        return Result.create(() ->
        {
            final byte[] encodedBytes = Strings.isNullOrEmpty(contents)
                ? new byte[0]
                : CharacterEncoding.UTF_8.encodeCharacters(contents).await();
            return this.setFileContents(rootedFilePath, encodedBytes).await();
        });
    }

    /**
     * Copy the provided sourceFile to the provided destinationFile.
     * @param sourceFile The file to copy create.
     * @param destinationFile The file to copy to.
     * @return The result of copying the file.
     */
    default Result<Void> copyFileTo(File sourceFile, File destinationFile)
    {
        PreCondition.assertNotNull(sourceFile, "sourceFile");
        PreCondition.assertNotNull(destinationFile, "destinationFile");

        return this.copyFileTo(sourceFile.getPath(), destinationFile.getPath());
    }

    /**
     * Copy the file at the provided sourceFile to the provided destinationFilePath.
     * @param sourceFile The file to copy.
     * @param destinationFilePath The path to copy the file to.
     * @return The result of copying the file.
     */
    default Result<Void> copyFileTo(File sourceFile, Path destinationFilePath)
    {
        PreCondition.assertNotNull(sourceFile, "sourceFile");
        FileSystem.validateRootedFilePath(destinationFilePath, "destinationFilePath");

        return this.copyFileTo(sourceFile.getPath(), destinationFilePath);
    }

    /**
     * Copy the file at the provided sourceFilePath to the provided destinationFile.
     * @param sourceFilePath The path to the file to copy.
     * @param destinationFile The file to copy to.
     * @return The result of copying the file.
     */
    public default Result<Void> copyFileTo(Path sourceFilePath, File destinationFile)
    {
        FileSystem.validateRootedFilePath(sourceFilePath, "sourceFilePath");
        PreCondition.assertNotNull(destinationFile, "destinationFile");

        return this.copyFileTo(sourceFilePath, destinationFile.getPath());
    }

    /**
     * Copy the file at the provided sourceFilePath to the provided destinationFilePath.
     * @param sourceFilePath The path to the file to copy.
     * @param destinationFilePath The path to copy the file to.
     * @return The result of copying the file.
     */
    public default Result<Void> copyFileTo(Path sourceFilePath, Path destinationFilePath)
    {
        FileSystem.validateRootedFilePath(sourceFilePath, "sourceFilePath");
        FileSystem.validateRootedFilePath(destinationFilePath, "destinationFilePath");

        return Result.createUsing(
            () -> this.getFileContentReadStream(sourceFilePath).await(),
            () -> this.getFileContentsByteWriteStream(destinationFilePath).await(),
            (ByteReadStream sourceStream, ByteWriteStream destinationStream) ->
            {
                destinationStream.writeAll(sourceStream).await();
            });
    }

    /**
     * Copy the file at the provided sourceFilePath to a file with the same name in the provided
     * destinationFolder.
     * @param sourceFile The file to copy.
     * @param destinationFolder The folder that the file will be copied to.
     * @return The result of copying the file.
     */
    public default Result<Void> copyFileToFolder(File sourceFile, Folder destinationFolder)
    {
        PreCondition.assertNotNull(sourceFile, "sourceFile");
        PreCondition.assertNotNull(destinationFolder, "destinationFolder");

        return this.copyFileToFolder(sourceFile.getPath(), destinationFolder.getPath());
    }

    /**
     * Copy the file at the provided sourceFilePath to a file with the same name in the provided
     * destinationFolder.
     * @param sourceFile The file to copy.
     * @param destinationFolderPath The folder path that the file will be copied to.
     * @return The result of copying the file.
     */
    public default Result<Void> copyFileToFolder(File sourceFile, Path destinationFolderPath)
    {
        PreCondition.assertNotNull(sourceFile, "sourceFile");
        PreCondition.assertNotNull(destinationFolderPath, "destinationFolderPath");

        return copyFileToFolder(sourceFile.getPath(), destinationFolderPath);
    }

    /**
     * Copy the file at the provided sourceFilePath to a file with the same name in the provided
     * destinationFolder.
     * @param sourceFilePath The path to the file to copy.
     * @param destinationFolder The folder that the file will be copied to.
     * @return The result of copying the file.
     */
    public default Result<Void> copyFileToFolder(Path sourceFilePath, Folder destinationFolder)
    {
        FileSystem.validateRootedFilePath(sourceFilePath, "sourceFilePath");
        PreCondition.assertNotNull(destinationFolder, "destinationFolder");

        return copyFileToFolder(sourceFilePath, destinationFolder.getPath());
    }

    /**
     * Copy the file at the provided sourceFilePath to a file with the same name in the provided
     * destinationFolderPath.
     * @param sourceFilePath The path to the file to copy.
     * @param destinationFolderPath The folder that the file will be copied to.
     * @return The result of copying the file.
     */
    public default Result<Void> copyFileToFolder(Path sourceFilePath, Path destinationFolderPath)
    {
        FileSystem.validateRootedFilePath(sourceFilePath, "sourceFilePath");
        FileSystem.validateRootedFolderPath(destinationFolderPath, "destinationFolderPath");

        final String fileName = sourceFilePath.getSegments().last().await();
        final Path destinationFilePath = destinationFolderPath.concatenateSegments(fileName);
        return copyFileTo(sourceFilePath, destinationFilePath);
    }

    /**
     * Validate that the provided rootedFolderPath {@link String} is a rooted folder path.
     * @param rootedFolderPath The {@link String} path to validate.
     */
    public static void validateRootedFolderPath(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath, "rootedFolderPath");
    }

    /**
     * Validate that the provided rootedFolderPath {@link String} is a rooted folder path. If the
     * provided {@link String} is not a rooted folder path, then an error will be thrown using the
     * provided expressionName.
     * @param rootedFolderPath The {@link String} path to validate.
     */
    public static void validateRootedFolderPath(String rootedFolderPath, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, expressionName);
        FileSystem.validateRootedFolderPath(Path.parse(rootedFolderPath), expressionName);
    }

    public static void validateRootedFolderPath(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath, "rootedFolderPath");
    }

    static void validateRootedFolderPath(Path rootedFolderPath, String expressionName)
    {
        PreCondition.assertNotNull(rootedFolderPath, expressionName);
        PreCondition.assertTrue(rootedFolderPath.isRooted(), expressionName + ".isRooted()");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFolderPath), "containsInvalidCharacters(" + expressionName + ")");
    }

    static void validateRootedFilePath(String rootedFilePath)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFilePath, "rootedFilePath");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), "rootedFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), "rootedFilePath.endsWith(\"/\")");
    }

    static void validateRootedFilePath(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath, "rootedFilePath");
    }

    static void validateRootedFilePath(Path rootedFilePath, String expressionName)
    {
        PreCondition.assertNotNull(rootedFilePath, expressionName);
        PreCondition.assertTrue(rootedFilePath.isRooted(), expressionName + ".isRooted()");
        PreCondition.assertFalse(rootedFilePath.endsWith("\\"), expressionName + ".endsWith(\"\\\")");
        PreCondition.assertFalse(rootedFilePath.endsWith("/"), expressionName + ".endsWith(\"/\")");
        PreCondition.assertFalse(containsInvalidCharacters(rootedFilePath), "containsInvalidCharacters(" + expressionName + "(" + Strings.escapeAndQuote(rootedFilePath.toString()) + "))");
    }

    char[] invalidCharacters = new char[]
    {
        '\u0000',
        '?',
        '<',
        '>',
        '|',
        '*',
        '\"',
        ':'
    };
    static boolean containsInvalidCharacters(Path path)
    {
        PreCondition.assertNotNull(path, "path");

        return path.withoutRoot()
            .then((Path pathWithoutRoot) ->
            {
                final String pathString = pathWithoutRoot.toString();
                return Strings.containsAny(pathString, invalidCharacters);
            })
            .catchError(NotFoundException.class, () -> false)
            .await();
    }

    static void preOrderEntriesTraversal(TraversalActions<Folder,FileSystemEntry> actions, Folder currentFolder)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<FileSystemEntry> entries = currentFolder.iterateEntries().toList();
        actions.returnValues(entries);
        actions.visitNodes(entries.instanceOf(Folder.class));
    }

    static void postOrderEntriesTraversal(TraversalActions<Folder,FileSystemEntry> actions, Folder currentFolder)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<FileSystemEntry> entries = currentFolder.iterateEntries().toList();
        actions.visitNodes(entries.instanceOf(Folder.class));
        actions.returnValues(entries);
    }

    static void preOrderFilesTraversal(TraversalActions<Folder,File> actions, Folder currentFolder)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<FileSystemEntry> entries = currentFolder.iterateEntries().toList();
        actions.returnValues(entries.instanceOf(File.class));
        actions.visitNodes(entries.instanceOf(Folder.class));
    }

    static void postOrderFilesTraversal(TraversalActions<Folder,File> actions, Folder currentFolder)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<FileSystemEntry> entries = currentFolder.iterateEntries().toList();
        actions.visitNodes(entries.instanceOf(Folder.class));
        actions.returnValues(entries.instanceOf(File.class));
    }

    static void preOrderFoldersTraversal(TraversalActions<Folder,Folder> actions, Folder currentFolder)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<Folder> folders = currentFolder.iterateEntries().instanceOf(Folder.class).toList();
        actions.returnValues(folders);
        actions.visitNodes(folders);
    }

    static void postOrderFoldersTraversal(TraversalActions<Folder,Folder> actions, Folder currentFolder)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<Folder> folders = currentFolder.iterateEntries().instanceOf(Folder.class).toList();
        actions.visitNodes(folders);
        actions.returnValues(folders);
    }
}
