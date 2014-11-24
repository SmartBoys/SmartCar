//
//  ModerViewController.m
//  PhoneCar
//
//  Created by 潘威 on 14-6-29.
//  Copyright (c) 2014年 中晋联合技术(北京)有限公司. All rights reserved.
//

#import "ModerViewController.h"


@interface ModerViewController ()
{
    InfiniteScrollPicker *isp;
}
@end

@implementation ModerViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:3];
    for (NSInteger index = 0; index < 3; index++)
    {
        NSString *imageName = [NSString stringWithFormat:@"m%d_s.png",index];
        [array addObject:[UIImage imageNamed:imageName]];
    }
    
    isp = [[InfiniteScrollPicker alloc] initWithFrame:CGRectMake(60, 30, 320, 200)];
    [isp setImageAry:array];
    [isp setHeightOffset:60];
    [isp setPositionRatio:2];
    [isp setAlphaOfobjs:0.8];
    [self.view addSubview:isp];
    [isp setBackgroundColor:[UIColor redColor]];

    
}

- (void)infiniteScrollPicker:(InfiniteScrollPicker *)infiniteScrollPicker didSelectAtImage:(UIImage *)image
{
    NSLog(@"selected");
}


- (IBAction)backClick
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - 隐藏状态拦

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

@end
