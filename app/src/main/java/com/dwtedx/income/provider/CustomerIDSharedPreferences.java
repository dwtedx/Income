package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class CustomerIDSharedPreferences {
	
	private static final String CUSTOMER_ID_PREFERENCES = "customer_id_preferences";

	private static SharedPreferences mSettingPreferences;
	private static int strValString;

	public static void init(Context context) {
		mSettingPreferences = context.getSharedPreferences(CUSTOMER_ID_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(CUSTOMER_ID_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static int getCustomerId() {
		strValString = mSettingPreferences.getInt(CUSTOMER_ID_PREFERENCES, -1);
		return strValString;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setCustomerId(int id) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putInt(CUSTOMER_ID_PREFERENCES, id);
		// Commit changes.
		editor.commit();
	}
	
}
