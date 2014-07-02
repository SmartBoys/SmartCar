package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import bluetooth.Cmd;
import com.zjlh.remotecar.R;

public class UseArrowsActivity extends BaseActivity implements View.OnClickListener {
	private Button imbt_touch_back, btnStop;
	private ImageButton imgbt_forward, imgbt_backward, imgbt_left, imgbt_right;
	private TextView txt_status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_control);
		
		imbt_touch_back = (Button) findViewById(R.id.imbt_touch_back);
		btnStop = (Button) findViewById(R.id.btnStop);
		imgbt_forward = (ImageButton) findViewById(R.id.imgbt_forward);
		imgbt_backward = (ImageButton) findViewById(R.id.imgbt_backward);
		imgbt_left = (ImageButton) findViewById(R.id.imgbt_left);
		imgbt_right = (ImageButton) findViewById(R.id.imgbt_right);
		
		imbt_touch_back.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		imgbt_forward.setOnClickListener(this);
		imgbt_backward.setOnClickListener(this);
		imgbt_left.setOnClickListener(this);
		imgbt_right.setOnClickListener(this);
		
		txt_status = (TextView) findViewById(R.id.txt_status);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imbt_touch_back:
			finish();
			break;
			
		case R.id.btnStop:
			writeCharacteristic(Cmd.STOP);
			break;
			
		case R.id.imgbt_forward:
			writeCharacteristic(Cmd.FORWARD);
			break;
			
		case R.id.imgbt_backward:
			writeCharacteristic(Cmd.BACK);
			break;
			
		case R.id.imgbt_left:
			writeCharacteristic(Cmd.LEFT);
			break;
			
		case R.id.imgbt_right:
			writeCharacteristic(Cmd.RIGHT);
			break;
		}
	}
}
