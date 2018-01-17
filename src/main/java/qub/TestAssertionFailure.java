package qub;

/**
 * A RuntimeException that is thrown when a test assertion fails.
 */
public class TestAssertionFailure extends RuntimeException
{
    private final String fullTestName;
    private final String[] messageLines;
    private final Throwable innerException;

    /**
     * Create a new TestAssertionFailure with the provided message lines.
     * @param fullTestName The full name of the test that failed.
     * @param messageLines The lines to display that will explain the test assertion failure.
     */
    public TestAssertionFailure(String fullTestName, String[] messageLines)
    {
        this(fullTestName, messageLines, null);
    }

    /**
     * Create a new TestAssertionFailure with the provided message lines.
     * @param fullTestName The full name of the test that failed.
     * @param messageLines The lines to display that will explain the test assertion failure.
     * @param innerException The exception that caused the test to fail.
     */
    public TestAssertionFailure(String fullTestName, String[] messageLines, Throwable innerException)
    {
        this.fullTestName = fullTestName;
        this.messageLines = messageLines;
        this.innerException = innerException;
    }

    /**
     * Get the full name of the test that failed.
     * @return The full name of the test that failed.
     */
    public String getFullTestName()
    {
        return fullTestName;
    }

    /**
     * Get the lines that explain the test assertion failure.
     * @return The lines that explain the test assertion failure.
     */
    public String[] getMessageLines()
    {
        return messageLines;
    }

    @Override
    public StackTraceElement[] getStackTrace()
    {
        return innerException != null ? innerException.getStackTrace() : super.getStackTrace();
    }
}
