package qub;

import java.io.IOException;

/**
 * A FileSystem implementation that interacts with a typical Windows, Linux, or MacOS device.
 */
public class JavaFileSystem extends FileSystemBase
{
    @Override
    public Iterable<Root> getRoots()
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
                        return getRoot(trimmedRootPathString);
                    }
                });
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        Array<FileSystemEntry> result = null;

        if (folderPath != null)
        {
            final java.io.File containerFile = new java.io.File(folderPath.toString());
            if (containerFile.exists() && containerFile.isDirectory())
            {
                final java.io.File[] containerEntryFiles = containerFile.listFiles();
                if (containerEntryFiles == null || containerEntryFiles.length == 0)
                {
                    result = new Array<>(0);
                }
                else
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
    public boolean folderExists(Path folderPath)
    {
        boolean result = false;

        if (folderPath != null && folderPath.isRooted())
        {
            final String folderPathString = folderPath.toString();
            final java.io.File folderFile = new java.io.File(folderPathString);
            result = folderFile.exists() && folderFile.isDirectory();
        }

        return result;
    }

    @Override
    public boolean createFolder(Path folderPath, Out<Folder> outputFolder)
    {
        boolean result = false;

        if (folderPath == null || !folderPath.isRooted() || !rootExists(folderPath.getRoot()))
        {
            if (outputFolder != null)
            {
                outputFolder.clear();
            }
        }
        else
        {
            final String folderPathString = folderPath.toString();
            final java.io.File folderFile = new java.io.File(folderPathString);
            result = folderFile.mkdirs();

            if (outputFolder != null)
            {
                outputFolder.set(getFolder(folderPath));
            }
        }

        return result;
    }

    @Override
    public boolean deleteFolder(Path folderPath)
    {
        boolean result = false;

        if (folderPath != null && folderPath.isRooted() && rootExists(folderPath.getRoot()))
        {
            final String folderPathString = folderPath.toString();
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
    public boolean fileExists(Path filePath)
    {
        boolean result = false;

        if (filePath != null && filePath.isRooted() && !filePath.endsWith("/") && !filePath.endsWith("\\"))
        {
            final String filePathString = filePath.toString();
            final java.io.File javaFolder = new java.io.File(filePathString);
            result = javaFolder.exists() && javaFolder.isFile();
        }

        return result;
    }

    @Override
    public boolean createFile(Path filePath, Out<File> outputFile)
    {
        boolean result = false;

        if (filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") || filePath.endsWith(":") || !rootExists(filePath.getRoot()))
        {
            if (outputFile != null)
            {
                outputFile.clear();
            }
        }
        else
        {
            final Path parentFolderPath = filePath.getParentPath();
            final Value<Folder> parentFolder = new Value<>();
            createFolder(parentFolderPath, parentFolder);
            if (parentFolder.get() != null)
            {
                final java.io.File file = new java.io.File(filePath.toString());
                try
                {
                    result = file.createNewFile();
                    if (outputFile != null)
                    {
                        outputFile.set(getFile(filePath));
                    }
                }
                catch (IOException e)
                {
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
    public boolean deleteFile(Path filePath)
    {
        boolean result = false;

        if (filePath != null && filePath.isRooted() && rootExists(filePath.getRoot()))
        {
            final String filePathString = filePath.toString();
            final java.io.File javaFile = new java.io.File(filePathString);
            result = deleteFolder(javaFile);
        }

        return result;
    }

    private static boolean deleteFile(java.io.File javaFile)
    {
        return javaFile.delete();
    }
}
