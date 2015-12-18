//
//  ViewController.m
//  Who's Next
//
//  Created by Julien Lacroix on 15/12/2015.
//  Copyright © 2015 Mobile Factory. All rights reserved.
//

#import "LoginController.h"
#import "UITextField+WhosNext.h"



#pragma mark - Login Controller: Private interface

@interface LoginController ()

@property (nonatomic, weak) IBOutlet UIButton *buttonPicture;
@property (nonatomic, weak) IBOutlet UITextField *textFieldName;
@property (nonatomic, weak) IBOutlet UITextField *textFieldBirthDate;

@end



#pragma mark - Login Controller: Implementation

@implementation LoginController



#pragma mark Life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initializeTextFieldName];
    [self initializeTextFieldBirthDate];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self handleConstraintsFor4s];
}



#pragma mark Private API: Initialize components

- (void)initializeTextFieldName {
    [self.textFieldName applyWhosNextStyleWithPlaceholder:@"Your name/pseudo..."];
}

- (void)initializeTextFieldBirthDate {
    [self.textFieldBirthDate applyWhosNextStyleWithPlaceholder:@"Your birth date..."];
}



#pragma mark Private API: Handle constraints for iPhone 4s

- (void)handleConstraintsFor4s {
    if ([UIScreen mainScreen].bounds.size.height > 480.0f) {
        return;
    }
    
    for (NSLayoutConstraint *constraint in self.view.constraints) {
        if ([@"picture_name" isEqualToString:constraint.identifier]) {
            constraint.constant = 10;
        }
        if ([@"birthDate_letsGo" isEqualToString:constraint.identifier]) {
            constraint.constant = 10;
        }
        if ([@"separator_socleNetwork" isEqualToString:constraint.identifier]) {
            constraint.constant = 10;
        }
    }
}

@end
