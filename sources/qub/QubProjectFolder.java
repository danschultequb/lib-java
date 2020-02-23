package qub;

public class QubProjectFolder extends Folder
{
    private QubProjectFolder(Folder folder)
    {
        super(folder.getFileSystem(), folder.getPath());
    }

    public static QubProjectFolder get(Folder projectFolder)
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
            .then((Folder folder) -> QubPublisherFolder.get(folder));
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

    public Result<Iterable<QubProjectVersionFolder>>  getProjectVersionFolders()
    {
        final Folder versionsFolder = this.getProjectVersionsFolder().await();
        return versionsFolder.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubProjectVersionFolder::get));
    }

    public Result<Iterable<QubProjectVersionFolder>> getProjectVersionFolders2()
    {
        return this.getProjectVersionFolders();
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return Result.create(() ->
        {
            final Folder versionsFolder = this.getProjectVersionsFolder().await();
            final Folder versionFolder = versionsFolder.getFolder(version).await();
            return QubProjectVersionFolder.get(versionFolder);
        });
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder2(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return Result.create(() ->
        {
            final Folder versionsFolder = this.getProjectVersionsFolder().await();
            final Folder versionFolder = versionsFolder.getFolder(version).await();
            return QubProjectVersionFolder.get(versionFolder);
        });
    }

    /**
     * Get the data folder system that is associated with this Qub project.
     * @return The data file system that is associated with this Qub project.
     */
    public Result<Folder> getProjectDataFolder()
    {
        return this.getFolder("data");
    }

    public Result<Folder> getProjectVersionsFolder()
    {
        return this.getFolder("versions");
    }

    public Result<File> getProjectJSONFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getProjectJSONFile2(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getCompiledSourcesFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile2().await());
    }

    public Result<File> getCompiledSourcesFile2(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile2().await());
    }

    public Result<File> getSourcesFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile2().await());
    }

    public Result<File> getSourcesFile2(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile2().await());
    }

    public Result<File> getCompiledTestsFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile2().await());
    }

    public Result<File> getCompiledTestsFile2(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder2(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile2().await());
    }
}
