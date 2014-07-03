package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import bluetooth.Cmd;
import com.zjlh.remotecar.R;

public class MoveBallActivity extends BaseActivity implements View.OnClickListener {
	private boolean lightOn;
	private boolean flashLightOn;
	private Button btnLight, btnFlashLight;
	private TextView txt_status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move_control);
		
		findViewById(R.id.button_moveControl_back).setOnClickListener(this);
		btnLight = (Button) findViewById(R.id.btnLight);
		btnLight.setOnClickListener(this);;
		btnFlashLight = (Button) findViewById(R.id.btnFlashLight);
		btnFlashLight.setOnClickListener(this);
		
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLight:
			if (lightOn) {
				writeCharacteristic(Cmd.LIGHT_OFF);
				btnLight.setText("开灯");
			} else {
				writeCharacteristic(Cmd.LIGHT_ON);
				btnLight.setText("关灯");
			}
			lightOn = !lightOn;
			break;
			
		case R.id.btnFlashLight:
			if (flashLightOn) {
				writeCharacteristic(Cmd.LIGHT_ON);
				btnLight.setText("关灯");
				btnFlashLight.setText("开闪光灯");
				lightOn = true;
			} else {
				btnFlashLight.setText("关闪光灯");
				writeCharacteristic(Cmd.FLASH_LIGHT_ON);
			}
			flashLightOn = !flashLightOn;
			break;
			
		case R.id.button_moveControl_back:
			finish();
			break;
		}
	}
}
