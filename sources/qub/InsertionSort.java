package qub;

/**
 * A type of {@link Sort} that sorts elements by inserting them in order.
 * See <a href="https://en.wikipedia.org/wiki/Insertion_sort">here</a> for more details.
 */
public class InsertionSort implements Sort
{
    private InsertionSort()
    {
    }

    public static InsertionSort create()
    {
        return new InsertionSort();
    }

    @Override
    public <T> void sort(MutableIndexable<T> values, Comparer<T> comparer)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(comparer, "comparer");

        final int valuesCount = values.getCount();
        for (int i = 0; i < valuesCount - 1; i++)
        {
            int minimumIndex = i;
            T minimumValue = values.get(minimumIndex);
            for (int j = i + 1; j < valuesCount; j++)
            {
                final T jValue = values.get(j);
                if (comparer.run(minimumValue, jValue) == Comparison.GreaterThan)
                {
                    minimumIndex = j;
                    minimumValue = jValue;
                }
            }
            values.swap(i, minimumIndex);
        }
    }
}
