package qub;

/**
 * A basic Array data structure that holds a fixed number of elements.
 * @param <T> The type of element contained by this Array.
 */
public interface Array<T> extends MutableIndexable<T>
{
    @Override
    default boolean any()
    {
        return 1 <= getCount();
    }

    @Override
    default ArrayIterator<T> iterate()
    {
        return ArrayIterator.create(this);
    }

    default ArrayIterator<T> iterateReverse()
    {
        return ArrayIterator.createReverse(this);
    }

    /**
     * Create a new array with the provided number of elements.
     * @param length The number of elements.
     * @return The new array.
     */
    static <T> Array<T> createWithLength(int length)
    {
        return ObjectArray.createWithLength(length);
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    @SafeVarargs
    static <T> Array<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return ObjectArray.create(values);
    }

    /**
     * Create an Array create the provided values.
     * @param values The values to initialize the array with.
     */
    static <T> Array<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(List.create(values));
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
     * Create a new {@link T}[] from the provided values.
     * @param values The values to put into an array.
     * @param <T> The type of value stored in the array.
     */
    public static <T> int toArray(Iterable<T> values, T[] output)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(output, "output");

        return Array.toArray(values.iterate(), output);
    }

    /**
     * Insert the values from the provided {@link Iterator} into the provided {@link T}[]. The
     * number of values that are inserted will be returned.
     * @param values The values to put into an array.
     * @param output The {@link T}[] that the values will be inserted into.
     * @param <T> The type of value stored in the array.
     */
    public static <T> int toArray(Iterator<T> values, T[] output)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(output, "output");

        values.start();

        int result = 0;
        while (values.hasCurrent() && result < output.length)
        {
            output[result] = values.getCurrent();
            result++;
            values.next();
        }

        PostCondition.assertBetween(0, result, output.length, "result");

        return result;
    }

    /**
     * Create a new boolean[] from the provided values.
     * @param values The values to create a boolean[] from.
     * @return The boolean[].
     */
    static Result<boolean[]> toBooleanArray(Iterator<Boolean> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.toBooleanArray(List.create(values));
    }

    /**
     * Convert the provided Iterable&lt;Boolean&gt; to a boolean[].
     * @param values The Iterable&lt;Boolean&gt; to convert to a boolean[].
     * @return The converted boolean[].
     */
    static Result<boolean[]> toBooleanArray(Iterable<Boolean> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Result.create(() ->
        {
            final boolean[] result = new boolean[values.getCount()];
            int index = 0;
            for (final Boolean value : values)
            {
                if (value == null)
                {
                    throw new NullPointerException("The " + index + " element cannot be null.");
                }
                result[index++] = value;
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Create a new byte[] from the provided values.
     * @param values The values to create a byte[] from.
     * @return The byte[].
     */
    static byte[] toByteArray(Iterator<Byte> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.toByteArray(List.create(values));
    }

    /**
     * Convert the provided byte Iterable into a byte array.
     * @param values The byte Iterable to convert to a byte array.
     * @return The byte array.
     */
    static byte[] toByteArray(Iterable<Byte> values)
    {
        PreCondition.assertNotNull(values, "values");

        byte[] result;
        if (values instanceof ByteArray)
        {
            result = ((ByteArray)values).toByteArray();
        }
        else if (values instanceof ByteList)
        {
            result = ((ByteList)values).toByteArray();
        }
        else
        {
            result = new byte[values.getCount()];
            int index = 0;
            for (final Byte value : values)
            {
                PreCondition.assertNotNull(value, "The " + English.addOrdinalIndicator(index) + " element");
                result[index++] = value;
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
            PreCondition.assertByte(values[i], "The " + English.addOrdinalIndicator(i) + " value");
            result[i] = (byte)values[i];
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(values.length, result.length, "result.length");

        return result;
    }

    /**
     * Convert the provided character Iterator into a char array.
     * @param values The character Iterator to convert to a char array.
     * @return The char array.
     */
    static Result<char[]> toCharArray(Iterator<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.toCharArray(values.toList());
    }

    /**
     * Convert the provided character Iterable into a char array.
     * @param values The character Iterable to convert to a char array.
     * @return The char array.
     */
    static Result<char[]> toCharArray(Iterable<Character> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Result.create(() ->
        {
            final int valuesCount = values.getCount();
            final char[] result = new char[valuesCount];
            if (valuesCount > 0)
            {
                int index = 0;
                for (final Character value : values)
                {
                    if (value == null)
                    {
                        throw new NullPointerException("The " + index + " element cannot be null.");
                    }
                    result[index++] = value;
                }
            }
            return result;
        });
    }

    /**
     * Convert the provided int Iterator into a int array.
     * @param values The int Iterator to convert to an int array.
     * @return The int array.
     */
    static Result<int[]> toIntArray(Iterator<Integer> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.toIntArray(values.toList());
    }

    /**
     * Convert the provided int Iterable into a int array.
     * @param values The int Iterable to convert to a int array.
     * @return The int array.
     */
    static Result<int[]> toIntArray(Iterable<Integer> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Result.create(() ->
        {
            final int valuesCount = values.getCount();
            final int[] result = new int[valuesCount];
            if (valuesCount > 0)
            {
                int index = 0;
                for (final Integer value : values)
                {
                    if (value == null)
                    {
                        throw new NullPointerException("The " + index + " element cannot be null.");
                    }
                    result[index++] = value;
                }
            }
            return result;
        });
    }

    /**
     * Get a new byte[] that is a clone of the provided toClone byte[].
     * @param toClone The byte[] to clone.
     * @return The cloned byte[].
     */
    static byte[] clone(byte[] toClone)
    {
        return Array.clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new byte[] that is a clone of the provided toClone byte[].
     * @param toClone The byte[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of bytes create toClone to clone.
     * @return The cloned byte[].
     */
    static byte[] clone(byte[] toClone, int startIndex, int length)
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
    static char[] clone(char[] toClone)
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
    static char[] clone(char[] toClone, int startIndex, int length)
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
    static int[] clone(int[] toClone)
    {
        return Array.clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new int[] that is a clone of the provided toClone int[].
     * @param toClone The int[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of characters create toClone to clone.
     * @return The cloned int[].
     */
    static int[] clone(int[] toClone, int startIndex, int length)
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
     * Get a new long[] that is a clone of the provided toClone long[].
     * @param toClone The long[] to clone.
     * @return The cloned long[].
     */
    static long[] clone(long[] toClone)
    {
        return Array.clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new long[] that is a clone of the provided toClone long[].
     * @param toClone The long[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of characters create toClone to clone.
     * @return The cloned long[].
     */
    static long[] clone(long[] toClone, int startIndex, int length)
    {
        long[] result;

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
            result = new long[resultLength];
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
    static void copy(byte[] copyFrom, int copyFromStartIndex, byte[] copyTo, int copyToStartIndex, int length)
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
    static void copy(char[] copyFrom, int copyFromStartIndex, char[] copyTo, int copyToStartIndex, int length)
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
     * Copy the contents of the copyFrom int[] to the copyTo int[] starting at the
     * copyToStartIndex.
     * @param copyFrom The int[] to copy create.
     * @param copyTo The int[] to copy to.
     * @param copyToStartIndex The index within copyTo to start copying to.
     * @param length The number of integers to copy from the copyFrom int[] to the copyTo int[].
     */
    static void copy(int[] copyFrom, int copyFromStartIndex, int[] copyTo, int copyToStartIndex, int length)
    {
        PreCondition.assertNotNull(copyFrom, "copyFrom");
        PreCondition.assertStartIndex(copyFromStartIndex, copyFrom.length, "copyFromStartIndex");
        PreCondition.assertNotNull(copyTo, "copyTo");
        PreCondition.assertStartIndex(copyToStartIndex, copyTo.length, "copyToStartIndex");
        PreCondition.assertLength(length, copyFromStartIndex, copyFrom.length);
        PreCondition.assertLength(length, copyToStartIndex, copyTo.length);

        System.arraycopy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);
    }

    /**
     * Copy the contents of the copyFrom int[] to the copyTo int[] starting at the
     * copyToStartIndex.
     * @param copyFrom The int[] to copy create.
     * @param copyTo The int[] to copy to.
     * @param copyToStartIndex The index within copyTo to start copying to.
     * @param length The number of integers to copy from the copyFrom int[] to the copyTo int[].
     */
    static void copy(long[] copyFrom, int copyFromStartIndex, long[] copyTo, int copyToStartIndex, int length)
    {
        PreCondition.assertNotNull(copyFrom, "copyFrom");
        PreCondition.assertStartIndex(copyFromStartIndex, copyFrom.length, "copyFromStartIndex");
        PreCondition.assertNotNull(copyTo, "copyTo");
        PreCondition.assertStartIndex(copyToStartIndex, copyTo.length, "copyToStartIndex");
        PreCondition.assertLength(length, copyFromStartIndex, copyFrom.length);
        PreCondition.assertLength(length, copyToStartIndex, copyTo.length);

        System.arraycopy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);
    }

    /**
     * Merge the provided byte arrays into a single byte array and return the merged byte array.
     * @param byteArrays The byte arrays to mergeBytes.
     * @return The merged byte array.
     */
    static byte[] mergeBytes(Iterable<byte[]> byteArrays)
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
            if (byteArray != null && byteArray.length > 0)
            {
                Array.copy(byteArray, 0, result, resultIndex, byteArray.length);
                resultIndex += byteArray.length;
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Merge the provided char arrays into a single char array and return the merged char array.
     * @param charArrays The char arrays to mergeBytes.
     * @return The merged char array.
     */
    static char[] mergeCharacters(Iterable<char[]> charArrays)
    {
        PreCondition.assertNotNull(charArrays, "charArrays");

        int totalByteCount = 0;
        for (final char[] byteArray : charArrays)
        {
            if (byteArray != null)
            {
                totalByteCount += byteArray.length;
            }
        }

        final char[] result = new char[totalByteCount];
        int resultIndex = 0;
        for (final char[] byteArray : charArrays)
        {
            if (byteArray != null)
            {
                Array.copy(byteArray, 0, result, resultIndex, byteArray.length);
                resultIndex += byteArray.length;
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the String representation of the elements within the provided boolean array.
     * @param booleanArray The boolean array to convert to a String.
     * @return The String representation of the elements within the provided boolean array.
     */
    public static String toString(boolean[] booleanArray)
    {
        final CharacterList builder = CharacterList.create();

        if (booleanArray == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final boolean element : booleanArray)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }
                builder.addAll(Booleans.toString(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided byte array.
     * @param byteArray The byte array to convert to a String.
     * @return The String representation of the elements within the provided byte array.
     */
    static String toString(byte[] byteArray)
    {
        final CharacterList builder = CharacterList.create();

        if (byteArray == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final byte element : byteArray)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }
                builder.addAll(Bytes.toString(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided char array.
     * @param array The char array to convert to a String.
     * @return The String representation of the elements within the provided char array.
     */
    static String toString(char[] array)
    {
        return Array.toString(array, (Character c) -> "'" + c + "'");
    }

    /**
     * Get the String representation of the elements within the provided char array.
     * @param array The char array to convert to a String.
     * @return The String representation of the elements within the provided char array.
     */
    static String toString(char[] array, Function1<Character,String> characterTransform)
    {
        PreCondition.assertNotNull(characterTransform, "characterTransform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final char element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(characterTransform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided short array.
     * @param array The short array to convert to a String.
     * @return The String representation of the elements within the provided short array.
     */
    static String toString(short[] array)
    {
        return Array.toString(array, (Short value) -> java.lang.Short.toString(value));
    }

    /**
     * Get the String representation of the elements within the provided short array.
     * @param array The short array to convert to a String.
     * @return The String representation of the elements within the provided short array.
     */
    static String toString(short[] array, Function1<Short,String> transform)
    {
        PreCondition.assertNotNull(transform, "transform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final short element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(transform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided int array.
     * @param array The int array to convert to a String.
     * @return The String representation of the elements within the provided int array.
     */
    static String toString(int[] array)
    {
        return Array.toString(array, Integers::toString);
    }

    /**
     * Get the String representation of the elements within the provided int array.
     * @param array The int array to convert to a String.
     * @return The String representation of the elements within the provided int array.
     */
    static String toString(int[] array, Function1<Integer,String> transform)
    {
        PreCondition.assertNotNull(transform, "transform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final int element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(transform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided long array.
     * @param array The long array to convert to a String.
     * @return The String representation of the elements within the provided long array.
     */
    static String toString(long[] array)
    {
        return Array.toString(array, Longs::toString);
    }

    /**
     * Get the String representation of the elements within the provided long array.
     * @param array The long array to convert to a String.
     * @return The String representation of the elements within the provided long array.
     */
    static String toString(long[] array, Function1<Long,String> transform)
    {
        PreCondition.assertNotNull(transform, "transform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final long element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(transform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided float array.
     * @param array The float array to convert to a String.
     * @return The String representation of the elements within the provided float array.
     */
    static String toString(float[] array)
    {
        return Array.toString(array, Floats::toString);
    }

    /**
     * Get the String representation of the elements within the provided float array.
     * @param array The float array to convert to a String.
     * @return The String representation of the elements within the provided float array.
     */
    static String toString(float[] array, Function1<Float,String> transform)
    {
        PreCondition.assertNotNull(transform, "transform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final float element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(transform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided double array.
     * @param array The double array to convert to a String.
     * @return The String representation of the elements within the provided double array.
     */
    static String toString(double[] array)
    {
        return Array.toString(array, Doubles::toString);
    }

    /**
     * Get the String representation of the elements within the provided double array.
     * @param array The double array to convert to a String.
     * @return The String representation of the elements within the provided double array.
     */
    static String toString(double[] array, Function1<Double,String> transform)
    {
        PreCondition.assertNotNull(transform, "transform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final double element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(transform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the String representation of the elements within the provided T array.
     * @param array The T array to convert to a String.
     * @return The String representation of the elements within the provided T array.
     */
    static <T> String toString(T[] array)
    {
        return Array.toString(array, Objects::toString);
    }

    /**
     * Get the String representation of the elements within the provided T array.
     * @param array The T array to convert to a String.
     * @return The String representation of the elements within the provided T array.
     */
    static <T> String toString(T[] array, Function1<T,String> transform)
    {
        PreCondition.assertNotNull(transform, "transform");

        final CharacterList builder = CharacterList.create();

        if (array == null)
        {
            builder.addAll("null");
        }
        else
        {
            builder.add('[');

            boolean addedFirstElement = false;
            for (final T element : array)
            {
                if (!addedFirstElement)
                {
                    addedFirstElement = true;
                }
                else
                {
                    builder.add(',');
                }

                builder.addAll(transform.run(element));
            }

            builder.add(']');
        }

        return builder.toString(true);
    }

    /**
     * Get the first index of the provided character in the array of characters. If the character
     * is not found, then -1 will be returned.
     * @param characters The array of characters to look through.
     * @param value The character to look for.
     * @return The first index of the provided character in the array of characters, or -1 if the
     * character is not found.
     */
    static int indexOf(char[] characters, char value)
    {
        PreCondition.assertNotNull(characters, "characters");

        final int result = characters.length == 0 ? -1 : Array.indexOf(characters, 0, characters.length, value);

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
    static int indexOf(char[] characters, int startIndex, int length, char value)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);

        int result = -1;
        final int endIndex = startIndex + length;
        for (int i = startIndex; i < endIndex; ++i)
        {
            if (characters[i] == value)
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
    static boolean contains(char[] array, char value)
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
    static boolean contains(int[] array, int value)
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
    static boolean contains(long[] array, long value)
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
    static <T> boolean contains(T[] array, T value)
    {
        boolean result = false;
        if (array != null)
        {
            for (final T arrayValue : array)
            {
                if (Comparer.equal(arrayValue, value))
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
    static void shiftLeft(byte[] values, int indexToRemove, int valuesToShift)
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
     * to the left. The result is that the value at the indexToRemove will be "removed".
     * @param values The values.
     * @param indexToRemove The index to "remove" create the array.
     * @param valuesToShift The number of values to shift.
     */
    static void shiftLeft(int[] values, int indexToRemove, int valuesToShift)
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
     * to the left. The result is that the value at the indexToRemove will be "removed".
     * @param values The values.
     * @param indexToRemove The index to "remove" create the array.
     * @param valuesToShift The number of values to shift.
     */
    static void shiftLeft(long[] values, int indexToRemove, int valuesToShift)
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
     * to the left. The result is that the value at the indexToRemove will be "removed".
     * @param values The values.
     * @param indexToRemove The index to "remove" create the array.
     * @param valuesToShift The number of values to shift.
     */
    static void shiftLeft(char[] values, int indexToRemove, int valuesToShift)
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
    static void shiftRight(byte[] values, int indexToOpen, int valuesToShift)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertBetween(0, indexToOpen, values.length - 2, "indexToOpen");
        PreCondition.assertBetween(0, valuesToShift, values.length - indexToOpen - 1, "valuesToShift");

        for (int i = indexToOpen + valuesToShift; indexToOpen < i; --i)
        {
            values[i] = values[i - 1];
        }
    }

    /**
     * Starting at the provided indexToRemove, shift the values in the provided array one position
     * to the right. The result is that a new position will be opened up the array.
     * @param values The values.
     * @param indexToOpen The index to open up in the array.
     * @param valuesToShift The number of values to shift.
     */
    static void shiftRight(int[] values, int indexToOpen, int valuesToShift)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertBetween(0, indexToOpen, values.length - 2, "indexToOpen");
        PreCondition.assertBetween(0, valuesToShift, values.length - indexToOpen - 1, "valuesToShift");

        for (int i = indexToOpen + valuesToShift; indexToOpen < i; --i)
        {
            values[i] = values[i - 1];
        }
    }

    /**
     * Starting at the provided indexToRemove, shift the values in the provided array one position
     * to the right. The result is that a new position will be opened up the array.
     * @param values The values.
     * @param indexToOpen The index to open up in the array.
     * @param valuesToShift The number of values to shift.
     */
    static void shiftRight(long[] values, int indexToOpen, int valuesToShift)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertBetween(0, indexToOpen, values.length - 2, "indexToOpen");
        PreCondition.assertBetween(0, valuesToShift, values.length - indexToOpen - 1, "valuesToShift");

        for (int i = indexToOpen + valuesToShift; indexToOpen < i; --i)
        {
            values[i] = values[i - 1];
        }
    }

    /**
     * Starting at the provided indexToRemove, shift the values in the provided array one position
     * to the right. The result is that a new position will be opened up the array.
     * @param values The values.
     * @param indexToOpen The index to open up in the array.
     * @param valuesToShift The number of values to shift.
     */
    static void shiftRight(char[] values, int indexToOpen, int valuesToShift)
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
