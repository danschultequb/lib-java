package qub;

public interface List<T> extends MutableIndexable<T>
{
    /**
     * Add the provided value at the end of this List.
     * @param value The value to add.
     */
    void add(T value);

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    void addAll(T[] values);

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    void addAll(Iterator<T> values);

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    void addAll(Iterable<T> values);

    /**
     * Remove the first value in this List that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this List was removed.
     */
    boolean remove(T value);

    /**
     * Remove and return the value at the provided index. If the index is not in the bounds of this
     * List, then no values will be removed and null will be returned.
     * @param index The index to remove from.
     * @return The value that was removed or null if the index is out of bounds.
     */
    T removeAt(int index);

    /**
     * Remove and return the first value in this ArrayList. If this ArrayList is empty, then null
     * will be returned.
     * @return The value that was removed, or null if the ArrayList was empty.
     */
    T removeFirst();

    /**
     * Remove and return the first value in this List that matches the provided condition. If this
     * List is empty or if no elements match the provided condition, then null will be returned.
     * @param condition The condition to run against each element in this List.
     * @return The element that was removed, or null if no element matched the condition.
     */
    T removeFirst(Function1<T,Boolean> condition);

    /**
     * Remove all of the elements from this List.
     */
    void clear();
}
