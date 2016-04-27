package com.ggl.game2048.controller;

import java.util.ArrayList;

import com.ggl.game2048.model.Cell;
import com.ggl.game2048.model.Game2048Model;

public class ReplayThread extends Thread {

  private Game2048Model model;
  ArrayList<Cell[][]> list;

  public ReplayThread(Game2048Model model, ArrayList<Cell[][]> list) {
    this.model = model;
    this.list = new ArrayList<Cell[][]>(list);
  }

  public void run() {

    if (list.isEmpty()) {
      System.err.println("the list is empty");
    }

    for (Cell[][] grid : list) {
      try {
        ReplayThread.sleep(1);
      } catch (InterruptedException exeption) {
        ReplayThread.currentThread().interrupt();
      }
      model.setGrid(grid);
    }
  }
}
