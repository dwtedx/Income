package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class LoginSharedPreferences {
	
	private static final String ACCOUNT_PREFERENCES = "login_preferences";

	private static SharedPreferences mSettingPreferences;
	private static String strValString;

	public static void init(Context context) {
		mSettingPreferences = context.getApplicationContext().getSharedPreferences(ACCOUNT_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(ACCOUNT_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static String getUserInfoStr() {
		strValString = mSettingPreferences.getString(ACCOUNT_PREFERENCES, "");
		return strValString;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setUserInfoStr(String str) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(ACCOUNT_PREFERENCES, str);
		// Commit changes.
		editor.commit();
	}
	
}
