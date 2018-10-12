package qub;

public class UIVerticalLayout implements UIElement, UIElementParent
{
    private final List<UIElement> childElements;
    private UIElementParent parentElement;
    private UIHeight height;

    public UIVerticalLayout()
    {
        this.childElements = new ArrayList<>();
    }

    @Override
    public void paint(UIPainter painter)
    {
        try (final Disposable savedTransform1 = painter.saveTransform())
        {
            for (final UIElement childElement : childElements)
            {
                try (final Disposable savedTransform2 = painter.saveTransform())
                {
                    childElement.paint(painter);
                }
                painter.translateY(childElement.getHeight());
            }
        }
    }

    @Override
    public void repaint()
    {
        final UIElementParent parentElement = getParentElement();
        if (parentElement != null)
        {
            parentElement.repaint();
        }
    }

    @Override
    public UIElementParent getParentElement()
    {
        return parentElement;
    }

    @Override
    public void setParent(UIElementParent parentElement)
    {
        this.parentElement = parentElement;
    }

    @Override
    public Window getParentWindow()
    {
        final UIElementParent parentElement = getParentElement();
        return parentElement == null ? null : parentElement.getParentWindow();
    }

    @Override
    public void parentWindowChanged(Window previousParentWindow, Window newParentWindow)
    {
        for (final UIElement childElement : childElements)
        {
            childElement.parentWindowChanged(previousParentWindow, newParentWindow);
        }
    }

    @Override
    public Distance getWidth()
    {
        return null;
    }

    @Override
    public void setWidth(Distance width)
    {

    }

    @Override
    public void setWidth(UIWidth width)
    {

    }

    @Override
    public Distance getHeight()
    {
        return height.getHeight(this);
    }

    @Override
    public void setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        setHeight(UIHeight.fixed(height));
    }

    @Override
    public void setHeight(UIHeight height)
    {
        PreCondition.assertNotNull(height, "height");

        this.height = height;
    }

    @Override
    public void setSize(Distance width, Distance height)
    {

    }

    public void add(UIElement element)
    {
        PreCondition.assertNotNull(element, "element");

        childElements.add(element);
    }
}
