package qub;

/**
 * A RuntimeException that is thrown when a test assertion fails.
 */
public class TestAssertionFailure extends RuntimeException
{
    private final String fullTestName;
    private final Iterable<String> messageLines;

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
     */
    public TestAssertionFailure(String fullTestName, Iterable<String> messageLines)
    {
        this(fullTestName, messageLines, null);
    }

    /**
     * Create a new TestAssertionFailure with the provided message lines.
     * @param fullTestName The full name of the test that failed.
     * @param messageLines The lines to display that will explain the test assertion failure.
     * @param cause The exception that caused the test to fail.
     */
    public TestAssertionFailure(String fullTestName, String[] messageLines, Throwable cause)
    {
        this(fullTestName, Iterable.create(messageLines), cause);
    }

    /**
     * Create a new TestAssertionFailure with the provided message lines.
     * @param fullTestName The full name of the test that failed.
     * @param messageLines The lines to display that will explain the test assertion failure.
     * @param cause The exception that caused the test to fail.
     */
    public TestAssertionFailure(String fullTestName, Iterable<String> messageLines, Throwable cause)
    {
        super(getMessage(fullTestName, messageLines), cause);
        this.fullTestName = fullTestName;
        this.messageLines = messageLines;
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
    public Iterable<String> getMessageLines()
    {
        return messageLines;
    }

    private static String getMessage(String fullTestName, Iterable<String> messageLines)
    {
        final InMemoryLineStream lineStream = new InMemoryLineStream();
        lineStream.writeLine(fullTestName);
        if (messageLines != null)
        {
            for (final String messageLine : messageLines)
            {
                lineStream.writeLine(messageLine);
            }
        }
        return lineStream.getText().awaitError();
    }
}
