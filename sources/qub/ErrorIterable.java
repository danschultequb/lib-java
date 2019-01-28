package qub;

public class ErrorIterable extends RuntimeException implements Iterable<Throwable>
{
    private final Iterable<Throwable> errors;

    private ErrorIterable(Iterable<Throwable> errors)
    {
        this.errors = errors;
    }

    public static RuntimeException from(Throwable error, Throwable... extraErrors)
    {
        final Array<Throwable> errorArray = new Array<>(1 + (extraErrors == null ? 0 : extraErrors.length));
        errorArray.set(0, error);
        if (extraErrors != null)
        {
            for (int i = 0; i < extraErrors.length; ++i)
            {
                errorArray.set(i + 1, extraErrors[i]);
            }
        }
        return ErrorIterable.from(errorArray);
    }

    public static RuntimeException from(Iterable<Throwable> errors)
    {
        RuntimeException result = null;

        if (!Iterable.isNullOrEmpty(errors))
        {
            final List<Throwable> errorList = List.create();
            for (final Throwable error : errors)
            {
                if (error instanceof ErrorIterable)
                {
                    errorList.addAll((ErrorIterable)error);
                }
                else if (error != null)
                {
                    errorList.add(error);
                }
            }

            if (!Iterable.isNullOrEmpty(errorList))
            {
                if (errorList.getCount() == 1)
                {
                    result = Exceptions.asRuntime(errorList.first());
                }
                else
                {
                    result = new ErrorIterable(errorList);
                }
            }
        }

        return result;
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public Iterator<Throwable> iterate()
    {
        return errors.iterate();
    }
}
