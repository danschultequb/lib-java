package qub;

/**
 * An interface that helps in interacting with a Profiler.
 */
public interface Profiler
{
    /**
     * Pause the application so that a Profiler can be attached.
     * @param process The process to pause.
     * @param classToAttachTo The class to indicate that the profiler should be attached to.
     * @return The result of pausing.
     */
    static Result<Void> waitForProfiler(Process process, Class<?> classToAttachTo)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(classToAttachTo, "classToAttachTo");

        return Result.create(() ->
        {
            process.getOutputCharacterWriteStream()
                .writeLine("Attach a profiler now to " + Types.getTypeName(classToAttachTo) + ". Press enter to continue...")
                .await();
            process.getInputCharacterReadStream()
                .readLine()
                .await();
        });
    }
}
