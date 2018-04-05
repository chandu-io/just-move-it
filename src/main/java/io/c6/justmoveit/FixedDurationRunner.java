package io.c6.justmoveit;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

/**
 * @author Chandrasekhar Thotakura
 */
final class FixedDurationRunner implements IntervalRunner {

  private static final Duration ONE_SECOND = Duration.ofSeconds(1);

  private final BiConsumer<Duration, Duration> block;
  private final ScheduledExecutorService executor = newScheduledThreadPool(1);

  private Duration remainingDuration;
  private Duration elapsedDuration;

  FixedDurationRunner(final Duration executionDuration,
      final BiConsumer<Duration, Duration> block) {
    remainingDuration = executionDuration;
    this.block = block;
    executor.scheduleAtFixedRate(this::run, 0, ONE_SECOND.toMillis(), MILLISECONDS);
    elapsedDuration = Duration.ZERO;
  }

  @Override
  public void stop() {
    if (!isDone()) {
      executor.shutdownNow();
    }
  }

  @Override
  public boolean isDone() {
    return executor.isShutdown();
  }

  private void run() {
    block.accept(elapsedDuration, remainingDuration);
    remainingDuration = remainingDuration.minus(ONE_SECOND);
    elapsedDuration = elapsedDuration.plus(ONE_SECOND);
  }
}
