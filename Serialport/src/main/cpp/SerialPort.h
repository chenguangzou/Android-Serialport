#include <jni.h>

//
// Created by Administrator on 2020/9/4.
//


/* Header for class om_serialport_SerialPort */

#ifndef _Included_com_cgzou_serialport_SerialPort
#define _Included_com_cgzou_serialport_SerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_cgzou_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_cgzou_serialport_SerialPort_open
        (JNIEnv *env, jclass clazz,jstring path, jint baudrate,jint parity, jint dataBits,jint stopBit, jint flowCon,jint block,jint closePort, jint flags);

/*
 * Class:     android_serialport_api_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_cgzou_serialport_SerialPort_close(JNIEnv *, jobject);
/*
 * Class:     om_serialport_SerialPort
 * Method:    tcflush
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_cgzou_serialport_SerialPort_tcFlush(JNIEnv *env, jobject thiz) ;
#ifdef __cplusplus
}
#endif
#endif
