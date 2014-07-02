package preference;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import config.CarApplication;

public class PreferenceSettings {
	private static final String PREF_FILE_NAME = "common_settings";
	private static final String MODE = "mode";
	
	public static final int MODE_MOVE_BALL = 1;
	public static final int MODE_USE_ARROWS = 2;
	public static final int MODE_USE_GSENSOR = 3;
	
	public static int getMode() {
		SharedPreferences sp = CarApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME,PreferenceActivity.MODE_PRIVATE);
		return sp.getInt(MODE, MODE_MOVE_BALL);
	}
	
	public static void setMode(int mode) {
		SharedPreferences sp = CarApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME,PreferenceActivity.MODE_PRIVATE);
		sp.edit().putInt(MODE, mode).commit();
	}
}
