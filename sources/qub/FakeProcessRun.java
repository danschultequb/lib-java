package qub;

/**
 * A fake run of a process.
 */
public class FakeProcessRun
{
    private final File executableFile;
    private final List<String> arguments;
    private Folder workingFolder;
    private int exitCode;

    public FakeProcessRun(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        this.executableFile = executableFile;
        this.arguments = List.create();
    }

    public FakeProcessRun addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

        return this;
    }

    public FakeProcessRun addArguments(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    public FakeProcessRun setWorkingFolder(Folder workingFolder)
    {
        this.workingFolder = workingFolder;

        return this;
    }

    public FakeProcessRun setExitCode(int exitCode)
    {
        this.exitCode = exitCode;

        return this;
    }

    public int getExitCode()
    {
        return this.exitCode;
    }

    public boolean matches(File executableFile, Iterable<String> arguments, Folder workingFolder)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        return this.executableFile.equals(executableFile) &&
            this.arguments.equals(arguments) &&
            (this.workingFolder == null || this.workingFolder.equals(workingFolder));
    }
}
