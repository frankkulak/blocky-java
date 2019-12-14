package controls;

/**
 * Represents a listener of a ButtonPanel.
 */
public interface ButtonPanelListener {
  /**
   * Notifies this listener that the button with the given text was pressed.
   *
   * @param buttonText text of button that was pressed
   */
  void buttonPressed(String buttonText);
}
