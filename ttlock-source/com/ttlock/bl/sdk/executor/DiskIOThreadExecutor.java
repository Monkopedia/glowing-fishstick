package com.ttlock.bl.sdk.executor;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executor that runs a task on a new background thread.
 */
public class DiskIOThreadExecutor implements Executor {

    private final ExecutorService mDiskIO;

    public DiskIOThreadExecutor() {
        mDiskIO =  Executors.newFixedThreadPool(1);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mDiskIO.execute(command);
        mDiskIO.shutdown();
    }
}
