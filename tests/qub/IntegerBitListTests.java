package qub;

public interface IntegerBitListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IntegerBitList.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final IntegerBitList list = IntegerBitList.create();
                test.assertNotNull(list);
                test.assertEqual(0, list.getCount());
            });
        });
    }
}
