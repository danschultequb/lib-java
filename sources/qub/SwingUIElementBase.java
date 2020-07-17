package qub;

public class SwingUIElementBase extends AWTUIElementBase
{
    private final RunnableEvent2<UIPadding,UIPadding> paddingChanged;

    public SwingUIElementBase(SwingUIBase uiBase, javax.swing.JComponent jComponent)
    {
        super(uiBase, jComponent);

        this.paddingChanged = Event2.create();
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

    public SwingUIElementBase setPadding(UIPadding padding)
    {
        final SwingUIBase uiBase = this.getUIBase();
        final javax.swing.JComponent jComponent = this.getJComponent();
        final UIPadding oldPadding = uiBase.getPadding(jComponent);
        if (!oldPadding.equals(padding))
        {
            uiBase.setPadding(jComponent, padding);
            this.paddingChanged.run(oldPadding, padding);
        }
        return this;
    }

    /**
     * Subscribe to be notified when this SwingUIElementBase's padding changes.
     * @param callback The callback that will be invoked when this SwingUIElementBase's padding changes.
     * @return A Disposable that can be disposed to unsubscribe the provided callback.
     */
    public Disposable onPaddingChanged(Action2<UIPadding,UIPadding> callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.paddingChanged.subscribe(callback);
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
