package qub;

public interface Size2Distance extends Size2<Distance>
{
    public static final Size2Distance zero = Size2Distance.create();

    public static MutableSize2Distance create()
    {
        return MutableSize2Distance.create();
    }

    public static MutableSize2Distance create(Distance width, Distance height)
    {
        return MutableSize2Distance.create(width, height);
    }
}
