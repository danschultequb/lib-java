package qub;

public interface Exceptions
{
    /**
     * A set of error types that will be ignored when comparing two errors. This means that when two
     * errors are compared and these error types are in the error cause chains, then those error
     * types will be skipped over until an error not of these types is found.
     */
    Iterable<Class<? extends Throwable>> defaultErrorTypesToGoPast = Iterable.create(RuntimeException.class, AwaitException.class);

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
        return Exceptions.getInstanceOf(error, errorType) != null;
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
    static <TError extends Throwable> boolean instanceOf(Throwable error, Class<TError> errorType, Iterable<Class<? extends Throwable>> errorTypesToGoPast)
    {
        return Exceptions.getInstanceOf(error, errorType, errorTypesToGoPast) != null;
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
        return Exceptions.getInstanceOf(error, errorType, Exceptions.defaultErrorTypesToGoPast);
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
    static <TError extends Throwable> TError getInstanceOf(Throwable error, Class<TError> errorType, Iterable<Class<? extends Throwable>> errorTypesToGoPast)
    {
        TError result = null;
        final boolean hasErrorTypesToGoPast = !Iterable.isNullOrEmpty(errorTypesToGoPast);
        while (error != null)
        {
            if (Types.instanceOf(error, errorType))
            {
                result = (TError)error;
            }

            if (result != null || !hasErrorTypesToGoPast || !errorTypesToGoPast.contains(error.getClass()))
            {
                error = null;
            }
            else
            {
                error = error.getCause();
            }
        }
        return result;
    }

    static Throwable unwrap(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        Throwable result = error;
        Throwable cause = result.getCause();
        while (cause != null && defaultErrorTypesToGoPast.contains(result.getClass()))
        {
            result = cause;
            cause = result.getCause();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static Result<Void> writeErrorString(CharacterWriteStream stream, Throwable error)
    {
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertNotNull(error, "error");

        return Result.create(() ->
        {
            final IndentedCharacterWriteStream writeStream = IndentedCharacterWriteStream.create(stream);
            writeStream.writeLine(error.toString()).await();

            final StackTraceElement[] stackTraceElements = error.getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length > 0)
            {
                writeStream.increaseIndent();
                for (final StackTraceElement stackTraceElement : stackTraceElements)
                {
                    writeStream.writeLine("at " + stackTraceElement.toString()).await();
                }

                Throwable cause = error.getCause();
                while (cause != null)
                {
                    writeStream.increaseIndent();
                    writeStream.writeLine("Caused by: " + Types.getFullTypeName(cause)).await();

                    writeStream.increaseIndent();
                    final String causeMessage = cause.getMessage();
                    if (!Strings.isNullOrEmpty(causeMessage))
                    {
                        writeStream.writeLine("Message: " + cause.getMessage()).await();
                    }
                    final StackTraceElement[] causeStackTraceElements = cause.getStackTrace();
                    for (final StackTraceElement stackTraceElement : causeStackTraceElements)
                    {
                        writeStream.writeLine("at " + stackTraceElement.toString()).await();
                    }

                    cause = cause.getCause();
                }
            }
        });
    }
}
