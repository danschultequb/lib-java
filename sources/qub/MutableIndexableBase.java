package qub;

public abstract class MutableIndexableBase<T> extends IndexableBase<T> implements MutableIndexable<T>
{
    @Override
    public void setFirst(T value)
    {
        MutableIndexableBase.setFirst(this, value);
    }

    @Override
    public void setLast(T value)
    {
        MutableIndexableBase.setLast(this, value);
    }

    public static <T> void setFirst(MutableIndexable<T> mutableIndexable, T value)
    {
        PreCondition.assertNotNull(mutableIndexable, "mutableIndexable");
        PreCondition.assertTrue(mutableIndexable.any(), "mutableIndexable.any()");

        mutableIndexable.set(0, value);
    }

    public static <T> void setLast(MutableIndexable<T> mutableIndexable, T value)
    {
        PreCondition.assertNotNull(mutableIndexable, "mutableIndexable");
        PreCondition.assertTrue(mutableIndexable.any(), "mutableIndexable.any()");

        mutableIndexable.set(mutableIndexable.getCount() - 1, value);
    }
}
