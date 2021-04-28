package qub;

public interface IteratorActions<T>
{
    /**
     * Indicate that the provided value should be returned by the Iterator.
     * @param value The value to return.
     */
    default void returnValue(T value)
    {
        this.returnValues(Iterator.create(value));
    }

    /**
     * Indicate that the provided values should be returned by the Iterator.
     * @param values The values to return.
     */
    void returnValues(Iterator<T> values);

    /**
     * Indicate that the provided values should be returned by the Iterator.
     * @param values The values to return.
     */
    default void returnValues(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.returnValues(values.iterate());
    }
}
