package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dwtedx.income.updateapp.UpdateService;

public class HomePrivacySharedPreferences {
	
	private static final String HOME_PRIVACY_PREFERENCES = "home_privacy_preferences";

	private static SharedPreferences mSettingPreferences;
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
		mSettingPreferences = context.getSharedPreferences(HOME_PRIVACY_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(HOME_PRIVACY_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsTip() {
		int code = UpdateService.getAPKVersionCode(mContext);
		int cacheCode = mSettingPreferences.getInt(HOME_PRIVACY_PREFERENCES, 0);
		if(0 == cacheCode){
			return true;
		}
		if(code != cacheCode){
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	public static void setIsTip(int versionCode) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putInt(HOME_PRIVACY_PREFERENCES, versionCode);
		// Commit changes.
		editor.commit();
	}
	
}

