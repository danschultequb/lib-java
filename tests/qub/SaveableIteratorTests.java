package qub;

public interface SaveableIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SaveableIterator.class, () ->
        {
            runner.testGroup("create(Iterator<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SaveableIterator.create(null),
                        new PreConditionFailure("innerIterator cannot be null."));
                });

                runner.test("with non-started and empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    final SaveableIterator<Integer> iterator = SaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with non-started and non-empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3, 4);
                    final SaveableIterator<Integer> iterator = SaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with started and empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    innerIterator.next();
                    final SaveableIterator<Integer> iterator = SaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with started and non-empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3, 4);
                    innerIterator.next();
                    final SaveableIterator<Integer> iterator = SaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                });
            });
        });
    }
    
    static void test(TestRunner runner, Function1<Iterator<Integer>,SaveableIterator<Integer>> creator)
    {
        runner.testGroup(SaveableIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer elementCount, Boolean started) ->
            {
                final Array<Integer> array = Array.createWithLength(elementCount);
                for (int i = 0; i < elementCount; ++i)
                {
                    array.set(i, i);
                }
                final Iterator<Integer> innerIterator = array.iterate();
                if (started)
                {
                    innerIterator.next();
                }
                return creator.run(innerIterator);
            });

            runner.testGroup("next()", () ->
            {
                runner.test("with no saves", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save before iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save during iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertTrue(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 3);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 3);

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save before iteration and then disposed during iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertTrue(save.dispose().await());
                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 2);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);

                    test.assertTrue(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 3);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 3);

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save before iteration and then disposed after iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertTrue(save.dispose().await());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save at each step, disposed after iteration in order", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    final List<Save> saves = List.create();
                    saves.add(iterator.save());

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                        saves.add(iterator.save());
                    }

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    saves.add(iterator.save());

                    for (int i = 0; i < saves.getCount(); i++)
                    {
                        final Save save = saves.get(i);
                        test.assertTrue(save.dispose().await());
                        SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    }

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save at each step, disposed after iteration in reverse order", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    final List<Save> saves = List.create();
                    saves.insert(0, iterator.save());

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                        saves.insert(0, iterator.save());
                    }

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    saves.insert(0, iterator.save());

                    for (int i = 0; i < saves.getCount(); i++)
                    {
                        final Save save = saves.get(i);
                        test.assertTrue(save.dispose().await());
                        SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    }

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });
            });

            runner.testGroup("save()", () ->
            {
                runner.test("when not started", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertTrue(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 1);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 1);

                    save.restore().await();

                    SaveableIteratorTests.assertIterator(test, iterator, false, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 1);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    save.restore().await();

                    SaveableIteratorTests.assertIterator(test, iterator, false, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);
                    }

                    test.assertFalse(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    save.restore().await();

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        SaveableIteratorTests.assertIterator(test, iterator, true, true, i);
                        SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    }

                    test.assertFalse(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("when at the end of the inner iterator", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    test.assertFalse(iterator.next());

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertFalse(iterator.next());
                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    save.restore().await();

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertFalse(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("when in the middle of the inner iterator", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final SaveableIterator<Integer> iterator = creator.run(innerIterator);

                    test.assertTrue(iterator.next());
                    test.assertTrue(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 2);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertTrue(iterator.next());
                    test.assertFalse(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    save.restore().await();

                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 2);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertTrue(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, true, 3);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertFalse(iterator.next());

                    SaveableIteratorTests.assertIterator(test, iterator, true, false, null);
                    SaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });
            });
        });
    }

    private static <T> void assertIterator(Test test, Iterator<T> iterator, boolean expectedHasStarted, boolean expectedHasCurrent, T expectedCurrentValue)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNull(iterator, "iterator");

        test.assertEqual(expectedHasStarted, iterator.hasStarted());
        test.assertEqual(expectedHasCurrent, iterator.hasCurrent());
        if (expectedHasCurrent)
        {
            test.assertEqual(expectedCurrentValue, iterator.getCurrent());
        }
        else
        {
            test.assertThrows(iterator::getCurrent,
                new PreConditionFailure("this.hasCurrent() cannot be false."));
        }
    }
}
