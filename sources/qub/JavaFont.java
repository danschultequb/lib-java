package qub;

public class JavaFont implements Font
{
    private final java.awt.Font awtFont;

    public JavaFont(java.awt.Font awtFont)
    {
        PreCondition.assertNotNull(awtFont, "awtFont");

        this.awtFont = awtFont;
    }

    public java.awt.Font getAWTFont()
    {
        return awtFont;
    }
}
