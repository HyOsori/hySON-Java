package com.osori.hyson;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HySON {
	private JSONArray m_jsonArray = null;
	
	public HySON()
	{
	}
	
	/** set m_jsonArray without constructor **/
	public void parse(String jsonString)
	{
		m_jsonArray = new JSONArray(jsonString);
	}

	/** parse String Array **/
	public String[] getArrayString(String jsonString)
	{
		parse(jsonString);
		String[] results = new String[m_jsonArray.length()];
		
		for(int i = 0; i < m_jsonArray.length(); ++i)
		{
			results[i] = m_jsonArray.getString(i);
		}
		
		return results;
	}

	/** parse Boolean Array **/
	public Boolean[] getArrayBoolean(String jsonString)
	{
		parse(jsonString);
		Boolean[] results = new Boolean[m_jsonArray.length()];
		
		for(int i = 0; i < m_jsonArray.length(); ++i)
		{
			results[i] = m_jsonArray.getBoolean(i);
		}
		
		return results;
	}
	
	/** parse Integer Array **/
	public int[] getArrayInt(String jsonString) {
		parse(jsonString);
		int[] results = new int[m_jsonArray.length()];
		
		for(int i=0; i<m_jsonArray.length(); i++)
		{
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
			obj = (T)constructor.newInstance();
			
			for (Field field : fields) {
				if (field.getType().isPrimitive() || field.getType() == String.class) {
					field.set(obj, json.get(field.getName()));
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
					field.set(obj, parse(jsonString, field.getName(), field.getType()));
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
}
