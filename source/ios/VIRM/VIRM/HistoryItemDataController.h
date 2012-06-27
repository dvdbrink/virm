//
//  HistoryItemDataController.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// The HistoryItemDataController is responsible for managing the list of HistoryItems.
//

#import <UIKit/UIKit.h>

@class HistoryItem;

@interface HistoryItemDataController : NSObject

@property (nonatomic, retain) NSMutableArray *historyList;

-(unsigned)countOfList;
-(HistoryItem *)objectInListAtIndex:(unsigned)theIndex;
-(void)addHistoryItem:(NSString *)name painter:(NSString *)painter image:(UIImage *)image;
-(void)addHistoryItem:(HistoryItem *) historyItem;
-(HistoryItem *)getLastAddedHistoryItem;

@end
