package qub;

public class TestError extends RuntimeException
{
    private final String testScope;
    private final Iterable<String> messageLines;

    public TestError(String testScope, String message)
    {
        this(testScope, Iterable.create(message), null);
    }

    public TestError(String testScope, String message, Throwable cause)
    {
        this(testScope, Iterable.create(message), cause);
    }

    public TestError(String testScope, Iterable<String> messageLines)
    {
        this(testScope, messageLines, null);
    }

    public TestError(String testScope, Iterable<String> messageLines, Throwable cause)
    {
        super(getMessage(testScope, messageLines), cause);

        PreCondition.assertNotNullAndNotEmpty(testScope, "testScope");
        PreCondition.assertNotNullAndNotEmpty(messageLines, "messageLines");

        this.testScope = testScope;
        this.messageLines = messageLines;
    }

    public String getTestScope()
    {
        return testScope;
    }

    /**
     * Get the lines that explain the test assertion failure.
     * @return The lines that explain the test assertion failure.
     */
    public Iterable<String> getMessageLines()
    {
        return messageLines;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof TestError && equals((TestError)rhs);
    }

    public boolean equals(TestError rhs)
    {
        return rhs != null &&
                   Comparer.equal(getMessage(), rhs.getMessage()) &&
                   Comparer.equal(getCause(), rhs.getCause());
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(getMessage(), getCause());
    }

    private static String getMessage(String testScope, Iterable<String> messageLines)
    {
        PreCondition.assertNotNullAndNotEmpty(testScope, "testScope");
        PreCondition.assertNotNullAndNotEmpty(messageLines, "messageLines");

        final InMemoryCharacterToByteStream characterStream = InMemoryCharacterToByteStream.create();
        characterStream.writeLine(testScope).await();
        if (messageLines != null)
        {
            for (final String messageLine : messageLines)
            {
                characterStream.writeLine(messageLine).await();
            }
        }
        return characterStream.getText().await();
    }
}
