package com.cgzou.serialport.utils;

import java.util.List;

public class CHKUtil {

    /**
     * 命令计算校验码在尾部追加
     *
     */
    public static  byte[] calculateChk1(byte[] cmd)
    {
        byte[] ret = new byte[cmd.length + 1];
        byte chk = 0;
        for (int index = 0; index < (ret.length - 1); index++)
        {
            ret[index] = cmd[index];
            if (ret[index] == 0x02 && index != 0) ret[index] = (byte)(ret[index] + 0x20);
            if (ret[index] == 0x03 && index != ret.length - 2) ret[index] = (byte)(ret[index] + 0x20);
            // byte和byte异或操作之后就会被默认转成int型，所以每次操作之后都要强行转换成byte
            chk = (byte)(chk ^ ret[index]);//异或
        }
        if (chk == 0x02 || chk == 0x03)
            chk = (byte)(chk + 0x20);
        ret[ret.length - 1] = chk;

        return ret;
    }

    public static byte[] calculateChk(byte[] cmd)
    {
        byte[] ret = new byte[cmd.length + 1];

        byte chk = 0;
        for (int index = 0; index < (ret.length - 1); index++)
        {
            ret[index] = cmd[index];
    		/*if (ret[index] == 0x02 && index != 0) ret[index] = (byte)(ret[index] + 0x20);
    		if (ret[index] == 0x03 && index != ret.length - 2) ret[index] = (byte)(ret[index] + 0x20);*/
            System.out.println(chk);
            chk^=ret[index];


            //System.out.print(byteToHexStr(cmd));
    		/*if (chk == 0x02 || chk == 0x03) {
        		chk = (byte)(chk + 0x20);
        	}*/

        }
        if (chk == 0x02 || chk == 0x03) {
            chk = (byte)(chk + 0x20);
        }
        ret[ret.length - 1] = chk;
        //System.out.print(chk);
        return ret;
    }
    public static int getChk(byte[] cmd,int len)
    {
        byte[] ret = new byte[len];

        byte chk = 0;
        for (int index = 0; index < len; index++)
        {
            ret[index] = cmd[index];
            chk^=ret[index];

        }
        return chk;
    }

    /**
     * BE1105接收数据校验
     * @param subList
     * @param len
     * @return
     */
    public static int checkChk_BE1105(List<Byte> subList,int len)
    {
        byte[] ret = new byte[len];

        byte chk = 0;
        for (int index = 0; index < len; index++)
        {
            ret[index] = subList.get(index);
            chk^=ret[index];

        }
        return chk;
    }
    public static  byte readChk(List<Byte> subList)
    {
        int len = subList.size();
        byte[] bytes = new byte[len];
        byte chk = 0;
        for (int index = 0; index < (len - 2); index++)
        {
            bytes[index ] = subList.get(index ).byteValue();
            if (bytes[index ]== 0x02 && index != 0) {
                bytes[index ] = (byte)(bytes[index ] + 0x20);
            }
            if (bytes[index ] == 0x03 && index != bytes.length - 2){
               bytes[index ]= (byte)(bytes[index ] + 0x20);
              }
            // byte和byte异或操作之后就会被默认转成int型，所以每次操作之后都要强行转换成byte
            chk = (byte)(chk ^ bytes[index ]);//异或
        }
        if (chk == 0x02 || chk == 0x03)
            chk = (byte)(chk + 0x20);
        if (bytes[len-1] ==chk){
            return chk ;
        }
        return 0;
    }

}
