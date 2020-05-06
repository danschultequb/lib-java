package qub;

public class SwingUIText implements UIText, SwingUIElement
{
    private final javax.swing.JLabel jLabel;
    private final Display display;

    private SwingUIText(Display display)
    {
        PreCondition.assertNotNull(display, "display");

        this.display = display;
        this.jLabel = new javax.swing.JLabel();
    }

    public static SwingUIText create(Display display)
    {
        return new SwingUIText(display);
    }

    @Override
    public javax.swing.JLabel getJComponent()
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
        final int widthInPixels = this.getJComponent().getWidth();
        final Distance result = this.display.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIText setHeight(Distance height)
    {
        return (SwingUIText)UIText.super.setHeight(height);
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
    public SwingUIText setSize(Size2D size)
    {
        return (SwingUIText)UIText.super.setSize(size);
    }

    @Override
    public SwingUIText setSize(Distance width, Distance height)
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
        final float fontSize2D = this.jLabel.getFont().getSize2D();
        final Distance result = Distance.fontPoints(fontSize2D);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIText setFontSize(Distance fontSize)
    {
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        final java.awt.Font font = this.jLabel.getFont();
        final float fontPoints = (float)fontSize.toFontPoints().getValue();
        final java.awt.Font updatedFont = font.deriveFont(fontPoints);
        this.jLabel.setFont(updatedFont);

        return this;
    }
}
