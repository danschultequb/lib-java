package qub;

public class UIElementToJComponentAdapter extends javax.swing.JComponent
{
    private final UIElement uiElement;

    public UIElementToJComponentAdapter(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        this.uiElement = uiElement;
    }

    @Override
    public void paint(java.awt.Graphics graphics)
    {
        PreCondition.assertNotNull(graphics, "graphics");
        PreCondition.assertTrue(Types.isSubTypeOf(graphics, java.awt.Graphics2D.class), "Types.isSubTypeOf(graphics, Graphics2D.class)");

        super.paint(graphics);

        final Graphics2DtoUIPainterAdapter painter = new Graphics2DtoUIPainterAdapter((java.awt.Graphics2D)graphics);
        uiElement.paint(painter);
    }
}
