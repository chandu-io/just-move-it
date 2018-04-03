package io.c6;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Chandrasekhar Thotakura
 */
public enum JustMoveIt {

  ONE;

  private static Logger L;
  private static Robot R;

  static {
    L = Logger.getLogger(Strings.LOGGER_NAME);
    L.setLevel(Level.ALL);
    try {
      FileHandler fileHandler = new FileHandler(Strings.LOG_FILE_NAME, true);
      fileHandler.setFormatter(new SimpleFormatter());
      L.addHandler(fileHandler);
    } catch (IOException e) {
      System.err.println(e);
    }
    try {
      R = new Robot();
    } catch (AWTException ex) {
      L.log(Level.SEVERE, "Could not initialize java.awt.Robot", ex);
      L.fine("Exiting app...");
      System.exit(1);
    }
  }

  private void run() {
    ControlWindow.ONE.open();
  }

  public static void main(final String[] tcs) {
    L.fine("App starting...");
    JustMoveIt.ONE.run();
  }

  private enum ControlWindow implements ActionListener, ItemListener {

    ONE;
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 200;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int SECS_PER_MINUTE = 60;
    private static final int MINS_PER_HOUR = 60;
    private JFrame frame;
    private JPanel pane, inputPanel, outputPanel;
    private JButton startButton, stopButton, iExitButton, oExitButton;
    private JCheckBox fixedTimeCheckBox;
    private JComboBox hoursComboBox, minutesComboBox, secondsComboBox;
    private JLabel intervalLabel, elapsedLabel, remainingLabel;
    private CardLayout cardLayout;
    private DurationUpdater task;
    private Timer timer;
    private int intervalSeconds, elapsedSeconds, scheduledSeconds;
    private boolean isFixedTime;

    private ControlWindow() {
      prepareInputPanel();
      prepareOutputPanel();

      cardLayout = new CardLayout();
      pane = new JPanel(cardLayout);
      pane.add(inputPanel);
      pane.add(outputPanel);
      frame = new JFrame(Strings.FRAME_TITLE);
      frame.setContentPane(pane);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
      frame.setLocationRelativeTo(null);

      timer = new Timer();
    }

    private void prepareInputPanel() {
      fixedTimeCheckBox = new JCheckBox(Strings.LABEL_FIXED_TIME);
      fixedTimeCheckBox.setMnemonic(KeyEvent.VK_F);
      fixedTimeCheckBox.setSelected(false);
      fixedTimeCheckBox.addItemListener(this);

      final JPanel panel1 = new JPanel(new FlowLayout());
      panel1.add(fixedTimeCheckBox);

      hoursComboBox = new JComboBox(getNumbers(23));
      hoursComboBox.setSelectedIndex(1);
      hoursComboBox.setEnabled(false);
      minutesComboBox = new JComboBox(getNumbers(59));
      minutesComboBox.setEnabled(false);

      final JPanel panel2 = new JPanel(new FlowLayout());
      panel2.add(new JLabel(Strings.LABEL_HOURS));
      panel2.add(hoursComboBox);
      panel2.add(new JLabel(Strings.LABEL_MINUTES));
      panel2.add(minutesComboBox);

      secondsComboBox = new JComboBox(
          Arrays.copyOfRange(getNumbers(120), 1, 121));
      secondsComboBox.setSelectedIndex(5);

      final JPanel panel3 = new JPanel(new FlowLayout());
      panel3.add(new JLabel(Strings.LABEL_TIME_INTERVAL_SEC));
      panel3.add(secondsComboBox);

      startButton = new JButton(Strings.LABEL_START);
      startButton.addActionListener(this);
      startButton.setMnemonic(KeyEvent.VK_S);
      iExitButton = new JButton(Strings.LABEL_EXIT_1);
      iExitButton.addActionListener(this);
      iExitButton.setMnemonic(KeyEvent.VK_X);

      final JPanel panel4 = new JPanel(new FlowLayout());
      panel4.add(startButton);
      panel4.add(iExitButton);

      inputPanel = new JPanel(new GridLayout(4, 1));
      inputPanel.add(panel1);
      inputPanel.add(panel2);
      inputPanel.add(panel3);
      inputPanel.add(panel4);
    }

    private void prepareOutputPanel() {
      intervalLabel = new JLabel(Strings.EMPTY);
      final JPanel panel1 = new JPanel(new FlowLayout());
      panel1.add(new JLabel(Strings.LABEL_TIME_INTERVAL));
      panel1.add(intervalLabel);

      elapsedLabel = new JLabel(Strings.EMPTY);
      final JPanel panel2 = new JPanel(new FlowLayout());
      panel2.add(new JLabel(Strings.LABEL_ELAPSED_TIME));
      panel2.add(elapsedLabel);

      remainingLabel = new JLabel(Strings.EMPTY);
      final JPanel panel3 = new JPanel(new FlowLayout());
      panel3.add(new JLabel(Strings.LABEL_REMAINING_TIME));
      panel3.add(remainingLabel);

      stopButton = new JButton(Strings.LABEL_STOP);
      stopButton.addActionListener(this);
      stopButton.setMnemonic(KeyEvent.VK_O);
      oExitButton = new JButton(Strings.LABEL_EXIT);
      oExitButton.addActionListener(this);
      oExitButton.setMnemonic(KeyEvent.VK_X);

      final JPanel panel4 = new JPanel(new FlowLayout());
      panel4.add(stopButton);
      panel4.add(oExitButton);

      outputPanel = new JPanel(new GridLayout(4, 1));
      outputPanel.add(panel1);
      outputPanel.add(panel2);
      outputPanel.add(panel3);
      outputPanel.add(panel4);
    }

    private void updateLabels() {
      final String remainingText = isFixedTime
          ? getFormattedTime(scheduledSeconds - elapsedSeconds)
          : Strings.LABEL_UNLIMITED;
      remainingLabel.setText(remainingText);
      elapsedLabel.setText(getFormattedTime(elapsedSeconds));
    }

    public void open() {
      frame.setVisible(true);
    }

    private String[] getNumbers(final int num) {
      String[] returnArr = new String[num + 1];
      for (int i = 0; i <= num; i += 1) {
        returnArr[i] = Strings.EMPTY + i;
      }
      return returnArr;
    }

    private int getValueFromComboBox(final JComboBox comboBox) {
      return Integer.parseInt(comboBox.getSelectedItem().toString());
    }

    private String getFormattedTime(final int seconds) {
      return String.format(Strings.FMT_HH_MM_SS,
          seconds / (MINS_PER_HOUR * SECS_PER_MINUTE),
          (seconds % (MINS_PER_HOUR * SECS_PER_MINUTE)) / MINS_PER_HOUR,
          seconds % SECS_PER_MINUTE);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
      final Object source = event.getSource();
      if (source == startButton) {
        intervalSeconds = getValueFromComboBox(secondsComboBox);
        final int minutes = getValueFromComboBox(minutesComboBox);
        final int hours = getValueFromComboBox(hoursComboBox);
        scheduledSeconds = (MINS_PER_HOUR * hours + minutes) * SECS_PER_MINUTE;
        intervalLabel.setText(Strings.EMPTY + intervalSeconds);
        elapsedSeconds = 0;
        updateLabels();
        task = new DurationUpdater();
        timer.scheduleAtFixedRate(task, MILLIS_PER_SECOND, MILLIS_PER_SECOND);
        cardLayout.last(pane);
      } else if (source == stopButton) {
        task.cancel();
        timer.purge();
        cardLayout.first(pane);
      } else if (source == iExitButton) {
        L.fine("Exiting app...");
        System.exit(0);
      } else if (source == oExitButton) {
        task.cancel();
        timer.cancel();
        L.fine("Exiting app...");
        System.exit(0);
      }
    }

    @Override
    public void itemStateChanged(final ItemEvent event) {
      if (event.getSource() == fixedTimeCheckBox) {
        final boolean isSelected =
            (event.getStateChange() == ItemEvent.SELECTED);
        hoursComboBox.setEnabled(isSelected);
        minutesComboBox.setEnabled(isSelected);
        isFixedTime = isSelected;
      }
    }

    private class DurationUpdater extends TimerTask {

      @Override
      public void run() {
        elapsedSeconds += 1;
        updateLabels();
        if ((isFixedTime && elapsedSeconds >= scheduledSeconds)
            || elapsedSeconds == Integer.MAX_VALUE) {
          task.cancel();
          timer.cancel();
          L.warning("Exiting app as elapsed time in seconds reached Integer.MAX_VALUE!");
          System.exit(1);
        }
        if (elapsedSeconds % intervalSeconds == 0) {
          R.keyRelease(KeyEvent.VK_F23);
        }
      }
    }
  }

  private final class Strings {

    private static final String APP_NAME = "JustMoveIt";
    private static final String VERSION = "0.0.5";

    private static final String EMPTY = "";
    private static final String LOGGER_NAME = APP_NAME;
    private static final String LOG_FILE_NAME = APP_NAME + ".log";
    private static final String FRAME_TITLE = APP_NAME + "-" + VERSION + " @ Chandu";

    private static final String LABEL_FIXED_TIME = "Fixed Time?";
    private static final String LABEL_HOURS = "Hours:";
    private static final String LABEL_MINUTES = "Minutes:";
    private static final String LABEL_TIME_INTERVAL_SEC = "Time interval (seconds):";
    private static final String LABEL_TIME_INTERVAL = "Time interval:";
    private static final String LABEL_ELAPSED_TIME = "Elapsed time:";
    private static final String LABEL_REMAINING_TIME = "Remaining time:";
    private static final String LABEL_STOP = "STOP";
    private static final String LABEL_EXIT = " EXIT ";
    private static final String LABEL_START = "START";
    private static final String LABEL_EXIT_1 = "  EXIT  ";
    private static final String LABEL_UNLIMITED = "UNLIMITED";

    private static final String FMT_HH_MM_SS = "   %02d : %02d : %02d";
  }
}
