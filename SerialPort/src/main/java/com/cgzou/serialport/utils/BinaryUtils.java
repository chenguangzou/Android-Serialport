package com.cgzou.serialport.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 二进制与16进制转换
 * @author chenguangzou
 * 2026/5/20 11:36
 */
public class BinaryUtils {

    /**
     * 字符串转成二进制相同的01并返回int
     * @param str
     * @return
     */
    public static int strToBinary(String str){
        char[] strChar=str.toCharArray();
        int intBinary = 0;
        int len=strChar.length-1;
        for(int i=len;i>=0;i--){
            int c =  Integer.parseInt(String.valueOf(strChar[i]));
            int m = (int)Math.pow(2,len-i);
            intBinary+=c*m;
        }
        return intBinary;
    }

    /**
     * 16进制数转2进制字符串
     *
     * @param hexString  16进制字符串
     * @param length     返回的2进制字符串长度
     * @return 2进制字符串
     */
    public static String hexToBinary(String hexString, int length) {
        int num = Integer.parseInt(hexString, 16);
        String result = Integer.toBinaryString(num);
        if (result.length() < length) {
            int diff = length - result.length();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < diff; i++) {
                sb.append("0");
            }
            sb.append(result);

            return sb.toString();
        }
        return result;
    }

    /**
     * 16进制数转2进制列表
     *
     * @param hexString 16进制字符串
     * @param length    返回的2进制字符串长度
     * @return 2进制字符串
     */
    public static List<Integer> hexToBinaryArray(String hexString, int length) {
        int num = Integer.parseInt(hexString, 16);
        // 将整数转换为二进制字符串
        String binaryString = Integer.toBinaryString(num);
        // 创建字符数组
        char[] binaryChars = binaryString.toCharArray();
        // 创建整数数组
        // int[] binaryArray = new int[length];
        List<Integer> binaryList = new ArrayList<>();
        if (binaryChars.length < length) {
            int diff = length - binaryChars.length;
            for (int i = 0; i < diff; i++) {
                binaryList.add(0);
            }
        }
        // 将字符数组中的每个字符转换为其对应的整数值
        //int len = binaryChars.length-1;
        for (int i = 0; i <binaryChars.length; i++) {
            int bit = Character.getNumericValue(binaryChars[i]);
            binaryList.add(bit);
        }
        return binaryList;
    }

    /**
     * 16进制数转2进制字符串
     *
     * @param hexString  16进制字符串
     * @param length     返回的2进制字符串长度
     * @return 2进制字符串
     */
    public static String hexToBinaryLong(String hexString, int length) {
        long num = Long.parseLong(hexString, 16);
        String result = Long.toBinaryString(num);
        if (result.length() < length) {
            int diff = length - result.length();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < diff; i++) {
                sb.append("0");
            }
            sb.append(result);

            return sb.toString();
        }
        return result;
    }

    /**
     *  int数转长度8的2进制字符串
     * @param intBinary int值
     * @return 长度8的2进制字符串
     */
    public static String intToBinaryString(int intBinary){
        //char[] strChar=str.toCharArray();
      /* StringBuilder sb = new StringBuilder(8);
        int binary = 0;
        for(int i=0;i<sb.length();i++){
            int c =  Integer.valueOf(String.valueOf(sb.charAt(i)));
            int m = (int)Math.pow(2,i);
            intBinary+=c*m;
        }*/
        StringBuilder sb = new StringBuilder(8);
        int binary = 0;
        int len=sb.length()-1;
        for(int i=len;i>=0;i--){
            int c =  Integer.parseInt(String.valueOf(sb.charAt(i)));
            int m = (int)Math.pow(2,i);
            intBinary+=c*m;
        }
        return sb.toString();
    }

    /**
     * 16进制数转2进制字符串
     *
     * @param num        int数
     * @param length     返回的2进制字符串长度
     * @return 2进制字符串
     */
    public static String hexToBinary(int num, int length) {
        String result = Integer.toBinaryString(num);
        if (result.length() < length) {
            int diff = length - result.length();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < diff; i++) {
                sb.append("0");
            }
            sb.append(result);

            return sb.toString();
        }
        return result;
    }

    /**
     * 二进制字符串转成16进制字符串
     * @param binaryString
     * @return
     */
    public static String binaryToHexString(String binaryString){
        if (binaryString == null || binaryString.isEmpty()) {
            return null;
        }
        String hexString = "" ;
        int dec = Integer.parseInt(binaryString, 2);
        hexString = Integer.toHexString(dec);
        if (hexString.length() < 2) {
            hexString = "0"+hexString;
        }
        return hexString;
    }
}
