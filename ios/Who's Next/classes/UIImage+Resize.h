//
//  UIImage+Resize.h
//  Who's Next
//
//  Created by Julien Lacroix on 20/01/2016.
//  Copyright © 2016 Mobile Factory. All rights reserved.
//

#import <UIKit/UIKit.h>



#pragma mark - UIImage (Resize): Public interface

@interface UIImage (Resize)

//  Public API
// ============
- (UIImage *)squareCropOnTopImageToSideLength:(CGFloat)sideLength;
- (UIImage *)squareCropOnCenterImageToSideLength:(CGFloat)sideLength;

@end
