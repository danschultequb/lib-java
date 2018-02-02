package qub;

/**
 * An abstract base FileSystem class that contains common methods among FileSystem implementations.
 */
public abstract class FileSystemBase implements FileSystem
{
    private AsyncRunner parallelRunner;

    @Override
    public final void setAsyncRunner(AsyncRunner parallelRunner)
    {
        this.parallelRunner = parallelRunner;
    }

    @Override
    public final boolean rootExists(String rootPath)
    {
        return rootExists(Path.parse(rootPath));
    }

    @Override
    public final boolean rootExists(String rootPath, Action1<String> onError)
    {
        return rootExists(Path.parse(rootPath), onError);
    }

    @Override
    public final boolean rootExists(Path rootPath)
    {
        return rootExists(rootPath, null);
    }

    @Override
    public final boolean rootExists(Path rootPath, Action1<String> onError)
    {
        boolean result = false;
        if (rootPath != null && rootPath.isRooted())
        {
            final Path onlyRootPath = rootPath.getRootPath();
            result = getRoots(onError).contains(new Function1<Root, Boolean>()
            {
                @Override
                public Boolean run(Root root)
                {
                    return root.getPath().equals(onlyRootPath);
                }
            });
        }
        return result;
    }

    @Override
    public final AsyncFunction<Boolean> rootExistsAsync(String rootPath)
    {
        final Path path = Path.parse(rootPath);
        return rootExistsAsync(path);
    }

    @Override
    public final AsyncFunction<Boolean> rootExistsAsync(String rootPath, Action1<String> onError)
    {
        final Path path = Path.parse(rootPath);
        return rootExistsAsync(path, onError);
    }

    @Override
    public final AsyncFunction<Boolean> rootExistsAsync(Path rootPath)
    {
        return rootExistsAsync(rootPath, null);
    }

    @Override
    public final AsyncFunction<Boolean> rootExistsAsync(final Path rootPath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return rootExists(rootPath, onError);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public final Root getRoot(String rootPath)
    {
        return getRoot(Path.parse(rootPath));
    }

    @Override
    public final Root getRoot(Path rootPath)
    {
        return new Root(this, rootPath);
    }

    @Override
    public final Iterable<Root> getRoots()
    {
        return getRoots(null);
    }

    @Override
    public final AsyncFunction<Iterable<Root>> getRootsAsync()
    {
        return getRootsAsync(null);
    }

    @Override
    public final AsyncFunction<Iterable<Root>> getRootsAsync(final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
            .schedule(new Function0<Iterable<Root>>()
            {
                @Override
                public Iterable<Root> run()
                {
                    return getRoots(onError);
                }
            })
            .thenOn(currentRunner);
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFolders(String folderPath)
    {
        return getFilesAndFolders(Path.parse(folderPath));
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFolders(String folderPath, Action1<String> onError)
    {
        return getFilesAndFolders(Path.parse(folderPath), onError);
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        return getFilesAndFolders(folderPath, null);
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath)
    {
        return getFilesAndFoldersRecursively(Path.parse(folderPath));
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath, Action1<String> onError)
    {
        return getFilesAndFoldersRecursively(Path.parse(folderPath), onError);
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath)
    {
        return getFilesAndFoldersRecursively(folderPath, null);
    }

    @Override
    public final Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath, Action1<String> onError)
    {
        List<FileSystemEntry> result = null;

        final Folder folder = getFolder(folderPath);
        if (folder != null && folder.exists())
        {
            result = new ArrayList<>();

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

    @Override
    public final AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return getFilesAndFoldersAsync(path);
    }

    @Override
    public final AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath, Action1<String> onError)
    {
        final Path path = Path.parse(folderPath);
        return getFilesAndFoldersAsync(path, onError);
    }

    @Override
    public final AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath)
    {
        return getFilesAndFoldersAsync(folderPath, null);
    }

    @Override
    public final AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(final Path folderPath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
                .schedule(new Function0<Iterable<FileSystemEntry>>()
                {
                    @Override
                    public Iterable<FileSystemEntry> run()
                    {
                        return getFilesAndFolders(folderPath, onError);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public final Iterable<Folder> getFolders(String folderPath)
    {
        return getFolders(Path.parse(folderPath));
    }

    @Override
    public final Iterable<Folder> getFolders(String folderPath, Action1<String> onError)
    {
        return getFolders(Path.parse(folderPath), onError);
    }

    @Override
    public final Iterable<Folder> getFolders(Path folderPath)
    {
        return getFolders(folderPath, null);
    }

    @Override
    public final Iterable<Folder> getFolders(Path folderPath, Action1<String> onError)
    {
        final Iterable<FileSystemEntry> entries = getFilesAndFolders(folderPath);
        return entries == null ? null : entries.instanceOf(Folder.class);
    }

    @Override
    public final Iterable<Folder> getFoldersRecursively(String folderPath)
    {
        return getFoldersRecursively(Path.parse(folderPath));
    }

    @Override
    public final Iterable<Folder> getFoldersRecursively(String folderPath, Action1<String> onError)
    {
        return getFoldersRecursively(Path.parse(folderPath), onError);
    }

    @Override
    public final Iterable<Folder> getFoldersRecursively(Path folderPath)
    {
        return getFoldersRecursively(folderPath, null);
    }

    @Override
    public final Iterable<Folder> getFoldersRecursively(Path folderPath, Action1<String> onError)
    {
        final Iterable<FileSystemEntry> entries = getFilesAndFoldersRecursively(folderPath, onError);
        return entries == null ? null : entries.instanceOf(Folder.class);
    }

    @Override
    public final AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return getFoldersAsync(path);
    }

    @Override
    public final AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath, Action1<String> onError)
    {
        final Path path = Path.parse(folderPath);
        return getFoldersAsync(path, onError);
    }

    @Override
    public final AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath)
    {
        return getFoldersAsync(folderPath, null);
    }

    @Override
    public final AsyncFunction<Iterable<Folder>> getFoldersAsync(final Path folderPath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
                .schedule(new Function0<Iterable<Folder>>()
                {
                    @Override
                    public Iterable<Folder> run()
                    {
                        return getFolders(folderPath, onError);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public final Iterable<File> getFiles(String folderPath)
    {
        return getFiles(Path.parse(folderPath));
    }

    @Override
    public final Iterable<File> getFiles(String folderPath, Action1<String> onError)
    {
        return getFiles(Path.parse(folderPath), onError);
    }

    @Override
    public final Iterable<File> getFiles(Path folderPath)
    {
        return getFiles(folderPath, null);
    }

    @Override
    public final Iterable<File> getFiles(Path folderPath, Action1<String> onError)
    {
        final Iterable<FileSystemEntry> entries = getFilesAndFolders(folderPath, onError);
        return entries == null ? null : entries.instanceOf(File.class);
    }

    @Override
    public final Iterable<File> getFilesRecursively(String folderPath)
    {
        return getFilesRecursively(Path.parse(folderPath));
    }

    @Override
    public final Iterable<File> getFilesRecursively(String folderPath, Action1<String> onError)
    {
        return getFilesRecursively(Path.parse(folderPath), onError);
    }

    @Override
    public final Iterable<File> getFilesRecursively(Path folderPath)
    {
        return getFilesRecursively(folderPath, null);
    }

    @Override
    public final Iterable<File> getFilesRecursively(Path folderPath, Action1<String> onError)
    {
        final Iterable<FileSystemEntry> entries = getFilesAndFoldersRecursively(folderPath, onError);
        return entries == null ? null : entries.instanceOf(File.class);
    }

    @Override
    public final AsyncFunction<Iterable<File>> getFilesAsync(String folderPath)
    {
        return getFilesAsync(Path.parse(folderPath));
    }

    @Override
    public final AsyncFunction<Iterable<File>> getFilesAsync(String folderPath, Action1<String> onError)
    {
        return getFilesAsync(Path.parse(folderPath), onError);
    }

    @Override
    public final AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath)
    {
        return getFilesAsync(folderPath, null);
    }

    @Override
    public final AsyncFunction<Iterable<File>> getFilesAsync(final Path folderPath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
            .schedule(new Function0<Iterable<File>>()
            {
                @Override
                public Iterable<File> run()
                {
                    return getFiles(folderPath, onError);
                }
            })
            .thenOn(currentRunner);
    }

    @Override
    public final Folder getFolder(String folderPath)
    {
        return getFolder(Path.parse(folderPath));
    }

    @Override
    public final Folder getFolder(Path folderPath)
    {
        return folderPath == null || !folderPath.isRooted() ? null : new Folder(this, folderPath);
    }

    @Override
    public final boolean folderExists(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return folderExists(path);
    }

    @Override
    public final boolean folderExists(String folderPath, Action1<String> onError)
    {
        final Path path = Path.parse(folderPath);
        return folderExists(path, onError);
    }

    @Override
    public final boolean folderExists(Path folderPath)
    {
        return folderExists(folderPath, null);
    }

    @Override
    public final AsyncFunction<Boolean> folderExistsAsync(String folderPath)
    {
        return folderExistsAsync(Path.parse(folderPath));
    }

    @Override
    public final AsyncFunction<Boolean> folderExistsAsync(String folderPath, Action1<String> onError)
    {
        return folderExistsAsync(Path.parse(folderPath), onError);
    }

    @Override
    public final AsyncFunction<Boolean> folderExistsAsync(Path folderPath)
    {
        return folderExistsAsync(folderPath, null);
    }

    @Override
    public final AsyncFunction<Boolean> folderExistsAsync(final Path folderPath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return folderExists(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public final boolean createFolder(String folderPath)
    {
        return createFolder(Path.parse(folderPath));
    }

    @Override
    public final boolean createFolder(String folderPath, Action1<String> onError)
    {
        return createFolder(Path.parse(folderPath), onError);
    }

    @Override
    public final boolean createFolder(String folderPath, Out<Folder> outputFolder)
    {
        return createFolder(Path.parse(folderPath), outputFolder);
    }

    @Override
    public final boolean createFolder(String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return createFolder(Path.parse(folderPath), outputFolder, onError);
    }

    @Override
    public final boolean createFolder(Path folderPath)
    {
        return createFolder(folderPath, null, null);
    }

    @Override
    public final boolean createFolder(Path folderPath, Action1<String> onError)
    {
        return createFolder(folderPath, null, onError);
    }

    @Override
    public final boolean createFolder(Path folderPath, Out<Folder> outputFolder)
    {
        return createFolder(folderPath, outputFolder, null);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(String folderPath)
    {
        return createFolderAsync(Path.parse(folderPath));
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(String folderPath, Action1<String> onError)
    {
        return createFolderAsync(Path.parse(folderPath), onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder)
    {
        return createFolderAsync(Path.parse(folderPath), outputFolder);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return createFolderAsync(Path.parse(folderPath), outputFolder, onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(Path folderPath)
    {
        return createFolderAsync(folderPath, null, null);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(Path folderPath, Action1<String> onError)
    {
        return createFolderAsync(folderPath, null, onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder)
    {
        return createFolderAsync(folderPath, outputFolder, null);
    }

    @Override
    public final AsyncFunction<Boolean> createFolderAsync(final Path folderPath, final Out<Folder> outputFolder, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFolder(folderPath, outputFolder, onError);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public final boolean deleteFolder(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return deleteFolder(path);
    }

    @Override
    public final boolean deleteFolder(String folderPath, Action1<String> onError)
    {
        final Path path = Path.parse(folderPath);
        return deleteFolder(path, onError);
    }

    @Override
    public final boolean deleteFolder(Path folderPath)
    {
        return deleteFolder(folderPath, null);
    }

    @Override
    public final AsyncFunction<Boolean> deleteFolderAsync(String folderPath)
    {
        return deleteFolderAsync(Path.parse(folderPath));
    }

    @Override
    public final AsyncFunction<Boolean> deleteFolderAsync(String folderPath, Action1<String> onError)
    {
        return deleteFolderAsync(Path.parse(folderPath), onError);
    }

    @Override
    public final AsyncFunction<Boolean> deleteFolderAsync(Path folderPath)
    {
        return deleteFolderAsync(folderPath, null);
    }

    @Override
    public final AsyncFunction<Boolean> deleteFolderAsync(final Path folderPath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
            .schedule(new Function0<Boolean>()
            {
                @Override
                public Boolean run()
                {
                    return deleteFolder(folderPath);
                }
            })
            .thenOn(currentRunner);
    }

    @Override
    public final File getFile(String filePath)
    {
        return getFile(Path.parse(filePath));
    }

    @Override
    public final File getFile(Path filePath)
    {
        return filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") ? null : new File(this, filePath);
    }

    @Override
    public final boolean fileExists(String filePath)
    {
        return fileExists(Path.parse(filePath));
    }

    @Override
    public final boolean fileExists(String filePath, Action1<String> onError)
    {
        return fileExists(Path.parse(filePath), onError);
    }

    @Override
    public final boolean fileExists(Path filePath)
    {
        return fileExists(filePath, null);
    }

    @Override
    public final AsyncFunction<Boolean> fileExistsAsync(String filePath)
    {
        return fileExistsAsync(Path.parse(filePath));
    }

    @Override
    public final AsyncFunction<Boolean> fileExistsAsync(String filePath, Action1<String> onError)
    {
        return fileExistsAsync(Path.parse(filePath), onError);
    }

    @Override
    public final AsyncFunction<Boolean> fileExistsAsync(Path filePath)
    {
        return fileExistsAsync(filePath, null);
    }

    @Override
    public final AsyncFunction<Boolean> fileExistsAsync(final Path filePath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return fileExists(filePath, onError);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public final boolean createFile(String filePath)
    {
        final Path path = Path.parse(filePath);
        return createFile(path);
    }

    @Override
    public final boolean createFile(String filePath, Action1<String> onError)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, onError);
    }

    @Override
    public final boolean createFile(String filePath, byte[] fileContents)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents);
    }

    @Override
    public final boolean createFile(String filePath, byte[] fileContents, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents)
    {
        return createFile(Path.parse(filePath), fileContents);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, onError);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, CharacterEncoding encoding)
    {
        return createFile(Path.parse(filePath), fileContents, encoding);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, encoding, onError);
    }

    @Override
    public final boolean createFile(String filePath, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, outputFile);
    }

    @Override
    public final boolean createFile(String filePath, Out<File> outputFile, Action1<String> onError)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, outputFile, onError);
    }

    @Override
    public final boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, outputFile);
    }

    @Override
    public final boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile, onError);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, Out<File> outputFile)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, outputFile, onError);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        return createFile(Path.parse(filePath), fileContents, encoding, outputFile);
    }

    @Override
    public final boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(Path.parse(filePath), fileContents, encoding, outputFile, onError);
    }

    @Override
    public final boolean createFile(Path filePath)
    {
        return createFile(filePath, (byte[])null);
    }

    @Override
    public final boolean createFile(Path filePath, Action1<String> onError)
    {
        return createFile(filePath, (byte[])null, onError);
    }

    @Override
    public final boolean createFile(Path filePath, byte[] fileContents)
    {
        return createFile(filePath, fileContents, (Out<File>)null);
    }

    @Override
    public final boolean createFile(Path filePath, byte[] fileContents, Action1<String> onError)
    {
        return createFile(filePath, fileContents, null, onError);
    }

    @Override
    public final boolean createFile(Path filePath, byte[] fileContents, Out<File> outFile)
    {
        return createFile(filePath, fileContents, outFile, null);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, (Out<File>)null);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, Action1<String> onError)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, null, onError);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding)
    {
        return createFile(filePath, fileContents, encoding, (Out<File>)null);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return createFile(filePath, fileContents, encoding, null, onError);
    }

    @Override
    public final boolean createFile(Path filePath, Out<File> outputFile)
    {
        return createFile(filePath, (byte[])null, outputFile);
    }

    @Override
    public final boolean createFile(Path filePath, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(filePath, (byte[])null, outputFile);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, Out<File> outputFile)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, outputFile);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return createFile(filePath, fileContents, CharacterEncoding.UTF_8, outputFile, onError);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return createFile(filePath, fileContentsBytes, outputFile);
    }

    @Override
    public final boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return createFile(filePath, fileContentsBytes, outputFile, onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(String filePath)
    {
        return createFileAsync(Path.parse(filePath));
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(String filePath, Action1<String> onError)
    {
        return createFileAsync(Path.parse(filePath), onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile)
    {
        return createFileAsync(Path.parse(filePath), outputFile);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return createFileAsync(Path.parse(filePath), outputFile, onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(Path filePath)
    {
        return createFileAsync(filePath, (Out<File>)null);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(Path filePath, Action1<String> onError)
    {
        return createFileAsync(filePath, null, onError);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(final Path filePath, final Out<File> outputFile)
    {
        return createFileAsync(filePath, outputFile, null);
    }

    @Override
    public final AsyncFunction<Boolean> createFileAsync(final Path filePath, final Out<File> outputFile, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
            .schedule(new Function0<Boolean>()
            {
                @Override
                public Boolean run()
                {
                    return createFile(filePath, outputFile, onError);
                }
            })
            .thenOn(currentRunner);
    }

    @Override
    public final boolean deleteFile(String filePath)
    {
        return deleteFile(Path.parse(filePath));
    }

    @Override
    public final boolean deleteFile(String filePath, Action1<String> onError)
    {
        return deleteFile(Path.parse(filePath), onError);
    }

    @Override
    public final boolean deleteFile(Path filePath)
    {
        return deleteFile(filePath, null);
    }

    @Override
    public final AsyncFunction<Boolean> deleteFileAsync(String filePath)
    {
        return deleteFileAsync(Path.parse(filePath));
    }

    @Override
    public final AsyncFunction<Boolean> deleteFileAsync(String filePath, Action1<String> onError)
    {
        return deleteFileAsync(Path.parse(filePath), onError);
    }

    @Override
    public final AsyncFunction<Boolean> deleteFileAsync(Path filePath)
    {
        return deleteFileAsync(filePath, null);
    }

    @Override
    public final AsyncFunction<Boolean> deleteFileAsync(final Path filePath, final Action1<String> onError)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return parallelRunner
            .schedule(new Function0<Boolean>()
            {
                @Override
                public Boolean run()
                {
                    return deleteFile(filePath, onError);
                }
            })
            .thenOn(currentRunner);
    }

    @Override
    public final byte[] getFileContents(String rootedFilePath)
    {
        return getFileContents(Path.parse(rootedFilePath));
    }

    @Override
    public final byte[] getFileContents(String rootedFilePath, Action1<String> onError)
    {
        return getFileContents(Path.parse(rootedFilePath), onError);
    }

    @Override
    public final byte[] getFileContents(Path rootedFilePath)
    {
        return getFileContents(rootedFilePath, null);
    }

    @Override
    public final byte[] getFileContents(Path rootedFilePath, Action1<String> onError)
    {
        final Iterable<byte[]> fileContentBlocks = getFileContentBlocks(rootedFilePath, onError);
        return Array.merge(fileContentBlocks);
    }

    @Override
    public final String getFileContentsAsString(String rootedFilePath)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath));
    }

    @Override
    public final String getFileContentsAsString(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath), onError);
    }

    @Override
    public final String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath), encoding);
    }

    @Override
    public final String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentsAsString(Path.parse(rootedFilePath), encoding, onError);
    }

    @Override
    public final String getFileContentsAsString(Path rootedFilePath)
    {
        return getFileContentsAsString(rootedFilePath, CharacterEncoding.UTF_8);
    }

    @Override
    public final String getFileContentsAsString(Path rootedFilePath, Action1<String> onError)
    {
        return getFileContentsAsString(rootedFilePath, CharacterEncoding.UTF_8, onError);
    }

    @Override
    public final String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentsAsString(rootedFilePath, encoding, null);
    }

    @Override
    public final String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        String result = null;
        if (encoding != null)
        {
            final byte[] fileContents = getFileContents(rootedFilePath, onError);
            result = encoding.decode(fileContents);
        }
        return result;
    }

    @Override
    public final Iterable<byte[]> getFileContentBlocks(String rootedFilePath)
    {
        return getFileContentBlocks(Path.parse(rootedFilePath));
    }

    @Override
    public final Iterable<byte[]> getFileContentBlocks(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentBlocks(Path.parse(rootedFilePath), onError);
    }

    @Override
    public final Iterable<byte[]> getFileContentBlocks(Path rootedFilePath)
    {
        return getFileContentBlocks(rootedFilePath, null);
    }

    @Override
    public final Iterable<byte[]> getFileContentBlocks(Path rootedFilePath, Action1<String> onError)
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

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath)
    {
        return getFileContentLines(Path.parse(rootedFilePath));
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, Action1<String> onError)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentLines(path, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines);
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentLines(Path.parse(rootedFilePath), encoding);
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), encoding, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines, encoding);
    }

    @Override
    public final Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentLines(Path.parse(rootedFilePath), includeNewLines, encoding, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath)
    {
        return getFileContentLines(rootedFilePath, true);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, Action1<String> onError)
    {
        return getFileContentLines(rootedFilePath, true, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.UTF_8);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.UTF_8, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentLines(rootedFilePath, true, encoding);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return getFileContentLines(rootedFilePath, true, encoding, onError);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, encoding, null);
    }

    @Override
    public final Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
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

    @Override
    public final ByteReadStream getFileContentByteReadStream(String rootedFilePath)
    {
        return getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    @Override
    public final ByteReadStream getFileContentByteReadStream(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentByteReadStream(Path.parse(rootedFilePath), onError);
    }

    @Override
    public final ByteReadStream getFileContentByteReadStream(Path rootedFilePath)
    {
        return getFileContentByteReadStream(rootedFilePath, null);
    }

    @Override
    public final CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath)
    {
        return getFileContentCharacterReadStream(Path.parse(rootedFilePath));
    }

    @Override
    public final CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath, Action1<String> onError)
    {
        return getFileContentCharacterReadStream(Path.parse(rootedFilePath), onError);
    }

    @Override
    public final CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath)
    {
        return getFileContentCharacterReadStream(rootedFilePath, null);
    }

    @Override
    public final CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath, Action1<String> onError)
    {
        final ByteReadStream contentByteReadStream = getFileContentByteReadStream(rootedFilePath, onError);
        return contentByteReadStream == null ? null : contentByteReadStream.asCharacterReadStream();
    }

    @Override
    public final boolean setFileContents(String rootedFilePath, byte[] fileContents)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents);
    }

    @Override
    public final boolean setFileContents(String rootedFilePath, byte[] fileContents, Action1<String> onError)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents, onError);
    }

    @Override
    public final boolean setFileContents(String rootedFilePath, String fileContents)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents);
    }

    @Override
    public final boolean setFileContents(String rootedFilePath, String fileContents, Action1<String> onError)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents, onError);
    }

    @Override
    public final boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents));
    }

    @Override
    public final boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return setFileContents(Path.parse(rootedFilePath), fileContents, encoding, onError);
    }

    @Override
    public final boolean setFileContents(Path rootedFilePath, byte[] fileContents)
    {
        return setFileContents(rootedFilePath, fileContents, null);
    }

    @Override
    public final boolean setFileContents(Path rootedFilePath, String fileContents)
    {
        return setFileContents(rootedFilePath, fileContents, (Action1<String>)null);
    }

    @Override
    public final boolean setFileContents(Path rootedFilePath, String fileContents, Action1<String> onError)
    {
        return setFileContents(rootedFilePath, fileContents, CharacterEncoding.UTF_8, onError);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return setFileContents(rootedFilePath, fileContents, encoding, null);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents), onError);
    }
}
