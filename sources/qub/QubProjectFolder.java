package qub;

public class QubProjectFolder
{
    private final Folder folder;

    public QubProjectFolder(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        this.folder = folder;
    }

    /**
     * Get the path to this QubProjectFolder.
     * @return The path to this QubProjectFolder.
     */
    public Path getPath()
    {
        return this.folder.getPath();
    }

    /**
     * Get whether or not this QubProjectFolder exists.
     * @return Whether or not this QubProjectFolder exists.
     */
    public Result<Boolean> exists()
    {
        return this.folder.exists();
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getPublisherFolder()
            .then((QubPublisherFolder publisherFolder) -> publisherFolder.getQubFolder().await());
    }

    public Result<QubPublisherFolder> getPublisherFolder()
    {
        return this.folder.getParentFolder()
            .then(QubPublisherFolder::new);
    }

    public Result<String> getPublisherName()
    {
        return this.getPublisherFolder()
            .then(QubPublisherFolder::getPublisherName);
    }

    public String getProjectName()
    {
        return this.folder.getName();
    }

    public Result<Iterable<QubProjectVersionFolder>> getProjectVersionFolders()
    {
        return this.folder.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubProjectVersionFolder::new));
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.folder.getFolder(version)
            .then(QubProjectVersionFolder::new);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof QubProjectFolder && this.equals((QubProjectFolder)rhs);
    }

    /**
     * Get whether or not this QubProjectFolder is equal to the provided QubProjectFolder.
     * @param rhs The QubProjectFolder to compare against this QubProjectFolder.
     * @return Whether or not this QubProjectFolder is equal to the provided QubProjectFolder.
     */
    public boolean equals(QubProjectFolder rhs)
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
