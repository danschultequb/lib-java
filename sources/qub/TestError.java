package qub;

public class TestError
{
    private final String testScope;
    private final Throwable error;

    public TestError(String testScope, Throwable error)
    {
        this.testScope = testScope;
        this.error = error;
    }

    public String getTestScope()
    {
        return testScope;
    }

    public Throwable getError()
    {
        return error;
    }

    public String getMessage()
    {
        return "An unexpected error occurred during: " + Strings.escapeAndQuote(testScope);
    }

    @Override
    public String toString()
    {
        return getMessage();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof TestError && equals((TestError)rhs);
    }

    public boolean equals(TestError rhs)
    {
        return rhs != null &&
                   Comparer.equal(testScope, rhs.testScope) &&
                   Comparer.equal(error, rhs.error);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(testScope, error);
    }
}
