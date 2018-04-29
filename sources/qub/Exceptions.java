package qub;

public class Exceptions
{
    public static void throwAsRuntime(Throwable error)
    {
        if (error != null)
        {
            if (error instanceof RuntimeException)
            {
                throw (RuntimeException)error;
            }
            else
            {
                throw new RuntimeException(error);
            }
        }
    }
}
