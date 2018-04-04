package io.c6.justmoveit;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author Chandrasekhar Thotakura
 */
final class AppWindow {

  private static final Logger LOG;

  static {
    LOG = Logger.getLogger(Strings.LOGGER_NAME);
    LOG.setLevel(Level.ALL);
    try {
      FileHandler fileHandler = new FileHandler(Strings.LOG_FILE_NAME, true);
      fileHandler.setFormatter(new SimpleFormatter());
      LOG.addHandler(fileHandler);
    } catch (final IOException e) {
      System.err.println(e);
    }
  }

  private static final int WINDOW_WIDTH = 300;
  private static final int WINDOW_HEIGHT = 200;

  private final JFrame frame;
  private final CardLayout cardLayout;
  private final JPanel pane;
  private final InputView inputPanel;
  private final OutputView outputPanel;

  private IntervalRunner runner;
  private Robot robot;

  AppWindow() {
    frame = new JFrame(Strings.FRAME_TITLE);
    cardLayout = new CardLayout();
    pane = new JPanel(cardLayout);
    inputPanel = new InputView(this);
    outputPanel = new OutputView(this);
  }

  void open() {
    pane.add(inputPanel.getContainer());
    pane.add(outputPanel.getContainer());
    frame.setContentPane(pane);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    initRobot();
  }

  private void initRobot() {
    try {
      robot = new Robot();
    } catch (final AWTException ex) {
      LOG.log(Level.SEVERE, Strings.LOG_ERR_ROBOT_INIT_ERROR, ex);
      LOG.fine(Strings.LOG_MSG_EXITING_APP);
      System.exit(1);
    }
  }

  private void pressKey() {
    robot.keyRelease(KeyEvent.VK_F23);
  }

  void onStart(final ActionEvent event) {
    cardLayout.last(pane);
    final boolean fixedTimeEnabled = inputPanel.isFixedTimeEnabled();
    final Duration executionDuration = inputPanel.getExecutionDuration();
    final Duration intervalDuration = inputPanel.getIntervalDuration();
    runner = fixedTimeEnabled
        ? new FixedDurationRunner(executionDuration, intervalDuration, this::fixedDurationConsumer)
        : new ForeverRunner(intervalDuration, this::foreverConsumer);
    outputPanel.updateIntervalDuration(intervalDuration);
  }

  void onStop(final ActionEvent event) {
    cardLayout.first(pane);
    runner.stop();
  }

  void onInputExit(final ActionEvent event) {
    LOG.fine(Strings.LOG_MSG_EXITING_APP);
    System.exit(0);
  }

  void onOutputExit(final ActionEvent event) {
    runner.stop();
    onInputExit(event);
  }

  void foreverConsumer(final Duration elapsed) {
    pressKey();
    outputPanel.updateLabels(elapsed, null);
  }

  void fixedDurationConsumer(final Duration elapsed, final Duration remaining) {
    pressKey();
    outputPanel.updateLabels(elapsed, remaining);
  }
}
