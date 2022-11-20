package io.c6.justmoveit;

/**
 * @author Chandrasekhar Thotakura
 */
enum StringsSingleton {

  Strings;

  final String APP_NAME = "JustMoveIt";
  final String VERSION = "1.0.2";

  final String EMPTY = "";
  final String LOGGER_NAME = APP_NAME;
  final String LOG_FILE_NAME = "%s.log".formatted(APP_NAME);
  final String FRAME_TITLE = "%s-%s @ Chandu".formatted(APP_NAME, VERSION);

  final String LABEL_FIXED_TIME = "Fixed Time?";
  final String LABEL_HOURS = "Hours:";
  final String LABEL_MINUTES = "Minutes:";
  final String LABEL_TIME_INTERVAL_SEC = "Time interval (seconds):";
  final String LABEL_TIME_INTERVAL = "Time interval:";
  final String LABEL_TIME_INTERVAL_SUFFIX = " second(s)";
  final String LABEL_ELAPSED_TIME = "Elapsed time:";
  final String LABEL_REMAINING_TIME = "Remaining time:";
  final String LABEL_STOP = "STOP";
  final String LABEL_EXIT = " EXIT ";
  final String LABEL_START = "START";
  final String LABEL_EXIT_1 = "  EXIT  ";
  final String LABEL_FOREVER = "FOREVER";

  final String FMT_HH_MM_SS = "   %02d : %02d : %02d";

  final String LOG_MSG_KEY_PRESSED = "Key pressed...";
  final String LOG_MSG_EXITING_APP = "Exiting app...";
  final String LOG_ERR_ROBOT_INIT_ERROR = "Could not initialize java.awt.Robot";
}
