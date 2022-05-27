package qub;

public interface ListSetTests
{
    public static void test(TestRunner runner)
    {
        SetTests.test(runner, (Integer count) ->
        {
            PreCondition.assertNotNull(count, "count");
            PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");

            final ListSet<Integer> result = ListSet.create();
            for (int i = 0; i < count; i++)
            {
                result.add(i);
            }

            PostCondition.assertNotNull(result, "result");
            PostCondition.assertEqual(count.intValue(), result.getCount(), "result.getCount()");

            return result;
        });
    }
}
