package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Acts as a modern looking button.
 */
public class ButtonPanel extends JPanel implements MouseListener, ActionListener {
  private final String text;
  private final int width, height, shadowOffset;
  private final Color color;
  private ButtonPanelListener listener; // INVARIANT: never null once set

  // fields for animation
  private final Timer timer;
  private boolean pushingDown;
  private int buttonHeight;
  private Color colorToUse;

  /**
   * Constructs a new ButtonPanel using the given specifications.
   *
   * @param text   text to render on button
   * @param width  width of button
   * @param height height of button
   * @param color  color of button in neutral state
   */
  public ButtonPanel(String text, int width, int height, Color color) {
    this.setPreferredSize(new Dimension(width, height));

    this.text = text;
    this.width = width;
    this.shadowOffset = (int) (height * 0.15);
    this.height = height - shadowOffset;
    this.color = color;

    this.timer = new Timer(5, this);
    this.buttonHeight = 0;
    this.colorToUse = color;

    this.addMouseListener(this);
  }

  /**
   * Sets the listener of this button panel.
   *
   * @param listener listener of this button panel
   * @throws IllegalArgumentException if given listener is null
   */
  public void setListener(ButtonPanelListener listener) throws IllegalArgumentException {
    if (listener == null) throw new IllegalArgumentException("Cannot set null listener.");
    this.listener = listener;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    AffineTransform ogTransform = g2d.getTransform();

    int arcSize = (int) (width * 0.1);

    // filling shadow
    g2d.setColor(colorToUse.darker());
    g2d.fillRoundRect(0, shadowOffset, width, height, arcSize, arcSize);

    // filling button
    g2d.setColor(colorToUse);
    g2d.fillRoundRect(0, buttonHeight, width, height, arcSize, arcSize);

    // adding text
    g2d.setColor(Color.WHITE);
    String fontName = g2d.getFont().getName();
    g.setFont(new Font(fontName, Font.PLAIN, 12));
    int textWidth = g2d.getFontMetrics().stringWidth(this.text);
    int textHeight = (int) g2d.getFontMetrics().getStringBounds(this.text, g2d).getHeight();
    g2d.drawString(this.text, (width / 2) - (textWidth / 2),
            (height / 2) + (textHeight / 2) - shadowOffset + buttonHeight);

    g2d.setTransform(ogTransform);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.wasPressed();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.pushingDown = true;
    this.timer.start();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    this.pushingDown = false;
    this.timer.start();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    this.colorToUse = color.brighter();
    this.repaint();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    this.colorToUse = color;
    this.repaint();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (pushingDown) {
      if (this.buttonHeight == this.shadowOffset) {
        this.timer.stop();
      } else {
        this.buttonHeight++;
        this.repaint();
      }
    } else {
      if (this.buttonHeight == 0) {
        this.timer.stop();
        this.wasPressed();
      } else {
        this.buttonHeight--;
        this.repaint();
      }
    }
  }

  /**
   * Notifies listener that this button was pressed.
   */
  private void wasPressed() {
    this.listener.buttonPressed(this.text);
  }
}
