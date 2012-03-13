package algorithms;

import org.junit.Ignore;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Ignore
public class TestHelp {

  public interface Callback<T> {
    void consume(T value);
  }

  public static <T> void timeAndLogMultipleCalls(String description, Callable<T> callable, int n, Callback<T> callback, TimeUnit unit) throws Exception {
    logTiming(description, n, timeMultipleCalls(callable, n, callback, unit), unit);
  }

  public static <T> long timeMultipleCalls(Callable<T> callable, int n, Callback<T> callback, TimeUnit unit) throws Exception {
    long before = System.nanoTime();
    for (int i = 0; i < n; i++) {
      callback.consume(callable.call());
    }
    return unit.convert(System.nanoTime() - before, TimeUnit.NANOSECONDS);
  }

  public static void logTiming(String description, int n, long time, TimeUnit unit) {
    System.out.format("%s = %,d %s (n = %,d)%n", description, time, unit.name().toLowerCase(), n);
  }
}
