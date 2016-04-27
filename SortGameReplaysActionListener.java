package com.ggl.game2048.controller;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.ggl.game2048.model.Cell;
import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.model.ScalaSort;
import com.ggl.game2048.properties.SaveOrLoadGame;
import com.ggl.game2048.view.Game2048Frame;

// acts after the "start game" button is pressed
public class SortGameReplaysActionListener implements ActionListener {

  private Game2048Frame frame;
  private Game2048Model model;
  private long sortTime = 0;
  private int sortRangeFrom = 0;
  private int sortRangeTo = 0;
  private int currentBestScore = 0;
  private ArrayList<Cell[][]> stepList;
  private SaveOrLoadGame saveOrLoadGame;

  public SortGameReplaysActionListener(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    JFrame sortFrame = new JFrame("Sort Replays");

    JPanel sortPanel = new JPanel();
    sortPanel.setLayout(new FlowLayout());

    JLabel rangeLabel = new JLabel("Enter sorting range: from ");
    sortPanel.add(rangeLabel);

    JTextArea rangeFromArea = new JTextArea();
    rangeFromArea.setColumns(5);
    sortPanel.add(rangeFromArea);

    JLabel toLabel = new JLabel("to");
    sortPanel.add(toLabel);

    JTextArea rangeToArea = new JTextArea();
    rangeToArea.setColumns(5);
    sortPanel.add(rangeToArea);

    JLabel modeLabel = new JLabel("Choose sorting mode: ");
    sortPanel.add(modeLabel);

    String[] modes = {"Java", "Scala"};
    JComboBox<String> modeBox = new JComboBox<String>(modes);
    sortPanel.add(modeBox);

    JButton OKButton = new JButton("OK");
    sortPanel.add(OKButton);

    JLabel resultLabel = new JLabel("Best replay score [0, 0]: ");
    sortPanel.add(resultLabel);

    sortFrame.setSize(350, 130);
    sortFrame.add(sortPanel);
    sortFrame.setLocationByPlatform(true);
    sortFrame.setVisible(true);

    OKButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (!rangeFromArea.getText().trim().equals("")) {
          if (!rangeToArea.getText().trim().equals("")) {

            sortRangeFrom = Integer.parseInt(rangeFromArea.getText().toString().trim());
            sortRangeTo = Integer.parseInt(rangeToArea.getText().toString().trim());
            rangeFromArea.setText("");
            rangeToArea.setText("");

            if (sortRangeFrom < 100000 && sortRangeFrom >= 0) {
              if (sortRangeTo < 100000 && sortRangeTo >= 0) {
                if (sortRangeFrom < sortRangeTo) {

                  if (modeBox.getSelectedItem() == "Java") {
                    System.out.println("Java: sorting");
                    sortRaplayFilesUsingJava();
                  }
                  if (modeBox.getSelectedItem() == "Scala") {
                    System.out.println("Scala: sorting");
                    sortRaplayFilesUsingScala();
                  }
                  long minutes, seconds, hours;
                  hours = sortTime / 3600;
                  minutes = sortTime % 3600 / 60;
                  seconds = sortTime % 60;
                  String result =
                      new String("Best replay score [" + sortRangeFrom + ", " + sortRangeTo + "]: "
                          + currentBestScore + " | Time: " + hours + ":" + minutes + ":" + seconds);
                  resultLabel.setText(result);
                }
              }
            }
          }
        }
      }
    });
  }

  private void sortRaplayFilesUsingJava() {
    long startTime = System.currentTimeMillis();
    for (int i = sortRangeFrom; i <= sortRangeTo; i++) {
      for (int j = sortRangeTo; j > i; j--) {
        if (calculeteHighScore(j) < calculeteHighScore(j - 1)) {
          swapFiles(j, j - 1);
          System.out.println(j + " < = > " + (j - 1));
        }
      }
    }
    long finishTime = System.currentTimeMillis();
    sortTime = (finishTime - startTime) / 1000;
  }

  private void sortRaplayFilesUsingScala() {
    long startTime = System.currentTimeMillis();
    sortRaplayFilesUsingJava();
    long finishTime = System.currentTimeMillis();
    sortTime = (finishTime - startTime) / 1000;
  }

  private int calculeteHighScore(int fileID) {
    int highScore = 0, cell = 0;

    saveOrLoadGame = new SaveOrLoadGame(model);
    stepList = saveOrLoadGame.loadGame(fileID);

    Cell[][] grid = stepList.get(stepList.size() - 1);
    for (int i = 0; i < model.getWidth(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        cell = grid[i][j].getValue();
        while (cell > 1) {
          highScore += cell;
          cell /= 2;
        }
        if (highScore > currentBestScore) {
          currentBestScore = highScore;
        }
      }
    }

    return highScore;
  }

  public void swapFiles(int firstFileID, int secondFileID) {

    ArrayList<Cell[][]> firstList, secondList;
    saveOrLoadGame = new SaveOrLoadGame(model);

    firstList = saveOrLoadGame.loadGame(firstFileID);
    secondList = saveOrLoadGame.loadGame(secondFileID);

    saveOrLoadGame.saveGame(secondList, firstFileID);
    saveOrLoadGame.saveGame(firstList, secondFileID);

  }
}
