package qub;

public class AWTUIBase extends UIBase
{
    protected AWTUIBase(Display display, AsyncRunner asyncRunner)
    {
        super(display, asyncRunner);
    }

    public static AWTUIBase create(Display display, AsyncRunner asyncRunner)
    {
        return new AWTUIBase(display, asyncRunner);
    }

    public Distance getWidth(AWTUIElement awtUIElement)
    {
        PreCondition.assertNotNull(awtUIElement, "awtUIElement");

        return this.getWidth(awtUIElement.getComponent());
    }

    public Distance getWidth(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int widthInPixels = component.getWidth();
        final Distance result = this.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public Distance getHeight(AWTUIElement awtUiElement)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        return this.getHeight(awtUiElement.getComponent());
    }

    public Distance getHeight(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int heightInPixels = component.getHeight();
        final Distance result = this.convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public void setSize(AWTUIElement awtUiElement, Distance width, Distance height)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        this.setSize(awtUiElement.getComponent(), width, height);
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

    /**
     * Register the provided callback to be invoked when the provided AWTUIElement's size changes.
     * @param awtUiElement The AWTUIElement to watch.
     * @param callback The callback to register.
     * @return A Disposable that can be disposed to unregister the provided callback from the
     * provided AWTUIElement.
     */
    public Disposable onSizeChanged(AWTUIElement awtUiElement, Action0 callback)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        return this.onSizeChanged(awtUiElement.getComponent(), callback);
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
     * Set the size of the font of the provided AWTUIElement.
     * @param awtUiElement The AWTUIElement to set the font size for.
     * @param fontSize The size of the font to set.
     */
    public void setFontSize(AWTUIElement awtUiElement, Distance fontSize)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        this.setFontSize(awtUiElement.getComponent(), fontSize);
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

    public Distance getFontSize(AWTUIElement awtUiElement)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        return this.getFontSize(awtUiElement.getComponent());
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
}
