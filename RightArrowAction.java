package com.ggl.game2048.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.view.Game2048Frame;

// Provides the algorithm, executed after the key is pressed
public class RightArrowAction extends AbstractAction {

  private static final long serialVersionUID = 2982995823948983992L;
  private Game2048Frame frame;
  private Game2048Model model;

  public RightArrowAction(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (model.isArrowActive()) {
      if (model.moveCellsRight()) { // The ...ArrowAction classes are very
                                    // similar, the only difference is
                                    // that function
        if (model.isGameOver()) {
          model.setArrowActive(false);
        } else {
          model.addNewCell();

          frame.repaintGridPanel();
          frame.updateScorePanel();
        }
      }
    }
  }

}
