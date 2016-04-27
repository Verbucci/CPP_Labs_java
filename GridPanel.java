package com.ggl.game2048.view;

import java.awt.Graphics;
import javax.swing.JPanel;

import com.ggl.game2048.model.Game2048Model;

// this class draws the grid game panel
public class GridPanel extends JPanel {
  private static final long serialVersionUID = 4019841629547494495L;

  private Game2048Model model;
  private GameOverImage image;

  public GridPanel(Game2048Model model) {
    this.model = model;
    this.setPreferredSize(model.getPreferredSize());
    this.image = new GameOverImage(model);
    this.image.run();
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    model.draw(graphics);

    if (model.isGameOver()) {
      graphics.drawImage(image.getImage(), 0, 0, null);
    }
  }

}
