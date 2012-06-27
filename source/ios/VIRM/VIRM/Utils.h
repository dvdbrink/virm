//
//  Utils.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This class contains several 'util' methods that are used throughout the app.
// Most of the methods deal with Mat/Image conversion.
// A (hardcoded) list of images that is used in this prototype is also made here.
//

#import <AVFoundation/AVFoundation.h>
#import "AppDelegate.h"

using namespace std;
using namespace cv;

@interface Utils : NSObject {
    NSMutableArray *imageList;   
    AppDelegate *appDelegate;  
    vector<NSString*> fileNames;
}

- (void) initializeImageList;
- (natural_t) get_free_memory;
- (vector<Mat>) getDescriptorsFromMatFiles;
- (vector<Mat>) getDescriptorsFromImageFiles: (BOOL) saveToMat;
- (vector<NSString*>) getFileNames;
- (Mat)MatFromUIImage:(UIImage *)image;
- (UIImage *) imageFromSampleBuffer:(CMSampleBufferRef) sampleBuffer;

@end
