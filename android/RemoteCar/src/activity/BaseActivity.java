package activity;

import java.util.List;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import bluetooth.BluetoothLeService;
import bluetooth.SampleGattAttributes;
import com.zjlh.remotecar.R;

public class BaseActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 10000;
	protected BluetoothAdapter mBluetoothAdapter;
	public Handler mHandler = new Handler();
	protected String mBeaconMac;
	protected BluetoothLeService mBluetoothLeService;
	protected BluetoothGattCharacteristic mReadCharacteristic, mWriteCharacteristic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
	}
	
	@Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (mBluetoothAdapter.isEnabled()) {
        	scanLeDevice(true);
        }
        
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mBeaconMac);
            System.out.println("Connect request result=" + result);
        }
    }

	@Override
	protected void onPause() {
		super.onPause();
		if (mBluetoothAdapter.isEnabled()) {
			scanLeDevice(false);
		}
		unregisterReceiver(mGattUpdateReceiver);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
//        if (device == null) return;
//        final Intent intent = new Intent(this, DeviceControlActivity.class);
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//        if (mScanning) {
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            mScanning = false;
//        }
//        startActivity(intent);
//    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
        	if (System.currentTimeMillis() - scanStartTime > SCAN_PERIOD / 2) {
	            // Stops scanning after a pre-defined scan period.
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
	                }
	            }, SCAN_PERIOD);
	
	            scanStartTime = System.currentTimeMillis();
	            mBluetoothAdapter.startLeScan(mLeScanCallback);
        	}
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device.getName().equalsIgnoreCase(SampleGattAttributes.BEACON_NAME)) {
            	mBeaconMac = device.getAddress();
            	mBluetoothAdapter.stopLeScan(mLeScanCallback);
            	Intent gattServiceIntent = new Intent(BaseActivity.this, BluetoothLeService.class);
            	if (mBluetoothLeService != null) {
        			unbindService(mServiceConnection);
        			mBluetoothLeService = null;
        		}
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
            }
        }
    };
    
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                System.out.println("Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mBeaconMac);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                updateConnectionState(R.string.disconnected);
                mReadCharacteristic = null;
                mWriteCharacteristic = null;
                connectStateChanged(false);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    protected void connectStateChanged(boolean connected) {
    }
    
    public boolean writeCharacteristic(String cmd) {
    	if (mWriteCharacteristic != null) {
	    	mWriteCharacteristic.setValue(cmd);
	        boolean res = mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
	        return res;
    	} else {
    		Toast.makeText(this, R.string.disconnected, Toast.LENGTH_SHORT).show();
    		if (mBluetoothAdapter.isEnabled()) {
            	scanLeDevice(true);
            }
    		return false;
    	}
    }
    
    private long scanStartTime;

    @Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBluetoothLeService != null) {
			unbindService(mServiceConnection);
			mBluetoothLeService = null;
		}
	}

    private void updateConnectionState(final int resourceId) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mConnectionState.setText(resourceId);TODO
//            }
//        });
    }

    private void displayData(String data) {
        //TODO
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        for (BluetoothGattService gattService : gattServices) {
        	if (gattService.getUuid().toString().equalsIgnoreCase(SampleGattAttributes.SERVICE_UUID)) {
        		for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
        			if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(SampleGattAttributes.READ_UUID)) {
        				mReadCharacteristic = gattCharacteristic;
        			} else if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(SampleGattAttributes.WRITE_UUID)) {
        				mWriteCharacteristic = gattCharacteristic;
        				connectStateChanged(true);
        			}
        		}
        		return;
        	}
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
