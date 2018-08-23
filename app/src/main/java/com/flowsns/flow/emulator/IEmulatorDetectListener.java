package com.flowsns.flow.emulator;

/**
 * 检查是否是模拟器的回调
 *
 * @author panxiangxing
 */
public interface IEmulatorDetectListener {

    void call(boolean isEmulator);
}
