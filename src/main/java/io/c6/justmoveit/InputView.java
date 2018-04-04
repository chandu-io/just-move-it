package io.c6.justmoveit;

import static java.time.Duration.ofDays;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.stream.LongStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Chandrasekhar Thotakura
 */
final class InputView {

  private static final long MINS_PER_HOUR = ofHours(1).toMinutes();
  private static final long HOURS_PER_DAY = ofDays(1).toHours();
  private static final long MAX_INTERVAL_SECONDS = 120;

  private final AppWindow mainWindow;
  private final JPanel inputPanel;
  private final JButton startButton;
  private final JButton exitButton;
  private final JCheckBox fixedTimeCheckBox;
  private final JComboBox<Long> hoursComboBox;
  private final JComboBox<Long> minutesComboBox;
  private final JComboBox<Long> secondsComboBox;

  InputView(final AppWindow mainWindow) {
    this.mainWindow = mainWindow;
    inputPanel = new JPanel(new GridLayout(4, 1));
    startButton = new JButton(Strings.LABEL_START);
    exitButton = new JButton(Strings.LABEL_EXIT_1);
    fixedTimeCheckBox = new JCheckBox(Strings.LABEL_FIXED_TIME);
    hoursComboBox = new JComboBox<>(numberedComboBoxModel(HOURS_PER_DAY));
    minutesComboBox = new JComboBox<>(numberedComboBoxModel(MINS_PER_HOUR));
    secondsComboBox = new JComboBox<>(numberedComboBoxModel(1, (MAX_INTERVAL_SECONDS + 1)));
    addFixedTimeCheckBox();
    addDurationComboBoxes();
    addIntervalComboBox();
    addControlButtons();
  }

  private void addFixedTimeCheckBox() {
    fixedTimeCheckBox.setMnemonic(KeyEvent.VK_F);
    fixedTimeCheckBox.setSelected(false);
    fixedTimeCheckBox.addItemListener(this::toggleCheckBox);
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
    startButton.addActionListener(mainWindow::onStart);
    startButton.setMnemonic(KeyEvent.VK_S);
    exitButton.addActionListener(mainWindow::onInputExit);
    exitButton.setMnemonic(KeyEvent.VK_X);
    final JPanel panel4 = new JPanel(new FlowLayout());
    panel4.add(startButton);
    panel4.add(exitButton);
    inputPanel.add(panel4);
  }

  private ComboBoxModel<Long> numberedComboBoxModel(long endExclusive) {
    return numberedComboBoxModel(0, endExclusive);
  }

  private ComboBoxModel<Long> numberedComboBoxModel(long startInclusive, long endExclusive) {
    final DefaultComboBoxModel<Long> model = new DefaultComboBoxModel<>();
    LongStream.range(startInclusive, endExclusive).forEach(model::addElement);
    return model;
  }

  private void toggleCheckBox(final ItemEvent event) {
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
