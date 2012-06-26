//
//  NetworkHandler.m
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

#import "NetworkHandler.h"
#import "OpenCVViewController.h"

@implementation NetworkHandler

- (id) initWithOpenCvViewController:(OpenCVViewController *)OpenCvViewController {
    if (self = [super init]) {
        
        viewController = OpenCvViewController;
    }
    return self;
}

- (void) connect:(NSString *)ip :(int)port {
    
    CFStreamCreatePairWithSocketToHost(kCFAllocatorDefault,
                                       (CFStringRef) ip,
                                       port,
                                       &readStream,
                                       &writeStream);
    if (readStream && writeStream) {
        CFReadStreamSetProperty(readStream,
                                kCFStreamPropertyShouldCloseNativeSocket,
                                kCFBooleanTrue);
        CFWriteStreamSetProperty(writeStream,
                                 kCFStreamPropertyShouldCloseNativeSocket,
                                 kCFBooleanTrue);
        inputStream = (NSInputStream *)readStream;
        [inputStream retain];
        [inputStream setDelegate:self];
        [inputStream scheduleInRunLoop:[NSRunLoop mainRunLoop]
                           forMode:NSDefaultRunLoopMode];
        [inputStream open];
        
        outputStream = (NSOutputStream *)writeStream;
        [outputStream retain];
        [outputStream setDelegate:self];
        [outputStream scheduleInRunLoop:[NSRunLoop mainRunLoop]
                           forMode:NSDefaultRunLoopMode];
        [outputStream open];
    }    
}

- (void)stream:(NSStream *)stream handleEvent:(NSStreamEvent)eventCode {
    switch(eventCode) {
        case NSStreamEventHasBytesAvailable: {      
            if(stream == inputStream) {            
                NSMutableData *data = [[NSMutableData alloc] init];                
                uint8_t buffer[1];                
                
                int len = [inputStream read:buffer maxLength:1];              
                [data appendBytes:buffer length:len];                
                
                [self handlePacket: data];
            } 
            break;
        }
        case NSStreamEventNone: {
            break;
        }
        case NSStreamEventOpenCompleted: {
            viewController.connected = YES;
            break;
        }
        case NSStreamEventHasSpaceAvailable: {
            break;
        }
        case NSStreamEventErrorOccurred: {
            NSError *error = [stream streamError]; 
            printf("[Network] Error: %s.\n", [[error localizedDescription] UTF8String]);
            break;
        }
        case NSStreamEventEndEncountered: {
            break;            
        }
    }
}

- (void) handlePacket: (NSMutableData *) data {
    uint8_t received[1];
    [data getBytes:received length:1];
    
    switch(received[0]) {
        case 0x01 : {
            printf("[Network] PING received.\n");
            break;
        }
        case 0x02 : {
            printf("[Network] OK received.\n");
            break;
        }
        case 0x03 : {
            printf("[Network] FAIL received.\n");            
            break;
        }
        case 0x04 : {
            printf("[Network] CLOSE received.\n");            
            break;
        }
        case 0x06 : {
            printf("[Network] MATCH received.\n");
            [self handleMatch];
            break;
        }
        case 0x07 : {
            printf("[Network] NO_MATCH received.\n");   
            viewController.enableMatching = YES;            
            break;
        }            
    }
}

- (void) handleMatch {
    uint8_t buffer[4];                
    
    [inputStream read:buffer maxLength:4];
    
    int length = 0;
    for (int i = 0; i < 4; i++) {
        length |= (buffer[i] & 0xFF) << (i << 3);
    }
    
    uint8_t stringBuffer[length];
    [inputStream read:stringBuffer maxLength:length];
    
    NSString *imageId = [[NSString alloc] initWithBytes:stringBuffer length:length encoding:NSUTF8StringEncoding];
    
    [viewController processMatch:imageId];
}

- (void) sendPing {
    Byte buffer[1];
    buffer[0] = 0x01;                    
    NSMutableData *data = [NSMutableData dataWithCapacity:0];
    [data appendBytes:buffer length:1];
    
    [outputStream write:(const uint8_t *)[data bytes] maxLength:[data length]];   
}

- (void) sendClose {
    Byte buffer[1];
    buffer[0] = 0x04;                    
    NSMutableData *data = [NSMutableData dataWithCapacity:0];
    [data appendBytes:buffer length:1];
    
    [outputStream write:(const uint8_t *)[data bytes] maxLength:[data length]];   
}

- (void) sendMat: (Mat) mat {
    Byte buffer[1];
    buffer[0] = 0x05;                    
    NSMutableData *data = [NSMutableData dataWithCapacity:0];
    [data appendBytes:buffer length:1];
        
    [data appendBytes:&mat.rows length:sizeof(mat.rows)];
    [data appendBytes:&mat.cols length:sizeof(mat.cols)];
        
    for(int i=0; i < mat.rows; i++) {
        for(int j=0; j < mat.cols; j++) {
            unsigned char value =  mat.at<unsigned char>(i, j);            
            [data appendBytes:&value length:sizeof(value)]; 
        }
    }
        
    [outputStream write:(const uint8_t *)[data bytes] maxLength:[data length]];   
}

- (NSInputStream *) getInputStream {
    return inputStream;
}

- (NSOutputStream *) getOutputStream {
    return outputStream;
}

@end
