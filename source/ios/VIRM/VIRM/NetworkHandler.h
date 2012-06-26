//
//  NetworkHandler.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This class is responsible for all network traffic.
// It's a delegate of NSStream. 
// The handleEvent method is called automatically.
//

#import <Foundation/Foundation.h>

@class OpenCVViewController;

using namespace cv;

@interface NetworkHandler : NSObject <NSStreamDelegate> {
    NSInputStream *inputStream;
    NSOutputStream *outputStream;
    CFReadStreamRef readStream;
    CFWriteStreamRef writeStream;
    
    OpenCVViewController *viewController;
}

- (id) initWithOpenCvViewController:(OpenCVViewController *)OpenCvViewController;

- (NSOutputStream *) getOutputStream;
- (NSInputStream *) getInputStream;

- (void) connect: (NSString *) ip: (int) port;
- (void) handlePacket: (NSMutableData *) data;
- (void) handleMatch;
- (void) sendMat: (Mat) mat;
- (void) sendPing;
- (void) sendClose;

@end
