//
//  ModerViewController.h
//  PhoneCar
//
//  Created by 潘威 on 14-6-29.
//  Copyright (c) 2014年 中晋联合技术(北京)有限公司. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "InfiniteScrollPicker.h"

@interface ModerViewController : UIViewController
@property (weak, nonatomic) IBOutlet InfiniteScrollPicker *scrollerView;
- (IBAction)backClick;

@end
