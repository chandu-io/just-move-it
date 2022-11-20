package io.c6.justmoveit;

import static io.c6.justmoveit.StringsSingleton.Strings;
import static io.c6.justmoveit.UtilsSingleton.Utils;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.Container;
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
    final var panel1 = new JPanel(new FlowLayout());
    panel1.add(new JLabel(Strings.LABEL_TIME_INTERVAL));
    panel1.add(intervalLabel);
    outputPanel.add(panel1);
  }

  private void addElapsedLabel() {
    final var panel2 = new JPanel(new FlowLayout());
    panel2.add(new JLabel(Strings.LABEL_ELAPSED_TIME));
    panel2.add(elapsedLabel);
    outputPanel.add(panel2);
  }

  private void addRemainingLabel() {
    final var panel3 = new JPanel(new FlowLayout());
    panel3.add(new JLabel(Strings.LABEL_REMAINING_TIME));
    panel3.add(remainingLabel);
    outputPanel.add(panel3);
  }

  private void addControlButtons() {
    stopButton.addActionListener(e -> mainWindow.onStopHandler());
    stopButton.setMnemonic(KeyEvent.VK_O);
    exitButton.addActionListener(e -> mainWindow.onExitHandler());
    exitButton.setMnemonic(KeyEvent.VK_X);
    final var panel4 = new JPanel(new FlowLayout());
    panel4.add(stopButton);
    panel4.add(exitButton);
    outputPanel.add(panel4);
  }

  void updateIntervalDuration(final Duration intervalDuration) {
    final var intervalText =
        intervalDuration.getSeconds() + Strings.LABEL_TIME_INTERVAL_SUFFIX;
    invokeLater(() -> intervalLabel.setText(intervalText));
  }

  void updateLabels(final Duration elapsedDuration, final Duration remainingDuration) {
    final var elapsedText = Utils.getFormattedDuration(elapsedDuration);
    final var remainingText = Optional.ofNullable(remainingDuration)
        .map(Utils::getFormattedDuration).orElse(Strings.LABEL_FOREVER);
    invokeLater(() -> {
      elapsedLabel.setText(elapsedText);
      remainingLabel.setText(remainingText);
    });
  }

  Container getContainer() {
    return outputPanel;
  }
}
