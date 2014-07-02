//
//  MainViewController.h
//  PhoneCar
//
//  Created by 潘威 on 14-6-29.
//  Copyright (c) 2014年 中晋联合技术(北京)有限公司. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BlueToothService.h"


@interface MainViewController : BlueToothService


@property (nonatomic, strong) CBPeripheral * peripheral;

@property (weak, nonatomic) IBOutlet UIView *backView;
- (IBAction)backClick;

-(IBAction) reScan:(id)Sender;

- (IBAction)comBtnClick:(id)sender;

@end
