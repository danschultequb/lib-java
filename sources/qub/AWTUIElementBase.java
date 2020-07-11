package qub;

public class AWTUIElementBase
{
    private final AWTUIBase uiBase;
    private final java.awt.Component component;
    private boolean autoWidth;
    private boolean autoHeight;

    public AWTUIElementBase(AWTUIBase uiBase, java.awt.Component component)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");
        PreCondition.assertNotNull(component, "component");

        this.uiBase = uiBase;
        this.component = component;
        this.autoWidth = true;
        this.autoHeight = true;

        this.updateSize();
    }

    protected AWTUIBase getUIBase()
    {
        return this.uiBase;
    }

    protected java.awt.Component getComponent()
    {
        return this.component;
    }

    /**
     * This method should be called from the AWT UI Event Dispatcher thread to register the parallel async runner with
     * that thread.
     */
    public void registerUIEventThread()
    {
        this.uiBase.registerUIEventThread();
    }

    public Result<Void> scheduleAsyncTask(Action0 action)
    {
        return this.uiBase.scheduleAsyncTask(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask(Action0 action)
    {
        return this.uiBase.createPausedAsyncTask(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask()
    {
        return this.uiBase.createPausedAsyncTask();
    }

    public void setWidth(Distance width)
    {
        this.autoWidth = false;
        this.setSizeInner(width, this.getHeight());
    }

    public Distance getWidth(java.awt.Component component)
    {
        return this.uiBase.getWidth(component);
    }

    public Distance getWidth()
    {
        return this.getWidth(this.component);
    }

    public void setHeight(Distance height)
    {
        this.autoHeight = false;
        this.setSizeInner(this.getWidth(), height);
    }

    public Distance getHeight(java.awt.Component component)
    {
        return this.uiBase.getHeight(component);
    }

    public Distance getHeight()
    {
        return this.getHeight(this.component);
    }

    public void setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        this.setSize(size.getWidth(), size.getHeight());
    }

    public void setSize(Distance width, Distance height)
    {
        this.autoWidth = false;
        this.autoHeight = false;
        this.setSizeInner(width, height);
    }

    private void setSizeInner(Distance width, Distance height)
    {
        this.uiBase.setSize(this.component, width, height);
    }

    public Size2D getSize(java.awt.Component component)
    {
        return this.uiBase.getSize(component);
    }

    public Size2D getSize()
    {
        return this.getSize(this.component);
    }

    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiBase.onSizeChanged(this.component, callback);
    }

    /**
     * Get the background color of this AWTUIElementBase's Component.
     * @return The background color of this AWTUIElementBase's Component.
     */
    public Color getBackgroundColor()
    {
        return this.uiBase.getBackgroundColor(this.component);
    }

    /**
     * Set the background color of this AWTUIElementBase's Component.
     * @param backgroundColor The background color of this AWTUIElementBase's Component.
     */
    public void setBackgroundColor(Color backgroundColor)
    {
        this.uiBase.setBackgroundColor(this.component, backgroundColor);
    }

    public Point2D getPosition(AWTUIElement awtUiElement)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        return this.getPosition(awtUiElement.getComponent());
    }

    public Point2D getPosition(java.awt.Component component)
    {
        return this.uiBase.getPosition(component);
    }

    public Point2D getPosition()
    {
        return this.getPosition(this.component);
    }

    public void updateSize()
    {
        if (this.autoWidth || this.autoHeight)
        {
            java.awt.Dimension newSize;
            final java.awt.Dimension preferredSize = this.component.getPreferredSize();
            if (this.autoWidth && this.autoHeight)
            {
                newSize = preferredSize;
            }
            else
            {
                newSize = this.component.getSize();
                if (this.autoWidth)
                {
                    newSize.width = preferredSize.width;
                }
                else
                {
                    newSize.height = preferredSize.height;
                }
            }
            this.component.setSize(newSize);
        }
    }

    public Distance getContentWidth(java.awt.Container container)
    {
        return this.uiBase.getContentWidth(container);
    }

    public int getContentWidthInPixels(java.awt.Container container)
    {
        return this.uiBase.getContentWidthInPixels(container);
    }

    public Distance getContentHeight(java.awt.Container container)
    {
        return this.uiBase.getContentHeight(container);
    }

    public int getContentHeightInPixels(java.awt.Container container)
    {
        return this.uiBase.getContentHeightInPixels(container);
    }

    public Size2D getContentSize(java.awt.Container container)
    {
        return this.uiBase.getContentSize(container);
    }

    public void setContentSize(java.awt.Container container, Size2D contentSize)
    {
        this.uiBase.setContentSize(container, contentSize);
    }

    public void setContentSize(java.awt.Container container, Distance contentWidth, Distance contentHeight)
    {
        this.uiBase.setContentSize(container, contentWidth, contentHeight);
    }

    public Distance getFontSize()
    {
        return this.uiBase.getFontSize(this.component);
    }

    public void setFontSize(Distance fontSize)
    {
        this.uiBase.setFontSize(this.component, fontSize);
    }
}
