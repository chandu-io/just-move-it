package io.c6.justmoveit;

import static io.c6.justmoveit.Utils.UTILS;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The `JPanel` with the information on current execution and other control buttons
 *
 * @author Chandrasekhar Thotakura
 */
final class OutputView {

  private final MainView mainWindow;
  private final JPanel outputPanel;
  private final JButton stopButton;
  private final JButton exitButton;
  private final JLabel intervalLabel;
  private final JLabel elapsedLabel;
  private final JLabel remainingLabel;

  OutputView(final MainView mainWindow) {
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
    stopButton.addActionListener(e -> mainWindow.onStopHandler());
    stopButton.setMnemonic(KeyEvent.VK_O);
    exitButton.addActionListener(e -> mainWindow.onExitHandler());
    exitButton.setMnemonic(KeyEvent.VK_X);
    final JPanel panel4 = new JPanel(new FlowLayout());
    panel4.add(stopButton);
    panel4.add(exitButton);
    outputPanel.add(panel4);
  }

  void updateIntervalDuration(final Duration intervalDuration) {
    invokeLater(() -> intervalLabel.setText(Strings.EMPTY + UTILS.toSeconds(intervalDuration)));
  }

  void updateLabels(final Duration elapsedDuration, final Duration remainingDuration) {
    invokeLater(() -> {
      elapsedLabel.setText(UTILS.getFormattedTime(elapsedDuration));
      final String text = Optional.ofNullable(remainingDuration)
          .map(UTILS::getFormattedTime)
          .orElse(Strings.LABEL_FOREVER);
      remainingLabel.setText(text);
    });
  }

  JPanel getContainer() {
    return outputPanel;
  }
}
