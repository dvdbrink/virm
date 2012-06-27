//
//  HistoryItemViewController.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// This ViewController is responsible for showing all the information about a HistoryItem.
// Currently this uses the paintingname, paintername and image thumbnail given in OpenCVViewController.
// The 'informationtext' is still hardcoded and must be supplied by the museum.
//

#import <UIKit/UIKit.h>

@class HistoryItem;

@interface HistoryItemViewController : UITableViewController <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) HistoryItem *historyItem;
@property (strong, nonatomic) IBOutlet UITextView *textView;

@end
