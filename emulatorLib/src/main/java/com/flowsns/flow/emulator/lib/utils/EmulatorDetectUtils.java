package com.flowsns.flow.emulator.lib.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author panxiangxing
 */
public class EmulatorDetectUtils {

    private static final String EMULATOR_ID = "000000000000000";

    private EmulatorDetectUtils() {}

    public static boolean checkHasLightSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            return false;
        }
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        return sensor != null;
    }

    public static boolean checkHasBlueTooth() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return false;
        }
        String name = defaultAdapter.getName();
        return !TextUtils.isEmpty(name);
    }

    public static boolean checkValidIMEI(Context context) {
        return !EMULATOR_ID.equals(EmulatorDetectUtils.getDeviceId(context));
    }

    private static String getDeviceId(Context context) {
        String deviceId;
        //先看底下
        if (!TextUtils.isEmpty(deviceId = ITelephonyUtil.getDeviceIdLevel2(context)) || !TextUtils.isEmpty(
            deviceId = IPhoneSubInfoUtil.getDeviceIdLevel2(context))) {
            return deviceId;
        }
        //再看中部
        if (!TextUtils.isEmpty(deviceId = ITelephonyUtil.getDeviceIdLevel1(context)) || !TextUtils.isEmpty(
            deviceId = IPhoneSubInfoUtil.getDeviceIdLevel1(context))) {
            return deviceId;
        }
        //再看上部
        if (!TextUtils.isEmpty(deviceId = IPhoneSubInfoUtil.getDeviceIdLevel0(context)) || !TextUtils.isEmpty(
            deviceId = ITelephonyUtil.getDeviceIdLevel0(context))) {
            return deviceId;
        }
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        return telephonyManager == null ? "" : telephonyManager.getDeviceId();
    }
}
