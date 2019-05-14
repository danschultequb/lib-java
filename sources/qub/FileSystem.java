package qub;

/**
 * The FileSystem class provides access to files, folders, and roots within a device's file system.
 */
public interface FileSystem
{
    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default Result<Boolean> rootExists(String rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return rootExists(Path.parse(rootPath));
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default Result<Boolean> rootExists(Path rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return getRoots()
            .then((Iterable<Root> roots) ->
            {
                final Path root = rootPath.getRoot().await();
                return roots.map(Root::getPath).contains(root);
            });
    }

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    default Result<Root> getRoot(String rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return getRoot(Path.parse(rootPath));
    }

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    default Result<Root> getRoot(Path rootPath)
    {
        FileSystem.validateRootedFolderPath(rootPath, "rootPath");

        return rootPath.getRoot()
            .then((Path root) -> new Root(this, root));
    }

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    Result<Iterable<Root>> getRoots();

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    default Result<Iterable<FileSystemEntry>> getFilesAndFolders(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFolders(Path.parse(rootedFolderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    default Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFoldersRecursively(Path.parse(rootedFolderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    default Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return rootedFolderPath.resolve()
            .thenResult(this::getFolder)
            .then((Folder folder) ->
            {
                List<FileSystemEntry> result = null;

                final Iterable<FileSystemEntry> folderEntries = folder.getFilesAndFolders()
                    .await();

                if (folderEntries != null)
                {
                    result = List.create();

                    final Queue<Folder> foldersToVisit = new ArrayListQueue<>();
                    foldersToVisit.enqueue(folder);

                    while (foldersToVisit.any())
                    {
                        final Folder currentFolder = foldersToVisit.dequeue();
                        final Iterable<FileSystemEntry> currentFolderEntries = currentFolder.getFilesAndFolders().await();
                        for (final FileSystemEntry entry : currentFolderEntries)
                        {
                            result.add(entry);
                            if (entry instanceof Folder)
                            {
                                foldersToVisit.enqueue((Folder)entry);
                            }
                        }
                    }
                }

                return result;
            });
    }

    /**
     * Get the folders at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    default Result<Iterable<Folder>> getFolders(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFolders(Path.parse(rootedFolderPath));
    }

    /**
     * Get the folders at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    default Result<Iterable<Folder>> getFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFolders(rootedFolderPath)
            .then((Iterable<FileSystemEntry> entries) -> entries.instanceOf(Folder.class));
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    default Result<Iterable<Folder>> getFoldersRecursively(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFoldersRecursively(Path.parse(rootedFolderPath));
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    default Result<Iterable<Folder>> getFoldersRecursively(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFoldersRecursively(rootedFolderPath)
            .then((Iterable<FileSystemEntry> entries) -> entries.instanceOf(Folder.class));
    }

    /**
     * Get the files at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    default Result<Iterable<File>> getFiles(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFiles(Path.parse(rootedFolderPath));
    }

    /**
     * Get the files at the provided folder path.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    default Result<Iterable<File>> getFiles(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFolders(rootedFolderPath)
            .then((Iterable<FileSystemEntry> entries) -> entries.instanceOf(File.class));
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    default Result<Iterable<File>> getFilesRecursively(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesRecursively(Path.parse(rootedFolderPath));
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    default Result<Iterable<File>> getFilesRecursively(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFilesAndFoldersRecursively(rootedFolderPath)
            .then((Iterable<FileSystemEntry> entries) -> entries.instanceOf(File.class));
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param rootedFolderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    default Result<Folder> getFolder(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return getFolder(Path.parse(rootedFolderPath));
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
            .then((Path resolvedRootedFolderPath) -> new Folder(this, resolvedRootedFolderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param rootedFolderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default Result<Boolean> folderExists(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return folderExists(Path.parse(rootedFolderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param rootedFolderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    Result<Boolean> folderExists(Path rootedFolderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param rootedFolderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    default Result<Folder> createFolder(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return createFolder(Path.parse(rootedFolderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param rootedFolderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    Result<Folder> createFolder(Path rootedFolderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param rootedFolderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    default Result<Void> deleteFolder(String rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        return deleteFolder(Path.parse(rootedFolderPath));
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

        return getFile(Path.parse(rootedFilePath));
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

        return fileExists(Path.parse(rootedFilePath));
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

        return createFile(Path.parse(rootedFilePath));
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

        return deleteFile(Path.parse(rootedFilePath));
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
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    default Result<ByteReadStream> getFileContentByteReadStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath);

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    default Result<CharacterReadStream> getFileContentCharacterReadStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentCharacterReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    default Result<CharacterReadStream> getFileContentCharacterReadStream(Path rootedFilePath)
    {
        return getFileContentByteReadStream(rootedFilePath)
            .then((ByteReadStream byteReadStream) -> byteReadStream.asCharacterReadStream());
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The byte[] contents of the file at the provided rootedFilePath.
     */
    default Result<byte[]> getFileContent(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContent(Path.parse(rootedFilePath));
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The byte[] contents of the file at the provided rootedFilePath.
     */
    default Result<byte[]> getFileContent(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentByteReadStream(rootedFilePath)
            .then((ByteReadStream byteReadStream) ->
            {
                byte[] result;
                try
                {
                    result = byteReadStream.readAllBytes()
                        .catchError(EndOfStreamException.class, () -> new byte[0])
                        .await();
                }
                finally
                {
                    byteReadStream.dispose().await();
                }
                return result;
            });
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The String contents of the file at the provided rootedFilePath.
     */
    default Result<String> getFileContentAsString(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentAsString(Path.parse(rootedFilePath));
    }

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The String contents of the file at the provided rootedFilePath.
     */
    default Result<String> getFileContentAsString(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContent(rootedFilePath).thenResult(CharacterEncoding.UTF_8::decodeAsString);
    }

    /**
     * Get a ByteWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteWriteStream to the contents of the file.
     */
    default Result<ByteWriteStream> getFileContentByteWriteStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentByteWriteStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath);

    /**
     * Get a CharacterWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterWriteStream to the contents of the file.
     */
    default Result<CharacterWriteStream> getFileContentCharacterWriteStream(String rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentCharacterWriteStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a CharacterWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterWriteStream to the contents of the file.
     */
    default Result<CharacterWriteStream> getFileContentCharacterWriteStream(Path rootedFilePath)
    {
        return getFileContentByteWriteStream(rootedFilePath)
            .then((ByteWriteStream writeStream) -> writeStream.asCharacterWriteStream());
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param content The byte[] contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<Void> setFileContent(String rootedFilePath, byte[] content)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return setFileContent(Path.parse(rootedFilePath), content);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param content The byte[] contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<Void> setFileContent(Path rootedFilePath, byte[] content)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return getFileContentByteWriteStream(rootedFilePath)
            .then((ByteWriteStream byteWriteStream) ->
            {
                try
                {
                    if (content != null && content.length > 0)
                    {
                        byteWriteStream.writeAllBytes(content).await();
                    }
                }
                finally
                {
                    byteWriteStream.dispose().await();
                }
            });
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param content The String contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<Void> setFileContentAsString(String rootedFilePath, String content)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return setFileContentAsString(Path.parse(rootedFilePath), content);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param content The String contents to set.
     * @return Whether or not the file's contents were set.
     */
    default Result<Void> setFileContentAsString(Path rootedFilePath, String content)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        return CharacterEncoding.UTF_8.encode(content)
            .thenResult((byte[] encodedContent) -> setFileContent(rootedFilePath, encodedContent));
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

        return copyFileTo(sourceFile.getPath(), destinationFile.getPath());
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

        return copyFileTo(sourceFile.getPath(), destinationFilePath);
    }

    /**
     * Copy the file at the provided sourceFilePath to the provided destinationFile.
     * @param sourceFilePath The path to the file to copy.
     * @param destinationFile The file to copy to.
     * @return The result of copying the file.
     */
    default Result<Void> copyFileTo(Path sourceFilePath, File destinationFile)
    {
        FileSystem.validateRootedFilePath(sourceFilePath, "sourceFilePath");
        PreCondition.assertNotNull(destinationFile, "destinationFile");

        return copyFileTo(sourceFilePath, destinationFile.getPath());
    }

    /**
     * Copy the file at the provided sourceFilePath to the provided destinationFilePath.
     * @param sourceFilePath The path to the file to copy.
     * @param destinationFilePath The path to copy the file to.
     * @return The result of copying the file.
     */
    default Result<Void> copyFileTo(Path sourceFilePath, Path destinationFilePath)
    {
        FileSystem.validateRootedFilePath(sourceFilePath, "sourceFilePath");
        FileSystem.validateRootedFilePath(destinationFilePath, "destinationFilePath");

        return Result.create(() ->
        {
            try (final ByteReadStream sourceStream = getFileContentByteReadStream(sourceFilePath).await())
            {
                try (final ByteWriteStream destinationStream = getFileContentByteWriteStream(destinationFilePath).await())
                {
                    destinationStream.writeAllBytes(sourceStream).await();
                }
            }
        });
    }

    static void validateRootedFolderPath(String rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath, "rootedFolderPath");
    }

    static void validateRootedFolderPath(String rootedFolderPath, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(rootedFolderPath, expressionName);
    }

    static void validateRootedFolderPath(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath, "rootedFolderPath");
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
}
