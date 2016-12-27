package com.osori.hyson;

import static org.junit.Assert.*;

import org.junit.Test;

public class HySONTest {

	/** String Array Parsing test **/
	
	@Test
	public void tesParseEmptyStringArray() {
		String jsonString = "[]";
		HySON.parse(jsonString);
		
		String[] results = HySON.getArrayString(jsonString);
		assertEquals(0, results.length);
	}
	
	@Test
	public void tesParseStringArray() {
		String jsonString = "[\"a\", \"b\", \"c\"]}";
		HySON.parse(jsonString);
		
		String[] results = null;

		results = HySON.getArrayString(jsonString);
		
		assertEquals(3, results.length);
		assertEquals("a", results[0]);
		assertEquals("b", results[1]);
		assertEquals("c", results[2]);
	}
	

	@Test
	public void testParseWithConstructor() {
		String jsonString = "[\"a\", \"b\", \"c\"]}";
		
		String[] results = null;

		results = HySON.getArrayString(jsonString);
		
		assertEquals(3, results.length);
		assertEquals("a", results[0]);
		assertEquals("b", results[1]);
		assertEquals("c", results[2]);
	}

	/** Boolean Array Parsing test **/
	
	@Test
	public void tesParseEmptBooleanArray() {
		String jsonString = "[]";
		HySON.parse(jsonString);
		
		Boolean[] results = HySON.getArrayBoolean(jsonString);
		assertEquals(0, results.length);
	}
	
	@Test
	public void tesParseBooleanArray() {
		String jsonString = "[\"true\", \"true\", \"false\"]}";
		HySON.parse(jsonString);
		
		Boolean[] results = null;

		results = HySON.getArrayBoolean(jsonString);
		
		assertEquals(3, results.length);
		assertTrue(results[0]);
		assertTrue(results[1]);
		assertFalse(results[2]);
	}
	
	@Test
	public void testParseBooleanWithConstructor() {
		String jsonString = "[\"true\", \"true\", \"false\"]}";

		Boolean[] results = null;

		results = HySON.getArrayBoolean(jsonString);
		
		assertEquals(3, results.length);
		assertTrue(results[0]);
		assertTrue(results[1]);
		assertFalse(results[2]);
	}
	
	/** integer Array Parsing Test **/
	@Test
	public void testParseEmptIntArray() {
		String jsonString = "[]";
		HySON.parse(jsonString);
		
		int[] results = HySON.getArrayInt(jsonString);
		assertEquals(0, results.length);
	}
		
	@Test
	public void testParseIntArray() {
		String jsonString = "[\"1\", \"2\", \"3\"]}";
		HySON.parse(jsonString);
		
		int[] results = null;

		results = HySON.getArrayInt(jsonString);
		
		assertEquals(3, results.length);
		assertEquals(1, results[0]);
		assertEquals(2, results[1]);
		assertEquals(3, results[2]);
	}
	
	@Test
	public void testParseIntWithConstructor() {
		String jsonString = "[\"10\", \"20\", \"30\"]}";
		
		int[] results = null;

		results = HySON.getArrayInt(jsonString);
		
		assertEquals(3, results.length);
		assertEquals(10, results[0]);
		assertEquals(20, results[1]);
		assertEquals(30, results[2]);
	}
	
	@Test
	public void testParseObject() {
		String jsonString = "{ \"value\" : 10 } ";
		Temp1 t = HySON.parse(jsonString, Temp1.class);
		
		assertNotNull(t);
		assertEquals(t.value, 10);
	}
	
	@Test
	public void testParseObjectWithKey() {
		String jsonString = "{\"object\": { \"value\" : 10 }} ";
		Temp1 t = HySON.parse(jsonString, "object", Temp1.class);
		
		assertNotNull(t);
		assertEquals(t.value, 10);
	}
	
	@Test
	public void testParseObjectInObject() {
		String jsonString = "{\"temp\": { \"value\" : 10 }} ";
		Temp2 t = HySON.parse(jsonString, Temp2.class);
		
		assertNotNull(t);
		assertNotNull(t.temp);
		assertEquals(t.temp.value, 10);
	}
	
	@Test
	public void testParseObjectArray() {
		String jsonString = "[{ \"value\" : 10 }]";
		Temp1[] t = (Temp1[])HySON.parseArray(jsonString, Temp1.class);
		
		assertNotNull(t);
		assertEquals(t.length, 1);
		assertNotNull(t[0]);
		assertEquals(t[0].value, 10);
	}
	
	@Test
	public void testParseArrayListInObject() {
		String jsonString = "{\"temps\": [{ \"value\" : 10 }]}";
		Temp3 t = HySON.parse(jsonString, Temp3.class);
		
		assertNotNull(t);
		assertNotNull(t.temps);
		assertEquals(t.temps.length, 1);
		assertNotNull(t.temps[0]);
		assertEquals(t.temps[0].value, 10);
	}
	
	@Test
	public void testParseMultiArrayListInObject() {
		String jsonString = "{members: [{\"temps\": [{ \"value\" : 0 }]}, {\"temps\": [{ \"value\" : 1 }]}]}";
		Temp4 t = HySON.parse(jsonString, Temp4.class);
		
		assertNotNull(t);
		assertNotNull(t.members);
		assertEquals(t.members.size(), 2);
		assertNotNull(t.members.get(0));
		assertNotNull(t.members.get(0).temps);
		assertNotNull(t.members.get(1));
		assertNotNull(t.members.get(1).temps);
		assertEquals(t.members.get(0).temps.length, 1);
		assertEquals(t.members.get(1).temps.length, 1);
		assertNotNull(t.members.get(0).temps[0]);
		assertNotNull(t.members.get(1).temps[0]);
		assertEquals(t.members.get(0).temps[0].value, 0);
		assertEquals(t.members.get(1).temps[0].value, 1);
	}
	
	@Test
	public void testParseMultMemberInObject() {
		String jsonString = "{\"a\": 1, \"b\" : \"횟횟\", \"c\" : [2, 3, 4], \"d\": [5, 6], \"temps\":[{\"value\": 0}], \"date\": \"2016-11-27 00:00:00\"}";
		ManyMembers members = HySON.parse(jsonString, ManyMembers.class);
		
		assertNotNull(members);
		assertNotNull(members.c);
		assertNotNull(members.d);
		assertNotNull(members.temps);
		assertEquals(members.a.intValue(), 1);
		assertEquals(members.b, "횟횟");
		assertEquals(members.c.size(), 3);
		assertEquals(members.c.get(0).intValue(), 2);
		assertEquals(members.c.get(1).intValue(), 3);
		assertEquals(members.c.get(2).intValue(), 4);
		assertEquals(members.d.length, 2);
		assertEquals(members.d[0].intValue(), 5);
		assertEquals(members.d[1].intValue(), 6);
		assertEquals(members.temps.size(), 1);
		assertNotNull(members.temps.get(0));
		assertEquals(members.temps.get(0).value, 0);
		System.out.println(members.date.getYear());
	}
}
