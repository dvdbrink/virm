//
//  AppDelegate.m
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// The appDelegate contains a few variables used throughout the application.
//

#import "AppDelegate.h"
#import "NetworkHandler.h"

@implementation AppDelegate

@synthesize window = _window;
@synthesize historyItemDataController = _historyItemDataController;
@synthesize remote = _remote;

@synthesize matchesNeeded = _matchesNeeded;
@synthesize maxDistance = _maxDistance;
@synthesize imageDimensions = _imageDimensions;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    _historyItemDataController = [[HistoryItemDataController alloc] init];
    
    _remote = NO;
    
    [self setDefaultValues];
    
    // Override point for customization after application launch.
    return YES;
}

- (void) setDefaultValues {
    self.maxDistance = 35;
    self.matchesNeeded = 12;
    self.imageDimensions = 150;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    [[[NetworkHandler alloc] init] sendClose];
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
