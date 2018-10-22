package io.c6.justmoveit;

import java.time.Duration;

import static io.c6.justmoveit.Utils.FOREVER;
import static io.c6.justmoveit.Utils.ONE_SECOND;
import static java.time.Duration.ZERO;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

interface Timer {
  Timer tick();
  Duration elapsedDuration();
  Duration remainingDuration();
}

final class StoppedTimer implements Timer {

  private final Duration elapsed;

  StoppedTimer(final Duration elapsedDuration) {
    elapsed = ofNullable(elapsedDuration)
        .filter(duration -> !duration.isNegative())
        .orElse(ZERO);
  }

  @Override
  public Timer tick() {
    return this;
  }

  @Override
  public Duration elapsedDuration() {
    return elapsed;
  }

  @Override
  public Duration remainingDuration() {
    return ZERO;
  }
}

final class ActiveTimer implements Timer {

  private final Duration remaining;
  private final Duration elapsed;

  private ActiveTimer(final Duration executionDuration, final Duration elapsedDuration) {
    remaining = ofNullable(executionDuration)
        .filter(duration -> !duration.isNegative())
        .orElse(ZERO);
    elapsed = ofNullable(elapsedDuration)
        .filter(duration -> !duration.isNegative())
        .orElse(ZERO);
  }

  static ActiveTimer countdownTimer(final Duration executionDuration) {
    return new ActiveTimer(executionDuration, ZERO);
  }

  static ActiveTimer stopwatchTimer() {
    return new ActiveTimer(FOREVER, ZERO);
  }

  @Override
  public Timer tick() {
    final Duration executionDuration = of(remaining)
        .filter(duration -> !duration.isZero())
        .map(duration -> duration.minus(ONE_SECOND))
        .orElse(ZERO);
    final Duration elapsedDuration = of(elapsed)
        .filter(duration -> !FOREVER.equals(duration))
        .map(duration -> duration.plus(ONE_SECOND))
        .orElse(FOREVER);
    if (ZERO.equals(executionDuration)) {
      return new StoppedTimer(elapsedDuration);
    } else {
      return new ActiveTimer(executionDuration, elapsedDuration);
    }
  }

  @Override
  public Duration elapsedDuration() {
    return elapsed;
  }

  @Override
  public Duration remainingDuration() {
    return remaining;
  }
}
