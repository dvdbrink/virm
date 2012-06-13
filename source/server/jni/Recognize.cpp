#include "Recognize.h"

JNIEXPORT jboolean JNICALL
Java_nl_clockwork_virm_server_detect_painting_PaintingDetector_nativeInit(JNIEnv* env, jobject obj, jint t) {
	threshold = t;
	return true;
}

JNIEXPORT jboolean JNICALL
Java_nl_clockwork_virm_server_detect_painting_PaintingDetector_nativeAddTrainedDescriptor(JNIEnv* env, jobject obj, jint rows, jint cols, jobjectArray mat) {
	cv::Mat trainedDescriptor(rows, cols, CV_8UC1);
	for (unsigned int i = 0; i < rows; i++) {
	     jintArray rowArray = (jintArray)env->GetObjectArrayElement(mat, i);
	     jint* row = env->GetIntArrayElements(rowArray, 0);
	     for (unsigned int j = 0; j < cols; j++) {
	    	 trainedDescriptor.row(i).col(j) = row[j];
	     }
	}
	trainedDescriptors.push_back(trainedDescriptor);
	return true;
}

JNIEXPORT jint JNICALL
Java_nl_clockwork_virm_server_detect_painting_PaintingDetector_nativeDetect(JNIEnv* env, jobject obj, jint rows, jint cols, jobjectArray mat) {
	cv::Mat descriptor(rows, cols, CV_8UC1);
	for (unsigned int i = 0; i < rows; i++) {
	     jintArray rowArray = (jintArray)env->GetObjectArrayElement(mat, i);
	     jint* row = env->GetIntArrayElements(rowArray, 0);
	     for (unsigned int j = 0; j < cols; j++) {
	    	 descriptor.row(i).col(j) = row[j];
	     }
	}

	int bestMatch = -1;
	int bestMatchMatches = 0;
	int position = 0;
	for (auto trainedDescriptor : trainedDescriptors) {
		matcher->match(descriptor, trainedDescriptor, matches);

		unsigned int goodMatches = 0;
		for (auto match : matches) {
			if (match.distance < threshold) {
				goodMatches++;
			}
		}

		if (bestMatch == -1 || goodMatches > bestMatchMatches) {
			bestMatch = position;
			bestMatchMatches = goodMatches;
		}

		position++;
	}

	return bestMatch;
}
