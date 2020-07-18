package qub;

/**
 * A UIVerticalLayout that displays other SwingUIElements in a vertical stack.
 */
public class SwingUIVerticalLayout implements UIVerticalLayout, SwingUIElement
{
    private final SwingUIElementBase uiElementBase;
    private final javax.swing.JPanel jPanel;
    private final List<AWTUIElement> uiElements;
    private HorizontalAlignment elementHorizontalAlignment;
    private VerticalAlignment elementVerticalAlignment;

    private SwingUIVerticalLayout(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.jPanel = new javax.swing.JPanel(null);
        this.uiElements = List.create();
        this.uiElementBase = new SwingUIElementBase(uiBase, this.jPanel);
        this.elementHorizontalAlignment = HorizontalAlignment.Left;
        this.elementVerticalAlignment = VerticalAlignment.Top;

        this.onSizeChanged(() ->
        {
            this.jPanel.setPreferredSize(this.jPanel.getSize());
            this.updateElementPositions();
        });
    }

    public static SwingUIVerticalLayout create(SwingUIBase uiBase)
    {
        return new SwingUIVerticalLayout(uiBase);
    }

    @Override
    public javax.swing.JPanel getComponent()
    {
        return this.jPanel;
    }

    @Override
    public javax.swing.JPanel getJComponent()
    {
        return this.jPanel;
    }

    @Override
    public SwingUIVerticalLayout setWidth(Distance width)
    {
        this.uiElementBase.setWidth(width);
        return this;
    }

    @Override
    public SwingUIVerticalLayout setWidthInPixels(int widthInPixels)
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
    public SwingUIVerticalLayout setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    @Override
    public SwingUIVerticalLayout setHeightInPixels(int heightInPixels)
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
    public SwingUIVerticalLayout setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    @Override
    public SwingUIVerticalLayout setSize(Distance width, Distance height)
    {
        this.uiElementBase.setSize(width, height);
        return this;
    }

    @Override
    public SwingUIVerticalLayout setSizeInPixels(int widthInPixels, int heightInPixels)
    {
        this.uiElementBase.setSizeInPixels(widthInPixels, heightInPixels);
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
    public UIPaddingInPixels getPaddingInPixels()
    {
        return this.uiElementBase.getPaddingInPixels();
    }

    @Override
    public SwingUIVerticalLayout setPadding(UIPadding padding)
    {
        this.uiElementBase.setPadding(padding);
        return this;
    }

    @Override
    public SwingUIVerticalLayout setPaddingInPixels(UIPaddingInPixels padding)
    {
        this.uiElementBase.setPaddingInPixels(padding);
        return this;
    }

    @Override
    public Disposable onPaddingChanged(Action0 callback)
    {
        return this.uiElementBase.onPaddingChanged(callback);
    }

    @Override
    public Size2D getContentSpaceSize()
    {
        return null;
    }

    @Override
    public Distance getContentSpaceWidth()
    {
        return null;
    }

    @Override
    public int getContentSpaceWidthInPixels()
    {
        return 0;
    }

    @Override
    public Distance getContentSpaceHeight()
    {
        return null;
    }

    @Override
    public int getContentSpaceHeightInPixels()
    {
        return 0;
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
    public int getContentWidthInPixels()
    {
        return 0;
    }

    @Override
    public Distance getContentHeight()
    {
        return null;
    }

    @Override
    public int getContentHeightInPixels()
    {
        return 0;
    }

    @Override
    public SwingUIVerticalLayout setBackgroundColor(Color backgroundColor)
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
    public SwingUIVerticalLayout add(UIElement uiElement)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.add(uiElement);
    }

    @Override
    public SwingUIVerticalLayout addAll(UIElement... uiElements)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.addAll(uiElements);
    }

    @Override
    public SwingUIVerticalLayout addAll(Iterable<? extends UIElement> uiElements)
    {
        PreCondition.assertNotNull(uiElements, "uiElements");

        if (uiElements.any())
        {
            for (final UIElement uiElement : uiElements)
            {
                PreCondition.assertNotNull(uiElement, "uiElement");
                PreCondition.assertInstanceOf(uiElement, AWTUIElement.class, "uiElement");

                final AWTUIElement awtUiElement = (AWTUIElement)uiElement;
                this.uiElements.add(awtUiElement);
                this.jPanel.add(awtUiElement.getComponent());
            }

            this.updateElementPositions();
        }

        return this;
    }

    /**
     * Get the relative positions of the elements in this layout.
     * @return The relative positions of the elements in this layout.
     */
    public Map<AWTUIElement,Point2D> getElementPositions()
    {
        final MutableMap<AWTUIElement,Point2D> result = Map.create();

        for (final AWTUIElement awtUiElement : this.uiElements)
        {
            result.set(awtUiElement, this.uiElementBase.getPosition(awtUiElement.getComponent()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the relative position of the provided element, if the element exists in this layout.
     * @param awtUiElement The element to get the relative position of.
     * @return The relative position of the provided element.
     */
    public Result<Point2D> getElementPosition(AWTUIElement awtUiElement)
    {
        PreCondition.assertNotNull(awtUiElement, "awtUiElement");

        return Result.create(() ->
        {
            if (!this.uiElements.contains(awtUiElement))
            {
                throw new NotFoundException(this + " doesn't contain " + awtUiElement + ", so it can't get it's relative position.");
            }
            return this.uiElementBase.getPosition(awtUiElement);
        });
    }

    private void updateElementPositions()
    {
        final java.awt.Dimension jPanelContentSize = this.jPanel.getPreferredSize();
        final java.awt.Insets jPanelInsets = this.jPanel.getInsets();

        int jPanelContentWidth = 0;
        int jPanelContentHeight = 0;
        for (final AWTUIElement uiElement : this.uiElements)
        {
            final int uiElementWidthInPixels = uiElement.getWidthInPixels();
            if (uiElementWidthInPixels > jPanelContentWidth)
            {
                jPanelContentWidth = uiElementWidthInPixels;
            }

            jPanelContentHeight += uiElement.getHeightInPixels();
        }
        jPanelContentSize.width = jPanelContentWidth + jPanelInsets.left + jPanelInsets.right;
        jPanelContentSize.height = jPanelContentHeight + jPanelInsets.top + jPanelInsets.bottom;

        int componentY = jPanelInsets.top;
        for (final AWTUIElement uiElement : this.uiElements)
        {
            final int componentWidth = uiElement.getWidthInPixels();
            final int componentHeight = uiElement.getHeightInPixels();

            int componentX = jPanelInsets.left;
            switch (this.elementHorizontalAlignment)
            {
                case Center:
                    componentX += (int)((jPanelContentWidth / 2.0) - (componentWidth / 2.0));
                    break;

                case Right:
                    componentX += jPanelContentWidth - componentWidth;
                    break;
            }
            uiElement.getComponent().setLocation(componentX, componentY);

            componentY += componentHeight;
        }

        this.jPanel.setPreferredSize(jPanelContentSize);
        this.uiElementBase.updateSize();
    }

    @Override
    public SwingUIVerticalLayout setElementHorizontalAlignment(HorizontalAlignment elementHorizontalAlignment)
    {
        PreCondition.assertNotNull(elementHorizontalAlignment, "elementHorizontalAlignment");

        if (this.elementHorizontalAlignment != elementHorizontalAlignment)
        {
            this.elementHorizontalAlignment = elementHorizontalAlignment;
            this.updateElementPositions();
        }

        return this;
    }

    @Override
    public HorizontalAlignment getElementHorizontalAlignment()
    {
        return this.elementHorizontalAlignment;
    }

    @Override
    public SwingUIVerticalLayout setElementVerticalAlignment(VerticalAlignment elementVerticalAlignment)
    {
        PreCondition.assertNotNull(elementVerticalAlignment, "elementVerticalAlignment");

        if (this.elementVerticalAlignment != elementVerticalAlignment)
        {
            this.elementVerticalAlignment = elementVerticalAlignment;
            this.updateElementPositions();
        }

        return this;
    }

    @Override
    public VerticalAlignment getElementVerticalAlignment()
    {
        return this.elementVerticalAlignment;
    }
}
