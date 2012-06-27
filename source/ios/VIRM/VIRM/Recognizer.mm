//
//  Recognizer.m
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This class contains the recognition logic of the application.
// It can be initiated with a dataset of Mat types.
// Goes through the three steps needed by OpenCV to recognize an image.
// Detection, then extraction of features. Then matching with the dataset.
//

#import "Recognizer.h"
#import "AppDelegate.h"

#include <vector>

using namespace std;
using namespace cv;

@implementation Recognizer


// Init method. The dataset parameter should contain descriptors of your entire dataset.
- (id) initWithDataSet:(vector<cv::Mat>)dataset {
    if (self = [super init]) {
        
        dataSetDescriptors = dataset;
        imageId = -1;
        appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        
        utils = [[Utils alloc] init];
    }
    return self;
}


// A simple test method that returns the first Mat in the dataset.
- (Mat) getTestMat {
    return dataSetDescriptors[0];
}


// The recognize method. This is called with a UIImage (camera capture) as parameter.
// The method retrieves descriptors, matches with the dataset en returns the imageId.
- (int) recognize:(UIImage *)image {

    Mat capturedDescriptors = [self getDescriptors:image];
    imageId = [self match:capturedDescriptors];
    capturedDescriptors.release();
    return imageId;
}

// Method to retrieve descriptors from a UIImage (camera capture).
- (Mat) getDescriptors:(UIImage *)image {   
    // Create an empty list of keypoints.
    vector<KeyPoint>keypoints;
    
    // Create an empty Mat.
    Mat capturedDescriptors;
    
    // Create an empty, grayscaled Mat.
    Mat grayImage(appDelegate.imageDimensions, appDelegate.imageDimensions, CV_8UC1);
    
    // Convert the camera capture to Mat format.
    Mat capture = [utils MatFromUIImage:image];
    
    // Resize the capture into the grayimage. This resizes the capture and converts it to grayscale.
    cv::resize(capture, grayImage, grayImage.size());
    
    // Detect features on the capture and store them in the keypoints list.
    featureDetector.detect(grayImage, keypoints);
    
    // Extract the features and store them in capturedDescriptors.
    featureExtractor.compute(grayImage, keypoints, capturedDescriptors);

    grayImage.release();
    capture.release();
    
    return capturedDescriptors;
}

// This method matches the descriptors from the camera capture with the entire dataset.
// Return -1 if no match was found, and the imageId (place in list) if it did match.
- (int) match:(cv::Mat)capturedMat {
    BFMatcher matcher(NORM_HAMMING);   
    vector<DMatch> matches;
    
    imageId = -1;
    int bestMatch = 0;
    int goodMatches;
    
    // Loop through the entire dataset.
    for(int i=0; i < dataSetDescriptors.size(); i++) {
        // Clear previous results.
        matches.clear();
        goodMatches = 0;
        
        // Match and save matches in list (matches).
        matcher.match(capturedMat, dataSetDescriptors[i], matches);        
        
        // Loop through all the matches and check if they are good enough.
        // A good enough match has a low distance. (lower than maxDistance).
        for(int k = 0; k < capturedMat.rows; k++ ) {
            if( matches[k].distance < appDelegate.maxDistance ) {
                goodMatches++;   
            }
        }
        
        // A simple check to see which imageId was the best match so far.
        if(goodMatches > bestMatch) {
            bestMatch = goodMatches;
            imageId = i;
        }
        
        // Check if the amount of good matches is higher than the amount of matches needed.
        // If so: return the imageId.        
        if(goodMatches > appDelegate.matchesNeeded) {                         
            return imageId;           
        }
        
    }
    return -1;
}

@end