package qub;

/**
 * An object that can be used to build user interfaces.
 */
public interface UIBuilder
{
    /**
     * Create a new UIWindow.
     * @return The new UIWindow.
     */
    UIWindow createUIWindow();

    /**
     * Create a new UIButton.
     * @return The new UIButton.
     */
    UIButton createUIButton();

    /**
     * Create a new UIText.
     * @return The new UIText.
     */
    UIText createUIText();

    /**
     * Create a new UITextBox.
     * @return The new UITextBox.
     */
    UITextBox createUITextBox();

    /**
     * Create a new UIVerticalLayout.
     * @return The new UIVerticalLayout.
     */
    UIVerticalLayout createUIVerticalLayout();

    /**
     * Create a new UIHorizontalLayout.
     * @return The new UIHorizontalLayout.
     */
    UIHorizontalLayout createUIHorizontalLayout();
}
