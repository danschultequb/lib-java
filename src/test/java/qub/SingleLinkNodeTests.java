package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class SingleLinkNodeTests extends IterableTests
{
    @Override
    protected Iterable<Integer> createIterable(int count)
    {
        SingleLinkNode<Integer> head = null;
        if (count > 0)
        {
            SingleLinkNode<Integer> tail = null;
            for (int i = 0; i < count; ++i)
            {
                if(head == null)
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
        }
        return head;
    }

    @Test
    public void constructorWithNull()
    {
        final SingleLinkNode<Integer> node = new SingleLinkNode<>(null);
        assertNull(node.getValue());
        assertNull(node.getNext());
    }

    @Test
    public void constructorWithNonNull()
    {
        final SingleLinkNode<Integer> node = new SingleLinkNode<>(55);
        assertEquals(55, node.getValue().intValue());
        assertNull(node.getNext());
    }

    @Test
    public void setValueWithNull()
    {
        final SingleLinkNode<Integer> node = new SingleLinkNode<>(33);
        node.setValue(null);
        assertNull(node.getValue());
    }

    @Test
    public void setValueWithNonNull()
    {
        final SingleLinkNode<Integer> node = new SingleLinkNode<>(33);
        node.setValue(44);
        assertEquals(44, node.getValue().intValue());
    }

    @Test
    public void setNextWithNull()
    {
        final SingleLinkNode<Integer> node = new SingleLinkNode<>(66);
        node.setNext(null);
        assertNull(node.getNext());
    }

    @Test
    public void setNextWithNonNull()
    {
        final SingleLinkNode<Integer> node = new SingleLinkNode<>(77);
        node.setNext(node);
        assertSame(node, node.getNext());
    }
}
