package com.galamdring.android.popularmovies.Data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OurExecutors {
    public static final Object LOCK = new Object();
    private static OurExecutors INSTANCE;
    private final Executor dbIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private OurExecutors(Executor dbIO, Executor networkIO, Executor mainThread){
        this.dbIO=dbIO;
        this.networkIO = networkIO;
        this.mainThread=mainThread;
    }

    public static OurExecutors getINSTANCE() {
        if(INSTANCE==null){
            synchronized (LOCK){
                INSTANCE = new OurExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),new MainThreadExecutor());
            }
        }
        return INSTANCE;
    }

    public Executor getDbIO() {
        return dbIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
