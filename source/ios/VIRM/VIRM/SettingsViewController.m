//
//  SettingsViewController.m
//  VIRM
//
//  Created by Steven Elzinga on 5/10/12.
//  Copyright (c) Clockwork. All rights reserved.
//
// ==============================================
// A Viewcontroller containing some settings to tweak the recognition.
//

#import "SettingsViewController.h"
#import "AppDelegate.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController
@synthesize maxDistanceLabel;
@synthesize matchesNeededLabel;
@synthesize imageDimensionsLabel;
@synthesize maxDistanceTextLabel;
@synthesize matchesNeededTextLabel;
@synthesize imageDimensionsTextLabel;
@synthesize addressTextLabel;
@synthesize setServerPort;
@synthesize setServerIp;
@synthesize setMaxDistance;
@synthesize setMatchesNeeded;
@synthesize setImageDimensions;
@synthesize switchRemote;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];    
    
    [self setDefaultValues];
    [self setRemoteSettings:NO];
}

- (IBAction)applyChanges:(id)sender {
    appDelegate.maxDistance = (int) setMaxDistance.value;
    maxDistanceLabel.text = [NSString stringWithFormat:@"%i", (int) setMaxDistance.value];
    
    appDelegate.matchesNeeded = (int) setMatchesNeeded.value;
    matchesNeededLabel.text = [NSString stringWithFormat:@"%i", (int) setMatchesNeeded.value];

    imageDimensionsLabel.text = [NSString stringWithFormat:@"%i * %i", (int) setImageDimensions.value, (int) setImageDimensions.value];    
    appDelegate.imageDimensions = (int) setImageDimensions.value;  
    
    appDelegate.serverIp = setServerIp.text;
    appDelegate.serverPort = [setServerPort.text intValue];
    appDelegate.remote = switchRemote.isOn;
    
    [self setRemoteSettings:switchRemote.isOn];
}

- (IBAction)resetToDefaults:(id)sender {
    [appDelegate setDefaultValues];
    [self setDefaultValues];
}

- (void) setDefaultValues {
    setMaxDistance.value = (float) appDelegate.maxDistance;
    setMatchesNeeded.value = (float) appDelegate.matchesNeeded;
    setImageDimensions.value = (float) appDelegate.imageDimensions;
    
    maxDistanceLabel.text = [NSString stringWithFormat:@"%i", appDelegate.maxDistance];
    matchesNeededLabel.text = [NSString stringWithFormat:@"%i", appDelegate.matchesNeeded];
    imageDimensionsLabel.text = [NSString stringWithFormat:@"%i * %i", appDelegate.imageDimensions, appDelegate.imageDimensions];   
    
    setServerIp.text = appDelegate.serverIp;
    setServerPort.text = [NSString stringWithFormat:@"%i", appDelegate.serverPort];
}

- (void)setRemoteSettings: (BOOL)enabled {
    maxDistanceLabel.enabled = !enabled;
    maxDistanceTextLabel.enabled = !enabled; 
    matchesNeededLabel.enabled = !enabled;   
    matchesNeededTextLabel.enabled = !enabled;       
    imageDimensionsLabel.enabled = !enabled;     
    imageDimensionsTextLabel.enabled = !enabled;           
    setMaxDistance.enabled = !enabled;
    setMatchesNeeded.enabled = !enabled;
    setImageDimensions.enabled = !enabled;
    
    addressTextLabel.enabled = enabled;     
    setServerIp.enabled = enabled;
    setServerPort.enabled = enabled;
    
    // Change textfield and text colors
    if(enabled) {
        setServerIp.textColor = [UIColor blackColor];        
        setServerPort.textColor = [UIColor blackColor];        
        setServerIp.backgroundColor = [UIColor whiteColor];
        setServerPort.backgroundColor = [UIColor whiteColor];        
    } 
    else {
        setServerIp.textColor = [UIColor grayColor];        
        setServerPort.textColor = [UIColor grayColor];        
        setServerIp.backgroundColor = [UIColor lightGrayColor];
        setServerPort.backgroundColor = [UIColor lightGrayColor];
    }
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == self.setServerIp || theTextField == self.setServerPort) {
        [theTextField resignFirstResponder];
    }
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [self animateTextField: textField up: YES];
}


- (void)textFieldDidEndEditing:(UITextField *)textField
{
    [self animateTextField: textField up: NO];
}

- (void) animateTextField: (UITextField*) textField up: (BOOL) up
{
    const int movementDistance = 100;
    const float movementDuration = 0.3f;
    
    int movement = (up ? -movementDistance : movementDistance);
    
    [UIView beginAnimations: @"anim" context: nil];
    [UIView setAnimationBeginsFromCurrentState: YES];
    [UIView setAnimationDuration: movementDuration];
    self.view.frame = CGRectOffset(self.view.frame, 0, movement);
    [UIView commitAnimations];
}


- (void)viewDidUnload
{
    [self setSetMaxDistance:nil];
    [self setSetMatchesNeeded:nil];
    [self setSetImageDimensions:nil];
    [self setMaxDistanceLabel:nil];
    [self setMatchesNeededLabel:nil];
    [self setImageDimensionsLabel:nil];
    [self setSwitchRemote:nil];
    [self setSetServerIp:nil];
    [self setSetServerPort:nil];
    [self setAddressTextLabel:nil];
    [self setMaxDistanceTextLabel:nil];
    [self setMatchesNeededTextLabel:nil];
    [self setImageDimensionsTextLabel:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    [setMaxDistance release];
    [setMatchesNeeded release];
    [setImageDimensions release];
    [maxDistanceLabel release];
    [matchesNeededLabel release];
    [imageDimensionsLabel release];
    [switchRemote release];
    [setServerIp release];
    [setServerPort release];
    [addressTextLabel release];
    [maxDistanceTextLabel release];
    [matchesNeededTextLabel release];
    [imageDimensionsTextLabel release];
    [super dealloc];
}
@end
