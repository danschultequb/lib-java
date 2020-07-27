package qub;

public class AWTUIElementBase
{
    private final AWTUIBase uiBase;
    private final java.awt.Component component;
    private final RunnableEvent0 sizeChanged;
    private int widthInPixels;
    private int heightInPixels;
    private DynamicDistance dynamicWidth;
    private boolean autoWidth;
    private boolean autoHeight;

    public AWTUIElementBase(AWTUIBase uiBase, java.awt.Component component)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");
        PreCondition.assertNotNull(component, "component");

        this.uiBase = uiBase;
        this.component = component;
        this.sizeChanged = Event0.create();
        this.autoWidth = true;
        this.autoHeight = true;

        this.component.addComponentListener(new java.awt.event.ComponentListener()
        {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e)
            {
                final AWTUIElementBase uiElementBase = AWTUIElementBase.this;
                uiElementBase.uiBase.scheduleAsyncTask(() ->
                {
                    uiElementBase.setSizeInPixels(uiElementBase.component.getWidth(), uiElementBase.component.getHeight());
                });
            }

            @Override
            public void componentMoved(java.awt.event.ComponentEvent e)
            {
            }

            @Override
            public void componentShown(java.awt.event.ComponentEvent e)
            {
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e)
            {
            }
        });

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

    public AWTUIElementBase setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        final int widthInPixels = (int)this.uiBase.convertHorizontalDistanceToPixels(width);
        return this.setWidthInPixels(widthInPixels);
    }

    public AWTUIElementBase setDynamicWidth(DynamicDistance dynamicWidth)
    {
        PreCondition.assertNotNull(dynamicWidth, "dynamicWidth");

        if (this.dynamicWidth != null)
        {
            this.dynamicWidth.dispose().await();
            this.dynamicWidth = null;
        }

        this.dynamicWidth = dynamicWidth;
        dynamicWidth.onChanged(() ->
        {
            this.uiBase.setWidth(this.component, this.dynamicWidth.get());
        });

        this.autoWidth = false;
        final Distance width = dynamicWidth.get();
        if (!this.getWidth().equals(width))
        {
            this.uiBase.setWidth(this.component, this.dynamicWidth.get());
            this.sizeChanged.run();
        }

        return this;
    }

    public AWTUIElementBase setWidthInPixels(int widthInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");

        this.autoWidth = false;
        if (this.getWidthInPixels() != widthInPixels)
        {
            this.uiBase.setWidthInPixels(this.component, widthInPixels);
            this.sizeChanged.run();
        }
        return this;
    }

    private AWTUIElementBase setComponentWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.autoWidth = false;
        if (!this.getWidth().equals(width))
        {
            this.uiBase.setWidth(this.component, width);
            this.sizeChanged.run();
        }

        return this;
    }

    private AWTUIElementBase setComponentWidthInPixels(int widthInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");

        this.autoWidth = false;
        if (this.getWidthInPixels() != widthInPixels)
        {
            this.widthInPixels = widthInPixels;
            this.uiBase.setWidthInPixels(this.component, widthInPixels);
            this.sizeChanged.run();
        }

        return this;
    }

    /**
     * Get the width of the provided component.
     * @param component The component to get the width of.
     * @return The width of the provided component.
     */
    public Distance getWidth(java.awt.Component component)
    {
        return this.uiBase.getWidth(component);
    }

    /**
     * Get the width of this AWTUIElementBase's component.
     * @return The width of this AWTUIElementBase's component.
     */
    public Distance getWidth()
    {
        return this.getWidth(this.component);
    }

    /**
     * Get the width of the provided component in pixels.
     * @param component The component to get the width of.
     * @return The width of the provided component in pixels.
     */
    public int getWidthInPixels(java.awt.Component component)
    {
        return this.uiBase.getWidthInPixels(component);
    }

    /**
     * Get the width of this AWTUIElementBase's component in pixels.
     * @return The width of this AWTUIElementBase's component in pixels.
     */
    public int getWidthInPixels()
    {
        return this.getWidthInPixels(this.component);
    }

    public AWTUIElementBase setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.autoHeight = false;
        if (!this.getHeight().equals(height))
        {
            this.uiBase.setHeight(this.component, height);
            this.sizeChanged.run();
        }
        return this;
    }

    public AWTUIElementBase setHeightInPixels(int heightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        this.autoHeight = false;
        if (this.getHeightInPixels() != heightInPixels)
        {
            this.uiBase.setHeightInPixels(this.component, heightInPixels);
            this.sizeChanged.run();
        }
        return this;
    }

    public Distance getHeight(java.awt.Component component)
    {
        return this.uiBase.getHeight(component);
    }

    public Distance getHeight()
    {
        return this.getHeight(this.component);
    }

    public int getHeightInPixels()
    {
        return this.uiBase.getHeightInPixels(this.component);
    }

    public AWTUIElementBase setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return this.setSize(size.getWidth(), size.getHeight());
    }

    public AWTUIElementBase setSize(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.autoWidth = false;
        this.autoHeight = false;
        if (!this.getWidth().equals(width) || !this.getHeight().equals(height))
        {
            this.uiBase.setSize(this.component, width, height);
            this.sizeChanged.run();
        }
        return this;
    }

    public AWTUIElementBase setSizeInPixels(int widthInPixels, int heightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        this.autoWidth = false;
        this.autoHeight = false;
        if (this.getWidthInPixels() != widthInPixels || this.getHeightInPixels() != heightInPixels)
        {
            this.uiBase.setSizeInPixels(this.component, widthInPixels, heightInPixels);
            this.sizeChanged.run();
        }
        return this;
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
        return this.sizeChanged.subscribe(callback);
    }

    public UIPadding getPadding(java.awt.Container container)
    {
        return this.getUIBase().getPadding(container);
    }

    public UIPaddingInPixels getPaddingInPixels(java.awt.Container container)
    {
        return this.getUIBase().getPaddingInPixels(container);
    }

    public Distance getContentSpaceWidth(java.awt.Container container)
    {
        return this.uiBase.getContentSpaceWidth(container);
    }

    public int getContentSpaceWidthInPixels(java.awt.Container container)
    {
        return this.uiBase.getContentSpaceWidthInPixels(container);
    }

    public Distance getContentSpaceHeight(java.awt.Container container)
    {
        return this.uiBase.getContentSpaceHeight(container);
    }

    public int getContentSpaceHeightInPixels(java.awt.Container container)
    {
        return this.uiBase.getContentSpaceHeightInPixels(container);
    }

    public Size2D getContentSpaceSize(java.awt.Container container)
    {
        return this.uiBase.getContentSpaceSize(container);
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
    public AWTUIElementBase setBackgroundColor(Color backgroundColor)
    {
        this.uiBase.setBackgroundColor(this.component, backgroundColor);
        return this;
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

    public Distance getFontSize()
    {
        return this.uiBase.getFontSize(this.component);
    }

    public AWTUIElementBase setFontSize(Distance fontSize)
    {
        this.uiBase.setFontSize(this.component, fontSize);
        return this;
    }
}
