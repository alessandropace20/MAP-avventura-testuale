package code.yankton_bank.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility di concorrenza usate per animazioni e stampa testuale.
 */

public final class Concurrent {
    private static final AtomicLong THREAD_COUNTER = new AtomicLong(1); // Thread counter
    private static final ExecutorService EXEC = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setName("god-bg-" + THREAD_COUNTER.getAndIncrement()); // Counter per nomi univoci
        t.setDaemon(true);
        return t;
    });

    private Concurrent() {}

    public static void runAsync(Runnable task) {
        EXEC.submit(task);
    }

    public static void shutdown() {
        EXEC.shutdown();
        try {
            if (!EXEC.awaitTermination(2, TimeUnit.SECONDS)) {
                EXEC.shutdownNow();
            }
        } catch (InterruptedException e) {
            EXEC.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}