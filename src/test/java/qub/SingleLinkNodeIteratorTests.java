//package qub;
//
//public class SingleLinkNodeIteratorTests extends IteratorTests
//{
//    @Override
//    protected Iterator<Integer> createIterator(int count, boolean started)
//    {
//        SingleLinkNode<Integer> head = null;
//        SingleLinkNode<Integer> tail = null;
//        for (int i = 0; i < count; ++i)
//        {
//            if (head == null)
//            {
//                head = new SingleLinkNode<>(i);
//                tail = head;
//            }
//            else
//            {
//                tail.setNext(new SingleLinkNode<>(i));
//                tail = tail.getNext();
//            }
//        }
//
//        final SingleLinkNodeIterator<Integer> iterator = new SingleLinkNodeIterator<>(head);
//
//        if (started)
//        {
//            iterator.next();
//        }
//
//        return iterator;
//    }
//}
