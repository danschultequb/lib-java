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
        final Path path = Path.parse(filePath);
        return createFile(path, (byte[])null, null);
    }

    @Override
    public boolean createFile(String filePath, byte[] fileContents)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, null);
    }

    @Override
    public boolean createFile(String filePath, String fileContents)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, CharacterEncoding.ASCII, null);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, CharacterEncoding encoding)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, encoding, null);
    }

    @Override
    public boolean createFile(String filePath, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, outputFile);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, outputFile);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, encoding, outputFile);
    }

    @Override
    public boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, fileContents, outputFile);
    }

    @Override
    public boolean createFile(Path filePath)
    {
        return createFile(filePath, (byte[])null, null);
    }

    @Override
    public boolean createFile(Path filePath, byte[] fileContents)
    {
        return createFile(filePath, fileContents, null);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents)
    {
        return createFile(filePath, fileContents, CharacterEncoding.ASCII, null);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding)
    {
        return createFile(filePath, fileContents, encoding, null);
    }

    @Override
    public boolean createFile(Path filePath, Out<File> outputFile)
    {
        return createFile(filePath, (byte[])null, outputFile);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, Out<File> outputFile)
    {
        return createFile(filePath, fileContents, CharacterEncoding.ASCII, outputFile);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return createFile(filePath, fileContentsBytes, outputFile);
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

    @Override
    public byte[] getFileContents(String rootedFilePath)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContents(path);
    }

    @Override
    public String getFileContentsAsString(String rootedFilePath)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentsAsString(path);
    }

    @Override
    public String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentsAsString(path, encoding);
    }

    @Override
    public String getFileContentsAsString(Path rootedFilePath)
    {
        return getFileContentsAsString(rootedFilePath, CharacterEncoding.ASCII);
    }

    @Override
    public Iterable<byte[]> getFileContentBlocks(String rootedFilePath)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentBlocks(path);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentLines(path);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentLines(path, includeNewLines);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentLines(path, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        final Path path = Path.parse(rootedFilePath);
        return getFileContentLines(path, includeNewLines, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath)
    {
        return getFileContentLines(rootedFilePath, true);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines)
    {
        return getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.ASCII);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding)
    {
        return getFileContentLines(rootedFilePath, true, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        Iterable<String> result = null;

        final String fileContents = getFileContentsAsString(rootedFilePath, encoding);
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
    public boolean setFileContents(String rootedFilePath, byte[] fileContents)
    {
        final Path path = Path.parse(rootedFilePath);
        return setFileContents(path, fileContents);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, String fileContents)
    {
        return setFileContents(rootedFilePath, fileContents, CharacterEncoding.ASCII);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents)
    {
        return setFileContents(rootedFilePath, fileContents, CharacterEncoding.ASCII);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents));
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents));
    }
}
