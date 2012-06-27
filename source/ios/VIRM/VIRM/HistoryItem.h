//
//  HistoryItem.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// A historyItem contains all the information a user needs about a painting.
//

#import <UIKit/UIKit.h>

@interface HistoryItem : NSObject

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *painter;
@property (nonatomic, copy) UIImage *image;
@property (nonatomic, copy) NSDate *date;

-(id)initWithName:(NSString *)name painter:(NSString *)painter image:(UIImage *)image date:(NSDate *)date;

@end
