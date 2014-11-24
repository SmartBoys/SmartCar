//
//  BlueToothService.h
//  PhoneCar
//
//  Created by 潘威 on 14-6-29.
//  Copyright (c) 2014年 中晋联合技术(北京)有限公司. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>

#define KScanBlueTooth @"scanBlueTooth"


@protocol CBCentralManagerDelegate;
@protocol CBPeripheralDelegate;
@class CBCentralManager;

@interface BlueToothService : UIViewController <CBCentralManagerDelegate,CBPeripheralDelegate,UITableViewDataSource,UITableViewDelegate>


@property (strong,nonatomic)IBOutlet UITableView * tableViewPeripheral;
@property (strong,nonatomic)IBOutlet UITextView * tvLog;
@property (strong,nonatomic)IBOutlet UIBarButtonItem * centerMgrState;

@property (strong,nonatomic) CBCentralManager * cbCentralMgr;
@property (strong,nonatomic) NSMutableArray *peripheralArray;
@property (strong,nonatomic) NSMutableArray *PeripheralIdentifiers;



-(void)addLog:(NSString*)log;
-(void)addLogWithService:(CBService*)service;
-(void)addLogWithCharacteristic:(CBCharacteristic*)characteristic;

@end
