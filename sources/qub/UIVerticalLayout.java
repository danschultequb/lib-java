package qub;

public class UIVerticalLayout implements UIElement, UIElementParent
{
    private final List<UIElement> childElements;
    private UIElementParent parentElement;
    private UIWidth width;
    private UIHeight height;
    private boolean calculatingSize;
    private Distance padding;

    public UIVerticalLayout()
    {
        this.childElements = new ArrayList<>();
    }

    @Override
    public void paint(UIPainter painter)
    {
        try (final Disposable savedTransform1 = painter.saveTransform())
        {
            final Distance padding = getPadding();
            if (padding != null && !padding.equals(Distance.zero))
            {
                painter.translate(padding, padding);
            }

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
    public Distance getPadding()
    {
        return padding;
    }

    @Override
    public UIElement setPadding(Distance padding)
    {
        PreCondition.assertNullOrGreaterThanOrEqualTo(padding, Distance.zero, "padding");

        this.padding = padding;

        return this;
    }

    @Override
    public Distance getWidth()
    {
        return width.getWidth(this);
    }

    @Override
    public UIVerticalLayout setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");

        return setWidth(UIWidth.fixed(width));
    }

    @Override
    public UIVerticalLayout setWidth(UIWidth width)
    {
        PreCondition.assertNotNull(width, "width");

        this.width = width;

        return this;
    }

    @Override
    public Distance getHeight()
    {
        return height.getHeight(this);
    }

    @Override
    public UIVerticalLayout setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        return setHeight(UIHeight.fixed(height));
    }

    @Override
    public UIVerticalLayout setHeight(UIHeight height)
    {
        PreCondition.assertNotNull(height, "height");

        this.height = height;

        return this;
    }

    @Override
    public UIVerticalLayout setSize(Distance width, Distance height)
    {
        setWidth(width);
        setHeight(height);

        return this;
    }

    @Override
    public UIElement setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return setSize(size.getWidth(), size.getHeight());
    }

    @Override
    public Distance getContentWidth()
    {
        final Distance result = getContentSize().getWidth();

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public Distance getContentHeight()
    {
        final Distance result = getContentSize().getHeight();

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public Size2D getContentSize()
    {
        Distance resultWidth = Distance.zero;
        Distance resultHeight = Distance.zero;
        for (final UIElement childElement : childElements)
        {
            resultWidth = Comparer.maximum(resultWidth, childElement.getWidth());
            resultHeight = resultHeight.plus(childElement.getHeight());
        }
        final Size2D result = new Size2D(resultWidth, resultHeight);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public UIVerticalLayout add(UIElement element)
    {
        PreCondition.assertNotNull(element, "element");

        childElements.add(element);

        return this;
    }
}
