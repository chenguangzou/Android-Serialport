package com.cgzou.serialport;

import android.util.Log;

import com.cgzou.serialport.SerialParam;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";

    private FileDescriptor mFd;

    private FileInputStream mFileInputStream;

    private FileOutputStream mFileOutputStream;

    private boolean isOpen = false;

    static {
        System.loadLibrary("serial_port_cgzou");
    }

    /**
     * com.cgzou.serialport.SerialPort
     * @param file       串口设备文件
     * @param suFilePath su文件的系统位置
     * @param baudRate   波特率，一般是9600
     * @param parity     奇偶校验，0 None, 1 Odd, 2 Even
     * @param dataBits   数据位，5 - 8 限制了最多8位
     * @param stopBit    停止位，1 或 2
     * @param flowCon    流控，默认0不使用流控，1硬件流控，2软件流控
     * @param block      是否堵塞，默认0为不堵塞，1为堵塞
     * @param closePort  已有串口打开是否关闭，默认0为不关闭，1为关闭
     * @return
     * @method com.cgzou.serialport.SerialPort
     * @description 创建串口对象
     * @author: cgz
     * @date: 2021/3/19 18:23
     */
    public SerialPort(File file, String suFilePath, int baudRate, int parity, int dataBits, int stopBit, int flowCon, int block, int closePort,int flags) throws SecurityException, IOException {
        OpenSerialPort(file, suFilePath, baudRate, parity, dataBits, stopBit, flowCon, block, closePort,flags);
    }

    /**
     * com.cgzou.serialport.SerialPort
     * @param  param
     * @return
     * @author chenguangzou
     * @date 2023/7/1 17:36
     */
    public SerialPort(SerialParam param) throws SecurityException, IOException {
        if (param != null) {
            OpenSerialPort(param.getDevice(), param.getSuFilePath(), param.getBaudRate(), param.getParity(), param.getDataBits(), param.getStopBit(), param.getFlowCon(), param.getBlock(), param.getClosePort(),param.getFlags());
        }
    }

    /**
     * @param file       串口设备文件
     * @param suFilePath su文件的系统位置
     * @param baudRate   波特率，一般是9600
     * @param parity     奇偶校验，0 None, 1 Odd, 2 Even
     * @param dataBits   数据位，5 - 8 限制了最多8位
     * @param stopBit    停止位，1 或 2
     * @param flowCon    流控，默认0不使用流控，1硬件流控，2软件流控
     * @param block      是否堵塞，默认0为不堵塞，1为堵塞
     * @param closePort  已有串口打开是否关闭，默认0为不关闭，1为关闭
     * @param flags      flags默认是0
     * @return
     * @method com.cgzou.serialport.SerialPort
     * @description 创建串口对象
     * @author: cgz
     * @date: 2021/3/19 18:23
     */
    public void OpenSerialPort(File file, String suFilePath, int baudRate, int parity, int dataBits, int stopBit, int flowCon, int block, int closePort,int flags) throws SecurityException, IOException {

        /* Check access permission 有读写权限可直接打开串口*/
        if (!file.canRead() || !file.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                String suPath;
                if (suFilePath != null) {
                    suPath = suFilePath;
                } else {
                    suPath = getSystemSuFilePath();
                }
                su = Runtime.getRuntime().exec(suPath);
                // su = Runtime.getRuntime().exec("/system/xbin/su");
                //su = Runtime.getRuntime().exec("/system/bin/su");

                String cmd = "chmod 666 " + file.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

              /*if ((su.waitFor() != 0) || !file.canRead()
                        || !file.canWrite()) {
                  Log.e("serialPort","com.cgzou.serialport.SerialPort:没有权限");
                   throw new SecurityException();
                }*/
                if (!file.canRead() || !file.canWrite()) {
                    Log.e("serialPort", "com.cgzou.serialport.SerialPort:没有权限");
                    throw new SecurityException();
                }
                //if ((su.waitFor() != 0)) {
                if (0 != su.waitFor()) {
                    Log.e("serialPort", "com.cgzou.serialport.SerialPort:没有权限");
                    throw new SecurityException();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // throw new SecurityException();
            }
        }
        mFd = open(file.getAbsolutePath(), baudRate, parity, dataBits, stopBit, flowCon, block, closePort,flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
        isOpen = true;
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int parity, int dataBits, int stopBit, int flowCon, int block, int closePort,int flags);

    public native void close();

    //清除串口数据
    public native void tcFlush();

    public static String getSystemSuFilePath() {
        String filepath = "/system/bin/su";
        File f = null;
        final String suPaths[] = {"/system/bin/", "/system/xbin/"};
        try {
            for (int i = 0; i < suPaths.length; i++) {
                f = new File(suPaths[i] + "su");
                if (f != null && f.exists()) {
                    filepath = suPaths[i] + "su";
                    return filepath;
                }
            }
        } catch (Exception e) {
            return filepath;
        }
        return filepath;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * 串口波特率定义
     */
    public enum BAUDRATE {
        B0(0),
        B50(50),
        B75(75),
        B110(110),
        B134(134),
        B150(150),
        B200(200),
        B300(300),
        B600(600),
        B1200(1200),
        B1800(1800),
        B2400(2400),
        B4800(4800),
        B9600(9600),
        B19200(19200),
        B38400(38400),
        B57600(57600),
        B115200(115200),
        B230400(230400),
        B460800(460800),
        B500000(500000),
        B576000(576000),
        B921600(921600),
        B1000000(1000000),
        B1152000(1152000),
        B1500000(1500000),
        B2000000(2000000),
        B2500000(2500000),
        B3000000(3000000),
        B3500000(3500000),
        B4000000(4000000);

        int baudrate;

        BAUDRATE(int baudrate) {
            this.baudrate = baudrate;
        }

        int getBaudrate() {
            return this.baudrate;
        }

    }

    /**
     * 串口停止位定义
     */
    public enum STOPB {
        /**
         * 1位停止位
         */
        B1(1),
        /**
         * 2位停止位
         */
        B2(2);

        int stopBit;

        STOPB(int stopBit) {
            this.stopBit = stopBit;
        }

        public int getStopBit() {
            return this.stopBit;
        }

    }

    /**
     * 串口数据位定义
     */
    public enum DATAB {
        /**
         * 5位数据位
         */
        CS5(5),
        /**
         * 6位数据位
         */
        CS6(6),
        /**
         * 7位数据位
         */
        CS7(7),
        /**
         * 8位数据位
         */
        CS8(8);

        int dataBit;

        DATAB(int dataBit) {
            this.dataBit = dataBit;
        }

        public int getDataBit() {
            return this.dataBit;
        }
    }

    /**
     * 串口校验位定义
     */
    public enum PARITY {
        /**
         * 无奇偶校验
         */
        NONE(0),
        /**
         * 奇校验
         */
        ODD(1),
        /**
         * 偶校验
         */
        EVEN(2);

        int parity;

        PARITY(int parity) {
            this.parity = parity;
        }

        public int getParity() {
            return this.parity;
        }
    }

    /**
     * 串口流控定义
     */
    public enum FLOWCON {
        /**
         * 不使用流控
         */
        NONE(0),
        /**
         * 硬件流控
         */
        HARD(1),
        /**
         * 软件流控
         */
        SOFT(2);

        int flowCon;

        FLOWCON(int flowCon) {
            this.flowCon = flowCon;
        }

        public int getFlowCon() {
            return this.flowCon;
        }
    }

}
