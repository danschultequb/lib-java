package qub;

public interface Floats
{
    /**
     * The number of bits in a float/Float.
     */
    int bitCount = 32;

    /**
     * The number of bytes in a float/Float.
     */
    int byteCount = 4;

    /**
     * Get the string representation of the provided float.
     * @param value The value to convert to a String.
     * @return The string representation of the provided float.
     */
    static String toString(float value)
    {
        return java.lang.Float.toString(value);
    }

    /**
     * Get the string representation of the provided float.
     * @param value The value to convert to a String.
     * @return The string representation of the provided float.
     */
    static String toString(java.lang.Float value)
    {
        PreCondition.assertNotNull(value, "value");

        return java.lang.Float.toString(value);
    }
}
