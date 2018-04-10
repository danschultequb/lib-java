package qub;

/**
 * The FileSystem class provides access to files, folders, and roots within a device's file system.
 */
public interface FileSystem
{
    /**
     * Get the AsyncRunner that this FileSystem object will use to schedule its asynchronous
     * operations.
     * @return The AsyncRunner to use to schedule asynchronous operations.
     */
    AsyncRunner getAsyncRunner();

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    AsyncFunction<Result<Boolean>> rootExists(String rootPath);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    AsyncFunction<Result<Boolean>> rootExists(Path rootPath);

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    Result<Root> getRoot(String rootPath);

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    Result<Root> getRoot(Path rootPath);

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    Result<Iterable<Root>> getRoots();

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    AsyncFunction<Result<Iterable<Root>>> getRootsAsync();

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    Result<Iterable<FileSystemEntry>> getFilesAndFolders(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(Path folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    Result<Iterable<Folder>> getFolders(String folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    Result<Iterable<Folder>> getFolders(Path folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(String folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(Path folderPath);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    Result<Iterable<Folder>> getFoldersRecursively(String folderPath);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    Result<Iterable<Folder>> getFoldersRecursively(Path folderPath);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(String folderPath);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(Path folderPath);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    AsyncFunction<Result<Iterable<File>>> getFiles(String folderPath);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    AsyncFunction<Result<Iterable<File>>> getFiles(Path folderPath);

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    AsyncFunction<Result<Iterable<File>>> getFilesRecursively(String folderPath);

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    AsyncFunction<Result<Iterable<File>>> getFilesRecursively(Path folderPath);

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    Result<Folder> getFolder(String folderPath);

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    Result<Folder> getFolder(Path folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    AsyncFunction<Result<Boolean>> folderExists(String folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    AsyncFunction<Result<Boolean>> folderExists(Path folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    Result<Folder> createFolder(String folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    Result<Folder> createFolder(Path folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Result<Folder>> createFolderAsync(String folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Result<Folder>> createFolderAsync(Path folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    Result<Boolean> deleteFolder(String folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    Result<Boolean> deleteFolder(Path folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    AsyncFunction<Result<Boolean>> deleteFolderAsync(String folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    AsyncFunction<Result<Boolean>> deleteFolderAsync(Path folderPath);

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    Result<File> getFile(String filePath);

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    Result<File> getFile(Path filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    AsyncFunction<Result<Boolean>> fileExists(String filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    AsyncFunction<Result<Boolean>> fileExists(Path filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    Result<File> createFile(String filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    Result<File> createFile(Path filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Result<File>> createFileAsync(String filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Result<File>> createFileAsync(Path filePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    AsyncFunction<Result<Boolean>> deleteFile(String filePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    AsyncFunction<Result<Boolean>> deleteFile(Path filePath);

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param filePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    AsyncFunction<Result<DateTime>> getFileLastModified(String filePath);

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param filePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    AsyncFunction<Result<DateTime>> getFileLastModified(Path filePath);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(String rootedFilePath);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(Path rootedFilePath);

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The byte[] contents of the file at the provided rootedFilePath.
     */
    AsyncFunction<Result<byte[]>> getFileContent(String rootedFilePath);

    /**
     * Get the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return The byte[] contents of the file at the provided rootedFilePath.
     */
    AsyncFunction<Result<byte[]>> getFileContent(Path rootedFilePath);

    /**
     * Get a ByteWriteStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteWriteStream to the contents of the file.
     */
    AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(String rootedFilePath);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(Path rootedFilePath);

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param content The byte[] contents to set.
     * @return Whether or not the file's contents were set.
     */
    AsyncFunction<Result<Boolean>> setFileContent(String rootedFilePath, byte[] content);

    /**
     * Set the contents of the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted path to the file.
     * @param content The byte[] contents to set.
     * @return Whether or not the file's contents were set.
     */
    AsyncFunction<Result<Boolean>> setFileContent(Path rootedFilePath, byte[] content);
}
