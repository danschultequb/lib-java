package qub;

public interface Profiler
{
    String parameterName = "pause";

    static boolean takeProfilerArgument(Console console)
    {
        PreCondition.assertNotNull(console, "console");

        final CommandLine commandLine = console.getCommandLine();

        boolean result = false;
        final CommandLineArgument profileArgument = commandLine.remove(parameterName);
        if (profileArgument != null)
        {
            final String profileArgumentValue = profileArgument.getValue();
            result = Strings.isNullOrEmpty(profileArgumentValue) || profileArgumentValue.equalsIgnoreCase("true");
        }

        return result;
    }

    static Result<Void> waitForProfiler(Console console, Class<?> classToAttachTo)
    {
        return Result.create(() ->
        {
            console.writeLine("Attach a profiler now to " + Types.getTypeName(classToAttachTo) + ". Press enter to continue...").await();
            console.readLine().await();
        });

    }
}
