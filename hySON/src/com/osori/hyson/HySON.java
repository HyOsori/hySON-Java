package com.osori.hyson;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HySON {
	private JSONArray m_jsonArray = null;

	public HySON() {
	}

	/** set m_jsonArray without constructor **/
	public void parse(String jsonString) {
		m_jsonArray = new JSONArray(jsonString);
	}

	/** parse String Array **/
	public String[] getArrayString(String jsonString) {
		parse(jsonString);
		String[] results = new String[m_jsonArray.length()];

		for (int i = 0; i < m_jsonArray.length(); ++i) {
			results[i] = m_jsonArray.getString(i);
		}

		return results;
	}

	/** parse Boolean Array **/
	public Boolean[] getArrayBoolean(String jsonString) {
		parse(jsonString);
		Boolean[] results = new Boolean[m_jsonArray.length()];

		for (int i = 0; i < m_jsonArray.length(); ++i) {
			results[i] = m_jsonArray.getBoolean(i);
		}

		return results;
	}

	/** parse Integer Array **/
	public int[] getArrayInt(String jsonString) {
		parse(jsonString);
		int[] results = new int[m_jsonArray.length()];

		for (int i = 0; i < m_jsonArray.length(); i++) {
			results[i] = m_jsonArray.getInt(i);
		}

		return results;
	}

	/** parse Custom Object **/
	public <T extends Object> T parse(String jsonString, Class c) {
		JSONObject json = new JSONObject(jsonString);
		T obj = null;

		try {
			// get constructor
			Constructor<?> constructor = c.getConstructor();
			// get fields
			Field[] fields = c.getFields();
			// make new object
			obj = (T) constructor.newInstance();

			for (Field field : fields) {
				if (isJSONUnit(field.getType())) {
					field.set(obj, json.get(field.getName()));
				} else if (field.getType() == java.util.Date.class) {
					field.set(obj, stringToDate(json.getString(field.getName())));
				} else if (field.getType().isArray()) {
					field.set(obj, parseArray(json.optString(field.getName()), field.getType().getComponentType()));
				} else if (field.getType() == ArrayList.class) {
					if (field.getAnnotation(Member.class) != null) {
						Member member = field.getAnnotation(Member.class);
						field.set(obj, parseArrayList(jsonString, field.getName(), member.value()));
					} else {
						field.set(obj, new ArrayList<>());
					}
				} else {
					field.set(obj, parse(json.optString(field.getName()), field.getType()));
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
	public Object parseArray(String jsonString, Class c) {
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
	public <T extends Object> ArrayList<T> parseArrayList(String jsonString, String key, Class c) {
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
	
	private boolean isJSONUnit(Class c) {
		return c.isPrimitive() || c == Float.class || c == Integer.class || c == String.class
				 || c == Double.class || c == Boolean.class || c == Character.class || c == Byte.class
				 || c == Short.class || c == Long.class;
	}
	
	private java.util.Date stringToDate(String dateStr) {
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
