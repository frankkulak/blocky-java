package blocky.program.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import util.ButtonPanel;

/**
 * Acts as a start menu for the Blocky game.
 */
public class StartMenuPanel extends JPanel {
  private final int width, height;

  /**
   * TODO
   *
   * @param width  width of this panel
   * @param height height of this panel
   */
  public StartMenuPanel(int width, int height) {
    this.width = width;
    this.height = height;

    setFixedSize(this, width, height);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setOpaque(false);

    int btnWidth = (int) (width * 0.7);
    int btnHeight = (int) (btnWidth * 0.2);
    Color btnColor = new Color(35, 200, 125);

    JPanel playGameBtnPanel = new JPanel();
    ButtonPanel playGameBtn = new ButtonPanel("Play Game", btnWidth, btnHeight, btnColor);
    playGameBtnPanel.add(playGameBtn, BorderLayout.CENTER);
    setFixedSize(playGameBtnPanel, width, height / 4);

    JPanel settingsBtnPanel = new JPanel();
    ButtonPanel settingsBtn = new ButtonPanel("Settings", btnWidth, btnHeight, btnColor);
    settingsBtnPanel.add(settingsBtn, BorderLayout.CENTER);
    setFixedSize(settingsBtnPanel, width, height / 4);

    // todo add text
    this.add(playGameBtnPanel);
    this.add(settingsBtnPanel);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    AffineTransform ogTransform = g2d.getTransform();

    g2d.setColor(Color.BLACK);
    String fontName = g2d.getFont().getName();
    g.setFont(new Font(fontName, Font.PLAIN, 30));
    int textWidth = g2d.getFontMetrics().stringWidth("BLOCKY");
    g2d.drawString("BLOCKY", (width / 2) - (textWidth / 2), (height / 5));

    this.paintComponents(g);

    g2d.setTransform(ogTransform);
  }

  /**
   * Sets the given component to have the given width and height as a fixed size.
   *
   * @param comp   component to set a fixed size for
   * @param width  width to set for given component
   * @param height height to set for given component
   */
  private static void setFixedSize(Component comp, int width, int height) {
    comp.setMinimumSize(new Dimension(width, height));
    comp.setPreferredSize(new Dimension(width, height));
    comp.setMaximumSize(new Dimension(width, height));
  }
}
