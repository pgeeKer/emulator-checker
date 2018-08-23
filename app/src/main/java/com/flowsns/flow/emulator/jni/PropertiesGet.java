package com.flowsns.flow.emulator.jni;

import android.text.TextUtils;
import com.flowsns.flow.emulator.ShellAdbUtils;

/**
 * C 代码获取设备的信息，避免 hook
 *
 * @author panxiangxing
 */
class PropertiesGet {

    static {
        System.loadLibrary("propertyGet");
    }

    private static native String native_get(String key);

    private static native String native_get(String key, String def);

    //  qemu模拟器特征
    static boolean isEmulatorByQEMU() {
        String qemu = native_get("ro.kernel.qemu");
        return "1".equals(qemu);
    }

    static boolean isEmulatorByBuild() {
        if (!TextUtils.isEmpty(PropertiesGet.native_get("ro.product.model")) && PropertiesGet.native_get(
            "ro.product.model").toLowerCase().contains("sdk")) {
            return true;
        }

        // ro.product.manufacturer likes unknown
        if (!TextUtils.isEmpty(PropertiesGet.native_get("ro.product.manufacturer")) && PropertiesGet.native_get(
            "ro.product.manufacture").toLowerCase().contains("unknown")) {
            return true;
        }

        // ro.product.device likes generic
        return !TextUtils.isEmpty(PropertiesGet.native_get("ro.product.device")) && PropertiesGet.native_get(
            "ro.product.device").toLowerCase().contains("generic");
    }

    /**
     * 真机极少手机是属于 x86 的，因此忽略掉这部分真机，而模拟器是运行在 X86 上的
     */
    static boolean isEmulatorFromAbi() {
        String abi = PropertiesGet.native_get("ro.product.cpu.abi");
        return !TextUtils.isEmpty(abi) && abi.contains("x86") || isEmulatorFromCpu();
    }

    // 查杀比较严格，放在最后，直接pass x86
    private static boolean isEmulatorFromCpu() {
        ShellAdbUtils.CommandResult commandResult = ShellAdbUtils.execCommand("cat /proc/cpuinfo", false);
        String cpuInfo = commandResult.successMsg;
        return !TextUtils.isEmpty(cpuInfo) && ((cpuInfo.contains("intel") || cpuInfo.contains("amd")));
    }
}
