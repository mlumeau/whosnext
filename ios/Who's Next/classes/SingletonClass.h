//
//  SingletonClass.h
//  Who's Next
//
//  Created by Julien Lacroix on 17/12/2015.
//  Copyright © 2015 Mobile Factory. All rights reserved.
//

#define SINGLETON_CLASS(class, singleton_name)  \
+ (class *)singleton_name {                     \
    static dispatch_once_t onceToken;           \
    static class *singleton_name = nil;         \
    dispatch_once(&onceToken, ^{                \
        singleton_name = [class new];           \
    });                                         \
    return singleton_name;                      \
}                                               \
