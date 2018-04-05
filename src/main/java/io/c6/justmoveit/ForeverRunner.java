package io.c6.justmoveit;

import static io.c6.justmoveit.Utils.ONE_SECOND;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * @author Chandrasekhar Thotakura
 */
final class ForeverRunner implements IntervalRunner {

  private final Consumer<Duration> block;
  private final ScheduledExecutorService executor = newScheduledThreadPool(1);

  private Duration elapsedDuration;

  ForeverRunner(final Consumer<Duration> block) {
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
    block.accept(elapsedDuration);
    elapsedDuration = elapsedDuration.plus(ONE_SECOND);
  }
}
