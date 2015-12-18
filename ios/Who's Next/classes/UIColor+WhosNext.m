//
//  UIColor+WhosNext.m
//  Who's Next
//
//  Created by Julien Lacroix on 17/12/2015.
//  Copyright © 2015 Mobile Factory. All rights reserved.
//

#import "UIColor+WhosNext.h"



#pragma mark - UIColor+WhosNext: Implementation

@implementation UIColor (WhosNext)



#pragma mark Public API

+ (UIColor *)whosNextDarkBlueColorWithAlpha:(CGFloat)alpha {
    return [UIColor colorWithRed:2.0f/255.0f green:136.0f/255.0f blue:209.0f/255.0f alpha:alpha];
}

+ (UIColor *)whosNextDarkBlueColor {
    return [UIColor colorWithRed:2.0f/255.0f green:136.0f/255.0f blue:209.0f/255.0f alpha:1.0f];
}

+ (UIColor *)whosNextWhiteColor {
    return [UIColor colorWithRed:251.0f/255.0f green:251.0f/255.0f blue:251.0f/255.0f alpha:1.0f];
}

@end
