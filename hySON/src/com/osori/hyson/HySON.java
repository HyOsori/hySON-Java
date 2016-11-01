package com.osori.hyson;
import org.json.*;

public class HySON {
	
	private JSONArray m_jsonArray = null;
	
	public HySON()
	{
	}
	
	public HySON(String jsonString)
	{
		m_jsonArray = new JSONArray(jsonString);
	}
	
	/** set m_jsonArray without constructor **/
	public void parse(String jsonString)
	{
		m_jsonArray = new JSONArray(jsonString);
	}

	/** parse String Array **/
	public String[] getArrayString(String jsonString)
	{
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
		Boolean[] results = new Boolean[m_jsonArray.length()];
		
		for(int i = 0; i < m_jsonArray.length(); ++i)
		{
			results[i] = m_jsonArray.getBoolean(i);
		}
		
		return results;
	}
}
