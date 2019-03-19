package qub;

/**
 * A contiguous array of bits.
 */
public class BitArray extends Array<Integer>
{
    public static final long maximumBitCount = ((long)Integers.maximum) * Integers.bitCount;

    private final int[] bitChunks;
    private final long bitCount;

    /**
     * Create a new BitArray containing the provided number of bits.
     * @param bitCount The number of bits the created BitArray will contain.
     */
    public BitArray(long bitCount)
    {
        this(createBitChunks(bitCount), bitCount);
    }

    public BitArray(int[] bitChunks, long bitCount)
    {
        PreCondition.assertNotNull(bitChunks, "bitChunks");
        PreCondition.assertGreaterThanOrEqualTo(bitCount, 0, "bitCount");

        this.bitCount = bitCount;
        this.bitChunks = bitChunks;
    }

    private static int[] createBitChunks(long bitCount)
    {
        PreCondition.assertBetween(0, bitCount, maximumBitCount, "bitCount");

        return new int[getBitChunkCount(bitCount)];
    }

    private static int[] createBitChunksFromBytes(byte[] bits, long bitCount)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertBetween(bits.length == 0 ? 0 : ((bits.length - 1) * Bytes.bitCount) + 1, bitCount, bits.length == 0 ? 0 : bits.length * Bytes.bitCount, "bitCount");
        PreCondition.assertLessThanOrEqualTo(bitCount, bits.length * Bytes.bitCount, "bitCount");

        final int[] result = new int[getBitChunkCount(bitCount)];
        for (int byteIndex = 0; byteIndex < bits.length; ++byteIndex)
        {
            final int leftShift = (Integers.byteCount - 1 - (byteIndex % Integers.byteCount)) * Bytes.bitCount;
            final byte currentByte = bits[byteIndex];
            final int shiftedByte = Bytes.toUnsignedInt(currentByte) << leftShift;

            final int intIndex = byteIndex / Integers.byteCount;
            result[intIndex] |= shiftedByte;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public static int getBitChunkCount(long bitCount)
    {
        PreCondition.assertBetween(0, bitCount, maximumBitCount, "bitCount");

        return (int)Math.ceiling((double)bitCount / (double)Integers.bitCount);
    }

    public static BitArray createFromBytes(byte[] bits)
    {
        PreCondition.assertNotNull(bits, "bits");

        final BitArray result = BitArray.createFromBytes(bits, bits.length * Bytes.bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bits.length * Bytes.bitCount, result.getCount(), "result.getCount()");

        return result;
    }

    public static BitArray createFromBytes(byte[] bits, long bitCount)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertBetween(bits.length == 0 ? 0 : ((bits.length - 1) * Bytes.bitCount) + 1, bitCount, bits.length == 0 ? 0 : bits.length * Bytes.bitCount, "bitCount");
        PreCondition.assertLessThanOrEqualTo(bitCount, bits.length * Bytes.bitCount, "bitCount");

        final int[] bitChunks = BitArray.createBitChunksFromBytes(bits, bitCount);
        final BitArray result = new BitArray(bitChunks, bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitCount, result.getCount(), "result.getCount()");

        return result;
    }

    public static BitArray createFromIntegers(int... bits)
    {
        PreCondition.assertNotNull(bits, "bits");

        final BitArray result = new BitArray(bits, bits.length * Integers.bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bits.length * Integers.bitCount, result.getCount(), "result.getCount()");

        return result;
    }

    public static BitArray createFromIntegers(int[] bits, long bitCount)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertBetween(bits.length == 0 ? 0 : ((bits.length - 1) * Integers.bitCount) + 1, bitCount, bits.length == 0 ? 0 : bits.length * Integers.bitCount, "bitCount");

        final BitArray result = new BitArray(Array.clone(bits), bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitCount, result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Convert the provided bit String into a BitArray.
     * @param bits The String of bits to convert to a BitArray.
     * @return The converted BitArray.
     * @preCondition bits != null && bits.length() > 0 && bits contains only '0','1'
     * @postCondition result != null && result.getCount() == bits.length()
     */
    public static BitArray createFromBitString(String bits)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertContainsOnly(bits, new char[] { '0', '1' }, "bits");

        final BitArray result = new BitArray(bits.length());
        for (int i = 0; i < result.getCount(); ++i)
        {
            result.set(i, bits.charAt(i) - '0');
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bits.length(), result.getCount(), "result.getCount()");

        return result;
    }

    public static BitArray createFromHexString(String hexString)
    {
        PreCondition.assertNotNull(hexString, "hexString");

        final BitArray result = new BitArray(hexString.length() * 4);
        for (int i = 0; i < hexString.length(); ++i)
        {
            final char c = hexString.charAt(i);
            final int cInt = Integers.fromHexChar(c);
            result.copyFrom(cInt, i * 4, 4);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(hexString.length() * 4, result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Clone this BitArray.
     * @return A clone of this BitArray.
     */
    public BitArray clone()
    {
        return BitArray.createFromIntegers(bitChunks, bitCount);
    }

    /**
     * The number of bits contained in this BitArray.
     * @return The number of bits contained in this BitArray.
     */
    public int getCount()
    {
        PreCondition.assertBetween(0, bitCount, Integers.maximum, "getCount()");

        return (int)bitCount;
    }

    /**
     * The number of bits contained in this BitArray.
     * @return The number of bits contained in this BitArray.
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
    public int getBitChunkCount()
    {
        return bitChunks.length;
    }

    /**
     * Get the bit at the provided bitIndex. Bit indexes start with the most significant (left-most)
     * bit at 0 and go to the least significant (right-most) bit at bitCount - 1.
     * @param index The index of the bit to get.
     * @return The bit's value at the provided bitIndex.
     * @preCondition 0 <= bitIndex <= getCount() - 1
     * @postCondition result == 0 || result == 1
     */
    @Override
    public Integer get(int index)
    {
        return getBit(index);
    }

    /**
     * Get the bit at the provided bitIndex. Bit indexes start with the most significant (left-most)
     * bit at 0 and go to the least significant (right-most) bit at bitCount - 1.
     * @param index The index of the bit to get.
     * @return The bit's value at the provided bitIndex.
     * @preCondition 0 <= bitIndex <= getCount() - 1
     * @postCondition result == 0 || result == 1
     */
    public int getBit(long index)
    {
        PreCondition.assertIndexAccess(index, getBitCount());

        final int bitChunk = bitChunks[bitIndexToChunkIndex(index)];
        final int chunkBitOffset = bitIndexToChunkBitOffset(index);
        return Integers.getBit(bitChunk, chunkBitOffset);
    }

    /**
     * Set the bit at the provided bitIndex to the provided value. Bit indexes start with the most
     * significant (left-most) bit at 0 and go to the least significant (right-most) bit at
     * bitCount - 1.
     * @param index The index of the bit to get.
     * @param value The value to set. This can be either 0 or 1.
     * @preCondition 0 <= bitIndex <= getCount() - 1
     * @preCondition value == 0 || value == 1
     */
    @Override
    public void set(int index, Integer value)
    {
        PreCondition.assertIndexAccess(index, getBitCount());
        PreCondition.assertNotNull(value, "value");

        setBit(index, value);
    }

    /**
     * Set the bit at the provided bitIndex to the provided value. Bit indexes start with the most
     * significant (left-most) bit at 0 and go to the least significant (right-most) bit at
     * bitCount - 1.
     * @param index The index of the bit to get.
     * @param value The value to set. This can be either 0 or 1.
     * @preCondition 0 <= bitIndex <= getCount() - 1
     * @preCondition value == 0 || value == 1
     */
    public void set(int index, int value)
    {
        PreCondition.assertIndexAccess(index, getBitCount());
        PreCondition.assertOneOf(value, new int[] { 0, 1 }, "value");

        setBit(index, value);
    }

    /**
     * Set the bit at the provided bitIndex to the provided value. Bit indexes start with the most
     * significant (left-most) bit at 0 and go to the least significant (right-most) bit at
     * bitCount - 1.
     * @param index The index of the bit to get.
     * @param value The value to set. This can be either 0 or 1.
     * @preCondition 0 <= bitIndex <= getCount() - 1
     * @preCondition value == 0 || value == 1
     */
    public void setBit(long index, int value)
    {
        PreCondition.assertIndexAccess(index, getBitCount());
        PreCondition.assertOneOf(value, new int[] { 0, 1 }, "value");

        final int chunkIndex = bitIndexToChunkIndex(index);
        final int chunkBitOffset = bitIndexToChunkBitOffset(index);
        bitChunks[chunkIndex] = Integers.setBit(bitChunks[chunkIndex], chunkBitOffset, value);
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
        for (int i = 0; i < getBitChunkCount(); ++i)
        {
            bitChunks[i] = chunkValue;
        }
    }

    /**
     * Set the last bits of this BitArray to be the bits in the provided value.
     * @param value The long of bits to assign to the end of this BitArray.
     */
    public void setLastBitsFromLong(long value)
    {
        PreCondition.assertGreaterThanOrEqualTo(getCount(), Long.SIZE, "getCount()");

        copyFrom(value, getCount() - Long.SIZE, Long.SIZE);
    }

    /**
     * Copy the bits create the copyFrom BitArray to this BitArray.
     * @param copyFrom The BitArray to copy create.
     * @param copyFromStartIndex The index to start reading create.
     * @param copyToStartIndex The index to start writing to.
     * @param copyLength The number of bits to copy.
     */
    public void copyFrom(BitArray copyFrom, long copyFromStartIndex, long copyToStartIndex, long copyLength)
    {
        PreCondition.assertNotNull(copyFrom, "copyFrom");
        PreCondition.assertStartIndex(copyFromStartIndex, copyFrom.getCount(), "copyFromStartIndex");
        PreCondition.assertStartIndex(copyToStartIndex, this.getCount(), "copyToStartIndex");
        PreCondition.assertBetween(0, copyLength, Math.minimum(copyFrom.getCount() - copyFromStartIndex, this.getCount() - copyToStartIndex), "copyLength");

        for (long i = 0; i < copyLength; ++i)
        {
            this.setBit(copyToStartIndex + i, copyFrom.getBit(copyFromStartIndex + i));
        }
    }

    /**
     * Copy the bits create the copyFrom long to this BitArray.
     * @param copyFrom The long to copy create.
     * @param copyToStartIndex The index to start writing to.
     * @param copyLength The number of bits to copy. Bits will be copied create the least significant
     *                   side of the long.
     */
    public void copyFrom(long copyFrom, long copyToStartIndex, int copyLength)
    {
        PreCondition.assertStartIndex(copyToStartIndex, this.getCount(), "copyToStartIndex");
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
        rotateLeft(bitsToRotate, 0, getCount());
    }

    /**
     * Rotate the bits in this BitArray the provided number of spaces to the left starting at the
     * provided startIndex and going for the provided length.
     * @param bitsToRotate The number of bit spaces to rotate.
     * @preCondition bitsToRotate >= 0
     */
    public void rotateLeft(long bitsToRotate, long startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getCount());
        PreCondition.assertLength(length, startIndex, getCount());

        if (bitsToRotate % getCount() != 0)
        {
            final BitArray temp = new BitArray(length);
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
        rotateRight(bitsToRotate, 0, getCount());
    }

    /**
     * Rotate the bits in this BitArray the provided number of spaces to the right starting at the
     * provided startIndex and going for the provided length.
     * @param bitsToRotate The number of bit spaces to rotate.
     * @preCondition bitsToRotate >= 0
     */
    public void rotateRight(long bitsToRotate, long startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getCount());
        PreCondition.assertLength(length, startIndex, getCount());

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
        shiftLeft(bitsToShift, 0, getCount());
    }

    /**
     * Shift the bits in this BitArray in the provided range bitsToShift positions to the left.
     * @param bitsToShift The number of positions to shift the bits to the left.
     * @param startIndex The index to start shifting bits at.
     * @param length The number of bits to shift.
     */
    public void shiftLeft(long bitsToShift, long startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getCount());
        PreCondition.assertLength(length, startIndex, getCount());

        final BitArray tempBits = new BitArray(length);
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
     * @preCondition getCount() == rhs.getCount()
     * @postCondition result != null
     * @postCondition getCount() == result.getCount()
     */
    public BitArray xor(BitArray rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertEqual(getCount(), rhs.getCount(), "rhs.getCount()");

        final long bitCount = getCount();
        final BitArray result = new BitArray(bitCount);
        for (long i = 0; i < bitCount; ++i)
        {
            result.setBit(i, (getBit(i) + rhs.getBit(i)) % 2);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getCount(), result.getCount(), "result.getCount()");

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

        final BitArray result = new BitArray(bitIndexPermutations.length);
        for (int i = 0; i < bitIndexPermutations.length; ++i)
        {
            final long bitIndex = bitIndexPermutations[i];
            result.set(i, getBit(bitIndex));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitIndexPermutations.length, result.getCount(), "result.getCount()");

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
        return permuteByBitNumber(0, getCount(), bitNumberPermutations);
    }

    public BitArray permuteByBitNumber(long startIndex, long length, long[] bitNumberPermutations)
    {
        PreCondition.assertStartIndex(startIndex, getCount());
        PreCondition.assertLength(length, startIndex, getCount());
        PreCondition.assertNotNull(bitNumberPermutations, "bitNumberPermutations");

        final BitArray result = new BitArray(bitNumberPermutations.length);
        for (int i = 0; i < bitNumberPermutations.length; ++i)
        {
            final long bitNumber = bitNumberPermutations[i];
            final long bitIndex = bitNumber - 1;
            result.set(i, getBit(startIndex + bitIndex));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bitNumberPermutations.length, result.getCount(), "result.getCount()");

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

        final long thisBitCount = this.getCount();
        final long rhsBitCount = rhs.getCount();
        final BitArray result = new BitArray(thisBitCount + rhsBitCount);
        if (thisBitCount > 0)
        {
            result.copyFrom(this, 0, 0, thisBitCount);
        }
        if (rhsBitCount > 0)
        {
            result.copyFrom(rhs, 0, thisBitCount, rhsBitCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getCount() + rhs.getCount(), result.getCount(), "result.getCount()");

        return result;
    }

    public ArrayIterator<Integer> iterate()
    {
        final ArrayIterator<Integer> result = ArrayIterator.create(this);

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
        PreCondition.assertEqual(0, getCount() % blockBitCount, "getCount() % blockBitCount");

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
        PreCondition.assertEqual(0, getCount() % Integers.bitCount, "getCount() % Integers.bitCount");

        final Iterator<Integer> result = bitChunks.length == 0 ? Iterator.empty() : Iterator.create(bitChunks);

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
        return toInteger(0, getCount());
    }

    /**
     * Get the integer representation of the subsection of this BitArray.
     * @param startIndex The start index of the subsection.
     * @param length The number of bits in the subsection.
     * @return The integer representation of this BitArray.
     */
    public int toInteger(int startIndex, long length)
    {
        PreCondition.assertStartIndex(startIndex, getCount());
        PreCondition.assertBetween(1, length, Math.minimum(getCount() - startIndex, Integers.bitCount), "length");

        int mask = 1;
        for (int i = 1; i < length; ++i)
        {
            mask = (mask << 1) | 0x1;
        }

        final int resultBeforeMask = this.bitChunks[0] >>> (Integers.bitCount - (startIndex + length));
        final int result = resultBeforeMask & mask;

        return result;
    }

    /**
     * Get a byte[] that contains this BitArray's bits.
     * @return The byte[] that contains this BitArray's bits.
     */
    public byte[] toByteArray()
    {
        PreCondition.assertBetween(0, getBitCount(), (long)Integers.maximum * Bytes.bitCount, "getBitCount()");

        final int byteCount = (int)Math.ceiling((double)bitCount / Bytes.bitCount);
        final byte[] result = new byte[byteCount];
        for (int byteIndex = 0; byteIndex < byteCount; ++byteIndex)
        {
            final int chunkBitShift = ((Integers.byteCount - 1 - (byteIndex % Integers.byteCount))) * Bytes.bitCount;
            result[byteIndex] = (byte)((bitChunks[byteIndex / Integers.byteCount] >>> chunkBitShift) & 0xFF);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the binary (bit) representation of this BitArray.
     * @return The binary (bit) representation of this BitArray.
     */
    public String toBitString()
    {
        final long bitCount = getCount();
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
        for (int chunk : bitChunks)
        {
            builder.append(Integers.toHexString(chunk, false));
        }
        final long lastChunkBitCount = bitCount % Integers.bitCount;
        if (lastChunkBitCount != 0)
        {
            final int lastChunkHexCharCount = (int)Math.ceiling((double)lastChunkBitCount / Integers.byteCount);
            final int charsToRemove = (Integers.bitCount / Integers.byteCount) - lastChunkHexCharCount;
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
           Comparer.equal(bitChunks, rhs.bitChunks);
    }

    public static int bitIndexToChunkIndex(long bitIndex)
    {
        PreCondition.assertBetween(0, bitIndex, maximumBitCount, "bitIndex");

        return (int)(bitIndex / Integers.bitCount);
    }

    public static int bitIndexToChunkBitOffset(long bitIndex)
    {
        PreCondition.assertBetween(0, bitIndex, maximumBitCount, "bitIndex");

        return (int)(bitIndex % Integers.bitCount);
    }
}
