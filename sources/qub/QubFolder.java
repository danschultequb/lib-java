package qub;

public class QubFolder extends Folder
{
    private QubFolder(Folder qubFolder)
    {
        super(qubFolder.getFileSystem(), qubFolder.getPath());
    }

    public static QubFolder get(Folder qubFolder)
    {
        PreCondition.assertNotNull(qubFolder, "qubFolder");

        return new QubFolder(qubFolder);
    }

    public Result<File> getShortcutFile(String shortcutName)
    {
        PreCondition.assertNotNullAndNotEmpty(shortcutName, "shortcutName");

        return this.getFile(shortcutName);
    }

    /**
     * Get the publisher folders that are present in this QubFolder.
     *
     * @return The publisher folders that are present in this QubFolder.
     */
    public Iterator<QubPublisherFolder> iteratePublisherFolders()
    {
        return this.iterateFolders()
            .catchError(FolderNotFoundException.class)
            .map(QubPublisherFolder::get);
    }

    /**
     * Get a QubPublisherFolder for the publisher with the provided name.
     *
     * @param publisherName The name of the publisher.
     * @return The QubPublisherFolder for the publisher with the provided name.
     */
    public Result<QubPublisherFolder> getPublisherFolder(String publisherName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");

        return this.getFolder(publisherName)
            .then(QubPublisherFolder::get);
    }

    public Iterator<QubProjectFolder> iterateProjectFolders(String publisherName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");

        return LazyIterator.create(() ->
        {
            final QubPublisherFolder publisherFolder = this.getPublisherFolder(publisherName).await();
            return publisherFolder.iterateProjectFolders();
        });
    }

    public Result<QubProjectFolder> getProjectFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return Result.create(() ->
        {
            final QubPublisherFolder publisherFolder = this.getPublisherFolder(publisherName).await();
            return publisherFolder.getProjectFolder(projectName).await();
        });
    }

    public Result<Folder> getProjectDataFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.getProjectDataFolder().await();
        });
    }

    public Iterator<QubProjectVersionFolder> iterateProjectVersionFolders(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return LazyIterator.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.iterateProjectVersionFolders();
        });
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.getProjectVersionFolder(version).await();
        });
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String publisherName, String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(publisherName, projectName, version.toString());
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(ProjectSignature projectSignature)
    {
        PreCondition.assertNotNull(projectSignature, "projectSignature");

        return this.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion());
    }

    public Result<QubProjectVersionFolder> getProjectLatestVersionFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.getLatestProjectVersionFolder().await();
        });
    }
}
