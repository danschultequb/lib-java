package qub;

public class ArrayListTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ArrayList<T>", new Action0()
        {
            @Override
            public void run()
            {
                ListTests.test(runner, new Function1<Integer, List<Integer>>()
                {
                    @Override
                    public List<Integer> run(Integer count)
                    {
                        final ArrayList<Integer> result = new ArrayList<>();
                        for (int i = 0; i < count; ++i)
                        {
                            result.add(i);
                        }
                        return result;
                    }
                });
                
                runner.test("fromValues()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ArrayList<Integer> arrayList1 = ArrayList.fromValues();
                        test.assertEqual(0, arrayList1.getCount());

                        final ArrayList<Integer> arrayList2 = ArrayList.fromValues((Integer[])null);
                        test.assertEqual(0, arrayList2.getCount());

                        final ArrayList<Integer> arrayList3 = ArrayList.fromValues(1, 2, 3);
                        test.assertEqual(3, arrayList3.getCount());
                        for (int i = 0; i < 3; ++i)
                        {
                            test.assertEqual(i + 1, arrayList3.get(i));
                        }

                        final ArrayList<Integer> arrayList4 = ArrayList.fromValues((Iterator<Integer>)null);
                        test.assertEqual(0, arrayList4.getCount());

                        final ArrayList<Integer> arrayList5 = ArrayList.fromValues(new Array<Integer>(0).iterate());
                        test.assertEqual(0, arrayList5.getCount());

                        final ArrayList<Integer> arrayList6 = ArrayList.fromValues(Array.fromValues(1, 2, 3).iterate());
                        test.assertEqual(3, arrayList6.getCount());
                        for (int i = 0; i < 3; ++i)
                        {
                            test.assertEqual(i + 1, arrayList6.get(i));
                        }

                        final ArrayList<Integer> arrayList7 = ArrayList.fromValues((Iterable<Integer>)null);
                        test.assertEqual(0, arrayList7.getCount());

                        final ArrayList<Integer> arrayList8 = ArrayList.fromValues(new Array<Integer>(0));
                        test.assertEqual(0, arrayList8.getCount());

                        final ArrayList<Integer> arrayList9 = ArrayList.fromValues(Array.fromValues(1, 2, 3));
                        test.assertEqual(3, arrayList9.getCount());
                        for (int i = 0; i < 3; ++i)
                        {
                            test.assertEqual(i + 1, arrayList9.get(i));
                        }
                    }
                });
            }
        });
    }
}
