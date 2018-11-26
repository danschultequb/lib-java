package qub;

/**
 * A basic Array data structure that holds a fixed number of elements.
 * @param <T> The type of element contained by this Array.
 */
public class Array<T> implements MutableIndexable<T>
{
    private final Object[] data;

    /**
     * Create a new Array with the provided size.
     * @param count The number of elements in the Array.
     */
    public Array(int count)
    {
        data = new Object[count];
    }

    /**
     * Get the number of elements in this Array.
     * @return The number of elements in this Array.
     */
    @Override
    public int getCount()
    {
        return data.length;
    }

    /**
     * Get the element at the provided index. If the provided index is outside of the bounds of this
     * Array, then null will be returned.
     * @param index The index of the element to return.
     * @return The element at the provided index, or null if the provided index is out of bounds.
     */
    @SuppressWarnings("unchecked")
    public T get(int index)
    {
        PreCondition.assertBetween(0, index, getCount(), "index");

        return (T)data[index];
    }

    /**
     * Set the element at the provided index. If the provided index is outside of the bounds of this
     * Array, then nothing will happen.
     * @param index The index of the element to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, T value)
    {
        PreCondition.assertBetween(0, index, getCount(), "index");

        data[index] = value;
    }

    /**
     * Set all of the elements of this Array to the provided value.
     * @param value The value to set all of the elements of this Array to.
     */
    public void setAll(T value)
    {
        java.util.Arrays.fill(data, value);
    }

    @Override
    public Iterator<T> iterate()
    {
        return new ArrayIterator<>(this);
    }

    /**
     * Get an Iterator that will iterate over the contents of this Array in reverse.
     * @return An Iterator that will iterate over the contents of this Array in reverse.
     */
    public Iterator<T> iterateReverse() { return new ArrayIterator<>(this, getCount() - 1, -1); }

    @Override
    public boolean any()
    {
        return 1 <= getCount();
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
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static Array<Boolean> fromValues(boolean[] values)
    {
        final int length = values == null ? 0 : values.length;
        final Array<Boolean> result = new Array<>(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[i]);
        }
        return result;
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static Array<Byte> fromValues(byte[] values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.fromValues(values, 0, values.length);
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static Array<Byte> fromValues(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertBetween(0, startIndex, values.length - 1, "startIndex");
        PreCondition.assertBetween(0, length, values.length - startIndex, "length");

        final Array<Byte> result = new Array<>(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[startIndex + i]);
        }
        return result;
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static Array<Character> fromValues(char[] values)
    {
        final int length = values == null ? 0 : values.length;
        final Array<Character> result = new Array<>(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[i]);
        }
        return result;
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static Array<Integer> fromValues(int[] values)
    {
        final int length = values == null ? 0 : values.length;
        final Array<Integer> result = new Array<>(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[i]);
        }
        return result;
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> fromValues(T[] values)
    {
        final int length = values == null ? 0 : values.length;
        final Array<T> result = new Array<>(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[i]);
        }
        return result;
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> fromValues(Iterator<T> values)
    {
        return fromValues(ArrayList.fromValues(values));
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> fromValues(Iterable<T> values)
    {
        final int length = values == null ? 0 : values.getCount();
        final Array<T> result = new Array<>(length);

        if (values != null && values.any())
        {
            int i = 0;
            for (final T value : values)
            {
                result.set(i, value);
                ++i;
            }
        }
        return result;
    }

    /**
     * Convert the provided boolean Iterator into a boolean array.
     * @param booleans The boolean Iterator to convert to a boolean array.
     * @return The boolean array.
     */
    public static boolean[] toBooleanArray(Iterator<Boolean> booleans)
    {
        boolean[] result;
        if (booleans == null)
        {
            result = null;
        }
        else if (!booleans.any())
        {
            result = new boolean[0];
        }
        else
        {
            final ArrayList<Boolean> list = new ArrayList<>();
            list.addAll(booleans);
            result = toBooleanArray(list);
        }
        return result;
    }

    /**
     * Convert the provided boolean Iterable into a boolean array.
     * @param booleans The boolean Iterable to convert to a boolean array.
     * @return The boolean array.
     */
    public static boolean[] toBooleanArray(Iterable<Boolean> booleans)
    {
        boolean[] result;
        if (booleans == null)
        {
            result = null;
        }
        else if (!booleans.any())
        {
            result = new boolean[0];
        }
        else
        {
            result = new boolean[booleans.getCount()];
            int index = 0;
            for (final Boolean value : booleans)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Convert the provided byte Iterator into a byte array.
     * @param bytes The byte Iterator to convert to a byte array.
     * @return The byte array.
     */
    public static byte[] toByteArray(Iterator<Byte> bytes)
    {
        byte[] result;
        if (bytes == null)
        {
            result = null;
        }
        else if (!bytes.any())
        {
            result = new byte[0];
        }
        else
        {
            final ArrayList<Byte> list = new ArrayList<>();
            for (final Byte value : bytes)
            {
                list.add(value);
            }

            result = toByteArray(list);
        }
        return result;
    }

    /**
     * Convert the provided byte Iterable into a byte array.
     * @param bytes The byte Iterable to convert to a byte array.
     * @return The byte array.
     */
    public static byte[] toByteArray(Iterable<Byte> bytes)
    {
        byte[] result;
        if (bytes == null)
        {
            result = null;
        }
        else if (!bytes.any())
        {
            result = new byte[0];
        }
        else
        {
            result = new byte[bytes.getCount()];
            int index = 0;
            for (final Byte value : bytes)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Convert the provided character Iterator into a char array.
     * @param characters The character Iterator to convert to a char array.
     * @return The char array.
     */
    public static char[] toCharArray(Iterator<Character> characters)
    {
        char[] result;
        if (characters == null)
        {
            result = null;
        }
        else if (!characters.any())
        {
            result = new char[0];
        }
        else
        {
            final ArrayList<Character> list = new ArrayList<>();
            for (final Character value : characters)
            {
                list.add(value);
            }

            result = toCharArray(list);
        }
        return result;
    }

    /**
     * Convert the provided character Iterable into a char array.
     * @param characters The character Iterable to convert to a char array.
     * @return The char array.
     */
    public static char[] toCharArray(Iterable<Character> characters)
    {
        char[] result;
        if (characters == null)
        {
            result = null;
        }
        else if (!characters.any())
        {
            result = new char[0];
        }
        else
        {
            result = new char[characters.getCount()];
            int index = 0;
            for (final Character value : characters)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Convert the provided int Iterator into a int array.
     * @param ints The int Iterator to convert to an int array.
     * @return The int array.
     */
    public static int[] toIntArray(Iterator<Integer> ints)
    {
        int[] result;
        if (ints == null)
        {
            result = null;
        }
        else if (!ints.any())
        {
            result = new int[0];
        }
        else
        {
            final ArrayList<Integer> list = new ArrayList<>();
            for (final Integer value : ints)
            {
                list.add(value);
            }

            result = toIntArray(list);
        }
        return result;
    }

    /**
     * Convert the provided int Iterable into a int array.
     * @param ints The int Iterable to convert to a int array.
     * @return The int array.
     */
    public static int[] toIntArray(Iterable<Integer> ints)
    {
        int[] result;
        if (ints == null)
        {
            result = null;
        }
        else if (!ints.any())
        {
            result = new int[0];
        }
        else
        {
            result = new int[ints.getCount()];
            int index = 0;
            for (final Integer value : ints)
            {
                result[index] = value;
                ++index;
            }
        }
        return result;
    }

    /**
     * Convert the provided String Iterator into a String array.
     * @param strings The String Iterator to convert to a String array.
     * @return The String array.
     */
    public static String[] toStringArray(Iterator<String> strings)
    {
        String[] result;
        if (strings == null)
        {
            result = null;
        }
        else if (!strings.any())
        {
            result = new String[0];
        }
        else
        {
            final ArrayList<String> list = new ArrayList<>();
            for (final String value : strings)
            {
                list.add(value);
            }

            result = toStringArray(list);
        }
        return result;
    }

    /**
     * Convert the provided String Iterable into a String array.
     * @param strings The String Iterable to convert to a String array.
     * @return The String array.
     */
    public static String[] toStringArray(Iterable<String> strings)
    {
        String[] result;
        if (strings == null)
        {
            result = null;
        }
        else if (!strings.any())
        {
            result = new String[0];
        }
        else
        {
            result = new String[strings.getCount()];
            int index = 0;
            for (final String value : strings)
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
     * @param startIndex The index to start cloning from.
     * @param length The number of bytes from toClone to clone.
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
            System.arraycopy(toClone, startIndex, result, 0, resultLength);
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
     * @param startIndex The index to start cloning from.
     * @param length The number of characters from toClone to clone.
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
     * @param startIndex The index to start cloning from.
     * @param length The number of characters from toClone to clone.
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
     * @param copyFrom The byte[] to copy from.
     * @param copyTo The byte[] to copy to.
     * @param copyToStartIndex The index within copyTo to start copying to.
     * @param length The number of bytes to copy from the copyFrom byte[] to the copyTo byte[].
     */
    public static void copy(byte[] copyFrom, int copyFromStartIndex, byte[] copyTo, int copyToStartIndex, int length)
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
     * Copy the contents of the copyFrom char[] to the copyTo char[] starting at the
     * copyToStartIndex.
     * @param copyFrom The char[] to copy from.
     * @param copyTo The char[] to copy to.
     * @param copyToStartIndex The index within copyTo to start copying to.
     * @param length The number of bytes to copy from the copyFrom byte[] to the copyTo byte[].
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
        byte[] result;

        if (byteArrays == null)
        {
            result = null;
        }
        else
        {
            int totalByteCount = 0;
            for (final byte[] byteArray : byteArrays)
            {
                if (byteArray != null)
                {
                    totalByteCount += byteArray.length;
                }
            }

            result = new byte[totalByteCount];
            int resultIndex = 0;
            for (final byte[] byteArray : byteArrays)
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
}
