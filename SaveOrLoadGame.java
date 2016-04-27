package com.ggl.game2048.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.ggl.game2048.model.Cell;
import com.ggl.game2048.model.Game2048Model;

// this class provides the file i/o functions to read and store the records
public class SaveOrLoadGame {

  private static final String fileName = "savedGame.txt";
  private Game2048Model model;

  public SaveOrLoadGame(Game2048Model model) {
    this.model = model;
  }

  public ArrayList<Cell[][]> loadGame() {
    File file = new File(fileName);
    ArrayList<Cell[][]> stepList = new ArrayList<Cell[][]>();

    try (ObjectInputStream gameStream = new ObjectInputStream(new FileInputStream(file))) {

      Cell[][] grid = new Cell[model.getWidth()][model.getWidth()];

      for (int i = 0; i < model.getStepQuantity(); i++) {
        try {
          grid = (Cell[][]) gameStream.readObject();
        } catch (ClassNotFoundException classExeption) {
          System.err.println("Error! loadGame: class not found");
          classExeption.printStackTrace();
        }

        stepList.add(grid.clone());
      }

    } catch (FileNotFoundException fileExeption) {
      System.err.println("Error! loadGame: can't open file");
    } catch (IOException ioExeption) {
      System.err.println("Error! loadGame: IO error, can't read");
    }

    if (stepList.isEmpty())
      System.out.println("empty printList");
    for (Cell[][] grid : stepList) {
      System.out.println(grid);
    }
    return stepList;
  }

  public void saveGame(ArrayList<Cell[][]> stepList) {
    File file = new File(fileName);
    FileOutputStream fileOutStream;
    try {
      fileOutStream = new FileOutputStream(file, false);

      try (ObjectOutputStream gameStateStream = new ObjectOutputStream(fileOutStream)) {

        if (stepList.isEmpty()) {
          System.err.println("Error! saveGame: StepList is empty");
          return;
        }

        for (Cell[][] grid : stepList) {
          gameStateStream.writeObject(grid);
        }

        gameStateStream.close();
      } catch (FileNotFoundException fileExeption) {
        fileExeption.printStackTrace();
        System.err.println("Error! saveGame: can't save to file");
        return;
      } catch (IOException ioExeption) {
        ioExeption.printStackTrace();
        System.err.println("Error! saveGame: IO error");
        return;
      }
    } catch (FileNotFoundException fileExeption) {
      fileExeption.printStackTrace();
    }
  }

}
