//
//  HistoryViewController.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This ViewController shows an interactive list of all the HistoryItems.
//

#import <UIKit/UIKit.h>

@class HistoryItemViewController;
@class HistoryItemDataController;

@interface HistoryViewController : UITableViewController

@property (nonatomic, retain) HistoryItemDataController *dataController;
@property (strong, nonatomic) HistoryItemViewController *historyItemViewController;

@end
