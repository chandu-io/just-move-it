package io.c6.justmoveit;

/**
 * @author Chandrasekhar Thotakura
 */
enum Strings {

  STRINGS;

  static final String APP_NAME = "JustMoveIt";
  static final String VERSION = "1.0.2";

  static final String EMPTY = "";
  static final String LOGGER_NAME = APP_NAME;
  static final String LOG_FILE_NAME = APP_NAME + ".log";
  static final String FRAME_TITLE = APP_NAME + "-" + VERSION + " @ Chandu";

  static final String LABEL_FIXED_TIME = "Fixed Time?";
  static final String LABEL_HOURS = "Hours:";
  static final String LABEL_MINUTES = "Minutes:";
  static final String LABEL_TIME_INTERVAL_SEC = "Time interval (seconds):";
  static final String LABEL_TIME_INTERVAL = "Time interval:";
  static final String LABEL_ELAPSED_TIME = "Elapsed time:";
  static final String LABEL_REMAINING_TIME = "Remaining time:";
  static final String LABEL_STOP = "STOP";
  static final String LABEL_EXIT = " EXIT ";
  static final String LABEL_START = "START";
  static final String LABEL_EXIT_1 = "  EXIT  ";
  static final String LABEL_FOREVER = "FOREVER";

  static final String FMT_HH_MM_SS = "   %02d : %02d : %02d";

  static final String LOG_MSG_KEY_PRESSED = "Key pressed...";
  static final String LOG_MSG_EXITING_APP = "Exiting app...";
  static final String LOG_ERR_ROBOT_INIT_ERROR = "Could not initialize java.awt.Robot";
}
