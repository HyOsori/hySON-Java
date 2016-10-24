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

	public String[] getArrayString(String jsonString)
	{
		String[] results = new String[m_jsonArray.length()];
		
		for(int i = 0; i < m_jsonArray.length(); ++i)
		{
			results[i] = m_jsonArray.getString(i);
		}
		
		return results;
	}
	
	public void parse(String jsonString)
	{
		m_jsonArray = new JSONArray(jsonString);
	}

}
