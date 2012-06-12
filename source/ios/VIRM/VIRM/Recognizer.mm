//
//  Recognizer.m
//  VIRM
//
//  Created by Clockwork Clockwork on 5/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Recognizer.h"
#import "AppDelegate.h"

#include <vector>

using namespace std;
using namespace cv;

@implementation Recognizer

- (id) initWithDataSet:(vector<cv::Mat>)dataset {
    if (self = [super init]) {
        
        dataSetDescriptors = dataset;
        imageId = -1;
        appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        
        utils = [[Utils alloc] init];
    }
    return self;
}

- (Mat) getTestMat {
    return dataSetDescriptors[0];
}

- (int) recognize:(UIImage *)image {

    capturedDescriptors = [self getDescriptors:image];
    imageId = [self match:capturedDescriptors];
    
    return imageId;
}

- (Mat) getDescriptors:(UIImage *)image {   
    // Clear previous results.
    keypoints.clear();
    
    Mat grayImage(appDelegate.imageDimensions, appDelegate.imageDimensions, CV_8UC1);
    Mat capture = [utils MatFromUIImage:image];
    
    cv::resize(capture, grayImage, grayImage.size());
    
    featureDetector.detect(grayImage, keypoints);
    
    featureExtractor.compute(grayImage, keypoints, capturedDescriptors);

    grayImage.release();
    capture.release();
    
    return capturedDescriptors;
}

- (int) match:(cv::Mat)capturedMat {
    BFMatcher matcher(NORM_HAMMING);   
    
    imageId = -1;
    int bestMatch = 0;
    int goodMatches;
    
    // Use the matcher.
    for(int i=0; i < dataSetDescriptors.size(); i++) {
        // Clear results & set distances.
        matches.clear();
        goodMatches = 0;
        
        // Match.
        matcher.match(capturedMat, dataSetDescriptors[i], matches);        
        
        // Save good matches (low distance) in list.
        for(int k = 0; k < capturedMat.rows; k++ ) {
            if( matches[k].distance < appDelegate.maxDistance ) {
                goodMatches++;   
            }
        }
        
        if(goodMatches > bestMatch) {
            bestMatch = goodMatches;
            imageId = i;
        }
        
        if(goodMatches > appDelegate.matchesNeeded) {             
            return imageId;           
        }
        
    }
    printf("[OpenCV] Image ID : %d (%d matches) \n", imageId, bestMatch);
    
    return -1;
}

@end