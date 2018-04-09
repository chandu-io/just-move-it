package io.c6.justmoveit;

import static java.time.Duration.ofDays;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toCollection;

import java.time.Duration;
import java.util.Vector;
import java.util.stream.LongStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

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

  boolean isDivisible(final Duration large, final Duration small) {
    final long largeMillis = large.toMillis();
    final long smallMillis = small.toMillis();
    return largeMillis % smallMillis == 0;
  }

  <E> E getComboBoxValue(final JComboBox<E> comboBox) {
    return comboBox.getItemAt(comboBox.getSelectedIndex());
  }

  ComboBoxModel<Long> numberedComboBoxModel(final long endExclusive) {
    return numberedComboBoxModel(0, endExclusive);
  }

  ComboBoxModel<Long> numberedComboBoxModel(final long startInclusive, final long endExclusive) {
    return new DefaultComboBoxModel<>(
        LongStream.range(startInclusive, endExclusive).boxed()
        .collect(toCollection(Vector::new)));
  }
}
