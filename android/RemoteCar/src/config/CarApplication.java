package config;

import android.app.Application;

public class CarApplication extends Application {
	private static CarApplication mInstance;
	
	public static CarApplication getApplication() {
		return mInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
}
