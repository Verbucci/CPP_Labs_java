package com.ggl.game2048.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import com.ggl.game2048.properties.SaveOrLoadGame;
import com.ggl.game2048.controller.ReplayThread;

// Main game algorithm, contains all the functions working with the grid
public class Game2048Model {

  private static final int FRAME_THICKNESS = 3;
  private int GRID_WIDTH = 8;
  private int STEP_COUNTER = 0;
  private int STEP_QUANTITY = 1;
  private boolean replayEnabled = false;
  private boolean botReplayEnabled = false;
  private boolean recordFilesEnable = false;
  private boolean gameStarted = false;
  private boolean arrowActive;
  private int highScore;
  private int highCell;
  private int currentScore;
  private int currentCell;
  private int filesQuantity = 10000;
  private int gameCounter = 0;
  private int fileForReplayID = 0;
  private Cell[][] grid;
  private Random random;
  private ArrayList<Cell[][]> stepList;
  private SaveOrLoadGame saveOrLoadGame;

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
    if (replayEnabled || botReplayEnabled) {
      STEP_COUNTER = 0;
      stepList = new ArrayList<Cell[][]>();
    }
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
    boolean actionSuccess = false;

    if (moveCellsUpLoop()) {
      actionSuccess = true;
    }

    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < (GRID_WIDTH - 1); j++) {
        int nextJ = j + 1;
        actionSuccess = combineCells(i, nextJ, i, j, actionSuccess);
      }
    }

    if (moveCellsUpLoop()) {
      actionSuccess = true;
    }

    return actionSuccess;
  }

  private boolean moveCellsUpLoop() {
    boolean actionSuccess = false;

    for (int i = 0; i < GRID_WIDTH; i++) {
      boolean columnDirty = false;
      do {
        columnDirty = false;
        for (int j = 0; j < (GRID_WIDTH - 1); j++) {
          int nextJ = j + 1;
          boolean cellDirty = moveCell(i, nextJ, i, j);
          if (cellDirty) {
            columnDirty = true;
            actionSuccess = true;
          }
        }
      } while (columnDirty);
    }

    return actionSuccess;
  }

  public boolean moveCellsDown() {
    boolean actionSuccess = false;

    if (moveCellsDownLoop()) {
      actionSuccess = true;
    }

    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = GRID_WIDTH - 1; j > 0; j--) {
        int previousJ = j - 1;
        actionSuccess = combineCells(i, previousJ, i, j, actionSuccess);
      }
    }

    if (moveCellsDownLoop()) {
      actionSuccess = true;
    }

    return actionSuccess;
  }

  private boolean moveCellsDownLoop() {
    boolean actionSuccess = false;

    for (int i = 0; i < GRID_WIDTH; i++) {
      boolean columnDirty = false;
      do {
        columnDirty = false;
        for (int j = GRID_WIDTH - 1; j > 0; j--) {
          int previousJ = j - 1;
          boolean cellDirty = moveCell(i, previousJ, i, j);
          if (cellDirty) {
            columnDirty = true;
            actionSuccess = true;
          }
        }
      } while (columnDirty);
    }

    return actionSuccess;
  }

  public boolean moveCellsLeft() {
    boolean actionSuccess = false;

    if (moveCellsLeftLoop()) {
      actionSuccess = true;
    }

    for (int j = 0; j < GRID_WIDTH; j++) {
      for (int i = 0; i < (GRID_WIDTH - 1); i++) {
        int nextI = i + 1;
        actionSuccess = combineCells(nextI, j, i, j, actionSuccess);
      }
    }

    if (moveCellsLeftLoop()) {
      actionSuccess = true;
    }

    return actionSuccess;
  }

  private boolean moveCellsLeftLoop() {
    boolean actionSuccess = false;

    for (int j = 0; j < GRID_WIDTH; j++) {
      boolean rowDirty = false;
      do {
        rowDirty = false;
        for (int i = 0; i < (GRID_WIDTH - 1); i++) {
          int nextI = i + 1;
          boolean cellDirty = moveCell(nextI, j, i, j);
          if (cellDirty) {
            rowDirty = true;
            actionSuccess = true;
          }
        }
      } while (rowDirty);
    }

    return actionSuccess;
  }

  public boolean moveCellsRight() {
    boolean actionSuccess = false;

    if (moveCellsRightLoop()) {
      actionSuccess = true;
    }
    for (int j = 0; j < GRID_WIDTH; j++) {
      for (int i = (GRID_WIDTH - 1); i > 0; i--) {
        int previousI = i - 1;
        actionSuccess = combineCells(previousI, j, i, j, actionSuccess);
      }
    }

    if (moveCellsRightLoop()) {
      actionSuccess = true;
    }

    return actionSuccess;
  }

  private boolean moveCellsRightLoop() {
    boolean actionSuccess = false;

    for (int j = 0; j < GRID_WIDTH; j++) {
      boolean rowDirty = false;
      do {
        rowDirty = false;
        for (int i = (GRID_WIDTH - 1); i > 0; i--) {
          int previousI = i - 1;
          boolean cellDirty = moveCell(previousI, j, i, j);
          if (cellDirty) {
            rowDirty = true;
            actionSuccess = true;
          }
        }
      } while (rowDirty);
    }

    return actionSuccess;
  }

  private boolean combineCells(int firstCellX, int firstCellY, int secondCellX, int secondCellY,
      boolean actionSuccess) {
    if (!grid[firstCellX][firstCellY].isZeroValue()) {
      int value = grid[firstCellX][firstCellY].getValue();
      if (grid[secondCellX][secondCellY].getValue() == value) {
        int newValue = value + value;
        grid[secondCellX][secondCellY].setValue(newValue);
        grid[firstCellX][firstCellY].setValue(0);
        updateScore(newValue, newValue);
        actionSuccess = true;
      }
    }
    return actionSuccess;
  }

  private boolean moveCell(int firstCellX, int firstCellY, int secondCellX, int secondCellY) {
    boolean actionSuccess = false;
    if (!grid[firstCellX][firstCellY].isZeroValue()
        && (grid[secondCellX][secondCellY].isZeroValue())) {
      int value = grid[firstCellX][firstCellY].getValue();
      grid[secondCellX][secondCellY].setValue(value);
      grid[firstCellX][firstCellY].setValue(0);
      actionSuccess = true;
    }
    return actionSuccess;
  }

  private void updateScore(int value, int cellValue) {
    currentScore += value;
    currentCell = (cellValue > currentCell) ? cellValue : currentCell;
  }

  public Dimension getPreferredSize() {
    int width = GRID_WIDTH * Cell.getCellWidth() + FRAME_THICKNESS * 5;
    return new Dimension(width, width);
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

  public boolean getBotReplayEnabled() {
    return botReplayEnabled;
  }

  public int getStepQuantity() {
    return STEP_QUANTITY;
  }

  public int getFilesQuantity() {
    return filesQuantity;
  }

  public int getWidth() {
    return GRID_WIDTH;
  }

  public void setGridWidth(int gridWidth) {
    GRID_WIDTH = gridWidth;
  }

  public void setCellWidth(int cellWidth) {
    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < GRID_WIDTH; j++) {
        grid[i][j].setCellWidth(cellWidth);
      }
    }
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

  public void setReplayEnabled(boolean replayEnabled) {
    this.replayEnabled = replayEnabled;
  }

  public boolean getRecordFilesEnabled() {
    return recordFilesEnable;
  }

  public void setCurrentGameCounter(int gameCounter) {
    this.gameCounter = gameCounter;
  }

  public int getCurrentGameCounter() {
    return gameCounter;
  }

  public void setFileForReplayID(int fileForReplayID) {
    this.fileForReplayID = fileForReplayID;
  }

  public int getFileForReplayID() {
    if (botReplayEnabled) {
      return 0;
    } else {
      return fileForReplayID;
    }
  }

  public void setGrid(Cell[][] grid) {
    this.grid = grid;
  }

  public void writeListToFile() {
    saveOrLoadGame = new SaveOrLoadGame(this);
    saveOrLoadGame.saveGame(stepList, gameCounter);
    stepList.clear();
  }

  public void draw(Graphics graphics) {
    graphics.setColor(Color.BLACK);
    Dimension dimension = getPreferredSize();

    graphics.fillRect(0, 0, dimension.width, dimension.height);

    for (int i = 0; i < GRID_WIDTH; i++) {
      for (int j = 0; j < GRID_WIDTH; j++) {
        grid[i][j].draw(graphics);
      }
    }

    if (replayEnabled) {
      saveCurrentSteps();
    }
  }

  public void saveCurrentSteps() {
    stepList.add(grid);
    STEP_COUNTER++;
    if (isGameOver()) {
      writeListToFile();
      STEP_QUANTITY = STEP_COUNTER;
      STEP_COUNTER = 0;
    }
  }

  public void replayGame() {
    saveOrLoadGame = new SaveOrLoadGame(this);
    stepList = saveOrLoadGame.loadGame(gameCounter);
    ReplayThread replay = new ReplayThread(this, stepList);
    replay.run();
  }

  public void printList() {
    if (stepList.isEmpty())
      System.out.println("empty printList");
    for (Cell[][] grid : stepList) {
      System.out.println(grid);
    }
  }

}
