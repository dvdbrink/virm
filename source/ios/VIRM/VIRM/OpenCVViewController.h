//
//  OpenCVViewController.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This is the main ViewController, which sets up recognition, the camera and the networkhandler.
// It handles the cameraoutput and any possible matches.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import "AppDelegate.h"
#import "MBProgressHUD.h"
#import "Recognizer.h"
#import "Utils.h"
#import "Camera.h"
#import "NetworkHandler.h"

using namespace std;
using namespace cv;

@interface OpenCVViewController : UIViewController <AVCaptureVideoDataOutputSampleBufferDelegate> {    
    
    AppDelegate *appDelegate;
    MBProgressHUD *HUD;   

    Recognizer *recognizer;
    Utils *utils;
    Camera *camera;    
    NetworkHandler *networkHandler;
    
    vector<NSString*> fileNames;
    vector<Mat> dataSetDescriptors;    
}

@property (nonatomic, retain) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, assign) BOOL enableMatching;
@property (nonatomic, assign) BOOL connected;

- (void) processMatch: (NSString *) matchName;

@end
