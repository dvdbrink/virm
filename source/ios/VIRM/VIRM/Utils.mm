//
//  Utils.m
//  VIRM
//
//  Created by Clockwork Clockwork on 5/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Utils.h"
#import <mach/mach.h>
#import <mach/mach_host.h>

using namespace std;
using namespace cv;

@implementation Utils

- (id) init {
    if(self = [super init]) {
        appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    }
    return self;
}

- (vector<NSString*>) getFileNames {  
    return fileNames;
}

-(natural_t) get_free_memory {
    mach_port_t host_port;
    mach_msg_type_number_t host_size;
    vm_size_t pagesize;
    host_port = mach_host_self();
    host_size = sizeof(vm_statistics_data_t) / sizeof(integer_t);
    host_page_size(host_port, &pagesize);
    vm_statistics_data_t vm_stat;
    if (host_statistics(host_port, HOST_VM_INFO, (host_info_t)&vm_stat, &host_size) != KERN_SUCCESS) {
        NSLog(@"Failed to fetch vm statistics");
        return 0;
    }
    /* Stats in bytes */
    natural_t mem_free = vm_stat.free_count * pagesize;
    return mem_free;
}

- (vector<Mat>)getDescriptorsFromImageFiles:(BOOL)saveToMat {
    vector<Mat> dataSetDescriptors;
    
    vector<KeyPoint> keypoints;
    OrbFeatureDetector featureDetector;
    OrbDescriptorExtractor featureExtractor;    
    Mat descriptors;

    for(NSString *filename in imageList) {
    
        keypoints.clear();
    
        UIImage* imageUI = [UIImage imageNamed:filename];
        
        Mat grayImage(appDelegate.imageDimensions, appDelegate.imageDimensions, CV_8UC1);
        Mat capture = [self MatFromUIImage:imageUI];
        
        cv::resize(capture, grayImage, grayImage.size());        
    
        featureDetector.detect(grayImage, keypoints);
        featureExtractor.compute(grayImage, keypoints, descriptors); 
    
        // Save the image as .mat file
        if(saveToMat) {
            [self saveDescriptorsToFile:descriptors fileName:filename];
        }
    
        dataSetDescriptors.push_back(descriptors);    
    }
    return dataSetDescriptors;
}

- (vector<Mat>)getDescriptorsFromMatFiles {
    
    vector<Mat> dataSetDescriptors;
    
    for(NSString *filename in imageList) {
    
        filename = [filename substringToIndex:[filename length] - 4];
    
        NSString *filePath = [[NSBundle mainBundle] pathForResource:filename ofType:@"mat"];
    
        NSData *content = [NSData dataWithContentsOfFile:filePath];
    
        uint32_t rows; 
        [content getBytes:&rows range:NSMakeRange(0, 4)]; 
    
        uint32_t cols;    
        [content getBytes:&cols range:NSMakeRange(4, 4)];         
    
        Mat descriptors(rows, cols, CV_8U);
    
        int startPos = 8;
        for(int i=0; i < rows; i++) {
            for(int j=0; j < cols; j++) {
                unsigned char value;
                [content getBytes:&value range:NSMakeRange(startPos, 1)];                
                descriptors.row(i).col(j) = value;             
                startPos +=  1;
            }
        }
        dataSetDescriptors.push_back(descriptors);
    }
    
    return dataSetDescriptors;
}

- (void)saveDescriptorsToFile: (Mat)descriptors fileName:(NSString *)filename {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:filename];
    
    NSMutableData *data = [[NSMutableData alloc] init];
    [data appendBytes:&descriptors.rows length:sizeof(descriptors.rows)];
    [data appendBytes:&descriptors.cols length:sizeof(descriptors.cols)];   
    
    for(int i=0; i < descriptors.rows; i++) {
        for(int j=0; j < descriptors.cols; j++) {
            unsigned char value =  descriptors.at<unsigned char>(i, j);            
            [data appendBytes:&value length:sizeof(value)];            
        }
    }
    
    [data writeToFile:filePath atomically:YES];
    [data release];
    printf("[OpenCV] Saved image: %s.\n", [filename UTF8String]);
}

- (Mat)MatFromUIImage:(UIImage *)image
{
    CGColorSpaceRef colorSpace = CGImageGetColorSpace(image.CGImage);
    CGFloat cols = image.size.width;
    CGFloat rows = image.size.height;
    
    Mat cvMat(rows, cols, CV_8UC4); // 8 bits per component, 4 channels
    
    CGContextRef contextRef = CGBitmapContextCreate(cvMat.data,                 // Pointer to backing data
                                                    cols,                      // Width of bitmap
                                                    rows,                     // Height of bitmap
                                                    8,                          // Bits per component
                                                    cvMat.step[0],              // Bytes per row
                                                    colorSpace,                 // Colorspace
                                                    kCGImageAlphaNoneSkipLast |
                                                    kCGBitmapByteOrderDefault); // Bitmap info flags
    
    CGContextDrawImage(contextRef, CGRectMake(0, 0, cols, rows), image.CGImage);
    CGContextRelease(contextRef);
    
    return cvMat;
}


// Create a UIImage from sample buffer data
- (UIImage *) imageFromSampleBuffer:(CMSampleBufferRef) sampleBuffer 
{
    // Get a CMSampleBuffer's Core Video image buffer for the media data
    CVImageBufferRef imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer); 
    // Lock the base address of the pixel buffer
    CVPixelBufferLockBaseAddress(imageBuffer, 0); 
    
    // Get the number of bytes per row for the pixel buffer
    void *baseAddress = CVPixelBufferGetBaseAddress(imageBuffer); 
    
    // Get the number of bytes per row for the pixel buffer
    size_t bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer); 
    // Get the pixel buffer width and height
    size_t bWidth = CVPixelBufferGetWidth(imageBuffer); 
    size_t bHeight = CVPixelBufferGetHeight(imageBuffer); 
    
    // Create a device-dependent RGB color space
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB(); 
    
    // Create a bitmap graphics context with the sample buffer data
    CGContextRef context = CGBitmapContextCreate(baseAddress, bWidth, bHeight, 8, 
                                                 bytesPerRow, colorSpace, kCGBitmapByteOrder32Little | kCGImageAlphaPremultipliedFirst); 
    // Create a Quartz image from the pixel data in the bitmap graphics context
    CGImageRef quartzImage = CGBitmapContextCreateImage(context); 
    // Unlock the pixel buffer
    CVPixelBufferUnlockBaseAddress(imageBuffer,0);
    
    // Free up the context and color space
    CGContextRelease(context); 
    CGColorSpaceRelease(colorSpace);
    
    // Create an image object from the Quartz image
    UIImage *image = [UIImage imageWithCGImage:quartzImage];
    
    // Release the Quartz image
    CGImageRelease(quartzImage);
    
    return (image);
}

- (void) initializeImageList {
    imageList = [[NSMutableArray alloc] init];
    
//    [imageList addObject:@"DeNachtwacht.jpg"];
//    [imageList addObject:@"MonaLisa.jpg"];    
//    [imageList addObject:@"VanGogh.jpg"];    
    
//    [imageList addObject:@"IMG_20120328_133650.jpg"];
//    [imageList addObject:@"IMG_20120328_133717.jpg"];
//    [imageList addObject:@"IMG_20120328_133800.jpg"];
//    [imageList addObject:@"IMG_20120328_133813.jpg"];
//    [imageList addObject:@"IMG_20120328_133844.jpg"];
//    [imageList addObject:@"IMG_20120328_133855.jpg"];
//    [imageList addObject:@"IMG_20120328_133903.jpg"];
//    [imageList addObject:@"IMG_20120328_134104.jpg"];
//    [imageList addObject:@"IMG_20120328_134112.jpg"];
//    [imageList addObject:@"IMG_20120328_134125.jpg"];
//    [imageList addObject:@"IMG_20120328_134135.jpg"];
//    [imageList addObject:@"IMG_20120328_134143.jpg"];
//    [imageList addObject:@"IMG_20120328_134152.jpg"];
//    [imageList addObject:@"IMG_20120328_134208.jpg"];
//    [imageList addObject:@"IMG_20120328_134301.jpg"];
//    [imageList addObject:@"IMG_20120328_134320.jpg"];
//    [imageList addObject:@"IMG_20120328_134432.jpg"];
//    [imageList addObject:@"IMG_20120328_134446.jpg"];
//    [imageList addObject:@"IMG_20120328_134503.jpg"];
//    [imageList addObject:@"IMG_20120328_134513.jpg"];
//    [imageList addObject:@"IMG_20120328_134521.jpg"];
//    [imageList addObject:@"IMG_20120328_134529.jpg"];
//    [imageList addObject:@"IMG_20120328_134544.jpg"];
//    [imageList addObject:@"IMG_20120328_134551.jpg"];
//    [imageList addObject:@"IMG_20120328_134601.jpg"];
//    [imageList addObject:@"IMG_20120328_134610.jpg"];
//    [imageList addObject:@"IMG_20120328_134621.jpg"];
//    [imageList addObject:@"IMG_20120328_134629.jpg"];
//    [imageList addObject:@"IMG_20120328_134705.jpg"];
//    [imageList addObject:@"IMG_20120328_134719.jpg"];
//    [imageList addObject:@"IMG_20120328_134727.jpg"];
//    [imageList addObject:@"IMG_20120328_134750.jpg"];
//    [imageList addObject:@"IMG_20120328_134801.jpg"];
//    [imageList addObject:@"IMG_20120328_134811.jpg"];
//    [imageList addObject:@"IMG_20120328_134823.jpg"];
//    [imageList addObject:@"IMG_20120328_134832.jpg"];
//    [imageList addObject:@"IMG_20120328_134840.jpg"];
//    [imageList addObject:@"IMG_20120328_134849.jpg"];
//    [imageList addObject:@"IMG_20120328_134934.jpg"];
//    [imageList addObject:@"IMG_20120328_134948.jpg"];
//    [imageList addObject:@"IMG_20120328_134955.jpg"];
//    [imageList addObject:@"IMG_20120328_135004.jpg"];
//    [imageList addObject:@"IMG_20120328_135012.jpg"];
//    [imageList addObject:@"IMG_20120328_135021.jpg"];
//    [imageList addObject:@"IMG_20120328_135036.jpg"];
//    [imageList addObject:@"IMG_20120328_135059.jpg"];
//    [imageList addObject:@"IMG_20120328_135112.jpg"];
//    [imageList addObject:@"IMG_20120328_135135.jpg"];
//    [imageList addObject:@"IMG_20120328_135226.jpg"];
//    [imageList addObject:@"IMG_20120328_135601.jpg"];
//    [imageList addObject:@"IMG_20120328_135613.jpg"];
//    [imageList addObject:@"IMG_20120328_135628.jpg"];
//    [imageList addObject:@"IMG_20120328_135646.jpg"];
//    [imageList addObject:@"IMG_20120328_135941.jpg"];
    
//    // Museum #2
    [imageList addObject:@"IMG_20120502_134328.jpg"]; 
    [imageList addObject:@"IMG_20120502_134336.jpg"]; 
    [imageList addObject:@"IMG_20120502_134349.jpg"]; 
    [imageList addObject:@"IMG_20120502_134358.jpg"]; 
    [imageList addObject:@"IMG_20120502_134407.jpg"]; 
    [imageList addObject:@"IMG_20120502_134418.jpg"]; 
    [imageList addObject:@"IMG_20120502_134433.jpg"]; 
    [imageList addObject:@"IMG_20120502_134440.jpg"]; 
    [imageList addObject:@"IMG_20120502_134447.jpg"]; 
    [imageList addObject:@"IMG_20120502_134455.jpg"]; 
    [imageList addObject:@"IMG_20120502_134526.jpg"]; 
    [imageList addObject:@"IMG_20120502_134534.jpg"]; 
    [imageList addObject:@"IMG_20120502_134541.jpg"]; 
    [imageList addObject:@"IMG_20120502_134547.jpg"]; 
    [imageList addObject:@"IMG_20120502_134557.jpg"]; 
    [imageList addObject:@"IMG_20120502_134605.jpg"]; 
    [imageList addObject:@"IMG_20120502_134612.jpg"]; 
    [imageList addObject:@"IMG_20120502_134619.jpg"]; 
    [imageList addObject:@"IMG_20120502_134626.jpg"]; 
    [imageList addObject:@"IMG_20120502_134647.jpg"]; 
    [imageList addObject:@"IMG_20120502_134653.jpg"]; 
    [imageList addObject:@"IMG_20120502_134700.jpg"]; 
    [imageList addObject:@"IMG_20120502_134707.jpg"]; 
    [imageList addObject:@"IMG_20120502_134713.jpg"]; 
    [imageList addObject:@"IMG_20120502_134720.jpg"]; 
    [imageList addObject:@"IMG_20120502_134755.jpg"]; 
    [imageList addObject:@"IMG_20120502_134803.jpg"]; 
    [imageList addObject:@"IMG_20120502_134812.jpg"]; 
    [imageList addObject:@"IMG_20120502_134822.jpg"]; 
    [imageList addObject:@"IMG_20120502_134834.jpg"]; 
    [imageList addObject:@"IMG_20120502_134842.jpg"]; 
    [imageList addObject:@"IMG_20120502_134850.jpg"]; 
    [imageList addObject:@"IMG_20120502_134859.jpg"]; 
    [imageList addObject:@"IMG_20120502_134907.jpg"]; 
    [imageList addObject:@"IMG_20120502_134914.jpg"]; 
    [imageList addObject:@"IMG_20120502_134939.jpg"]; 
    [imageList addObject:@"IMG_20120502_134948.jpg"]; 
    [imageList addObject:@"IMG_20120502_134957.jpg"]; 
    [imageList addObject:@"IMG_20120502_135009.jpg"];
    [imageList addObject:@"IMG_20120502_135018.jpg"]; 
    [imageList addObject:@"IMG_20120502_135027.jpg"]; 
    [imageList addObject:@"IMG_20120502_135050.jpg"]; 
    [imageList addObject:@"IMG_20120502_135057.jpg"];    
    [imageList addObject:@"IMG_20120502_135126.jpg"]; 
    [imageList addObject:@"IMG_20120502_135140.jpg"]; 
    [imageList addObject:@"IMG_20120502_135152.jpg"]; 
    [imageList addObject:@"IMG_20120502_135200.jpg"]; 
    [imageList addObject:@"IMG_20120502_135207.jpg"]; 
    [imageList addObject:@"IMG_20120502_135215.jpg"]; 
    [imageList addObject:@"IMG_20120502_135224.jpg"];    
    
    for(NSString *filename in imageList) {
        fileNames.push_back(filename);        
    }    
}

@end
