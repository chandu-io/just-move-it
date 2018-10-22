package io.c6.justmoveit;

import java.time.Duration;

import static io.c6.justmoveit.ActiveTimer.countdownTimer;
import static java.time.Duration.ZERO;
import static java.util.Optional.ofNullable;

/**
 * Implementation for `IntervalRunner`. An executor will run the `FixedDurationTask`
 * given by the client for every second until the specified execution duration is completed.
 *
 * @author Chandrasekhar Thotakura
 */
final class FixedDurationRunner implements IntervalRunner {

  private final FixedDurationTask task;
  private final TaskRunner taskRunner;
  private Timer timer;

  FixedDurationRunner(final FixedDurationTask task, final Duration executionDuration) {
    this.task = ofNullable(task).orElseThrow();
    timer = countdownTimer(ofNullable(executionDuration).orElse(ZERO));
    taskRunner = new TaskRunner(this);
  }

  @Override
  public void stop() {
    taskRunner.stop();
  }

  @Override
  public void run() {
    task.execute(timer.elapsedDuration(), timer.remainingDuration());
    timer = timer.tick();
  }

  @FunctionalInterface
  interface FixedDurationTask {

    void execute(Duration elapsed, Duration remaining);
  }
}
