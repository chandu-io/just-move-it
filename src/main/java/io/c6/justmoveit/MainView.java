package io.c6.justmoveit;

import static io.c6.justmoveit.Utils.UTILS;
import static java.time.Duration.ZERO;
import static java.util.logging.Level.SEVERE;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * Main application window which contains the core logic
 * of triggering key press using `java.awt.Robot`
 *
 * @author Chandrasekhar Thotakura
 */
final class MainView {

  private static final Logger LOG;

  static {
    LOG = Logger.getLogger(Strings.LOGGER_NAME);
    LOG.setLevel(Level.ALL);
    try {
      final FileHandler fileHandler = new FileHandler(Strings.LOG_FILE_NAME, true);
      fileHandler.setFormatter(new SimpleFormatter());
      LOG.addHandler(fileHandler);
    } catch (final IOException e) {
      Logger.getGlobal().log(SEVERE, e.getMessage(), e);
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

  MainView() {
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
    frame.addWindowListener(onBeforeClosing());
    frame.setResizable(false);
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    initRobot();
  }

  private WindowAdapter onBeforeClosing() {
    return new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        super.windowClosing(e);
        LOG.fine(Strings.LOG_MSG_EXITING_APP);
        cleanup();
      }
    };
  }

  private void initRobot() {
    try {
      robot = new Robot();
    } catch (final AWTException ex) {
      LOG.log(SEVERE, Strings.LOG_ERR_ROBOT_INIT_ERROR, ex);
      LOG.fine(Strings.LOG_MSG_EXITING_APP);
      System.exit(1);
    }
  }

  private void tryPressingKey(final Duration elapsed) {
    if (UTILS.isDivisible(elapsed, inputPanel.getIntervalDuration())) {
      LOG.info(Strings.LOG_MSG_KEY_PRESSED);
      robot.keyRelease(KeyEvent.VK_F23);
    }
  }

  private void foreverConsumerTask(final Duration elapsed) {
    tryPressingKey(elapsed);
    outputPanel.updateLabels(elapsed, null);
  }

  private void fixedDurationConsumerTask(final Duration elapsed, final Duration remaining) {
    if (ZERO.equals(remaining)) {
      onExitHandler();
    } else {
      tryPressingKey(elapsed);
      outputPanel.updateLabels(elapsed, remaining);
    }
  }

  private void cleanup() {
    if (runner != null) {
      runner.stop();
    }
  }

  void onStartHandler() {
    cardLayout.last(pane);
    final boolean fixedTimeEnabled = inputPanel.isFixedTimeEnabled();
    final Duration executionDuration = inputPanel.getExecutionDuration();
    final Duration intervalDuration = inputPanel.getIntervalDuration();
    runner = fixedTimeEnabled
        ? new FixedDurationRunner(executionDuration, this::fixedDurationConsumerTask)
        : new ForeverRunner(this::foreverConsumerTask);
    outputPanel.updateIntervalDuration(intervalDuration);
  }

  void onStopHandler() {
    cardLayout.first(pane);
    cleanup();
  }

  void onExitHandler() {
    LOG.fine(Strings.LOG_MSG_EXITING_APP);
    cleanup();
    invokeLater(frame::dispose);
  }
}
