package qub;

public class MapIndexableTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MapIndexable.class, () ->
        {
            IndexableTests.test(runner, (Integer count) ->
            {
                final List<String> innerIterable = new ArrayList<>();
                for (int i = 0; i < count; ++i)
                {
                    innerIterable.add(Integer.toString(i));
                }
                return new MapIndexable<>(innerIterable, Integer::parseInt);
            });
        });
    }
}
