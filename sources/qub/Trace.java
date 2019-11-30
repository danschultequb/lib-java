package qub;

/**
 * A static class that keeps track of when sections of code are entered and exited.
 */
public abstract class Trace
{
    private static Function0<CharacterWriteStream> writeStreamGetter;
    private static Clock clock;
    private static boolean writeTimestamps;
    private static boolean enabled;

    /**
     * Set the CharacterWriteStream that Trace will log to.
     * @param writeStream The CharacterWriteStream that Trace will log to.
     */
    public static void setWriteStream(CharacterWriteStream writeStream)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");

        Trace.setWriteStream(() -> writeStream);
    }

    public static void setWriteStream(Function0<CharacterWriteStream> writeStreamGetter)
    {
        PreCondition.assertNotNull(writeStreamGetter, "writeStreamGetter");

        Trace.writeStreamGetter = writeStreamGetter;
    }

    private static CharacterWriteStream getWriteStream()
    {
        PreCondition.assertNotNull(Trace.writeStreamGetter, "Trace.writeStreamGetter");

        final CharacterWriteStream result = Trace.writeStreamGetter.run();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the Clock that Trace will use to log timestamps.
     * @param clock The Clock that Trace will use to log timestamps.
     */
    public static void setClock(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        Trace.clock = clock;
    }

    /**
     * Set whether or not timestamps will be added to each of the logs.
     * @param writeTimestamps Whether or not timestamps will be added to each of the logs.
     */
    public static void setWriteTimestamps(boolean writeTimestamps)
    {
        Trace.writeTimestamps = writeTimestamps;
    }

    /**
     * Set whether or not Trace is enabled. When Trace is enabled, logs will be written to its
     * ByteWriteStream. When Trace is disabled, logs will be ignored.
     * @param enabled Whether or not Trace is enabled.
     */
    public static void setEnabled(boolean enabled)
    {
        Trace.enabled = enabled;
    }

    /**
     * Enable Trace.
     */
    public static void enable()
    {
        Trace.setEnabled(true);
    }

    /**
     * Enable Trace for the execution of the provided action. When the action completes, return
     * Trace back to the enabled state that it was before enable(Action0) was called..
     * @param action The action to run while Trace is enabled.
     */
    public static void enable(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final boolean enabledBackup = Trace.enabled;
        setEnabled(true);
        try
        {
            action.run();
        }
        finally
        {
            setEnabled(enabledBackup);
        }
    }

    /**
     * Enable Trace for the execution of the provided function. When the function completes, return
     * Trace back to the enabled state that it was before enable(Function0) was called.
     * @param function The function to run while Trace is enabled.
     * @param <T> The type of value that the function will return.
     * @return The function's return value.
     */
    public static <T> T enable(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        final boolean enabledBackup = Trace.enabled;
        setEnabled(true);
        T result;
        try
        {
            result = function.run();
        }
        finally
        {
            setEnabled(enabledBackup);
        }
        return result;
    }

    /**
     * Disable Trace.
     */
    public static void disable()
    {
        Trace.setEnabled(false);
    }

    private static void log(String logType, String message)
    {
        PreCondition.assertNotNullAndNotEmpty(logType, "logType");
        PreCondition.assertNotNull(message, "message");
        PreCondition.assertNotNull(Trace.writeStreamGetter, "Trace.writeStream");
        PreCondition.assertTrue(!Trace.writeTimestamps || Trace.clock != null, "!Trace.writeTimestamps || Trace.clock != null");

        if (Trace.enabled)
        {
            final CharacterWriteStream writeStream = Trace.getWriteStream();
            if (Trace.writeTimestamps)
            {
                final DateTime timestamp = clock.getCurrentDateTime();
                final long millisecondsSinceEpoch = (long)timestamp.getDurationSinceEpoch().toMilliseconds().getValue();
                writeStream.write(millisecondsSinceEpoch + ",");
            }
            writeStream.write(logType + "," + Strings.escapeAndQuote(message) + "\n");
        }
    }

    /**
     * Log the provided message to the ByteWriteStream.
     * @param message The message to log to the ByteWriteStream.
     */
    public static void log(String message)
    {
        Trace.log("LOG", message);
    }

    /**
     * Log the provided message before and after the provided action is invoked.
     * @param message The message that describes the section.
     * @param action The action to run.
     */
    public static void section(String message, Action0 action)
    {
        PreCondition.assertNotNull(message, "message");
        PreCondition.assertNotNull(action, "action");

        Trace.log("ENTER", message);
        try
        {
            action.run();
        }
        finally
        {
            Trace.log("EXIT", message);
        }
    }

    /**
     * Log the provided message before and action the provided function is invoked.
     * @param message The message that describes the section.
     * @param function The function to run.
     * @param <T> The type of value that the function returns.
     * @return The return value of the function.
     */
    public static <T> T section(String message, Function0<T> function)
    {
        PreCondition.assertNotNull(message, "message");
        PreCondition.assertNotNull(function, "function");

        Trace.log("ENTER", message);
        T result;
        try
        {
            result = function.run();
        }
        finally
        {
            Trace.log("EXIT", message);
        }
        return result;
    }
}
