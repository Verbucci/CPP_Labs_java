package com.ggl.game2048.view;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.ggl.game2048.properties.HighScoreProperties;
import com.ggl.game2048.controller.DownArrowAction;
import com.ggl.game2048.controller.LeftArrowAction;
import com.ggl.game2048.controller.RightArrowAction;
import com.ggl.game2048.controller.UpArrowAction;
import com.ggl.game2048.model.Game2048Model;

// Here the main game frame is created and filled with all the components
public class Game2048Frame {
  private ControlPanel controlPanel;
  private Game2048Model model;
  private GridPanel gridPanel;
  private HighScoreProperties highScoreProperties;
  private JFrame frame;
  private JPanel mainPanel;
  private ScorePanel scorePanel;

  public Game2048Frame(Game2048Model model) {
    this.model = model;
    this.highScoreProperties = new HighScoreProperties(model);
    this.highScoreProperties.loadProperties();
    createPartControl();
  }

  private void createPartControl() {
    model.setGridWidth(4);
    gridPanel = new GridPanel(model);
    scorePanel = new ScorePanel(model);
    controlPanel = new ControlPanel(this, model);

    frame = new JFrame();
    frame.setTitle("2048");
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        exitProcedure();
      }
    });

    setKeyBindings();

    mainPanel = new JPanel();
    mainPanel.setLayout(new FlowLayout());
    mainPanel.add(createSidePanel());
    mainPanel.add(gridPanel);

    frame.add(mainPanel);
    frame.setLocationByPlatform(true);
    frame.pack();
    frame.setVisible(true);
  }

  private JPanel createSidePanel() {
    JPanel sidePanel = new JPanel();
    sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
    sidePanel.add(Box.createVerticalStrut(30));
    sidePanel.add(scorePanel.getPanel());
    sidePanel.add(controlPanel.getPanel());
    return sidePanel;
  }

  // This function initializes the input map and sets the key bindings
  private void setKeyBindings() {
    InputMap inputMap = gridPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);

    inputMap.put(KeyStroke.getKeyStroke("UP"), "up arrow");
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down arrow");
    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left arrow");
    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right arrow");

    inputMap = gridPanel.getInputMap(JPanel.WHEN_FOCUSED);
    inputMap.put(KeyStroke.getKeyStroke("UP"), "up arrow");
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down arrow");
    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left arrow");
    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right arrow");

    gridPanel.getActionMap().put("up arrow", new UpArrowAction(this, model));
    gridPanel.getActionMap().put("down arrow", new DownArrowAction(this, model));
    gridPanel.getActionMap().put("left arrow", new LeftArrowAction(this, model));
    gridPanel.getActionMap().put("right arrow", new RightArrowAction(this, model));
  }

  // calls this function while exiting
  public void exitProcedure() {
    model.setHighScores();
    highScoreProperties.saveProperties();
    frame.dispose();
    System.exit(0);
  }

  public int getSelectedWidth() {
    return controlPanel.getCurrentWidth();
  }

  public void resetGridPanel() {
    mainPanel.remove(gridPanel);
    gridPanel = new GridPanel(model);
    setKeyBindings();
    mainPanel.add(gridPanel);
    mainPanel.revalidate();
    frame.pack();
  }

  public void repaintGridPanel() {
    gridPanel.repaint();
  }

  public void updateScorePanel() {
    scorePanel.updatePartControl();
  }

}
