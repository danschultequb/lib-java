package qub;

/**
 * A BitList implementation that internally uses an int[].
 */
public class IntegerBitList extends BitList
{
    private final IntegerList integerList;
    private int bitCount;

    private IntegerBitList()
    {
        super(Integers.bitCount);

        this.integerList = IntegerList.create();
    }

    public static IntegerBitList create()
    {
        return new IntegerBitList();
    }

    @Override
    public int getCount()
    {
        return this.bitCount;
    }

    @Override
    protected Integer get(int bitChunkIndex, int bitChunkOffset)
    {
        return Integers.getBit(this.integerList.getAsInt(bitChunkIndex), bitChunkOffset);
    }

    @Override
    protected BitList set(int bitChunkIndex, int bitChunkOffset, int value)
    {
        this.integerList.set(bitChunkIndex, Integers.setBit(this.integerList.getAsInt(bitChunkIndex), bitChunkOffset, value));

        return this;
    }

    @Override
    protected BitList insert(int bitChunkIndex, int bitChunkOffset, int value)
    {
        int bitChunk = this.integerList.getAsInt(bitChunkIndex);

        int modifiedBitChunk = Integers.setBit(bitChunk, bitChunkOffset, value);
        for (int i = bitChunkOffset; i < this.bitChunkSize - 1; i++)
        {
            modifiedBitChunk = Integers.setBit(modifiedBitChunk, i + 1, Integers.getBit(bitChunk, i));
        }

        int carryOverBit = Integers.getBit(bitChunk, this.bitChunkSize - 1);
        for (int i = bitChunkIndex + 1; i < this.integerList.getCount(); i++)
        {
            bitChunk = this.integerList.getAsInt(i);
            this.integerList.set(i, (carryOverBit << (this.bitChunkSize - 1)) | (bitChunk >>> 1));
            carryOverBit = Integers.getBit(bitChunk, this.bitChunkSize - 1);
        }

        this.bitCount++;

        return this;
    }

    @Override
    protected int removeAt(int bitChunkIndex, int bitChunkOffset)
    {
        return 0;
    }

    @Override
    public Iterator<Integer> iterate()
    {
        return null;
    }
}
