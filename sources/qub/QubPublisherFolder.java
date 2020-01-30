package qub;

public class QubPublisherFolder extends Folder
{
    private QubPublisherFolder(Folder publisherFolder)
    {
        super(publisherFolder.getFileSystem(), publisherFolder.getPath());
    }

    public static QubPublisherFolder create(Folder publisherFolder)
    {
        PreCondition.assertNotNull(publisherFolder, "publisherFolder");
        PreCondition.assertGreaterThanOrEqualTo(publisherFolder.getPath().getSegments().getCount(), 2, "publisherFolder.getPath().getSegments().getCount()");

        return new QubPublisherFolder(publisherFolder);
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getParentFolder()
            .then((Folder parentFolder) -> QubFolder.create(parentFolder));
    }

    public String getPublisherName()
    {
        return this.getName();
    }

    /**
     * Get the project folders that are present in this QubPublisherFolder.
     * @return The project folders that are present in this QubPublisherFolder.
     */
    public Result<Iterable<QubProjectFolder>> getProjectFolders()
    {
        return this.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubProjectFolder::create));
    }

    /**
     * Get a QubProjectFolder for the project with the provided name.
     * @param projectName The name of the project.
     * @return The QubProjectFolder for the project with the provided name.
     */
    public Result<QubProjectFolder> getProjectFolder(String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.getFolder(projectName)
            .then((Folder folder) -> QubProjectFolder.create(folder));
    }
}
