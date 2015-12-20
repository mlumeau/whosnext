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

@interface LoginController () <UIImagePickerControllerDelegate, UINavigationControllerDelegate, UITextFieldDelegate>

@property (nonatomic, weak) IBOutlet UIButton *buttonPicture;
@property (nonatomic, weak) IBOutlet UITextField *textFieldName;
@property (nonatomic, weak) IBOutlet UITextField *textFieldBirthDate;
@property (nonatomic, weak) IBOutlet UIScrollView *scrollView;
@property(nonatomic, strong) UIGestureRecognizer *tapper;
@property(nonatomic, assign) BOOL hasScroll;


@end



#pragma mark - Login Controller: Implementation

@implementation LoginController



#pragma mark Life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initializeTextFieldName];
    [self initializeTextFieldBirthDate];
    
    self.tapper = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    self.tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:self.tapper];
    
    self.hasScroll = false;
    [self registerForKeyboardNotifications];

}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self handleConstraintsFor4s];
}


-(void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
}

-(void)viewWillDisappear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:NO animated:animated];
}

- (void)dealloc
{
    // unregister for keyboard notifications
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidHideNotification object:nil];
    
    [self.view removeGestureRecognizer:self.tapper];
    
}

#pragma mark ViewContoller : Handle the keyboard notifications

// Call this method somewhere in your view controller setup code.
- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillBeHidden:)
                                                 name:UIKeyboardWillHideNotification object:nil];
    
}

- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);

    CGRect aRect = [UIScreen mainScreen].bounds;
    CGFloat kbLevel = aRect.size.height - kbSize.height;
    CGFloat textFieldBirthDateYLevel= self.textFieldBirthDate.frame.size.height + self.textFieldBirthDate.frame.origin.y;
    
    if (kbLevel < textFieldBirthDateYLevel) {
        self.scrollView.contentInset = contentInsets;
        self.scrollView.scrollIndicatorInsets = contentInsets;
        [self.scrollView scrollRectToVisible:self.textFieldBirthDate.frame animated:YES];
        self.hasScroll = true;
    }
}

- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    if(self.hasScroll){
        UIEdgeInsets contentInsets = UIEdgeInsetsZero;
        self.scrollView.contentInset = contentInsets;
        self.scrollView.scrollIndicatorInsets = contentInsets;
    }
}


#pragma mark ViewController : Handle Gesture

- (void)handleSingleTap:(UITapGestureRecognizer *) sender
{
    [self.view endEditing:YES];
}

#pragma mark Private API: Initialize components

- (void)initializeTextFieldName {
    [self.textFieldName applyWhosNextStyleWithPlaceholder:@"Your name/pseudo..."];
}

- (void)initializeTextFieldBirthDate {
    [self.textFieldBirthDate applyWhosNextStyleWithPlaceholder:@"Your birthdate..."];
}

#pragma mark ViewController: Manage Pictures Picker

- (IBAction)takeOrSelectPhotoOnButtonClick:(UIButton *)sender {
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Information" message:@"Choose the way to select your picture"
                                                            preferredStyle:UIAlertControllerStyleActionSheet];
    
    UIAlertAction* takePhoto = [UIAlertAction
                                actionWithTitle:@"Take a picture"
                                style:UIAlertActionStyleDefault
                                handler:^(UIAlertAction * action)
                                {
                                    [self takePhoto];
                                    
                                }];
    UIAlertAction* selectPhoto = [UIAlertAction
                                  actionWithTitle:@"Select Picture from your library"
                                  style:UIAlertActionStyleDefault
                                  handler:^(UIAlertAction * action)
                                  {
                                      [self selectPhoto];
                                  }];
    
    UIAlertAction* cancel = [UIAlertAction
                             actionWithTitle:@"Cancel"
                             style:UIAlertActionStyleDefault
                             handler:^(UIAlertAction * action)
                             {
                                 [self dismissViewControllerAnimated:true completion:^{
                                     
                                 }];
                             }];
    
    
    [alert addAction:takePhoto];
    [alert addAction:selectPhoto];
    [alert addAction:cancel];
    
    [self presentViewController:alert animated:YES completion:nil];
    
}

- (void)takePhoto {
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    picker.delegate                 = self;
    picker.allowsEditing            = NO;
    picker.sourceType               = UIImagePickerControllerSourceTypeCamera;
    
    [self presentViewController:picker animated:YES completion:NULL];
}

- (void)selectPhoto {
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.allowsEditing = NO;
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    
    [self presentViewController:picker animated:YES completion:NULL];
}



- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:NULL];
}


- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [self.buttonPicture setBackgroundImage:info[UIImagePickerControllerOriginalImage] forState:UIControlStateNormal];
    
    
    if(picker.sourceType == UIImagePickerControllerSourceTypeCamera){
        UIImageWriteToSavedPhotosAlbum(info[UIImagePickerControllerOriginalImage] , nil, nil, nil);
    }
    
    [picker dismissViewControllerAnimated:YES completion:NULL];
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
