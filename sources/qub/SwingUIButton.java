package qub;

/**
 * A UIButton that is implemented using a Swing JButton.
 */
public class SwingUIButton implements UIButton, SwingUIElement
{
    private final SwingUIElementBase uiElementBase;
    private final javax.swing.JButton jButton;

    private SwingUIButton(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.jButton = new javax.swing.JButton();
        this.uiElementBase = new SwingUIElementBase(uiBase, this.jButton);
    }

    /**
     * Create a new SwingUIButton.
     * @return The new SwingUIButton.
     */
    public static SwingUIButton create(SwingUIBase base)
    {
        return new SwingUIButton(base);
    }

    @Override
    public javax.swing.JButton getComponent()
    {
        return this.jButton;
    }

    @Override
    public javax.swing.JButton getJComponent()
    {
        return this.jButton;
    }

    @Override
    public SwingUIButton setWidth(Distance width)
    {
        this.uiElementBase.setWidth(width);
        return this;
    }

    @Override
    public SwingUIButton setWidthInPixels(int widthInPixels)
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
    public SwingUIButton setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    @Override
    public SwingUIButton setHeightInPixels(int heightInPixels)
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
    public SwingUIButton setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    @Override
    public SwingUIButton setSize(Distance width, Distance height)
    {
        this.uiElementBase.setSize(width, height);
        return this;
    }

    @Override
    public SwingUIButton setSizeInPixels(int widthInPixels, int heightInPixels)
    {
        this.uiElementBase.setSizeInPixels(widthInPixels, heightInPixels);
        return this;
    }

    @Override
    public SwingUIButton setBackgroundColor(Color backgroundColor)
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
    public SwingUIButton setPadding(UIPadding padding)
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
    public String getText()
    {
        return this.jButton.getText();
    }

    @Override
    public SwingUIButton setText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        this.jButton.setText(text);
        this.uiElementBase.updateSize();

        return this;
    }

    @Override
    public Distance getFontSize()
    {
        return this.uiElementBase.getFontSize();
    }

    @Override
    public SwingUIButton setFontSize(Distance fontSize)
    {
        this.uiElementBase.setFontSize(fontSize);
        return this;
    }

    @Override
    public Disposable onClick(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        final java.awt.event.ActionListener actionListener = (java.awt.event.ActionEvent actionEvent) ->
        {
            this.uiElementBase.scheduleAsyncTask(callback).await();
        };
        this.jButton.addActionListener(actionListener);
        return Disposable.create(() ->
        {
            this.jButton.removeActionListener(actionListener);
        });
    }

    /**
     * Perform a "click" event on this button.
     */
    public void click()
    {
        this.jButton.doClick();
    }
}
