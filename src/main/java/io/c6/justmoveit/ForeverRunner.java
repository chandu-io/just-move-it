package io.c6.justmoveit;

import static io.c6.justmoveit.Utils.ONE_SECOND;
import static java.time.Duration.ZERO;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Implementation for `IntervalRunner`. An executor will run the `ForeverTask`
 * given by the client for every second for the lifetime of the application.
 *
 * @author Chandrasekhar Thotakura
 */
final class ForeverRunner implements IntervalRunner {

  private static final int EXECUTOR_POOL_SIZE = 1;
  private static final int EXECUTOR_DELAY_MILLIS = 0;

  private final ForeverTask task;
  private final ScheduledExecutorService executor = newScheduledThreadPool(EXECUTOR_POOL_SIZE);

  private Duration elapsedDuration;

  ForeverRunner(final ForeverTask task) {
    this.task = task;
    executor.scheduleAtFixedRate(
        this::run, EXECUTOR_DELAY_MILLIS, ONE_SECOND.toMillis(), MILLISECONDS);
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
