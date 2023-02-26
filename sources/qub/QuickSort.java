package qub;

public interface QuickSort extends Sort
{
    public static QuickSort create()
    {
        return HoareQuickSort.create();
    }
}
