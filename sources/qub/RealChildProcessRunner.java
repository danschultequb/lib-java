package qub;

/**
 * A ProcessRunner implementation that invokes external processes.
 */
public class RealChildProcessRunner implements ChildProcessRunner
{
    private final AsyncRunner parallelAsyncRunner;
    private final Folder currentFolder;
    private final Iterable<Folder> foldersInPathEnvironmentVariable;
    private final Iterable<String> executableFileExtensions;

    private RealChildProcessRunner(AsyncRunner parallelAsyncRunner, Folder currentFolder, Iterable<Folder> foldersInPathEnvironmentVariable, Iterable<String> executableFileExtensions)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");
        PreCondition.assertNotNull(currentFolder, "currentFolder");
        PreCondition.assertNotNull(foldersInPathEnvironmentVariable, "foldersInPathEnvironmentVariable");
        PreCondition.assertNotNull(executableFileExtensions, "executableFileExtensions");

        this.parallelAsyncRunner = parallelAsyncRunner;
        this.currentFolder = currentFolder;
        this.foldersInPathEnvironmentVariable = foldersInPathEnvironmentVariable;
        this.executableFileExtensions = executableFileExtensions;
    }

    public static RealChildProcessRunner create(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final AsyncRunner parallelAsyncRunner = process.getParallelAsyncRunner();
        final EnvironmentVariables environmentVariables = process.getEnvironmentVariables();
        final Folder currentFolder = process.getCurrentFolder();
        return RealChildProcessRunner.create(parallelAsyncRunner, environmentVariables, currentFolder);
    }

    public static RealChildProcessRunner create(AsyncRunner parallelAsyncRunner, EnvironmentVariables environmentVariables, Folder currentFolder)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");
        PreCondition.assertNotNull(environmentVariables, "environmentVariables");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        final Iterable<Folder> foldersToSearch = RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, currentFolder.getFileSystem());
        final Iterable<String> executableFileExtensions = RealChildProcessRunner.getExecutableFileExtensions(environmentVariables);
        return RealChildProcessRunner.create(parallelAsyncRunner, currentFolder, foldersToSearch, executableFileExtensions);
    }

    public static RealChildProcessRunner create(AsyncRunner parallelAsyncRunner, Folder currentFolder, Iterable<Folder> foldersToSearch, Iterable<String> executableFileExtensions)
    {
        return new RealChildProcessRunner(parallelAsyncRunner, currentFolder, foldersToSearch, executableFileExtensions);
    }

    /**
     * Parse the PATH environment variable into Folder objects that will be searched when looking
     * for an executable.
     * @param environmentVariables The environment variables to get the PATH environment variable from.
     * @param fileSystem The file system that will be used to resolve the folder paths referenced
     *                   by the PATH environment variable.
     * @return The Folder objects that will be searched when looking for an executable.
     */
    public static Iterable<Folder> getFoldersInPathEnvironmentVariable(EnvironmentVariables environmentVariables, FileSystem fileSystem)
    {
        PreCondition.assertNotNull(environmentVariables, "environmentVariables");
        PreCondition.assertNotNull(fileSystem, "fileSystem");

        final List<Folder> result = List.create();
        final String pathString = environmentVariables.get("PATH").catchError().await();
        if (!Strings.isNullOrEmpty(pathString))
        {
            final String[] pathParts = pathString.split(";");
            for (final String pathPart : pathParts)
            {
                if (!Strings.isNullOrEmpty(pathPart))
                {
                    final String resolvedPathPart = environmentVariables.resolve(pathPart);
                    final Folder pathPartFolder = fileSystem.getFolder(resolvedPathPart).catchError().await();
                    if (pathPartFolder != null)
                    {
                        result.add(pathPartFolder);
                    }
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public static Iterable<String> getExecutableFileExtensions(EnvironmentVariables environmentVariables)
    {
        PreCondition.assertNotNull(environmentVariables, "environmentVariables");

        final List<String> result = List.create();
        final String pathExtString = environmentVariables.get("PATHEXT").catchError().await();
        if (!Strings.isNullOrEmpty(pathExtString))
        {
            final String[] pathExtParts = pathExtString.split(";");
            for (final String pathExtPart : pathExtParts)
            {
                if (!Strings.isNullOrEmpty(pathExtPart))
                {
                    result.add(environmentVariables.resolve(pathExtPart));
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Folder getCurrentFolder()
    {
        return this.currentFolder;
    }

    /**
     * Get the default folders that this process factory will search in when looking for an
     * executable file.
     * @return The default folders that this process factory will search in when looking for an
     * executable file.
     */
    public Iterable<Folder> getFoldersInPathEnvironmentVariable()
    {
        return this.foldersInPathEnvironmentVariable;
    }
    
    /**
     * Get the default executable file extensions that this process factory will add when looking
     * for an executable file.
     * @return The default executable file extensions that this process factory will add when
     * looking for an executable file.
     */
    public Iterable<String> getExecutableFileExtensions()
    {
        return this.executableFileExtensions;
    }

    /**
     * Find the executable file with the provided name in this process factory's default folders to
     * search and with this process factory's default executable file extensions.
     * @param executablePath The relative path of the executable file to find.
     * @return The found executable file.
     */
    public Result<File> findExecutableFile(Path executablePath, Folder workingFolder)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertFalse(executablePath.isRooted(), "executablePath.isRooted()");
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        return Result.create(() ->
        {
            File result = null;
            final List<Folder> foldersToSearch = List.create(workingFolder);
            if (!executablePath.contains('/') && !executablePath.contains('\\'))
            {
                foldersToSearch.addAll(this.foldersInPathEnvironmentVariable);
            }

            for (final Folder folderToSearch : foldersToSearch)
            {
                final File fileWithoutAddedExtension = folderToSearch.getFile(executablePath).await();
                if (fileWithoutAddedExtension.exists().await())
                {
                    result = fileWithoutAddedExtension;
                    break;
                }

                for (final String executableFileExtension : this.executableFileExtensions)
                {
                    String filePathWithAddedExtension = executablePath.toString();
                    if (!executablePath.endsWith(".") && !executableFileExtension.startsWith("."))
                    {
                        filePathWithAddedExtension += '.';
                    }
                    filePathWithAddedExtension += executableFileExtension;

                    final File fileWithAddedExtension = folderToSearch.getFile(filePathWithAddedExtension).await();

                    if (fileWithAddedExtension.exists().await())
                    {
                        result = fileWithAddedExtension;
                        break;
                    }
                }

                if (result != null)
                {
                    break;
                }
            }

            if (result == null)
            {
                throw new FileNotFoundException(workingFolder.getFile(executablePath).await());
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    @Override
    public Result<RealChildProcess> start(ChildProcessParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return Result.create(() ->
        {
            Folder workingFolder;
            final Path workingFolderPath = parameters.getWorkingFolderPath();
            if (workingFolderPath == null)
            {
                workingFolder = this.currentFolder;
            }
            else
            {
                final FileSystem fileSystem = this.currentFolder.getFileSystem();
                workingFolder = fileSystem.getFolder(workingFolderPath).await();
            }

            Path executablePath = parameters.getExecutablePath();
            if (!executablePath.isRooted())
            {
                executablePath = this.findExecutableFile(executablePath, workingFolder).await().getPath();
            }

            final java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(executablePath.toString());

            final Iterable<String> arguments = parameters.getArguments();
            if (!Iterable.isNullOrEmpty(arguments))
            {
                for (final String argument : arguments)
                {
                    builder.command().add(argument);
                }
            }

            builder.directory(new java.io.File(workingFolder.toString()));

            final ByteReadStream inputStream = parameters.getInputStream();
            if (inputStream != null)
            {
                builder.redirectInput();
            }

            final Action1<ByteReadStream> outputStreamHandler = parameters.getOutputStreamHandler();
            if (outputStreamHandler != null)
            {
                builder.redirectOutput();
            }

            final Action1<ByteReadStream> errorStreamHandler = parameters.getErrorStreamHandler();
            if (errorStreamHandler != null)
            {
                builder.redirectError();
            }

            RealChildProcess result;
            try
            {
                final java.lang.Process childProcess = builder.start();

                if (inputStream != null)
                {
                    this.parallelAsyncRunner.schedule(() ->
                    {
                        final OutputStreamToByteWriteStream processInputStream = OutputStreamToByteWriteStream.create(childProcess.getOutputStream(), true);
                        processInputStream.writeAll(inputStream).catchError().await();
                    });
                }

                final Result<Void> outputAction = outputStreamHandler == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() ->
                    {
                        try (final ByteReadStream childProcessOutputStream = new InputStreamToByteReadStream(childProcess.getInputStream()))
                        {
                            outputStreamHandler.run(childProcessOutputStream);
                        }
                    });

                final Result<Void> errorAction = errorStreamHandler == null
                    ? Result.success()
                    : this.parallelAsyncRunner.schedule(() ->
                    {
                        try (final ByteReadStream childProcessErrorStream = new InputStreamToByteReadStream(childProcess.getErrorStream()))
                        {
                            errorStreamHandler.run(childProcessErrorStream);
                        }
                    });

                result = RealChildProcess.create(childProcess, outputAction, errorAction);
            }
            catch (java.io.IOException e)
            {
                final String errorMessage = e.getMessage();
                if (errorMessage.endsWith("CreateProcess error=267, The directory name is invalid"))
                {
                    throw new FolderNotFoundException(workingFolderPath);
                }
                else if (errorMessage.endsWith("CreateProcess error=2, The system cannot find the file specified"))
                {
                    throw new FileNotFoundException(executablePath);
                }
                throw Exceptions.asRuntime(e);
            }

            return result;
        });
    }
}
