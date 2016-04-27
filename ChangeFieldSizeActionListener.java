package com.ggl.game2048.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.view.Game2048Frame;

// acts after the "start game" button is pressed
public class ChangeFieldSizeActionListener implements ActionListener {

  private Game2048Frame frame;
  private Game2048Model model;

  public ChangeFieldSizeActionListener(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    model.setGridWidth(frame.getSelectedWidth());
    frame.resetGridPanel();
    model.initializeGrid();
  }

}
