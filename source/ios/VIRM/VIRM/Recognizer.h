//
//  Recognizer.h
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

#import <AVFoundation/AVFoundation.h>
#import "AppDelegate.h"
#import "Utils.h"

using namespace std;
using namespace cv;

@interface Recognizer : NSObject {
    vector<Mat> dataSetDescriptors;
//    vector<KeyPoint> keypoints;
//    vector<DMatch> matches;
        
    
    OrbFeatureDetector featureDetector;
    OrbDescriptorExtractor featureExtractor;   
    
    AppDelegate *appDelegate;
    int imageId;
    
    Utils *utils;
}

- (id) initWithDataSet: (vector<Mat>) dataset;
- (Mat) getTestMat;
- (int) recognize: (UIImage *) image;
- (Mat) getDescriptors: (UIImage *) image;
- (int) match: (Mat) capturedMat;

@end
