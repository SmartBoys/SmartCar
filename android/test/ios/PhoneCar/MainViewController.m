//
//  MainViewController.m
//  PhoneCar
//
//  Created by 潘威 on 14-6-29.
//  Copyright (c) 2014年 中晋联合技术(北京)有限公司. All rights reserved.
//

#import "MainViewController.h"
#import "CoreBluetooth/CoreBluetooth.h"


@interface MainViewController ()

@end

@implementation MainViewController



- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
}

- (IBAction)backClick
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)comBtnClick:(UIButton *)sender
{
    
    if (self.peripheral != nil)
    {
        for (CBService *service in self.peripheral.services)
        {
            
            
            for (CBCharacteristic * characteristic in service.characteristics)
            {
                NSLog(@"%@",characteristic);
                
                
                
                if ([characteristic.UUID isEqual:[CBUUID UUIDWithString:@"FFF2"]] )
                {
                    
                    NSString *commad =sender.titleLabel.text;
                    NSData *comData = [commad dataUsingEncoding:NSUTF8StringEncoding];
                    
                    [self.peripheral writeValue:comData forCharacteristic:characteristic type:CBCharacteristicWriteWithResponse];
                    
                    //                        characteristic.value
                }
                
            }
            
        }

    }
    
    
}


-(IBAction) reScan:(id)Sender
{
    [self.cbCentralMgr stopScan];
    [self.tvLog setText:@""];
    [self.peripheralArray removeAllObjects];
    [self.PeripheralIdentifiers removeAllObjects];
    
    NSDictionary * dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithBool:NO],CBCentralManagerScanOptionAllowDuplicatesKey, nil];
    [self.cbCentralMgr scanForPeripheralsWithServices:nil options:dic];
}


#pragma mark - 


#pragma mark - UITableViewDataSource
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.peripheralArray.count;
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cellIdentifier = @"cell";
    UITableViewCell * cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cellIdentifier];
    }
    
    if (self.peripheralArray.count > indexPath.row) {
        CBPeripheral *peripheral = [self.peripheralArray objectAtIndex:indexPath.row];
        NSString *state;
        switch (peripheral.state) {
            case CBPeripheralStateConnected:
                state = @"Connected";
                break;
            case CBPeripheralStateConnecting:
                state = @"Connecting";
                break;
            case CBPeripheralStateDisconnected:
                state = @"Disconnect";
                break;
                
            default:
                break;
        }
        
        cell.textLabel.text = [NSString stringWithFormat:@"%@  %@", peripheral.name,state];
        cell.detailTextLabel.text = [peripheral.identifier UUIDString];
        if (peripheral.isConnected) {
            cell.accessoryType = UITableViewCellAccessoryDetailButton;
        }else{
            cell.accessoryType = UITableViewCellAccessoryNone;
        }
        
    }
    return cell;
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    CBPeripheral * peripheral = [self.peripheralArray objectAtIndex:indexPath.row];
    
    
    if (peripheral.isConnected) {
        [self.cbCentralMgr cancelPeripheralConnection:peripheral];
    }else{
        
        [self.cbCentralMgr connectPeripheral:peripheral options:[NSDictionary dictionaryWithObject:[NSNumber numberWithBool:YES] forKey:CBConnectPeripheralOptionNotifyOnDisconnectionKey]];
        [self.backView removeFromSuperview];
        self.peripheral = peripheral;
    }
    
}


#pragma mark - 隐藏状态拦
- (BOOL)prefersStatusBarHidden
{
    return YES;
}
@end
