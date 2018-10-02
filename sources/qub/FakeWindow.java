package qub;

public class FakeWindow extends WindowBase
{
    private boolean opened;
    private boolean disposed;
    private UIPainter painter;
    private UIElement content;
    private Distance width;
    private Distance height;

    public FakeWindow()
    {
        painter = new FakePainter();
        width = Distance.zero;
        height = Distance.zero;
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

    @Override
    public void setWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.width = width;
    }

    @Override
    public Distance getWidth()
    {
        return width;
    }

    @Override
    public void setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.height = height;
    }

    @Override
    public Distance getHeight()
    {
        return height;
    }
}
