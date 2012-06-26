//
//  HistoryItem.m
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// A historyItem contains all the information a user needs about a painting.
//

#import "HistoryItem.h"

@implementation HistoryItem

@synthesize image = _image, name = _name, painter = _painter, date = _date;

-(id)initWithName:(NSString *)name painter: (NSString *)painter image:(UIImage *)image date:(NSDate *) date{
    self = [super init];
    if (self) {
        _name = name;
        _painter = painter;
        _image = image;
        _date = date;
        return self;
    }
    return nil;
}


@end