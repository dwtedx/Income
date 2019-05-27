package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class AccountSharedPreferences {
	
	private static final String ACCOUNT_PREFERENCES = "account_preferences";

	private static SharedPreferences mSettingPreferences;
	private static String strValString;

	public static void init(Context context) {
		mSettingPreferences = context.getSharedPreferences(ACCOUNT_PREFERENCES, Activity.MODE_PRIVATE);
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
	public static String getAccountStr() {
		strValString = mSettingPreferences.getString(ACCOUNT_PREFERENCES, "1|现金");
		return strValString;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setAccountSt(String str) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(ACCOUNT_PREFERENCES, str);
		// Commit changes.
		editor.commit();
	}
	
}
