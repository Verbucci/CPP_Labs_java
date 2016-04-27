package com.ggl.game2048.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.ggl.game2048.model.Game2048Model;

// draws the GAME OVER image in the end of the game
public class GameOverImage implements Runnable {

  private BufferedImage image;
  private Game2048Model model;

  public GameOverImage(Game2048Model model) {
    this.model = model;
  }

  @Override
  public void run() {
    String text = "Game Over";
    Dimension dimension = model.getPreferredSize();
    image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = image.createGraphics();

    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));

    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, dimension.width, dimension.height);

    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

    graphics.setColor(Color.RED);
    Font font = graphics.getFont();
    Font largeFont = font.deriveFont(Font.BOLD, 80);
    FontRenderContext fontRender = new FontRenderContext(null, true, true);
    Rectangle2D rectangle = largeFont.getStringBounds(text, fontRender);
    int rectangleWidth = (int) Math.round(rectangle.getWidth());
    int rectangleHeight = (int) Math.round(rectangle.getHeight());
    int rectangleX = (int) Math.round(rectangle.getX());
    int rectangleY = (int) Math.round(rectangle.getY());

    int coordinateX = (dimension.width / 2) - (rectangleWidth / 2) - rectangleX;
    int coordinateY = (dimension.height / 2) - (rectangleHeight / 2) - rectangleY;

    graphics.setFont(largeFont);
    graphics.drawString(text, coordinateX, coordinateY);

    graphics.dispose();

    model.setGameStarted(false);
  }

  public BufferedImage getImage() {
    return image;
  }

}
