//package qub;
//
//public class MapIteratorTests extends IteratorTests
//{
//    @Override
//    protected Iterator<Integer> createIterator(int count, boolean started)
//    {
//        final Array<Long> array = new Array<>(count);
//        for (int i = 0; i < count; ++i)
//        {
//            array.set(i, (long)i);
//        }
//
//        final Iterator<Integer> iterator = array.iterate().map(new Function1<Long, Integer>()
//        {
//            @Override
//            public Integer run(Long value)
//            {
//                return value.intValue();
//            }
//        });
//
//        if (started)
//        {
//            iterator.next();
//        }
//
//        return iterator;
//    }
//}
