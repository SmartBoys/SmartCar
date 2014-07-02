package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.zjlh.remotecar.R;

public class HelpActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		findViewById(R.id.imgbt_help_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
