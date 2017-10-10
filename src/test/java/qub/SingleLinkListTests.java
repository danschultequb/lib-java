package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class SingleLinkListTests extends ListTests
{
    @Override
    protected List<Integer> createList(int count)
    {
        final SingleLinkList<Integer> result = new SingleLinkList<>();
        for (int i = 0; i < count; ++i)
        {
            result.add(i);
        }
        return result;
    }

    @Test
    public void fromValues()
    {
        final SingleLinkList<Integer> list1 = SingleLinkList.fromValues();
        assertEquals(0, list1.getCount());

        final SingleLinkList<Integer> list2 = SingleLinkList.fromValues((Integer[])null);
        assertEquals(0, list2.getCount());

        final SingleLinkList<Integer> arrayList3 = SingleLinkList.fromValues(1, 2, 3);
        assertEquals(3, arrayList3.getCount());
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(i + 1, arrayList3.get(i).intValue());
        }

        final SingleLinkList<Integer> arrayList4 = SingleLinkList.fromValues((Iterator<Integer>)null);
        assertEquals(0, arrayList4.getCount());

        final SingleLinkList<Integer> arrayList5 = SingleLinkList.fromValues(new Array<Integer>(0).iterate());
        assertEquals(0, arrayList5.getCount());

        final SingleLinkList<Integer> arrayList6 = SingleLinkList.fromValues(Array.fromValues(1, 2, 3).iterate());
        assertEquals(3, arrayList6.getCount());
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(i + 1, arrayList6.get(i).intValue());
        }

        final SingleLinkList<Integer> arrayList7 = SingleLinkList.fromValues((Iterable<Integer>)null);
        assertEquals(0, arrayList7.getCount());

        final SingleLinkList<Integer> arrayList8 = SingleLinkList.fromValues(new Array<Integer>(0));
        assertEquals(0, arrayList8.getCount());

        final SingleLinkList<Integer> arrayList9 = SingleLinkList.fromValues(Array.fromValues(1, 2, 3));
        assertEquals(3, arrayList9.getCount());
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(i + 1, arrayList9.get(i).intValue());
        }
    }
}
