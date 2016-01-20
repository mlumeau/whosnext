//
//  UIImage+Resize.m
//  Who's Next
//
//  Created by Julien Lacroix on 20/01/2016.
//  Copyright © 2016 Mobile Factory. All rights reserved.
//

#import "UIImage+Resize.h"



#pragma mark UIImage (Resize): Implementation

@implementation UIImage (Resize)



#pragma mark Public API

- (UIImage *)squareCropOnTopImageToSideLength:(CGFloat)sideLength {
    return [self squareCropImageWithSideLength:sideLength cropOnCenter:NO];
}

- (UIImage *)squareCropOnCenterImageToSideLength:(CGFloat)sideLength {
    return [self squareCropImageWithSideLength:sideLength cropOnCenter:YES];
}



#pragma mark Private API

- (UIImage *)squareCropImageWithSideLength:(CGFloat)sideLength cropOnCenter:(BOOL)cropOnCenter {
    // input size comes from image
    CGSize inputSize = self.size;
    
    // round up side length to avoid fractional output size
    sideLength = ceilf(sideLength);
    
    // output size has sideLength for both dimensions
    CGSize outputSize = CGSizeMake(sideLength, sideLength);
    
    // calculate scale so that smaller dimension fits sideLength
    CGFloat scale = MAX(sideLength / inputSize.width, sideLength / inputSize.height);
    
    // scaling the image with this scale results in this output size
    CGSize scaledInputSize = CGSizeMake(inputSize.width * scale, inputSize.height * scale);
    
    // determine point in center of "canvas"
    CGPoint center = CGPointMake(outputSize.width/2.0, outputSize.height/2.0);
    
    // calculate drawing rect relative to output Size
    float x = 0.0f;
    float y = 0.0f;
    if (cropOnCenter) {
        x = center.x - scaledInputSize.width/2.0;
        y = center.y - scaledInputSize.height/2.0;
    }
    CGRect outputRect = CGRectMake(x, y, scaledInputSize.width, scaledInputSize.height);
    
    // begin a new bitmap context, scale 0 takes display scale
    UIGraphicsBeginImageContextWithOptions(outputSize, YES, 0);
    
    // optional: set the interpolation quality.
    // For this you need to grab the underlying CGContext
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGContextSetInterpolationQuality(ctx, kCGInterpolationHigh);
    
    // draw the source image into the calculated rect
    [self drawInRect:outputRect];
    
    // create new image from bitmap context
    UIImage *outImage = UIGraphicsGetImageFromCurrentImageContext();
    
    // clean up
    UIGraphicsEndImageContext();
    
    // pass back new image
    return outImage;
}

@end
