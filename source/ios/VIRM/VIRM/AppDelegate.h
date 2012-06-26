//
//  AppDelegate.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// The appDelegate contains a few variables used throughout the application.
//

#import <UIKit/UIKit.h>
#import "HistoryItemDataController.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (nonatomic, assign) int maxDistance;
@property (nonatomic, assign) int matchesNeeded;
@property (nonatomic, assign) int imageDimensions;

@property (nonatomic, assign) BOOL remote;

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) HistoryItemDataController *historyItemDataController;

- (void) setDefaultValues;

@end
