package qub;

public class MapIterableTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MapIterable.class, () ->
        {
            IterableTests.test(runner, (Integer count) ->
            {
                final List<String> innerIterable = new ArrayList<>();
                for (int i = 0; i < count; ++i)
                {
                    innerIterable.add(Integer.toString(i));
                }
                return new MapIterable<>(innerIterable, Integer::parseInt);
            });
        });
    }
}
