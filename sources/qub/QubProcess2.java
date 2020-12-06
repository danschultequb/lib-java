package qub;

/**
 * An interface for a process/application that runs from the Qub folder.
 */
public interface QubProcess2 extends Process
{
    /**
     * Get the Qub folder that contains the main binaries for this process.
     * @return The Qub folder that contains the main binaries for this process.
     */
    default Result<QubFolder> getQubFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getQubFolder().await();
        });
    }

    /**
     * Get the name of the current process's publisher.
     * @return The name of the current process's publisher.
     */
    default Result<String> getPublisherName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherName().await();
        });
    }

    default Result<QubPublisherFolder> getQubPublisherFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherFolder().await();
        });
    }

    /**
     * Get the name of the current process's project.
     * @return The name of the current process's project.
     */
    default Result<String> getProjectName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectName().await();
        });
    }

    default Result<QubProjectFolder> getQubProjectFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectFolder().await();
        });
    }

    /**
     * Get the data folder that is associated with the current process's project.
     * @return The data folder that is associated with the current process's project.
     */
    default Result<Folder> getQubProjectDataFolder()
    {
        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getQubProjectFolder().await();
            return projectFolder.getProjectDataFolder().await();
        });
    }

    /**
     * Get the version of the current process's project.
     * @return The version of the current process's project.
     */
    default Result<VersionNumber> getVersion()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getVersion().await();
        });
    }

    /**
     * Get the QubProjectVersionFolder for the current process.
     * @return The QubProjectVersionFolder for the current process.
     */
    Result<QubProjectVersionFolder> getQubProjectVersionFolder();
}
