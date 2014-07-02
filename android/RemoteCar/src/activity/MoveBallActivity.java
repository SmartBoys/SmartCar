package activity;

import android.os.Bundle;
import android.view.View;
import com.zjlh.remotecar.R;

public class MoveBallActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move_control);
		
		findViewById(R.id.button_moveControl_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
