package qub;

public class HoareQuickSort implements QuickSort
{
    private HoareQuickSort()
    {
    }

    public static HoareQuickSort create()
    {
        return new HoareQuickSort();
    }

    private static <T> int partition(MutableIndexable<T> values, Comparer<T> comparer, int lowIndex, int highIndex)
    {
        final T pivotValue = values.get((lowIndex + highIndex) / 2);

        int i = lowIndex - 1;
        int j = highIndex + 1;

        int result = -1;

        while (true)
        {
            do
            {
                i++;
            }
            while (comparer.run(values.get(i), pivotValue) == Comparison.LessThan);

            do
            {
                j--;
            }
            while (comparer.run(values.get(j), pivotValue) == Comparison.GreaterThan);

            if (i >= j)
            {
                result = j;
                break;
            }

            values.swap(i, j);
        }

        return result;
    }

    private static <T> void sort(MutableIndexable<T> values, Comparer<T> comparer, int lowIndex, int highIndex)
    {
        if (0 <= lowIndex && lowIndex < highIndex)
        {
            final int pivotIndex = HoareQuickSort.partition(values, comparer, lowIndex, highIndex);
            HoareQuickSort.sort(values, comparer, lowIndex, pivotIndex);
            HoareQuickSort.sort(values, comparer, pivotIndex + 1, highIndex);
        }
    }

    @Override
    public <T> void sort(MutableIndexable<T> values, Comparer<T> comparer)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(comparer, "comparer");

        HoareQuickSort.sort(values, comparer, 0, values.getCount() - 1);
    }
}
