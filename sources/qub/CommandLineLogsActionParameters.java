package qub;

public class CommandLineLogsActionParameters
{
    private Folder logsFolder;
    private CharacterWriteStream output;
    private DefaultApplicationLauncher defaultApplicationLauncher;
    private ChildProcessRunner processRunner;
    private Path openWith;

    private CommandLineLogsActionParameters()
    {
    }

    public static CommandLineLogsActionParameters create()
    {
        return new CommandLineLogsActionParameters();
    }

    public Folder getLogsFolder()
    {
        return this.logsFolder;
    }

    public CommandLineLogsActionParameters setLogsFolder(Folder logsFolder)
    {
        PreCondition.assertNotNull(logsFolder, "logsFolder");

        this.logsFolder = logsFolder;

        return this;
    }

    public CharacterWriteStream getOutput()
    {
        return this.output;
    }

    public CommandLineLogsActionParameters setOutput(CharacterWriteStream output)
    {
        PreCondition.assertNotNull(output, "output");

        this.output = output;

        return this;
    }

    public DefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        return this.defaultApplicationLauncher;
    }

    public CommandLineLogsActionParameters setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        PreCondition.assertNotNull(defaultApplicationLauncher, "defaultApplicationLauncher");

        this.defaultApplicationLauncher = defaultApplicationLauncher;

        return this;
    }

    public ChildProcessRunner getProcessFactory()
    {
        return this.processRunner;
    }

    public CommandLineLogsActionParameters setProcessFactory(ChildProcessRunner processRunner)
    {
        PreCondition.assertNotNull(processRunner, "processFactory");

        this.processRunner = processRunner;

        return this;
    }

    public Path getOpenWith()
    {
        return this.openWith;
    }

    public CommandLineLogsActionParameters setOpenWith(Path openWith)
    {
        PreCondition.assertNotNull(openWith, "openWith");

        this.openWith = openWith;

        return this;
    }
}
