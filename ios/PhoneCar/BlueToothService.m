//
//  BlueToothService.m
//  PhoneCar
//
//  Created by 潘威 on 14-6-29.
//  Copyright (c) 2014年 中晋联合技术(北京)有限公司. All rights reserved.
//

#import "BlueToothService.h"
#import "CoreBluetooth/CoreBluetooth.h"


@interface BlueToothService ()


@end

@implementation BlueToothService

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self addLog:@"viewDidLoad"];
    
    self.navigationItem.leftItemsSupplementBackButton = true;
    
    
    self.cbCentralMgr = [[CBCentralManager alloc] initWithDelegate:self queue:nil];
    self.peripheralArray = [NSMutableArray array];
    self.PeripheralIdentifiers = [NSMutableArray array];
    
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidAppear:(BOOL)animated
{
    [self.centerMgrState setTitle:[self getcentralManagerStateStr:self.cbCentralMgr]];
    
}

-(void)viewWillDisappear:(BOOL)animated
{
    self.cbCentralMgr.delegate = nil;
    [self.cbCentralMgr stopScan];
    
}

#pragma mark - tool
-(void)addLog:(NSString*)log
{
    [self.tvLog setText:[NSString stringWithFormat:@"%@ \n%@",self.tvLog.text,log]];
    NSLog(@"%@",log);
}

-(void)addLogWithService:(CBService*)service
{
    [self addLog:[NSString stringWithFormat:@"%@ uuid:%@",service,service.UUID]];
    
}
-(void)addLogWithCharacteristic:(CBCharacteristic*)characteristic
{
    [self addLog:[NSString stringWithFormat:@"%@ characteristic:%@",characteristic,characteristic.UUID]];
    
}


#pragma mark -CBCentralManagerDelegate

-(NSString*)getcentralManagerStateStr:(CBCentralManager*)central
{
    NSString* result;
    switch (central.state) {
        case CBCentralManagerStatePoweredOff:
            result = @"PoweredOff";
            break;
            
        case CBCentralManagerStatePoweredOn:
            result = @"PoweredOn";
            break;
            
        case CBCentralManagerStateResetting:
            result = @"Resetting";
            break;
            
        case CBCentralManagerStateUnauthorized:
            result = @"Unauthorized";
            break;
            
        case CBCentralManagerStateUnknown:
            result = @"Unknown";
            break;
            
        case CBCentralManagerStateUnsupported:
            result = @"Unsupported";
            break;
            
        default:
            result = @"没有啦";
            break;
    }
    return result;
}


- (void)centralManagerDidUpdateState:(CBCentralManager *)central
{
    [self addLog:@"------------centralManagerDidUpdateState---------------"];
    switch (central.state) {
        case CBCentralManagerStateUnknown:
            [self addLog:@"State Unknown"];
            [self.centerMgrState setTitle: @"State Unknown"];
        breakcenterMgrStateCentralManagerStateResetting:
            [self addLog:@"State Resetting"];
            [self.centerMgrState setTitle: @"State Resetting"];
            break;
            
        case CBCentralManagerStateUnsupported:
            [self addLog:@"State Unsupported"];
            [self.centerMgrState setTitle: @"State Unsupported"];
            break;
            
        case CBCentralManagerStateUnauthorized:
            [self addLog:@"State Unauthorized"];
            [self.centerMgrState setTitle: @"State Unauthorized"];
            break;
            
        case CBCentralManagerStatePoweredOff:
            [self addLog:@"State PoweredOff"];
            [self.centerMgrState setTitle: @"State PoweredOff"];
            break;
            
        case CBCentralManagerStatePoweredOn:
            [self addLog:@"State PoweredOn"];
            [self.centerMgrState setTitle: @"State PoweredOn"];
            break;
            
        default:
            
            [self addLog:@"State 未知"];
            [self.centerMgrState setTitle: @"State  未知"];
            break;
    }
    
}


- (void)centralManager:(CBCentralManager *)central didDiscoverPeripheral:(CBPeripheral *)peripheral advertisementData:(NSDictionary *)advertisementData RSSI:(NSNumber *)RSSI
{
    [self addLog:@"------------didDiscoverPeripheral---------------"];
    [self addLog:peripheral.name];
    [self addLog:[peripheral.RSSI stringValue]];
    [self addLog:[peripheral.identifier UUIDString]];
    
    [self.peripheralArray addObject:peripheral];
    [self.tableViewPeripheral reloadData];
    
//    //发送通知
//    NSDictionary *dict = [NSDictionary dictionaryWithObjectsAndKeys:self.peripheralArray,KScanBlueTooth, nil];
//    [[NSNotificationCenter defaultCenter] postNotificationName:KScanBlueTooth object:nil userInfo:dict];
}

- (void)centralManager:(CBCentralManager *)central willRestoreState:(NSDictionary *)dict
{
    [self addLog:@"-------------willRestoreState-----------------"];
    [self addLog:[NSString stringWithFormat:@"%@",dict]];
    
    
}
- (void)centralManager:(CBCentralManager *)central didRetrievePeripherals:(NSArray *)peripherals
{
    [self addLog:@"-------------didRetrievePeripherals-----------------"];
    for (CBPeripheral * peripheral in peripherals) {
        [self addLog:[NSString stringWithFormat:@"%@ name:%@",peripheral,peripheral.name]];
    }
    
}

/*!
 *  @method centralManager:didRetrieveConnectedPeripherals:
 *
 *  @param central      The central manager providing this information.
 *  @param peripherals  A list of <code>CBPeripheral</code> objects representing all peripherals currently connected to the system.
 *
 *  @discussion         This method returns the result of a {@link retrieveConnectedPeripherals} call.
 *
 */
- (void)centralManager:(CBCentralManager *)central didRetrieveConnectedPeripherals:(NSArray *)peripherals
{
    [self addLog:@"-------------didRetrieveConnectedPeripherals-----------------"];
    for (CBPeripheral * peripheral in peripherals) {
        [self addLog:[NSString stringWithFormat:@"%@ name:%@",peripheral,peripheral.name]];
    }
    
    
}

/*!
 *  @method centralManager:didConnectPeripheral:
 *
 *  @param central      The central manager providing this information.
 *  @param peripheral   The <code>CBPeripheral</code> that has connected.
 *
 *  @discussion         This method is invoked when a connection initiated by {@link connectPeripheral:options:} has succeeded.
 *
 */
- (void)centralManager:(CBCentralManager *)central didConnectPeripheral:(CBPeripheral *)peripheral
{
    [self addLog:@"-------------didConnectPeripheral-----------------"];
    [self addLog:peripheral.name];
    [self.tableViewPeripheral reloadData];
    [self.PeripheralIdentifiers addObject:peripheral];
    
    peripheral.delegate = self;
    //查询蓝牙服务
    [peripheral discoverServices:nil];
    
}

/*!
 *  @method centralManager:didFailToConnectPeripheral:error:
 *
 *  @param central      The central manager providing this information.
 *  @param peripheral   The <code>CBPeripheral</code> that has failed to connect.
 *  @param error        The cause of the failure.
 *
 *  @discussion         This method is invoked when a connection initiated by {@link connectPeripheral:options:} has failed to complete. As connection attempts do not
 *                      timeout, the failure of a connection is atypical and usually indicative of a transient issue.
 *
 */
- (void)centralManager:(CBCentralManager *)central didFailToConnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
    [self addLog:@"-------------didFailToConnectPeripheral-----------------"];
    [self addLog:peripheral.name];
    
    
}

/*!
 *  @method centralManager:didDisconnectPeripheral:error:
 *
 *  @param central      The central manager providing this information.
 *  @param peripheral   The <code>CBPeripheral</code> that has disconnected.
 *  @param error        If an error occurred, the cause of the failure.
 *
 *  @discussion         This method is invoked upon the disconnection of a peripheral that was connected by {@link connectPeripheral:options:}. If the disconnection
 *                      was not initiated by {@link cancelPeripheralConnection}, the cause will be detailed in the <i>error</i> parameter. Once this method has been
 *                      called, no more methods will be invoked on <i>peripheral</i>'s <code>CBPeripheralDelegate</code>.
 *
 */
- (void)centralManager:(CBCentralManager *)central didDisconnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
    [self addLog:@"-------------didDisconnectPeripheral-----------------"];
    [self addLog:peripheral.name];
    [self.tableViewPeripheral reloadData];
    
    
}

#pragma mark CBPeripheralDelegate

/*!
 *  @method peripheralDidUpdateName:
 *
 *  @param peripheral	The peripheral providing this update.
 *
 *  @discussion			This method is invoked when the @link name @/link of <i>peripheral</i> changes.
 */
- (void)peripheralDidUpdateName:(CBPeripheral *)peripheral NS_AVAILABLE(NA, 6_0)
{
    [self addLog:@"-------------peripheralDidUpdateName-----------------"];
    [self addLog:peripheral.name];
    
}

/*!
 *  @method peripheralDidInvalidateServices:
 *
 *  @param peripheral	The peripheral providing this update.
 *
 *  @discussion			This method is invoked when the @link services @/link of <i>peripheral</i> have been changed. At this point,
 *						all existing <code>CBService</code> objects are invalidated. Services can be re-discovered via @link discoverServices: @/link.
 *
 *	@deprecated			Use {@link peripheral:didModifyServices:} instead.
 */
- (void)peripheralDidInvalidateServices:(CBPeripheral *)peripheral NS_DEPRECATED(NA, NA, 6_0, 7_0)
{
    [self addLog:@"-------------peripheralDidInvalidateServices-----------------"];
    [self addLog:peripheral.name];
    
}

/*!
 *  @method peripheral:didModifyServices:
 *
 *  @param peripheral			The peripheral providing this update.
 *  @param invalidatedServices	The services that have been invalidated
 *
 *  @discussion			This method is invoked when the @link services @/link of <i>peripheral</i> have been changed.
 *						At this point, the designated <code>CBService</code> objects have been invalidated.
 *						Services can be re-discovered via @link discoverServices: @/link.
 */
- (void)peripheral:(CBPeripheral *)peripheral didModifyServices:(NSArray *)invalidatedServices NS_AVAILABLE(NA, 7_0)
{
    [self addLog:@"-------------didModifyServices-----------------"];
    [self addLog:peripheral.name];
    
}

/*!
 *  @method peripheralDidUpdateRSSI:error:
 *
 *  @param peripheral	The peripheral providing this update.
 *	@param error		If an error occurred, the cause of the failure.
 *
 *  @discussion			This method returns the result of a @link readRSSI: @/link call.
 */
- (void)peripheralDidUpdateRSSI:(CBPeripheral *)peripheral error:(NSError *)error
{
    [self addLog:@"-------------peripheralDidUpdateRSSI-----------------"];
    [self addLog:peripheral.name];
    
}

/*!
 *  @method peripheral:didDiscoverServices:
 *
 *  @param peripheral	The peripheral providing this information.
 *	@param error		If an error occurred, the cause of the failure.
 *
 *  @discussion			This method returns the result of a @link discoverServices: @/link call. If the service(s) were read successfully, they can be retrieved via
 *						<i>peripheral</i>'s @link services @/link property.
 *
 */
//返回的蓝牙服务通知通过代理实现
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverServices:(NSError *)error
{
    
    [self addLog:@"-------------didDiscoverServices-----------------"];
    [self addLog:peripheral.name];
    
    for (CBService* service in peripheral.services){
        [self addLogWithService:service];
        //查询服务所带的特征值
        [peripheral discoverCharacteristics:nil forService:service];
        [peripheral discoverIncludedServices:nil forService:service];
    }
}

/*!
 *  @method peripheral:didDiscoverIncludedServicesForService:error:
 *
 *  @param peripheral	The peripheral providing this information.
 *  @param service		The <code>CBService</code> object containing the included services.
 *	@param error		If an error occurred, the cause of the failure.
 *
 *  @discussion			This method returns the result of a @link discoverIncludedServices:forService: @/link call. If the included service(s) were read successfully,
 *						they can be retrieved via <i>service</i>'s <code>includedServices</code> property.
 */
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverIncludedServicesForService:(CBService *)service error:(NSError *)error
{
    [self addLog:@"-------------didDiscoverIncludedServicesForService-----------------"];
    [self addLog:peripheral.name];
    //    [self addLogWithService: service];
    for (CBService * server in service.includedServices) {
        [self addLogWithService: service];
    }
    
}

/*!
 *  @method peripheral:didDiscoverCharacteristicsForService:error:
 *
 *  @param peripheral	The peripheral providing this information.
 *  @param service		The <code>CBService</code> object containing the characteristic(s).
 *	@param error		If an error occurred, the cause of the failure.
 *
 *  @discussion			This method returns the result of a @link discoverCharacteristics:forService: @/link call. If the characteristic(s) were read successfully,
 *						they can be retrieved via <i>service</i>'s <code>characteristics</code> property.
 */

//返回的蓝牙特征值通知通过代理实现
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverCharacteristicsForService:(CBService *)service error:(NSError *)error
{
    
    [self addLog:@"-------------didDiscoverCharacteristicsForService-----------------"];
    [self addLog:peripheral.name];
    [self addLog:[NSString stringWithFormat:@"%@",service]];
    for (CBCharacteristic * characteristic in service.characteristics) {
        [self addLogWithCharacteristic:characteristic];
        [peripheral setNotifyValue:YES forCharacteristic:characteristic];
        Byte byteData[20] = {0x10, 0x52, 0x90, 0xe8, 0x08, 0x02, 0x18, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0xb4, 0xf2, 0xd3, 0xd6, 0xd0};
        NSData * data = [NSData dataWithBytes:byteData length:20];
        //给蓝牙发数据
        [peripheral writeValue:data forCharacteristic:characteristic type:CBCharacteristicWriteWithResponse];
        [NSThread sleepForTimeInterval:0.1];
        
        Byte byteData1[13] = {0x10, 0xcb, 0xde, 0x4d, 0xa0, 0x9c, 0x26, 0x8c, 0x95, 0x7f, 0xf8, 0x97, 0x94};
        data = [NSData dataWithBytes:byteData1 length:20];
        [peripheral writeValue:data forCharacteristic:characteristic type:CBCharacteristicWriteWithResponse];
        
    }
}

/*!
 *  @method peripheral:didUpdateValueForCharacteristic:error:
 *
 *  @param peripheral		The peripheral providing this information.
 *  @param characteristic	A <code>CBCharacteristic</code> object.
 *	@param error			If an error occurred, the cause of the failure.
 *
 *  @discussion				This method is invoked after a @link readValueForCharacteristic: @/link call, or upon receipt of a notification/indication.
 */
// 处理蓝牙发过来的数据
- (void)peripheral:(CBPeripheral *)peripheral didUpdateValueForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    [self addLog:@"-------------didUpdateValueForCharacteristic-----------------"];
    [self addLog:peripheral.name];
    [self addLog:[NSString stringWithFormat:@"%@ value:%@",characteristic,characteristic.value]];
    
}

/*!
 *  @method peripheral:didWriteValueForCharacteristic:error:
 *
 *  @param peripheral		The peripheral providing this information.
 *  @param characteristic	A <code>CBCharacteristic</code> object.
 *	@param error			If an error occurred, the cause of the failure.
 *
 *  @discussion				This method returns the result of a {@link writeValue:forCharacteristic:type:} call, when the <code>CBCharacteristicWriteWithResponse</code> type is used.
 */
//给蓝牙发数据触发的代理
- (void)peripheral:(CBPeripheral *)peripheral didWriteValueForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    
    [self addLog:@"-------------didWriteValueForCharacteristic-----------------"];
    [self addLog:peripheral.name];
    if ([[[UIDevice currentDevice] systemVersion] floatValue]>=7.0)
    {
        [self addLog:[NSString stringWithFormat:@"%@ value:%@",characteristic,characteristic.value]];//ios7时，这里的value并不是写进去的值
    }else{
        [self addLog:[NSString stringWithFormat:@"%@ value:%@",characteristic,characteristic.value]];
    }
    
}

/*!
 *  @method peripheral:didUpdateNotificationStateForCharacteristic:error:
 *
 *  @param peripheral		The peripheral providing this information.
 *  @param characteristic	A <code>CBCharacteristic</code> object.
 *	@param error			If an error occurred, the cause of the failure.
 *
 *  @discussion				This method returns the result of a @link setNotifyValue:forCharacteristic: @/link call.
 */
- (void)peripheral:(CBPeripheral *)peripheral didUpdateNotificationStateForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    
    [self addLog:@"-------------didUpdateNotificationStateForCharacteristic-----------------"];
    [self addLog:peripheral.name];
    [self addLog:[NSString stringWithFormat:@"%@",characteristic]];
}

/*!
 *  @method peripheral:didDiscoverDescriptorsForCharacteristic:error:
 *
 *  @param peripheral		The peripheral providing this information.
 *  @param characteristic	A <code>CBCharacteristic</code> object.
 *	@param error			If an error occurred, the cause of the failure.
 *
 *  @discussion				This method returns the result of a @link discoverDescriptorsForCharacteristic: @/link call. If the descriptors were read successfully,
 *							they can be retrieved via <i>characteristic</i>'s <code>descriptors</code> property.
 */
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverDescriptorsForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    [self addLog:@"-------------didDiscoverDescriptorsForCharacteristic-----------------"];
    [self addLog:peripheral.name];
    [self addLog:[NSString stringWithFormat:@"%@",characteristic]];
    
}

/*!
 *  @method peripheral:didUpdateValueForDescriptor:error:
 *
 *  @param peripheral		The peripheral providing this information.
 *  @param descriptor		A <code>CBDescriptor</code> object.
 *	@param error			If an error occurred, the cause of the failure.
 *
 *  @discussion				This method returns the result of a @link readValueForDescriptor: @/link call.
 */
- (void)peripheral:(CBPeripheral *)peripheral didUpdateValueForDescriptor:(CBDescriptor *)descriptor error:(NSError *)error
{
    [self addLog:@"-------------didUpdateValueForDescriptor-----------------"];
    [self addLog:peripheral.name];
    [self addLog:[NSString stringWithFormat:@"%@",descriptor]];
    
}

/*!
 *  @method peripheral:didWriteValueForDescriptor:error:
 *
 *  @param peripheral		The peripheral providing this information.
 *  @param descriptor		A <code>CBDescriptor</code> object.
 *	@param error			If an error occurred, the cause of the failure.
 *
 *  @discussion				This method returns the result of a @link writeValue:forDescriptor: @/link call.
 */
- (void)peripheral:(CBPeripheral *)peripheral didWriteValueForDescriptor:(CBDescriptor *)descriptor error:(NSError *)error
{
    [self addLog:@"-------------didWriteValueForDescriptor-----------------"];
    [self addLog:peripheral.name];
    [self addLog:[NSString stringWithFormat:@"%@",descriptor]];
    
}


#pragma mark - UITableViewDataSource
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 0;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

@end


