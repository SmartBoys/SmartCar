package view;

import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.zjlh.remotecar.R;

public class TestActivity extends Activity implements OnClickListener {
	protected static final String TAG = "TestActivity";
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	private Handler mHandler = new Handler();

	// 服务uuid
	public final static UUID BEACONSERVICEUUID = UUID
	// .fromString("7D4CD077-B1BE-4A0E-018A-A7B3EA8C7A7E");
			.fromString("0000fff0-0000-1000-8000-00805f9b34fb");

	// 特征值uuid
	public final static UUID RUUID = UUID.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");
	// 特征值uuid
	public final static UUID WUUID = UUID.fromString("0000FFF2-0000-1000-8000-00805f9b34fb");

	private Button btn_f;
	private Button btn_b;
	private Button btn_l;
	private Button btn_r;
	private Button btn_s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		btn_f = (Button) findViewById(R.id.btn_f);
		btn_b = (Button) findViewById(R.id.btn_b);
		btn_l = (Button) findViewById(R.id.btn_l);
		btn_r = (Button) findViewById(R.id.btn_r);
		btn_s = (Button) findViewById(R.id.btn_s);

		btn_f.setOnClickListener(this);
		btn_b.setOnClickListener(this);
		btn_l.setOnClickListener(this);
		btn_r.setOnClickListener(this);
		btn_s.setOnClickListener(this);
		init();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_f:// 前
			break;
		case R.id.btn_b:// 后
			break;
		case R.id.btn_l:// 左
			break;
		case R.id.btn_r:// 右
			break;
		case R.id.btn_s:// 停
			break;
		}

	}

	private void scanLeDevice(final boolean enable) {
    if (enable) {
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, 10000);

        boolean res = mBluetoothAdapter.startLeScan(mLeScanCallback);
        System.out.println("res=" + res);
    } else {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
}

	private void init() {
		// mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		// 开始蓝牙搜索设备
		scanLeDevice(true);
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@SuppressLint("NewApi")
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			System.out.println("onLeScan");
			// 判断搜索到的设备是否是ZeroBeacon这个模块
			// 如果是则建立连接
			mBluetoothGatt = device.connectGatt(TestActivity.this, true, myNewGattCallback);

			BluetoothGattService mHRP = mBluetoothGatt.getService(BEACONSERVICEUUID);
			BluetoothGattCharacteristic mHRCPcharac2 = mHRP.getCharacteristic(WUUID);
			// int value = (byte) 0x01;
			mHRCPcharac2.setValue("k");
			// mHRCPcharac2.setValue(value,
			// BluetoothGattCharacteristic.FORMAT_UINT8, 0);
			mBluetoothGatt.writeCharacteristic(mHRCPcharac2);
			// BluetoothGattCharacteristic mHRMcharac =
			// mHRP.getCharacteristic(RUUID);
			// mBluetoothGatt.setCharacteristicNotification(mHRMcharac, true);

			// 如果重其他地方得到这个传入过来的device
			// 可以直接进行连接 mBluetoothGatt = device.connectGatt(MainActivity.this,
			// false,myNewGattCallback);

		}
	};
	private BluetoothGattCallback myNewGattCallback = new BluetoothGattCallback() {

		// 连接状态发生改变时的回掉

		// @Override
		// public void onConnectionStateChange(BluetoothGatt gatt, int status,
		// int newState) {
		// System.out.println("Bluetooth status: " + status);
		// Toast.makeText(getApplicationContext(), "Bluetooth status: " +
		// status, Toast.LENGTH_SHORT).show();
		// super.onConnectionStateChange(gatt, status, newState);
		// Log.d(TAG, "Bluetooth status: " + status);
		// //连接状态为已连接
		// if (newState == BluetoothProfile.STATE_CONNECTED) {
		//
		// //查找服务 mBluetoothGatt.discoverServices()
		// Log.i(TAG, "Connected to GATT server.");
		// /*Log.i(TAG, "Attempting to start service discovery:"
		// + mBluetoothGatt.discoverServices());*/
		// } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
		// Log.i(TAG, "Disconnected from GATT server.");
		// }
		// }

		// 当服务被发现时的回掉
		// @Override
		// public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		// System.out.println("dis status = " + status);
		// for (BluetoothGattService service : gatt.getServices()) {
		// System.out.println("uuid=" + service.getUuid());
		// //是指定的beacon中供读写的服务
		// if (service.getUuid().equals(BEACONSERVICEUUID)) {
		// System.out.println("kdfjasldkfjfjjjjjjjjjjjjjj");
		//
		// //供读写的特征值
		//
		// BluetoothGattCharacteristic characteristic = service
		// .getCharacteristic(RUUID);
		// BluetoothGattCharacteristic characterist= service
		// .getCharacteristic(WUUID);
		//
		// //读取特征值
		// //如果成功执行onCharacteristicRead回掉
		// boolean readCharacteristic = gatt.readCharacteristic(characteristic);
		//
		// //写入值的话是
		// gatt.writeCharacteristic(characterist);
		//
		// }
		// }
		// };

		// @Override
		// public void onCharacteristicRead(BluetoothGatt gatt,
		// BluetoothGattCharacteristic characteristic, int status) {
		//
		// //获取特征值的值
		// characteristic.getValue();
		// // characteristic.getStringValue(0);
		// // characteristic.getIntValue(formatType, offset);
		// // characteristic.getFloatValue(formatType, offset);
		// }
		//
		// @Override
		// public void onCharacteristicWrite(BluetoothGatt gatt,
		// BluetoothGattCharacteristic characteristic, int status) {
		// characteristic.setValue("m");
		// super.onCharacteristicWrite(gatt, characteristic, status);
		//
		//
		// }
		//
		// @Override
		// public void onDescriptorWrite(BluetoothGatt gatt,
		// BluetoothGattDescriptor descriptor, int status) {
		// // TODO Auto-generated method stub
		// super.onDescriptorWrite(gatt, descriptor, status);
		// }
		//
		// @Override
		// public void onReliableWriteCompleted(BluetoothGatt gatt, int status)
		// {
		// // TODO Auto-generated method stub
		// super.onReliableWriteCompleted(gatt, status);
		// };
		//

	};

}
