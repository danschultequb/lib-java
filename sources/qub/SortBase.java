package qub;

public abstract class SortBase implements Sort
{
    @Override
    public <T extends Comparable<T>> void sort(MutableIndexable<T> indexable)
    {
        SortBase.sort(this, indexable);
    }

    public static <T extends Comparable<T>> void sort(Sort sort, MutableIndexable<T> indexable)
    {
        sort.sort(indexable, new Function2<T, T, Comparison>()
        {
            @Override
            public Comparison run(T lhs, T rhs)
            {
                return lhs.compareTo(rhs);
            }
        });
    }
}
