package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import bluetooth.Cmd;
import com.zjlh.remotecar.R;

public class MoveBallActivity extends BaseActivity implements View.OnClickListener {
	private boolean lightOn;
	private boolean flashLightOn;
	private Button btnLight, btnFlashLight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move_control);
		
		findViewById(R.id.button_moveControl_back).setOnClickListener(this);
		btnLight = (Button) findViewById(R.id.btnLight);
		btnLight.setOnClickListener(this);;
		btnFlashLight = (Button) findViewById(R.id.btnFlashLight);
		btnFlashLight.setOnClickListener(this);;
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
