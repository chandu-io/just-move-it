package io.c6.justmoveit;

import static io.c6.justmoveit.StringsSingleton.Strings;
import static io.c6.justmoveit.UtilsSingleton.Utils;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.time.Duration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The `JPanel` with the options to select the duration and other control buttons
 *
 * @author Chandrasekhar Thotakura
 */
final class InputView {

  private static final Duration MAX_INTERVAL = Utils.ONE_MINUTE.plus(Utils.ONE_MINUTE);

  private final MainView mainWindow;
  private final JPanel inputPanel;
  private final JButton startButton;
  private final JButton exitButton;
  private final JCheckBox fixedTimeCheckBox;
  private final JComboBox<Long> hoursComboBox;
  private final JComboBox<Long> minutesComboBox;
  private final JComboBox<Long> secondsComboBox;

  InputView(final MainView mainWindow) {
    this.mainWindow = mainWindow;
    inputPanel = new JPanel(new GridLayout(4, 1));
    startButton = new JButton(Strings.LABEL_START);
    exitButton = new JButton(Strings.LABEL_EXIT_1);
    fixedTimeCheckBox = new JCheckBox(Strings.LABEL_FIXED_TIME);
    hoursComboBox = new JComboBox<>(Utils.numberedComboBoxModel(Utils.ONE_DAY.toHours()));
    minutesComboBox = new JComboBox<>(Utils.numberedComboBoxModel(Utils.ONE_HOUR.toMinutes()));
    secondsComboBox = new JComboBox<>(Utils.numberedComboBoxModel(1, (MAX_INTERVAL.toSeconds() + 1)));
    addFixedTimeCheckBox();
    addDurationComboBoxes();
    addIntervalComboBox();
    addControlButtons();
  }

  private void addFixedTimeCheckBox() {
    fixedTimeCheckBox.setMnemonic(KeyEvent.VK_F);
    fixedTimeCheckBox.setSelected(false);
    fixedTimeCheckBox.addItemListener(e -> toggleCheckBox());
    final var panel1 = new JPanel(new FlowLayout());
    panel1.add(fixedTimeCheckBox);
    inputPanel.add(panel1);
  }

  private void addDurationComboBoxes() {
    hoursComboBox.setSelectedIndex(1);
    hoursComboBox.setEnabled(false);
    minutesComboBox.setEnabled(false);
    final var panel2 = new JPanel(new FlowLayout());
    panel2.add(new JLabel(Strings.LABEL_HOURS));
    panel2.add(hoursComboBox);
    panel2.add(new JLabel(Strings.LABEL_MINUTES));
    panel2.add(minutesComboBox);
    inputPanel.add(panel2);
  }

  private void addIntervalComboBox() {
    secondsComboBox.setSelectedIndex((int) (Utils.ONE_HOUR.toMinutes() - 1));
    final var panel3 = new JPanel(new FlowLayout());
    panel3.add(new JLabel(Strings.LABEL_TIME_INTERVAL_SEC));
    panel3.add(secondsComboBox);
    inputPanel.add(panel3);
  }

  private void addControlButtons() {
    startButton.addActionListener(e -> mainWindow.onStartHandler());
    startButton.setMnemonic(KeyEvent.VK_S);
    exitButton.addActionListener(e -> mainWindow.onExitHandler());
    exitButton.setMnemonic(KeyEvent.VK_X);
    final var panel4 = new JPanel(new FlowLayout());
    panel4.add(startButton);
    panel4.add(exitButton);
    inputPanel.add(panel4);
  }

  private void toggleCheckBox() {
    final boolean fixedTimeEnabled = isFixedTimeEnabled();
    hoursComboBox.setEnabled(fixedTimeEnabled);
    minutesComboBox.setEnabled(fixedTimeEnabled);
  }

  boolean isFixedTimeEnabled() {
    return fixedTimeCheckBox.isSelected();
  }

  Duration getExecutionDuration() {
    final var minutes = Utils.getSelected(minutesComboBox);
    final var hours = Utils.getSelected(hoursComboBox);
    return Duration.ofHours(hours).plus(Duration.ofMinutes(minutes));
  }

  Duration getIntervalDuration() {
    final var intervalSeconds = Utils.getSelected(secondsComboBox);
    return Duration.ofSeconds(intervalSeconds);
  }

  Container getContainer() {
    return inputPanel;
  }
}
