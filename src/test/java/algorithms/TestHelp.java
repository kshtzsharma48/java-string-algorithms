package algorithms;

import org.junit.Ignore;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Ignore
public class TestHelp {

    public interface Callback<T> {
        void consume(T value);
    }

    public static <T> long timeMultipleCalls(Callable<T> callable, int n, Callback<T> callback) throws Exception {
        long before = System.nanoTime();
        for (int i = 0; i < n; i++) {
            callback.consume(callable.call());
        }
        return System.nanoTime() - before;
    }

    public static void logTiming(String description, long timeNanos) {
        System.out.format("%s = %,dms%n", description, TimeUnit.NANOSECONDS.toMillis(timeNanos));
    }
}
