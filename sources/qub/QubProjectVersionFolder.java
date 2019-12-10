package qub;

public class QubProjectVersionFolder
{
    private final Folder folder;

    public QubProjectVersionFolder(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        this.folder = folder;
    }

    /**
     * Get the path to this QubProjectVersionFolder.
     * @return The path to this QubProjectVersionFolder.
     */
    public Path getPath()
    {
        return this.folder.getPath();
    }

    /**
     * Get whether or not this QubProjectVersionFolder exists.
     * @return Whether or not this QubProjectVersionFolder exists.
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
        return this.getProjectFolder()
            .then((QubProjectFolder projectFolder) -> projectFolder.getPublisherFolder().await());
    }

    public Result<String> getPublisherName()
    {
        return this.getPublisherFolder()
            .then(QubPublisherFolder::getPublisherName);
    }

    public Result<QubProjectFolder> getProjectFolder()
    {
        return this.folder.getParentFolder()
            .then(QubProjectFolder::new);
    }

    public Result<String> getProjectName()
    {
        return this.getProjectFolder()
            .then(QubProjectFolder::getProjectName);
    }

    public String getVersion()
    {
        return this.folder.getName();
    }

    public Result<File> getProjectJSONFile()
    {
        return this.folder.getFile("project.json");
    }

    public Result<File> getCompiledSourcesFile()
    {
        return this.folder.getFile(this.getProjectName().await() + ".jar");
    }

    public Result<File> getSourcesFile()
    {
        return this.folder.getFile(this.getProjectName().await() + ".sources.jar");
    }

    public Result<File> getCompiledTestsFile()
    {
        return this.folder.getFile(this.getProjectName().await() + ".tests.jar");
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof QubProjectVersionFolder && this.equals((QubProjectVersionFolder)rhs);
    }

    /**
     * Get whether or not this QubProjectVersionFolder is equal to the provided QubProjectVersionFolder.
     * @param rhs The QubProjectVersionFolder to compare against this QubProjectVersionFolder.
     * @return Whether or not this QubProjectVersionFolder is equal to the provided QubProjectVersionFolder.
     */
    public boolean equals(QubProjectVersionFolder rhs)
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
