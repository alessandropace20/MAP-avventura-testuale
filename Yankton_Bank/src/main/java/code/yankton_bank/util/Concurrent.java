/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Utility di concorrenza usate per animazioni e stampa testuale.
 */

public final class Concurrent {
    private static final ExecutorService EXEC = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setName("god-bg-" + t.getId());
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
