package com.ggl.game2048.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Cell implements Serializable {
  /**
   * the serialVersionUID is necessary because
   * this class will be written to the replay file
   */
  private static final long serialVersionUID = -7794066972936428894L;
  private static final int CELL_WIDTH = 120;
  private int value;
  private Point cellLocation;

  public Cell(int value) {
    setValue(value);
  }

  public static int getCellWidth() {
    return CELL_WIDTH;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public boolean isZeroValue() {
    return (value == 0);
  }

  public void setCellLocation(int coordinateX, int coordinateY) {
    setCellLocation(new Point(coordinateX, coordinateY));
  }

  public void setCellLocation(Point cellLocation) {
    this.cellLocation = cellLocation;
  }

  public void draw(Graphics graphics) {
    if (value == 0) {
      graphics.setColor(Color.GRAY);
      graphics.fillRect(cellLocation.x, cellLocation.y, CELL_WIDTH, CELL_WIDTH);
    } else {
      Font font = graphics.getFont();
      FontRenderContext fontRender = new FontRenderContext(null, true, true);

      String text = Integer.toString(value);
      BufferedImage image = createImage(font, fontRender, CELL_WIDTH, text);

      graphics.drawImage(image, cellLocation.x, cellLocation.y, null);
    }
  }

  private BufferedImage createImage(Font font, FontRenderContext fontRender, int width,
      String text) {

    Font largeFont = font.deriveFont((float) (width / 4));
    Rectangle2D rectangle = largeFont.getStringBounds(text, fontRender);
    int rectangleWidth = (int) Math.round(rectangle.getWidth());
    int rectangleHeight = (int) Math.round(rectangle.getHeight());
    int rectangleX = (int) Math.round(rectangle.getX());
    int rectangleY = (int) Math.round(rectangle.getY());

    BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);

    Graphics draw = image.getGraphics();
    draw.setColor(getTileColor());
    draw.fillRect(0, 0, image.getWidth(), image.getHeight());

    int x = (width / 2) - (rectangleWidth / 2) - rectangleX;
    int y = (width / 2) - (rectangleHeight / 2) - rectangleY;

    draw.setFont(largeFont);
    draw.setColor(getTextColor());
    draw.drawString(text, x, y);
    draw.dispose();
    return image;
  }

  private Color getTileColor() {
    Color color = Color.WHITE;

    switch (value) {
      case 2:
        color = Color.WHITE;
        break;
      case 4:
        color = Color.WHITE;
        break;
      case 8:
        color = new Color(255, 255, 170);
        break;
      case 16:
        color = new Color(255, 255, 128);
        break;
      case 32:
        color = new Color(255, 255, 85);
        break;
      case 64:
        color = new Color(255, 255, 43);
        break;
      case 128:
        color = new Color(255, 255, 0);
        break;
      case 256:
        color = new Color(213, 213, 0);
        break;
      case 512:
        color = new Color(170, 170, 0);
        break;
      case 1024:
        color = new Color(128, 128, 0);
        break;
      case 2048:
        color = new Color(85, 85, 0);
        break;
      default:
        color = new Color(43, 43, 0);
        break;
    }

    return color;
  }

  private Color getTextColor() {
    return (value >= 256) ? Color.WHITE : Color.BLACK;
  }
}
