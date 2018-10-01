package qub;

public class FakeWindow extends WindowBase
{
    private boolean opened;
    private boolean disposed;
    private UIPainter painter;
    private UIElement content;

    public FakeWindow()
    {
        painter = new FakePainter();
    }

    @Override
    public void setPainter(UIPainter painter)
    {
        PreCondition.assertNotNull(painter, "painter");

        this.painter = painter;
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        final Result<Boolean> result = !disposed ? Result.successTrue() : Result.successFalse();
        disposed = true;
        return result;
    }

    @Override
    public void repaint()
    {
        if (content != null && isOpen())
        {
            content.paint(painter);
        }
    }

    @Override
    public boolean isOpen()
    {
        return opened && !disposed;
    }

    @Override
    public void open()
    {
        PreCondition.assertFalse(isOpen(), "isOpen()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        opened = true;

        repaint();
    }

    @Override
    public void setContent(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        if (content != uiElement)
        {
            if (content != null)
            {
                content.setParent(null);
            }
            content = uiElement;
            uiElement.setParent(this);

            repaint();
        }
    }
}
