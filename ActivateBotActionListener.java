package com.ggl.game2048.controller;

import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.view.Game2048Frame;

// Press start game -> activate bot to see how it works
public class ActivateBotActionListener implements ActionListener {

  private Game2048Frame frame;
  private Game2048Model model;
  private int localGameCounter;

  public ActivateBotActionListener(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent event) {

    int repeatsNumber;

    if (model.getRecordFilesEnabled()) {
      repeatsNumber = 100000;
    } else {
      repeatsNumber = 1;
    }

    for (int i = 0; i < repeatsNumber; i++) {
      if (model.isGameStarted()) {

        int movement;
        Random randomGenerator = new Random();

        while (!model.isGameOver()) {

          movement = randomGenerator.nextInt(4);

          switch (movement) {
            case 0:
              if (model.isArrowActive()) {
                if (model.moveCellsUp()) {
                  if (model.isGameOver()) {
                    model.setArrowActive(false);
                  } else {
                    model.addNewCell();

                    if (model.getBotReplayEnabled()) {
                      model.saveCurrentSteps();
                    }
                  }
                }
              }
              break;
            case 1:
              if (model.isArrowActive()) {
                if (model.moveCellsDown()) {
                  if (model.isGameOver()) {
                    model.setArrowActive(false);
                  } else {
                    model.addNewCell();

                    if (model.getBotReplayEnabled()) {
                      model.saveCurrentSteps();
                    }
                  }
                }
              }
              break;
            case 2:
              if (model.isArrowActive()) {
                if (model.moveCellsLeft()) {
                  if (model.isGameOver()) {
                    model.setArrowActive(false);
                  } else {
                    model.addNewCell();

                    if (model.getBotReplayEnabled()) {
                      model.saveCurrentSteps();
                    }
                  }
                }
              }
              break;
            case 3:
              if (model.isArrowActive()) {
                if (model.moveCellsRight()) {
                  if (model.isGameOver()) {
                    model.setArrowActive(false);
                  } else {
                    model.addNewCell();

                    if (model.getBotReplayEnabled()) {
                      model.saveCurrentSteps();
                    }
                  }
                }
              }
              break;
          }
        }
        frame.repaintGridPanel();
        frame.updateScorePanel();
      }

      if (model.getRecordFilesEnabled()) {
        model.setHighScores();
        model.initializeGrid();
        model.setGameStarted(true);
        model.setArrowActive(true);
        model.addNewCell();
        model.addNewCell();

        localGameCounter = model.getCurrentGameCounter();
        model.setCurrentGameCounter(localGameCounter + 1);
      }

    }
  }

}
