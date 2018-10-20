package qub;

public class BitArrayBlockIterator implements Iterator<BitArray>
{
    private final BitArray source;
    private final long blockSize;
    private boolean hasStarted;
    private long currentBitIndex;
    private BitArray currentBlock;

    public BitArrayBlockIterator(BitArray source, long blockSize)
    {
        this.source = source;
        this.blockSize = blockSize;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return currentBlock != null;
    }

    @Override
    public BitArray getCurrent()
    {
        return currentBlock;
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentBitIndex < source.getBitCount())
        {
            currentBitIndex += blockSize;
        }

        if (currentBitIndex < source.getBitCount())
        {
            currentBlock = BitArray.fromBitCount(blockSize);
            currentBlock.copyFrom(source, currentBitIndex, 0, blockSize);
        }
        else
        {
            currentBlock = null;
        }

        return hasCurrent();
    }
}
