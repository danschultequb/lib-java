package qub;

import javax.swing.*;

/**
 * A UIHorizontalLayout that displays other SwingUIElements in a horizontal stack.
 */
public class SwingUIHorizontalLayout implements UIHorizontalLayout, SwingUIElement
{
    private final javax.swing.JPanel jPanel;
    private final SwingUIElementBase uiElementBase;
    private final List<AWTUIElement> elements;

    private SwingUIHorizontalLayout(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.jPanel = new javax.swing.JPanel();
        this.jPanel.setLayout(null);
        this.uiElementBase = new SwingUIElementBase(uiBase, this.jPanel);
        this.elements = List.create();
    }

    public static SwingUIHorizontalLayout create(SwingUIBase uiBase)
    {
        return new SwingUIHorizontalLayout(uiBase);
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
    public SwingUIHorizontalLayout setWidth(Distance width)
    {
        this.uiElementBase.setWidth(width);
        return this;
    }

    @Override
    public Distance getWidth()
    {
        return this.uiElementBase.getWidth();
    }

    @Override
    public SwingUIHorizontalLayout setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    @Override
    public Distance getHeight()
    {
        return this.uiElementBase.getHeight();
    }

    @Override
    public SwingUIHorizontalLayout setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    @Override
    public SwingUIHorizontalLayout setSize(Distance width, Distance height)
    {
        this.uiElementBase.setSize(width, height);
        return this;
    }

    @Override
    public SwingUIHorizontalLayout setBackgroundColor(Color backgroundColor)
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
    public SwingUIHorizontalLayout add(UIElement uiElement)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.add(uiElement);
    }

    @Override
    public SwingUIHorizontalLayout addAll(UIElement... uiElements)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.addAll(uiElements);
    }

    @Override
    public SwingUIHorizontalLayout addAll(Iterable<? extends UIElement> uiElements)
    {
        PreCondition.assertNotNull(uiElements, "uiElements");

        if (uiElements.any())
        {
            final List<java.awt.Component> components = List.create();
            for (final UIElement uiElement : uiElements)
            {
                PreCondition.assertNotNull(uiElement, "uiElement");
                PreCondition.assertInstanceOf(uiElement, AWTUIElement.class, "uiElement");

                final AWTUIElement awtUiElement = (AWTUIElement)uiElement;
                this.elements.add(awtUiElement);

                components.add(awtUiElement.getComponent());
            }

            final java.awt.Dimension jPanelPreferredSize = this.jPanel.getPreferredSize();
            final java.awt.Insets jPanelInsets = this.jPanel.getInsets();

            int jPanelContentHeight = jPanelPreferredSize.height - jPanelInsets.top - jPanelInsets.bottom;
            for (final java.awt.Component component : components)
            {
                final int componentHeight = component.getHeight();
                if (componentHeight > jPanelContentHeight)
                {
                    jPanelContentHeight = componentHeight;
                }
            }
            jPanelPreferredSize.height = jPanelContentHeight + jPanelInsets.left + jPanelInsets.right;

            final int componentY = jPanelInsets.top;
            int componentX = jPanelPreferredSize.width - jPanelInsets.right;
            for (final java.awt.Component component : components)
            {
                this.jPanel.add(component);

                component.setLocation(componentX, componentY);

                componentX += component.getWidth();
            }
            jPanelPreferredSize.width = componentX + jPanelInsets.right;

            this.jPanel.setPreferredSize(jPanelPreferredSize);
            this.uiElementBase.updateSize();
        }

        return this;
    }
}
