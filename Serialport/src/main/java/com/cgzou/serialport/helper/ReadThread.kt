package com.cgzou.serialport.helper

import android.os.SystemClock
import android.util.Log
import com.cgzou.serialport.utils.Hex2Utils
import java.io.BufferedInputStream
import java.nio.ByteBuffer

/**
 *
 * @dsc:     读取线程
 * @Author:  ChenGuangZou
 * @date:    2024/7/23 10:33
 * @Version: 1.0
 */

class  ReadThread :Thread(){

    private val tag = "ReadThread"
    private var byteBufferLength = 256
    private var mBufferedInputStream: BufferedInputStream? = null
    private var lock = Any()
    private var readTime = 2L
    private var nextReadTime = 1L
    private var runOnceTime = 20L
    private var rxArray = ByteArray(byteBufferLength)
    private var rxBuffer = ByteBuffer.wrap(rxArray)
    private var isRead = false

    override fun run() {
        super.run()
        if (!isRead) {
            Log.d(tag, "读取线程停止读取数据...")
            return
        }
        while (!isInterrupted) {
            try {
                if (mBufferedInputStream != null) {
                    var read = -1
                    synchronized(lock) {
                        //read = mBufferedInputStream!!.read(buffer)
                        read = mBufferedInputStream!!.read(rxArray)
                        while (read > 0) {
                            SystemClock.sleep(readTime)
                            val buffer = ByteArray(read)
                            System.arraycopy(rxArray, 0, buffer, 0, read)
                            Log.d(tag, "数据长度:"+buffer.size+"接受数据："+ Hex2Utils.bytes2HexString(buffer,buffer.size))
                            // LogUtil.i(tag, "数据长度:"+buffer.size+"接受数据："+String(buffer))
                            rxBuffer.clear()
                            // readDataListener?.onReadData(buffer, read)
                            read = -1
                        }
                    }
                    sleep(nextReadTime)
                } else {
                    sleep(runOnceTime)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(tag,"串口通讯异常，尝试重启")
                e.printStackTrace()
                // sendBroadcast(Intent(Constant.BROADCAST_SERIAL_ERROR))
                //reopenSerial()
                return
            }
        }
    }

}
