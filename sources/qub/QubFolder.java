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

    public static Result<QubFolder> getFromType(FileSystem fileSystem, String typeFullName)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNullAndNotEmpty(typeFullName, "typeFullName");

        return Result.create(() ->
        {
            final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.getFromType(fileSystem, typeFullName).await();
            return projectVersionFolder.getQubFolder().await();
        });
    }

    public static Result<QubFolder> getFromType(FileSystem fileSystem, Class<?> type)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(type, "type");

        return Result.create(() ->
        {
            final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.getFromType(fileSystem, type).await();
            return projectVersionFolder.getQubFolder().await();
        });
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
    public Result<Iterable<QubPublisherFolder>> getPublisherFolders()
    {
        return this.getFolders()
            .catchError(FolderNotFoundException.class, () -> Iterable.create())
            .then((Iterable<Folder> folders) -> folders.map(QubPublisherFolder::get));
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
            .then((Folder folder) -> QubPublisherFolder.get(folder));
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

    public Result<Folder> getProjectDataFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.getProjectFolder(publisherName, projectName)
            .then((QubProjectFolder projectFolder) -> projectFolder.getProjectDataFolder().await());
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

    public Result<File> getProjectJSONFile(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(publisherName, projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getProjectJSONFile().await());
    }

    public Result<File> getProjectJSONFile(String publisherName, String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectJSONFile(publisherName, projectName, version.toString());
    }

    public Result<File> getProjectJSONFile(ProjectSignature projectSignature)
    {
        PreCondition.assertNotNull(projectSignature, "projectSignature");

        return this.getProjectJSONFile(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion());
    }

    public Result<File> getCompiledSourcesFile(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(publisherName, projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledSourcesFile().await());
    }

    public Result<File> getCompiledSourcesFile(String publisherName, String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getCompiledSourcesFile(publisherName, projectName, version.toString());
    }

    public Result<File> getSourcesFile(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(publisherName, projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getSourcesFile().await());
    }

    public Result<File> getSourcesFile(String publisherName, String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getSourcesFile(publisherName, projectName, version.toString());
    }

    public Result<File> getCompiledTestsFile(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(publisherName, projectName, version)
            .then((QubProjectVersionFolder projectVersionFolder) -> projectVersionFolder.getCompiledTestsFile().await());
    }

    public Result<File> getCompiledTestsFile(String publisherName, String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getCompiledTestsFile(publisherName, projectName, version.toString());
    }
}
