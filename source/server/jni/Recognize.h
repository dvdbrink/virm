// g++ -fPIC -shared -I/usr/lib/jvm/java-7-openjdk-i386/include Recognize.cpp -L/usr/local/lib -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_features2d -o ../librecognize.so

#ifndef VIRM_RECOGNIZE_H
#define VIRM_RECOGNIZE_H

#include <memory>
#include <string>
#include <vector>

#include <opencv2/opencv.hpp>

#include <jni.h>

extern "C" {
	static const int THRESHOLD = 35;

	static std::shared_ptr<cv::FeatureDetector> detector = std::make_shared<cv::OrbFeatureDetector>();
	static std::shared_ptr<cv::DescriptorExtractor> extractor = std::make_shared<cv::OrbDescriptorExtractor>();
	static std::shared_ptr<cv::DescriptorMatcher> matcher = std::make_shared<cv::BFMatcher>(cv::NORM_HAMMING);

	static std::vector<cv::Mat> trainedDescriptors;
	static cv::Mat image = cv::Mat(150, 150, CV_8UC1);
	static cv::vector<cv::KeyPoint> keypoints;
	static cv::vector<cv::DMatch> matches;
	static cv::Mat descriptor;

	JNIEXPORT jboolean JNICALL Java_nl_clockwork_virm_logic_Recognizer_nativeInit(JNIEnv*, jobject, jobjectArray);

	JNIEXPORT jint JNICALL Java_nl_clockwork_virm_logic_Recognizer_nativeDetect(JNIEnv*, jobject, jstring);
	JNIEXPORT jint JNICALL Java_nl_clockwork_virm_logic_Recognizer_nativeDetectMat(JNIEnv* env, jobject obj, jint rows, jint cols, jobjectArray mat);

	cv::Mat cvMatfromMatFile(const std::string&);
}

#endif // VIRM_RECOGNIZE_H
