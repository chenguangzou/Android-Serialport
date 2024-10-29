package com.cgzou.serialport.utils;

/**
 * @dsc:      异或校验
 * @Author:   ChenGuangZou
 * @date:     2024/7/22 14:53
 * @Version:  1.0
 */
public class BbcUtils {

    /**
     * 获取异或校验
     * @param bytes
     * @param start
     * @param end
     * @return byte
     * @date   2024/7/23 10:04
     */
    public static byte getBBC(byte[] bytes, int start, int end) {

        byte bbc = 0x00;
        int i;
        for (i = start; i < end; i++) {
            bbc = (byte) (bbc ^ bytes[i]);
        }
        return bbc;
    }

}