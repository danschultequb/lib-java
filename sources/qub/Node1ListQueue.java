package qub;

public class Node1ListQueue<T> extends ListQueue<T>
{
    private Node1ListQueue()
    {
        super(Node1List.create());
    }

    public static <T> Node1ListQueue<T> create()
    {
        return new Node1ListQueue<>();
    }
}
