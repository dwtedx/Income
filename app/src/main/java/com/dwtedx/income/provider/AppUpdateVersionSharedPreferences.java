package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dwtedx.income.updateapp.UpdateService;

public class AppUpdateVersionSharedPreferences {
	
	private static final String APP_UPDATE_VERSION_PREFERENCES = "app_update_version_preferences";
	private static Context mContext;

	private static SharedPreferences mSettingPreferences;

	public static void init(Context context) {
		mContext = context.getApplicationContext();
		mSettingPreferences = mContext.getSharedPreferences(APP_UPDATE_VERSION_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(APP_UPDATE_VERSION_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsFirstOpen() {
		int code = UpdateService.getAPKVersionCode(mContext);
		int cacheCode = mSettingPreferences.getInt(APP_UPDATE_VERSION_PREFERENCES, 0);
		if(0 == cacheCode){
			setIsFirstOpen(code);
			return true;
		}
		if(code != cacheCode){
			setIsFirstOpen(code);
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	private static void setIsFirstOpen(int versionCode) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putInt(APP_UPDATE_VERSION_PREFERENCES, versionCode);
		// Commit changes.
		editor.commit();
	}
	
}

