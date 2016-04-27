package com.ggl.game2048.controller;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

// shows the sort widget, sorts the files
public class SortGameReplaysActionListener implements ActionListener {

  private Game2048Model model;
  private long sortTime = 0;
  private int sortRangeFrom = 0;
  private int sortRangeTo = 0;
  private int currentBestScore = 0;
  private int currentWorstScore = 0;
  private int currentBestScoreFileID = 0;
  private int currentWorstScoreFileID = 0;
  private ArrayList<Cell[][]> stepList;
  private SaveOrLoadGame saveOrLoadGame;

  public SortGameReplaysActionListener(Game2048Model model) {
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

    String[] modes = {"Java", "Scala", "Help"};
    JComboBox<String> modeBox = new JComboBox<String>(modes);
    sortPanel.add(modeBox);

    JButton OKButton = new JButton("OK");
    sortPanel.add(OKButton);

    JLabel sortRangeLabel = new JLabel("Sorting results: [from, to]: ");
    sortPanel.add(sortRangeLabel);

    JLabel resultLabel = new JLabel("Worst game: score [fileID], best game: score [fileID]");
    sortPanel.add(resultLabel);

    JLabel timeLabel = new JLabel("Sorting time: 00:00:000");
    sortPanel.add(timeLabel);

    sortFrame.setSize(350, 170);
    sortFrame.setResizable(false);
    sortFrame.add(sortPanel);
    sortFrame.setLocationByPlatform(true);
    sortFrame.setVisible(true);

    saveOrLoadGame = new SaveOrLoadGame(model);

    OKButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (!rangeFromArea.getText().trim().equals("")) {
          if (!rangeToArea.getText().trim().equals("")) {

            sortRangeFrom = Integer.parseInt(rangeFromArea.getText().toString().trim());
            sortRangeTo = Integer.parseInt(rangeToArea.getText().toString().trim());
            rangeFromArea.setText("");
            rangeToArea.setText("");

            if (sortRangeFrom < 10000 && sortRangeFrom >= 0) {
              if (sortRangeTo < 10000 && sortRangeTo >= 0) {
                if (sortRangeFrom < sortRangeTo) {

                  if (modeBox.getSelectedItem() == "Java") {
                    sortRaplayFilesUsingJava();
                  } else if (modeBox.getSelectedItem() == "Scala") {
                    sortRaplayFilesUsingScala();
                  } else if (modeBox.getSelectedItem() == "Help") {
                    stepList = saveOrLoadGame.loadGame(sortRangeFrom);

                    List<Cell> cellList = new ArrayList<Cell>();

                    for (Cell[][] grid : stepList) {
                      for (int i = 0; i < model.getWidth(); i++) {
                        for (int j = 0; j < model.getWidth(); j++) {
                          cellList.add(grid[i][j]);
                        }
                      }
                    }

                    ScalaSort sort = new ScalaSort();
                    sort.printPseudocode(cellList.toArray(new Cell[cellList.size()]));
                  }

                  long minutes, seconds, milliseconds;
                  milliseconds = sortTime % 1000;
                  minutes = sortTime / 1000 % 3600 / 60;
                  seconds = sortTime / 1000 % 60;
                  String range =
                      new String("Sorting results: [" + sortRangeFrom + ", " + sortRangeTo + "]: ");
                  sortRangeLabel.setText(range);

                  String result = new String(
                      "Worst game: " + currentWorstScore + " [" + currentWorstScoreFileID + "], "
                          + "best game: " + currentBestScore + " [" + currentBestScoreFileID + "]");
                  resultLabel.setText(result);

                  String timeResult =
                      new String("Sorting time: " + minutes + ":" + seconds + ":" + milliseconds);
                  timeLabel.setText(timeResult);
                }
              }
            }
          }
        }
      }
    });
  }

  private void sortRaplayFilesUsingJava() {
    int temp;

    int[] recordList = new int[sortRangeTo - sortRangeFrom + 1];
    int[] replayFileID = new int[sortRangeTo - sortRangeFrom + 1];

    long startTime = System.currentTimeMillis();

    for (int i = sortRangeFrom; i <= sortRangeTo; i++) {
      replayFileID[i - sortRangeFrom] = i;
      recordList[i - sortRangeFrom] = calculateHighScore(i);
    }

    for (int i = sortRangeFrom; i <= sortRangeTo; i++) {
      for (int j = sortRangeTo; j > i; j--) {
        if (recordList[j - sortRangeFrom] < recordList[j - sortRangeFrom - 1]) {
          temp = recordList[j - sortRangeFrom];
          recordList[j - sortRangeFrom] = recordList[j - sortRangeFrom - 1];
          recordList[j - sortRangeFrom - 1] = temp;

          temp = replayFileID[j - sortRangeFrom];
          replayFileID[j - sortRangeFrom] = replayFileID[j - sortRangeFrom - 1];
          replayFileID[j - sortRangeFrom - 1] = temp;
        }
      }
    }

    long finishTime = System.currentTimeMillis();
    sortTime = (finishTime - startTime);

    currentBestScore = recordList[sortRangeTo - sortRangeFrom];
    currentBestScoreFileID = replayFileID[sortRangeTo - sortRangeFrom];

    currentWorstScore = recordList[0];
    currentWorstScoreFileID = replayFileID[0];
  }


  private void sortRaplayFilesUsingScala() {

    int[] recordList = new int[sortRangeTo - sortRangeFrom + 1];
    int[] replayFileID = new int[sortRangeTo - sortRangeFrom + 1];

    long startTime = System.currentTimeMillis();
    for (int i = sortRangeFrom; i <= sortRangeTo; i++) {
      replayFileID[i - sortRangeFrom] = i;
      recordList[i - sortRangeFrom] = calculateHighScore(i);
    }

    ScalaSort sort = new ScalaSort();
    sort.sort(recordList, replayFileID);

    long finishTime = System.currentTimeMillis();
    sortTime = (finishTime - startTime);

    currentBestScore = recordList[sortRangeTo - sortRangeFrom];
    currentBestScoreFileID = replayFileID[sortRangeTo - sortRangeFrom];

    currentWorstScore = recordList[0];
    currentWorstScoreFileID = replayFileID[0];
  }


  private int calculateHighScore(int fileID) {
    int highScore = 0, cell = 0;

    stepList = saveOrLoadGame.loadGame(fileID);

    Cell[][] grid = stepList.get(stepList.size() - 1);

    for (int i = 0; i < model.getWidth(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        cell = grid[i][j].getValue();
        while (cell > 1) {
          highScore += cell;
          cell /= 2;
        }
      }
    }

    return highScore;
  }

}
