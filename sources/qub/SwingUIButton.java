package qub;

/**
 * A UIButton that is implemented using a Swing JButton.
 */
public class SwingUIButton implements UIButton, SwingUIElement
{
    private final AWTUIBase uiBase;
    private final javax.swing.JButton jButton;

    private SwingUIButton(AWTUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;
        this.jButton = new javax.swing.JButton();
    }

    /**
     * Create a new SwingUIButton.
     * @return The new SwingUIButton.
     */
    public static SwingUIButton create(AWTUIBase base)
    {
        return new SwingUIButton(base);
    }

    /**
     * Create a new SwingUIButton.
     * @param display The display that the SwingUIButton will be shown on.
     * @param asyncRunner The AsyncRunner that this SwingUIButton's events will be invoked on.
     * @return The new SwingUIButton.
     */
    public static SwingUIButton create(Display display, AsyncRunner asyncRunner)
    {
        return SwingUIButton.create(AWTUIBase.create(display, asyncRunner));
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
        return (SwingUIButton)UIButton.super.setWidth(width);
    }

    @Override
    public Distance getWidth()
    {
        return this.uiBase.getWidth(this);
    }

    @Override
    public SwingUIButton setHeight(Distance height)
    {
        return (SwingUIButton)UIButton.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        return this.uiBase.getHeight(this);
    }

    @Override
    public SwingUIButton setSize(Size2D size)
    {
        return (SwingUIButton)UIButton.super.setSize(size);
    }

    @Override
    public SwingUIButton setSize(Distance width, Distance height)
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
        return this.jButton.getText();
    }

    @Override
    public SwingUIButton setText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        this.jButton.setText(text);

        return this;
    }

    @Override
    public Distance getFontSize()
    {
        return this.uiBase.getFontSize(this);
    }

    @Override
    public SwingUIButton setFontSize(Distance fontSize)
    {
        this.uiBase.setFontSize(this, fontSize);
        return this;
    }

    @Override
    public Disposable onClick(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        final java.awt.event.ActionListener actionListener = (java.awt.event.ActionEvent actionEvent) ->
        {
            this.uiBase.scheduleAsyncTask(callback).await();
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
