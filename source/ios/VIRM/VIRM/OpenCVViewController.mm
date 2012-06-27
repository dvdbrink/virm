//
//  OpenCVViewController.m
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This is the main ViewController, which sets up recognition, the camera and the networkhandler.
// It handles the cameraoutput and any possible matches.
//

#import "OpenCVViewController.h"
#import "AppDelegate.h"
#import "HistoryItemViewController.h"
#import "HistoryItem.h"
#import "Recognizer.h"
#import "Camera.h"

#include <opencv2/core/core.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <vector>

using namespace std;
using namespace cv;

@implementation OpenCVViewController

@synthesize previewLayer = _previewLayer;
@synthesize enableMatching = _enableMatching;
@synthesize connected = _connected;

#pragma mark -
#pragma mark Initialization
- (id)init {
	self = [super init];
	if (self) {
		self.previewLayer = nil;
	}
	return self;
}

// When the view is first loaded, the application will be set up while the HUD shows a progress icon.
- (void)viewDidLoad {
    
    appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    
    _connected = NO;
    
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
	[self.navigationController.view addSubview:HUD];     
    
    HUD.labelText = @"Initializing..";
    [HUD showWhileExecuting:@selector(setupApplication) onTarget:self withObject:nil animated:YES];
}

// A seperate setup method to facilitate the progress icon.
- (void) setupApplication {   
    
    utils = [[Utils alloc] init];
    [utils initializeImageList];
    
    fileNames = [utils getFileNames];
    recognizer = [[Recognizer alloc] initWithDataSet:[utils getDescriptorsFromMatFiles]];
    
    camera = [[Camera alloc] init];
    [camera setup];
    [self setCameraDelegate];
    [self addCameraView];
    [camera start];
}

// Loaded everytime the view (re)appears. This method checks if remote was enabled through the settings.
// If it has been enabled, it will set up the networkhandler.
- (void) viewDidAppear:(BOOL)animated {
    _enableMatching = YES;
    
    if(appDelegate.remote == YES && _connected == NO) {
        networkHandler = [[NetworkHandler alloc] initWithOpenCvViewController:self];
        [self setupNetwork];  
    }
}

- (void) viewWillAppear:(BOOL)animated
{
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    [super viewWillAppear:animated];
}

// When the view disappears, matching has to be disabled.
- (void) viewWillDisappear:(BOOL)animated
{
    _enableMatching = NO;
    [self.navigationController setNavigationBarHidden:NO animated:animated];
    [super viewWillDisappear:animated];
}

// This method adds the cameraview to the ViewController.
- (void) addCameraView {
    [self setPreviewLayer:[[AVCaptureVideoPreviewLayer alloc] initWithSession:[camera getCaptureSession]]];
    self.previewLayer.orientation = UIInterfaceOrientationPortrait;
    [[self previewLayer] setVideoGravity:AVLayerVideoGravityResizeAspectFill];
    CGRect layerRect = [[self view] bounds];
	[self.previewLayer setBounds:layerRect];
    [self.previewLayer setPosition:CGPointMake(CGRectGetMidX(layerRect), CGRectGetMidY(layerRect))];    
    [self.view.layer addSublayer: self.previewLayer]; 
}

// This method sets 'self' as the CameraDelegate.
- (void) setCameraDelegate {
    // Configure output.
    AVCaptureVideoDataOutput *output = [camera getOutput];    
    
    dispatch_queue_t queue = dispatch_queue_create("myQueue", NULL);
   [output setSampleBufferDelegate:self queue:queue];    
    dispatch_release(queue); 
}

// Method to connect to the server.
- (void)setupNetwork {
    
    [networkHandler connect:appDelegate.serverIp :appDelegate.serverPort];
}

// Delegate routine that is called when a sample buffer was written
- (void)captureOutput:(AVCaptureOutput *)captureOutput 
didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer 
       fromConnection:(AVCaptureConnection *)connection {
    
    if(_enableMatching == YES) {
        NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
        UIImage *captureUI = [utils imageFromSampleBuffer:sampleBuffer];        
        
        // Remote handling. Retrieves descriptors from image and sends it through network.
        if(appDelegate.remote == YES && _connected == YES) {
            Mat descriptors = [recognizer getDescriptors:captureUI];
            [networkHandler sendMat:descriptors];
            _enableMatching = NO;            
        }
        
        // Local handling. Uses recognizer to recognize. In case of a match, it calls processMatch.
        if(appDelegate.remote == NO) {
            int match = [recognizer recognize:captureUI];
            if(match > -1) {  
                _enableMatching = NO;                 
                
                NSString* matchName = fileNames[match];
                [self processMatch:matchName];
        
            }
        }
        [pool drain];        
    }   
}

// Method to process a match. The matchName is currently a NSString containing the image filename.
// A thumbnail is created using this filename.
// Then a HistoryItem is created using the matchName as paintingname and paintername.
// Lastly, the method calls SwitchToPaintingView to display the information.
- (void) processMatch: (NSString *) matchName {
       
    // Remote thumbnail - needs fixing
//    UIImage *img = [UIImage imageNamed:@"MonaLisa.jpg"];
    
    // Local thumbnail - needs fixing
    UIImage *img = [UIImage imageNamed:matchName];
    
    [appDelegate.historyItemDataController addHistoryItem:matchName painter:matchName image:img];  
    
    [self performSelectorOnMainThread:@selector(switchToPaintingView) withObject:nil waitUntilDone:NO];    
}

// Method responsible for switching the view to the HistoryItemViewController.
-(void)switchToPaintingView{   
    
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];   
    
    HistoryItemViewController *paintingViewController =[storyboard instantiateViewControllerWithIdentifier:@"paintingViewController"];   
    
    paintingViewController.historyItem = [appDelegate.historyItemDataController getLastAddedHistoryItem];   
    
    [self.navigationController pushViewController:paintingViewController animated:YES];   
}

- (void)viewDidUnload {
	self.previewLayer = nil;
}

- (void)dealloc {
    [super dealloc];
}

@end
