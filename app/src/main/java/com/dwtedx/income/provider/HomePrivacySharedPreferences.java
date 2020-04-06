package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dwtedx.income.updateapp.UpdateService;

public class HomePrivacySharedPreferences {
	
	private static final String HOME_PRIVACY_PREFERENCES = "home_privacy_bool_preferences";

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
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(HOME_PRIVACY_PREFERENCES, null);
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsTip() {
		boolean isTip = mSettingPreferences.getBoolean(HOME_PRIVACY_PREFERENCES, true);
		return isTip;
	}

	@SuppressLint("NewApi")
	public static void setIsTip(boolean isTip) {
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putBoolean(HOME_PRIVACY_PREFERENCES, isTip);
		editor.commit();
	}
	
}

