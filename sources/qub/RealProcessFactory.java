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
        return this.currentFolder.getPath().normalize();
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
            return new BasicProcessBuilder(this, executablePath, this.getWorkingFolderPath());
        });
    }

    @Override
    public Result<ChildProcess> start(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectOutputAction, Action1<ByteReadStream> redirectErrorAction, CharacterWriteStream verbose)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        return Result.create(() ->
        {
            final File executableFile = this.findExecutableFile(executablePath, true, workingFolderPath, verbose)
                .catchError(FileNotFoundException.class, () -> this.findExecutableFile(executablePath, false, workingFolderPath, verbose).await())
                .await();

            final java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(executableFile.toString());

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

            ChildProcess result;
            try
            {
                if (verbose != null)
                {
                    verbose.write(workingFolderPath.toString()).await();
                    verbose.write(':').await();
                    for (final String commandPart : builder.command())
                    {
                        verbose.write(' ').await();
                        verbose.write(commandPart).await();
                    }
                    verbose.writeLine();
                }
                final java.lang.Process childProcess = builder.start();

                final Result<Void> inputAction = redirectedInputStream == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() ->
                        {
                            final OutputStreamToByteWriteStream processInputStream = new OutputStreamToByteWriteStream(childProcess.getOutputStream(), true);
                            processInputStream.writeAll(redirectedInputStream).catchError().await();
                        });

                final Result<Void> outputAction = redirectOutputAction == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() -> redirectOutputAction.run(new InputStreamToByteReadStream(childProcess.getInputStream())));

                final Result<Void> errorAction = redirectErrorAction == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() -> redirectErrorAction.run(new InputStreamToByteReadStream(childProcess.getErrorStream())));

                final Function0<Integer> processFunction = () ->
                {
                    final int exitCode;
                    try
                    {
                        exitCode = childProcess.waitFor();
                    }
                    catch (Throwable e)
                    {
                        throw Exceptions.asRuntime(e);
                    }
                    return exitCode;
                };

                result = BasicChildProcess.create(processFunction, inputAction, outputAction, errorAction);
            }
            catch (Throwable e)
            {
                throw Exceptions.asRuntime(e);
            }

            return result;
        });
    }

    public Result<File> getExecutableFile(Path executableFilePath, boolean checkExtensions, CharacterWriteStream verbose)
    {
        PreCondition.assertNotNull(executableFilePath, "executableFilePath");
        PreCondition.assertTrue(executableFilePath.isRooted(), "executableFilePath.isRooted()");

        return Result.create(() ->
        {
            File result = null;
            final FileSystem fileSystem = this.currentFolder.getFileSystem();
            if (checkExtensions)
            {
                if (verbose != null)
                {
                    verbose.write("Checking " + Strings.escapeAndQuote(executableFilePath) + "... ").await();
                }
                if (fileSystem.fileExists(executableFilePath).await())
                {
                    if (verbose != null)
                    {
                        verbose.writeLine("Yes!").await();
                    }
                    result = fileSystem.getFile(executableFilePath).await();
                }
                else
                {
                    if (verbose != null)
                    {
                        verbose.writeLine("No.").await();
                    }
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
                    if (verbose != null)
                    {
                        verbose.write("Checking " + Strings.escapeAndQuote(executablePathWithExtension) + "... ").await();
                    }
                    result = files.first((File file) -> executablePathWithExtension.equals(file.getPath()));
                    if (result != null)
                    {
                        if (verbose != null)
                        {
                            verbose.writeLine("Yes!").await();
                        }
                        break;
                    }
                    else
                    {
                        if (verbose != null)
                        {
                            verbose.writeLine("No.").await();
                        }
                    }
                }

                if (result == null)
                {
                    if (verbose != null)
                    {
                        verbose.write("Checking " + Strings.escapeAndQuote(executablePathWithoutExtension) + "... ").await();
                    }
                    result = files.first((File file) -> executablePathWithoutExtension.equals(file.getPath().withoutFileExtension()));
                    if (result == null)
                    {
                        if (verbose != null)
                        {
                            verbose.writeLine("No.").await();
                        }
                        throw new FileNotFoundException(executablePathWithoutExtension);
                    }
                    else
                    {
                        if (verbose != null)
                        {
                            verbose.writeLine("Yes!").await();
                        }
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
    public Result<File> findExecutableFile(Path executablePath, boolean checkExtensions, Path workingFolderPath, CharacterWriteStream verbose)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        return Result.create(() ->
        {
            if (verbose != null)
            {
                verbose.writeLine("Looking for executable: " + Strings.escapeAndQuote(executablePath) + " (check extensions: " + checkExtensions + ")").await();
            }

            File result;

            if (executablePath.isRooted())
            {
                result = this.getExecutableFile(executablePath.normalize(), checkExtensions, verbose).await();
            }
            else
            {
                final Path currentFolderExecutablePath = workingFolderPath.concatenateSegment(executablePath);

                result = this.getExecutableFile(currentFolderExecutablePath.normalize(), checkExtensions, verbose)
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
                            final String resolvedPathString = this.environmentVariables.resolve(pathString);
                            if (Strings.isNullOrEmpty(resolvedPathString))
                            {
                                if (verbose != null)
                                {
                                    verbose.writeLine("WARNING: Found null or empty path string.").await();
                                }
                            }
                            else
                            {
                                final Path path = Path.parse(resolvedPathString);
                                if (!path.isRooted())
                                {
                                    if (verbose != null)
                                    {
                                        verbose.writeLine("WARNING: Skipping relative path string (" + Strings.escapeAndQuote(resolvedPathString) + ").").await();
                                    }
                                }
                                else
                                {
                                    final Path resolvedExecutablePath = path.concatenateSegment(executablePath).normalize();
                                    result = this.getExecutableFile(resolvedExecutablePath, checkExtensions, verbose).catchError().await();
                                    if (result != null)
                                    {
                                        break;
                                    }
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
