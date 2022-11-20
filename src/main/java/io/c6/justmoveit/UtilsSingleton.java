package io.c6.justmoveit;

import static io.c6.justmoveit.StringsSingleton.Strings;
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
enum UtilsSingleton {

  Utils;

  final Duration ONE_SECOND = Duration.ofSeconds(1);
  final Duration ONE_MINUTE = Duration.ofMinutes(1);
  final Duration ONE_HOUR = Duration.ofHours(1);
  final Duration ONE_DAY = Duration.ofDays(1);

  String getFormattedDuration(final Duration duration) {
    var HH = duration.toHours();
    var mm = duration.toMinutesPart();
    var ss = duration.toSecondsPart();
    return Strings.FMT_HH_MM_SS.formatted(HH, mm, ss);
  }

  boolean isDivisibleInSeconds(final Duration large, final Duration small) {
    return large.toSeconds() % small.toSeconds() == 0;
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
