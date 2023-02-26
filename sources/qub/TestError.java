package qub;

public class TestError extends RuntimeException
{
    private final String testScope;
    private final String errorMessage;

    public TestError(String testScope, String errorMessage)
    {
        this(testScope, errorMessage, null);
    }

    public TestError(String testScope, String errorMessage, Throwable cause)
    {
        super(TestError.getMessage(testScope, errorMessage), cause);

        this.testScope = testScope;
        this.errorMessage = errorMessage;
    }

    public String getTestScope()
    {
        return this.testScope;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof TestError && this.equals((TestError)rhs);
    }

    public boolean equals(TestError rhs)
    {
        return rhs != null &&
                   Comparer.equal(this.getMessage(), rhs.getMessage()) &&
                   Comparer.equal(this.getCause(), rhs.getCause());
    }

    private static String getMessage(String testScope, String errorMessage)
    {
        PreCondition.assertNotNullAndNotEmpty(testScope, "testScope");
        PreCondition.assertNotNullAndNotEmpty(errorMessage, "errorMessage");

        final String result;

        try (final CharacterListWriteStream stream = CharacterListWriteStream.create())
        {
            stream.writeLine(testScope).await();
            stream.writeLine(errorMessage).await();
            result = stream.toString();
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }
}
