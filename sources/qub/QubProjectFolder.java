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

    public Iterator<QubProjectVersionFolder> iterateProjectVersionFolders()
    {
        return LazyIterator.create(() ->
        {
            final Folder versionsFolder = this.getProjectVersionsFolder().await();
            return versionsFolder.iterateFolders()
                .catchError(FolderNotFoundException.class)
                .map(QubProjectVersionFolder::get);
        });
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

    public Result<QubProjectVersionFolder> getProjectVersionFolder(VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return Result.create(() ->
        {
            final Folder versionsFolder = this.getProjectVersionsFolder().await();
            final Folder versionFolder = versionsFolder.getFolder(version.toString()).await();
            return QubProjectVersionFolder.get(versionFolder);
        });
    }

    public Result<QubProjectVersionFolder> getLatestProjectVersionFolder()
    {
        return this.getLatestProjectVersionFolder(Comparer::compare);
    }

    public Result<QubProjectVersionFolder> getLatestProjectVersionFolder(Comparer<QubProjectVersionFolder> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        return Result.create(() ->
        {
            QubProjectVersionFolder result;
            final Iterator<QubProjectVersionFolder> projectVersionFolders = this.iterateProjectVersionFolders();
            if (!projectVersionFolders.any())
            {
                throw new NotFoundException("No project named " + this.getPublisherName().await() + "/" + this.getProjectName() + " has been published.");
            }
            else
            {
                result = projectVersionFolders.takeCurrent();
                for (final QubProjectVersionFolder projectVersionFolder : projectVersionFolders)
                {
                    if (comparer.run(result, projectVersionFolder) == Comparison.LessThan)
                    {
                        result = projectVersionFolder;
                    }
                }
            }
            return result;
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

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getProjectJSONFile(VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getCompiledSourcesFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile().await());
    }

    public Result<File> getCompiledSourcesFile(VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile().await());
    }

    public Result<File> getSourcesFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile().await());
    }

    public Result<File> getSourcesFile(VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile().await());
    }

    public Result<File> getCompiledTestsFile(String version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile().await());
    }

    public Result<File> getCompiledTestsFile(VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile().await());
    }
}
