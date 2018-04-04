package io.c6.justmoveit;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Chandrasekhar Thotakura
 */
final class ForeverRunner implements IntervalRunner {

  private static final Duration ONE_SECOND = Duration.ofSeconds(1);

  private final Consumer<Duration> block;
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  private Duration elapsedDuration;

  ForeverRunner(final Duration intervalDuration,
      final Consumer<Duration> block) {
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
    block.accept(elapsedDuration);
    elapsedDuration = elapsedDuration.plus(ONE_SECOND);
  }

  public static void main(String[] args) {
    new ForeverRunner(ONE_SECOND, elapsed -> {
      System.out.println(String.format("%s, %s",
          elapsed.toString(),
          Thread.currentThread().toString()));
    });
  }
}
