package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dwtedx.income.updateapp.UpdateService;

public class IncomeTypeTipSharedPreferences {
	
	private static final String INCOME_TYPE_TIP_PREFERENCES = "income_type_tip_preferences";

	private static SharedPreferences mSettingPreferences;
	private static Context mContext;

	public static void init(Context context) {
		mContext = context.getApplicationContext();
		mSettingPreferences = mContext.getSharedPreferences(INCOME_TYPE_TIP_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(INCOME_TYPE_TIP_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsTip() {
		int code = UpdateService.getAPKVersionCode(mContext);
		int cacheCode = mSettingPreferences.getInt(INCOME_TYPE_TIP_PREFERENCES, 0);
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
		editor.putInt(INCOME_TYPE_TIP_PREFERENCES, versionCode);
		// Commit changes.
		editor.commit();
	}
	
}

