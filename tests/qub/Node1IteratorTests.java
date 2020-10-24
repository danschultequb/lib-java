package qub;

public interface Node1IteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Node1Iterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                Node1<Integer> head = null;
                Node1<Integer> tail = null;
                for (int i = 0; i < count; ++i)
                {
                    if (head == null)
                    {
                        head = Node1.create(i);
                        tail = head;
                    }
                    else
                    {
                        tail.setNode1(Node1.create(i));
                        tail = tail.getNode1();
                    }
                }

                final Node1Iterator<Integer> iterator = new Node1Iterator<>(head);

                if (started)
                {
                    iterator.next();
                }

                return iterator;
            });
        });
    }
}
