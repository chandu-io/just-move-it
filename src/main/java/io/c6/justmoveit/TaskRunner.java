package io.c6.justmoveit;

import java.util.concurrent.ScheduledExecutorService;

import static io.c6.justmoveit.Utils.MILLIS_PER_SECOND;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

final class TaskRunner {

  private static final int EXECUTOR_POOL_SIZE = 1;
  private static final long EXECUTOR_DELAY_MILLIS = 0L;

  private final ScheduledExecutorService executor = newScheduledThreadPool(EXECUTOR_POOL_SIZE);

  TaskRunner(final Runnable runnable) {
    executor.scheduleAtFixedRate(runnable, EXECUTOR_DELAY_MILLIS, MILLIS_PER_SECOND, MILLISECONDS);
  }

  void stop() {
    executor.shutdownNow();
  }
}
