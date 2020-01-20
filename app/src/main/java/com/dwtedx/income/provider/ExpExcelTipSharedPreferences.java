package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ExpExcelTipSharedPreferences {
	
	private static final String EXP_EXCEL_TIP_PREFERENCES = "exp_excel_tip_preferences";

	private static SharedPreferences mSettingPreferences;
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
		mSettingPreferences = context.getSharedPreferences(EXP_EXCEL_TIP_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(EXP_EXCEL_TIP_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean getIsTip() {
		boolean cache = mSettingPreferences.getBoolean(EXP_EXCEL_TIP_PREFERENCES, false);
		return cache;
	}

	@SuppressLint("NewApi")
	public static void setIsTip(boolean isTip) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putBoolean(EXP_EXCEL_TIP_PREFERENCES, isTip);
		// Commit changes.
		editor.commit();
	}
	
}

