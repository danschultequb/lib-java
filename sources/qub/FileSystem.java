package qub;

/**
 * The FileSystem class provides access to files, folders, and roots within a device's file system.
 */
public interface FileSystem
{
    /**
     * Set the AsyncRunner that this FileSystem object will use to schedule its asynchronous
     * operations.
     * @param asyncRunner The AsyncRunner to use to schedule asynchronous operations.
     */
    void setAsyncRunner(AsyncRunner asyncRunner);

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
    boolean rootExists(String rootPath);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    boolean rootExists(String rootPath, Action1<String> onError);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    boolean rootExists(Path rootPath);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    boolean rootExists(Path rootPath, Action1<String> onError);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> rootExistsAsync(String rootPath);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> rootExistsAsync(String rootPath, Action1<String> onError);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> rootExistsAsync(Path rootPath);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> rootExistsAsync(Path rootPath, Action1<String> onError);

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    Root getRoot(String rootPath);

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    Root getRoot(Path rootPath);

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    Iterable<Root> getRoots();

    /**
     * Get the roots of this FileSystem.
     * @param onError The action that will be called when an error occurs.
     * @return The roots of this FileSystem.
     */
    Iterable<Root> getRoots(Action1<String> onError);

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    AsyncFunction<Iterable<Root>> getRootsAsync();

    /**
     * Get the roots of this FileSystem.
     * @param onError The action that will be called when an error occurs.
     * @return The roots of this FileSystem.
     */
    AsyncFunction<Iterable<Root>> getRootsAsync(Action1<String> onError);

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    Iterable<FileSystemEntry> getFilesAndFolders(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path.
     */
    Iterable<FileSystemEntry> getFilesAndFolders(String folderPath, Action1<String> onError);

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder Path.
     */
    Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath, Action1<String> onError);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath, Action1<String> onError);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath, Action1<String> onError);

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath);

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path.
     */
    AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath, Action1<String> onError);

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath);

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder Path.
     */
    AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath, Action1<String> onError);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    Iterable<Folder> getFolders(String folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    Iterable<Folder> getFolders(String folderPath, Action1<String> onError);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    Iterable<Folder> getFolders(Path folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    Iterable<Folder> getFolders(Path folderPath, Action1<String> onError);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    Iterable<Folder> getFoldersRecursively(String folderPath);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path and its subfolders.
     */
    Iterable<Folder> getFoldersRecursively(String folderPath, Action1<String> onError);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    Iterable<Folder> getFoldersRecursively(Path folderPath);

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path and its subfolders.
     */
    Iterable<Folder> getFoldersRecursively(Path folderPath, Action1<String> onError);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath, Action1<String> onError);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath);

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath, Action1<String> onError);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    Iterable<File> getFiles(String folderPath);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    Iterable<File> getFiles(String folderPath, Action1<String> onError);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    Iterable<File> getFiles(Path folderPath);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    Iterable<File> getFiles(Path folderPath, Action1<String> onError);

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    Iterable<File> getFilesRecursively(String folderPath);

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path and its subfolders.
     */
    Iterable<File> getFilesRecursively(String folderPath, Action1<String> onError);

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    Iterable<File> getFilesRecursively(Path folderPath);

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path and its subfolders.
     */
    Iterable<File> getFilesRecursively(Path folderPath, Action1<String> onError);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    AsyncFunction<Iterable<File>> getFilesAsync(String folderPath);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    AsyncFunction<Iterable<File>> getFilesAsync(String folderPath, Action1<String> onError);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath);

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath, Action1<String> onError);

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    Folder getFolder(String folderPath);

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    Folder getFolder(Path folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    boolean folderExists(String folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    boolean folderExists(String folderPath, Action1<String> onError);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    boolean folderExists(Path folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    boolean folderExists(Path folderPath, Action1<String> onError);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> folderExistsAsync(String folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> folderExistsAsync(String folderPath, Action1<String> onError);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> folderExistsAsync(Path folderPath);

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> folderExistsAsync(Path folderPath, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(String folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(String folderPath, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(String folderPath, Out<Folder> outputFolder);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(String folderPath, Out<Folder> outputFolder, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(Path folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(Path folderPath, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(Path folderPath, Out<Folder> outputFolder);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    boolean createFolder(Path folderPath, Out<Folder> outputFolder, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(String folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(String folderPath, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(Path folderPath);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(Path folderPath, Action1<String> onError);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder);

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder, Action1<String> onError);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    boolean deleteFolder(String folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    boolean deleteFolder(String folderPath, Action1<String> onError);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    boolean deleteFolder(Path folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    boolean deleteFolder(Path folderPath, Action1<String> onError);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    AsyncFunction<Boolean> deleteFolderAsync(String folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    AsyncFunction<Boolean> deleteFolderAsync(String folderPath, Action1<String> onError);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    AsyncFunction<Boolean> deleteFolderAsync(Path folderPath);

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    AsyncFunction<Boolean> deleteFolderAsync(Path folderPath, Action1<String> onError);

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    File getFile(String filePath);

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    File getFile(Path filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    boolean fileExists(String filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    boolean fileExists(String filePath, Action1<String> onError);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    boolean fileExists(Path filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    boolean fileExists(Path filePath, Action1<String> onError);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> fileExistsAsync(String filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> fileExistsAsync(String filePath, Action1<String> onError);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> fileExistsAsync(Path filePath);

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    AsyncFunction<Boolean> fileExistsAsync(Path filePath, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, byte[] fileContents);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, byte[] fileContents, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, CharacterEncoding encoding);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, Out<File> outputFile);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, Out<File> outputFile);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, byte[] fileContents);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, byte[] fileContents, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, Out<File> outputFile);

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, byte[] fileContents, Out<File> outputFile);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, Out<File> outputFile);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile);

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(String filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(String filePath, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(Path filePath);

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(Path filePath, Action1<String> onError);

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(Path filePath, Out<File> outputFile);

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    AsyncFunction<Boolean> createFileAsync(Path filePath, Out<File> outputFile, Action1<String> onError);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    boolean deleteFile(String filePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    boolean deleteFile(String filePath, Action1<String> onError);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    boolean deleteFile(Path filePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    boolean deleteFile(Path filePath, Action1<String> onError);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    AsyncFunction<Boolean> deleteFileAsync(String filePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    AsyncFunction<Boolean> deleteFileAsync(String filePath, Action1<String> onError);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    AsyncFunction<Boolean> deleteFileAsync(Path filePath);

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    AsyncFunction<Boolean> deleteFileAsync(Path filePath, Action1<String> onError);

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param filePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    DateTime getFileLastModified(String filePath);

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param filePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    DateTime getFileLastModified(Path filePath);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    byte[] getFileContents(String rootedFilePath);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    byte[] getFileContents(String rootedFilePath, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    byte[] getFileContents(Path rootedFilePath);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    byte[] getFileContents(Path rootedFilePath, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(String rootedFilePath);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(String rootedFilePath, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(Path rootedFilePath);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(Path rootedFilePath, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding);

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    Iterable<byte[]> getFileContentBlocks(String rootedFilePath);

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    Iterable<byte[]> getFileContentBlocks(String rootedFilePath, Action1<String> onError);

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    Iterable<byte[]> getFileContentBlocks(Path rootedFilePath);

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    Iterable<byte[]> getFileContentBlocks(Path rootedFilePath, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding);

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    ByteReadStream getFileContentByteReadStream(String rootedFilePath);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A ByteReadStream to the contents of the file.
     */
    ByteReadStream getFileContentByteReadStream(String rootedFilePath, Action1<String> onError);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    ByteReadStream getFileContentByteReadStream(Path rootedFilePath);

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A ByteReadStream to the contents of the file.
     */
    ByteReadStream getFileContentByteReadStream(Path rootedFilePath, Action1<String> onError);

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath);

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A CharacterReadStream to the contents of the file.
     */
    CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath, Action1<String> onError);

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath);

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A CharacterReadStream to the contents of the file.
     */
    CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath, Action1<String> onError);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(String rootedFilePath, byte[] fileContents);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(String rootedFilePath, byte[] fileContents, Action1<String> onError);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(Path rootedFilePath, byte[] fileContents);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(Path rootedFilePath, byte[] fileContents, Action1<String> onError);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(String rootedFilePath, String fileContents);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(String rootedFilePath, String fileContents, Action1<String> onError);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(Path rootedFilePath, String fileContents);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(Path rootedFilePath, String fileContents, Action1<String> onError);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding);

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError);
}
