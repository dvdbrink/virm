//
//  HistoryItemDataController.m
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// The HistoryItemDataController is responsible for managing the list of HistoryItems.
//

#import "HistoryItemDataController.h"
#import "HistoryItem.h"

@interface HistoryItemDataController ()
-(void)initializeDefaultDataList;
@end

@implementation HistoryItemDataController

@synthesize historyList = _historyList;

-(id)init {
    if(self = [super init]) {
        [self initializeDefaultDataList];
        return self;
    }
    return nil;
}

-(void)initializeDefaultDataList {   
    NSMutableArray *list = [[NSMutableArray alloc] init];
    self.historyList = list;
    
//    UIImage *img = [UIImage imageNamed:@"MonaLisa.jpg"];
//    [self addHistoryItem:@"Test" painter:@"Test" image:img];
}

-(void)setHistoryList:(NSMutableArray *)historyList:(NSMutableArray *)newList{
    if(_historyList != newList) {
        _historyList = [newList mutableCopy];
    }
}

-(unsigned)countOfList {
    return [self.historyList count];
}

-(HistoryItem *)objectInListAtIndex:(unsigned int)theIndex {
    return [self.historyList objectAtIndex:theIndex];
}

-(HistoryItem *)getLastAddedHistoryItem {
    return [self objectInListAtIndex:[self countOfList]-1];
}

-(void)addHistoryItem:(NSString *)name painter:(NSString *)painter image:(UIImage *)image {
    HistoryItem *historyItem;
    NSDate *today = [[NSDate alloc] init];
    historyItem = [[HistoryItem alloc] initWithName:name painter:painter image:image date:today];
    [self.historyList addObject:historyItem];
}

-(void)addHistoryItem:(HistoryItem *) historyItem {
    historyItem = [[HistoryItem alloc] initWithName:historyItem.name painter:historyItem.painter image:historyItem.image date:historyItem.date];
    [self.historyList addObject:historyItem];
}

@end
