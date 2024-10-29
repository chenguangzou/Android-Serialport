package com.cgzou.serialport.utils;

import android.util.Log;

/**
 * 二进制字符串转换
 * @author chenguangzou
 * @version 1.0
 * @date 2023/11/11 10:54
 */
public class BinaryUtil {

    /**
     * 字符串转成二进制相同的01并返回int
     * @param str
     * @return
     */
    public static int strToBinary(String str){
        Log.i("strToBinary",str);
        char[] strChar=str.toCharArray();
        int intBinary = 0;
        int len=strChar.length-1;
        for(int i=len;i>=0;i--){
            int c =  Integer.valueOf(String.valueOf(strChar[i]));
            int m = (int)Math.pow(2,len-i);
            intBinary+=c*m;
        }
        return intBinary;
    }

    public static String intToBinaryString(int intBinary){
        StringBuilder sb = new StringBuilder(8);
        int binary = 0;
        int len=sb.length()-1;
        for(int i=len;i>=0;i--){
            int c =  Integer.valueOf(String.valueOf(sb.charAt(i)));
            int m = (int)Math.pow(2,i);
            intBinary+=c*m;
        }
        return sb.toString();
    }

    /**
     * 二进制字符串转成16进制字符串
     * @param binaryString
     * @return
     */
    public static String binaryToHexString(String binaryString){
        if (binaryString == null || binaryString.length() == 0 ) {
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
