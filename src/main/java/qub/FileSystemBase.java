package qub;

/**
 * An abstract base FileSystem class that contains common methods among FileSystem implementations.
 */
public abstract class FileSystemBase implements FileSystem
{
    @Override
    public boolean rootExists(String rootPath)
    {
        return rootExists(Path.parse(rootPath));
    }

    @Override
    public boolean rootExists(final Path rootPath)
    {
        return getRoots().any(new Function1<Root, Boolean>()
        {
            @Override
            public Boolean run(Root root)
            {
                return root.getPath().equals(rootPath);
            }
        });
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
    public Iterable<FileSystemEntry> getFilesAndFolders(String folderPath)
    {
        return getFilesAndFolders(Path.parse(folderPath));
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
    public boolean createFolder(String folderPath)
    {
        return createFolder(Path.parse(folderPath), null);
    }

    @Override
    public boolean createFolder(String folderPath, Value<Folder> outputFolder)
    {
        return createFolder(Path.parse(folderPath), outputFolder);
    }

    @Override
    public boolean createFolder(Path folderPath)
    {
        return createFolder(folderPath, null);
    }

    @Override
    public boolean deleteFolder(String folderPath)
    {
        final Path path = Path.parse(folderPath);
        return deleteFolder(path);
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
    public boolean fileExists(String fileExists)
    {
        return fileExists(Path.parse(fileExists));
    }

    @Override
    public boolean createFile(String filePath)
    {
        return createFile(Path.parse(filePath), null);
    }

    @Override
    public boolean createFile(String filePath, Value<File> outputFile)
    {
        final Path path = Path.parse(filePath);
        return createFile(path, outputFile);
    }

    @Override
    public boolean createFile(Path filePath)
    {
        return createFile(filePath, null);
    }
}
