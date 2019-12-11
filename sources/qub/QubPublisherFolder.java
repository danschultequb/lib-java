package qub;

public class QubPublisherFolder extends Folder
{
    public QubPublisherFolder(Folder folder)
    {
        this(folder.getFileSystem(), folder.getPath());
    }

    public QubPublisherFolder(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getParentFolder()
            .then((Folder parentFolder) -> new QubFolder(parentFolder));
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
            .then((Iterable<Folder> folders) -> folders.map((Folder folder) -> new QubProjectFolder(folder)));
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
            .then((Folder folder) -> new QubProjectFolder(folder));
    }
}
