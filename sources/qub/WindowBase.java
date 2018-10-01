package qub;

public abstract class WindowBase extends DisposableBase implements Window
{
    @Override
    public Window getParentElement()
    {
        return this;
    }

    @Override
    public Window getParentWindow()
    {
        return this;
    }
}
