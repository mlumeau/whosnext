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

@interface LoginController () <UITextFieldDelegate>

// View
@property (nonatomic, weak) IBOutlet UIScrollView *scrollViewContent;
@property (nonatomic, weak) IBOutlet UITextField *textFieldName;
@property (nonatomic, weak) IBOutlet UITextField *textFieldBirthDate;
@property (nonatomic, strong) UITapGestureRecognizer *tapGestureRecognizer;
@property (nonatomic, assign) CGPoint scrollViewContentOffsetOrigin;

@end



#pragma mark - Login Controller: Implementation

@implementation LoginController



#pragma mark Life cycle

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShowNotification:) name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHideNotification:) name:UIKeyboardWillHideNotification object:nil];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initializeTextFieldName];
    [self initializeTextFieldBirthDate];
    [self initializeScrollViewGesture];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self handleConstraintsFor4s];
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
    [self.scrollViewContent removeGestureRecognizer:self.tapGestureRecognizer];
}



#pragma mark Handle user event

- (void)touchesBeganOnScrollView {
    if (self.textFieldBirthDate.isFirstResponder) {
        [self.textFieldBirthDate resignFirstResponder];
    }
    if (self.textFieldName.isFirstResponder) {
        [self.textFieldName resignFirstResponder];
    }
}



#pragma mark UIKeyboardDidShowNotification implementation

- (void)keyboardWillShowNotification:(NSNotification *)notification {
    self.scrollViewContentOffsetOrigin = self.scrollViewContent.contentOffset;
    CGFloat keyboardHeight             = [[notification.userInfo objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size.height;
    
    [self handleTextFieldAccordingToKeyboardHeight:keyboardHeight];
}

- (void)keyboardWillHideNotification:(NSNotification *)notification {
    self.scrollViewContent.contentOffset = self.scrollViewContentOffsetOrigin;
}



#pragma mark Private API: Initialize components

- (void)initializeTextFieldName {
    [self.textFieldName applyWhosNextStyleWithPlaceholder:@"Your name/pseudo..."];
}

- (void)initializeTextFieldBirthDate {
    [self.textFieldBirthDate applyWhosNextStyleWithPlaceholder:@"Your birth date..."];
}

- (void)initializeScrollViewGesture {
    self.tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(touchesBeganOnScrollView)];
    [self.scrollViewContent addGestureRecognizer:self.tapGestureRecognizer];
}



#pragma mark Private API: Handle text field according to keayboard

- (void)handleTextFieldAccordingToKeyboardHeight:(CGFloat)keyboardHeight {
    CGRect applicationFrame = [UIScreen mainScreen].bounds;
    CGFloat keyboardLevel   = applicationFrame.size.height - keyboardHeight;
    CGFloat textFieldLevel  = self.textFieldBirthDate.frame.origin.y + self.textFieldBirthDate.frame.size.height;
    
    if (keyboardLevel < textFieldLevel) {
        CGFloat offset = 20.0f; // Offset for correction frame
        self.scrollViewContent.contentOffset = CGPointMake(self.scrollViewContent.contentOffset.x, self.scrollViewContent.contentOffset.y + ( textFieldLevel - keyboardLevel + offset ));
    }
}



#pragma mark Private API: Handle constraints for iPhone 4s

- (void)handleConstraintsFor4s {
    if ([UIScreen mainScreen].bounds.size.height > 480.0f) {
        return;
    }

    for (NSLayoutConstraint *constraint in self.scrollViewContent.constraints) {
        if([@"top_picture" isEqualToString:constraint.identifier]) {
            constraint.constant = 0;
        }
        if ([@"picture_name" isEqualToString:constraint.identifier]) {
            constraint.constant = 10;
        }
    }
}

@end
