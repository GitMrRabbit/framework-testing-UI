package com.ashot.base;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public interface Stabilization {
    default void stabilization(int millis) {
        try {
            sleep(millis);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException("Test stabilization was interrupted", e);
        }
    }
}
