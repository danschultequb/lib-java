package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoubleLinkNodeTests extends IterableTests
{
    @Override
    protected Iterable<Integer> createIterable(int count)
    {
        DoubleLinkNode<Integer> result = null;

        if (count > 0)
        {
            result = new DoubleLinkNode<>(0);
            DoubleLinkNode<Integer> currentNode = result;
            for (int i = 1; i < count; ++i)
            {
                final DoubleLinkNode<Integer> nextNode = new DoubleLinkNode<>(i);
                currentNode.setNext(nextNode);
                nextNode.setPrevious(currentNode);

                currentNode = nextNode;
            }
        }

        return result;
    }

    @Test
    public void constructor()
    {
        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(20);
        assertEquals(20, node.getValue().intValue());
        assertNull(node.getPrevious());
        assertNull(node.getNext());
    }

    @Test
    public void setValueWithNull()
    {
        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(10);
        node.setValue(null);
        assertNull(node.getValue());
    }

    @Test
    public void setValueWithNonNull()
    {
        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(10);
        node.setValue(20);
        assertEquals(20, node.getValue().intValue());
    }

    @Test
    public void setPrevious()
    {
        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(11);
        node.setPrevious(node);
        assertSame(node, node.getPrevious());
    }

    @Test
    public void setNext()
    {
        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(11);
        node.setNext(node);
        assertSame(node, node.getNext());
    }
}
