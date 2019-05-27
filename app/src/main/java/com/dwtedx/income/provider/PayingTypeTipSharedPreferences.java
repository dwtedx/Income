package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dwtedx.income.updateapp.UpdateService;

public class PayingTypeTipSharedPreferences {
	
	private static final String PAYING_TYPE_TIP_PREFERENCES = "paying_type_tip_preferences";

	private static SharedPreferences mSettingPreferences;
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
		mSettingPreferences = context.getSharedPreferences(PAYING_TYPE_TIP_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(PAYING_TYPE_TIP_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsTip() {
		int code = UpdateService.getAPKVersionCode(mContext);
		int cacheCode = mSettingPreferences.getInt(PAYING_TYPE_TIP_PREFERENCES, 0);
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
		editor.putInt(PAYING_TYPE_TIP_PREFERENCES, versionCode);
		// Commit changes.
		editor.commit();
	}
	
}

