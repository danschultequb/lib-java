package qub;

/**
 * A ProcessFactory implementation that invokes external processes.
 */
public class RealProcessFactory implements ProcessFactory
{
    private final AsyncRunner parallelAsyncRunner;
    private final EnvironmentVariables environmentVariables;
    private final Folder currentFolder;

    public RealProcessFactory(AsyncRunner parallelAsyncRunner, EnvironmentVariables environmentVariables, Folder currentFolder)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");
        PreCondition.assertNotNull(environmentVariables, "environmentVariables");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        this.parallelAsyncRunner = parallelAsyncRunner;
        this.environmentVariables = environmentVariables;
        this.currentFolder = currentFolder;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.currentFolder.getPath();
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    @Override
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.create(() ->
        {
            final File executableFile = findExecutableFile(executablePath, true)
                .catchError(FileNotFoundException.class, () -> findExecutableFile(executablePath, false).await())
                .await();
            return new JavaProcessBuilder(this, executableFile.getPath(), this.currentFolder.getPath());
        });
    }

    @Override
    public Result<Integer> run(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectOutputAction, Action1<ByteReadStream> redirectErrorAction)
    {
        PreCondition.assertNotNull(executablePath, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        return Result.create(() ->
        {
            final java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(executablePath.toString());

            if (arguments.any())
            {
                for (final String argument : arguments)
                {
                    builder.command().add(argument);
                }
            }

            builder.directory(new java.io.File(workingFolderPath.toString()));

            if (redirectedInputStream != null)
            {
                builder.redirectInput();
            }

            if (redirectOutputAction != null)
            {
                builder.redirectOutput();
            }

            if (redirectErrorAction != null)
            {
                builder.redirectError();
            }

            int exitCode;
            try
            {
                final java.lang.Process process = builder.start();

                final Result<Void> inputAction = redirectedInputStream == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() ->
                        {
                            final OutputStreamToByteWriteStream processInputStream = new OutputStreamToByteWriteStream(process.getOutputStream(), true);
                            processInputStream.writeAllBytes(redirectedInputStream).catchError().await();
                        });

                final Result<Void> outputAction = redirectOutputAction == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() -> redirectOutputAction.run(new InputStreamToByteReadStream(process.getInputStream())));

                final Result<Void> errorAction = redirectErrorAction == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() -> redirectErrorAction.run(new InputStreamToByteReadStream(process.getErrorStream())));

                exitCode = process.waitFor();

                Result.await(inputAction, outputAction, errorAction);
            }
            catch (Throwable e)
            {
                throw Exceptions.asRuntime(e);
            }

            return exitCode;
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
