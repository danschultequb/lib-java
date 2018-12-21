package qub;

public interface Algorithms
{
    /**
     * Traverse a provided space starting with the provided value and using the getNextValues
     * function to find all reachable values.
     * @param startValues The start value.
     * @param getNextValues The function to use to get the next values from a given value.
     * @param <T> The type of values used in this function.
     * @return All of the values that are reachable.
     */
    static <T> Iterable<T> traverse(Iterable<T> startValues, Function1<T,Iterable<T>> getNextValues)
    {
        PreCondition.assertNotNull(getNextValues, "getNextValues");

        final List<T> result = List.create();
        final Stack<T> toVisit = Stack.create();
        toVisit.pushAll(startValues);
        while (toVisit.any())
        {
            final T value = toVisit.pop();

            result.add(value);

            final Iterable<T> nextValues = getNextValues.run(value);
            for (final T nextValue : nextValues)
            {
                if (result.doesNotContain(nextValue))
                {
                    toVisit.push(nextValue);
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
