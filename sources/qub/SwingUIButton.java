package qub;

/**
 * A UIButton that is implemented using a Swing JButton.
 */
public class SwingUIButton implements UIButton, SwingUIElement
{
    private final javax.swing.JButton jButton;
    private final Display display;
    private final AsyncRunner asyncRunner;

    private SwingUIButton(Display display, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.jButton = new javax.swing.JButton();
        this.display = display;
        this.asyncRunner = asyncRunner;
    }

    /**
     * Create a new SwingUIButton.
     * @param display The display that the SwingUIButton will be shown on.
     * @param asyncRunner The AsyncRunner that this SwingUIButton's events will be invoked on.
     * @return The new SwingUIButton.
     */
    public static SwingUIButton create(Display display, AsyncRunner asyncRunner)
    {
        return new SwingUIButton(display, asyncRunner);
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
        final int widthInPixels = this.getJComponent().getWidth();
        final Distance result = this.display.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIButton setHeight(Distance height)
    {
        return (SwingUIButton)UIButton.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        final int heightInPixels = this.getJComponent().getHeight();
        final Distance result = this.display.convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIButton setSize(Size2D size)
    {
        return (SwingUIButton)UIButton.super.setSize(size);
    }

    @Override
    public SwingUIButton setSize(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final double widthInPixels = this.display.convertHorizontalDistanceToPixels(width);
        final double heightInPixels = this.display.convertVerticalDistanceToPixels(height);
        this.getJComponent().setSize((int)widthInPixels, (int)heightInPixels);

        return this;
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
        final float fontSize2D = this.jButton.getFont().getSize2D();
        final Distance result = Distance.fontPoints(fontSize2D);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIButton setFontSize(Distance fontSize)
    {
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        final java.awt.Font font = this.jButton.getFont();
        final float fontPoints = (float)fontSize.toFontPoints().getValue();
        final java.awt.Font updatedFont = font.deriveFont(fontPoints);
        this.jButton.setFont(updatedFont);

        return this;
    }

    @Override
    public Disposable onClick(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        final java.awt.event.ActionListener actionListener = (java.awt.event.ActionEvent actionEvent) ->
        {
            callback.run();
        };
        this.jButton.addActionListener(actionListener);
        return BasicDisposable.create(() ->
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
