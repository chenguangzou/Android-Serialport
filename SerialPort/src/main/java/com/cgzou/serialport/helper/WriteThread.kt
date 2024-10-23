package com.cgzou.serialport.helper

import android.os.SystemClock
import android.util.Log
import com.cgzou.serialport.utils.Hex2Utils
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 *
 * @dsc:     写线程
 * @Author:  ChenGuangZou
 * @date:    2024/7/23 10:35
 * @Version: 1.0
 */
class WriteThread(time: Long, outputStream: OutputStream?) : Thread() {

    private val tag = "WriteThread"
    private var queueLength = 128
    private val queue: ArrayBlockingQueue<Any?> = ArrayBlockingQueue<Any?>(queueLength)
    private var isWrite: Boolean = false
    private var writeTime = 10L
    private var runOnceTime = 20L
    private var mOutputStream: OutputStream? = null

    init {
        isWrite = true
        writeTime = time
        mOutputStream = outputStream
        mOutputStream?.flush()
    }

    fun setWriteTime(writeTime: Long) {
        this.writeTime = writeTime
    }

    fun getWriteTime(): Long {
        return writeTime
    }

    fun setIsWrite(isWrite: Boolean) {
        this.isWrite = isWrite
    }

    fun getIsWrite(): Boolean {
        return isWrite
    }

    fun putQueue(bytes: ByteArray) {
        queue.put(bytes)
        // queue.offer(bytes,3000,MILLISECONDS)
    }

    fun send(bytes: ByteArray): Boolean {
        if (mOutputStream != null) {
            try {
                val stringBuilder = StringBuilder()
                stringBuilder.append("发送串口数据: ")
                stringBuilder.append(bytes.size)
                Log.d(tag, stringBuilder.toString() + "," + Hex2Utils.bytes2HexString(bytes, bytes.size))
                mOutputStream!!.write(bytes)
                mOutputStream!!.flush()
                Log.d(tag, "===========发送串口数据结束============")
                return true
            } catch (e: IOException) {
                e.printStackTrace();
                Log.d(tag, "===========发送串口数据异常============")
            }
        }
        return false
    }

    override fun run() {
        super.run()
        if (!isWrite) {
            Log.d(tag, "停止写数据...")
            return
        }
        while (!isInterrupted) {
            try {
                if (mOutputStream != null) {
                    if (!queue.isEmpty()) {
                        // val bytes: ByteArray = queue.take() as ByteArray
                        val bytes: ByteArray = queue.poll(20, MILLISECONDS) as ByteArray
                        send(bytes)
                        sleep(writeTime)
                        continue
                    }
                }
                sleep(runOnceTime)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(tag, "串口通讯异常，尝试重启")
                e.printStackTrace()
                return
            }
        }
    }

    fun startWrite() {
        if (mOutputStream != null) {
            isWrite = true
            mOutputStream!!.flush()
        }
    }

    fun stopWrite() {
        isWrite = false
        if (mOutputStream != null) {
            mOutputStream = null
        }
    }

}