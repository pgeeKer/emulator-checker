package com.flowsns.flow.emulator;

import android.app.Application;

/**
 * @author panxiangxing
 */
public class EmulatorCheckApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(this));
    }
}
