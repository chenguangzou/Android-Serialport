package com.cgzou.serialport.utils

import android.util.Log

/**
 *
 * @dsc:     日志工具
 * @Author:  ChenGuangZou
 * @date:    2024/7/22 9:42
 * @Version: 1.0
 */
class LogUtil {

    companion object {

        //  private var LOG_DEBUG =  BuildConfig.DEBUG

        private var LOG_DEBUG =  true

        /**
         * 日志开关
         * @param logEnabled Boolean false关闭日志，true打开日志 ，默认true打开日志
         */
        fun setLog(logEnabled:Boolean){
            LOG_DEBUG = logEnabled
        }
        fun v(tag: String, msg: String) {
            logger("v", tag, msg)
        }

        fun d(tag: String, msg: String) {
            logger("d", tag, msg)
        }

        fun i(tag: String, msg: String) {
            logger("i", tag, msg)
        }

        fun w(tag: String, msg: String) {
            logger("w", tag, msg)
        }

        fun e(tag: String, msg: String) {
            logger("e", tag, msg)
        }

        private fun logger(priority: String, tag: String, msg: String) {
            if (!LOG_DEBUG) {
                return
            }
            when (priority) {
                "v" -> Log.v(tag, msg)
                "d" -> Log.d(tag, msg)
                "i" -> Log.i(tag, msg)
                "w" -> Log.w(tag, msg)
                else -> Log.e(tag, msg)
            }
        }
    }
}