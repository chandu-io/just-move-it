package io.c6.justmoveit;

import java.time.Duration;

import static io.c6.justmoveit.ActiveTimer.stopwatchTimer;
import static java.util.Optional.ofNullable;

/**
 * Implementation for `IntervalRunner`. An executor will run the `ForeverTask`
 * given by the client for every second for the lifetime of the application.
 *
 * @author Chandrasekhar Thotakura
 */
final class ForeverRunner implements IntervalRunner {

  private final ForeverTask task;
  private final TaskRunner taskRunner;
  private Timer timer;

  ForeverRunner(final ForeverTask task) {
    this.task = ofNullable(task).orElseThrow();
    timer = stopwatchTimer();
    taskRunner = new TaskRunner(this);
  }

  @Override
  public void stop() {
    taskRunner.stop();
  }

  @Override
  public void run() {
    task.execute(timer.elapsedDuration());
    timer = timer.tick();
  }

  @FunctionalInterface
  interface ForeverTask {

    void execute(Duration elapsed);
  }
}
