package qub;

public class ShellSort extends SortBase
{
    @Override
    public <T> void sort(MutableIndexable<T> indexable, Function2<T, T, Comparison> comparer)
    {
        if (indexable != null && comparer != null)
        {
            final int indexableCount = indexable.getCount();
            if (indexableCount > 1)
            {
                for (int indexToSet = 0; indexToSet < indexableCount - 1; ++indexToSet)
                {
                    int minimumIndex = indexToSet;

                    for (int currentIndex = indexToSet + 1; currentIndex < indexableCount; ++currentIndex)
                    {
                        if (comparer.run(indexable.get(currentIndex), indexable.get(minimumIndex)) == Comparison.LessThan)
                        {
                            minimumIndex = currentIndex;
                        }
                    }

                    if (indexToSet != minimumIndex)
                    {
                        final T temp = indexable.get(indexToSet);
                        indexable.set(indexToSet, indexable.get(minimumIndex));
                        indexable.set(minimumIndex, temp);
                    }
                }
            }
        }
    }
}
