package com.cgzou.serialport.utils;

public class ByteUtils {

    /**
     * 将int转换成2位byte数组，高位在前,低位在后
     * 改变高低位顺序只需调换数组序号
     *
     * @param value
     * @param hi    高位在前,低位在后为true;低位在前为,高位在后为false,
     * @return
     */
    public static byte[] intTo2Byte(int value, boolean hi) {
        byte[] bytes = new byte[2];
        if (hi) {
            bytes[0] = (byte) ((value >> 8) & 0xFF);
            bytes[1] = (byte) (value & 0xFF);
        } else {
            bytes[1] = (byte) ((value >> 8) & 0xFF);
            bytes[0] = (byte) (value & 0xFF);
        }
        return bytes;
    }

    /**
     * 将int转换成3位byte数组，高位在前,低位在后
     * 改变高低位顺序只需调换数组序号
     *
     * @param value
     * @param hi    高位在前,低位在后为true;低位在前,高位在后为false,
     * @return
     */
    public static byte[] intTo3Byte(int value, boolean hi) {
        byte[] bytes = new byte[3];
        if (hi) {
            bytes[0] = (byte) ((value >> 16) & 0xFF);
            bytes[1] = (byte) ((value >> 8) & 0xFF);
            bytes[2] = (byte) (value & 0xFF);
        } else {
            bytes[2] = (byte) ((value >> 16) & 0xFF);
            bytes[1] = (byte) ((value >> 8) & 0xFF);
            bytes[0] = (byte) (value & 0xFF);
        }
        return bytes;
    }

    /**
     * 将int转换成4位byte数组，高位在前,低位在后
     * 改变高低位顺序只需调换数组序号
     *
     * @param value
     * @param hi    高位在前,低位在后为true;低位在前,高位在后为false,
     * @return
     */
    public static byte[] intTo4Byte(int value, boolean hi) {
        byte[] bytes = new byte[4];
        if (hi) {
            bytes[0] = (byte) ((value >> 24) & 0xFF);
            bytes[1] = (byte) ((value >> 16) & 0xFF);
            bytes[2] = (byte) ((value >> 8) & 0xFF);
            bytes[3] = (byte) (value & 0xFF);
        } else {
            bytes[3] = (byte) ((value >> 24) & 0xFF);
            bytes[2] = (byte) ((value >> 16) & 0xFF);
            bytes[1] = (byte) ((value >> 8) & 0xFF);
            bytes[0] = (byte) (value & 0xFF);
        }
        return bytes;
    }

    /**
     * 温度字节转温度 Short计算方式
     * A．-128~127B．-32767~32767C．-32768~23767D．任意整数
     *
     * @param bytes 温度数组
     * @param hi    高位在前,低位在后为true;低位在前,高位在后为false
     * @return
     */
    public static float byteToTemp(byte[] bytes, boolean hi) {
        float temp = 0.00f;
        int t = 0;
        for (int i = 0; i < bytes.length; i++) {

        }
        switch (bytes.length) {
            case 1:
                t = bytes[0] & 0xFF;
                temp = (short) t;
                break;
            case 2:
                t = bytes2ToInt(bytes, hi);
                temp = (short) t;
                break;
        }

        return temp;
    }
    /*小端，低字节在后*/

    /**
     * byte数组转int
     * @param bytes 字节数组
     * @param hi    高位在前,低位在后为true;低位在前,高位在后为false,
     * @return
     */
    public static int bytes4ToInt(byte[] bytes, boolean hi) {

        if (hi) {
            return bytes[3] & 0xFF |
                    (bytes[2] & 0xFF) << 8 |
                    (bytes[1] & 0xFF) << 16 |
                    (bytes[0] & 0xFF) << 24;
        }
        return bytes[0] & 0xFF |
                (bytes[1] & 0xFF) << 8 |
                (bytes[2] & 0xFF) << 16 |
                (bytes[3] & 0xFF) << 24;
    }
    /**
     * byte数组转int
     * @param bytes 字节数组
     * @param hi    高位在前,低位在后为true;低位在前,高位在后为false,
     * @return
     */
    public static int bytes3ToInt(byte[] bytes, boolean hi) {

        if (hi) {
            return bytes[0] & 0xFF |
                    (bytes[1] & 0xFF) << 8 |
                    (bytes[2] & 0xFF) << 16 ;
        }
        return bytes[3] & 0xFF |
                (bytes[2] & 0xFF) << 8 |
                (bytes[1] & 0xFF) << 16 ;
    }
    /**
     * byte数组转int
     * @param bytes 字节数组
     * @param hi    高位在前,低位在后为true;低位在前,高位在后为false,
     * @return
     */
    public static int bytes2ToInt(byte[] bytes, boolean hi) {

        if (hi) {
            return bytes[1]  & 0xFF |
                    (bytes[0] & 0xFF) << 8 ;
        }
        return bytes[0]  & 0xFF |
                (bytes[1] & 0xFF) << 8 ;
    }

    /**
     * ASCII转string
     * @param bytes
     * @return
     */
    public static String asciiToString(byte[] bytes) {
        return new String( bytes);
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{(byte) ((a >> 24) & 255), (byte) ((a >> 16) & 255), (byte) ((a >> 8) & 255), (byte) (a & 255)};
    }

}
