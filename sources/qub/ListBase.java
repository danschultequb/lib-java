package qub;

public abstract class ListBase<T> extends MutableIndexableBase<T> implements List<T>
{
    @Override
    public void addAll(T[] values)
    {
        ListBase.addAll(this, values);
    }

    @Override
    public void addAll(Iterator<T> values)
    {
        ListBase.addAll(this, values);
    }

    @Override
    public void addAll(Iterable<T> values)
    {
        ListBase.addAll(this, values);
    }

    @Override
    public boolean remove(T value)
    {
        return ListBase.remove(this, value);
    }

    @Override
    public T removeFirst()
    {
        return ListBase.removeFirst(this);
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        return ListBase.removeFirst(this, condition);
    }

    @Override
    public T removeLast()
    {
        return ListBase.removeLast(this);
    }

    @Override
    public void clear()
    {
        ListBase.clear(this);
    }

    @Override
    public boolean endsWith(T value)
    {
        final int count = getCount();
        return count > 0 && Comparer.equal(get(count - 1), value);
    }

    @Override
    public boolean endsWith(Iterable<T> values)
    {
        boolean result = false;

        final int count = getCount();
        final int valuesCount = values == null ? 0 : values.getCount();
        if (count >= valuesCount)
        {
            result = this.takeLast(valuesCount).equals(values);
        }

        return result;
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    public static <T> void addAll(List<T> list, T[] values)
    {
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                list.add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    public static <T> void addAll(List<T> list, Iterator<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                list.add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    public static <T> void addAll(List<T> list, Iterable<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                list.add(value);
            }
        }
    }

    public static <T,U> Result<T> validateInsertIndex(List<U> list, int insertIndex)
    {
        return Result.between(0, insertIndex, list.getCount(), "insertIndex");
    }

    /**
     * Remove the first value in this List that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this List was removed.
     */
    public static <T> boolean remove(List<T> list, T value)
    {
        final int indexToRemove = list.indexOf(value);
        final boolean result = indexToRemove != -1;
        if (result)
        {
            list.removeAt(indexToRemove);
        }
        return result;
    }

    /**
     * Remove and return the first value in this ArrayList. If this ArrayList is empty, then null
     * will be returned.
     * @return The value that was removed, or null if the ArrayList was empty.
     */
    public static <T> T removeFirst(List<T> list)
    {
        return list.removeAt(0);
    }

    public static <T> T removeFirst(List<T> list, Function1<T,Boolean> condition)
    {
        final int removeIndex = list.indexOf(condition);
        return removeIndex < 0 ? null : list.removeAt(removeIndex);
    }

    public static <T> T removeLast(List<T> list)
    {
        final int count = list.getCount();
        return count == 0 ? null : list.removeAt(count - 1);
    }

    public static <T> void clear(List<T> list)
    {
        while (list.any())
        {
            list.removeFirst();
        }
    }
}
