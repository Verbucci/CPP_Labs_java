package com.ggl.game2048.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.ggl.game2048.controller.StartGameActionListener;
import com.ggl.game2048.controller.ActivateBotActionListener;
import com.ggl.game2048.model.Game2048Model;

//creates the panel with the buttons

public class ControlPanel {

	private static final Insets regularInsets = new Insets(10, 10, 0, 10);

	private Game2048Frame frame;

	private Game2048Model model;

	private JPanel panel;

	public ControlPanel(Game2048Frame frame, Game2048Model model) {
		this.frame = frame;
		this.model = model;
		createPartControl();
	}

	private void createPartControl() {
		StartGameActionListener startListener = new StartGameActionListener(frame, model);
		
		ActivateBotActionListener botListener = new ActivateBotActionListener(frame, model);

		panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		int gridy = 0;

		JButton startGameButton = new JButton("Start Game");
		startGameButton.addActionListener(startListener);
		addComponent(panel, startGameButton, 0, gridy++, 1, 1, regularInsets, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL);
		
		JButton activateBotButton = new JButton("Activate Bot");
		activateBotButton.addActionListener(botListener);
		addComponent(panel, activateBotButton, 0, gridy++, 1, 1, regularInsets, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL);
	}

	private void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
			int gridheight, Insets insets, int anchor, int fill) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0D, 1.0D, anchor, fill,
				insets, 0, 0);
		container.add(component, gbc);
	}

	public JPanel getPanel() {
		return panel;
	}
}