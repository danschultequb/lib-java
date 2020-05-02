package qub;

/**
 * An iterator that iterates over Unicode code points.
 */
public interface UnicodeCodePointIterator extends Iterator<Integer>
{
    /**
     * Set whether or not a Unicode byte order mark (BOM) will be returned if it is found.
     * @param returnByteOrderMark Whether or not a Unicode byte order mark (BOM) will be returned if
     *                            it is found.
     * @return This object for method chaining.
     */
    UnicodeCodePointIterator setReturnByteOrderMark(boolean returnByteOrderMark);

    /**
     * Get whether or not a Unicode byte order mark (BOM) will be returned if it is found.
     * @return Whether or not a Unicode byte order mark (BOM) will be returned if it is found.
     */
    boolean getReturnByteOrderMark();

    /**
     * Get whether or not this iterator found a Unicode byte order mark (BOM) while decoding. If
     * this iterator hasn't iterated over its first code point yet, then this will return null.
     * @return Whether or not this iterator found a Unicode byte order mark (BOM) while decoding. If
     * this iterator hasn't iterated over its first code point yet, then this will return null.
     */
    Boolean foundByteOrderMark();
}
