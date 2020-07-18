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
        component.setSize(widthInPixels, heightInPixels);
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
        component.setSize(widthInPixels, heightInPixels);
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

    public Distance getContentWidth(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int contentWidthInPixels = this.getContentWidthInPixels(container);
        final Distance result = this.convertHorizontalPixelsToDistance(contentWidthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public int getContentWidthInPixels(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int widthInPixels = container.getWidth();
        final java.awt.Insets insets = container.getInsets();
        final int result = widthInPixels - insets.left - insets.right;

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance getContentHeight(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int contentHeightInPixels = this.getContentHeightInPixels(container);
        final Distance result = this.convertVerticalPixelsToDistance(contentHeightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public int getContentHeightInPixels(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int heightInPixels = container.getHeight();
        final java.awt.Insets insets = container.getInsets();
        final int result = heightInPixels - insets.top - insets.bottom;

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Size2D getContentSize(java.awt.Container container)
    {
        PreCondition.assertNotNull(container, "container");

        final int widthInPixels = container.getWidth();
        final int heightInPixels = container.getHeight();
        final java.awt.Insets insets = container.getInsets();
        final int contentWidthInPixels = widthInPixels - insets.left - insets.right;
        final int contentHeightInPixels = heightInPixels - insets.top - insets.bottom;
        final Size2D result = this.convertPixelsToSize2D(contentWidthInPixels, contentHeightInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public void setContentSize(java.awt.Container container, Size2D contentSize)
    {
        PreCondition.assertNotNull(container, "container");
        PreCondition.assertNotNull(contentSize, "contentSize");

        this.setContentSize(container, contentSize.getWidth(), contentSize.getHeight());
    }

    public void setContentSize(java.awt.Container container, Distance contentWidth, Distance contentHeight)
    {
        PreCondition.assertNotNull(container, "container");
        PreCondition.assertNotNull(contentWidth, "contentWidth");
        PreCondition.assertGreaterThanOrEqualTo(contentWidth, Distance.zero, "contentWidth");
        PreCondition.assertNotNull(contentHeight, "contentHeight");
        PreCondition.assertGreaterThanOrEqualTo(contentHeight, Distance.zero, "contentHeight");

        final int contentWidthInPixels = (int)this.convertHorizontalDistanceToPixels(contentWidth);
        final int contentHeightInPixels = (int)this.convertVerticalDistanceToPixels(contentHeight);

        final java.awt.Insets jFrameInsets = container.getInsets();
        final int widthInPixels = contentWidthInPixels + jFrameInsets.left + jFrameInsets.right;
        final int heightInPixels = contentHeightInPixels + jFrameInsets.top + jFrameInsets.bottom;

        container.setSize(widthInPixels, heightInPixels);
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
