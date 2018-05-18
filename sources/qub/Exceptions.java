package qub;

public class Exceptions
{
    public static RuntimeException asRuntime(Throwable error)
    {
        RuntimeException result;
        if (error == null)
        {
            result = null;
        }
        else if (error instanceof RuntimeException)
        {
            result = (RuntimeException)error;
        }
        else
        {
            result = new RuntimeException(error);
        }
        return result;
    }

    public static void throwAsRuntime(Throwable error)
    {
        if (error != null)
        {
            throw Exceptions.asRuntime(error);
        }
    }
}
