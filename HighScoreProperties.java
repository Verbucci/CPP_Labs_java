package com.ggl.game2048.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import com.ggl.game2048.model.Game2048Model;

// this class provides the file i/o functions to read and store the records
public class HighScoreProperties {

  private static final String fileName = "game2048.properties";
  private static final String highCell = "highCell";
  private static final String highScore = "highScore";
  private Game2048Model model;

  public HighScoreProperties(Game2048Model model) {
    this.model = model;
  }

  public void loadProperties() {
    Properties properties = new Properties();

    InputStream stream = null;
    File file = new File(fileName);
    try {
      stream = new FileInputStream(file);
      properties.load(stream);
      model.setHighScore(Integer.parseInt(properties.getProperty(highScore)));
      model.setHighCell(Integer.parseInt(properties.getProperty(highCell)));
    } catch (FileNotFoundException e) {

    } catch (IOException exeption) {
      exeption.printStackTrace();
    }
  }

  public void saveProperties() {
    Properties properties = new Properties();
    properties.setProperty(highScore, Integer.toString(model.getHighScore()));
    properties.setProperty(highCell, Integer.toString(model.getHighCell()));

    OutputStream stream = null;
    File file = new File(fileName);

    try {
      stream = new FileOutputStream(file);
      properties.store(stream, "2048 High Score");
      stream.flush();
    } catch (FileNotFoundException exeption) {
      exeption.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      if (stream != null) {
        stream.close();
      }
    } catch (IOException exeption) {
      exeption.printStackTrace();
    }

  }
}
