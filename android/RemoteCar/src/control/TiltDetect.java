package control;

import bluetooth.Cmd;
import activity.BaseActivity;
import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.ImageView;

public class TiltDetect implements SensorEventListener {
	private long lastUpdate;
	private Sensor mAccelerometer;
	private SensorManager mSensorManager;
	private String currCmd;
	private BaseActivity mBaseActivity;
	private ImageView img_wheel;
	private Matrix mMatrix = new Matrix();

	public TiltDetect(Context context, ImageView img_wheel) {
		mSensorManager = ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE));
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.img_wheel = img_wheel;
		mBaseActivity = (BaseActivity) context;
	}
	
	private void actionCmd(String cmd) {
		if (!cmd.equals(currCmd)) {
			if (cmd.equals(Cmd.STOP) && currCmd == null) {
				return;
			}
			mBaseActivity.writeCharacteristic(cmd);
			currCmd = cmd;
			
			if (cmd.equals(Cmd.FORWARD_LEFT)) {
				mMatrix.setRotate(-45, img_wheel.getWidth()/2, img_wheel.getHeight()/2);
			} else if (cmd.equals(Cmd.FORWARD_RIGHT)) {
				mMatrix.setRotate(45, img_wheel.getWidth()/2, img_wheel.getHeight()/2);
			} else {
				mMatrix.setRotate(0, img_wheel.getWidth()/2, img_wheel.getHeight()/2);
			}
			img_wheel.setImageMatrix(mMatrix);
		}
	}
	
	public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
	}

	public void onSensorChanged(SensorEvent paramSensorEvent) {
		long currTime = System.currentTimeMillis();
		float x = paramSensorEvent.values[0];
		float y = paramSensorEvent.values[1];
		if (currTime - lastUpdate > 200) {
			lastUpdate = currTime;
			if (x <= 2) {
				actionCmd(Cmd.FORWARD);
			} else if (x >= 8){
				actionCmd(Cmd.BACK);
			} else {
				if (y <= -4) {
					actionCmd(Cmd.FORWARD_LEFT);
				} else if (y >= 4) {
					actionCmd(Cmd.FORWARD_RIGHT);
				} else {
					actionCmd(Cmd.STOP);
				}
			}
		}
	}

	public boolean start() {
		if (mAccelerometer != null) {
			Log.d("TiltDetect", "mAccelerometer not null");
			return mSensorManager.registerListener(this, mAccelerometer, 3);
		}
		return false;
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	}
}
