package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import bluetooth.Cmd;
import com.zjlh.remotecar.R;
import control.TiltDetect;

public class UseGsensorActivity extends BaseActivity {
	private TiltDetect tilt;
	private ImageView img_wheel;
	
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
