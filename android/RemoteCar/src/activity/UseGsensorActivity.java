package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import bluetooth.Cmd;
import com.zjlh.remotecar.R;
import control.TiltDetect;

public class UseGsensorActivity extends BaseActivity {
	private TiltDetect tilt;
	private ImageView img_wheel;
	private TextView txt_status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tilt_control);
		
		findViewById(R.id.imbt_tilt_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		img_wheel = (ImageView) findViewById(R.id.img_wheel);
		tilt = new TiltDetect(this, img_wheel);
		txt_status = (TextView) findViewById(R.id.txt_status);
		txt_status.setText("未连接");
	}
	
	@Override
	protected void connectStateChanged(boolean connected) {
		if (connected) {
			txt_status.setText("已连接");
		} else {
			txt_status.setText("未连接");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!tilt.start()) {
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		if (mWriteCharacteristic != null) {
			writeCharacteristic(Cmd.STOP);
		}
		super.onPause();
		tilt.stop();
	}
}
