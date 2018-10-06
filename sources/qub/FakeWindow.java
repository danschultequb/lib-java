package qub;

import java.awt.Font;

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
            }

            content = uiElement;

            if (uiElement != null)
            {
                uiElement.setParent(this);
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

    @Override
    public Distance getTextWidth(String text)
    {
        return null;
    }

    @Override
    public Distance getTextWidth(String text, Font font)
    {
        return null;
    }
}
