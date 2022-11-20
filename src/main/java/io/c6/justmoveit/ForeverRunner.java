package io.c6.justmoveit;

import static io.c6.justmoveit.UtilsSingleton.Utils;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for `IntervalRunner`. An executor will run the `ForeverTask`
 * given by the client for every second for the lifetime of the application.
 *
 * @author Chandrasekhar Thotakura
 */
final class ForeverRunner implements IntervalRunner {

  private static final int EXECUTOR_POOL_SIZE = 1;
  private static final Duration EXECUTOR_DELAY = Duration.ZERO;

  private final ForeverTask task;
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXECUTOR_POOL_SIZE);

  private Duration elapsedDuration;

  ForeverRunner(final ForeverTask task) {
    this.task = task;
    executor.scheduleAtFixedRate(
        this::run, EXECUTOR_DELAY.toMillis(), Utils.ONE_SECOND.toMillis(), TimeUnit.MILLISECONDS);
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
    task.execute(elapsedDuration);
    elapsedDuration = elapsedDuration.plus(Utils.ONE_SECOND);
  }

  @FunctionalInterface
  interface ForeverTask {

    void execute(Duration elapsed);
  }
}
