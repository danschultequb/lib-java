package qub;

/**
 * A UITextBox that is implemented using a Swing JTextField.
 */
public class SwingUITextBox implements UITextBox, SwingUIElement
{
    private final SwingUIElementBase uiElementBase;
    private final javax.swing.JTextField jTextField;

    private SwingUITextBox(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.jTextField = new javax.swing.JTextField();
        this.uiElementBase = new SwingUIElementBase(uiBase, this.jTextField);
    }

    /**
     * Create a new SwingUITextBox.
     * @return The new SwingUITextBox.
     */
    public static SwingUITextBox create(SwingUIBase base)
    {
        return new SwingUITextBox(base);
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
        this.uiElementBase.setWidth(width);
        return this;
    }

    @Override
    public Distance getWidth()
    {
        return this.uiElementBase.getWidth();
    }

    @Override
    public SwingUITextBox setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    @Override
    public Distance getHeight()
    {
        return this.uiElementBase.getHeight();
    }

    @Override
    public SwingUITextBox setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    @Override
    public SwingUITextBox setSize(Distance width, Distance height)
    {
        this.uiElementBase.setSize(width, height);
        return this;
    }

    @Override
    public Size2D getSize()
    {
        return this.uiElementBase.getSize();
    }

    @Override
    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiElementBase.onSizeChanged(callback);
    }

    @Override
    public SwingUITextBox setBackgroundColor(Color backgroundColor)
    {
        this.uiElementBase.setBackgroundColor(backgroundColor);
        return this;
    }

    @Override
    public Color getBackgroundColor()
    {
        return this.uiElementBase.getBackgroundColor();
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

        return this.uiElementBase.onTextChanged(this.jTextField, callback);
    }

    @Override
    public Distance getFontSize()
    {
        return this.uiElementBase.getFontSize();
    }

    @Override
    public SwingUITextBox setFontSize(Distance fontSize)
    {
        this.uiElementBase.setFontSize(fontSize);
        return this;
    }
}
