//
//  WhosNextApiStore.h
//  Who's Next
//
//  Created by Julien Lacroix on 17/12/2015.
//  Copyright © 2015 Mobile Factory. All rights reserved.
//

#import <Foundation/Foundation.h>



#pragma mark - WhosNextApiStore: Public interface

@interface WhosNextApiStore : NSObject

//  Life cycle
// ============
+ (WhosNextApiStore *)sharedStore;

@end
