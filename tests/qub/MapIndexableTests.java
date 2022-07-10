package qub;

public interface MapIndexableTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MapIndexable.class, () ->
        {
            IndexableTests.test(runner, (Integer count) ->
            {
                final List<String> innerIterable = List.create();
                for (int i = 0; i < count; ++i)
                {
                    innerIterable.add(Integer.toString(i));
                }
                return MapIndexable.create(innerIterable, Integer::parseInt);
            });
        });
    }
}
