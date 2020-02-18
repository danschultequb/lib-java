package qub;

public class QubProjectFolder extends Folder
{
    private QubProjectFolder(Folder folder)
    {
        super(folder.getFileSystem(), folder.getPath());
    }

    public static QubProjectFolder create(Folder projectFolder)
    {
        PreCondition.assertNotNull(projectFolder, "projectFolder");
        PreCondition.assertGreaterThanOrEqualTo(projectFolder.getPath().getSegments().getCount(), 3, "projectFolder.getPath().getSegments().getCount()");

        return new QubProjectFolder(projectFolder);
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getPublisherFolder()
            .then((QubPublisherFolder publisherFolder) -> publisherFolder.getQubFolder().await());
    }

    public Result<QubPublisherFolder> getPublisherFolder()
    {
        return this.getParentFolder()
            .then((Folder folder) -> QubPublisherFolder.create(folder));
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
            .then((Iterable<Folder> folders) -> folders.map(QubProjectVersionFolder::create));
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getFolder(version)
            .then((Folder folder) -> QubProjectVersionFolder.create(folder));
    }

    /**
     * Get the data folder system that is associated with this Qub project.
     * @return The data file system that is associated with this Qub project.
     */
    public Result<Folder> getProjectDataFolder()
    {
        return this.getFolder("data");
    }
}
