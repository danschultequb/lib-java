package qub;

/**
 * An abstract base FileSystem class that contains common methods among FileSystem implementations.
 */
public abstract class FileSystemBase implements FileSystem
{
    private AsyncRunner runner;

    @Override
    public void setAsyncRunner(AsyncRunner runner)
    {
        this.runner = runner;
    }

    @Override
    public boolean rootExists(String rootPath)
    {
        return rootExists(Path.parse(rootPath));
    }

    @Override
    public boolean rootExists(final Path rootPath)
    {
        return getRoots().contains(new Function1<Root, Boolean>()
        {
            @Override
            public Boolean run(Root root)
            {
                return root.getPath().equals(rootPath);
            }
        });
    }

    @Override
    public AsyncFunction<Boolean> rootExistsAsync(final String rootPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return rootExists(rootPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> rootExistsAsync(final Path rootPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return rootExists(rootPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public Root getRoot(String rootPath)
    {
        return getRoot(Path.parse(rootPath));
    }

    @Override
    public Root getRoot(Path rootPath)
    {
        return new Root(this, rootPath);
    }

    @Override
    public AsyncFunction<Iterable<Root>> getRootsAsync()
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<Root>>()
                {
                    @Override
                    public Iterable<Root> run()
                    {
                        return getRoots();
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(String folderPath)
    {
        return getFilesAndFolders(Path.parse(folderPath));
    }

    @Override
    public AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(final String folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<FileSystemEntry>>()
                {
                    @Override
                    public Iterable<FileSystemEntry> run()
                    {
                        return getFilesAndFolders(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(final Path folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<FileSystemEntry>>()
                {
                    @Override
                    public Iterable<FileSystemEntry> run()
                    {
                        return getFilesAndFolders(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public Iterable<Folder> getFolders(String folderPath)
    {
        return getFolders(Path.parse(folderPath));
    }

    @Override
    public Iterable<Folder> getFolders(Path folderPath)
    {
        final Iterable<FileSystemEntry> entries = getFilesAndFolders(folderPath);
        return entries == null ? null : entries.instanceOf(Folder.class);
    }

    @Override
    public AsyncFunction<Iterable<Folder>> getFoldersAsync(final String folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<Folder>>()
                {
                    @Override
                    public Iterable<Folder> run()
                    {
                        return getFolders(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Iterable<Folder>> getFoldersAsync(final Path folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<Folder>>()
                {
                    @Override
                    public Iterable<Folder> run()
                    {
                        return getFolders(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public Iterable<File> getFiles(String folderPath)
    {
        return getFiles(Path.parse(folderPath));
    }

    @Override
    public Iterable<File> getFiles(Path folderPath)
    {
        final Iterable<FileSystemEntry> entries = getFilesAndFolders(folderPath);
        return entries == null ? null : entries.instanceOf(File.class);
    }

    @Override
    public AsyncFunction<Iterable<File>> getFilesAsync(final String folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<File>>()
                {
                    @Override
                    public Iterable<File> run()
                    {
                        return getFiles(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Iterable<File>> getFilesAsync(final Path folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Iterable<File>>()
                {
                    @Override
                    public Iterable<File> run()
                    {
                        return getFiles(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public Folder getFolder(String folderPath)
    {
        return getFolder(Path.parse(folderPath));
    }

    @Override
    public Folder getFolder(Path folderPath)
    {
        return folderPath == null || !folderPath.isRooted() ? null : new Folder(this, folderPath);
    }

    @Override
    public boolean folderExists(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return folderExists(path);
    }

    @Override
    public AsyncFunction<Boolean> folderExistsAsync(final String folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
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
    public AsyncFunction<Boolean> folderExistsAsync(final Path folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
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
    public boolean createFolder(String folderPath)
    {
        return createFolder(Path.parse(folderPath), null);
    }

    @Override
    public boolean createFolder(String folderPath, Out<Folder> outputFolder)
    {
        return createFolder(Path.parse(folderPath), outputFolder);
    }

    @Override
    public boolean createFolder(Path folderPath)
    {
        return createFolder(folderPath, null);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(final String folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFolder(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(final String folderPath, final Out<Folder> outputFolder)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFolder(folderPath, outputFolder);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(final Path folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFolder(folderPath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(final Path folderPath, final Out<Folder> outputFolder)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFolder(folderPath, outputFolder);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public boolean deleteFolder(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return deleteFolder(path);
    }

    @Override
    public AsyncFunction<Boolean> deleteFolderAsync(final String folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
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
    public AsyncFunction<Boolean> deleteFolderAsync(final Path folderPath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
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
    public File getFile(String filePath)
    {
        return getFile(Path.parse(filePath));
    }

    @Override
    public File getFile(Path filePath)
    {
        return filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") ? null : new File(this, filePath);
    }

    @Override
    public boolean fileExists(String filePath)
    {
        return fileExists(Path.parse(filePath));
    }

    @Override
    public AsyncFunction<Boolean> fileExistsAsync(final String filePath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return fileExists(filePath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> fileExistsAsync(final Path filePath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return fileExists(filePath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public boolean createFile(String filePath)
    {
        return createFile(Path.parse(filePath), null);
    }

    @Override
    public boolean createFile(String filePath, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, outputFile);
    }

    @Override
    public boolean createFile(Path filePath)
    {
        return createFile(filePath, null);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(final String filePath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFile(filePath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(final String filePath, final Out<File> outputFile)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFile(filePath, outputFile);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(final Path filePath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFile(filePath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(final Path filePath, final Out<File> outputFile)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return createFile(filePath, outputFile);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public boolean deleteFile(String filePath)
    {
        final Path path = Path.parse(filePath);
        return deleteFile(path);
    }

    @Override
    public AsyncFunction<Boolean> deleteFileAsync(final String filePath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return deleteFile(filePath);
                    }
                })
                .thenOn(currentRunner);
    }

    @Override
    public AsyncFunction<Boolean> deleteFileAsync(final Path filePath)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return runner
                .schedule(new Function0<Boolean>()
                {
                    @Override
                    public Boolean run()
                    {
                        return deleteFile(filePath);
                    }
                })
                .thenOn(currentRunner);
    }
}
