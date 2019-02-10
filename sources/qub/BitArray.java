package qub;

/**
 * A contiguous array of bits.
 */
public class BitArray
{
    private static final int bitsPerChunk = Integer.SIZE;
    private static final long maximumBitCount = ((long)Integer.MAX_VALUE) * bitsPerChunk;

    private final int[] chunks;
    private final long bitCount;

    private BitArray(long bitCount)
    {
        this(new int[(int)Math.ceiling(bitCount / 32.0)], bitCount);
    }

    private BitArray(int[] chunks, long bitCount)
    {
        this.bitCount = bitCount;
        this.chunks = chunks;
    }

    /**
     * Clone this BitArray.
     * @return A clone of this BitArray.
     */
    public BitArray clone()
    {
        return BitArray.fromIntArray(chunks, bitCount);
    }

    /**
     * The number of bits contained in this BitArray.
     * @return The number of bits contained in this BitArray.
     * @postCondition result >= 1
     */
    public long getBitCount()
    {
        return bitCount;
    }

    /**
     * The number of chunks that it takes to contain these bits.
     * @return The number of bytes that it takes to contain these bits.
     * @postCondition result >= 1
     */
    public int getChunkCount()
    {
        return chunks.length;
    }

    /**
     * Get the bit at the provided bitIndex. Bit indexes start with the most significant (left-most)
     * bit at 0 and go to the least significant (right-most) bit at bitCount - 1.
     * @param bitIndex The index of the bit to get.
     * @return The bit's value at the provided bitIndex.
     * @preCondition 0 <= bitIndex <= getBitCount() - 1
     * @postCondition result == 0 || result == 1
     */
    public int getBit(long bitIndex)
    {
        PreCondition.assertStartIndex(bitIndex, getBitCount(), "bitIndex");

        final int chunk = chunks[bitIndexToChunkIndex(bitIndex)];
        final int chunkBitOffset = bitIndexToChunkBitOffset(bitIndex);
        return Integers.getBit(chunk, chunkBitOffset);
    }

    /**
     * Set the bit at the provided bitIndex to the provided value. Bit indexes start with the most
     * significant (left-most) bit at 0 and go to the least significant (right-most) bit at
     * bitCount - 1.
     * @param bitIndex The index of the bit to get.
     * @param value The value to set. This can be either 0 or 1.
     * @preCondition 0 <= bitIndex <= getBitCount() - 1
     * @preCondition value == 0 || value == 1
     */
    public void setBit(long bitIndex, int value)
    {
        PreCondition.assertStartIndex(bitIndex, getBitCount(), "bitIndex");
        PreCondition.assertOneOf(value, new int[] { 0, 1 }, "value");

        final int chunkIndex = bitIndexToChunkIndex(bitIndex);
        final int chunkBitOffset = bitIndexToChunkBitOffset(bitIndex);
        chunks[chunkIndex] = Integers.setBit(chunks[chunkIndex], chunkBitOffset, value);
    }

    /**
     * Set all of the bits in this BitArray to be the provided value.
     * @param value The value to set all of the bits in this BitArray to.
     * @preCondition value == 0 || value == 1
     */
    public void setAllBits(int value)
    {
        PreCondition.assertOneOf(value, new int[] { 0, 1 }, "value");

        final int chunkValue = (value == 0 ? 0x00000000 : 0xFFFFFFFF);
        for (int i = 0; i < getChunkCount(); ++i)
        {
            chunks[i] = chunkValue;
        }
    }

    /**
     * Set the last bits of this BitArray to be the bits in the provided value.
     * @param value The long of bits to assign to the end of this BitArray.
     */
    public void setLastBitsFromLong(long value)
    {
        PreCondition.assertGreaterThanOrEqualTo(getBitCount(), Long.SIZE, "getBitCount()");

        copyFrom(value, getBitCount() - Long.SIZE, Long.SIZE);
    }

    /**
     * Copy the bits from the copyFrom BitArray to this BitArray.
     * @param copyFrom The BitArray to copy from.
     * @param copyFromStartIndex The index to start reading from.
     * @param copyToStartIndex The index to start writing to.
     * @param copyLength The number of bits to copy.
     */
    public void copyFrom(BitArray copyFrom, long copyFromStartIndex, long copyToStartIndex, long copyLength)
    {
        PreCondition.assertNotNull(copyFrom, "copyFrom");
        PreCondition.assertStartIndex(copyFromStartIndex, copyFrom.getBitCount(), "copyFromStartIndex");
        PreCondition.assertStartIndex(copyToStartIndex, this.getBitCount(), "copyToStartIndex");
        PreCondition.assertBetween(0, copyLength, Math.minimum(copyFrom.getBitCount() - copyFromStartIndex, this.getBitCount() - copyToStartIndex), "copyLength");

        for (long i = 0; i < copyLength; ++i)
        {
            this.setBit(copyToStartIndex + i, copyFrom.getBit(copyFromStartIndex + i));
        }
    }

    /**
     * Copy the bits from the copyFrom long to this BitArray.
     * @param copyFrom The long to copy from.
     * @param copyToStartIndex The index to start writing to.
     * @param copyLength The number of bits to copy. Bits will be copied from the least significant
     *                   side of the long.
     */
    public void copyFrom(long copyFrom, long copyToStartIndex, int copyLength)
    {
        PreCondition.assertStartIndex(copyToStartIndex, this.getBitCount(), "copyToStartIndex");
        PreCondition.assertLength(copyLength, 0, Long.SIZE, "copyLength");

        while (copyLength > 0)
        {
            this.setBit(copyToStartIndex + copyLength - 1, (int)(copyFrom & 0x1));
            copyFrom = copyFrom >>> 1;
            --copyLength;
        }
    }

    /**
     * Rotate the bits in this BitArray the one space to the left.
     */
    public void rotateLeft()
    {
        rotateLeft(1);
    }

    /**
     * Rotate the bits in this BitArray the provided number of spaces to the left.
     * @param bitsToRotate The number of bit spaces to rotate.
     * @preCondition bitsToRotate >= 0
     */
    public void rotateLeft(long bitsToRotate)
    {
        rotateLeft(bitsToRotate, 0, getBitCount());
    }

    /**
     * Rotate the bits in this BitArray the provided number of spaces to the left starting at the
     * provided startIndex and going for the provided length.
     * @param bitsToRotate The number of bit spaces to rotate.
     * @preCondition bitsToRotate >= 0
     */
    public void rotateLeft(long bitsToRotate, long startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getBitCount());
        PreCondition.assertLength(length, startIndex, getBitCount());

        if (bitsToRotate % getBitCount() != 0)
        {
            final BitArray temp = BitArray.fromBitCount(length);
            for (long i = 0; i < length; ++i)
            {
                final long getIndex = startIndex + Math.modulo(i + bitsToRotate, length);
                temp.setBit(i, this.getBit(getIndex));
            }
            this.copyFrom(temp, 0, startIndex, length);
        }
    }

    /**
     * Rotate the bits in this BitArray the one space to the right.
     */
    public void rotateRight()
    {
        rotateRight(1);
    }

    /**
     * Rotate the bits in this BitArray the provided number of spaces to the right.
     * @param bitsToRotate The number of bit spaces to rotate.
     * @preCondition bitsToRotate >= 0
     */
    public void rotateRight(long bitsToRotate)
    {
        rotateRight(bitsToRotate, 0, getBitCount());
    }

    /**
     * Rotate the bits in this BitArray the provided number of spaces to the right starting at the
     * provided startIndex and going for the provided length.
     * @param bitsToRotate The number of bit spaces to rotate.
     * @preCondition bitsToRotate >= 0
     */
    public void rotateRight(long bitsToRotate, long startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getBitCount());
        PreCondition.assertLength(length, startIndex, getBitCount());

        rotateLeft(-bitsToRotate, startIndex, length);
    }

    /**
     * Shift the bits in this BitArray one position to the left.
     */
    public void shiftLeft()
    {
        shiftLeft(1);
    }

    /**
     * Shift the bits in this BitArray bitsToShift positions to the left.
     * @param bitsToShift The number of positions to shift the bits to the left.
     */
    public void shiftLeft(long bitsToShift)
    {
        shiftLeft(bitsToShift, 0, getBitCount());
    }

    /**
     * Shift the bits in this BitArray in the provided range bitsToShift positions to the left.
     * @param bitsToShift The number of positions to shift the bits to the left.
     * @param startIndex The index to start shifting bits at.
     * @param length The number of bits to shift.
     */
    public void shiftLeft(long bitsToShift, long startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getBitCount());
        PreCondition.assertLength(length, startIndex, getBitCount());

        final BitArray tempBits = BitArray.fromBitCount(length);
        final long afterRangeEndIndex = startIndex + length;
        for (long i = 0; i < length; ++i)
        {
            final long indexToGetFrom = startIndex + i + bitsToShift;
            final int valueToSet = (indexToGetFrom < startIndex || afterRangeEndIndex <= indexToGetFrom ? 0 : getBit(indexToGetFrom));
            tempBits.setBit(i, valueToSet);
        }
        this.copyFrom(tempBits, 0, startIndex, length);
    }

    public void shiftRight()
    {
        shiftLeft(-1);
    }

    public void shiftRight(long bitsToShift)
    {
        shiftLeft(-bitsToShift);
    }

    public void shiftRight(long bitsToShift, long startIndex, long length)
    {
        shiftLeft(-bitsToShift, startIndex, length);
    }

    /**
     * Perform an xor operation between this BitArray and the provided BitArray and return the
     * result.
     * @param rhs The right hand side of the xor operation.
     * @return The result of the xor operation.
     * @preCondition rhs != null
     * @preCondition getBitCount() == rhs.getBitCount()
     * @postCondition result != null
     * @postCondition getBitCount() == result.getBitCount()
     */
    public BitArray xor(BitArray rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertEqual(getBitCount(), rhs.getBitCount(), "rhs.getBitCount()");

        final long bitCount = getBitCount();
        final BitArray result = BitArray.fromBitCount(bitCount);
        for (long i = 0; i < bitCount; ++i)
        {
            result.setBit(i, (getBit(i) + rhs.getBit(i)) % 2);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getBitCount(), result.getBitCount(), "result.getBitCount()");

        return result;
    }

    /**
     * Get the permutation of this BitArray by using the provided bitIndex permutation mapping. The
     * value at each element of the bitIndexPermutations array is an index into this BitArray. For
     * example, if this method were given the permutation array [4, 2, 0, 3, 1], then the result
     * would be a BitArray with this BitArray's 5th bit (the first bit in the array is at index 0,
     * so the 5th bit is at index 4), 3nd bit, 1st bit, 4th bit, and then 2nd
     * bit.
     * @param bitIndexPermutations The bit index permutation mapping.
     * @return The result of applying the provided permutation to this BitArray.
     */
    public BitArray permuteByBitIndex(long[] bitIndexPermutations)
    {
        PreCondition.assertNotNull(bitIndexPermutations, "bitIndexPermutations");

        final BitArray result = BitArray.fromBitCount(bitIndexPermutations.length);
        for (int i = 0; i < bitIndexPermutations.length; ++i)
        {
            final long bitIndex = bitIndexPermutations[i];
            result.setBit(i, getBit(bitIndex));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitIndexPermutations.length, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    /**
     * Get the permutation of this BitArray by using the provided bitNumber permutation mapping. The
     * value at each element of the bitNumberPermutations array is a reference to a bit in this
     * BitArray. For example, if this method were given the permutation array [5, 3, 1, 4, 2], then
     * the result would be a BitArray with this BitArray's 5th bit, 3nd bit, 1st bit, 4th bit, and
     * then 2nd bit.
     * @param bitNumberPermutations The bit number permutation mapping.
     * @return The result of applying the provided permutation to this BitArray.
     */
    public BitArray permuteByBitNumber(long[] bitNumberPermutations)
    {
        return permuteByBitNumber(0, getBitCount(), bitNumberPermutations);
    }

    public BitArray permuteByBitNumber(long startIndex, long length, long[] bitNumberPermutations)
    {
        PreCondition.assertStartIndex(startIndex, getBitCount());
        PreCondition.assertLength(length, startIndex, getBitCount());
        PreCondition.assertNotNull(bitNumberPermutations, "bitNumberPermutations");

        final BitArray result = BitArray.fromBitCount(bitNumberPermutations.length);
        for (int i = 0; i < bitNumberPermutations.length; ++i)
        {
            final long bitNumber = bitNumberPermutations[i];
            final long bitIndex = bitNumber - 1;
            result.setBit(i, getBit(startIndex + bitIndex));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitNumberPermutations.length, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    /**
     * Create a new BitArray that is the result of concatenating this BitArray with the provided
     * BitArray.
     * @param rhs The BitArray to concatenate to the end of this BitArray.
     * @return The concatenated BitArray.
     */
    public BitArray concatenate(BitArray rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final long thisBitCount = this.getBitCount();
        final long rhsBitCount = rhs.getBitCount();
        final BitArray result = BitArray.fromBitCount(thisBitCount + rhsBitCount);
        if (thisBitCount > 0)
        {
            result.copyFrom(this, 0, 0, thisBitCount);
        }
        if (rhsBitCount > 0)
        {
            result.copyFrom(rhs, 0, thisBitCount, rhsBitCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getBitCount() + rhs.getBitCount(), result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public Iterator<Integer> iterate()
    {
        final Iterator<Integer> result = new BitArrayIterator(this);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertFalse(result.hasStarted(), "result.hasStarted()");

        return result;
    }

    /**
     * Iterate through this BitArray by blocking several bits together.
     * @param blockBitCount The size of each block.
     * @return An Iterator that will iterate through the blocks in this BitArray.
     */
    public Iterator<BitArray> iterateBlocks(long blockBitCount)
    {
        PreCondition.assertGreaterThanOrEqualTo(blockBitCount, 1, "blockBitCount");
        PreCondition.assertEqual(0, getBitCount() % blockBitCount, "getBitCount() % blockBitCount");

        final Iterator<BitArray> result = new BitArrayBlockIterator(this, blockBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertFalse(result.hasStarted(), "result.hasStarted()");

        return result;
    }

    /**
     * Iterate through this BitArray by blocking in 32-bit blocks and treating them as integers.
     * @return An Iterator that will iterate through the integer-sized blocks in this BitArray.
     */
    public Iterator<Integer> iterateIntegers()
    {
        PreCondition.assertEqual(0, getBitCount() % Integer.SIZE, "getBitCount() % Integer.SIZE");

        final Iterator<Integer> result = Array.create(chunks).iterate();

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertFalse(result.hasStarted(), "result.hasStarted()");

        return result;
    }

    /**
     * Get the String representation of this BitArray.
     * @return The String representation of this BitArray.
     */
    @Override
    public String toString()
    {
        return toBitString();
    }

    /**
     * Get the integer representation of this BitArray.
     * @return The integer representation of this BitArray.
     */
    public int toInteger()
    {
        return toInteger(0, getBitCount());
    }

    /**
     * Get the integer representation of the subsection of this BitArray.
     * @param startIndex The start index of the subsection.
     * @param length The number of bits in the subsection.
     * @return The integer representation of this BitArray.
     */
    public int toInteger(int startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getBitCount());
        PreCondition.assertBetween(1, length, Math.minimum(getBitCount() - startIndex, Integer.SIZE), "length");

        int mask = 1;
        for (int i = 1; i < length; ++i)
        {
            mask = (mask << 1) | 0x1;
        }

        final int resultBeforeMask = this.chunks[0] >>> (bitsPerChunk - (startIndex + length));
        final int result = resultBeforeMask & mask;

        return result;
    }

    /**
     * Get the binary (bit) representation of this BitArray.
     * @return The binary (bit) representation of this BitArray.
     */
    public String toBitString()
    {
        final long bitCount = getBitCount();
        final StringBuilder builder = new StringBuilder((int)bitCount);
        for (long i = 0; i < bitCount; ++i)
        {
            builder.append(getBit(i));
        }
        return builder.toString();
    }

    public String toHexString()
    {
        final StringBuilder builder = new StringBuilder();
        for (int chunk : chunks)
        {
            builder.append(Integers.toHexString(chunk, false));
        }
        final long lastChunkBitCount = bitCount % bitsPerChunk;
        if (lastChunkBitCount != 0)
        {
            final int lastChunkHexCharCount = (int)Math.ceiling(lastChunkBitCount / 4.0);
            final int charsToRemove = (bitsPerChunk / 4) - lastChunkHexCharCount;
            builder.setLength(builder.length() - charsToRemove);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof BitArray && equals((BitArray)rhs);
    }

    /**
     * Get whether or not the bits in this BitArray object are the same as the bits in the provided rhs
     * BitArray object.
     * @param rhs The BitArray object to compare against this BitArray object.
     * @return whether or not the bits in this BitArray object are the same as the bits in the provided
     * rhs BitArray object.
     */
    public boolean equals(BitArray rhs)
    {
        return rhs != null &&
           bitCount == rhs.bitCount &&
           Comparer.equal(chunks, rhs.chunks);
    }

    /**
     * Create a new BitArray containing the provided number of bits.
     * @param bitCount The number of bits the created BitArray will contain.
     * @return A BitArray with the provided number of bits.
     * @preCondition 0 <= bitCount <= BitArray.maximumBitCount
     * @postCondition result != null
     * @postCondition result.getBitCount() == bitCount
     */
    public static BitArray fromBitCount(long bitCount)
    {
        PreCondition.assertBetween(0, bitCount, maximumBitCount, "bitCount");

        return new BitArray(bitCount);
    }

    /**
     * Convert the provided bit String into a BitArray.
     * @param bits The String of bits to convert to a BitArray.
     * @return The converted BitArray.
     * @preCondition bits != null && bits.length() > 0 && bits contains only '0','1'
     * @postCondition result != null && result.getBitCount() == bits.length()
     */
    public static BitArray fromBitString(String bits)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertContainsOnly(bits, new char[] { '0', '1' }, "bits");

        final BitArray result = BitArray.fromBitCount(bits.length());
        for (int i = 0; i < result.getBitCount(); ++i)
        {
            result.setBit(i, bits.charAt(i) - '0');
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bits.length(), result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public static BitArray fromHexString(String hexString)
    {
        PreCondition.assertNotNull(hexString, "hexString");

        final BitArray result = BitArray.fromBitCount(hexString.length() * 4);
        for (int i = 0; i < hexString.length(); ++i)
        {
            final char c = hexString.charAt(i);
            final int cInt = Integers.fromHexChar(c);
            result.copyFrom(cInt, i * 4, 4);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(hexString.length() * 4, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public static BitArray fromByteArray(byte[] bits)
    {
        PreCondition.assertNotNull(bits, "bits");

        final BitArray result = BitArray.fromByteArray(bits, bits.length * Byte.SIZE);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bits.length * Byte.SIZE, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public static BitArray fromByteArray(byte[] bits, long bitCount)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertBetween(bits.length == 0 ? 0 : ((bits.length - 1) * Byte.SIZE) + 1, bitCount, bits.length == 0 ? 0 : bits.length * Byte.SIZE, "bitCount");
        PreCondition.assertLessThanOrEqualTo(bitCount, bits.length * Byte.SIZE, "bitCount");

        final int[] resultBits = new int[(int)Math.ceiling((double)bitCount / (double)Integer.SIZE)];
        for (int byteIndex = 0; byteIndex < bits.length; ++byteIndex)
        {
            final int leftShift = (3 - (byteIndex % 4)) * Byte.SIZE;
            final byte currentByte = bits[byteIndex];
            final int shiftedByte = Bytes.toUnsignedInt(currentByte) << leftShift;

            final int intIndex = byteIndex / 4;
            resultBits[intIndex] |= shiftedByte;
        }

        final BitArray result = new BitArray(resultBits, bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitCount, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public static BitArray fromIntArray(int[] bits, long bitCount)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertBetween(bits.length == 0 ? 0 : ((bits.length - 1) * Integer.SIZE) + 1, bitCount, bits.length == 0 ? 0 : bits.length * Integer.SIZE, "bitCount");

        final BitArray result = new BitArray(Array.clone(bits), bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitCount, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    private static int bitIndexToChunkIndex(long bitIndex)
    {
        return (int)(bitIndex / bitsPerChunk);
    }

    private static int bitIndexToChunkBitOffset(long bitIndex)
    {
        return (int)(bitIndex % bitsPerChunk);
    }
}
