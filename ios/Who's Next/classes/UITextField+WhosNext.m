//
//  UITextField+WhosNext.m
//  Who's Next
//
//  Created by Julien Lacroix on 17/12/2015.
//  Copyright © 2015 Mobile Factory. All rights reserved.
//

#import "UITextField+WhosNext.h"
#import "UIColor+WhosNext.h"



#pragma mark - UITextField+WhosNext: Implementation

@implementation UITextField (WhosNext)

#pragma mark Public API

- (void)applyWhosNextStyleWithPlaceholder:(NSString *)placeholder {
    self.backgroundColor       = [UIColor whosNextDarkBlueColorWithAlpha:0.5f];
    self.tintColor             = [UIColor whosNextWhiteColor];
    self.textAlignment         = NSTextAlignmentCenter;
    self.keyboardAppearance    = UIKeyboardAppearanceDark;
    self.font                  = [UIFont fontWithName:@"AvenirNext-UltraLight" size:20.0f];
    self.returnKeyType         = UIReturnKeyDone;
    self.attributedPlaceholder = [[NSAttributedString alloc] initWithString:placeholder attributes:@{NSForegroundColorAttributeName: [UIColor whosNextWhiteColor]}];
}

@end
