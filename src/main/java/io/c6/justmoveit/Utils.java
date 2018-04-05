package io.c6.justmoveit;

import static java.time.Duration.ofDays;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

import java.time.Duration;
import java.util.stream.LongStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Chandrasekhar Thotakura
 */
enum Utils {

  UTILS;

  static final Duration ONE_SECOND = ofSeconds(1);
  static final Duration ONE_MINUTE = ofMinutes(1);
  static final Duration ONE_HOUR = ofHours(1);
  static final Duration ONE_DAY = ofDays(1);
  static final long SECS_PER_MINUTE = UTILS.toSeconds(ONE_MINUTE);
  static final long MINS_PER_HOUR = ONE_HOUR.toMinutes();
  static final long HOURS_PER_DAY = ONE_DAY.toHours();

  long toSeconds(final Duration duration) {
    return duration.toMillis() / ONE_SECOND.toMillis();
  }

  String getFormattedTime(final long seconds) {
    return String.format(Strings.FMT_HH_MM_SS,
        seconds / (MINS_PER_HOUR * SECS_PER_MINUTE),
        (seconds % (MINS_PER_HOUR * SECS_PER_MINUTE)) / MINS_PER_HOUR,
        seconds % SECS_PER_MINUTE);
  }

  String getFormattedTime(final Duration duration) {
    return getFormattedTime(toSeconds(duration));
  }

  ComboBoxModel<Long> numberedComboBoxModel(long endExclusive) {
    return numberedComboBoxModel(0, endExclusive);
  }

  ComboBoxModel<Long> numberedComboBoxModel(long startInclusive, long endExclusive) {
    final DefaultComboBoxModel<Long> model = new DefaultComboBoxModel<>();
    LongStream.range(startInclusive, endExclusive).forEach(model::addElement);
    return model;
  }


  interface Strings {

    String APP_NAME = "JustMoveIt";
    String VERSION = "1.0.0";

    String EMPTY = "";
    String LOGGER_NAME = APP_NAME;
    String LOG_FILE_NAME = APP_NAME + ".log";
    String FRAME_TITLE = APP_NAME + "-" + VERSION + " @ Chandu";

    String LABEL_FIXED_TIME = "Fixed Time?";
    String LABEL_HOURS = "Hours:";
    String LABEL_MINUTES = "Minutes:";
    String LABEL_TIME_INTERVAL_SEC = "Time interval (seconds):";
    String LABEL_TIME_INTERVAL = "Time interval:";
    String LABEL_ELAPSED_TIME = "Elapsed time:";
    String LABEL_REMAINING_TIME = "Remaining time:";
    String LABEL_STOP = "STOP";
    String LABEL_EXIT = " EXIT ";
    String LABEL_START = "START";
    String LABEL_EXIT_1 = "  EXIT  ";
    String LABEL_FOREVER = "FOREVER";

    String FMT_HH_MM_SS = "   %02d : %02d : %02d";

    String LOG_MSG_KEY_PRESSED = "Key pressed...";
    String LOG_MSG_EXITING_APP = "Exiting app...";
    String LOG_ERR_ROBOT_INIT_ERROR = "Could not initialize java.awt.Robot";
  }
}
