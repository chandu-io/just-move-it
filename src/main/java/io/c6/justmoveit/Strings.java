package io.c6.justmoveit;

/**
 * @author Chandrasekhar Thotakura
 */
interface Strings {

  String APP_NAME = "Main";
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
