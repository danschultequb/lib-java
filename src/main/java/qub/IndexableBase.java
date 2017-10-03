package qub;

public abstract class IndexableBase<T> extends IterableBase<T> implements Indexable<T>
{
    @Override
    public int indexOf(Function1<T,Boolean> condition)
    {
        int result = -1;
        if (condition != null)
        {
            int index = 0;
            for (final T element : this)
            {
                if (condition.run(element))
                {
                    result = index;
                    break;
                }
                else
                {
                    ++index;
                }
            }
        }
        return result;
    }
}
