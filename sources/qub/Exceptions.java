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

    /**
     * Get whether or not the provided error or the unwrapped version of the provided error is of
     * the provided error type.
     * @param error The error to check.
     * @param errorType The type of error to check for.
     * @param <TError> The type of error to check for.
     * @return Whether or not the provided error or the unwrapped version of the provided error is
     * of the provided error type.
     */
    static <TError extends Throwable> boolean instanceOf(Throwable error, Class<TError> errorType)
    {
        return getInstanceOf(error, errorType) != null;
    }

    /**
     * Return the provided error if it is of the provided errorType. If it isn't, then unwrap the
     * provided error and return the unwrapped error if it is of the provided errorType. If neither
     * the provided error or the unwrapped error are of the provided errorType, then return null.
     * @param error The error to check.
     * @param errorType The error type to check.
     * @param <TError> The type of error to check for.
     * @return The matching error, or null if neither the error or the unwrapped error match the
     * error type.
     */
    @SuppressWarnings("unchecked")
    static <TError extends Throwable> TError getInstanceOf(Throwable error, Class<TError> errorType)
    {
        TError result = null;
        if (error != null)
        {
            if (Types.instanceOf(error, errorType))
            {
                result = (TError)error;
            }
            else
            {
                final Throwable unwrappedError = Exceptions.unwrap(error);
                if (Types.instanceOf(unwrappedError, errorType))
                {
                    result = (TError)unwrappedError;
                }
            }
        }
        return result;
    }
}
