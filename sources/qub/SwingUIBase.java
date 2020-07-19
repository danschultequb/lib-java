package qub;

public class SwingUIBase extends AWTUIBase
{
    protected SwingUIBase(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        super(display, mainAsyncRunner, parallelAsyncRunner);
    }

    public static SwingUIBase create(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        return new SwingUIBase(display, mainAsyncRunner, parallelAsyncRunner);
    }

    public static SwingUIBase create(Process process)
    {
        return SwingUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }

    /**
     * Get the padding of the provided Swing JComponent.
     * @param jComponent The JComponent to get the padding of.
     * @return The padding of the provided Swing JComponent.
     */
    public UIPadding getPadding(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        return this.convertUIPaddingInPixelsToUIPadding(this.getPaddingInPixels(jComponent));
    }

    /**
     * Get the padding of the provided Swing JComponent in pixels.
     * @param jComponent The JComponent to get the padding of.
     * @return The padding of the provided Swing JComponent in pixels.
     */
    public UIPaddingInPixels getPaddingInPixels(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final java.awt.Insets insets = jComponent.getInsets();
        return UIPaddingInPixels.create(insets.left, insets.top, insets.right, insets.bottom);
    }

    /**
     * Set the padding for the provided JComponent.
     * @param jComponent The JComponent to set the padding of.
     * @param padding The padding to set.
     */
    public void setPadding(javax.swing.JComponent jComponent, UIPadding padding)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");
        PreCondition.assertNotNull(padding, "padding");

        this.setPaddingInPixels(jComponent, this.convertUIPaddingToUIPaddingInPixels(padding));
    }

    /**
     * Set the padding for the provided JComponent in pixels.
     * @param jComponent The JComponent to set the padding of.
     * @param padding The padding to set in pixels.
     */
    public void setPaddingInPixels(javax.swing.JComponent jComponent, UIPaddingInPixels padding)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");
        PreCondition.assertNotNull(padding, "padding");

        final javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder(
            padding.getTop(),
            padding.getLeft(),
            padding.getBottom(),
            padding.getRight());
        jComponent.setBorder(border);
    }

    public Size2D getContentSpaceSize(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final java.awt.Insets insets = jComponent.getInsets();
        final int paddingWidth = insets.left + insets.right;
        final int paddingHeight = insets.top + insets.bottom;
        final int jComponentWidth = jComponent.getWidth();
        final int jComponentHeight = jComponent.getHeight();

        Size2D result;
        if (jComponentWidth < paddingWidth)
        {
            if (jComponentHeight < paddingHeight)
            {
                result = Size2D.zero;
            }
            else
            {
                result = Size2D.create(Distance.zero, this.convertVerticalPixelsToDistance(jComponentHeight - paddingHeight));
            }
        }
        else
        {
            final Distance contentSpaceWidth = this.convertHorizontalPixelsToDistance(jComponentWidth - paddingWidth);
            if (jComponentHeight < paddingHeight)
            {
                result = Size2D.create(contentSpaceWidth, Distance.zero);
            }
            else
            {
                result = Size2D.create(contentSpaceWidth, this.convertVerticalPixelsToDistance(jComponentHeight - paddingHeight));
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Distance getContentSpaceWidth(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final int contentSpaceWidthInPixels = this.getContentSpaceWidthInPixels(jComponent);
        final Distance result = contentSpaceWidthInPixels == 0 ? Distance.zero : this.convertHorizontalPixelsToDistance(contentSpaceWidthInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public int getContentSpaceWidthInPixels(javax.swing.JComponent container)
    {
        PreCondition.assertNotNull(container, "jComponent");

        final java.awt.Insets insets = container.getInsets();
        final int paddingWidth = insets.left + insets.right;
        final int jComponentWidth = container.getWidth();

        final int result = jComponentWidth < paddingWidth ? 0 : jComponentWidth - paddingWidth;

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance getContentSpaceHeight(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final int contentSpaceHeightInPixels = this.getContentSpaceHeightInPixels(jComponent);
        final Distance result = contentSpaceHeightInPixels == 0 ? Distance.zero : this.convertVerticalPixelsToDistance(contentSpaceHeightInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public int getContentSpaceHeightInPixels(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final java.awt.Insets insets = jComponent.getInsets();
        final int paddingHeight = insets.top + insets.bottom;
        final int jComponentHeight = jComponent.getHeight();

        final int result = jComponentHeight < paddingHeight ? 0 : jComponentHeight - paddingHeight;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Register the provided callback to be invoked when the provided component's text changes.
     * @param jTextComponent The component to watch.
     * @param callback The callback to register.
     * @return A Disposable that can be disposed to unregister the provided callback from the
     * provided component.
     */
    public Disposable onTextChanged(javax.swing.text.JTextComponent jTextComponent, Action1<String> callback)
    {
        PreCondition.assertNotNull(jTextComponent, "jTextComponent");
        PreCondition.assertNotNull(callback, "callback");

        final javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener()
        {

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }
        };
        jTextComponent.getDocument().addDocumentListener(documentListener);
        return Disposable.create(() -> jTextComponent.getDocument().removeDocumentListener(documentListener));
    }

    @Override
    public void setBackgroundColor(java.awt.Component component, Color backgroundColor)
    {
        super.setBackgroundColor(component, backgroundColor);

        if (component instanceof javax.swing.JComponent)
        {
            ((javax.swing.JComponent)component).setOpaque(backgroundColor.getAlphaComponent() != Color.ComponentMin);
        }
    }
}
