package qub;

/**
 * A UITextBox that is implemented using a Swing JTextField.
 */
public class SwingUITextBox implements UITextBox, SwingUIElement
{
    private final SwingUIBase uiBase;
    private final javax.swing.JTextField jTextField;

    private SwingUITextBox(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;
        this.jTextField = new javax.swing.JTextField();
    }

    /**
     * Create a new SwingUITextBox.
     * @return The new SwingUITextBox.
     */
    public static SwingUITextBox create(SwingUIBase base)
    {
        return new SwingUITextBox(base);
    }

    /**
     * Create a new SwingUITextBox.
     * @param display The display that the SwingUITextBox will be shown on.
     * @param asyncRunner The AsyncRunner that this SwingUITextBox's events will be invoked on.
     * @return The new SwingUITextBox.
     */
    public static SwingUITextBox create(Display display, AsyncRunner asyncRunner)
    {
        return SwingUITextBox.create(SwingUIBase.create(display, asyncRunner));
    }

    @Override
    public javax.swing.JTextField getComponent()
    {
        return this.jTextField;
    }

    @Override
    public javax.swing.JTextField getJComponent()
    {
        return this.jTextField;
    }

    @Override
    public SwingUITextBox setWidth(Distance width)
    {
        return (SwingUITextBox)UITextBox.super.setWidth(width);
    }

    @Override
    public Distance getWidth()
    {
        return this.uiBase.getWidth(this);
    }

    @Override
    public SwingUITextBox setHeight(Distance height)
    {
        return (SwingUITextBox)UITextBox.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        return this.uiBase.getHeight(this);
    }

    @Override
    public SwingUITextBox setSize(Size2D size)
    {
        return (SwingUITextBox)UITextBox.super.setSize(size);
    }

    @Override
    public SwingUITextBox setSize(Distance width, Distance height)
    {
        this.uiBase.setSize(this, width, height);
        return this;
    }

    @Override
    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiBase.onSizeChanged(this, callback);
    }

    @Override
    public String getText()
    {
        return this.jTextField.getText();
    }

    @Override
    public SwingUITextBox setText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        this.jTextField.setText(text);

        return this;
    }

    @Override
    public Disposable onTextChanged(Action1<String> callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.uiBase.onTextChanged(this.jTextField, callback);
    }

    @Override
    public Distance getFontSize()
    {
        return this.uiBase.getFontSize(this);
    }

    @Override
    public SwingUITextBox setFontSize(Distance fontSize)
    {
        this.uiBase.setFontSize(this, fontSize);
        return this;
    }
}
