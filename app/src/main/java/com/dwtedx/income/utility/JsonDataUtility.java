package com.dwtedx.income.utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonDataUtility {
    private final static String TAG = "TP_JsonData";
	public static JSONArray getJSONArray(JSONObject object, String query) {
		JSONArray result = null;
		if (object != null) {
			try {
				result = object.getJSONArray(query);
			} catch (JSONException e) {
				Log.w(TAG, "getJSONArray(" + query + "), " + e.toString());
			}			
		}
		return result;
	}
	public static JSONObject getJSONObject(JSONObject object, String query) {
		JSONObject result = null;
		if (object != null) {
			try {
				result = object.getJSONObject(query);
			} catch (JSONException e) {
				Log.w(TAG, "getJSONObject(" + query + "), " + e.toString());
			}			
		}
		return result;
	}
	public static long getLong(JSONObject object, String query, long defaultValue) {
		long result = defaultValue;
		if (object != null) {
			try {
				result = object.getLong(query);
			} catch (JSONException e) {
				Log.w(TAG, "getLong(" + query + "), " + e.toString());
			}			
		}
		return result;
	}
	
	public static String getString(JSONObject object, String query, String defaultValue) {
		String result = defaultValue;
		if (object != null) {
			try {
				result = object.getString(query);
			} catch (JSONException e) {
				Log.w(TAG, "getString(" + query + "), " + e.toString());
			}
			if (result != null && result.length() == 0) result = defaultValue;
			if (result != null && result.equals("null")) result = defaultValue;
		}
		return result;
	}
	
	public static int getInt(JSONObject object, String query, int defaultValue) {
		int  result = defaultValue;
		if (object != null) {
			try {			
				result = object.getInt(query);
			} catch (JSONException e) {
				Log.w(TAG, "getInt(" + query + "), " + e.toString());
			}		
		}
		return result;
	}
	
	public static boolean getBoolean(JSONObject object, String query, boolean defaultValue) {
		boolean  result = defaultValue;
		if (object != null) {
			try {
				result = object.getBoolean(query);
			} catch (JSONException e) {
				Log.w(TAG, "getBoolean(" + query + "), " + e.toString());
			}			
		}
		return result;
	}
	
	public static double getDouble(JSONObject object, String query, double defaultValue) {
		double  result = defaultValue;
		if (object != null) {
			try {
				result = object.getDouble(query);
			} catch (JSONException e) {
				Log.w(TAG, "getDouble(" + query + "), " + e.toString());
			}			
		}
		return result;
	}
}


