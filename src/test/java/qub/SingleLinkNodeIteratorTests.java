package qub;

public class SingleLinkNodeIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("SingleLinkNodeIterator<T>", new Action0()
        {
            @Override
            public void run()
            {
                IteratorTests.test(runner, new Function2<Integer, Boolean, Iterator<Integer>>()
                {
                    @Override
                    public Iterator<Integer> run(Integer count, Boolean started)
                    {
                        SingleLinkNode<Integer> head = null;
                        SingleLinkNode<Integer> tail = null;
                        for (int i = 0; i < count; ++i)
                        {
                            if (head == null)
                            {
                                head = new SingleLinkNode<>(i);
                                tail = head;
                            }
                            else
                            {
                                tail.setNext(new SingleLinkNode<>(i));
                                tail = tail.getNext();
                            }
                        }

                        final SingleLinkNodeIterator<Integer> iterator = new SingleLinkNodeIterator<>(head);

                        if (started)
                        {
                            iterator.next();
                        }

                        return iterator;
                    }
                });
            }
        });
    }
}
