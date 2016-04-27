package com.ggl.game2048.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ggl.game2048.controller.StartGameActionListener;
import com.ggl.game2048.controller.ActivateBotActionListener;
import com.ggl.game2048.controller.ChangeFieldSizeActionListener;
import com.ggl.game2048.controller.EnableReplayActionListener;
import com.ggl.game2048.controller.SortGameReplaysActionListener;
import com.ggl.game2048.controller.GameStatistics;
import com.ggl.game2048.model.Game2048Model;

// creates the panel with the buttons
public class ControlPanel {

  private static final Insets regularInsets = new Insets(10, 10, 0, 10);
  private JComboBox<String> modeBox;
  private Game2048Frame frame;
  private Game2048Model model;
  private JPanel panel;

  public ControlPanel(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
    createPartControl();
  }

  private void createPartControl() {
    GameStatistics statisticsListener = new GameStatistics(model);
    StartGameActionListener startListener = new StartGameActionListener(frame, model);
    ActivateBotActionListener botListener = new ActivateBotActionListener(frame, model);
    EnableReplayActionListener replayListener = new EnableReplayActionListener(frame, model);
    SortGameReplaysActionListener sortReplaysListener = new SortGameReplaysActionListener(model);
    ChangeFieldSizeActionListener changeFieldSizeListener =
        new ChangeFieldSizeActionListener(frame, model);

    panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    int gridY = 0;

    JButton startGameButton = new JButton("Start Game");
    startGameButton.addActionListener(startListener);
    addComponent(panel, startGameButton, 0, gridY++, 1, 1, regularInsets,
        GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);

    JButton activateBotButton = new JButton("Activate Bot");
    activateBotButton.addActionListener(botListener);
    addComponent(panel, activateBotButton, 0, gridY++, 1, 1, regularInsets,
        GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);

    JButton replayButton = new JButton("Replay");
    replayButton.addActionListener(replayListener);
    addComponent(panel, replayButton, 0, gridY++, 1, 1, regularInsets,
        GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);

    JButton sortReplaysButton = new JButton("Sort Replays");
    sortReplaysButton.addActionListener(sortReplaysListener);
    addComponent(panel, sortReplaysButton, 0, gridY++, 1, 1, regularInsets,
        GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);

    JButton showStatistics = new JButton("Statistics");
    showStatistics.addActionListener(statisticsListener);
    addComponent(panel, showStatistics, 0, gridY++, 1, 1, regularInsets,
        GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);

    JLabel currentCellLabel = new JLabel("Choose field size:");
    addComponent(panel, currentCellLabel, 0, gridY++, 1, 1, regularInsets,
        GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);

    String[] modes =
        {"Normal: [4x4]", "Medium: [5x5]", "Large: [6x6]", "Huge: [7x7]", "Extra: [8x8]"};
    modeBox = new JComboBox<String>(modes);
    modeBox.addActionListener(changeFieldSizeListener);
    addComponent(panel, modeBox, 0, gridY++, 1, 1, regularInsets, GridBagConstraints.LINE_START,
        GridBagConstraints.HORIZONTAL);
  }

  private void addComponent(Container container, Component component, int gridX, int gridY,
      int gridWidth, int gridHeight, Insets insets, int anchor, int fill) {
    GridBagConstraints ñonstraints = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight,
        1.0D, 1.0D, anchor, fill, insets, 0, 0);
    container.add(component, ñonstraints);
  }

  public int getCurrentWidth() {
    int currentWidth = 0;
    if (modeBox.getSelectedItem() == "Normal: [4x4]") {
      currentWidth = 4;
      model.setCellWidth(120);
    } else if (modeBox.getSelectedItem() == "Medium: [5x5]") {
      currentWidth = 5;
      model.setCellWidth(110);
    } else if (modeBox.getSelectedItem() == "Large: [6x6]") {
      currentWidth = 6;
      model.setCellWidth(100);
    } else if (modeBox.getSelectedItem() == "Huge: [7x7]") {
      currentWidth = 7;
      model.setCellWidth(90);
    } else if (modeBox.getSelectedItem() == "Extra: [8x8]") {
      currentWidth = 8;
      model.setCellWidth(90);
    }
    model.setGridWidth(currentWidth);
    return currentWidth;
  }

  public JPanel getPanel() {
    return panel;
  }

}
