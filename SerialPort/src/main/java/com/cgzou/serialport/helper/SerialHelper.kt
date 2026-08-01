package com.cgzou.serialport.helper

import android.os.SystemClock
import com.cgzou.serialport.SerialParam
import com.cgzou.serialport.SerialPort
import com.cgzou.serialport.SerialPortFinder
import com.cgzou.serialport.interfaces.OnReadSerialDataListener
import com.cgzou.serialport.utils.Hex2Utils
import com.cgzou.serialport.utils.LogUtil
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 串口帮助类
 * @dsc:     类作用描述
 * @Author:  ChenGuangZou
 * @date     2024/10/22 9:56
 * @Version: 1.0
 */
class SerialHelper {

    private val tag = javaClass.simpleName

    private var mReadThread: ReadThread? = null

    var mWriteThread: WriteThread? = null

    private var mSendPool: ExecutorService? = null

    private var mRunnable: SendRunnable? = null

    private var mSerialPort: SerialPort? = null

    var mInputStream: InputStream? = null

    var mOutputStream: OutputStream? = null

    private var mBufferedInputStream: BufferedInputStream? = null

    // 缓存包的字节长度
    var readBufferLength = 128

    private var rxArray = ByteArray(readBufferLength)

    private var rxBuffer = ByteBuffer.wrap(rxArray)

    private var lock = Any()

    // 读取数据时间
    var readTime = 1L

    // 读取数据的时间间隔
    var readOnceTime = 2L

    // 输入流为空的时候，线程运行一次的时间
    private var runOnceTime = 20L

    var isRead = false

    private var mPort = "/dev/ttyS1"

    var isOpen = false

    var mListener: OnReadSerialDataListener? = null

    var bufferParse: BufferParse? = null


    fun openSerial(port: String, baudRate: Int) {
        mPort = port
        openSerial(SerialParam(File(mPort), baudRate))
    }

    fun openSerial(param: SerialParam) {
        if (isRead) {    // 串口已经正常运行返回
            return
        }
        try {
            mSerialPort = SerialPort(param)
            mSerialPort!!.tcFlush()
            mOutputStream = mSerialPort!!.outputStream
            mInputStream = mSerialPort!!.inputStream
            if (mInputStream != null) {
                mBufferedInputStream = BufferedInputStream(mInputStream)
            }
            startReadThread()
            // startWriteThread()
            isOpen = mSerialPort!!.isOpen
            LogUtil.d(
                tag,
                ("开启串口成功 " + param.file.absolutePath + " 波特率 " + param.baudRate)
            )
        } catch (e: java.lang.Exception) {
            LogUtil.e(tag, "open serial failed")
            LogUtil.d(tag, "开启串口失败 " + param.file.absolutePath + " 波特率 " + param.baudRate)
            e.printStackTrace()
            // sendBroadcast(Intent(Constant.BROADCAST_SERIAL_ERROR))
        }
    }

    /**
     * 发送一次串口数据
     * @param  bytes ByteArray
     * @return Boolean
     */
    fun send(bytes: ByteArray): Boolean {
        if (mOutputStream != null) {
            try {
                mOutputStream!!.write(bytes)
                mOutputStream!!.flush()
                val stringBuilder = StringBuilder()
                stringBuilder.append("发送串口数据: ")
                stringBuilder.append(bytes.size)
                stringBuilder.append("," + Hex2Utils.bytesToHex2String(bytes, bytes.size))
                LogUtil.d(tag, stringBuilder.toString())
                LogUtil.d(tag, "===========发送串口数据结束============")
                return true
            } catch (e: IOException) {
                e.printStackTrace();
                LogUtil.d(tag, "===========发送串口数据异常============")
            }
        }
        return false
    }


    /**
     * 使用写线程发送数据
     * @param bytes ByteArray
     */
    fun sendByQueue(bytes: ByteArray) {
        mWriteThread?.putQueue(bytes)
    }

    /**
     * 使用线程池发送数据
     * @param runnable 自定义Runnable
     */
    fun send(runnable: Runnable) {
        if (mSendPool == null) {
            mSendPool = Executors.newSingleThreadExecutor()
        }
        mSendPool?.execute(runnable)
    }

    /**
     * 使用线程池发送数据,内置的Runnable
     * @param bytes
     */
    fun sendByRunnable(bytes: ByteArray) {
        if (mSendPool == null) {
            mSendPool = Executors.newSingleThreadExecutor()
        }
        if (mRunnable == null) {
            mRunnable = SendRunnable()
        }
        mRunnable!!.bytes = bytes
        mSendPool?.execute(mRunnable)
    }

    private fun closeReadThread() {
        isRead = false
        if (mReadThread != null && mReadThread!!.isAlive) {
            if (!mReadThread!!.isInterrupted) {
                mReadThread!!.interrupt()
            }
            mReadThread = null
        }
    }

    private fun startReadThread() {
        isRead = true
        if (mReadThread == null) {
            mReadThread = ReadThread()
            mReadThread!!.start()
        } else if (mReadThread != null && mReadThread!!.isAlive) {
            if (mReadThread!!.isInterrupted) {
                mReadThread = ReadThread()
                mReadThread!!.start()
            } else {
                mReadThread!!.interrupt()
                mReadThread!!.start()
            }
        }
    }

    fun startWriteThread() {
        if (mWriteThread == null) {
            mWriteThread = WriteThread(10L, mOutputStream)
            mWriteThread!!.start()
            mWriteThread!!.startWrite()
        } else if (mWriteThread != null && mWriteThread!!.isAlive) {
            if (mWriteThread!!.isInterrupted) {
                mWriteThread = WriteThread(10L, mOutputStream)
                mWriteThread!!.start()
            } else {
                mWriteThread!!.interrupt()
                mWriteThread!!.start()
            }
        }
    }

    fun closeWriteThread() {
        if (mWriteThread != null && mWriteThread!!.isAlive) {
            mWriteThread!!.stopWrite()
            if (!mWriteThread!!.isInterrupted) {
                mWriteThread!!.interrupt()
            }
            mWriteThread = null
        }
    }

    fun closeSerial() {
        try {

            closeReadThread()
            closeWriteThread()
            if (mSerialPort != null) {
                mSerialPort!!.close()
                mSerialPort = null
                isOpen = false
            }
            mBufferedInputStream?.close()
            mBufferedInputStream = null
            mInputStream?.close()
            mInputStream = null
            mOutputStream?.close()
            mOutputStream = null
            mSendPool?.shutdownNow()
            mSendPool = null

            rxBuffer.clear()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 获取设备路径列表
     * @return Array<String>
     */
    fun getDevicePaths(): Array<String> {
        val spFinder: SerialPortFinder = SerialPortFinder()
        return spFinder.allDevicesPath
    }

    private inner class ReadThread : Thread() {
        override fun run() {
            super.run()
            if (!isRead) {
                LogUtil.d(tag, "串口读取线程停止读取数据...")
                return
            }
            while (!isInterrupted) {
                try {
                    if (mBufferedInputStream != null) {
                        var read: Int
                        synchronized(lock) {
                            read = -1
                            read = mBufferedInputStream!!.read(rxArray)
                            while (read > 0) {
                                SystemClock.sleep(readTime)
                                val buffer = ByteArray(read)
                                System.arraycopy(rxArray, 0, buffer, 0, read)
                                rxBuffer.clear()
                                read = -1
                                bufferParse?.parse(buffer, buffer.size)
                                mListener?.onReceive(buffer, buffer.size)
                            }
                        }
                        sleep(readOnceTime)
                    } else {
                        sleep(runOnceTime)
                    }
                } catch (e: Exception) {
                    LogUtil.e(tag, "串口通讯异常，尝试重启")
                    e.printStackTrace()
                    // reopenSerial()
                    return
                }
            }
        }
    }

    private inner class SendRunnable : Runnable {
        var bytes: ByteArray? = null
        override fun run() {
            // 如果 bytes 为 null，send 不会被调用；如果不为 null，自动解包并传入
            bytes?.let { nonNullBytes ->
                send(nonNullBytes)
            }
        }
    }


}