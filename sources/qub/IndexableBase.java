package qub;

import java.util.Objects;

public abstract class IndexableBase<T> implements Indexable<T>
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

    @Override
    public int indexOf(final T value)
    {
        return indexOf(element -> Comparer.equal(element, value));
    }
}
