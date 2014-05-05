/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_apache_hadoop_io_compress_bzip2_Bzip2Compressor */

#ifndef _Included_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
#define _Included_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
#ifdef __cplusplus
extern "C" {
#endif
#undef org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_DEFAULT_DIRECT_BUFFER_SIZE
#define org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_DEFAULT_DIRECT_BUFFER_SIZE 65536L
#undef org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_DEFAULT_BLOCK_SIZE
#define org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_DEFAULT_BLOCK_SIZE 9L
#undef org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_DEFAULT_WORK_FACTOR
#define org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_DEFAULT_WORK_FACTOR 30L
/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    initIDs
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_initIDs
  (JNIEnv *, jclass, jstring);

/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    init
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_init
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    deflateBytesDirect
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_deflateBytesDirect
  (JNIEnv *, jobject);

/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    getBytesRead
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_getBytesRead
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    getBytesWritten
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_getBytesWritten
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    end
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_end
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_apache_hadoop_io_compress_bzip2_Bzip2Compressor
 * Method:    getLibraryName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_apache_hadoop_io_compress_bzip2_Bzip2Compressor_getLibraryName
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif