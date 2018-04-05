package io.c6.justmoveit;

import static io.c6.justmoveit.Utils.HOURS_PER_DAY;
import static io.c6.justmoveit.Utils.MINS_PER_HOUR;
import static io.c6.justmoveit.Utils.UTILS;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

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
 *
 * The `JPanel` with the options to select the duration and other control buttons
 *
 * @author Chandrasekhar Thotakura
 *
 */
final class InputView {

  private static final long MAX_INTERVAL_SECONDS = 120;

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
    hoursComboBox = new JComboBox<>(UTILS.numberedComboBoxModel(HOURS_PER_DAY));
    minutesComboBox = new JComboBox<>(UTILS.numberedComboBoxModel(MINS_PER_HOUR));
    secondsComboBox = new JComboBox<>(UTILS.numberedComboBoxModel(1, (MAX_INTERVAL_SECONDS + 1)));
    addFixedTimeCheckBox();
    addDurationComboBoxes();
    addIntervalComboBox();
    addControlButtons();
  }

  private void addFixedTimeCheckBox() {
    fixedTimeCheckBox.setMnemonic(KeyEvent.VK_F);
    fixedTimeCheckBox.setSelected(false);
    fixedTimeCheckBox.addItemListener(e -> toggleCheckBox());
    final JPanel panel1 = new JPanel(new FlowLayout());
    panel1.add(fixedTimeCheckBox);
    inputPanel.add(panel1);
  }

  private void addDurationComboBoxes() {
    hoursComboBox.setSelectedIndex(1);
    hoursComboBox.setEnabled(false);
    minutesComboBox.setEnabled(false);
    final JPanel panel2 = new JPanel(new FlowLayout());
    panel2.add(new JLabel(Strings.LABEL_HOURS));
    panel2.add(hoursComboBox);
    panel2.add(new JLabel(Strings.LABEL_MINUTES));
    panel2.add(minutesComboBox);
    inputPanel.add(panel2);
  }

  private void addIntervalComboBox() {
    secondsComboBox.setSelectedIndex((int) (MINS_PER_HOUR - 1));
    final JPanel panel3 = new JPanel(new FlowLayout());
    panel3.add(new JLabel(Strings.LABEL_TIME_INTERVAL_SEC));
    panel3.add(secondsComboBox);
    inputPanel.add(panel3);
  }

  private void addControlButtons() {
    startButton.addActionListener(e -> mainWindow.onStartHandler());
    startButton.setMnemonic(KeyEvent.VK_S);
    exitButton.addActionListener(e -> mainWindow.onExitHandler());
    exitButton.setMnemonic(KeyEvent.VK_X);
    final JPanel panel4 = new JPanel(new FlowLayout());
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
    final long minutes = (long) minutesComboBox.getSelectedItem();
    final long hours = (long) hoursComboBox.getSelectedItem();
    return ofHours(hours).plus(ofMinutes(minutes));
  }

  Duration getIntervalDuration() {
    final long intervalSeconds = (long) secondsComboBox.getSelectedItem();
    return ofSeconds(intervalSeconds);
  }

  JPanel getContainer() {
    return inputPanel;
  }
}
