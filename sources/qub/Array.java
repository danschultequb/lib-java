package qub;

/**
 * A basic Array data structure that holds a fixed number of elements.
 * @param <T> The type of element contained by this Array.
 */
public abstract class Array<T> implements MutableIndexable<T>
{
    @Override
    public boolean any()
    {
        return 1 <= getCount();
    }

    @Override
    public ArrayIterator<T> iterate()
    {
        return ArrayIterator.create(this);
    }

    public ArrayIterator<T> iterateReverse()
    {
        return ArrayIterator.createReverse(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    /**
     * Create a new array with the provided number of elements.
     * @param length The number of elements.
     * @return The new array.
     */
    static <U> Array<U> createWithLength(int length)
    {
        return new ObjectArray<>(length);
    }

    /**
     * Create a new BitArray with the provided length.
     * @param length The length of the BitArray.
     * @return The new BooleanArray.
     */
    static BitArray createBitArray(long length)
    {
        PreCondition.assertBetween(0, length, BitArray.maximumBitCount, "length");

        return new BitArray(length);
    }

    /**
     * Create a new BitArray from the provided bit values.
     * @param values The bit values.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromBits(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        final BitArray result = Array.createBitArray(values.length);
        for (int i = 0; i < values.length; ++i)
        {
            PreCondition.assertBit(values[i], "The " + i + " bit");
            result.set(i, values[i]);
        }

        return result;
    }

    /**
     * Create a BitArray from the provided values.
     * @param values The values to create the BitArray from.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromBytes(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return BitArray.createFromBytes(values);
    }

    /**
     * Create a BitArray from the provided values.
     * @param values The values to create the BitArray from.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromBytes(byte[] values, long bitCount)
    {
        PreCondition.assertNotNull(values, "values");

        return BitArray.createFromBytes(values, bitCount);
    }

    /**
     * Create a BitArray from the provided values.
     * @param values The values to create the BitArray from.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromBytes(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.createBitArrayFromBytes(Array.toByteArray(values));
    }

    /**
     * Create a BitArray from the provided values.
     * @param values The values to create the BitArray from.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromIntegers(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        return BitArray.createFromIntegers(values);
    }

    /**
     * Create a BitArray from the provided bit string.
     * @param bitString The bit string.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromBitString(String bitString)
    {
        return BitArray.createFromBitString(bitString);
    }

    /**
     * Create a BitArray from the provided hexadecimal string.
     * @param hexString The hexadecimal string.
     * @return The new BitArray.
     */
    static BitArray createBitArrayFromHexString(String hexString)
    {
        return BitArray.createFromHexString(hexString);
    }

    /**
     * Create a new BooleanArray with the provided length.
     * @param length The length of the BooleanArray.
     * @return The new BooleanArray.
     */
    static BooleanArray createBooleanArray(int length)
    {
        PreCondition.assertGreaterThanOrEqualTo(length, 0, "length");

        return new BooleanArray(length);
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    static BooleanArray createBooleanArray(boolean... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new BooleanArray(values);
    }

    /**
     * Create a new boolean[] from the provided values.
     * @param values The values to create a boolean[] from.
     * @return The boolean[].
     */
    static Result<boolean[]> toBooleanArray(Iterator<Boolean> values)
    {
        PreCondition.assertNotNull(values, "values");

        return toBooleanArray(values.toList());
    }

    static Result<boolean[]> toBooleanArray(Iterable<Boolean> values)
    {
        PreCondition.assertNotNull(values, "values");

        Result<boolean[]> result = null;

        final boolean[] array = new boolean[values.getCount()];
        int index = 0;
        for (final Boolean value : values)
        {
            if (value == null)
            {
                result = Result.error(new NullPointerException("The " + index + " element cannot be null."));
                break;
            }
            else
            {
                array[index++] = value;
            }
        }

        if (result == null)
        {
            result = Result.success(array);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @return The new ByteArray.
     */
    static ByteArray createByte(byte... values)
    {
        return new ByteArray(values);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @param startIndex The start index into the values.
     * @param length The number of bytes to copy.
     * @return The new ByteArray.
     */
    static ByteArray createByte(byte[] values, int startIndex, int length)
    {
        return new ByteArray(values, startIndex, length);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @return The new ByteArray.
     */
    static ByteArray createByte(int... values)
    {
        return new ByteArray(values);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @param startIndex The start index into the values.
     * @param length The number of bytes to copy.
     * @return The new ByteArray.
     */
    static ByteArray createByte(int[] values, int startIndex, int length)
    {
        return new ByteArray(values, startIndex, length);
    }

    /**
     * Create a new byte[] from the provided values.
     * @param values The values to create a byte[] from.
     * @return The byte[].
     */
    static Result<byte[]> toByteArray(Iterator<Byte> values)
    {
        PreCondition.assertNotNull(values, "values");

        return toByteArray(values.toList());
    }

    /**
     * Convert the provided byte Iterable into a byte array.
     * @param values The byte Iterable to convert to a byte array.
     * @return The byte array.
     */
    public static Result<byte[]> toByteArray(Iterable<Byte> values)
    {
        PreCondition.assertNotNull(values, "values");

        Result<byte[]> result = null;

        if (values instanceof ByteArray)
        {
            result = Result.success(((ByteArray)values).toByteArray());
        }
        else if (values instanceof ByteList)
        {
            result = Result.success(((ByteList)values).toByteArray());
        }
        else
        {
            final byte[] array = new byte[values.getCount()];
            int index = 0;
            for (final Byte value : values)
            {
                if (value == null)
                {
                    result = Result.error(new NullPointerException("The " + index + " element cannot be null."));
                    break;
                }
                else
                {
                    array[index++] = value;
                }
            }

            if (result == null)
            {
                result = Result.success(array);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Create a new byte[] from the provided integer values.
     * @param values The integer values.
     * @return The new byte[].
     */
    static byte[] toByteArray(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; ++i)
        {
            PreCondition.assertByte(values[i], "The " + i + " value");
            result[i] = (byte)values[i];
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(values.length, result.length, "result.length");

        return result;
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    static CharacterArray createCharacter(char... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new CharacterArray(values);
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    static IntegerArray createInteger(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new IntegerArray(values);
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    @SafeVarargs
    public static <T> Array<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new ObjectArray<>(values);
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return create(List.create(values));
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> create(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        final Array<T> result = Array.createWithLength(values.getCount());
        if (result.getCount() > 0)
        {
            int i = 0;
            for (final T value : values)
            {
                result.set(i++, value);
            }
        }
        return result;
    }

    /**
     * Convert the provided character Iterator into a char array.
     * @param values The character Iterator to convert to a char array.
     * @return The char array.
     */
    public static char[] toCharArray(Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        char[] result;
        if (!values.any())
        {
            result = new char[0];
        }
        else
        {
            final ArrayList<Character> list = new ArrayList<>();
            for (final Character value : values)
            {
                list.add(value);
            }

            result = toCharArray(list);
        }
        return result;
    }

    /**
     * Convert the provided character Iterable into a char array.
     * @param values The character Iterable to convert to a char array.
     * @return The char array.
     */
    public static char[] toCharArray(Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        char[] result;
        if (!values.any())
        {
            result = new char[0];
        }
        else
        {
            result = new char[values.getCount()];
            int index = 0;
            for (final Character value : values)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Convert the provided int Iterator into a int array.
     * @param values The int Iterator to convert to an int array.
     * @return The int array.
     */
    public static int[] toIntArray(Iterator<Integer> values)
    {
        PreCondition.assertNotNull(values, "values");

        int[] result;
        if (!values.any())
        {
            result = new int[0];
        }
        else
        {
            final ArrayList<Integer> list = new ArrayList<>();
            for (final Integer value : values)
            {
                list.add(value);
            }

            result = toIntArray(list);
        }
        return result;
    }

    /**
     * Convert the provided int Iterable into a int array.
     * @param values The int Iterable to convert to a int array.
     * @return The int array.
     */
    public static int[] toIntArray(Iterable<Integer> values)
    {
        PreCondition.assertNotNull(values, "values");

        int[] result;
        if (!values.any())
        {
            result = new int[0];
        }
        else
        {
            result = new int[values.getCount()];
            int index = 0;
            for (final Integer value : values)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Convert the provided String Iterator into a String array.
     * @param values The String Iterator to convert to a String array.
     * @return The String array.
     */
    public static String[] toStringArray(Iterator<String> values)
    {
        PreCondition.assertNotNull(values, "values");

        String[] result;
        if (!values.any())
        {
            result = new String[0];
        }
        else
        {
            final ArrayList<String> list = new ArrayList<>();
            for (final String value : values)
            {
                list.add(value);
            }

            result = toStringArray(list);
        }
        return result;
    }

    /**
     * Convert the provided String Iterable into a String array.
     * @param values The String Iterable to convert to a String array.
     * @return The String array.
     */
    public static String[] toStringArray(Iterable<String> values)
    {
        PreCondition.assertNotNull(values, "values");

        String[] result;
        if (!values.any())
        {
            result = new String[0];
        }
        else
        {
            result = new String[values.getCount()];
            int index = 0;
            for (final String value : values)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Get a new byte[] that is a clone of the provided toClone byte[].
     * @param toClone The byte[] to clone.
     * @return The cloned byte[].
     */
    public static byte[] clone(byte[] toClone)
    {
        return clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new byte[] that is a clone of the provided toClone byte[].
     * @param toClone The byte[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of bytes create toClone to clone.
     * @return The cloned byte[].
     */
    public static byte[] clone(byte[] toClone, int startIndex, int length)
    {
        byte[] result;

        if (toClone == null || length < 0 || startIndex < 0 || toClone.length < startIndex)
        {
            result = null;
        }
        else if (toClone.length == 0)
        {
            result = toClone;
        }
        else
        {
            final int resultLength = Math.minimum(toClone.length - startIndex, length);
            result = new byte[resultLength];
            java.lang.System.arraycopy(toClone, startIndex, result, 0, resultLength);
        }

        return result;
    }

    /**
     * Get a new char[] that is a clone of the provided toClone char[].
     * @param toClone The char[] to clone.
     * @return The cloned char[].
     */
    public static char[] clone(char[] toClone)
    {
        return clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new char[] that is a clone of the provided toClone char[].
     * @param toClone The char[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of characters create toClone to clone.
     * @return The cloned char[].
     */
    public static char[] clone(char[] toClone, int startIndex, int length)
    {
        char[] result;

        if (toClone == null || length < 0 || startIndex < 0 || toClone.length < startIndex)
        {
            result = null;
        }
        else if (toClone.length == 0)
        {
            result = toClone;
        }
        else
        {
            final int resultLength = Math.minimum(toClone.length - startIndex, length);
            result = new char[resultLength];
            System.arraycopy(toClone, startIndex, result, 0, resultLength);
        }

        return result;
    }

    /**
     * Get a new int[] that is a clone of the provided toClone int[].
     * @param toClone The int[] to clone.
     * @return The cloned int[].
     */
    public static int[] clone(int[] toClone)
    {
        return clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new int[] that is a clone of the provided toClone int[].
     * @param toClone The int[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of characters create toClone to clone.
     * @return The cloned int[].
     */
    public static int[] clone(int[] toClone, int startIndex, int length)
    {
        int[] result;

        if (toClone == null || length < 0 || startIndex < 0 || toClone.length < startIndex)
        {
            result = null;
        }
        else if (toClone.length == 0)
        {
            result = toClone;
        }
        else
        {
            final int resultLength = Math.minimum(toClone.length - startIndex, length);
            result = new int[resultLength];
            System.arraycopy(toClone, startIndex, result, 0, resultLength);
        }

        return result;
    }

    /**
     * Copy the contents of the copyFrom byte[] to the copyTo byte[] starting at the
     * copyToStartIndex.
     * @param copyFrom The byte[] to copy create.
     * @param copyTo The byte[] to copy to.
     * @param copyToStartIndex The index within copyTo to start copying to.
     * @param length The number of bytes to copy create the copyFrom byte[] to the copyTo byte[].
     */
    public static void copy(byte[] copyFrom, int copyFromStartIndex, byte[] copyTo, int copyToStartIndex, int length)
    {
        PreCondition.assertNotNull(copyFrom, "copyFrom");
        PreCondition.assertBetween(0, copyFromStartIndex, Math.maximum(0, copyFrom.length - 1), "copyFromStartIndex");
        PreCondition.assertNotNull(copyTo, "copyTo");
        PreCondition.assertBetween(0, copyToStartIndex, Math.maximum(0, copyTo.length - 1), "copyToStartIndex");
        PreCondition.assertBetween(0, length, Math.minimum(copyFrom.length - copyFromStartIndex, copyTo.length - copyToStartIndex), "length");

        if (length > 0)
        {
            System.arraycopy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);
        }
    }

    /**
     * Copy the contents of the copyFrom char[] to the copyTo char[] starting at the
     * copyToStartIndex.
     * @param copyFrom The char[] to copy create.
     * @param copyTo The char[] to copy to.
     * @param copyToStartIndex The index within copyTo to start copying to.
     * @param length The number of bytes to copy create the copyFrom byte[] to the copyTo byte[].
     */
    public static void copy(char[] copyFrom, int copyFromStartIndex, char[] copyTo, int copyToStartIndex, int length)
    {
        if (copyFrom != null && copyTo != null && 1 <= copyFrom.length && 1 <= copyTo.length &&
                0 <= copyFromStartIndex && copyFromStartIndex < copyFrom.length &&
                0 <= copyToStartIndex && copyToStartIndex < copyTo.length &&
                1 <= length)
        {
            length = Math.minimum(copyTo.length - copyToStartIndex, Math.minimum(copyFrom.length, length));
            System.arraycopy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);
        }
    }

    /**
     * Merge the provided byte arrays into a single byte array and return the merged byte array.
     * @param byteArrays The byte arrays to mergeBytes.
     * @return The merged byte array.
     */
    public static byte[] mergeBytes(Iterable<byte[]> byteArrays)
    {
        PreCondition.assertNotNull(byteArrays, "byteArrays");

        int totalByteCount = 0;
        for (final byte[] byteArray : byteArrays)
        {
            if (byteArray != null)
            {
                totalByteCount += byteArray.length;
            }
        }

        final byte[] result = new byte[totalByteCount];
        int resultIndex = 0;
        for (final byte[] byteArray : byteArrays)
        {
            if (byteArray != null)
            {
                Array.copy(byteArray, 0, result, resultIndex, byteArray.length);
                resultIndex += byteArray.length;
            }
        }

        return result;
    }

    /**
     * Merge the provided char arrays into a single char array and return the merged char array.
     * @param charArrays The char arrays to mergeBytes.
     * @return The merged char array.
     */
    public static char[] mergeCharacters(Iterable<char[]> charArrays)
    {
        char[] result;

        if (charArrays == null)
        {
            result = null;
        }
        else
        {
            int totalByteCount = 0;
            for (final char[] byteArray : charArrays)
            {
                if (byteArray != null)
                {
                    totalByteCount += byteArray.length;
                }
            }

            result = new char[totalByteCount];
            int resultIndex = 0;
            for (final char[] byteArray : charArrays)
            {
                if (byteArray != null)
                {
                    Array.copy(byteArray, 0, result, resultIndex, byteArray.length);
                    resultIndex += byteArray.length;
                }
            }
        }

        return result;
    }

    /**
     * Get the String representation of the elements within the provided byte array.
     * @param byteArray The byte array to convert to a String.
     * @return The String representation of the elements within the provided byte array.
     */
    public static String toString(byte[] byteArray)
    {
        final StringBuilder builder = new StringBuilder();

        if (byteArray == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final byte element : byteArray)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                builder.append(element);
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided char array.
     * @param array The char array to convert to a String.
     * @return The String representation of the elements within the provided char array.
     */
    public static String toString(char[] array)
    {
        return Array.toString(array, (Character c) -> "'" + c + "'");
    }

    /**
     * Get the String representation of the elements within the provided char array.
     * @param array The char array to convert to a String.
     * @return The String representation of the elements within the provided char array.
     */
    public static String toString(char[] array, Function1<Character,String> characterTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final char element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (characterTransform != null)
                {
                    builder.append(characterTransform.run(element));
                }
                else
                {
                    builder.append(element);
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided short array.
     * @param array The short array to convert to a String.
     * @return The String representation of the elements within the provided short array.
     */
    public static String toString(short[] array)
    {
        return Array.toString(array, null);
    }

    /**
     * Get the String representation of the elements within the provided short array.
     * @param array The short array to convert to a String.
     * @return The String representation of the elements within the provided short array.
     */
    public static String toString(short[] array, Function1<Short,String> valueTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final short element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (valueTransform != null)
                {
                    builder.append(valueTransform.run(element));
                }
                else
                {
                    builder.append(element);
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided int array.
     * @param array The int array to convert to a String.
     * @return The String representation of the elements within the provided int array.
     */
    public static String toString(int[] array)
    {
        return Array.toString(array, null);
    }

    /**
     * Get the String representation of the elements within the provided int array.
     * @param array The int array to convert to a String.
     * @return The String representation of the elements within the provided int array.
     */
    public static String toString(int[] array, Function1<Integer,String> valueTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final int element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (valueTransform != null)
                {
                    builder.append(valueTransform.run(element));
                }
                else
                {
                    builder.append(element);
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided long array.
     * @param array The long array to convert to a String.
     * @return The String representation of the elements within the provided long array.
     */
    public static String toString(long[] array)
    {
        return Array.toString(array, null);
    }

    /**
     * Get the String representation of the elements within the provided long array.
     * @param array The long array to convert to a String.
     * @return The String representation of the elements within the provided long array.
     */
    public static String toString(long[] array, Function1<Long,String> valueTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final long element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (valueTransform != null)
                {
                    builder.append(valueTransform.run(element));
                }
                else
                {
                    builder.append(element);
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided float array.
     * @param array The float array to convert to a String.
     * @return The String representation of the elements within the provided float array.
     */
    public static String toString(float[] array)
    {
        return Array.toString(array, null);
    }

    /**
     * Get the String representation of the elements within the provided float array.
     * @param array The float array to convert to a String.
     * @return The String representation of the elements within the provided float array.
     */
    public static String toString(float[] array, Function1<Float,String> valueTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final float element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (valueTransform != null)
                {
                    builder.append(valueTransform.run(element));
                }
                else
                {
                    builder.append(element);
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided double array.
     * @param array The double array to convert to a String.
     * @return The String representation of the elements within the provided double array.
     */
    public static String toString(double[] array)
    {
        return Array.toString(array, null);
    }

    /**
     * Get the String representation of the elements within the provided double array.
     * @param array The double array to convert to a String.
     * @return The String representation of the elements within the provided double array.
     */
    public static String toString(double[] array, Function1<Double,String> valueTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final double element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (valueTransform != null)
                {
                    builder.append(valueTransform.run(element));
                }
                else
                {
                    builder.append(element);
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the String representation of the elements within the provided T array.
     * @param array The T array to convert to a String.
     * @return The String representation of the elements within the provided T array.
     */
    public static <T> String toString(T[] array)
    {
        return Array.toString(array, null);
    }

    /**
     * Get the String representation of the elements within the provided T array.
     * @param array The T array to convert to a String.
     * @return The String representation of the elements within the provided T array.
     */
    public static <T> String toString(T[] array, Function1<T,String> valueTransform)
    {
        final StringBuilder builder = new StringBuilder();

        if (array == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedFirstElement = false;
            for (final T element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.append(',');
                }
                if (valueTransform != null)
                {
                    builder.append(valueTransform.run(element));
                }
                else
                {
                    builder.append(Objects.toString(element));
                }
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * Get the first index of the provided character in the array of characters. If the character
     * is not found, then -1 will be returned.
     * @param characters The array of characters to look through.
     * @param value The character to look for.
     * @return The first index of the provided character in the array of characters, or -1 if the
     * character is not found.
     */
    public static int indexOf(char[] characters, char value)
    {
        PreCondition.assertNotNull(characters, "characters");

        final int result = Array.indexOf(characters, 0, characters.length, value);

        PostCondition.assertBetween(-1, result, characters.length - 1, "result");

        return result;
    }

    /**
     * Get the first index of the provided character in the array of characters. If the character
     * is not found, then -1 will be returned.
     * @param characters The array of characters to look through.
     * @param value The character to look for.
     * @return The first index of the provided character in the array of characters, or -1 if the
     * character is not found.
     */
    public static int indexOf(char[] characters, int startIndex, int length, char value)
    {
        PreCondition.assertNotNull(characters, "characters");

        int result = -1;
        for (int i = 0; i < length; ++i)
        {
            if (characters[startIndex + i] == value)
            {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Get whether or not the provided array contains the provided value.
     * @param array The array to check.
     * @param value The value to look for.
     * @return Whether or not the provided array contains the provided value.
     */
    public static boolean contains(char[] array, char value)
    {
        boolean result = false;
        if (array != null)
        {
            for (int i = 0; i < array.length; ++i)
            {
                if (array[i] == value)
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get whether or not the provided array contains the provided value.
     * @param array The array to check.
     * @param value The value to look for.
     * @return Whether or not the provided array contains the provided value.
     */
    public static boolean contains(int[] array, int value)
    {
        boolean result = false;
        if (array != null)
        {
            for (int i = 0; i < array.length; ++i)
            {
                if (array[i] == value)
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get whether or not the provided array contains the provided value.
     * @param array The array to check.
     * @param value The value to look for.
     * @return Whether or not the provided array contains the provided value.
     */
    public static boolean contains(long[] array, long value)
    {
        boolean result = false;
        if (array != null)
        {
            for (int i = 0; i < array.length; ++i)
            {
                if (array[i] == value)
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get whether or not the provided array contains the provided value.
     * @param array The array to check.
     * @param value The value to look for.
     * @return Whether or not the provided array contains the provided value.
     */
    public static <T> boolean contains(T[] array, T value)
    {
        boolean result = false;
        if (array != null)
        {
            for (int i = 0; i < array.length; ++i)
            {
                if (Comparer.equal(array[i], value))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Starting at the provided indexToRemove, shift the values in the provided array one position
     * to the left. The result is that the value at the indexToRemove will be "removed".
     * @param values The values.
     * @param indexToRemove The index to "remove" create the array.
     * @param valuesToShift The number of values to shift.
     */
    public static void shiftLeft(byte[] values, int indexToRemove, int valuesToShift)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertBetween(0, indexToRemove, values.length - 2, "indexToRemove");
        PreCondition.assertBetween(0, valuesToShift, values.length - indexToRemove - 1, "valuesToShift");

        for (int i = indexToRemove; i < indexToRemove + valuesToShift; ++i)
        {
            values[i] = values[i + 1];
        }
    }

    /**
     * Starting at the provided indexToRemove, shift the values in the provided array one position
     * to the right. The result is that a new position will be opened up the array.
     * @param values The values.
     * @param indexToOpen The index to open up in the array.
     * @param valuesToShift The number of values to shift.
     */
    public static void shiftRight(byte[] values, int indexToOpen, int valuesToShift)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertBetween(0, indexToOpen, values.length - 2, "indexToOpen");
        PreCondition.assertBetween(0, valuesToShift, values.length - indexToOpen - 1, "valuesToShift");

        for (int i = indexToOpen + valuesToShift; indexToOpen < i; --i)
        {
            values[i] = values[i - 1];
        }
    }
}
