package qub;

public interface MapIterableTests
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
                return MapIterable.create(innerIterable, Integer::parseInt);
            });
        });
    }
}
