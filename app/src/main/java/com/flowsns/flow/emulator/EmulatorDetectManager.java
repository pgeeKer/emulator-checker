package com.flowsns.flow.emulator;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.flowsns.flow.emulator.jni.EmulatorCheckService;
import com.flowsns.flow.emulator.utils.EmulatorDetectUtils;

/**
 * 模拟器检查管理类
 *
 * @author panxiangxing
 */
public class EmulatorDetectManager {

    private IEmulatorDetectListener emulatorDetectListener;
    private Context context;

    public EmulatorDetectManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setEmulatorDetectListener(IEmulatorDetectListener emulatorDetectListener) {
        this.emulatorDetectListener = emulatorDetectListener;
    }

    public void startDetectService() {
        if (emulatorDetectListener == null) {
            Log.e("TAG", "先设置回调监听器");
            return;
        }
        if (!shouldCheckByNative()) {
            emulatorDetectListener.call(true);
            return;
        }
        Intent intent = new Intent(context, EmulatorCheckService.class);
        context.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    private boolean shouldCheckByNative() {
        return EmulatorDetectUtils.checkValidIMEI(context) && EmulatorDetectUtils.checkHasLightSensor(context)
            && EmulatorDetectUtils.checkHasBlueTooth();
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IEmulatorCheck emulatorCheck = IEmulatorCheck.Stub.asInterface(iBinder);
            if (emulatorCheck == null) {
                return;
            }
            if (emulatorDetectListener == null) {
                return;
            }
            try {
                boolean emulator = emulatorCheck.isEmulator();
                emulatorDetectListener.call(emulator);
            } catch (RemoteException e) {
                e.printStackTrace();
                emulatorDetectListener.call(true);
            } finally {
                context.unbindService(this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };
}
