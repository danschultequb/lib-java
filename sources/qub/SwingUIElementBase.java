package qub;

public class SwingUIElementBase extends AWTUIElementBase
{
    private final RunnableEvent0 paddingChanged;

    public SwingUIElementBase(SwingUIBase uiBase, javax.swing.JComponent jComponent)
    {
        super(uiBase, jComponent);

        this.paddingChanged = Event0.create();
    }

    @Override
    protected SwingUIBase getUIBase()
    {
        return (SwingUIBase)super.getUIBase();
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

    public UIPadding getPadding()
    {
        return this.getUIBase().getPadding(this.getJComponent());
    }

    public UIPaddingInPixels getPaddingInPixels()
    {
        return this.getUIBase().getPaddingInPixels(this.getJComponent());
    }

    public SwingUIElementBase setPadding(UIPadding padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return this.setPaddingInPixels(this.getUIBase().convertUIPaddingToUIPaddingInPixels(padding));
    }

    public SwingUIElementBase setPaddingInPixels(UIPaddingInPixels padding)
    {
        final SwingUIBase uiBase = this.getUIBase();
        final javax.swing.JComponent jComponent = this.getJComponent();
        final UIPaddingInPixels oldPadding = uiBase.getPaddingInPixels(jComponent);
        if (!oldPadding.equals(padding))
        {
            uiBase.setPaddingInPixels(jComponent, padding);
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

    public Size2D getContentSpaceSize()
    {
        return this.getContentSpaceSize(this.getJComponent());
    }

    public Size2D getContentSpaceSize(javax.swing.JComponent jComponent)
    {
        return this.getUIBase().getContentSpaceSize(jComponent);
    }

    public Distance getContentSpaceWidth()
    {
        return this.getContentSpaceWidth(this.getJComponent());
    }

    public Distance getContentSpaceWidth(javax.swing.JComponent jComponent)
    {
        return this.getUIBase().getContentSpaceWidth(jComponent);
    }

    public Distance getContentSpaceHeight()
    {
        return this.getContentSpaceHeight(this.getJComponent());
    }

    public Distance getContentSpaceHeight(javax.swing.JComponent jComponent)
    {
        return this.getUIBase().getContentSpaceHeight(jComponent);
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
        return this.getUIBase().onTextChanged(jTextComponent, callback);
    }
}
