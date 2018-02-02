package qub;

public class QueueTests
{
    public static void test(final TestRunner runner, final Function0<Queue<Integer>> createQueue)
    {
        runner.testGroup("Queue<T>", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("enqueue()", new Action1<qub.Test>()
                {
                    @Override
                    public void run(Test test)
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
                    }
                });
                
                runner.test("peek()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Queue<Integer> queue = createQueue.run();
                        test.assertNull(queue.peek());

                        queue.enqueue(20);
                        test.assertEqual(20, queue.peek());

                        queue.enqueue(21);
                        test.assertEqual(20, queue.peek());

                        test.assertEqual(20, queue.dequeue());
                        test.assertEqual(21, queue.peek());
                    }
                });
            }
        });
    }
}
