package com.dwtedx.income.provider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class BudgetSharedPreferences {
	
	private static final String BUDGET_PREFERENCES = "budget_preferences";

	private static SharedPreferences mSettingPreferences;
	private static float strValString;

	public static void init(Context context) {
		mSettingPreferences = context.getSharedPreferences(BUDGET_PREFERENCES, Activity.MODE_PRIVATE);
	}

	public static void relese() {
		mSettingPreferences = null;
	}
	
	public static void clear() {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putString(BUDGET_PREFERENCES, null);
		// Commit changes.
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static float getBudget() {
		strValString = mSettingPreferences.getFloat(BUDGET_PREFERENCES, 3000.00f);
		return strValString;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setBudget(float budget) {
		// Retrieve an editor to modify the shared preferences.
		SharedPreferences.Editor editor = mSettingPreferences.edit();
		editor.putFloat(BUDGET_PREFERENCES, budget);
		// Commit changes.
		editor.commit();
	}
	
}
