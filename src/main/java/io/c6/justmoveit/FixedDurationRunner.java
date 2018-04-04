package io.c6.justmoveit;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author Chandrasekhar Thotakura
 */
final class FixedDurationRunner implements IntervalRunner {

  private static final Duration ONE_SECOND = Duration.ofSeconds(1);

  private final BiConsumer<Duration, Duration> block;
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  private Duration remainingDuration;
  private Duration elapsedDuration;

  FixedDurationRunner(final Duration executionDuration,
      final Duration intervalDuration,
      final BiConsumer<Duration, Duration> block) {
    remainingDuration = executionDuration;
    elapsedDuration = Duration.ZERO;
    this.block = block;
    executor.scheduleAtFixedRate(this::run, 0,
        intervalDuration.toMillis(), TimeUnit.MILLISECONDS);
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
    if (Duration.ZERO.equals(remainingDuration)) {
      stop();
    }
  }

  public static void main(String[] args) {
    new FixedDurationRunner(Duration.ofSeconds(3), ONE_SECOND, (elapsed, remaining) -> {
      System.out.println(String.format("%s, %s, %s",
          elapsed.toString(),
          remaining.toString(),
          Thread.currentThread().toString()));
    });
  }
}
