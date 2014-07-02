package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.zjlh.remotecar.R;

public class NavigationActivity extends BaseActivity implements View.OnClickListener {
	public static final int TYPE_N = 1;
	public static final int TYPE_FB = 2;
	public static final int TYPE_RECT = 3;
	public static final String TYPE = "type";
	private Button button_navi_back, button_navi_start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navi);
		int type = getIntent().getIntExtra(TYPE, TYPE_N);
		if (type == TYPE_N) {
			findViewById(android.R.id.content).setBackgroundResource(R.drawable.n);
		} else if (type == TYPE_FB) {
			findViewById(android.R.id.content).setBackgroundResource(R.drawable.fb);
		} else if (type == TYPE_RECT) {
			findViewById(android.R.id.content).setBackgroundResource(R.drawable.rect);
		}
		
		button_navi_back = (Button) findViewById(R.id.button_navi_back);
		button_navi_start = (Button) findViewById(R.id.button_navi_start);
		button_navi_back.setOnClickListener(this);
		button_navi_start.setOnClickListener(this);
		button_navi_start.setText(R.string.start);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_navi_back:
			finish();
			break;
			
		case R.id.button_navi_start:
			
			break;
		}
	}
}
