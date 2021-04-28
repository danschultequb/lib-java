package qub;

public interface QueueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Queue.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final Queue<Integer> queue = Queue.create();
                test.assertNotNull(queue);
                test.assertEqual(0, queue.getCount());
            });

            runner.testGroup("create(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Queue.create((Iterable<Integer>)null),
                        new PreConditionFailure("initialValues cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Queue<Integer> queue = Queue.create(Iterable.create());
                    test.assertNotNull(queue);
                    test.assertEqual(0, queue.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Queue<Integer> queue = Queue.create(Iterable.create(1, 2, 3));
                    test.assertNotNull(queue);
                    test.assertEqual(1, queue.dequeue().await());
                    test.assertEqual(2, queue.dequeue().await());
                    test.assertEqual(3, queue.dequeue().await());
                    test.assertEqual(0, queue.getCount());
                });
            });
        });
    }

    static void test(TestRunner runner, Function0<Queue<Integer>> createQueue)
    {
        runner.testGroup(Queue.class, () ->
        {
            runner.test("enqueue()", test ->
            {
                final Queue<Integer> queue = createQueue.run();
                test.assertThrows(() -> queue.dequeue().await(),
                    new QueueEmptyException());
                test.assertFalse(queue.any());

                queue.enqueue(0);
                test.assertTrue(queue.any());
                test.assertEqual(1, queue.getCount());

                queue.enqueue(1);
                test.assertTrue(queue.any());
                test.assertEqual(2, queue.getCount());

                test.assertEqual(0, queue.dequeue().await());
                test.assertTrue(queue.any());
                test.assertEqual(1, queue.getCount());

                test.assertEqual(1, queue.dequeue().await());
                test.assertFalse(queue.any());
                test.assertEqual(0, queue.getCount());

                test.assertThrows(() -> queue.dequeue().await(),
                    new QueueEmptyException());
                test.assertFalse(queue.any());
                test.assertEqual(0, queue.getCount());
            });

            runner.testGroup("enqueueAll(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    test.assertThrows(() -> queue.enqueueAll((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(0, queue.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    final Queue<Integer> enqueueAllTests = queue.enqueueAll(Iterable.create());
                    test.assertSame(queue, enqueueAllTests);
                    test.assertEqual(0, queue.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    final Queue<Integer> enqueueAllTests = queue.enqueueAll(Iterable.create(0, 1, 2));
                    test.assertSame(queue, enqueueAllTests);
                    test.assertEqual(3, queue.getCount());
                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertEqual(i, queue.dequeue().await());
                    }
                });
            });

            runner.test("peek()", test ->
            {
                final Queue<Integer> queue = createQueue.run();
                test.assertThrows(() -> queue.peek().await(),
                    new QueueEmptyException());

                queue.enqueue(20);
                test.assertEqual(20, queue.peek().await());

                queue.enqueue(21);
                test.assertEqual(20, queue.peek().await());

                test.assertEqual(20, queue.dequeue().await());
                test.assertEqual(21, queue.peek().await());
            });
        });
    }
}
