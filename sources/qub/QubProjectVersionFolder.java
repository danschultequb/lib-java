package qub;

public class QubProjectVersionFolder extends Folder
{
    private QubProjectVersionFolder(Folder projectVersionFolder)
    {
        super(projectVersionFolder.getFileSystem(), projectVersionFolder.getPath());
    }

    public static Result<QubProjectVersionFolder> create(Path projectVersionEntryPath, FileSystem fileSystem)
    {
        PreCondition.assertNotNull(projectVersionEntryPath, "projectVersionEntryPath");
        PreCondition.assertNotNull(fileSystem, "fileSystem");

        return Result.create(() ->
        {
            QubProjectVersionFolder result = null;
            final File projectVersionFile = fileSystem.getFile(projectVersionEntryPath).await();
            if (projectVersionFile.exists().await())
            {
                result = QubProjectVersionFolder.create(projectVersionFile);
            }
            else
            {
                final Folder projectVersionFolder = fileSystem.getFolder(projectVersionEntryPath).await();
                if (projectVersionFolder.exists().await())
                {
                    result = QubProjectVersionFolder.create(projectVersionFolder);
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    public static Result<QubProjectVersionFolder> create(Class<?> type, FileSystem fileSystem)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(fileSystem, "fileSystem");

        return Result.create(() ->
        {
            final Path typeContainerPath = Types.getTypeContainerPath(type).await();
            return QubProjectVersionFolder.create(typeContainerPath, fileSystem).await();
        });
    }

    public static QubProjectVersionFolder create(File projectVersionFile)
    {
        PreCondition.assertNotNull(projectVersionFile, "projectVersionFile");
        PreCondition.assertGreaterThanOrEqualTo(projectVersionFile.getPath().getSegments().getCount(), 5, "projectVersionFile.getPath().getSegments().getCount()");

        return QubProjectVersionFolder.create(projectVersionFile.getParentFolder().await());
    }

    public static QubProjectVersionFolder create(Folder projectVersionFolder)
    {
        PreCondition.assertNotNull(projectVersionFolder, "projectVersionFolder");
        PreCondition.assertGreaterThanOrEqualTo(projectVersionFolder.getPath().getSegments().getCount(), 4, "projectVersionFolder.getPath().getSegments().getCount()");

        return new QubProjectVersionFolder(projectVersionFolder);
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
        return this.getParentFolder()
            .then((Folder folder) -> QubProjectFolder.create(folder));
    }

    public Result<String> getProjectName()
    {
        return this.getProjectFolder()
            .then(QubProjectFolder::getProjectName);
    }

    public String getVersion()
    {
        return this.getName();
    }

    public Result<File> getProjectJSONFile()
    {
        return this.getFile("project.json");
    }

    public Result<File> getCompiledSourcesFile()
    {
        return this.getFile(this.getProjectName().await() + ".jar");
    }

    public Result<File> getSourcesFile()
    {
        return this.getFile(this.getProjectName().await() + ".sources.jar");
    }

    public Result<File> getCompiledTestsFile()
    {
        return this.getFile(this.getProjectName().await() + ".tests.jar");
    }

    /**
     * Get the ProjectSignature for this project version folder.
     * @return The ProjectSignature for this project version folder.
     */
    public ProjectSignature getProjectSignature()
    {
        final String publisher = this.getPublisherName().await();
        final String project = this.getProjectName().await();
        final String version = this.getVersion();
        return new ProjectSignature(publisher, project, version);
    }

    /**
     * Get the data folder system that is associated with this Qub project.
     * @return The data file system that is associated with this Qub project.
     */
    public Result<Folder> getProjectDataFolder()
    {
        return Result.create(() ->
        {
            return this.getProjectFolder().await()
                .getProjectDataFolder().await();
        });
    }
}
