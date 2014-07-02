package activity;

import android.os.Bundle;
import android.view.View;
import com.zjlh.remotecar.R;

public class UseGsensorActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tilt_control);
		
		findViewById(R.id.imbt_tilt_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});;
	}
}
