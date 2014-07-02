package activity;

import preference.PreferenceSettings;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.zjlh.remotecar.R;

public class MainActivity extends Activity implements View.OnClickListener {
	private Button imgbt_mode, imgbt_navi, imgbt_help, imgbt_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imgbt_mode = (Button) findViewById(R.id.imgbt_mode);
		imgbt_navi = (Button) findViewById(R.id.imgbt_navi);
		imgbt_help = (Button) findViewById(R.id.imgbt_help);
		imgbt_start = (Button) findViewById(R.id.imgbt_start);
		
		imgbt_mode.setOnClickListener(this);
		imgbt_navi.setOnClickListener(this);
		imgbt_help.setOnClickListener(this);
		imgbt_start.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbt_mode:
			Intent modeIntent = new Intent(MainActivity.this, PagerSelectorActivity.class);
			modeIntent.putExtra(PagerSelectorActivity.TYPE, PagerSelectorActivity.ViewPagerAdapter.TYPE_MODE);
			startActivity(modeIntent);
			break;
			
		case R.id.imgbt_navi:
			Intent navaIntent = new Intent(MainActivity.this, PagerSelectorActivity.class);
			navaIntent.putExtra(PagerSelectorActivity.TYPE, PagerSelectorActivity.ViewPagerAdapter.TYPE_NAVIGATION);
			startActivity(navaIntent);
			break;
			
		case R.id.imgbt_help:
			startActivity(new Intent(MainActivity.this, HelpActivity.class));
			break;
			
		case R.id.imgbt_start:
			int mode = PreferenceSettings.getMode();
			if (mode == PreferenceSettings.MODE_MOVE_BALL) {
				startActivity(new Intent(MainActivity.this, MoveBallActivity.class));
			} else if (mode == PreferenceSettings.MODE_USE_ARROWS) {
				startActivity(new Intent(MainActivity.this, UseArrowsActivity.class));
			} else if (mode == PreferenceSettings.MODE_USE_GSENSOR) {
				startActivity(new Intent(MainActivity.this, UseGsensorActivity.class));
			}
			break;
		}
	}

}
