package qub;

/**
 * A ProcessBuilder that builds up a process for invocation.
 */
public class BasicProcessBuilder implements ProcessBuilder
{
    private final ProcessFactory factory;
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private ByteReadStream redirectedInputStream;
    private Action1<ByteReadStream> redirectOutputAction;
    private Action1<ByteReadStream> redirectErrorAction;

    public BasicProcessBuilder(ProcessFactory factory, Path executablePath, Path workingFolderPath)
    {
        PreCondition.assertNotNull(factory, "factory");
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        this.factory = factory;
        this.executablePath = executablePath;
        this.arguments = List.create();
        this.workingFolderPath = workingFolderPath;
    }

    @Override
    public Result<Integer> run()
    {
        return this.factory.run(
            this.executablePath,
            this.arguments,
            this.workingFolderPath,
            this.redirectedInputStream,
            this.redirectOutputAction,
            this.redirectErrorAction);
    }

    @Override
    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    @Override
    public String getCommand()
    {
        return ProcessFactory.getCommand(this.executablePath, this.arguments, this.workingFolderPath);
    }

    @Override
    public BasicProcessBuilder addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

        return this;
    }

    @Override
    public BasicProcessBuilder addArguments(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    @Override
    public BasicProcessBuilder addArguments(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.arguments;
    }


    @Override
    public BasicProcessBuilder setWorkingFolder(String workingFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(workingFolderPath, "workingFolderPath");

        return this.setWorkingFolder(Path.parse(workingFolderPath));
    }


    @Override
    public BasicProcessBuilder setWorkingFolder(Path workingFolderPath)
    {
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");
        PreCondition.assertTrue(workingFolderPath.isRooted(), "workingFolderPath.isRooted()");

        this.workingFolderPath = workingFolderPath;

        return this;
    }


    @Override
    public BasicProcessBuilder setWorkingFolder(Folder workingFolder)
    {
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        return this.setWorkingFolder(workingFolder.getPath());
    }


    @Override
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }


    @Override
    public BasicProcessBuilder redirectInput(ByteReadStream redirectedInputStream)
    {
        this.redirectedInputStream = redirectedInputStream;
        return this;
    }


    @Override
    public BasicProcessBuilder redirectOutput(Action1<ByteReadStream> redirectOutputAction)
    {
        this.redirectOutputAction = redirectOutputAction;
        return this;
    }


    @Override
    public BasicProcessBuilder redirectOutput(ByteWriteStream redirectedOutputStream)
    {
        return this.redirectOutput((ByteReadStream output) -> redirectedOutputStream.writeAll(output).await());
    }


    @Override
    public BasicProcessBuilder redirectOutputLines(Action1<String> onOutputLine)
    {
        return this.redirectOutput(BasicProcessBuilder.byteReadStreamToLineAction(onOutputLine));
    }


    @Override
    public BasicProcessBuilder redirectError(Action1<ByteReadStream> redirectErrorAction)
    {
        this.redirectErrorAction = redirectErrorAction;
        return this;
    }


    @Override
    public BasicProcessBuilder redirectError(ByteWriteStream redirectedErrorStream)
    {
        return this.redirectError((ByteReadStream error) -> redirectedErrorStream.writeAll(error).await());
    }


    @Override
    public BasicProcessBuilder redirectErrorLines(Action1<String> onErrorLine)
    {
        return this.redirectError(BasicProcessBuilder.byteReadStreamToLineAction(onErrorLine));
    }

    /**
     * Get a function that will take in a ByteReadStream and will invoked the provided function on
     * each line.
     * @param onLineAction The function to invoke for each line of the ByteReadStream.
     * @return The function.
     */
    private static Action1<ByteReadStream> byteReadStreamToLineAction(Action1<String> onLineAction)
    {
        PreCondition.assertNotNull(onLineAction, "onLineAction");

        return (ByteReadStream byteReadStream) ->
        {
            final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();
            String line;
            do
            {
                line = characterReadStream.readLine(true)
                    .catchError(EndOfStreamException.class)
                    .await();
                if (line != null)
                {
                    onLineAction.run(line);
                }
            }
            while (line != null);
        };
    }
}
