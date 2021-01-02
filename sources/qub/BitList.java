package qub;

public abstract class BitList implements List<Integer>
{
    protected final int bitChunkSize;

    protected BitList(int bitChunkSize)
    {
        PreCondition.assertGreaterThanOrEqualTo(bitChunkSize, 1, "bitChunkSize");

        this.bitChunkSize = bitChunkSize;
    }

    private int getBitChunkIndex(long index)
    {
        return (int)(index / this.bitChunkSize);
    }

    private int getBitChunkOffset(long index)
    {
        return (int)(index % this.bitChunkSize);
    }

    @Override
    public abstract int getCount();

    protected abstract Integer get(int bitChunkIndex, int bitChunkOffset);

    @Override
    public Integer get(int index)
    {
        return this.get((long)index);
    }

    public Integer get(long index)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.getCount() - 1, "index");

        return this.get(this.getBitChunkIndex(index), this.getBitChunkOffset(index));
    }

    protected abstract BitList set(int bitChunkIndex, int bitChunkOffset, int value);

    @Override
    public BitList set(int index, Integer value)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.getCount() - 1, "index");
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertBetween(0, value, 1, "value");

        return this.set(this.getBitChunkIndex(index), this.getBitChunkOffset(index), value);
    }

    protected abstract BitList insert(int bitChunkIndex, int bitChunkOffset, int value);

    @Override
    public BitList insert(int insertIndex, Integer value)
    {
        PreCondition.assertBetween(0, insertIndex, this.getCount(), "index");
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertBetween(0, value, 1, "value");

        return this.insert(this.getBitChunkIndex(insertIndex), this.getBitChunkOffset(insertIndex), value);
    }

    protected abstract int removeAt(int bitChunkIndex, int bitChunkOffset);

    @Override
    public Integer removeAt(int index)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.getCount() - 1, "index");

        return this.removeAt(this.getBitChunkIndex(index), this.getBitChunkOffset(index));
    }
}
