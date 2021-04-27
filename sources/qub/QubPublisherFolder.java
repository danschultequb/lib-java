package qub;

public class QubPublisherFolder extends Folder
{
    private QubPublisherFolder(Folder publisherFolder)
    {
        super(publisherFolder.getFileSystem(), publisherFolder.getPath());
    }

    public static QubPublisherFolder get(Folder publisherFolder)
    {
        PreCondition.assertNotNull(publisherFolder, "publisherFolder");
        PreCondition.assertGreaterThanOrEqualTo(publisherFolder.getPath().getSegments().getCount(), 2, "publisherFolder.getPath().getSegments().getCount()");

        return new QubPublisherFolder(publisherFolder);
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getParentFolder()
            .then(QubFolder::get);
    }

    public String getPublisherName()
    {
        return this.getName();
    }

    /**
     * Get the project folders that are present in this QubPublisherFolder.
     * @return The project folders that are present in this QubPublisherFolder.
     */
    public Iterator<QubProjectFolder> iterateProjectFolders()
    {
        return this.iterateFolders()
            .catchError(FolderNotFoundException.class)
            .map(QubProjectFolder::get);
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
            .then(QubProjectFolder::get);
    }

    public Result<Folder> getProjectDataFolder(String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.getProjectFolder(projectName)
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectDataFolder().await());
    }

    public Iterator<QubProjectVersionFolder> iterateProjectVersionFolders(String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return LazyIterator.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(projectName).await();
            return projectFolder.iterateProjectVersionFolders();
        });
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectFolder(projectName)
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectVersionFolder(version).await());
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectFolder(projectName)
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectVersionFolder(version).await());
    }

    public Result<File> getProjectJSONFile(String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getProjectJSONFile(String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getCompiledSourcesFile(String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile().await());
    }

    public Result<File> getCompiledSourcesFile(String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile().await());
    }

    public Result<File> getSourcesFile(String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile().await());
    }

    public Result<File> getSourcesFile(String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile().await());
    }

    public Result<File> getCompiledTestsFile(String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile().await());
    }

    public Result<File> getCompiledTestsFile(String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile().await());
    }
}
