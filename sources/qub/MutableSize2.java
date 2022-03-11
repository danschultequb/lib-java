package qub;

public interface MutableSize2<T> extends Size2<T>
{
    /**
     * Set the width dimension of this {@link MutableSize2}.
     * @param width The width dimension of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    MutableSize2<T> setWidth(T width);

    /**
     * Set the height dimension of this {@link MutableSize2}.
     * @param height The height dimension of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    MutableSize2<T> setHeight(T height);

    MutableSize2<T> set(T width, T height);
}
