package qub;

public class SwingUIElementBase extends AWTUIElementBase
{
    public SwingUIElementBase(SwingUIBase uiBase, javax.swing.JComponent jComponent)
    {
        super(uiBase, jComponent);
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

    public void setPadding(UIPadding padding)
    {
        this.getUIBase().setPadding(this.getJComponent(), padding);
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
