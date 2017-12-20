package qub;

/**
 * A RuntimeException that is thrown when a test assertion fails.
 */
public class TestAssertionFailure extends RuntimeException
{
    private final String[] messageLines;

    /**
     * Create a new TestAssertionFailure with the provided message lines.
     * @param messageLines The lines to display that will explain the test assertion failure.
     */
    public TestAssertionFailure(String[] messageLines)
    {
        this.messageLines = messageLines;
    }

    /**
     * Get the lines that explain the test assertion failure.
     * @return The lines that explain the test assertion failure.
     */
    public String[] getMessageLines()
    {
        return messageLines;
    }
}
