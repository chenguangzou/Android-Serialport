/*
 * Copyright 2009-2011 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>
#include "SerialPort.h"
//#include "com_cgzou_serialport_SerialPort.h"

#include "android/log.h"

static const char *TAG = "serial_port";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
extern "C" {
static speed_t getBaudrate(jint baudrate) {
    switch (baudrate) {
        case 0:
            return B0;
        case 50:
            return B50;
        case 75:
            return B75;
        case 110:
            return B110;
        case 134:
            return B134;
        case 150:
            return B150;
        case 200:
            return B200;
        case 300:
            return B300;
        case 600:
            return B600;
        case 1200:
            return B1200;
        case 2400:
            return B2400;
        case 4800:
            return B4800;
        case 9600:
            return B9600;
        case 19200:
            return B19200;
        case 1800:
            return B1800;
        case 38400:
            return B38400;
        case 57600:
            return B57600;
        case 115200:
            return B115200;
        case 230400:
            return B230400;
        case 460800:
            return B460800;
        case 500000:
            return B500000;
        case 576000:
            return B576000;
        case 921600:
            return B921600;
        case 1000000:
            return B1000000;
        case 1152000:
            return B1152000;
        case 1500000:
            return B1500000;
        case 2000000:
            return B2000000;
        case 2500000:
            return B2500000;
        case 3000000:
            return B3000000;
        case 3500000:
            return B3500000;
        case 4000000:
            return B4000000;
        default:
            return -1;
    }
}
static void throwException(JNIEnv *env, const char *name, const char *msg) {
    jclass cls = env->FindClass(name);
/* if cls is NULL, an exception has already been thrown */
    if (cls != NULL) {
        env->ThrowNew(cls, msg);
    }

/* free the local ref */
    env->DeleteLocalRef(cls);
}

/*
 * Class:     android_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_cgzou_serialport_SerialPort_open
        (JNIEnv *env, jclass clazz, jstring path, jint baudrate, jint parity, jint dataBits,
         jint stopBit, jint flowCon, jint block, jint closePort, jint flags) {
    int fd;
   // int flags = 0;
    speed_t speed;
    jobject mFileDescriptor;


/* Check arguments */
    {
        speed = getBaudrate(baudrate);
        if (speed == -1) {
            throwException(env, "java/lang/IllegalArgumentException", "Invalid baudrate");
            return NULL;
        }
        if (parity < 0 || parity > 2) {
            throwException(env, "java/lang/IllegalArgumentException", "Invalid parity");
            return NULL;
        }
        if (dataBits < 5 || dataBits > 8) {
            throwException(env, "java/lang/IllegalArgumentException", "Invalid dataBits");
            return NULL;
        }
        if (stopBit < 1 || stopBit > 2) {
            throwException(env, "java/lang/IllegalArgumentException", "Invalid stopBit");
            return NULL;
        }
    }

/* Opening device */
    {
        jboolean iscopy;
        const char *path_utf = env->GetStringUTFChars(path, &iscopy);
        if (block ==1) {                                                                      //1就使用堵塞方式打开
            fd = open(path_utf, O_RDWR | flags);
        } else if (block == 0) {
            fd = open(path_utf, O_RDWR | O_NOCTTY | O_NONBLOCK | O_NDELAY | flags); //0就使用非堵塞方式打开
        }

        env->ReleaseStringUTFChars(path, path_utf);
        if (fd == -1) {
            throwException(env, "java/io/IOException", "Cannot open port");
            return NULL;
        }
    }

/* Configure device */
    {
        struct termios cfg;
//VTIME和VMIN仅在阻塞模式下有效
        if (block == 1) {
            cfg.c_cc[VTIME] = 10;
            cfg.c_cc[VMIN] = 0;
        }
        if (tcgetattr(fd, &cfg)) {
            close(fd);
            throwException(env, "java/io/IOException", "tcgetattr() failed");
            return NULL;
        }

        cfmakeraw(&cfg);
        cfsetispeed(&cfg, speed);
        cfsetospeed(&cfg, speed);

       /* More attribute set */
        cfg.c_cflag &= ~CSIZE;                    //先屏蔽其他标志紧接着将数据位修改为8bit
        switch (dataBits) {
            case 5:
                cfg.c_cflag |= CS5;
                break;      //使用5位数据位
            case 6:
                cfg.c_cflag |= CS6;
                break;      //使用6位数据位break;
            case 7:
                cfg.c_cflag |= CS7;
                break;      //使用7位数据位
            case 8:
                cfg.c_cflag |= CS8;
                break;      //使用8位数据位
            default:
                cfg.c_cflag |= CS8;
                break;     //默认8位数据位
        }
        switch (parity) {
            case 0:
                cfg.c_cflag &= ~PARENB;
                break;             //无奇偶校验
            case 1:
                cfg.c_cflag |= (PARODD | PARENB);
                break;   //奇校验
            case 2:
                cfg.c_iflag &= ~(IGNPAR | PARMRK);            // 偶校验
                cfg.c_iflag |= INPCK;
                cfg.c_cflag |= PARENB;
                cfg.c_cflag &= ~PARODD;
                break;
            default:
                cfg.c_cflag &= ~PARENB;
                break;             //默认无奇偶校验
        }
        switch (stopBit) {
            case 0:
                break;                           //无停止位
            case 1:
                cfg.c_cflag &= ~CSTOPB;
                break;    //1位停止位
            case 2:
                cfg.c_cflag |= CSTOPB;
                break;    //2位停止位
            default:
                break;                           //默认无停止位
        }
// hardware flow control
        switch (flowCon) {
            case 0:
                cfg.c_cflag &= ~CRTSCTS;
                break;             //不使用流控
            case 1:
                cfg.c_cflag |= CRTSCTS;
                break;              //硬件流控
            case 2:
                cfg.c_cflag |= IXON | IXOFF | IXANY;
                break; //软件流控
            default:
                cfg.c_cflag &= ~CRTSCTS;
                break;            //默认不使用流控
        }

        int rc = tcsetattr(fd, TCSANOW, &cfg);
        if (rc) {
            if (closePort == 1) {
                close(fd);//已有串口打開就关闭
                LOGD("tcsetattr() failed", rc);
                throwException(env, "java/io/IOException", strcat("tcsetattr() failed: ", rc + ""));
            }
            return NULL;
        }

    }

/* Create a corresponding file descriptor */
    {
        jclass cFileDescriptor = env->FindClass("java/io/FileDescriptor");
        jmethodID iFileDescriptor = env->GetMethodID(cFileDescriptor, "<init>", "()V");
        jfieldID descriptorID = env->GetFieldID(cFileDescriptor, "descriptor", "I");
        mFileDescriptor = env->NewObject(cFileDescriptor, iFileDescriptor);
        env->SetIntField(mFileDescriptor, descriptorID, (jint) fd);
    }

    return mFileDescriptor;
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */

JNIEXPORT void JNICALL Java_com_cgzou_serialport_SerialPort_close
        (JNIEnv *env, jobject thiz) {
    jclass SerialPortClass = env->GetObjectClass(thiz);
    jclass FileDescriptorClass = env->FindClass("java/io/FileDescriptor");

    jfieldID mFdID = env->GetFieldID(SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
    jfieldID descriptorID = env->GetFieldID(FileDescriptorClass, "descriptor", "I");
    jobject mFd = env->GetObjectField(thiz, mFdID);
    jint descriptor = env->GetIntField(mFd, descriptorID);

// LOGD("close(fd = %d)", descriptor);
    close(descriptor);
}

/*
*Class : android_serial_SerialPort
*Method : tcflush
*Signature ()V
*/
JNIEXPORT void JNICALL Java_com_cgzou_serialport_SerialPort_tcFlush
        (JNIEnv *env, jobject thiz) {

    jclass SerialPortClass = env->GetObjectClass(thiz);
    jclass FileDescriptorClass = env->FindClass("java/io/FileDescriptor");

    jfieldID mFdID = env->GetFieldID(SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
    jfieldID descriptorID = env->GetFieldID(FileDescriptorClass, "descriptor", "I");
    jobject mFd = env->GetObjectField(thiz, mFdID);
    jint descriptor = env->GetIntField(mFd, descriptorID);

// LOGD("tcflush(fd = %d)", descriptor);
    tcflush(descriptor, TCIOFLUSH);

}
}