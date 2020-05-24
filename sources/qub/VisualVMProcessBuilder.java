package qub;

public class VisualVMProcessBuilder extends ProcessBuilderDecorator<VisualVMProcessBuilder> implements VisualVMArguments<VisualVMProcessBuilder>
{
    public static final String executablePathString = "visualvm";
    public static final Path executablePath = Path.parse(VisualVMProcessBuilder.executablePathString);

    private VisualVMProcessBuilder(ProcessBuilder processBuilder)
    {
        super(processBuilder);
    }

    /**
     * Get a VisualVMProcessBuilder from the provided Process.
     * @param process The Process to get the VisualVMProcessBuilder from.
     * @return The VisualVMProcessBuilder.
     */
    public static Result<VisualVMProcessBuilder> get(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        return VisualVMProcessBuilder.get(process.getProcessFactory());
    }

    /**
     * Get a VisualVMProcessBuilder from the provided ProcessFactory.
     * @param processFactory The ProcessFactory to get the VisualVMProcessBuilder from.
     * @return The VisualVMProcessBuilder.
     */
    public static Result<VisualVMProcessBuilder> get(ProcessFactory processFactory)
    {
        PreCondition.assertNotNull(processFactory, "processFactory");

        return Result.create(() ->
        {
            return new VisualVMProcessBuilder(processFactory.getProcessBuilder(VisualVMProcessBuilder.executablePath).await());
        });
    }
}
