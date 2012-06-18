#ifndef VIRM_RECOGNIZEGPU_H
#define VIRM_RECOGNIZEGPU_H

#include <string>
#include <vector>

#include <opencv2/core/core.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/gpu/gpu.hpp>

#include <jni.h>

extern "C" {
	static const int THRESHOLD = 35;
	static std::vector<cv::gpu::GpuMat> trainedDescriptors;
	static cv::vector<cv::DMatch> matches;

	JNIEXPORT jboolean JNICALL Java_nl_clockwork_virm_server_Recognizer_nativeInit(JNIEnv*, jobject, jobjectArray);

	JNIEXPORT jint JNICALL Java_nl_clockwork_virm_server_Recognizer_nativeDetect(JNIEnv*, jobject, jstring);
	JNIEXPORT jint JNICALL Java_nl_clockwork_virm_server_Recognizer_nativeDetectMat(JNIEnv* env, jobject obj, jint rows, jint cols, jobjectArray mat);

	cv::gpu::GpuMat cvMatfromMatFile(const std::string&);
}

#endif // VIRM_RECOGNIZEGPU_H
