package com.cgzou.serialport.helper

import android.util.Log
import com.cgzou.serialport.interfaces.OnReadSerialDataListener
import com.cgzou.serialport.utils.Hex2Utils

/**
 *
 * @dsc:     串口接收的数据缓存并解析
 * @Author:  ChenGuangZou
 * @date:    2024/7/22 8:50
 * @Version: 1.0
 */
abstract class BufferParse {

    private val tag = "BufferParse"

    // hex字符串大写缓存
    private var buffer = StringBuffer()

    // 缓存大小
    private val bufferSize = 1024

    // 帧头，可用来过滤数据
    private var frameHeader = ""

    // 是否开始解析默认解析
    private var isParse = true


    /**
     * 1.解析数据
     * @param bytes ByteArray?   字节数组
     * @param size Int           数据长度
     */
    fun parse(bytes: ByteArray?, size: Int) {
        putCache(bytes, size)
        val index = buffer.indexOf(frameHeader)
        if (index != -1) {
            deleteMore(index)
            try {
                doParse()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 3.具体的数据解析过程
     */
    abstract fun doParse()


    /**
     * 2.添加字节数组到缓存，之后可以parse
     * @param bytes ByteArray?     字节数组
     * @param size Int             数据长度
     */
    private fun putCache(bytes: ByteArray?, size: Int) {
        if (!isParse) {    //是否开始解析
            return
        }
        if (bytes != null) {
            val data = Hex2Utils.bytesToHexString(bytes, size)
            buffer.append(data)
            if (buffer.length > bufferSize) {
                Log.w(tag, "清理过多数据...")
                buffer.setLength(0)
            }
        }
    }

    /**
     * 移除多余数据
     * @param index Int
     */
    private fun deleteMore(index: Int) {
        if (index > 0) {
            val stringBuilder = StringBuilder()
            stringBuilder.append("移除多余数据：")
            stringBuilder.append(index)
            Log.d(tag, stringBuilder.toString())
        }
        buffer.delete(0, index)
    }

    fun startParse() {
        isParse = true
    }

    fun stopParse() {
        isParse = false
    }

}