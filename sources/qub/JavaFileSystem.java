package qub;

/**
 * A FileSystem implementation that interacts with a typical Windows, Linux, or MacOS device.
 */
public class JavaFileSystem extends FileSystemBase
{
    private AsyncRunner asyncRunner;

    @Override
    public void setAsyncRunner(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public Iterable<Root> getRoots(Action1<String> onError)
    {
        return Array
                .fromValues(java.io.File.listRoots())
                .map(new Function1<java.io.File, Root>()
                {
                    @Override
                    public Root run(java.io.File root)
                    {
                        final String rootPathString = root.getAbsolutePath();
                        final String trimmedRootPathString = rootPathString.equals("/") ? rootPathString : rootPathString.substring(0, rootPathString.length() - 1);
                        return JavaFileSystem.this.getRoot(trimmedRootPathString);
                    }
                });
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path rootedFolderPath, Action1<String> onError)
    {
        Array<FileSystemEntry> result = new Array<>(0);

        if (rootExists(rootedFolderPath))
        {
            final java.io.File containerFile = new java.io.File(rootedFolderPath.toString());
            if (containerFile.exists() && containerFile.isDirectory())
            {
                final java.io.File[] containerEntryFiles = containerFile.listFiles();
                if (containerEntryFiles != null && containerEntryFiles.length > 0)
                {
                    final ArrayList<Folder> folders = new ArrayList<>();
                    final ArrayList<File> files = new ArrayList<>();
                    for (final java.io.File containerEntryFile : containerEntryFiles)
                    {
                        final String containerEntryPathString = containerEntryFile.getAbsolutePath();
                        final Path containerEntryPath = Path.parse(containerEntryPathString).normalize();
                        if (containerEntryFile.isFile())
                        {
                            files.add(getFile(containerEntryPath));
                        }
                        else if (containerEntryFile.isDirectory())
                        {
                            folders.add(getFolder(containerEntryPath));
                        }
                    }

                    result = new Array<>(containerEntryFiles.length);
                    final int foldersCount = folders.getCount();
                    for (int i = 0; i < foldersCount; ++i)
                    {
                        result.set(i, folders.get(i));
                    }
                    for (int i = 0; i < files.getCount(); ++i)
                    {
                        result.set(i + foldersCount, files.get(i));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean folderExists(Path rootedFolderPath, Action1<String> onError)
    {
        boolean result = false;

        if (rootExists(rootedFolderPath))
        {
            final String folderPathString = rootedFolderPath.toString();
            final java.io.File folderFile = new java.io.File(folderPathString);
            result = folderFile.exists() && folderFile.isDirectory();
        }

        return result;
    }

    @Override
    public boolean createFolder(Path rootedFolderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        boolean result = false;

        if (!rootExists(rootedFolderPath))
        {
            if (outputFolder != null)
            {
                outputFolder.clear();
            }
        }
        else
        {
            final String folderPathString = rootedFolderPath.toString();
            final java.io.File folderFile = new java.io.File(folderPathString);
            result = folderFile.mkdirs();

            if (outputFolder != null)
            {
                outputFolder.set(getFolder(rootedFolderPath));
            }
        }

        return result;
    }

    @Override
    public boolean deleteFolder(Path rootedFolderPath, Action1<String> onError)
    {
        boolean result = false;

        if (rootExists(rootedFolderPath))
        {
            final String folderPathString = rootedFolderPath.toString();
            final java.io.File folderFile = new java.io.File(folderPathString);
            result = deleteFolder(folderFile);
        }

        return result;
    }

    private static boolean deleteFolder(java.io.File javaFolder)
    {
        boolean result = true;

        final java.io.File[] javaSubEntries = javaFolder.listFiles();
        if (javaSubEntries != null && javaSubEntries.length > 0)
        {
            for (final java.io.File javaSubEntry : javaSubEntries)
            {
                if (javaSubEntry.isDirectory())
                {
                    result &= deleteFolder(javaSubEntry);
                }
                else
                {
                    result &= deleteFile(javaSubEntry);
                }
            }
        }

        if (result)
        {
            result = javaFolder.delete();
        }

        return result;
    }

    @Override
    public boolean fileExists(Path rootedFilePath, Action1<String> onError)
    {
        boolean result = false;

        if (rootExists(rootedFilePath))
        {
            final String filePathString = rootedFilePath.toString();
            final java.io.File javaFolder = new java.io.File(filePathString);
            result = javaFolder.exists() && javaFolder.isFile();
        }

        return result;
    }

    @Override
    public boolean createFile(Path rootedFilePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError)
    {
        boolean result = false;

        if (!rootExists(rootedFilePath) || rootedFilePath.endsWith("/") || rootedFilePath.endsWith("\\") || rootedFilePath.endsWith(":"))
        {
            if (outputFile != null)
            {
                outputFile.clear();
            }
        }
        else
        {
            final Path parentFolderPath = rootedFilePath.getParentPath();
            final Value<Folder> parentFolder = new Value<>();
            createFolder(parentFolderPath, parentFolder);
            if (parentFolder.get() != null)
            {
                final String filePathString = rootedFilePath.toString();
                final java.io.File file = new java.io.File(filePathString);
                try
                {
                    result = file.createNewFile();
                    if (result && fileContents != null && fileContents.length > 0)
                    {
                        try (final java.io.FileOutputStream writeStream = new java.io.FileOutputStream(filePathString))
                        {
                            writeStream.write(fileContents);
                        }
                    }
                    if (outputFile != null)
                    {
                        outputFile.set(getFile(rootedFilePath));
                    }
                }
                catch (java.io.IOException e)
                {
                    if (onError != null)
                    {
                        onError.run(e.getMessage());
                    }

                    if (outputFile != null)
                    {
                        outputFile.clear();
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean deleteFile(Path rootedFilePath, Action1<String> onError)
    {
        boolean result = false;

        if (rootExists(rootedFilePath))
        {
            final String filePathString = rootedFilePath.toString();
            final java.io.File javaFile = new java.io.File(filePathString);
            result = deleteFolder(javaFile);
        }

        return result;
    }

    @Override
    public DateTime getFileLastModified(Path rootedFilePath)
    {
        DateTime result = null;

        if (rootExists(rootedFilePath))
        {
            final String filePathString = rootedFilePath.toString();
            final java.io.File javaFile = new java.io.File(filePathString);
            final long lastModifiedMilliseconds = javaFile.lastModified();
            if (lastModifiedMilliseconds != 0)
            {
                result = DateTime.local(lastModifiedMilliseconds);
            }
        }

        return result;
    }

    @Override
    public ByteReadStream getFileContentByteReadStream(Path rootedFilePath, Action1<String> onError)
    {
        ByteReadStream result = null;

        if (rootExists(rootedFilePath))
        {
            final String filePathString = rootedFilePath.toString();

            java.io.FileInputStream fileInputStream = null;
            try
            {
                fileInputStream = new java.io.FileInputStream(filePathString);
            }
            catch (java.io.FileNotFoundException e)
            {
                if (onError != null)
                {
                    onError.run(e.getMessage());
                }
            }

            if (fileInputStream != null)
            {
                result = new InputStreamToByteReadStream(fileInputStream);
            }
        }

        return result;
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, byte[] fileContents, Action1<String> onError)
    {
        boolean result = false;

        if (rootExists(rootedFilePath))
        {
            try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(rootedFilePath.toString()))
            {
                outputStream.write(fileContents == null ? new byte[0] : fileContents);
                result = true;
            }
            catch (java.io.IOException e)
            {
                if (onError != null)
                {
                    onError.run(e.getMessage());
                }
                result = createFile(rootedFilePath, fileContents, onError);
            }
        }

        return result;
    }

    private static boolean deleteFile(java.io.File javaFile)
    {
        return javaFile.delete();
    }
}
