package qub;

public interface Exceptions
{
    /**
     * Ensure that the provided error is a RuntimeException. If it isn't, then wrap the error in a
     * RuntimeException and return the wrapped error.
     * @param error The error to ensure is a RuntimeException.
     * @return The error if it is a RuntimeException or a wrapped RuntimeException if it isn't.
     */
    static RuntimeException asRuntime(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return error instanceof RuntimeException
            ? (RuntimeException)error
            : new RuntimeException(error);
    }

    /**
     * Get whether or not the provided error is wrapped in a generic RuntimeException.
     * @param error The error to check.
     * @return Whether or not the provided error is wrapped in a generic RuntimeException.
     */
    static boolean isWrapped(Throwable error)
    {
        return error != null && error.getClass() == RuntimeException.class && error.getCause() != null;
    }

    /**
     * If the provided error is a wrapped error, then unwrap it and return the inner error. If it is
     * not a wrapped error, then just return the provided error.
     * @param error The error to unwrap.
     * @return The unwrapped error, or the provided error if it was not wrapped.
     */
    static Throwable unwrap(Throwable error)
    {
        return isWrapped(error) ? error.getCause() : error;
    }
}
