package com.flowsns.flow.emulator.lib.jni;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.flowsns.flow.emulator.IEmulatorCheck;

/**
 * aidl 创建独立进程检查是否是模拟器，避免模拟器或者没有对应 arm 加载 so 崩溃
 *
 * @author panxiangxing
 */
public class EmulatorCheckService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("pan", "emulator service create");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IEmulatorCheck.Stub() {
            @Override
            public boolean isEmulator() throws RemoteException {
                return EmulatorCheckService.isEmulator();
            }

            @Override
            public void kill() throws RemoteException {
                stopSelf();
                System.exit(0);
            }
        };
    }

    private static boolean isEmulator() {
        return PropertiesGet.isEmulatorByQEMU() || PropertiesGet.isEmulatorByBuild()
            || PropertiesGet.isEmulatorFromAbi();
    }
}
