package qub;

public class QubPublisherFolder
{
    private final Folder folder;

    public QubPublisherFolder(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        this.folder = folder;
    }

    /**
     * Get the path to this QubPublisherFolder.
     * @return The path to this QubPublisherFolder.
     */
    public Path getPath()
    {
        return this.folder.getPath();
    }

    /**
     * Get whether or not this QubPublisherFolder exists.
     * @return Whether or not this QubPublisherFolder exists.
     */
    public Result<Boolean> exists()
    {
        return this.folder.exists();
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.folder.getParentFolder()
            .then(QubFolder::new);
    }

    public String getPublisherName()
    {
        return this.folder.getName();
    }

    /**
     * Get the project folders that are present in this QubPublisherFolder.
     * @return The project folders that are present in this QubPublisherFolder.
     */
    public Result<Iterable<QubProjectFolder>> getProjectFolders()
    {
        return this.folder.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubProjectFolder::new));
    }

    /**
     * Get a QubProjectFolder for the project with the provided name.
     * @param projectName The name of the project.
     * @return The QubProjectFolder for the project with the provided name.
     */
    public Result<QubProjectFolder> getProjectFolder(String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.folder.getFolder(projectName)
            .then(QubProjectFolder::new);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof QubPublisherFolder && this.equals((QubPublisherFolder)rhs);
    }

    /**
     * Get whether or not this QubPublisherFolder is equal to the provided QubPublisherFolder.
     * @param rhs The QubPublisherFolder to compare against this QubPublisherFolder.
     * @return Whether or not this QubPublisherFolder is equal to the provided QubPublisherFolder.
     */
    public boolean equals(QubPublisherFolder rhs)
    {
        return rhs != null &&
            this.folder.equals(rhs.folder);
    }

    @Override
    public String toString()
    {
        return this.folder.toString();
    }
}
