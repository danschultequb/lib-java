package qub;

public class SwingUIText implements UIText, AWTUIElement
{
    private AWTUIBase uiBase;
    private final javax.swing.JLabel jLabel;

    private SwingUIText(AWTUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;
        this.jLabel = new javax.swing.JLabel();
    }

    public static SwingUIText create(AWTUIBase base)
    {
        return new SwingUIText(base);
    }

    public static SwingUIText create(Display display, AsyncRunner asyncRunner)
    {
        return SwingUIText.create(AWTUIBase.create(display, asyncRunner));
    }

    @Override
    public javax.swing.JLabel getComponent()
    {
        return this.jLabel;
    }

    @Override
    public SwingUIText setWidth(Distance width)
    {
        return (SwingUIText)UIText.super.setWidth(width);
    }

    @Override
    public Distance getWidth()
    {
        return this.uiBase.getWidth(this.jLabel);
    }

    @Override
    public SwingUIText setHeight(Distance height)
    {
        return (SwingUIText)UIText.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        return this.uiBase.getHeight(this.jLabel);
    }

    @Override
    public SwingUIText setSize(Size2D size)
    {
        return (SwingUIText)UIText.super.setSize(size);
    }

    @Override
    public SwingUIText setSize(Distance width, Distance height)
    {
        this.uiBase.setSize(this.jLabel, width, height);
        return this;
    }

    @Override
    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiBase.onSizeChanged(this.jLabel, callback);
    }

    @Override
    public String getText()
    {
        return this.jLabel.getText();
    }

    @Override
    public SwingUIText setText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        this.jLabel.setText(text);

        return this;
    }

    @Override
    public Distance getFontSize()
    {
        return this.uiBase.getFontSize(this.jLabel);
    }

    @Override
    public SwingUIText setFontSize(Distance fontSize)
    {
        this.uiBase.setFontSize(this.jLabel, fontSize);
        return this;
    }
}
