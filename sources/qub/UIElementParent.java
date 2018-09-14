package qub;

public interface UIElementParent
{
    /**
     * Indicate that this UIElementParent should be repainted.
     */
    void repaint();

    /**
     * Get this UIElementParent's parent.
     * @return This UIElementParent's parent.
     */
    UIElementParent getParentElement();
}
