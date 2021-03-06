//
//  Camera.m
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
#import "Camera.h"

@implementation Camera

@synthesize captureSession = _captureSession, viewController = _viewController, output = _output, isRunning = _isRunning;

- (id)initWithViewController:(UIViewController *)viewController {
    if(self = [super init]) {
        self.viewController = viewController;
        _isRunning = NO;
    }
    return self;
}

- (void)start {
    if(_isRunning == NO) {   
        _isRunning = YES;
        [self.captureSession startRunning];
    }
}

- (void)stop {
    if(_isRunning == YES) {   
        _isRunning = NO;
        [self.captureSession stopRunning];
    }
}

- (AVCaptureVideoDataOutput *)getOutput {
    return self.output;
}

- (AVCaptureSession *) getCaptureSession {
    return self.captureSession;
}

- (void)setup 
{
    NSError *error = nil;
    
    // Create the session
    self.captureSession = [[AVCaptureSession alloc] init];
    
    // Configure the session to produce lower resolution video frames, if your 
    // processing algorithm can cope. We'll specify medium quality for the
    // chosen device.
    self.captureSession.sessionPreset = AVCaptureSessionPresetMedium;
    
    // Find a suitable AVCaptureDevice
    AVCaptureDevice *device = [AVCaptureDevice
                               defaultDeviceWithMediaType:AVMediaTypeVideo];
    
    // Create a device input with the device and add it to the session.
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device 
                                                                        error:&error];
    if (!input) {
        // Handling the error appropriately.
    }
    [self.captureSession addInput:input];
    
    // Create a VideoDataOutput and add it to the session
    _output = [[[AVCaptureVideoDataOutput alloc] init] autorelease];
    [self.captureSession addOutput:_output];
    
    // Specify the pixel format
    _output.videoSettings = 
    [NSDictionary dictionaryWithObject:
     [NSNumber numberWithInt:kCVPixelFormatType_32BGRA] 
                                forKey:(id)kCVPixelBufferPixelFormatTypeKey];
    
    
    AVCaptureConnection *conn = [_output connectionWithMediaType:AVMediaTypeVideo];
    
    if (conn.supportsVideoMinFrameDuration)
        conn.videoMinFrameDuration = CMTimeMake(1,15);
    if (conn.supportsVideoMaxFrameDuration)
        conn.videoMaxFrameDuration = CMTimeMake(1,15);
}

@end
