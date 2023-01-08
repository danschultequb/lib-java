package qub;

public interface Size2Integer extends Size2<Integer>
{
    public static MutableSize2Integer create()
    {
        return MutableSize2Integer.create();
    }

    public static MutableSize2Integer create(int width, int height)
    {
        return MutableSize2Integer.create(width, height);
    }

    @Override
    default Integer getWidth()
    {
        return this.getWidthAsInt();
    }

    int getWidthAsInt();

    @Override
    default Integer getHeight()
    {
        return this.getHeightAsInt();
    }

    int getHeightAsInt();
}
