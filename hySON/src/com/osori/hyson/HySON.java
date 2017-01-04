package com.osori.hyson;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HySON {
	private static final HashMap<Class, Class> primToWrap = new HashMap<Class, Class>() { 
		{
			put(byte.class, Byte.class);
			put(short.class, Short.class);
			put(int.class, Integer.class);
			put(float.class, Float.class);
			put(long.class, Long.class);
			put(double.class, Double.class);
			put(boolean.class, Boolean.class);
			put(char.class, Character.class);
		}
	};

	/** parse Custom Object **/
	public static <T extends Object> T parse(String jsonString, Class c) {
		JSONObject json = null;

		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}

		return parse(json, c);
	}

	private static <T extends Object> T parse(JSONObject json, Class c) {
		T obj = null;

		try {
			// get constructor
			Constructor<?> constructor = c.getConstructor();

			// make new object
			obj = (T) constructor.newInstance();

		} catch (NoSuchMethodException | SecurityException | 
				InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}

		Iterator<String> keys = json.keys();
		Field field;
		String key;

		while (keys.hasNext()) {
			key = keys.next();

			try {
				field = c.getField(key);

				if (isJSONUnit(field.getType())) {
					field.set(obj, stringToObject(field.getType(), json.optString(key)));
				} else if (field.getType() == java.util.Date.class) {
					field.set(obj, stringToDate(json.getString(key)));
				} else if (field.getType().isArray()) {
					field.set(obj, parseArray(json.optString(key), field.getType().getComponentType()));
				} else if (field.getType() == ArrayList.class) {
					if (field.getAnnotation(Member.class) != null) {
						Member member = field.getAnnotation(Member.class);
						field.set(obj, parseArrayList(json.optString(key), member.value()));
					} else {
						field.set(obj, new ArrayList<>());
					}
				} else {
					field.set(obj, parse(json.optString(key), field.getType()));
				}
				
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println("There is no field named " + key);
				
				continue;
			} catch (IllegalArgumentException | IllegalAccessException | JSONException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println("Parsing error at field named " + key);
				
				continue;
			}
		}

		return obj;
	}

	/** parse Custom Object With Key **/
	public <T> T parse(String jsonString, String key, Class c) {
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			return null;
		}
		
		return parse(json, key, c);
	}

	private <T> T parse(JSONObject json, String key, Class c) {
		return parse(json.getJSONObject(key), c);
	}

	/** parse Custom Object Array **/
	public static Object parseArray(String jsonString, Class c) {
		JSONArray jsonArray = null;

		try {
			jsonArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			return null;
		}

		Object array = Array.newInstance(c, jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				Array.set(array, i, parse(jsonArray.getJSONObject(i), c));
			} catch (JSONException e) {
				Array.set(array, i, jsonArray.get(i));
			}
		}

		return array;
	}

	/** parse Custom Object ArrayList **/
	public static <T> ArrayList<T> parseArrayList(String jsonString, Class c) {
		JSONArray jsonArray = null;
		
		try {
			jsonArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			return null;
		}
		
		ArrayList<T> array = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				array.add(parse(jsonArray.getJSONObject(i), c));
			} catch (JSONException e) {
				array.add((T) jsonArray.get(i));
			}
		}

		return array;
	}

	private static boolean isJSONUnit(Class c) {
		return c.isPrimitive() || c == Float.class || c == Integer.class || c == String.class || c == Double.class
				|| c == Boolean.class || c == Character.class || c == Byte.class || c == Short.class || c == Long.class;
	}
	
	private static Object stringToObject(Class fieldType, String value) {
		if (fieldType.equals(String.class)) {
			return value;
		}
		
		Class c = fieldType;
		Object result = null;
		
		if (fieldType.isPrimitive()) {
			c = primToWrap.get(fieldType);
		}
		
		try {
			Class[] methodParamClass = new Class[] {String.class};
			Method parser = c.getMethod("parse" + (c.equals(Integer.class) ? "Int" : c.getSimpleName()), methodParamClass);
			
			Object[] methodParamObject = new Object[] {value};
			result = parser.invoke(c, methodParamObject);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	private static java.util.Date stringToDate(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date(System.currentTimeMillis());
		}
	}
}
