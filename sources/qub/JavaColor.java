package qub;

public class JavaColor implements Color
{
    private final java.awt.Color awtColor;

    public JavaColor(java.awt.Color awtColor)
    {
        PreCondition.assertNotNull(awtColor, "awtColor");

        this.awtColor = awtColor;
    }

    public java.awt.Color getAWTColor()
    {
        return awtColor;
    }
}
