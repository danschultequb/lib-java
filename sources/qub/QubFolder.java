package qub;

public class QubFolder
{
    private final Folder folder;

    /**
     * Create a new QubFolder object from the provided folder.
     * @param folder The folder that the QubFolder object will operate on.
     */
    public QubFolder(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        this.folder = folder;
    }

    /**
     * Get the path to this QubFolder.
     * @return The path to this QubFolder.
     */
    public Path getPath()
    {
        return this.folder.getPath();
    }

    /**
     * Get whether or not this QubFolder exists.
     * @return Whether or not this QubFolder exists.
     */
    public Result<Boolean> exists()
    {
        return this.folder.exists();
    }

    public Result<File> getShortcutFile(String shortcutName)
    {
        PreCondition.assertNotNullAndNotEmpty(shortcutName, "shortcutName");

        return this.folder.getFile(shortcutName);
    }

    /**
     * Get the publisher folders that are present in this QubFolder.
     * @return The publisher folders that are present in this QubFolder.
     */
    public Result<Iterable<QubPublisherFolder>> getPublisherFolders()
    {
        return this.folder.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubPublisherFolder::new));
    }

    /**
     * Get a QubPublisherFolder for the publisher with the provided name.
     * @param publisherName The name of the publisher.
     * @return The QubPublisherFolder for the publisher with the provided name.
     */
    public Result<QubPublisherFolder> getPublisherFolder(String publisherName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");

        return this.folder.getFolder(publisherName)
            .then(QubPublisherFolder::new);
    }

    public Result<Iterable<QubProjectFolder>> getProjectFolders(String publisherName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");

        return this.getPublisherFolder(publisherName)
            .then((QubPublisherFolder publisherFolder) -> publisherFolder.getProjectFolders().await());
    }

    public Result<QubProjectFolder> getProjectFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.getPublisherFolder(publisherName)
            .then((QubPublisherFolder publisherFolder) -> publisherFolder.getProjectFolder(projectName).await());
    }

    public Result<Iterable<QubProjectVersionFolder>> getProjectVersionFolders(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.getProjectFolder(publisherName, projectName)
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectVersionFolders().await());
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectFolder(publisherName, projectName)
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectVersionFolder(version).await());
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof QubFolder && this.equals((QubFolder)rhs);
    }

    /**
     * Get whether or not this QubFolder is equal to the provided QubFolder.
     * @param rhs The QubFolder to compare against this QubFolder.
     * @return Whether or not this QubFolder is equal to the provided QubFolder.
     */
    public boolean equals(QubFolder rhs)
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
