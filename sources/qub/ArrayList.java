package qub;

/**
 * A array-based List data structure that can expand when it gets full.
 * @param <T> The type of element stored in this List.
 */
public class ArrayList<T> extends JavaList<T>
{
    public ArrayList()
    {
        super(new java.util.ArrayList<T>());
    }
}
