#include "Recognize.h"

#include <fstream>

JNIEXPORT jboolean JNICALL
Java_nl_clockwork_virm_server_Recognizer_nativeInit(JNIEnv* env, jobject obj, jobjectArray files) {
	unsigned int numFiles = env->GetArrayLength(files);

	for (unsigned int i = 0; i < numFiles; i++) {
		jstring file = (jstring) env->GetObjectArrayElement(files, i);
		trainedDescriptors.push_back(cvMatfromMatFile(env->GetStringUTFChars(file, 0)));
	}

	return true;
}

JNIEXPORT jint JNICALL
Java_nl_clockwork_virm_server_Recognizer_nativeDetect(JNIEnv* env, jobject obj, jstring file) {
	cv::Mat rawImage(cv::imread(env->GetStringUTFChars(file, 0), 0));
	cv::resize(rawImage, image, image.size());

	detector->detect(image, keypoints);
	extractor->compute(image, keypoints, descriptor);

	int bestMatch = -1;
	int bestMatchMatches = 0;
	int position = 0;
	for (auto trainedDescriptor : trainedDescriptors) {
		matcher->match(descriptor, trainedDescriptor, matches);

		unsigned int goodMatches = 0;
		for (auto match : matches) {
			if (match.distance < THRESHOLD) {
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

JNIEXPORT jint JNICALL
Java_nl_clockwork_virm_server_Recognizer_nativeDetectMat(JNIEnv* env, jobject obj, jint rows, jint cols, jobjectArray mat) {
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
			if (match.distance < THRESHOLD) {
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


cv::Mat cvMatfromMatFile(const std::string& fileName) {
	std::ifstream file(fileName.c_str(), std::ios::binary);

	unsigned int rows, cols = 0;
	file.read(reinterpret_cast<char*>(&rows), sizeof(int));
	file.read(reinterpret_cast<char*>(&cols), sizeof(int));

	if (rows > 0 && cols > 0) {
		cv::Mat out(rows, cols, CV_8UC1);
		for (unsigned int row = 0; row < rows; row++) {
			for (unsigned int col = 0; col < cols; col++) {
				int value = 0;
				file.read(reinterpret_cast<char*>(&value), sizeof(int));
				out.row(row).col(col) = value;
			}
		}
		return out;
	}

	return cv::Mat();
}
