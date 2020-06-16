package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class ScanSetupSharedPreferences {
	
	private static final String SCAN_SETUP_PREFERENCES = "scan_setup_preferences";

	private static SharedPreferences mSettingPreferences;
	private static boolean strValString;

	public static void init(Context context) {
		mSettingPreferences = context.getSharedPreferences(SCAN_SETUP_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(SCAN_SETUP_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getScanSetup() {
		strValString = mSettingPreferences.getBoolean(SCAN_SETUP_PREFERENCES, true);
		return strValString;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setScanSetup(boolean valString) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putBoolean(SCAN_SETUP_PREFERENCES, valString);
		// Commit changes.
		editor.commit();
	}
	
}
