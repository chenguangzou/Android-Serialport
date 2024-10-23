package com.cgzou.serialport;

import java.io.File;

/**
 * 串口参数
 *
 * @author chenguangzou
 * @date 2023/7/1 17:08
 */
public class SerialParam {

    // 串口设备文件
    private File device = null;

    // su文件的系统位置
    private String suFilePath = null;

    // 波特率，一般是9600
    private int baudRate = 0;

    // 奇偶校验，0 None, 1 Odd, 2 Even
    private int parity = 0;

    // 数据位，5 - 8 限制了最多8位
    private int dataBits = 8;

    // 停止位，1 或 2
    private int stopBit = 1;

    // 流控，默认0不使用流控，1硬件流控，2软件流控
    private int flowCon = 0;

    // 是否堵塞，默认0为不堵塞，1为堵塞
    private int block = 0;

    // 已有串口打开是否关闭，默认0为不关闭，1为关闭
    private int closePort = 0;

    // 标识，可用于标识市哪个串口也可是其他
    private int flags= 0;

    /**
     * @method       com.cgzou.serialport.SerialPort
     * @description  创建串口对象
     * @author:      chenguangzou
     * @param device       串口设备文件
     * @param suFilePath   su文件的系统位置
     * @param baudRate     波特率，一般是9600
     * @param parity       奇偶校验，0 None, 1 Odd, 2 Even
     * @param dataBits     数据位，5 - 8 限制了最多8位
     * @param stopBit      停止位，1 或 2
     * @param flowCon      流控，默认0不使用流控，1硬件流控，2软件流控
     * @param block        是否堵塞，默认0为不堵塞，1为堵塞
     * @param closePort    已有串口打开是否关闭，默认0为不关闭，1为关闭
     * @param flags        flags默认0
     * @return
     * @@date              2024/10/16 14:18
     */
    public SerialParam(File device, String suFilePath, int baudRate, int parity, int dataBits, int stopBit, int flowCon, int block, int closePort, int flags) {
        this.device = device;
        this.suFilePath = suFilePath;
        this.baudRate = baudRate;
        this.parity = parity;
        this.dataBits = dataBits;
        this.stopBit = stopBit;
        this.flowCon = flowCon;
        this.block = block;
        this.closePort = closePort;
        this.flags = flags;
    }

    public SerialParam(File device, String suFilePath, int baudRate, int flags) {
        this.device = device;
        this.suFilePath = suFilePath;
        this.baudRate = baudRate;
        this.flags = flags;
    }

    public SerialParam(File device, String suFilePath, int baudRate) {
        this.device = device;
        this.suFilePath = suFilePath;
        this.baudRate = baudRate;
    }

    public SerialParam(File device, int baudRate, int flags) {
        this.device = device;
        this.baudRate = baudRate;
        this.flags = flags;
    }

    public SerialParam(File device, int baudRate) {
        this.device = device;
        this.baudRate = baudRate;
    }

    public File getDevice() {
        return device;
    }

    public void setDevice(File device) {
        this.device = device;
    }

    public String getSuFilePath() {
        return suFilePath;
    }

    public void setSuFilePath(String suFilePath) {
        this.suFilePath = suFilePath;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBit() {
        return stopBit;
    }

    public void setStopBit(int stopBit) {
        this.stopBit = stopBit;
    }

    public int getFlowCon() {
        return flowCon;
    }

    public void setFlowCon(int flowCon) {
        this.flowCon = flowCon;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getClosePort() {
        return closePort;
    }

    public void setClosePort(int closePort) {
        this.closePort = closePort;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}
