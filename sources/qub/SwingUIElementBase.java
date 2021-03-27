package qub;

public class SwingUIElementBase extends AWTUIElementBase
{
    private final RunnableEvent0 paddingChanged;

    public SwingUIElementBase(AWTUIBase uiBase, javax.swing.JComponent jComponent)
    {
        super(uiBase, jComponent);

        this.paddingChanged = Event0.create();
    }

    @Override
    protected javax.swing.JComponent getComponent()
    {
        return (javax.swing.JComponent)super.getComponent();
    }

    protected javax.swing.JComponent getJComponent()
    {
        return this.getComponent();
    }

    /**
     * Get the padding of this SwingUIElementBase's JComponent.
     * @return The padding of this SwingUIElementBase's JComponent.
     */
    public UIPadding getPadding()
    {
        final UIPaddingInPixels paddingInPixels = this.getPaddingInPixels();
        final UIPadding result = this.getUIBase().convertUIPaddingInPixelsToUIPadding(paddingInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the padding of this SwingUIElementBase's JComponent in pixels.
     * @return The padding of this SwingUIElementBase's JComponent in pixels.
     */
    public UIPaddingInPixels getPaddingInPixels()
    {
        final javax.swing.JComponent jComponent = this.getJComponent();
        final java.awt.Insets insets = jComponent.getInsets();
        return UIPaddingInPixels.create(insets.left, insets.top, insets.right, insets.bottom);
    }

    /**
     * Set the padding of this SwingUIElementBase's JComponent.
     * @param padding The padding of this SwingUIElementBase's JComponent.
     * @return This object for method chaining.
     */
    public SwingUIElementBase setPadding(UIPadding padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return this.setPaddingInPixels(this.getUIBase().convertUIPaddingToUIPaddingInPixels(padding));
    }

    /**
     * Set the padding of this SwingUIElementBase's JComponent in pixels.
     * @param padding The padding of this SwingUIElementBase's JComponent in pixels.
     * @return This object for method chaining.
     */
    public SwingUIElementBase setPaddingInPixels(UIPaddingInPixels padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        final javax.swing.JComponent jComponent = this.getJComponent();
        final UIPaddingInPixels oldPadding = this.getPaddingInPixels();
        if (!oldPadding.equals(padding))
        {
            final javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder(
                padding.getTop(),
                padding.getLeft(),
                padding.getBottom(),
                padding.getRight());
            jComponent.setBorder(border);
            this.paddingChanged.run();
        }
        return this;
    }

    /**
     * Subscribe to be notified when this SwingUIElementBase's padding changes.
     * @param callback The callback that will be invoked when this SwingUIElementBase's padding changes.
     * @return A Disposable that can be disposed to unsubscribe the provided callback.
     */
    public Disposable onPaddingChanged(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.paddingChanged.subscribe(callback);
    }

    /**
     * Get the space that is available for content (size minus padding) for this
     * SwingUIElementBase's JComponent.
     * @return The space that is available for content (size minus padding) for this
     * SwingUIElementBase's JComponent.
     */
    public Size2D getContentSpaceSize()
    {
        return this.getContentSpaceSize(this.getJComponent());
    }

    /**
     * Get the space that is available for content (size minus padding) for the provided
     * JComponent.
     * @return The space that is available for content (size minus padding) for the provided
     * JComponent.
     */
    public Size2D getContentSpaceSize(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final java.awt.Insets insets = jComponent.getInsets();
        final int paddingWidth = insets.left + insets.right;
        final int paddingHeight = insets.top + insets.bottom;
        final int jComponentWidth = jComponent.getWidth();
        final int jComponentHeight = jComponent.getHeight();

        final UIBase uiBase = this.getUIBase();

        Size2D result;
        if (jComponentWidth < paddingWidth)
        {
            if (jComponentHeight < paddingHeight)
            {
                result = Size2D.zero;
            }
            else
            {
                result = Size2D.create(Distance.zero, uiBase.convertVerticalPixelsToDistance(jComponentHeight - paddingHeight));
            }
        }
        else
        {
            final Distance contentSpaceWidth = uiBase.convertHorizontalPixelsToDistance(jComponentWidth - paddingWidth);
            if (jComponentHeight < paddingHeight)
            {
                result = Size2D.create(contentSpaceWidth, Distance.zero);
            }
            else
            {
                result = Size2D.create(contentSpaceWidth, uiBase.convertVerticalPixelsToDistance(jComponentHeight - paddingHeight));
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the width that is available for content (width minus padding width) for this
     * SwingUIElementBase's JComponent.
     * @return The width that is available for content (width minus padding width) for this
     * SwingUIElementBase's JComponent.
     */
    public Distance getContentSpaceWidth()
    {
        return this.getContentSpaceWidth(this.getJComponent());
    }

    /**
     * Get the width that is available for content (width minus padding width) for the provided
     * JComponent.
     * @return The width that is available for content (width minus padding width) for the provided
     * JComponent.
     */
    public Distance getContentSpaceWidth(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final int contentSpaceWidthInPixels = this.getContentSpaceWidthInPixels(jComponent);
        final UIBase uiBase = this.getUIBase();
        final Distance result = contentSpaceWidthInPixels == 0
            ? Distance.zero
            : uiBase.convertHorizontalPixelsToDistance(contentSpaceWidthInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the width that is available for content (width minus padding width) for this
     * SwingUIElementBase's JComponent in pixels.
     * @return The width that is available for content (width minus padding width) for this
     * SwingUIElementBase's JComponent in pixels.
     */
    public int getContentSpaceWidthInPixels()
    {
        return this.getContentSpaceWidthInPixels(this.getJComponent());
    }

    /**
     * Get the width that is available for content (width minus padding width) for the provided
     * JComponent in pixels.
     * @return The width that is available for content (width minus padding width) for the provided
     * JComponent in pixels.
     */
    public int getContentSpaceWidthInPixels(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final java.awt.Insets insets = jComponent.getInsets();
        final int paddingWidth = insets.left + insets.right;
        final int jComponentWidth = jComponent.getWidth();

        final int result = jComponentWidth < paddingWidth ? 0 : jComponentWidth - paddingWidth;

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    /**
     * Get the height that is available for content (height minus padding height) for this
     * SwingUIElementBase's JComponent.
     * @return The height that is available for content (height minus padding height) for this
     * SwingUIElementBase's JComponent.
     */
    public Distance getContentSpaceHeight()
    {
        return this.getContentSpaceHeight(this.getJComponent());
    }

    /**
     * Get the height that is available for content (height minus padding height) for the provided
     * JComponent.
     * @return The height that is available for content (height minus padding height) for the
     * provided JComponent.
     */
    public Distance getContentSpaceHeight(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final int contentSpaceHeightInPixels = this.getContentSpaceHeightInPixels(jComponent);
        final UIBase uiBase = this.getUIBase();
        final Distance result = contentSpaceHeightInPixels == 0
            ? Distance.zero
            : uiBase.convertVerticalPixelsToDistance(contentSpaceHeightInPixels);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the height that is available for content (height minus padding height) for this
     * SwingUIElementBase's JComponent in pixels.
     * @return The height that is available for content (height minus padding height) for this
     * SwingUIElementBase's JComponent in pixels.
     */
    public int getContentSpaceHeightInPixels()
    {
        return this.getContentSpaceHeightInPixels(this.getJComponent());
    }

    /**
     * Get the height that is available for content (height minus padding height) for the provided
     * JComponent in pixels.
     * @return The height that is available for content (height minus padding height) for the
     * provided JComponent in pixels.
     */
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
        final Disposable result = Disposable.create(() -> jTextComponent.getDocument().removeDocumentListener(documentListener));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public SwingUIElementBase setBackgroundColor(Color backgroundColor)
    {
        super.setBackgroundColor(backgroundColor);

        final javax.swing.JComponent jComponent = this.getJComponent();
        jComponent.setOpaque(backgroundColor.getAlphaComponent() != Color.ComponentMin);

        return this;
    }
}
