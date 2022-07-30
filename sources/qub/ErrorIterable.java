package qub;

/**
 * A {@link RuntimeException} that can contain multiple {@link Throwable} errors.
 */
public class ErrorIterable extends RuntimeException implements Iterable<Throwable>
{
    private final Iterable<Throwable> errors;

    private ErrorIterable(Iterable<Throwable> errors)
    {
        super(errors.toString());

        this.errors = errors;
    }

    public static RuntimeException create(Throwable... errors)
    {
        PreCondition.assertNotNull(errors, "errors");

        return ErrorIterable.create(Iterable.create(errors));
    }

    public static RuntimeException create(Iterable<Throwable> errors)
    {
        PreCondition.assertNotNull(errors, "errors");

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

        final RuntimeException result;
        if (errorList.getCount() == 1)
        {
            result = Exceptions.asRuntime(errorList.first().await());
        }
        else
        {
            result = new ErrorIterable(errorList);
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
