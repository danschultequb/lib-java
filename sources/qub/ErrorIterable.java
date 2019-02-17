package qub;

public class ErrorIterable extends RuntimeException implements Iterable<Throwable>
{
    private final Iterable<Throwable> errors;

    private ErrorIterable(Iterable<Throwable> errors)
    {
        this.errors = errors;
    }

    public static RuntimeException create(Throwable... errors)
    {
        PreCondition.assertNotNullAndNotEmpty(errors, "errors");

        return ErrorIterable.create(Iterable.create(errors));
    }

    public static RuntimeException create(Iterable<Throwable> errors)
    {
        PreCondition.assertNotNull(errors, "errors");

        RuntimeException result = null;

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
