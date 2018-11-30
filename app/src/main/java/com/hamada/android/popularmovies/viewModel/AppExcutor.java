package com.hamada.android.popularmovies.viewModel;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExcutor {
    private final static Object lock=new Object();
    private static AppExcutor mInstance;
    private final Executor deskIo;
    private final Executor mainThread;
    private final Executor networkIo;

    public AppExcutor(Executor deskIo, Executor mainThread, Executor networkIo) {
        this.deskIo = deskIo;
        this.mainThread = mainThread;
        this.networkIo = networkIo;
    }
    public static AppExcutor getExecutor(){
        if (mInstance==null){
            synchronized (lock){
                mInstance=new AppExcutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),new MainExcutor());
            }
        }
        return mInstance;
    }

    public Executor getDeskIo() {
        return deskIo;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getNetworkIo() {
        return networkIo;
    }

    private static class MainExcutor implements Executor{

        private Handler mainThread=new Handler(Looper.getMainLooper());


        @Override
        public void execute(@NonNull Runnable command) {

            mainThread.post(command);
        }
    }
}
