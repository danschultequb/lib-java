package qub;

/**
 * A UIText implementation that is based on a javax.swing.JLabel.
 */
public class SwingUIText implements UIText, SwingUIElement
{
    private final SwingUIElementBase uiElementBase;
    private final javax.swing.JLabel jLabel;

    private SwingUIText(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.jLabel = new javax.swing.JLabel();
        this.uiElementBase = new SwingUIElementBase(uiBase, this.jLabel);
    }

    public static SwingUIText create(SwingUIBase base)
    {
        return new SwingUIText(base);
    }

    @Override
    public javax.swing.JLabel getComponent()
    {
        return this.jLabel;
    }

    @Override
    public javax.swing.JLabel getJComponent()
    {
        return this.jLabel;
    }

    @Override
    public SwingUIText setWidth(Distance width)
    {
        this.uiElementBase.setWidth(width);
        return this;
    }

    @Override
    public SwingUIText setWidthInPixels(int widthInPixels)
    {
        this.uiElementBase.setWidthInPixels(widthInPixels);
        return this;
    }

    @Override
    public Distance getWidth()
    {
        return this.uiElementBase.getWidth();
    }

    @Override
    public SwingUIText setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    @Override
    public SwingUIText setHeightInPixels(int heightInPixels)
    {
        this.uiElementBase.setHeightInPixels(heightInPixels);
        return this;
    }

    @Override
    public Distance getHeight()
    {
        return this.uiElementBase.getHeight();
    }

    @Override
    public SwingUIText setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    @Override
    public SwingUIText setSize(Distance width, Distance height)
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
    public UIPadding getPadding()
    {
        return this.uiElementBase.getPadding();
    }

    @Override
    public SwingUIText setPadding(UIPadding padding)
    {
        this.uiElementBase.setPadding(padding);
        return this;
    }

    @Override
    public Disposable onPaddingChanged(Action2<UIPadding, UIPadding> callback)
    {
        return this.uiElementBase.onPaddingChanged(callback);
    }

    @Override
    public Size2D getContentSpaceSize()
    {
        return null;
    }

    @Override
    public Size2D getContentSpaceWidth()
    {
        return null;
    }

    @Override
    public Size2D getContentSpaceHeight()
    {
        return null;
    }

    @Override
    public Size2D getContentSize()
    {
        return null;
    }

    @Override
    public Distance getContentWidth()
    {
        return null;
    }

    @Override
    public Distance getContentHeight()
    {
        return null;
    }

    @Override
    public SwingUIText setBackgroundColor(Color backgroundColor)
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
        return this.jLabel.getText();
    }

    @Override
    public SwingUIText setText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        this.jLabel.setText(text);
        this.uiElementBase.updateSize();

        return this;
    }

    @Override
    public Distance getFontSize()
    {
        return this.uiElementBase.getFontSize();
    }

    @Override
    public SwingUIText setFontSize(Distance fontSize)
    {
        this.uiElementBase.setFontSize(fontSize);

        return this;
    }
}
