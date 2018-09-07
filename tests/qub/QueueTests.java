package qub;

public class QueueTests
{
    public static void test(TestRunner runner, Function0<Queue<Integer>> createQueue)
    {
        runner.testGroup(Queue.class, () ->
        {
            runner.test("enqueue()", test ->
            {
                final Queue<Integer> queue = createQueue.run();
                test.assertNull(queue.dequeue());
                test.assertFalse(queue.any());

                queue.enqueue(0);
                test.assertTrue(queue.any());
                test.assertEqual(1, queue.getCount());

                queue.enqueue(1);
                test.assertTrue(queue.any());
                test.assertEqual(2, queue.getCount());

                test.assertEqual(0, queue.dequeue());
                test.assertTrue(queue.any());
                test.assertEqual(1, queue.getCount());

                test.assertEqual(1, queue.dequeue());
                test.assertFalse(queue.any());
                test.assertEqual(0, queue.getCount());

                test.assertNull(queue.dequeue());
                test.assertFalse(queue.any());
                test.assertEqual(0, queue.getCount());
            });

            runner.testGroup("enqueueAll(T[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    test.assertThrows(() -> queue.enqueueAll((Integer[])null));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    queue.enqueueAll(new Integer[0]);
                    test.assertEqual(0, queue.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    queue.enqueueAll(new Integer[] { 0, 1, 2 });
                    test.assertEqual(3, queue.getCount());
                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertEqual(i, queue.dequeue());
                    }
                });
            });

            runner.testGroup("enqueueAll(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    test.assertThrows(() -> queue.enqueueAll((Iterable<Integer>)null));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    queue.enqueueAll(new Array<>(0));
                    test.assertEqual(0, queue.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Queue<Integer> queue = createQueue.run();
                    queue.enqueueAll(Array.fromValues(new Integer[] { 0, 1, 2 }));
                    test.assertEqual(3, queue.getCount());
                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertEqual(i, queue.dequeue());
                    }
                });
            });

            runner.test("peek()", test ->
            {
                final Queue<Integer> queue = createQueue.run();
                test.assertNull(queue.peek());

                queue.enqueue(20);
                test.assertEqual(20, queue.peek());

                queue.enqueue(21);
                test.assertEqual(20, queue.peek());

                test.assertEqual(20, queue.dequeue());
                test.assertEqual(21, queue.peek());
            });
        });
    }
}
