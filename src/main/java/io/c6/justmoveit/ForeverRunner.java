package io.c6.justmoveit;

import static io.c6.justmoveit.Utils.ONE_SECOND;
import static java.time.Duration.ZERO;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Chandrasekhar Thotakura
 */
final class ForeverRunner implements IntervalRunner {

  private final ForeverTask task;
  private final ScheduledExecutorService executor = newScheduledThreadPool(1);

  private Duration elapsedDuration;

  ForeverRunner(final ForeverTask task) {
    this.task = task;
    executor.scheduleAtFixedRate(this::run, 0, ONE_SECOND.toMillis(), MILLISECONDS);
    elapsedDuration = ZERO;
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
    task.execute(elapsedDuration);
    elapsedDuration = elapsedDuration.plus(ONE_SECOND);
  }

  @FunctionalInterface
  interface ForeverTask {

    void execute(Duration elapsed);
  }
}
