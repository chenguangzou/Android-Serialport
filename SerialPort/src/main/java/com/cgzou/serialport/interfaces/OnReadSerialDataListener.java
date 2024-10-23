package com.cgzou.serialport.interfaces;

/**
 * 数据接收接口
 * @author chenguangzou
 * @version 1.0
 * @date 2023/9/19 8:32
 */
public interface OnReadSerialDataListener {

    /**
     * 解析出一帧数据
     * @param data  接收的数据
     * @param size  数据大小
     */
    void onReceive(byte[] data,int size);

}
