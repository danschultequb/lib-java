package qub;

public interface JavaIteratorToIteratorAdapterTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaIteratorToIteratorAdapter.class, () ->
        {
            runner.testGroup("create()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaIteratorToIteratorAdapter.create(null),
                        new PreConditionFailure("javaIterator cannot be null."));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final java.util.Iterator<Integer> javaIterator = createJavaIterator();
                    final JavaIteratorToIteratorAdapter<Integer> iterator = JavaIteratorToIteratorAdapter.create(javaIterator);
                    assertIterator(test, iterator, false, null);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null);
                });

                runner.test("with single-value Iterator", (Test test) ->
                {
                    final java.util.Iterator<Integer> javaIterator = createJavaIterator(7);
                    final JavaIteratorToIteratorAdapter<Integer> iterator = JavaIteratorToIteratorAdapter.create(javaIterator);
                    assertIterator(test, iterator, false, null);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 7);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null);
                });

                runner.test("with multiple-value Iterator", (Test test) ->
                {
                    final java.util.Iterator<Integer> javaIterator = createJavaIterator(7, 3, 20, 1038);
                    final JavaIteratorToIteratorAdapter<Integer> iterator = JavaIteratorToIteratorAdapter.create(javaIterator);
                    assertIterator(test, iterator, false, null);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 7);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 3);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 20);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 1038);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null);
                });
            });
        });
    }

    static void assertIterator(Test test, JavaIteratorToIteratorAdapter<Integer> iterator, boolean expectedHasStarted, Integer expectedCurrentValue)
    {
        test.assertNotNull(iterator);
        test.assertEqual(expectedHasStarted, iterator.hasStarted());
        test.assertEqual(expectedCurrentValue != null, iterator.hasCurrent());
        if (expectedCurrentValue == null)
        {
            test.assertThrows(iterator::getCurrent,
                new PreConditionFailure("hasCurrent() cannot be false."));
        }
        else
        {
            test.assertEqual(expectedCurrentValue, iterator.getCurrent());
        }
    }

    static java.util.Iterator<Integer> createJavaIterator(int... values)
    {
        final java.util.ArrayList<Integer> javaList = new java.util.ArrayList<>();
        for (final int value : values)
        {
            javaList.add(value);
        }
        return javaList.iterator();
    }
}
