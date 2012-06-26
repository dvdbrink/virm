//
//  SettingsViewController.h
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// A Viewcontroller containing some settings to tweak the recognition.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface SettingsViewController : UIViewController {
    AppDelegate *appDelegate;
}
@property (retain, nonatomic) IBOutlet UILabel *maxDistanceLabel;
@property (retain, nonatomic) IBOutlet UILabel *matchesNeededLabel;
@property (retain, nonatomic) IBOutlet UILabel *imageDimensionsLabel;

@property (retain, nonatomic) IBOutlet UISlider *setMaxDistance;
@property (retain, nonatomic) IBOutlet UISlider *setMatchesNeeded;
@property (retain, nonatomic) IBOutlet UISlider *setImageDimensions;
@property (retain, nonatomic) IBOutlet UISwitch *switchRemote;

- (IBAction)applyChanges:(id)sender;
- (IBAction)resetToDefaults:(id)sender;

- (void) setDefaultValues;



@end
