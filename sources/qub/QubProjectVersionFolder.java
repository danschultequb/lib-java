package qub;

public class QubProjectVersionFolder extends Folder implements Comparable<QubProjectVersionFolder>
{
    private QubProjectVersionFolder(Folder projectVersionFolder)
    {
        super(projectVersionFolder.getFileSystem(), projectVersionFolder.getPath());
    }

    public static QubProjectVersionFolder get(Folder projectVersionFolder)
    {
        PreCondition.assertNotNull(projectVersionFolder, "projectVersionFolder");
        PreCondition.assertGreaterThanOrEqualTo(projectVersionFolder.getPath().getSegments().getCount(), 5, "projectVersionFolder.getPath().getSegments().getCount()");

        return new QubProjectVersionFolder(projectVersionFolder);
    }

    public Result<QubFolder> getQubFolder()
    {
        return this.getProjectFolder()
            .then((QubProjectFolder projectFolder) -> projectFolder.getQubFolder().await());
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
        return Result.create(() ->
        {
            final Folder versionsFolder = this.getParentFolder().await();
            final Folder projectFolder = versionsFolder.getParentFolder().await();
            return QubProjectFolder.get(projectFolder);
        });
    }

    public Result<String> getProjectName()
    {
        return this.getProjectFolder()
            .then(QubProjectFolder::getProjectName);
    }

    public Result<? extends VersionNumber> getVersion()
    {
        return VersionNumber.parse(this.getName());
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
    public Result<ProjectSignature> getProjectSignature()
    {
        return Result.create(() ->
        {
            final String publisher = this.getPublisherName().await();
            final String project = this.getProjectName().await();
            final VersionNumber version = this.getVersion().await();
            return ProjectSignature.create(publisher, project, version);
        });
    }

    public Result<Folder> getProjectVersionsFolder()
    {
        return this.getProjectFolder()
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectVersionsFolder().await());
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

    @Override
    public Comparison compareWith(QubProjectVersionFolder rhs)
    {
        Comparison result;

        if (rhs == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Comparer.compare(this.getPublisherName().await(), rhs.getPublisherName().await());
            if (result == Comparison.Equal)
            {
                result = Comparer.compare(this.getProjectName().await(), rhs.getProjectName().await());
                if (result == Comparison.Equal)
                {
                    result = this.getVersion().await().compareWith(rhs.getVersion().await());
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
