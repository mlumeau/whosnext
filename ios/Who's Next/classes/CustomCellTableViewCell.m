//
//  CustomCellTableViewCell.m
//  Formation
//
//  Created by El Amrani Benoit on 16/12/2015.
//  Copyright © 2015 Sopra Steria. All rights reserved.
//

#import "CustomCellTableViewCell.h"

NSString* const identifier = @"CustomCellTableViewCell";

@interface CustomCellTableViewCell()



@end

@implementation CustomCellTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void) updateCellWithText: (NSString*)string {
    self.title.text = string;
}

-(void) updateCellWithImage: (UIImage*)image {
    self.image.image = image;
}

@end
