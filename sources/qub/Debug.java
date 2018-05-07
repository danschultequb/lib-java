package qub;

public class Debug
{
    public static void log(Object caller, String message)
    {
        System.out.println(Thread.currentThread().getId() + ", " + caller.toString() + ") " + message);
    }
}
