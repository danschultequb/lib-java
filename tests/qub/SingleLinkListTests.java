package qub;

public class SingleLinkListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SingleLinkList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final SingleLinkList<Integer> result = new SingleLinkList<>();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });

            runner.test("fromValues()", test ->
            {
                final SingleLinkList<Integer> list1 = SingleLinkList.fromValues(new Integer[0]);
                test.assertEqual(0, list1.getCount());

                final SingleLinkList<Integer> list2 = SingleLinkList.fromValues((Integer[])null);
                test.assertEqual(0, list2.getCount());

                final SingleLinkList<Integer> arrayList3 = SingleLinkList.fromValues(new Integer[] { 1, 2, 3 });
                test.assertEqual(3, arrayList3.getCount());
                for (int i = 0; i < 3; ++i)
                {
                    test.assertEqual(i + 1, arrayList3.get(i));
                }

                final SingleLinkList<Integer> arrayList4 = SingleLinkList.fromValues((Iterator<Integer>)null);
                test.assertEqual(0, arrayList4.getCount());

                final SingleLinkList<Integer> arrayList5 = SingleLinkList.fromValues(new Array<Integer>(0).iterate());
                test.assertEqual(0, arrayList5.getCount());

                final SingleLinkList<Integer> arrayList6 = SingleLinkList.fromValues(Array.fromValues(new Integer[] { 1, 2, 3 }).iterate());
                test.assertEqual(3, arrayList6.getCount());
                for (int i = 0; i < 3; ++i)
                {
                    test.assertEqual(i + 1, arrayList6.get(i));
                }

                final SingleLinkList<Integer> arrayList7 = SingleLinkList.fromValues((Iterable<Integer>)null);
                test.assertEqual(0, arrayList7.getCount());

                final SingleLinkList<Integer> arrayList8 = SingleLinkList.fromValues(new Array<Integer>(0));
                test.assertEqual(0, arrayList8.getCount());

                final SingleLinkList<Integer> arrayList9 = SingleLinkList.fromValues(Array.fromValues(new Integer[] { 1, 2, 3 }));
                test.assertEqual(3, arrayList9.getCount());
                for (int i = 0; i < 3; ++i)
                {
                    test.assertEqual(i + 1, arrayList9.get(i));
                }
            });
        });
    }
}
