//
//  CustomCellTableViewCell.h
//  Formation
//
//  Created by El Amrani Benoit on 16/12/2015.
//  Copyright © 2015 Sopra Steria. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MGSwipeTableCell.h"

extern NSString* const identifier;

@interface CustomCellTableViewCell : MGSwipeTableCell

@property(nonatomic, weak) IBOutlet UILabel *title;
@property(nonatomic, weak) IBOutlet UIImageView *image;

-(void) updateCellWithText:(NSString*)string;
-(void) updateCellWithImage: (UIImage*)image;


@end
