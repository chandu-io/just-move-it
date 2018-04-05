package io.c6.justmoveit;

import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.time.Duration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Chandrasekhar Thotakura
 */
final class OutputView {

  private static final long SECS_PER_MINUTE = toSeconds(ofMinutes(1));
  private static final long MINS_PER_HOUR = ofHours(1).toMinutes();

  private final AppWindow mainWindow;
  private final JPanel outputPanel;
  private final JButton stopButton;
  private final JButton exitButton;
  private final JLabel intervalLabel;
  private final JLabel elapsedLabel;
  private final JLabel remainingLabel;

  OutputView(final AppWindow mainWindow) {
    this.mainWindow = mainWindow;
    outputPanel = new JPanel(new GridLayout(4, 1));
    intervalLabel = new JLabel(Strings.EMPTY);
    elapsedLabel = new JLabel(Strings.EMPTY);
    remainingLabel = new JLabel(Strings.EMPTY);
    stopButton = new JButton(Strings.LABEL_STOP);
    exitButton = new JButton(Strings.LABEL_EXIT);
    addIntervalLabel();
    addElapsedLabel();
    addRemainingLabel();
    addControlButtons();
  }

  private void addIntervalLabel() {
    final JPanel panel1 = new JPanel(new FlowLayout());
    panel1.add(new JLabel(Strings.LABEL_TIME_INTERVAL));
    panel1.add(intervalLabel);
    outputPanel.add(panel1);
  }

  private void addElapsedLabel() {
    final JPanel panel2 = new JPanel(new FlowLayout());
    panel2.add(new JLabel(Strings.LABEL_ELAPSED_TIME));
    panel2.add(elapsedLabel);
    outputPanel.add(panel2);
  }

  private void addRemainingLabel() {
    final JPanel panel3 = new JPanel(new FlowLayout());
    panel3.add(new JLabel(Strings.LABEL_REMAINING_TIME));
    panel3.add(remainingLabel);
    outputPanel.add(panel3);
  }

  private void addControlButtons() {
    stopButton.addActionListener(mainWindow::onStop);
    stopButton.setMnemonic(KeyEvent.VK_O);
    exitButton.addActionListener(mainWindow::onExit);
    exitButton.setMnemonic(KeyEvent.VK_X);
    final JPanel panel4 = new JPanel(new FlowLayout());
    panel4.add(stopButton);
    panel4.add(exitButton);
    outputPanel.add(panel4);
  }

  private static long toSeconds(final Duration duration) {
    return duration.toMillis() / ofSeconds(1).toMillis();
  }

  private String getFormattedTime(final long seconds) {
    return String.format(Strings.FMT_HH_MM_SS,
        seconds / (MINS_PER_HOUR * SECS_PER_MINUTE),
        (seconds % (MINS_PER_HOUR * SECS_PER_MINUTE)) / MINS_PER_HOUR,
        seconds % SECS_PER_MINUTE);
  }

  private String getFormattedTime(final Duration duration) {
    return getFormattedTime(toSeconds(duration));
  }

  void updateIntervalDuration(final Duration intervalDuration) {
    invokeLater(() -> {
      intervalLabel.setText(Strings.EMPTY + toSeconds(intervalDuration));
    });
  }

  void updateLabels(final Duration elapsedDuration, final Duration remainingDuration) {
    invokeLater(() -> {
      elapsedLabel.setText(getFormattedTime(elapsedDuration));
      if (remainingDuration != null) {
        remainingLabel.setText(getFormattedTime(remainingDuration));
      } else {
        remainingLabel.setText(Strings.LABEL_FOREVER);
      }
    });
  }

  JPanel getContainer() {
    return outputPanel;
  }
}
