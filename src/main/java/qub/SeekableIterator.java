package qub;

/**
 * An Iterator that can seek through its contents.
 */
public interface SeekableIterator<T> extends Iterator<T>
{
    /**
     * Get the element index that this SeekableIterator is currently pointing at.
     * @return The element index that this SeekableInterator is currently pointing at.
     */
    int getCurrentIndex();

    /**
     * Set the element index that this SeekableIterator is pointing at. If the provided index is
     * less than zero, then the current element index will be set to 0. If the provided index is
     * greater than the number of elements in the Iterator, then the current element index will be
     * set to the number of elements in the Iterator.
     * @param index The element index that this SeekableIterator will point to.
     */
    void setCurrentIndex(int index);
}
