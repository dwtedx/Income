package com.dwtedx.income.utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
 * @Company 路之遥网络科技有限公司
 * @Description Json处理工具
 */
public class ParseJsonToObject {
    private final static String TAG = "ParseJsonToObject";
    private final static String JAVA_BASE_TYPE_STR = String.class.getName() + char.class.getName()
            + Integer.class.getName() + int.class.getName()
            + Short.class.getName() + short.class.getName()
            + Byte.class.getName() + byte.class.getName()
            + Long.class.getName() + long.class.getName()
            + Boolean.class.getName() + boolean.class.getName()
            + Float.class.getName() + float.class.getName()
            + Double.class.getName() + double.class.getName();

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 通过key把JSONArray转为List
     * @param cls
     * @param listKeyName
     * @param jsonObject
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjectList(Class<T> cls, String listKeyName, Object jsonObject){
    	List<T> returnObj = new ArrayList<T>();
    	
    	if (null != jsonObject && jsonObject instanceof JSONObject) {
    		JSONArray jsonArray = null;
			try {
				jsonArray = ((JSONObject)jsonObject).getJSONArray(listKeyName);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
    		
			returnObj = getObjectList(cls, jsonArray);
    	}
    	
    	return returnObj;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 把JSONArray转为List
     * @param cls
     * @param jsonObject
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjectList(Class<T> cls, Object jsonObject){
        List<T> returnObj = new ArrayList<T>();
        
        if (null != jsonObject && jsonObject instanceof JSONArray) {
            Object jsonArrayItemObj = null;
            T listItemObj = null;
            JSONArray jsonArray = (JSONArray)jsonObject;
            
            try {
                for(int i=0; i<jsonArray.length(); i++) {
                    jsonArrayItemObj = jsonArray.get(i);
                    if(jsonArrayItemObj instanceof JSONObject) {
                        listItemObj = getObject(cls, jsonArray.getJSONObject(i));
                    } else {
                        listItemObj = typeConvert(cls, jsonArrayItemObj.toString());
                    }
                    
                    if (null != listItemObj) {
                        returnObj.add(listItemObj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            // unknown error
        }
        
        return returnObj;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 把JSONObject转为对应的Obj
     * @param cls
     * @param jsonObject
     * @param <T>
     * @return
     */
    public static <T> T getObject(Class<T> cls, JSONObject jsonObject){
        T returnObj = null;
        
        if (null != jsonObject) {
	        try {
	            returnObj=cls.newInstance();
	            
	            Object subObject;
	            JSONObject subJsonObject=null;
	            String key;
	            String fieldName;
	            
	            Iterator<?> keys =jsonObject.keys();
	            while(keys.hasNext()){  
	                key=(String) keys.next();
	                subObject=jsonObject.get(key);
	                fieldName = key;
	                
	                // sub object
	                if(subObject instanceof JSONObject){
	                    subJsonObject=(JSONObject)subObject;
	                    
	                    Field field;
	                    try {
	                        field = cls.getDeclaredField(fieldName);
	                        field.setAccessible(true);
	                        field.set(returnObj, getObject(field.getType(), subJsonObject));
	                    } catch (NoSuchFieldException e) {
	                        // do nothing
	                    }
	                    
	                } else if (subObject instanceof JSONArray) { // Array object
	                    JSONArray subJsonArray=(JSONArray)subObject;
	                    Field field;
	                    
	                    try {
	                        field = cls.getDeclaredField(fieldName);
	                        field.setAccessible(true);
	                        
	                        Class<?> arrayCls = null;
	                        Type fieldType = field.getGenericType();
	                        if(fieldType instanceof ParameterizedType) {
		                       ParameterizedType paramType = (ParameterizedType) fieldType;
		                       arrayCls = (Class<?>)paramType.getActualTypeArguments()[0];
		                    }
	
	                        Object arrayList = getObjectList(arrayCls, subJsonArray);
	                        
	                        if (null != arrayList) {
	                        	field.set(returnObj, arrayList);
	                        }
	                    } catch (NoSuchFieldException e) {
	                        // do nothing
	                    }
	                    
	                }else { // member
	                    Field field = getClassField(cls, fieldName);
	                    if (null != field) {
	                        field.set(returnObj, typeConvert(field.getType(), subObject.toString()));
	                    } else {
	                        Log.i(TAG, "Filed not found:" + fieldName);
	                    }
	                }
	            }
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }  catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
        }
        
        return returnObj;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 把Obj转为JSONObject
     * @param object
     * @param <T>
     * @return
     */
    public static <T> JSONObject getJsonFromObj(T object){
    	JSONObject jsonObj= new JSONObject();
    	
    	if (null != object) {
        	Field[] field = object.getClass().getDeclaredFields();
        	String fieldName;
        	Class<?> fieldType;
        	Object value;
        	
        	for(int j=0 ; j<field.length ; j++){     //遍历所有属性
                if(field[j].isSynthetic()){
                    continue;
                }
        		field[j].setAccessible(true);
                fieldName = field[j].getName();    //获取属性的名字   
                fieldType = field[j].getType(); //获取属性的类型 ，不包含泛型参数
                try {
    				value = field[j].get(object);
    				if (fieldType.getName().equals("java.util.List")) { // is array list				    
    	            	//jsonObj.put(fieldName.substring(1), getJsonFromObjList((List<?>)value));
    	            	jsonObj.put(fieldName, getJsonFromObjList((List<?>)value));
    	            } else if (!isBaseType(fieldType.getName())){ // is object
    	            	//jsonObj.put(fieldName.substring(1), getJsonFromObj(value));
    	            	jsonObj.put(fieldName, getJsonFromObj(value));
    	            } else { // is base type
    	            	//jsonObj.put(fieldName.substring(1), value);
    	            	jsonObj.put(fieldName, value);
    	            }
    			} catch (IllegalAccessException e) {
    				e.printStackTrace();
    			} catch (IllegalArgumentException e) {
    				e.printStackTrace();
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
        	}
    	}
    	
    	return jsonObj;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 把List转为JSONArray
     * @param objList
     * @return
     */
    public static JSONArray getJsonFromObjList(List<?> objList){
    	JSONArray jsonObjList= new JSONArray();
    	
    	if (null != objList && objList.size()>0) {
        	if (isBaseType(objList.get(0).getClass().getName())) {
                for(int i=0 ; i<objList.size() ; i++){     //遍历所有属性
                    jsonObjList.put(objList.get(i));
                }
            } else {
                for(int i=0 ; i<objList.size() ; i++){     //遍历所有属性
                    jsonObjList.put(getJsonFromObj(objList.get(i)));
                }
            }
    	}
    	
    	return jsonObjList;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 把数组转为JSONArray
     * @param objList
     * @param <T>
     * @return
     */
    public static <T> JSONArray getJsonFromObjList(T[] objList){
        JSONArray jsonObjList= new JSONArray();
        
        if (null != objList && objList.length>0) {
            if (isBaseType(objList[0].getClass().getName())) {
                for(int i=0 ; i<objList.length ; i++){     //遍历所有属性
                    jsonObjList.put(objList[i]);
                }
            } else {
                for(int i=0 ; i<objList.length ; i++){     //遍历所有属性
                    jsonObjList.put(getJsonFromObj(objList[i]));
                }
            }
        }
        
        return jsonObjList;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 把List转为JSONArray(添加key)
     * @param objList
     * @param key
     * @return
     */
    public static JSONObject getJsonFromObjListWithKey(List<?> objList, String key){
    	JSONArray jsonObjList= getJsonFromObjList(objList);
    	
    	JSONObject jsonObj = new JSONObject();
    	try {
			jsonObj.put(key, jsonObjList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return jsonObj;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 此方法用于将一个字符串转换为相应的数据类型
     * @param cls
     * @param value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T typeConvert(Class<T> cls, String value){
        String className = cls.getName();
        Object retObj = null;
        
        if(className.equals("java.lang.String")){
            retObj = (value.equals("null"))? "" :value;
        } else if(className.equals(Integer.class.getName()) || className.equals(int.class.getName())){
            retObj = (value.equals("null"))? 0 : Integer.valueOf(value);
        } else if(className.equals(Short.class.getName()) || className.equals(short.class.getName())){
            retObj = (value.equals("null"))? 0 : Short.valueOf(value);
        } else if(className.equals(Byte.class.getName()) || className.equals(byte.class.getName())){
            retObj = (value.equals("null"))? 0 : Byte.valueOf(value);
        } else if(className.equals(Long.class.getName()) || className.equals(long.class.getName())){
            retObj = (value.equals("null"))? 0 : Long.valueOf(value);
        } else if(className.equals(Boolean.class.getName()) || className.equals(boolean.class.getName())){
            retObj = (value.equals("null"))? false : Boolean.valueOf(value);
        } else if(className.equals(Float.class.getName()) || className.equals(float.class.getName())){
            retObj = (value.equals("null"))? 0 : Float.valueOf(value);
        } else if(className.equals(char.class.getName())){
            retObj = (value.equals("null"))? ' ' :value.charAt(0);
        }else if(className.equals(Double.class.getName()) || className.equals(double.class.getName())){
            retObj = (value.equals("null"))? 0 : Double.valueOf(value);
        }else return null;
        
        return (T)retObj;
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 是否为基本数据类型
     * @param className
     * @return
     */
    public static boolean isBaseType(String className) {
        if (JAVA_BASE_TYPE_STR.indexOf(className) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 下午5:03.
     * @Description 反射类的 Field
     * @param clazz
     * @param fieldName
     * @return
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        Field field = null;
        
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        
        if (null != field) {
            field.setAccessible(true);
        } else {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {// 递归查询
                return getClassField(superclass, fieldName);  
            }
        }
        
        return field;  
    } 

}
