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

  static final Duration FOREVER = ofSeconds(Long.MAX_VALUE, 999_999_999L);
  static final Duration ONE_SECOND = ofSeconds(1);
  static final long MILLIS_PER_SECOND = ONE_SECOND.toMillis();
  static final long SECONDS_PER_MINUTE = ofMinutes(1).getSeconds();
  static final long MINUTES_PER_HOUR = ofHours(1).toMinutes();
  static final long HOURS_PER_DAY = ofDays(1).toHours();

  String getFormattedDuration(final long seconds) {
    return String.format(Strings.FMT_HH_MM_SS,
        seconds / (MINUTES_PER_HOUR * SECONDS_PER_MINUTE),
        (seconds % (MINUTES_PER_HOUR * SECONDS_PER_MINUTE)) / MINUTES_PER_HOUR,
        seconds % SECONDS_PER_MINUTE);
  }

  String getFormattedDuration(final Duration duration) {
    return getFormattedDuration(duration.getSeconds());
  }

  boolean isDivisibleInSeconds(final Duration large, final Duration small) {
    return large.getSeconds() % small.getSeconds() == 0;
  }

  <E> E getSelected(final JComboBox<E> comboBox) {
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
