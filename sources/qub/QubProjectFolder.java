package qub;

public class QubProjectFolder extends Folder
{
    public QubProjectFolder(Folder folder)
    {
        this(folder.getFileSystem(), folder.getPath());
    }

    public QubProjectFolder(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getPublisherFolder()
            .then((QubPublisherFolder publisherFolder) -> publisherFolder.getQubFolder().await());
    }

    public Result<QubPublisherFolder> getPublisherFolder()
    {
        return this.getParentFolder()
            .then((Folder folder) -> new QubPublisherFolder(folder));
    }

    public Result<String> getPublisherName()
    {
        return this.getPublisherFolder()
            .then(QubPublisherFolder::getPublisherName);
    }

    public String getProjectName()
    {
        return this.getName();
    }

    public Result<Iterable<QubProjectVersionFolder>> getProjectVersionFolders()
    {
        return this.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubProjectVersionFolder::new));
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getFolder(version)
            .then((Folder folder) -> new QubProjectVersionFolder(folder));
    }
}
