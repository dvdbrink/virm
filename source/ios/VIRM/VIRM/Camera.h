//
//  Camera.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This class represents the Camera screen.
// It is initiated and used from the OpenCVViewController.
// This class contains the initial camerasetup and start/stop methods.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface Camera : NSObject

- (id)initWithViewController: (UIViewController *) viewController;
- (void)stop;
- (void)start;
- (void)setup;
- (AVCaptureVideoDataOutput *) getOutput;
- (AVCaptureSession *) getCaptureSession;

@property (nonatomic, retain) AVCaptureSession *captureSession;
@property (nonatomic, retain) UIViewController *viewController;
@property (nonatomic, retain) AVCaptureVideoDataOutput *output;
@property (nonatomic, assign) BOOL isRunning;

@end
