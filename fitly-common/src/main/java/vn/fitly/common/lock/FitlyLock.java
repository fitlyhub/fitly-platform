/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    1:03:49 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.lock;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * 
 */
public class FitlyLock {

    private static final int STRIPE_COUNT = 1024;
    private static final ReentrantLock[] STRIPED_LOCKS = new ReentrantLock[STRIPE_COUNT];

    static {
        for (int i = 0; i < STRIPE_COUNT; i++) {
            STRIPED_LOCKS[i] = new ReentrantLock();
        }
    }

    private static ReentrantLock getLockForKey(String key) {
        int hash = key.hashCode() & 0x7FFFFFFF;
        return STRIPED_LOCKS[hash % STRIPE_COUNT];
    }

    /**
     * Executes a given action under a striped lock associated with the specified
     * key, and returns the computed result. This is highly recommended for
     * operations that fetch or compute data (e.g., resolving cache misses, querying
     * the database) to prevent Cache Stampede / Thundering Herd problems.
     *
     * @param key    The unique identifier used to acquire the specific lock.
     * @param action The {@link Supplier} containing the logic to be executed
     *               safely.
     * @param <T>    The type of the result returned by the supplier.
     * @return The result produced by the executed action.
     */
    public static <T> T supplyWithLock(String key, Supplier<T> action) {
        ReentrantLock lock = getLockForKey(key);
        lock.lock();
        try {
            // Thực thi khối lệnh được truyền vào từ bên ngoài
            return action.get();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Executes a given action under a striped lock associated with the specified
     * key without returning any result. This is ideal for isolated tasks,
     * fire-and-forget operations, or state updates that require strict
     * synchronization for a specific entity.
     *
     * @param key    The unique identifier used to acquire the specific lock.
     * @param action The {@link Runnable} containing the logic to be executed
     *               safely.
     */
    public static void runWithLock(String key, Runnable action) {
        ReentrantLock lock = getLockForKey(key);
        lock.lock();
        try {
            // Thực thi khối lệnh được truyền vào từ bên ngoài
            action.run();
        } finally {
            lock.unlock();
        }
    }

}
