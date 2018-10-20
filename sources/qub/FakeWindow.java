package qub;

public class FakeWindow implements Window
{
    private boolean opened;
    private boolean disposed;
    private UIPainter painter;
    private UIElement content;
    private Size2D size;
    private Display display;

    public FakeWindow()
    {
        painter = new FakePainter();
        size = Size2D.zero;
        display = new Display(1000, 1000, 100, 100);
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
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;

            setContent(null);

            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertTrue(isDisposed(), "isDisposed()");
        PostCondition.assertNull(getContent(), "getContent()");

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
        PreCondition.assertTrue(!isDisposed() || uiElement == null, "!isDisposed() || uiElement == null");

        if (content != uiElement)
        {
            if (content != null)
            {
                content.setParent(null);
                content.parentWindowChanged(this, null);
            }

            content = uiElement;

            if (uiElement != null)
            {
                final Window previousParentWindow = uiElement.getParentWindow();
                uiElement.setParent(this);
                if (previousParentWindow != this)
                {
                    uiElement.parentWindowChanged(previousParentWindow, this);
                }
            }

            repaint();
        }
    }

    @Override
    public UIElement getContent()
    {
        return content;
    }

    @Override
    public void setWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        setSize(getSize().changeWidth(width));
    }

    @Override
    public Distance getWidth()
    {
        return size.getWidth();
    }

    @Override
    public void setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        setSize(getSize().changeHeight(height));
    }

    @Override
    public Distance getHeight()
    {
        return size.getHeight();
    }

    @Override
    public Size2D getSize()
    {
        return size;
    }

    @Override
    public void setSize(Distance width, Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        setSize(new Size2D(width, height));
    }

    @Override
    public void setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        this.size = size;
    }

    public void setDisplay(Display display)
    {
        PreCondition.assertNotNull(display, "display");

        this.display = display;
    }

    @Override
    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        return display.convertHorizontalDistanceToPixels(horizontalDistance);
    }

    @Override
    public Distance convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        return display.convertHorizontalPixelsToDistance(horizontalPixels);
    }

    @Override
    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        return display.convertVerticalDistanceToPixels(verticalDistance);
    }

    @Override
    public Distance convertVerticalPixelsToDistance(double verticalPixels)
    {
        return display.convertVerticalPixelsToDistance(verticalPixels);
    }

    @Override
    public Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels)
    {
        return display.convertPixelsToSize2D(horizontalPixels, verticalPixels);
    }
}
