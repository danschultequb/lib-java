package qub;

public interface Size2Integer extends Size2<Integer>
{
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
