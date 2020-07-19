package qub;

public class AWTUIBase extends UIBase
{
    private final AsyncScheduler parallelAsyncRunner;

    protected AWTUIBase(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        super(display, mainAsyncRunner);

        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");

        this.parallelAsyncRunner = parallelAsyncRunner;
    }

    public static AWTUIBase create(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        return new AWTUIBase(display, mainAsyncRunner, parallelAsyncRunner);
    }

    public static AWTUIBase create(Process process)
    {
        return AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }

    /**
     * This method should be called from the AWT UI Event Dispatcher thread to register the parallel async runner with
     * that thread.
     */
    public void registerUIEventThread()
    {
        final AsyncScheduler asyncRunner = CurrentThread.getAsyncRunner()
            .catchError(NotFoundException.class)
            .await();
        if (asyncRunner == null)
        {
            CurrentThread.setAsyncRunner(this.parallelAsyncRunner);
        }
    }

    /**
     * Get the width of the provided component.
     * @param component The component to get the width of.
     * @return The width of the provided component.
     */
    public Distance getWidth(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int widthInPixels = component.getWidth();
        final Distance result = this.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    /**
     * Get the width of the provided component in pixels.
     * @param component The component to get the width of.
     * @return The width of the provided component in pixels.
     */
    public int getWidthInPixels(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int result = component.getWidth();

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public void setWidth(java.awt.Component component, Distance width)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        final int widthInPixels = (int)this.convertHorizontalDistanceToPixels(width);
        this.setWidthInPixels(component, widthInPixels);
    }

    public void setWidthInPixels(java.awt.Component component, int widthInPixels)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");

        final int heightInPixels = this.getHeightInPixels(component);
        this.setSizeInPixels(component, widthInPixels, heightInPixels);
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
        final Distance result = this.convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
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

    public void setHeight(java.awt.Component component, Distance height)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final int heightInPixels = (int)this.convertVerticalDistanceToPixels(height);
        this.setHeightInPixels(component, heightInPixels);
    }

    public void setHeightInPixels(java.awt.Component component, int heightInPixels)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        final int widthInPixels = this.getWidthInPixels(component);
        this.setSizeInPixels(component, widthInPixels, heightInPixels);
    }

    public void setSize(java.awt.Component component, Distance width, Distance height)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final int widthInPixels = (int)this.convertHorizontalDistanceToPixels(width);
        final int heightInPixels = (int)this.convertVerticalDistanceToPixels(height);
        this.setSizeInPixels(component, widthInPixels, heightInPixels);
    }

    /**
     * Set the size of the provided component to be the provided pixel distances.
     * @param component The component to set the size of.
     * @param widthInPixels The width of the component in pixels.
     * @param heightInPixels The height of the component in pixels.
     */
    public void setSizeInPixels(java.awt.Component component, int widthInPixels, int heightInPixels)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertGreaterThanOrEqualTo(widthInPixels, 0, "widthInPixels");
        PreCondition.assertGreaterThanOrEqualTo(heightInPixels, 0, "heightInPixels");

        component.setSize(widthInPixels, heightInPixels);
    }

    public Size2D getSize(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int widthInPixels = component.getWidth();
        final int heightInPixels = component.getHeight();
        final Size2D result = this.convertPixelsToSize2D(widthInPixels, heightInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
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
                AWTUIBase.this.scheduleAsyncTask(callback);
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

        return this.convertUIPaddingInPixelsToUIPadding(this.getPaddingInPixels(container));
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
                result = Size2D.create(Distance.zero, this.convertVerticalPixelsToDistance(containerHeight - paddingHeight));
            }
        }
        else
        {
            final Distance contentSpaceWidth = this.convertHorizontalPixelsToDistance(containerWidth - paddingWidth);
            if (containerHeight < paddingHeight)
            {
                result = Size2D.create(contentSpaceWidth, Distance.zero);
            }
            else
            {
                result = Size2D.create(contentSpaceWidth, this.convertVerticalPixelsToDistance(containerHeight - paddingHeight));
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Distance getContentSpaceWidth(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int contentSpaceWidthInPixels = this.getContentSpaceWidthInPixels(container);
        final Distance result = contentSpaceWidthInPixels == 0 ? Distance.zero : this.convertHorizontalPixelsToDistance(contentSpaceWidthInPixels);

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
        final Distance result = containerSpaceHeightInPixels == 0 ? Distance.zero : this.convertVerticalPixelsToDistance(containerSpaceHeightInPixels);

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

    /**
     * Get the position of the provided AWT Component relative to its parent container.
     * @param component The AWT Component to get the relative position of.
     * @return The position of the provided AWT Component relative to its parent container.
     */
    public Point2D getPosition(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final java.awt.Point location = component.getLocation();
        final Point2D result = this.convertPixelsToPoint2D(location.x, location.y);

        PostCondition.assertNotNull(result, "result");

        return result;
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
     * Set the background color of thie provided Component.
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
}
