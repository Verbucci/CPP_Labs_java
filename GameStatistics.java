package com.ggl.game2048.controller;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ggl.game2048.model.ScalaStatistics;
import com.ggl.game2048.model.Cell;
import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.properties.SaveOrLoadGame;

// shows the statistic widget
public class GameStatistics implements ActionListener {

  private Game2048Model model;
  private ArrayList<Cell[][]> stepList;
  private SaveOrLoadGame saveOrLoadGame;
  private final int filesQuantity = 10000;
  private int currentHighestCell = 0;
  private int averageScore, highestCell, totalPoints, minScore, maxScore;
  private JLabel scoreLabel, cellLabel, pointsLabel, maxScoreLabel, minScoreLabel;


  public GameStatistics(Game2048Model model) {
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    JFrame statisticsFrame = new JFrame("Statistics");

    JPanel statisticsPanel = new JPanel();
    statisticsPanel.setLayout(new FlowLayout());

    JLabel headLabel = new JLabel("[2048 Game Statistics]");
    statisticsPanel.add(headLabel);

    pointsLabel = new JLabel("Points earned: [...]");
    statisticsPanel.add(pointsLabel);

    maxScoreLabel = new JLabel("Highest score: [...]");
    statisticsPanel.add(maxScoreLabel);

    minScoreLabel = new JLabel("Smallest score: [...]");
    statisticsPanel.add(minScoreLabel);

    scoreLabel = new JLabel("Average score: [...]");
    statisticsPanel.add(scoreLabel);

    cellLabel = new JLabel("Highest cell: [...]");
    statisticsPanel.add(cellLabel);

    JButton startCalculating = new JButton("Calculate statistics");
    statisticsPanel.add(startCalculating);

    statisticsFrame.setSize(200, 200);
    statisticsFrame.setResizable(false);
    statisticsFrame.add(statisticsPanel);
    statisticsFrame.setLocationByPlatform(true);
    statisticsFrame.setVisible(true);

    startCalculating.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        makeStatistics();
      }
    });

  }

  private void makeStatistics() {
    saveOrLoadGame = new SaveOrLoadGame(model);
    calculateStatistics();
    String resultString = new String("Average score: " + averageScore);
    scoreLabel.setText(resultString);
    resultString = "Highest cell: " + highestCell;
    cellLabel.setText(resultString);
    resultString = "Points earned: " + totalPoints;
    pointsLabel.setText(resultString);
    resultString = "Highest score: " + maxScore;
    maxScoreLabel.setText(resultString);
    resultString = "Smallest score: " + minScore;
    minScoreLabel.setText(resultString);
  }

  private void calculateStatistics() {

    int[] recordList = new int[filesQuantity];
    int[] highestCells = new int[filesQuantity];

    for (int i = 0; i < filesQuantity; i++) {
      recordList[i] = calculateHighScore(i);
      highestCells[i] = currentHighestCell;
    }

    ScalaStatistics statistics = new ScalaStatistics();
    totalPoints = statistics.totalPointsEarned(recordList);
    averageScore = statistics.calculateAverageScore(recordList);
    highestCell = statistics.findHighestCell(highestCells);
    maxScore = statistics.findMaxScore(recordList);
    minScore = statistics.findMinScore(recordList);
  }

  private int calculateHighScore(int fileID) {
    int highScore = 0, cell = 0;

    currentHighestCell = 0;

    stepList = saveOrLoadGame.loadGame(fileID);

    Cell[][] grid = stepList.get(stepList.size() - 1);

    for (int i = 0; i < model.getWidth(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        cell = grid[i][j].getValue();
        if (cell > currentHighestCell) {
          currentHighestCell = cell;
        }
        while (cell > 1) {
          highScore += cell;
          cell /= 2;
        }
      }
    }

    return highScore;
  }

}
