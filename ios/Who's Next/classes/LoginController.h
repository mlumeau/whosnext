//
//  ViewController.h
//  Who's Next
//
//  Created by Julien Lacroix on 15/12/2015.
//  Copyright © 2015 Mobile Factory. All rights reserved.
//

#import <UIKit/UIKit.h>



#pragma mark - Login Controller: Public interface

@interface LoginController : UIViewController

- (IBAction)takeOrSelectPhotoOnButtonClick:(UIButton *)sender;
- (void)takePhoto;
- (void)selectPhoto;

@end
