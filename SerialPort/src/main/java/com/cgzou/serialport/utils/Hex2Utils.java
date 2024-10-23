package com.cgzou.serialport.utils;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class Hex2Utils {

    public static int isOdd(int num) {
        return num & 1;
    }

    public static int hexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * 字节转string  00
     *
     * @param intByte 字节
     * @return
     */
    public static String byte2Hex(Byte intByte) {
        return String.format("%02x", new Object[]{intByte}).toUpperCase();
    }

    /**
     * 字节转string  0x00
     *
     * @param intByte 字节
     * @return
     */
    //0x
    public static String byte0x2Hex(Byte intByte) {
        return String.format("0x%02x", new Object[]{intByte}).toUpperCase();

    }

    /**
     * 16位字符串转字节数组
     *
     * @param inHex
     * @return
     */
    public static byte[] hexToBytes(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * byte[]转string  0A0A
     *
     * @param src
     * @param len
     * @return
     */
    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length == 0 || len <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase(Locale.getDefault());

    }

    /**
     * byte[]转string 00 00
     *
     * @param src
     * @return
     */
    public static String bytes2HexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length == 0 || len <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            String hv = byte2Hex(src[i]);
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * byte[]转string 0x00 0x00
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexString0x(byte[] bytes, boolean is0x2) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : bytes) {
            if (is0x2) {
                strBuilder.append(byte0x2Hex(Byte.valueOf(valueOf)));
            } else {
                strBuilder.append(byte2Hex(Byte.valueOf(valueOf)));
            }
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }


    public static String hexStringToString(String s) {
        Exception e1;
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[(s.length() / 2)];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16) & 255);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String s2 = new String(baKeyword, StandardCharsets.UTF_8);
            try {
                String str = new String();
                return s2;
            } catch (Exception e2) {
                e1 = e2;
                s = s2;
                e1.printStackTrace();
                return s;
            }
        } catch (Exception e3) {
            e1 = e3;
            e1.printStackTrace();
            return s;
        }
    }

    /**
     * 字节list转16进制字符串
     */
    public static String arrBytes2HexString(List<Byte> bytes) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < bytes.size(); i++) {
            strBuilder.append(byte2Hex(Byte.valueOf(bytes.get(i))));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    public static byte[] toBytes(char[] chars) {
        Charset cs = StandardCharsets.UTF_8;
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        return cs.encode(cb).array();
    }

    public static char[] toChars(byte[] bytes) {
        Charset cs = StandardCharsets.UTF_8;
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        return cs.decode(bb).array();
    }

}
