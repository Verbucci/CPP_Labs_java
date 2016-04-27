package com.ggl.game2048.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.view.Game2048Frame;

// Press Replay to see how it works
public class EnableReplayActionListener implements ActionListener {

  private Game2048Frame frame;
  private Game2048Model model;

  public EnableReplayActionListener(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent event) {

    if (model.isGameStarted()) {
      model.replayGame();

      frame.repaintGridPanel();
      frame.updateScorePanel();
    }
  }

}
