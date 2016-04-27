package com.ggl.game2048.controller;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.ggl.game2048.model.Game2048Model;
import com.ggl.game2048.view.Game2048Frame;

// Press Replay to see how it works
public class EnableReplayActionListener implements ActionListener {

  private Game2048Frame frame;
  private Game2048Model model;
  private int inputID = 0;

  public EnableReplayActionListener(Game2048Frame frame, Game2048Model model) {
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent event) {

    if (model.isGameStarted()) {
      if (!model.getBotReplayEnabled()) {

        JFrame replayFrame = new JFrame("Replay");

        JPanel replayPanel = new JPanel();
        replayPanel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter replay file ID: ");
        replayPanel.add(label);

        JTextArea IDArea = new JTextArea();
        IDArea.setColumns(7);
        replayPanel.add(IDArea);

        JButton OKButton = new JButton("OK");
        replayPanel.add(OKButton);

        replayFrame.setSize(300, 100);
        replayFrame.add(replayPanel);
        replayFrame.setLocationByPlatform(true);
        replayFrame.setVisible(true);

        OKButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {

            if (!IDArea.getText().trim().equals("")) {

              inputID = Integer.parseInt(IDArea.getText().toString().trim());
              IDArea.setText("");

              model.setFileForReplayID(inputID);

              if (inputID < 100000 && inputID >= 0) {
                model.setCurrentGameCounter(inputID);
                model.replayGame();

                frame.repaintGridPanel();
                frame.updateScorePanel();
              }
            }
          }
        });
      } else {
        model.replayGame();

        frame.repaintGridPanel();
        frame.updateScorePanel();
      }
    }
  }

}
