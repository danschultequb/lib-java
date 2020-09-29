package qub;

public interface BasicSaveableIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicSaveableIterator.class, () ->
        {
            SaveableIteratorTests.test(runner, BasicSaveableIterator::create);

            runner.testGroup("create(Iterator<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> BasicSaveableIterator.create(null),
                        new PreConditionFailure("innerIterator cannot be null."));
                });

                runner.test("with non-started and empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertEqual(Iterable.create(), iterator.getBuffer());
                });

                runner.test("with non-started and non-empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3, 4);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertEqual(Iterable.create(), iterator.getBuffer());
                });

                runner.test("with started and empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    innerIterator.next();
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertEqual(Iterable.create(), iterator.getBuffer());
                });

                runner.test("with started and non-empty", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3, 4);
                    innerIterator.next();
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(Iterable.create(1), iterator.getBuffer());
                });
            });

            runner.testGroup("next()", () ->
            {
                runner.test("with no saves", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, 0, Iterable.create(i), 0);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 1, Iterable.create(3), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save before iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        final List<Integer> expectedBuffer = List.create();
                        for (int j = 1; j <= i; j++)
                        {
                            expectedBuffer.add(j);
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, expectedBuffer, 1);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 3, Iterable.create(1, 2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save during iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, 0, Iterable.create(i), 0);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertTrue(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 3, 1, Iterable.create(2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 3);

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 2, Iterable.create(2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save before iteration and then disposed during iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        final List<Integer> expectedBuffer = List.create();
                        for (int j = 1; j <= i; j++)
                        {
                            expectedBuffer.add(j);
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, expectedBuffer, 1);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertTrue(save.dispose().await());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 2, 1, Iterable.create(1, 2), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);

                    test.assertTrue(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 3, 0, Iterable.create(3), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 3);

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 1, Iterable.create(3), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save before iteration and then disposed after iteration", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        final List<Integer> expectedBuffer = List.create();
                        for (int j = 1; j <= i; j++)
                        {
                            expectedBuffer.add(j);
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, expectedBuffer, 1);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 3, Iterable.create(1, 2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertTrue(save.dispose().await());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, -1, Iterable.create(), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save at each step, disposed after iteration in order", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    final List<Save> saves = List.create();
                    saves.add(iterator.save());

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        final List<Integer> expectedBuffer = List.create();
                        for (int j = 1; j <= i; j++)
                        {
                            expectedBuffer.add(j);
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, expectedBuffer, i);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                        saves.add(iterator.save());
                    }

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 3, Iterable.create(1, 2, 3), 4);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    saves.add(iterator.save());

                    for (int i = 0; i < saves.getCount(); i++)
                    {
                        final Save save = saves.get(i);
                        test.assertTrue(save.dispose().await());
                        final int expectedBufferCurrentIndex;
                        final List<Integer> expectedBuffer = List.create();
                        if (i < saves.getCount() - 1)
                        {
                            expectedBufferCurrentIndex = 3;
                            expectedBuffer.addAll(1, 2, 3);
                        }
                        else
                        {
                            expectedBufferCurrentIndex = -1;
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, expectedBufferCurrentIndex, expectedBuffer, 4 - i);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    }

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, -1, Iterable.create(), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("with save at each step, disposed after iteration in reverse order", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    final List<Save> saves = List.create();
                    saves.insert(0, iterator.save());

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        final List<Integer> expectedBuffer = List.create();
                        for (int j = 1; j <= i; j++)
                        {
                            expectedBuffer.add(j);
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, expectedBuffer, i);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                        saves.insert(0, iterator.save());
                    }

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 3, Iterable.create(1, 2, 3), 4);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    saves.insert(0, iterator.save());

                    for (int i = 0; i < saves.getCount(); i++)
                    {
                        final Save save = saves.get(i);
                        test.assertTrue(save.dispose().await());
                        final int expectedBufferCurrentIndex;
                        final List<Integer> expectedBuffer = List.create();
                        if (i < saves.getCount() - 1)
                        {
                            expectedBufferCurrentIndex = 3;
                            expectedBuffer.addAll(1, 2, 3);
                        }
                        else
                        {
                            expectedBufferCurrentIndex = -1;
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, expectedBufferCurrentIndex, expectedBuffer, 4 - i);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    }

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, -1, Iterable.create(), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });
            });

            runner.testGroup("save()", () ->
            {
                runner.test("when not started", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertTrue(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 1, 0, Iterable.create(1), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 1);

                    save.restore().await();

                    BasicSaveableIteratorTests.assertIterator(test, iterator, false, false, null, -1, Iterable.create(1), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 1);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        final List<Integer> expectedBuffer = List.create();
                        for (int j = 1; j <= i; j++)
                        {
                            expectedBuffer.add(j);
                        }
                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, expectedBuffer, 1);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, i);
                    }

                    save.restore().await();

                    BasicSaveableIteratorTests.assertIterator(test, iterator, false, false, null, -1, Iterable.create(1, 2), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, Iterable.create(1, 2), 1);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);
                    }

                    test.assertFalse(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 2, Iterable.create(1, 2), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    save.restore().await();

                    for (int i = 1; i <= 2; i++)
                    {
                        test.assertTrue(iterator.next());

                        BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, i, i - 1, Iterable.create(1, 2), 1);
                        BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                    }

                    test.assertFalse(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 2, Iterable.create(1, 2), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("when at the end of the inner iterator", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    test.assertFalse(iterator.next());

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertFalse(iterator.next());
                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 0, Iterable.create(), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    save.restore().await();

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 0, Iterable.create(), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertFalse(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 0, Iterable.create(), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
                });

                runner.test("when in the middle of the inner iterator", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final BasicSaveableIterator<Integer> iterator = BasicSaveableIterator.create(innerIterator);

                    test.assertTrue(iterator.next());
                    test.assertTrue(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 2, 0, Iterable.create(2), 0);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, true, 2);

                    final Save save = iterator.save();
                    test.assertNotNull(save);

                    test.assertTrue(iterator.next());
                    test.assertFalse(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 2, Iterable.create(2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    save.restore().await();

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 2, 0, Iterable.create(2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertTrue(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, true, 3, 1, Iterable.create(2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);

                    test.assertFalse(iterator.next());

                    BasicSaveableIteratorTests.assertIterator(test, iterator, true, false, null, 2, Iterable.create(2, 3), 1);
                    BasicSaveableIteratorTests.assertIterator(test, innerIterator, true, false, null);
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

    private static <T> void assertIterator(Test test, BasicSaveableIterator<T> iterator, boolean expectedHasStarted, boolean expectedHasCurrent, T expectedCurrentValue, int expectedBufferCurrentIndex, Iterable<Integer> expectedBuffer, int expectedSaveCount)
    {
        BasicSaveableIteratorTests.assertIterator(test, iterator, expectedHasStarted, expectedHasCurrent, expectedCurrentValue);

        test.assertEqual(expectedBufferCurrentIndex, iterator.getBufferCurrentIndex(), "Wrong buffer current index.");
        test.assertEqual(expectedBuffer, iterator.getBuffer(), "Wrong buffer.");
        test.assertEqual(expectedSaveCount, iterator.getSaveCount(), "Wrong save count.");
    }
}
