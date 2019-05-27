package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class FingerprintSetupSharedPreferences {
	
	private static final String FINGERPRINT_SETUP_PREFERENCES = "fingerprint_setup_preferences";

	private static SharedPreferences mSettingPreferences;
	private static boolean strValString;

	public static void init(Context context) {
		mSettingPreferences = context.getSharedPreferences(FINGERPRINT_SETUP_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(FINGERPRINT_SETUP_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getFingerprintSetup() {
		strValString = mSettingPreferences.getBoolean(FINGERPRINT_SETUP_PREFERENCES, false);
		return strValString;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setFingerprintSetup(boolean valString) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putBoolean(FINGERPRINT_SETUP_PREFERENCES, valString);
		// Commit changes.
		editor.commit();
	}
	
}
