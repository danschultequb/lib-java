package qub;

public class ProcessBuilderFactory
{
    private final ProcessRunner processRunner;
    private final EnvironmentVariables environmentVariables;
    private final Folder currentFolder;

    public ProcessBuilderFactory(ProcessRunner processRunner, EnvironmentVariables environmentVariables, Folder currentFolder)
    {
        PreCondition.assertNotNull(processRunner, "processRunner");
        PreCondition.assertNotNull(environmentVariables, "environmentVariables");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        this.processRunner = processRunner;
        this.environmentVariables = environmentVariables;
        this.currentFolder = currentFolder;
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.create(() ->
        {
            final File executableFile = findExecutableFile(executablePath, true)
                .catchError(FileNotFoundException.class, () -> findExecutableFile(executablePath, false).await())
                .await();
            return new ProcessBuilder(this.processRunner, executableFile, this.currentFolder);
        });
    }

    public Result<File> getExecutableFile(final Path executableFilePath, boolean checkExtensions)
    {
        PreCondition.assertNotNull(executableFilePath, "executableFilePath");
        PreCondition.assertTrue(executableFilePath.isRooted(), "executableFilePath.isRooted()");

        return Result.create(() ->
        {
            File result = null;
            final FileSystem fileSystem = this.currentFolder.getFileSystem();
            if (checkExtensions)
            {
                if (fileSystem.fileExists(executableFilePath).await())
                {
                    result = fileSystem.getFile(executableFilePath).await();
                }
                else
                {
                    throw new FileNotFoundException(executableFilePath);
                }
            }
            else
            {
                final Path executablePathWithoutExtension = executableFilePath.withoutFileExtension();

                final Path folderPath = executableFilePath.getParent().await();
                final Folder folder = fileSystem.getFolder(folderPath).await();
                final Iterable<File> files = folder.getFiles().await();

                final String[] executableExtensions = new String[] { "", ".exe", ".bat", ".cmd" };
                for (final String executableExtension : executableExtensions)
                {
                    final Path executablePathWithExtension = Strings.isNullOrEmpty(executableExtension)
                        ? executablePathWithoutExtension
                        : executablePathWithoutExtension.concatenate(executableExtension);
                    result = files.first((File file) -> executablePathWithExtension.equals(file.getPath()));
                    if (result != null)
                    {
                        break;
                    }
                }

                if (result == null)
                {
                    result = files.first((File file) -> executablePathWithoutExtension.equals(file.getPath().withoutFileExtension()));
                    if (result == null)
                    {
                        throw new FileNotFoundException(executablePathWithoutExtension);
                    }
                }
            }
            return result;
        });
    }

    /**
     * Find the file to execute based on the provided executablePath. If the executablePath is not
     * rooted, then this function will check if a file exists by resolving the executablePath
     * against the current folder. If no file exists from that resolved path, then this function
     * will resolve the executablePath against each of the segments in the PATH environment
     * variable.
     * @param executablePath The path to the executable to run.
     * @param checkExtensions Whether or not file extensions should be considered when comparing
     *                        files. If this is true, the executablePath "program.exe" would not
     *                        match "program.cmd", but it would match if checkExtensions is false.
     * @return The file to execute.
     */
    public Result<File> findExecutableFile(Path executablePath, boolean checkExtensions)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.create(() ->
        {
            File result;

            if (executablePath.isRooted())
            {
                result = this.getExecutableFile(executablePath, checkExtensions).await();
            }
            else
            {
                final Path currentFolderPath = this.currentFolder.getPath();
                final Path currentFolderExecutablePath = currentFolderPath.concatenateSegment(executablePath);

                result = getExecutableFile(currentFolderExecutablePath, checkExtensions)
                    .catchError(FileNotFoundException.class)
                    .await();

                if (result == null)
                {
                    final String pathEnvironmentVariable = this.environmentVariables.get("path")
                        .catchError(NotFoundException.class)
                        .await();
                    if (!Strings.isNullOrEmpty(pathEnvironmentVariable))
                    {
                        final Iterable<String> pathStrings = Iterable.create(pathEnvironmentVariable.split(";"));
                        for (final String pathString : pathStrings)
                        {
                            if (!Strings.isNullOrEmpty(pathString))
                            {
                                final Path path = Path.parse(pathString);
                                final Path resolvedExecutablePath = path.concatenateSegment(executablePath);
                                result = getExecutableFile(resolvedExecutablePath, checkExtensions).catchError().await();
                                if (result != null)
                                {
                                    break;
                                }
                            }
                        }
                    }

                    if (result == null)
                    {
                        throw new qub.FileNotFoundException(executablePath);
                    }
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }
}
