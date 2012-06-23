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

- (void)viewDidLoad {
    printf("[OpenCV] View loaded.\n");
    
    appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    
    _connected = NO;
    
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
	[self.navigationController.view addSubview:HUD];     
    
    HUD.labelText = @"Initializing..";
    [HUD showWhileExecuting:@selector(setupApplication) onTarget:self withObject:nil animated:YES];
}


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

- (void) viewDidAppear:(BOOL)animated {
    printf("[OpenCV] Matching enabled.\n");
    _enableMatching = YES;
    
    if(appDelegate.remote == YES && _connected == NO) {
        printf("[Network] Setting up network.\n");
        networkHandler = [[NetworkHandler alloc] initWithOpenCvViewController:self];
        [self setupNetwork];  
    }
}

- (void) viewWillAppear:(BOOL)animated
{
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    [super viewWillAppear:animated];
}

- (void) viewWillDisappear:(BOOL)animated
{
    _enableMatching = NO;
    [self.navigationController setNavigationBarHidden:NO animated:animated];
    [super viewWillDisappear:animated];
}

- (void) addCameraView {
    [self setPreviewLayer:[[AVCaptureVideoPreviewLayer alloc] initWithSession:[camera getCaptureSession]]];
    self.previewLayer.orientation = UIInterfaceOrientationPortrait;
    [[self previewLayer] setVideoGravity:AVLayerVideoGravityResizeAspectFill];
    CGRect layerRect = [[self view] bounds];
	[self.previewLayer setBounds:layerRect];
    [self.previewLayer setPosition:CGPointMake(CGRectGetMidX(layerRect), CGRectGetMidY(layerRect))];    
    
    [self.view.layer addSublayer: self.previewLayer]; 
}

- (void) setCameraDelegate {
    // Configure output.
    AVCaptureVideoDataOutput *output = [camera getOutput];    
    
    dispatch_queue_t queue = dispatch_queue_create("myQueue", NULL);
   [output setSampleBufferDelegate:self queue:queue];    
    dispatch_release(queue); 
}

- (void)setupNetwork {
    printf("[Network] Setting up connection.\n");
    
    [networkHandler connect:@"50.17.57.182" :1337];
}

// Delegate routine that is called when a sample buffer was written
- (void)captureOutput:(AVCaptureOutput *)captureOutput 
didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer 
       fromConnection:(AVCaptureConnection *)connection {
    
    if(_enableMatching == YES) {
        NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
        UIImage *captureUI = [utils imageFromSampleBuffer:sampleBuffer];        
        
        if(appDelegate.remote == YES && _connected == YES) {
            Mat descriptors = [recognizer getDescriptors:captureUI];
            [networkHandler sendMat:descriptors];
            
            printf("[OpenCV] Matching disabled.\n");    
            _enableMatching = NO;            
        }
        
        if(appDelegate.remote == NO) {
            int match = [recognizer recognize:captureUI];
            if(match > -1) {
                printf("[OpenCV] Matching disabled.\n");    
                _enableMatching = NO;                 
                
                NSString* matchName = fileNames[match];
                [self processMatch:matchName];
        
            }
        }
        [pool drain];        
    }
    
//    natural_t freemem = [utils get_free_memory];
//    printf("[System] Free memory: %u.\n", freemem);    
}

- (void) processMatch: (NSString *) matchName {
    printf("[OpenCV] Image %s recognized!\n", [matchName UTF8String]);
       
    UIImage *img = [UIImage imageNamed:@"MonaLisa.jpg"];
    
    [appDelegate.historyItemDataController addHistoryItem:matchName painter:matchName image:img];  
    
    [self performSelectorOnMainThread:@selector(switchToPaintingView) withObject:nil waitUntilDone:NO];    
}

-(void)switchToPaintingView{   
    
    printf("[OpenCV] Switching to paintingview.\n");    
    
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];   
    
    HistoryItemViewController *paintingViewController =[storyboard instantiateViewControllerWithIdentifier:@"paintingViewController"];   
    
    paintingViewController.historyItem = [appDelegate.historyItemDataController getLastAddedHistoryItem];   
    
    [self.navigationController pushViewController:paintingViewController animated:YES];   
}

- (void)didReceiveMemoryWarning {
    printf("[OpenCV] Memory warning!\n");
}

- (void)viewDidUnload {
	self.previewLayer = nil;
}

- (void)dealloc {
    [super dealloc];
}

@end
