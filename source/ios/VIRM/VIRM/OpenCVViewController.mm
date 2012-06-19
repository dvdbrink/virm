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
    enableMatching = NO;
    
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
	[self.navigationController.view addSubview:HUD];     
    
    HUD.labelText = @"Loading images..";
    [HUD showWhileExecuting:@selector(setupApplication) onTarget:self withObject:nil animated:YES];
}


- (void) setupApplication {   
    
    utils = [[Utils alloc] init];
    [utils initializeImageList];
    
    fileNames = [utils getFileNames];
    recognizer = [[Recognizer alloc] initWithDataSet:[utils getDescriptorsFromMatFiles]];
//    recognizer = [[Recognizer alloc] initWithDataSet:[utils getDescriptorsFromImageFiles:YES]];
    
    camera = [[Camera alloc] init];
    [camera setup];
    [self setCameraDelegate];
    [self addCameraView];
    [camera start];
    
    //    networkHandler = [[NetworkHandler alloc] init];
    //    [self setupNetwork];  
}

- (void) viewDidAppear:(BOOL)animated {
    enableMatching = YES;    
}

- (void) viewWillAppear:(BOOL)animated
{
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    [super viewWillAppear:animated];
}

- (void) viewWillDisappear:(BOOL)animated
{
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
    
    [networkHandler connect:@"172.19.2.30" :1337];
//    [networkHandler sendPing];
    [networkHandler sendMat:[recognizer getTestMat]];
}

// Delegate routine that is called when a sample buffer was written
- (void)captureOutput:(AVCaptureOutput *)captureOutput 
didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer 
       fromConnection:(AVCaptureConnection *)connection {
    
    if([camera isRunning] == YES) {
        NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];  
        
        UIImage *captureUI = [utils imageFromSampleBuffer:sampleBuffer];
        
        int match = [recognizer recognize:captureUI];
    
        natural_t freemem = [utils get_free_memory];
        printf("[System] Free memory: %u.\n", freemem);
    
        if(match > -1 && enableMatching==YES) {
            enableMatching = NO;
            [self processMatch:match];
        
        }
        [pool drain];
    }
    
}

- (void) processMatch: (int) imageId {
    printf("[OpenCV] Image %d recognized!\n", imageId);

    NSString* fileName = fileNames[imageId];
       
    UIImage *img = [UIImage imageNamed:fileName];
    
    [appDelegate.historyItemDataController addHistoryItem:fileName painter:fileName image:img];  
    
    [self performSelectorOnMainThread:@selector(switchToPaintingView) withObject:nil waitUntilDone:NO];    
    
//    [camera stop];      	
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
