package qub;

public class LomutoQuickSort implements QuickSort
{
    private LomutoQuickSort()
    {
    }

    public static LomutoQuickSort create()
    {
        return new LomutoQuickSort();
    }

    private static <T> int partition(MutableIndexable<T> values, Comparer<T> comparer, int lowIndex, int highIndex)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(comparer, "comparer");
        PreCondition.assertBetween(0, lowIndex, values.getCount() - 1, "lowIndex");
        PreCondition.assertBetween(0, highIndex, values.getCount() - 1, "highIndex");
        PreCondition.assertLessThanOrEqualTo(lowIndex, highIndex, "lowIndex");

        final T pivotValue = values.get(highIndex);

        int i = lowIndex - 1;
        for (int j = lowIndex; j <= highIndex - 1; j++)
        {
            if (comparer.run(values.get(j), pivotValue) != Comparison.GreaterThan)
            {
                i++;
                values.swap(i, j);
            }
        }

        i++;
        values.swap(i, highIndex);

        return i;
    }

    private static <T> void sort(MutableIndexable<T> values, Comparer<T> comparer, int lowIndex, int highIndex)
    {
        if (0 <= lowIndex && lowIndex < highIndex)
        {
            final int pivotIndex = LomutoQuickSort.partition(values, comparer, lowIndex, highIndex);

            LomutoQuickSort.sort(values, comparer, lowIndex, pivotIndex - 1);
            LomutoQuickSort.sort(values, comparer, pivotIndex + 1, highIndex);
        }
    }

    @Override
    public <T> void sort(MutableIndexable<T> values, Comparer<T> comparer)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(comparer, "comparer");

        LomutoQuickSort.sort(values, comparer, 0, values.getCount() - 1);
    }
}
