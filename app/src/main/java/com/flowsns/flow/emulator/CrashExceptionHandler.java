package com.flowsns.flow.emulator;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

/**
 * catch crash exception handler
 *
 * @author panxiangxing
 */
public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;

    CrashExceptionHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Process.killProcess(Process.myPid());
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == Process.myPid()) {
                if (!context.getPackageName().equals(processInfo.processName)) {
                    Process.killProcess(Process.myPid());
                }
                break;
            }
        }
    }
}
