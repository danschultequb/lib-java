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
    default boolean rootExists(String rootPath)
    {
        return rootExists(Path.parse(rootPath));
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default boolean rootExists(String rootPath, Action1<String> onError)
    {
        return rootExists(Path.parse(rootPath), onError);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default boolean rootExists(Path rootPath)
    {
        return rootExists(rootPath, null);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default boolean rootExists(Path rootPath, Action1<String> onError)
    {
        boolean result = false;
        if (rootPath != null && rootPath.isRooted())
        {
            final Path onlyRootPath = rootPath.getRootPath();
            result = getRoots(onError).contains((Root root) -> root.getPath().equals(onlyRootPath));
        }
        return result;
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> rootExistsAsync(String rootPath)
    {
        return rootExistsAsync(Path.parse(rootPath));
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> rootExistsAsync(String rootPath, Action1<String> onError)
    {
        return rootExistsAsync(Path.parse(rootPath), onError);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> rootExistsAsync(Path rootPath)
    {
        return rootExistsAsync(rootPath, null);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> rootExistsAsync(Path rootPath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> rootExists(rootPath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    default Root getRoot(String rootPath)
    {
        return getRoot(Path.parse(rootPath));
    }

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    default Root getRoot(Path rootPath)
    {
        return new Root(this, rootPath);
    }

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    default Iterable<Root> getRoots()
    {
        return getRoots(null);
    }

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
    default AsyncFunction<Iterable<Root>> getRootsAsync()
    {
        return getRootsAsync(null);
    }

    /**
     * Get the roots of this FileSystem.
     * @param onError The action that will be called when an error occurs.
     * @return The roots of this FileSystem.
     */
    default AsyncFunction<Iterable<Root>> getRootsAsync(Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> getRoots(onError))
            .thenOn(currentRunner);
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    default Iterable<FileSystemEntry> getFilesAndFolders(String folderPath)
    {
        return getFilesAndFolders(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path.
     */
    default Iterable<FileSystemEntry> getFilesAndFolders(String folderPath, Action1<String> onError)
    {
        return getFilesAndFolders(Path.parse(folderPath), onError);
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    default Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        return getFilesAndFolders(folderPath, null);
    }

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
    default Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath)
    {
        return getFilesAndFoldersRecursively(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    default Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath, Action1<String> onError)
    {
        return getFilesAndFoldersRecursively(Path.parse(folderPath), onError);
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    default Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath)
    {
        return getFilesAndFoldersRecursively(folderPath, null);
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    default Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath, Action1<String> onError)
    {
        List<FileSystemEntry> result = new ArrayList<>();

        final Folder folder = getFolder(folderPath);
        if (folder != null && folder.exists())
        {
            final Queue<Folder> foldersToVisit = new SingleLinkListQueue<>();
            foldersToVisit.enqueue(getFolder(folderPath));

            while (foldersToVisit.any())
            {
                final Folder currentFolder = foldersToVisit.dequeue();
                final Iterable<FileSystemEntry> currentFolderEntries = currentFolder.getFilesAndFolders(onError);
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
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    default AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath)
    {
        return getFilesAndFoldersAsync(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path.
     */
    default AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath, Action1<String> onError)
    {
        return getFilesAndFoldersAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    default AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath)
    {
        return getFilesAndFoldersAsync(folderPath, null);
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder Path.
     */
    default AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> getFilesAndFolders(folderPath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    default Iterable<Folder> getFolders(String folderPath)
    {
        return getFolders(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    default Iterable<Folder> getFolders(String folderPath, Action1<String> onError)
    {
        return getFolders(Path.parse(folderPath), onError);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    default Iterable<Folder> getFolders(Path folderPath)
    {
        return getFolders(folderPath, null);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    default Iterable<Folder> getFolders(Path folderPath, Action1<String> onError)
    {
        return getFilesAndFolders(folderPath, onError).instanceOf(Folder.class);
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    default Iterable<Folder> getFoldersRecursively(String folderPath)
    {
        return getFoldersRecursively(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path and its subfolders.
     */
    default Iterable<Folder> getFoldersRecursively(String folderPath, Action1<String> onError)
    {
        return getFoldersRecursively(Path.parse(folderPath), onError);
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    default Iterable<Folder> getFoldersRecursively(Path folderPath)
    {
        return getFoldersRecursively(folderPath, null);
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path and its subfolders.
     */
    default Iterable<Folder> getFoldersRecursively(Path folderPath, Action1<String> onError)
    {
        return getFilesAndFoldersRecursively(folderPath, onError).instanceOf(Folder.class);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    default AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return getFoldersAsync(path);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    default AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath, Action1<String> onError)
    {
        final Path path = Path.parse(folderPath);
        return getFoldersAsync(path, onError);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    default AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath)
    {
        return getFoldersAsync(folderPath, null);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    default AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> getFolders(folderPath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    default Iterable<File> getFiles(String folderPath)
    {
        return getFiles(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    default Iterable<File> getFiles(String folderPath, Action1<String> onError)
    {
        return getFiles(Path.parse(folderPath), onError);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    default Iterable<File> getFiles(Path folderPath)
    {
        return getFiles(folderPath, null);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    default Iterable<File> getFiles(Path folderPath, Action1<String> onError)
    {
        return getFilesAndFolders(folderPath, onError).instanceOf(File.class);
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    default Iterable<File> getFilesRecursively(String folderPath)
    {
        return getFilesRecursively(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path and its subfolders.
     */
    default Iterable<File> getFilesRecursively(String folderPath, Action1<String> onError)
    {
        return getFilesRecursively(Path.parse(folderPath), onError);
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    default Iterable<File> getFilesRecursively(Path folderPath)
    {
        return getFilesRecursively(folderPath, null);
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path and its subfolders.
     */
    default Iterable<File> getFilesRecursively(Path folderPath, Action1<String> onError)
    {
        return getFilesAndFoldersRecursively(folderPath, onError).instanceOf(File.class);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    default AsyncFunction<Iterable<File>> getFilesAsync(String folderPath)
    {
        return getFilesAsync(Path.parse(folderPath), null);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    default AsyncFunction<Iterable<File>> getFilesAsync(String folderPath, Action1<String> onError)
    {
        return getFilesAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    default AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath)
    {
        return getFilesAsync(folderPath, null);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    default AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> getFiles(folderPath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    default Folder getFolder(String folderPath)
    {
        return getFolder(Path.parse(folderPath));
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    default Folder getFolder(Path folderPath)
    {
        return folderPath == null || !folderPath.isRooted() ? null : new Folder(this, folderPath);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default boolean folderExists(String folderPath)
    {
        return folderExists(Path.parse(folderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default boolean folderExists(String folderPath, Action1<String> onError)
    {
        return folderExists(Path.parse(folderPath), onError);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default boolean folderExists(Path folderPath)
    {
        return folderExists(folderPath, null);
    }

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
    default AsyncFunction<Boolean> folderExistsAsync(String folderPath)
    {
        return folderExistsAsync(Path.parse(folderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> folderExistsAsync(String folderPath, Action1<String> onError)
    {
        return folderExistsAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> folderExistsAsync(Path folderPath)
    {
        return folderExistsAsync(folderPath, null);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> folderExistsAsync(Path folderPath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> folderExists(folderPath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(String folderPath)
    {
        return createFolder(Path.parse(folderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(String folderPath, Action1<String> onError)
    {
        return createFolder(Path.parse(folderPath), onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(String folderPath, Out<Folder> outputFolder)
    {
        return createFolder(Path.parse(folderPath), outputFolder);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return createFolder(Path.parse(folderPath), outputFolder, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(Path folderPath)
    {
        return createFolder(folderPath, null, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(Path folderPath, Action1<String> onError)
    {
        return createFolder(folderPath, null, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    default boolean createFolder(Path folderPath, Out<Folder> outputFolder)
    {
        return createFolder(folderPath, outputFolder, null);
    }

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
    default AsyncFunction<Boolean> createFolderAsync(String folderPath)
    {
        return createFolderAsync(Path.parse(folderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(String folderPath, Action1<String> onError)
    {
        return createFolderAsync(Path.parse(folderPath), onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder)
    {
        return createFolderAsync(Path.parse(folderPath), outputFolder);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return createFolderAsync(Path.parse(folderPath), outputFolder, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(Path folderPath)
    {
        return createFolderAsync(folderPath, null, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(Path folderPath, Action1<String> onError)
    {
        return createFolderAsync(folderPath, null, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder)
    {
        return createFolderAsync(folderPath, outputFolder, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    default AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> createFolder(folderPath, outputFolder, onError))
            .thenOn(currentRunner);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    default boolean deleteFolder(String folderPath)
    {
        return deleteFolder(Path.parse(folderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    default boolean deleteFolder(String folderPath, Action1<String> onError)
    {
        return deleteFolder(Path.parse(folderPath), onError);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    default boolean deleteFolder(Path folderPath)
    {
        return deleteFolder(folderPath, null);
    }

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
    default AsyncFunction<Boolean> deleteFolderAsync(String folderPath)
    {
        return deleteFolderAsync(Path.parse(folderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    default AsyncFunction<Boolean> deleteFolderAsync(String folderPath, Action1<String> onError)
    {
        return deleteFolderAsync(Path.parse(folderPath), onError);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    default AsyncFunction<Boolean> deleteFolderAsync(Path folderPath)
    {
        return deleteFolderAsync(folderPath, null);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    default AsyncFunction<Boolean> deleteFolderAsync(Path folderPath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> deleteFolder(folderPath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    default File getFile(String filePath)
    {
        return getFile(Path.parse(filePath));
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    default File getFile(Path filePath)
    {
        return filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") ? null : new File(this, filePath);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default boolean fileExists(String filePath)
    {
        return fileExists(Path.parse(filePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default boolean fileExists(String filePath, Action1<String> onError)
    {
        return fileExists(Path.parse(filePath), onError);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default boolean fileExists(Path filePath)
    {
        return fileExists(filePath, null);
    }

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
    default AsyncFunction<Boolean> fileExistsAsync(String filePath)
    {
        return fileExistsAsync(Path.parse(filePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> fileExistsAsync(String filePath, Action1<String> onError)
    {
        return fileExistsAsync(Path.parse(filePath), onError);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> fileExistsAsync(Path filePath)
    {
        return fileExistsAsync(filePath, null);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    default AsyncFunction<Boolean> fileExistsAsync(Path filePath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> fileExists(filePath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath)
    {
        return createFile(Path.parse(filePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, byte[] fileContents)
    {
        return createFile(Path.parse(filePath), fileContents);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, byte[] fileContents, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, String fileContents)
    {
        return createFile(Path.parse(filePath), fileContents);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, String fileContents, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, String fileContents, CharacterEncoding encoding)
    {
        return createFile(Path.parse(filePath), fileContents, encoding);
    }

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
    default boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, encoding, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, Out<File> outputFile)
    {
        return createFile(Path.parse(filePath), outputFile);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), outputFile, onError);
    }

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
    default boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile);
    }

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
    default boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile, onError);
    }

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
    default boolean createFile(String filePath, String fileContents, Out<File> outputFile)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile);
    }

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
    default boolean createFile(String filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile, onError);
    }

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
    default boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        return createFile(Path.parse(filePath), fileContents, encoding, outputFile);
    }

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
    default boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, encoding, outputFile, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath)
    {
        return createFile(filePath, (byte[])null);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, Action1<String> onError)
    {
        return createFile(filePath, (byte[])null, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, byte[] fileContents)
    {
        return createFile(filePath, fileContents, (Out<File>)null);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, byte[] fileContents, Action1<String> onError)
    {
        return createFile(filePath, fileContents, null, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, String fileContents)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, (Out<File>)null);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, String fileContents, Action1<String> onError)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, null, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding)
    {
        return createFile(filePath, fileContents, encoding, (Out<File>)null);
    }

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
    default boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return createFile(filePath, fileContents, encoding, null, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, Out<File> outputFile)
    {
        return createFile(filePath, (byte[])null, outputFile);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default boolean createFile(Path filePath, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(filePath, (byte[])null, outputFile, onError);
    }

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
    default boolean createFile(Path filePath, byte[] fileContents, Out<File> outputFile)
    {
        return createFile(filePath, fileContents, outputFile, null);
    }

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
    default boolean createFile(Path filePath, String fileContents, Out<File> outputFile)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, outputFile);
    }

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
    default boolean createFile(Path filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, outputFile, onError);
    }

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
    default boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return createFile(filePath, fileContentsBytes, outputFile);
    }

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
    default boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return createFile(filePath, fileContentsBytes, outputFile, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(String filePath)
    {
        return createFileAsync(Path.parse(filePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(String filePath, Action1<String> onError)
    {
        return createFileAsync(Path.parse(filePath), onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile)
    {
        return createFileAsync(Path.parse(filePath), outputFile);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return createFileAsync(Path.parse(filePath), outputFile, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(Path filePath)
    {
        return createFileAsync(filePath, (Out<File>)null);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(Path filePath, Action1<String> onError)
    {
        return createFileAsync(filePath, null, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(Path filePath, Out<File> outputFile)
    {
        return createFileAsync(filePath, outputFile, null);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    default AsyncFunction<Boolean> createFileAsync(Path filePath, Out<File> outputFile, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> createFile(filePath, outputFile, onError))
            .thenOn(currentRunner);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    default boolean deleteFile(String filePath)
    {
        return deleteFile(Path.parse(filePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    default boolean deleteFile(String filePath, Action1<String> onError)
    {
        return deleteFile(Path.parse(filePath), onError);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    default boolean deleteFile(Path filePath)
    {
        return deleteFile(filePath, null);
    }

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
    default AsyncFunction<Boolean> deleteFileAsync(String filePath)
    {
        return deleteFileAsync(Path.parse(filePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    default AsyncFunction<Boolean> deleteFileAsync(String filePath, Action1<String> onError)
    {
        return deleteFileAsync(Path.parse(filePath), onError);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    default AsyncFunction<Boolean> deleteFileAsync(Path filePath)
    {
        return deleteFileAsync(filePath, null);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    default AsyncFunction<Boolean> deleteFileAsync(Path filePath, Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return getAsyncRunner()
            .schedule(() -> deleteFile(filePath, onError))
            .thenOn(currentRunner);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default byte[] getFileContents(String rootedFilePath)
    {
        return getFileContents(Path.parse(rootedFilePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default byte[] getFileContents(String rootedFilePath, Action1<String> onError)
    {
        return getFileContents(Path.parse(rootedFilePath), onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default byte[] getFileContents(Path rootedFilePath)
    {
        return getFileContents(rootedFilePath, null);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default byte[] getFileContents(Path rootedFilePath, Action1<String> onError)
    {
        return Array.merge(getFileContentBlocks(rootedFilePath, onError));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(String rootedFilePath)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath), onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(Path rootedFilePath)
    {
        return getFileContentsAsString(rootedFilePath, CharacterEncoding.UTF_8);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(Path rootedFilePath, Action1<String> onError)
    {
        return getFileContentsAsString(rootedFilePath, CharacterEncoding.UTF_8, onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath), encoding);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath), encoding, onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentsAsString(rootedFilePath, encoding, null);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    default String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        String result = null;
        if (encoding != null)
        {
            final byte[] fileContents = getFileContents(rootedFilePath, onError);
            result = encoding.decode(fileContents);
        }
        return result;
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    default Iterable<byte[]> getFileContentBlocks(String rootedFilePath)
    {
        return getFileContentBlocks(Path.parse(rootedFilePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    default Iterable<byte[]> getFileContentBlocks(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentBlocks(Path.parse(rootedFilePath), onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    default Iterable<byte[]> getFileContentBlocks(Path rootedFilePath)
    {
        return getFileContentBlocks(rootedFilePath, null);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    default Iterable<byte[]> getFileContentBlocks(Path rootedFilePath, Action1<String> onError)
    {
        List<byte[]> result = null;

        try (final ByteReadStream fileByteReadStream = getFileContentByteReadStream(rootedFilePath, onError))
        {
            if (fileByteReadStream != null)
            {
                result = new ArrayList<>();

                final byte[] buffer = new byte[1024];
                int bytesRead;
                do
                {
                    bytesRead = fileByteReadStream.readBytes(buffer);
                    if (bytesRead > 0)
                    {
                        final byte[] byteBlock = Array.clone(buffer, 0, bytesRead);
                        result.add(byteBlock);
                    }
                }
                while (bytesRead >= 0);
            }
        }

        return result;
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(String rootedFilePath)
    {
        return getFileContentLines(Path.parse(rootedFilePath));
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(Path rootedFilePath)
    {
        return getFileContentLines(rootedFilePath, true);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(Path rootedFilePath, Action1<String> onError)
    {
        return getFileContentLines(rootedFilePath, true, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines);
    }

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
    default Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.UTF_8);
    }

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
    default Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.UTF_8, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentLines(Path.parse(rootedFilePath), encoding);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), encoding, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentLines(rootedFilePath, true, encoding);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    default Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentLines(rootedFilePath, true, encoding, onError);
    }

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
    default Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines, encoding);
    }

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
    default Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines, encoding, onError);
    }

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
    default Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, encoding, null);
    }

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
    default Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        Iterable<String> result = null;

        final String fileContents = getFileContentsAsString(rootedFilePath, encoding, onError);
        if (fileContents != null)
        {
            final List<String> lines = new ArrayList<>();
            final int fileContentsLength = fileContents.length();
            int lineStartIndex = 0;

            while (lineStartIndex < fileContentsLength)
            {
                final int newLineCharacterIndex = fileContents.indexOf('\n', lineStartIndex);
                if (newLineCharacterIndex < 0)
                {
                    lines.add(fileContents.substring(lineStartIndex));
                    lineStartIndex = fileContentsLength;
                }
                else
                {
                    String line = fileContents.substring(lineStartIndex, newLineCharacterIndex + 1);
                    if (!includeNewLines)
                    {
                        final int newLineWidth = line.endsWith("\r\n") ? 2 : 1;
                        line = line.substring(0, line.length() - newLineWidth);
                    }
                    lines.add(line);
                    lineStartIndex = newLineCharacterIndex + 1;
                }
            }

            result = lines;
        }

        return result;
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    default ByteReadStream getFileContentByteReadStream(String rootedFilePath)
    {
        return getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A ByteReadStream to the contents of the file.
     */
    default ByteReadStream getFileContentByteReadStream(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentByteReadStream(Path.parse(rootedFilePath), onError);
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    default ByteReadStream getFileContentByteReadStream(Path rootedFilePath)
    {
        return getFileContentByteReadStream(rootedFilePath, null);
    }

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
    default CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath)
    {
        return getFileContentCharacterReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A CharacterReadStream to the contents of the file.
     */
    default CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentCharacterReadStream(Path.parse(rootedFilePath), onError);
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    default CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath)
    {
        return getFileContentCharacterReadStream(rootedFilePath, null);
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A CharacterReadStream to the contents of the file.
     */
    default CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath, Action1<String> onError)
    {
        final ByteReadStream contentByteReadStream = getFileContentByteReadStream(rootedFilePath, onError);
        return contentByteReadStream == null ? null : contentByteReadStream.asCharacterReadStream();
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(String rootedFilePath, byte[] fileContents)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(String rootedFilePath, byte[] fileContents, Action1<String> onError)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(Path rootedFilePath, byte[] fileContents)
    {
        return setFileContents(rootedFilePath, fileContents, null);
    }

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
    default boolean setFileContents(String rootedFilePath, String fileContents)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(String rootedFilePath, String fileContents, Action1<String> onError)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(Path rootedFilePath, String fileContents)
    {
        return setFileContents(rootedFilePath, fileContents, (Action1<String>)null);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(Path rootedFilePath, String fileContents, Action1<String> onError)
    {
        return setFileContents(rootedFilePath, fileContents, CharacterEncoding.UTF_8, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents));
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents, encoding, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return setFileContents(rootedFilePath, fileContents, encoding, null);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    default boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents), onError);
    }
}
