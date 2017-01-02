package com.osori.hyson;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HySON {

	public HySON() {
	}

	/** set m_jsonArray without constructor **/	
	public static void parse(String jsonString) {
	}

	/** parse String Array **/
	public static String[] getArrayString(String jsonString) {
		JSONArray m_jsonArray = new JSONArray(jsonString);
		String[] results = new String[m_jsonArray.length()];

		for (int i = 0; i < m_jsonArray.length(); ++i) {
			results[i] = m_jsonArray.getString(i);
		}

		return results;
	}

	/** parse Boolean Array **/
	public static Boolean[] getArrayBoolean(String jsonString) {
		JSONArray m_jsonArray = new JSONArray(jsonString);
		Boolean[] results = new Boolean[m_jsonArray.length()];

		for (int i = 0; i < m_jsonArray.length(); ++i) {
			results[i] = m_jsonArray.getBoolean(i);
		}

		return results;
	}

	/** parse Integer Array **/
	public static int[] getArrayInt(String jsonString) {
		JSONArray m_jsonArray = new JSONArray(jsonString);
		int[] results = new int[m_jsonArray.length()];

		for (int i = 0; i < m_jsonArray.length(); i++) {
			results[i] = m_jsonArray.getInt(i);
		}

		return results;
	}

	/** parse Custom Object **/
	public static <T extends Object> T parse(String jsonString, Class c) {
		JSONObject json = new JSONObject(jsonString);
		T obj = null;

		try {
			// get constructor
			Constructor<?> constructor = c.getConstructor();
			// get fields
			Field[] fields = c.getFields();
			// make new object
			obj = (T) constructor.newInstance();

			Iterator<String> keys = json.keys();
			Field field;
			String key;
			
			while (keys.hasNext()) {
				key = keys.next();
				
				try {
					field = c.getField(key);
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				
				if (isJSONUnit(field.getType())) {
					// TODO : Double -> float/Floa 예외처리, Double -> String 등도 수정해야함
					if (field.getType() == float.class) {
						field.set(obj, ((Double) json.get(key)).floatValue());
					} else if (field.getType() == Float.class) {
						field.set(obj, ((Double) json.get(key)).floatValue());
					} else {
						field.set(obj, json.get(key));
						
					}
				} else if (field.getType() == java.util.Date.class) {
					field.set(obj, stringToDate(json.getString(key)));
				} else if (field.getType().isArray()) {
					field.set(obj, parseArray(json.optString(key), field.getType().getComponentType()));
				} else if (field.getType() == ArrayList.class) {
					if (field.getAnnotation(Member.class) != null) {
						Member member = field.getAnnotation(Member.class);
						field.set(obj, parseArrayList(jsonString, key, member.value()));
					} else {
						field.set(obj, new ArrayList<>());
					}
				} else {
					field.set(obj, parse(json.optString(key), field.getType()));
				}
			}

		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}

	/** parse Custom Object With Key **/
	public <T extends Object> T parse(String jsonString, String key, Class c) {
		JSONObject json = new JSONObject(jsonString);
		jsonString = json.optString(key);

		return parse(jsonString, c);
	}

	/** parse Custom Object Array **/
	public static Object parseArray(String jsonString, Class c) {
		JSONArray m_jsonArray = new JSONArray(jsonString);

		Object array = Array.newInstance(c, m_jsonArray.length());

		for (int i = 0; i < m_jsonArray.length(); i++) {
			try {
				Array.set(array, i, parse(m_jsonArray.optString(i), c));
			} catch (JSONException e) {
				Array.set(array, i, m_jsonArray.get(i));
			}
		}

		return array;
	}

	/** parse Custom Object ArrayList **/
	public static <T extends Object> ArrayList<T> parseArrayList(String jsonString, String key, Class c) {
		JSONObject json = new JSONObject(jsonString);
		jsonString = json.optString(key);

		JSONArray m_jsonArray = new JSONArray(jsonString);
		ArrayList<T> array = new ArrayList<>();

		for (int i = 0; i < m_jsonArray.length(); i++) {
			try {
				array.add(parse(m_jsonArray.optString(i), c));
			} catch (JSONException e) {
				array.add((T) m_jsonArray.get(i));
			}
		}

		return array;
	}
	
	private static boolean isJSONUnit(Class c) {
		return c.isPrimitive() || c == Float.class || c == Integer.class || c == String.class
				 || c == Double.class || c == Boolean.class || c == Character.class || c == Byte.class
				 || c == Short.class || c == Long.class;
	}
	
	private static java.util.Date stringToDate(String dateStr) {
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date(System.currentTimeMillis());
		}
	}
}
