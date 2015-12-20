//
//  ViewGroupController.m
//  WhosNextIos
//
//  Created by El Amrani Benoit on 17/12/2015.
//  Copyright © 2015 MobileFactory. All rights reserved.
//

#import "ViewGroupController.h"
#import "CustomCellTableViewCell.h"


@interface ViewGroupController () <UITableViewDelegate, UITableViewDataSource, UIPickerViewDataSource, UIPickerViewDelegate, MGSwipeTableCellDelegate>

@property(nonatomic, weak) IBOutlet UITableView *groupTableView;
@property(nonatomic, weak) IBOutlet UIView *createGroupView;
@property(nonatomic, weak) IBOutlet UIButton *transparentView;
@property(nonatomic, weak) IBOutlet UIPickerView *pickerViewFrequency;
@property(nonatomic, strong) UIGestureRecognizer *tapper;
@property(nonatomic, assign) NSIndexPath *indexPathToBeDeleted;


@end

@implementation ViewGroupController

NSArray *pickerData;

#pragma mark ViewGroupController : life cycle

- (void)viewDidLoad {
    [super viewDidLoad];

    [self.groupTableView registerNib:[UINib nibWithNibName:identifier bundle:nil] forCellReuseIdentifier:identifier];
    
    [self.pickerViewFrequency selectRow:1 inComponent:0 animated:YES];
    pickerData = @[@"Daily", @"Weekly", @"Monthly"];

    
    self.tapper = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    self.tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:self.tapper];
    
    [self registerForKeyboardNotifications];
    
}

-(void)viewWillAppear:(BOOL)animated{
    self.createGroupView.hidden = YES;
    self.transparentView.hidden=YES;

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

//When the Create Group View is open and the keyboard is also open , we need to disable the transparentView
//in order to disable the animation of the Create Group View.
- (void)keyboardWasShown:(NSNotification*)aNotification {
    self.transparentView.enabled = NO;
}

- (void)keyboardWillBeHidden:(NSNotification*)aNotification {
    self.transparentView.enabled = YES;
}



#pragma mark ViewController : Handle Gesture

- (void)handleSingleTap:(UITapGestureRecognizer *) sender {
    [self.view endEditing:YES];
}

#pragma mark - ViewGroupController : handle position of the View to create a group.

- (void)moveGroupView:(UIView *)view showView:(BOOL)showView {
    CGFloat y = [UIScreen mainScreen].bounds.size.height;
    if(!showView){
        y = -y;
    }
    [UIView animateWithDuration:1.2f delay:0.0f usingSpringWithDamping:5.0f initialSpringVelocity:10.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        if(showView){
            [view setNeedsUpdateConstraints];
        }
        view.frame = CGRectMake(view.frame.origin.x, -y, view.frame.size.width, view.frame.size.height);
        if(!showView){
            dispatch_async(dispatch_get_main_queue(), ^{
                sleep(1);
                self.createGroupView.hidden = YES;
            });
        }

    } completion:NULL];
}

- (IBAction)OpenViewToCreateGroup:(UIButton *)sender {
    self.createGroupView.hidden = NO;
    self.transparentView.hidden=NO;
    self.transparentView.enabled = YES;
    [self moveGroupView:self.createGroupView showView:YES];
}

- (IBAction)HideViewGroupOnTouchExt:(UIView *)sender{
    
    self.transparentView.hidden=YES;
    [self moveGroupView:self.createGroupView showView:NO];

}

#pragma mark - ViewGroupController : Picker View for Frequency

// The number of columns of data
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

// The number of rows of data
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return pickerData.count;
}

-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, pickerView.frame.size.width, 30)];
    label.textColor = [UIColor whiteColor];
    label.textAlignment = NSTextAlignmentCenter;
    label.text = [NSString stringWithFormat:@"  %@", pickerData[row]];
    return label;
}


#pragma mark - ViewGroupController : Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 100;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CustomCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CustomCellTableViewCell" forIndexPath:indexPath];
    if(indexPath.item > 3){
        [cell updateCellWithText :@"Croissant"];
    }
    else {
        [cell updateCellWithText :@"Bières"];
    }
    [cell updateCellWithImage:[UIImage imageNamed:@"facebook"]];
    cell.delegate = self;

    cell.rightButtons = @[[MGSwipeButton buttonWithTitle:@"Delete" backgroundColor:[UIColor redColor]]];
    cell.rightSwipeSettings.transition = MGSwipeTransitionRotate3D;

    return cell;
}

-(BOOL) swipeTableCell:(MGSwipeTableCell*) cell tappedButtonAtIndex:(NSInteger) index direction:(MGSwipeDirection)direction fromExpansion:(BOOL) fromExpansion{
    UIAlertView *messageAlertDelete = [[UIAlertView alloc]
                                       initWithTitle:@"Row Selected" message:@"Do you really want to leave this group ?" delegate:nil cancelButtonTitle:@"NO" otherButtonTitles:@"YES", nil];
    messageAlertDelete.delegate = self;
    
    [messageAlertDelete show];
    return false;
};


#pragma mark - ViewGroupController : Handle Answer of AlertView to delete or not element from tableView

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 1){
        //TODO : delete user on group on the parse database and on the table view.
    }
}


#pragma mark -ViewGroupController : Handle item click

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    //TODO : navigate to last screen.
    
}


@end
