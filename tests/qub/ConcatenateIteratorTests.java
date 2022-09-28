package qub;

public interface ConcatenateIteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ConcatenateIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer valueCount, Boolean started) ->
            {
                final List<Integer> list1 = List.create();
                for (int i = 0; i < (valueCount / 2); i++)
                {
                    list1.add(i);
                }

                final List<Integer> list2 = List.create();
                for (int i = (valueCount / 2); i < valueCount; i++)
                {
                    list2.add(i);
                }

                final Iterator<Integer> result = ConcatenateIterator.create(
                    Iterator.create(
                        list1.iterate(),
                        list2.iterate()));

                if (started)
                {
                    result.start();
                }

                return result;
            });
        });
    }
}
