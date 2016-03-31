package com.ggl.game2048.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

// Main game algorithm, contains all the functions working with the grid
public class Game2048Model {

  private static final int FRAME_THICKNESS = 5;
  private static final int GRID_WIDTH = 4;

  private boolean gameStarted = false;
  private boolean arrowActive;
  private int highScore;
  private int highCell;
  private int currentScore;
  private int currentCell;
  private Cell[][] grid;
  private Random random;

  public Game2048Model() {
    this.grid = new Cell[GRID_WIDTH][GRID_WIDTH];
    this.random = new Random();
    this.highScore = 0;
    this.highCell = 0;
    this.currentScore = 0;
    this.currentCell = 0;
    this.arrowActive = false;
    initializeGrid();
  }

  public void initializeGrid() {
    int frameThicknessX = FRAME_THICKNESS;
    for (int i = 0; i < GRID_WIDTH; i++) {
      int frameThicknessY = FRAME_THICKNESS;
      for (int j = 0; j < GRID_WIDTH; j++) {
        Cell cell = new Cell(0);
        cell.setCellLocation(frameThicknessX, frameThicknessY);
        grid[i][j] = cell;
        frameThicknessY += FRAME_THICKNESS + Cell.getCellWidth();
      }
      frameThicknessX += FRAME_THICKNESS + Cell.getCellWidth();
    }
  }

  public void setHighScores() {
    highScore = (currentScore > highScore) ? currentScore : highScore;
    highCell = (currentCell > highCell) ? currentCell : highCell;
    currentScore = 0;
    currentCell = 0;
  }

  public boolean isGameOver() {
    return isGridFull() && !isMovePossible();
  }

  private boolean isGridFull() {
    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < GRID_WIDTH; j++) {
        if (grid[i][j].isZeroValue()) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isMovePossible() {
    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < (GRID_WIDTH - 1); j++) {
        int nextJ = j + 1;
        if (grid[i][j].getValue() == grid[i][nextJ].getValue()) {
          return true;
        }
      }
    }

    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < (GRID_WIDTH - 1); j++) {
        int nextJ = j + 1;
        if (grid[j][i].getValue() == grid[nextJ][i].getValue()) {
          return true;
        }
      }
    }
    return false;
  }

  public void addNewCell() {
    int value = (random.nextInt(10) < 9) ? 2 : 4;

    boolean locationFound = false;
    while (!locationFound) {
      int coordinateX = random.nextInt(GRID_WIDTH);
      int coordinateY = random.nextInt(GRID_WIDTH);
      if (grid[coordinateX][coordinateY].isZeroValue()) {
        grid[coordinateX][coordinateY].setValue(value);
        locationFound = true;
      }
    }
    updateScore(0, value);
  }

  public boolean moveCellsUp() {
    boolean dirty = false;

    if (moveCellsUpLoop()) {
      dirty = true;
    }
    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < (GRID_WIDTH - 1); j++) {
        int nextJ = j + 1;
        dirty = combineCells(i, nextJ, i, j, dirty);
      }
    }

    if (moveCellsUpLoop()) {
      dirty = true;
    }
    return dirty;
  }

  private boolean moveCellsUpLoop() {
    boolean dirty = false;

    for (int i = 0; i < GRID_WIDTH; i++) {
      boolean columnDirty = false;
      do {
        columnDirty = false;
        for (int j = 0; j < (GRID_WIDTH - 1); j++) {
          int nextJ = j + 1;
          boolean cellDirty = moveCell(i, nextJ, i, j);
          if (cellDirty) {
            columnDirty = true;
            dirty = true;
          }
        }
      } while (columnDirty);
    }

    return dirty;
  }

  public boolean moveCellsDown() {
    boolean dirty = false;

    if (moveCellsDownLoop()) {
      dirty = true;
    }
    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = GRID_WIDTH - 1; j > 0; j--) {
        int previousJ = j - 1;
        dirty = combineCells(i, previousJ, i, j, dirty);
      }
    }

    if (moveCellsDownLoop()) {
      dirty = true;
    }
    return dirty;
  }

  private boolean moveCellsDownLoop() {
    boolean dirty = false;

    for (int i = 0; i < GRID_WIDTH; i++) {
      boolean columnDirty = false;
      do {
        columnDirty = false;
        for (int j = GRID_WIDTH - 1; j > 0; j--) {
          int previousJ = j - 1;
          boolean cellDirty = moveCell(i, previousJ, i, j);
          if (cellDirty) {
            columnDirty = true;
            dirty = true;
          }
        }
      } while (columnDirty);
    }

    return dirty;
  }

  public boolean moveCellsLeft() {
    boolean dirty = false;

    if (moveCellsLeftLoop()) {
      dirty = true;
    }
    for (int j = 0; j < GRID_WIDTH; j++) {
      for (int i = 0; i < (GRID_WIDTH - 1); i++) {
        int nextI = i + 1;
        dirty = combineCells(nextI, j, i, j, dirty);
      }
    }

    if (moveCellsLeftLoop()) {
      dirty = true;
    }
    return dirty;
  }

  private boolean moveCellsLeftLoop() {
    boolean dirty = false;

    for (int j = 0; j < GRID_WIDTH; j++) {
      boolean rowDirty = false;
      do {
        rowDirty = false;
        for (int i = 0; i < (GRID_WIDTH - 1); i++) {
          int nextI = i + 1;
          boolean cellDirty = moveCell(nextI, j, i, j);
          if (cellDirty) {
            rowDirty = true;
            dirty = true;
          }
        }
      } while (rowDirty);
    }

    return dirty;
  }

  public boolean moveCellsRight() {
    boolean dirty = false;

    if (moveCellsRightLoop()) {
      dirty = true;
    }
    for (int j = 0; j < GRID_WIDTH; j++) {
      for (int i = (GRID_WIDTH - 1); i > 0; i--) {
        int previousI = i - 1;
        dirty = combineCells(previousI, j, i, j, dirty);
      }
    }

    if (moveCellsRightLoop()) {
      dirty = true;
    }
    return dirty;
  }

  private boolean moveCellsRightLoop() {
    boolean dirty = false;

    for (int j = 0; j < GRID_WIDTH; j++) {
      boolean rowDirty = false;
      do {
        rowDirty = false;
        for (int i = (GRID_WIDTH - 1); i > 0; i--) {
          int previousI = i - 1;
          boolean cellDirty = moveCell(previousI, j, i, j);
          if (cellDirty) {
            rowDirty = true;
            dirty = true;
          }
        }
      } while (rowDirty);
    }

    return dirty;
  }

  private boolean combineCells(int firstCellX, int firstCellY, int secondCellX, int secondCellY,
      boolean dirty) {
    if (!grid[firstCellX][firstCellY].isZeroValue()) {
      int value = grid[firstCellX][firstCellY].getValue();
      if (grid[secondCellX][secondCellY].getValue() == value) {
        int newValue = value + value;
        grid[secondCellX][secondCellY].setValue(newValue);
        grid[firstCellX][firstCellY].setValue(0);
        updateScore(newValue, newValue);
        dirty = true;
      }
    }
    return dirty;
  }

  private boolean moveCell(int firstCellX, int firstCellY, int secondCellX, int secondCellY) {
    boolean dirty = false;
    if (!grid[firstCellX][firstCellY].isZeroValue()
        && (grid[secondCellX][secondCellY].isZeroValue())) {
      int value = grid[firstCellX][firstCellY].getValue();
      grid[secondCellX][secondCellY].setValue(value);
      grid[firstCellX][firstCellY].setValue(0);
      dirty = true;
    }
    return dirty;
  }

  private void updateScore(int value, int cellValue) {
    currentScore += value;
    currentCell = (cellValue > currentCell) ? cellValue : currentCell;
  }

  public Cell getCell(int coordinateX, int coordinateY) {
    return grid[coordinateX][coordinateY];
  }

  public int getHighScore() {
    return highScore;
  }

  public int getHighCell() {
    return highCell;
  }

  public void setHighScore(int highScore) {
    this.highScore = highScore;
  }

  public void setHighCell(int highCell) {
    this.highCell = highCell;
  }

  public int getCurrentScore() {
    return currentScore;
  }

  public int getCurrentCell() {
    return currentCell;
  }

  public boolean isArrowActive() {
    return arrowActive;
  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public void setArrowActive(boolean arrowActive) {
    this.arrowActive = arrowActive;
  }

  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }

  public Dimension getPreferredSize() {
    int width = GRID_WIDTH * Cell.getCellWidth() + FRAME_THICKNESS * 5;
    return new Dimension(width, width);
  }

  public void draw(Graphics graphics) {
    graphics.setColor(Color.DARK_GRAY);
    Dimension d = getPreferredSize();
    graphics.fillRect(0, 0, d.width, d.height);

    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < GRID_WIDTH; j++) {
        grid[i][j].draw(graphics);
      }
    }
  }
}
