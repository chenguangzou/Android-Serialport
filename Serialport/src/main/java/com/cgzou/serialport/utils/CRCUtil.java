package com.cgzou.serialport.utils;

public class CRCUtil {

    /**
     * 获取验证码 byte数组，基于Modbus CRC16的校验算法
     * @author chenguangzou
     * @date   2023/6/30  11:56
     * @param  arr_buff   字节数据
     * @param  len        要检验的数据长度（减掉校验位之后的长度）
     * @param  hi         是否高位在前，true高位在前，false高位在后
     * @return byte[]
     */
    public static byte[] getCrc16(byte[] arr_buff,int len,boolean hi) {
        // int len = arr_buff.length;

        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                    // 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
            }
        }
        if (hi){
            return intToHiBytes(crc);
        }
        return intToBytes(crc);
    }

    /**
     * 获取验证码byte数组，CRC16的校验算法
     * @author chenguangzou
     * @date   2023/6/30 11:40
     * @param  arr_buff    字节数据
     * @param  start       校验开始位
     * @param  len         要检验的数据长度（减掉校验位之后的长度）
     * @param  hi          是否高位在前，true高位在前，false高位在后
     * @return byte[]
     */
    public static byte[] getCrc16(byte[] arr_buff,int start,int len,boolean hi) {
        // int len = arr_buff.length;

        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = start; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                    // 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
            }
        }
        if (hi){
            return intToHiBytes(crc);
        }
        return intToBytes(crc);
    }

    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int转换成byte数组，高位在前,低位在后
     * 改变高低位顺序只需调换数组序号
     * @param crc
     * @return
     */
    private static byte[] intToHiBytes(int crc) {
        byte[] src = new byte[2];
        src[0] = (byte) ((crc >> 8) & 0xFF);
        src[1] = (byte) (crc & 0xFF);
        return src;
    }
}
