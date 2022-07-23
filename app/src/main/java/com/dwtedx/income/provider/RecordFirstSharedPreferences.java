package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class RecordFirstSharedPreferences {
	
	private static final String RECORD_FIRST_PREFERENCES = "record_first_preferences";

	private static SharedPreferences mSettingPreferences;

	public static void init(Context context) {
		mSettingPreferences = context.getApplicationContext().getSharedPreferences(RECORD_FIRST_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(RECORD_FIRST_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsFirstAdd() {
		return mSettingPreferences.getBoolean(RECORD_FIRST_PREFERENCES, true);
	}

	@SuppressLint("NewApi")
	public static void setIsFirstAdd(boolean isFirstAdd) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putBoolean(RECORD_FIRST_PREFERENCES, isFirstAdd);
		// Commit changes.
		editor.commit();
	}
	
}

