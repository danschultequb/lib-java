package qub;

public class AWTUIElementBase
{
    private final AWTUIBase uiBase;
    private final java.awt.Component component;
    private final RunnableEvent0 sizeChanged;
    private int componentWidthInPixels;
    private int componentHeightInPixels;
    private DynamicDistance dynamicWidth;
    private Disposable dynamicWidthChangedSubscription;
    private boolean autoWidth;
    private boolean autoHeight;

    protected AWTUIElementBase(AWTUIBase uiBase, java.awt.Component component)
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
                AWTUIElementBase.this.scheduleAsyncTask(() ->
                {
                    AWTUIElementBase.this.setComponentSizeInPixels(component.getWidth(), component.getHeight());
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
        this.componentWidthInPixels = component.getWidth();
        this.componentHeightInPixels = component.getHeight();
    }

    /**
     * Create a new AWTUIElementBase object with the provided uiBase and component.
     * @param uiBase The AWTUIBase that contains common UI base functionality.
     * @param component The AWT component that the new AWTUIElementBase will wrap.
     * @return The new AWTUIElementBase object.
     */
    public static AWTUIElementBase create(AWTUIBase uiBase, java.awt.Component component)
    {
        return new AWTUIElementBase(uiBase, component);
    }

    protected AWTUIBase getUIBase()
    {
        return this.uiBase;
    }

    protected java.awt.Component getComponent()
    {
        return this.component;
    }

    public Result<Void> scheduleAsyncTask(Action0 action)
    {
        return this.uiBase.scheduleAsyncTask(action);
    }

    public AWTUIElementBase setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.autoWidth = false;
        this.clearDynamicWidth();
        return this.setComponentWidth(width);
    }

    public AWTUIElementBase setWidthInPixels(int widthInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");

        this.autoWidth = false;
        this.clearDynamicWidth();
        return this.setComponentWidthInPixels(widthInPixels);
    }

    public AWTUIElementBase setDynamicWidth(DynamicDistance dynamicWidth)
    {
        PreCondition.assertNotNull(dynamicWidth, "dynamicWidth");

        this.clearDynamicWidth();

        this.autoWidth = false;
        this.dynamicWidth = dynamicWidth;
        this.dynamicWidthChangedSubscription = dynamicWidth.onChanged(() ->
        {
            this.setComponentWidth(dynamicWidth.get());
        });
        this.setComponentWidth(dynamicWidth.get());

        return this;
    }

    private void clearDynamicWidth()
    {
        if (this.dynamicWidth != null)
        {
            this.dynamicWidthChangedSubscription.dispose().await();
            this.dynamicWidthChangedSubscription = null;
            this.dynamicWidth.dispose().await();
            this.dynamicWidth = null;
        }
    }

    private AWTUIElementBase setComponentWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");

        final int widthInPixels = (int)this.uiBase.convertHorizontalDistanceToPixels(width);
        return this.setComponentWidthInPixels(widthInPixels);
    }

    private AWTUIElementBase setComponentWidthInPixels(int widthInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");

        if (widthInPixels != this.componentWidthInPixels)
        {
            this.componentWidthInPixels = widthInPixels;
            this.component.setSize(widthInPixels, this.componentHeightInPixels);
            this.sizeChanged.run();
        }
        return this;
    }

    /**
     * Get the width of this AWTUIElementBase's component.
     * @return The width of this AWTUIElementBase's component.
     */
    public Distance getWidth()
    {
        final int widthInPixels = this.component.getWidth();
        final Distance result = this.getUIBase().convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    /**
     * Get the width of this AWTUIElementBase's component in pixels.
     * @return The width of this AWTUIElementBase's component in pixels.
     */
    public int getWidthInPixels()
    {
        final int result = this.component.getWidth();

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public AWTUIElementBase setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.autoHeight = false;
        return this.setComponentHeight(height);
    }

    public AWTUIElementBase setHeightInPixels(int heightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        this.autoHeight = false;
        return this.setComponentHeightInPixels(heightInPixels);
    }

    private AWTUIElementBase setComponentHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "componentHeight");

        final int heightInPixels = (int)this.uiBase.convertVerticalDistanceToPixels(height);
        return this.setComponentHeightInPixels(heightInPixels);
    }

    private AWTUIElementBase setComponentHeightInPixels(int heightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        if (heightInPixels != this.componentHeightInPixels)
        {
            this.componentHeightInPixels = heightInPixels;
            this.component.setSize(this.componentWidthInPixels, heightInPixels);
            this.sizeChanged.run();
        }
        return this;
    }

    /**
     * Get the height of the provided component.
     * @param component The component to get the height of.
     * @return The height of the provided component.
     */
    public Distance getHeight(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int heightInPixels = this.getHeightInPixels(component);
        final Distance result = this.getUIBase().convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    /**
     * Get the height of this AWTUIElementBase's Component.
     * @return The height of the this AWTUIElementBase's Component.
     */
    public Distance getHeight()
    {
        return this.getHeight(this.component);
    }

    /**
     * Get the height of this AWTUIElementBase's Component in pixels.
     * @return The height of the this AWTUIElementBase's Component in pixels.
     */
    public int getHeightInPixels()
    {
        return this.getHeightInPixels(this.component);
    }

    /**
     * Get the height of the provided component in pixels.
     * @param component The component to get the height of.
     * @return The height of the provided component in pixels.
     */
    public int getHeightInPixels(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int result = component.getHeight();

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
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
        return this.setComponentSize(width, height);
    }

    public AWTUIElementBase setSizeInPixels(int widthInPixels, int heightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        this.autoWidth = false;
        this.autoHeight = false;
        return this.setComponentSizeInPixels(widthInPixels, heightInPixels);
    }

    private AWTUIElementBase setComponentSize(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final int widthInPixels = (int)this.uiBase.convertHorizontalDistanceToPixels(width);
        final int heightInPixels = (int)this.uiBase.convertVerticalDistanceToPixels(height);
        return this.setComponentSizeInPixels(widthInPixels, heightInPixels);
    }

    private AWTUIElementBase setComponentSizeInPixels(int widthInPixels, int heightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        final boolean widthChanged = widthInPixels != this.componentWidthInPixels;
        final boolean heightChanged = heightInPixels != this.componentHeightInPixels;
        if (widthChanged || heightChanged)
        {
            this.componentWidthInPixels = widthInPixels;
            this.componentHeightInPixels = heightInPixels;
            this.component.setSize(widthInPixels, heightInPixels);
            this.sizeChanged.run();
        }
        return this;
    }

    public Size2D getSize(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int widthInPixels = component.getWidth();
        final int heightInPixels = component.getHeight();
        final Size2D result = this.getUIBase().convertPixelsToSize2D(widthInPixels, heightInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Size2D getSize()
    {
        return this.getSize(this.component);
    }

    public Disposable onSizeChanged(Action0 callback)
    {
        return this.sizeChanged.subscribe(callback);
    }

    /**
     * Register the provided callback to be invoked when the provided component's size changes.
     * @param component The component to watch.
     * @param callback The callback to register.
     * @return A Disposable that can be disposed to unregister the provided callback from the
     * provided component.
     */
    public Disposable onSizeChanged(java.awt.Component component, Action0 callback)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(callback, "callback");

        final java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener()
        {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e)
            {
                AWTUIElementBase.this.scheduleAsyncTask(callback);
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
        };
        component.addComponentListener(componentListener);
        return Disposable.create(() -> component.removeComponentListener(componentListener));
    }

    /**
     * Get the padding of the provided AWT container.
     * @param container The container to get the padding of.
     * @return The padding of the provided AWT container.
     */
    public UIPadding getPadding(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        return this.getUIBase().convertUIPaddingInPixelsToUIPadding(this.getPaddingInPixels(container));
    }

    /**
     * Get the padding of the provided AWT container in pixels.
     * @param container The container to get the padding of.
     * @return The padding of the provided AWT container in pixels.
     */
    public UIPaddingInPixels getPaddingInPixels(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final java.awt.Insets insets = container.getInsets();
        return UIPaddingInPixels.create(insets.left, insets.top, insets.right, insets.bottom);
    }

    public Distance getContentSpaceWidth(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int contentSpaceWidthInPixels = this.getContentSpaceWidthInPixels(container);
        final Distance result = contentSpaceWidthInPixels == 0 ? Distance.zero : this.getUIBase().convertHorizontalPixelsToDistance(contentSpaceWidthInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public int getContentSpaceWidthInPixels(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final java.awt.Insets insets = container.getInsets();
        final int paddingWidth = insets.left + insets.right;
        final int containerWidth = container.getWidth();

        final int result = containerWidth < paddingWidth ? 0 : containerWidth - paddingWidth;

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance getContentSpaceHeight(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int containerSpaceHeightInPixels = this.getContentSpaceHeightInPixels(container);
        final Distance result = containerSpaceHeightInPixels == 0 ? Distance.zero : this.getUIBase().convertVerticalPixelsToDistance(containerSpaceHeightInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public int getContentSpaceHeightInPixels(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final java.awt.Insets insets = container.getInsets();
        final int paddingHeight = insets.top + insets.bottom;
        final int containerHeight = container.getHeight();

        final int result = containerHeight < paddingHeight ? 0 : containerHeight - paddingHeight;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Size2D getContentSpaceSize(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final java.awt.Insets insets = container.getInsets();
        final int paddingWidth = insets.left + insets.right;
        final int paddingHeight = insets.top + insets.bottom;
        final int containerWidth = container.getWidth();
        final int containerHeight = container.getHeight();

        Size2D result;
        if (containerWidth < paddingWidth)
        {
            if (containerHeight < paddingHeight)
            {
                result = Size2D.zero;
            }
            else
            {
                result = Size2D.create(Distance.zero, this.getUIBase().convertVerticalPixelsToDistance(containerHeight - paddingHeight));
            }
        }
        else
        {
            final Distance contentSpaceWidth = this.getUIBase().convertHorizontalPixelsToDistance(containerWidth - paddingWidth);
            if (containerHeight < paddingHeight)
            {
                result = Size2D.create(contentSpaceWidth, Distance.zero);
            }
            else
            {
                result = Size2D.create(contentSpaceWidth, this.getUIBase().convertVerticalPixelsToDistance(containerHeight - paddingHeight));
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the background color of this AWTUIElementBase's Component.
     * @return The background color of this AWTUIElementBase's Component.
     */
    public Color getBackgroundColor()
    {
        return this.getBackgroundColor(this.component);
    }

    /**
     * Get the background color of the provided Component.
     * @return The background color of the provided Component.
     */
    public Color getBackgroundColor(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final java.awt.Color color = component.getBackground();
        final int redComponent = color.getRed();
        final int greenComponent = color.getGreen();
        final int blueComponent = color.getBlue();
        final int alphaComponent = color.getAlpha();
        final Color result = Color.create(redComponent, greenComponent, blueComponent, alphaComponent);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the background color of this AWTUIElementBase's Component.
     * @param backgroundColor The background color of this AWTUIElementBase's Component.
     */
    public AWTUIElementBase setBackgroundColor(Color backgroundColor)
    {
        this.setBackgroundColor(this.component, backgroundColor);
        return this;
    }

    /**
     * Set the background color of the provided Component.
     * @param backgroundColor The background color of the provided Component.
     */
    public void setBackgroundColor(java.awt.Component component, Color backgroundColor)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(backgroundColor, "backgroundColor");

        final int redComponent = backgroundColor.getRedComponent();
        final int greenComponent = backgroundColor.getGreenComponent();
        final int blueComponent = backgroundColor.getBlueComponent();
        final int alphaComponent = backgroundColor.getAlphaComponent();
        component.setBackground(new java.awt.Color(redComponent, greenComponent, blueComponent, alphaComponent));
    }

    public Point2D getPosition(AWTUIElement awtUiElement)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        return this.getPosition(awtUiElement.getComponent());
    }

    /**
     * Get the position of the provided AWT Component relative to its parent container.
     * @param component The AWT Component to get the relative position of.
     * @return The position of the provided AWT Component relative to its parent container.
     */
    public Point2D getPosition(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final java.awt.Point location = component.getLocation();
        final Point2D result = this.getUIBase().convertPixelsToPoint2D(location.x, location.y);

        PostCondition.assertNotNull(result, "result");

        return result;
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
        return this.getFontSize(this.component);
    }

    public Distance getFontSize(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final float fontSize2D = component.getFont().getSize2D();
        final Distance result = Distance.fontPoints(fontSize2D);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    /**
     * Set the size of the font of this AWTUIElementBase's component.
     * @param fontSize The size of the font to set.
     */
    public void setFontSize(Distance fontSize)
    {
        this.setFontSize(this.component, fontSize);
    }

    /**
     * Set the size of the font of the provided Component.
     * @param component The Component to set the font size for.
     * @param fontSize The size of the font to set.
     */
    public void setFontSize(java.awt.Component component, Distance fontSize)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        final java.awt.Font font = component.getFont();
        final float fontPoints = (float)fontSize.toFontPoints().getValue();
        final java.awt.Font updatedFont = font.deriveFont(fontPoints);
        component.setFont(updatedFont);
    }
}
