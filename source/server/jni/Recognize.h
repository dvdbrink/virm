#ifndef VIRM_RECOGNIZE_H
#define VIRM_RECOGNIZE_H

#include <memory>
#include <vector>

#include <opencv2/opencv.hpp>

#include <jni.h>

extern "C" {
	static int threshold, minGoodMatches;

	static std::shared_ptr<cv::FeatureDetector> detector = std::make_shared<cv::OrbFeatureDetector>();
	static std::shared_ptr<cv::DescriptorExtractor> extractor = std::make_shared<cv::OrbDescriptorExtractor>();
	static std::shared_ptr<cv::DescriptorMatcher> matcher = std::make_shared<cv::BFMatcher>(cv::NORM_HAMMING);

	static std::vector<cv::Mat> trainedDescriptors;
	static cv::Mat image = cv::Mat(150, 150, CV_8UC1);
	static cv::vector<cv::KeyPoint> keypoints;
	static cv::vector<cv::DMatch> matches;
	static cv::Mat descriptor;

	JNIEXPORT jboolean JNICALL
	Java_nl_clockwork_virm_server_detect_painting_PaintingDetector_nativeInit(JNIEnv*, jobject, jint t, jint mgm);

	JNIEXPORT jboolean JNICALL
	Java_nl_clockwork_virm_server_detect_painting_PaintingDetector_nativeAddTrainedDescriptor(JNIEnv*, jobject, jint rows, jint cols, jobjectArray mat);

	JNIEXPORT jint JNICALL
	Java_nl_clockwork_virm_server_detect_painting_PaintingDetector_nativeDetect(JNIEnv*, jobject, jint rows, jint cols, jobjectArray mat);
}

#endif // VIRM_RECOGNIZE_H
